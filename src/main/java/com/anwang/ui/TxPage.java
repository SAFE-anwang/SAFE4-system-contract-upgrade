package com.anwang.ui;

import com.anwang.contracts.MultiSig;
import com.anwang.types.ContractModel;
import com.anwang.types.MultiSigTx;
import com.anwang.types.TxDataModel;
import com.anwang.ui.components.ConfirmDialog;
import com.anwang.ui.components.ExecuteDialog;
import com.anwang.ui.components.RevokeDialog;
import com.anwang.ui.components.TxDataInfoDialog;
import com.anwang.utils.CommonUtil;
import com.anwang.utils.TimeUtil;
import io.reactivex.disposables.Disposable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.utils.Numeric;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.util.List;
import java.util.*;

public class TxPage extends JPanel implements PropertyChangeListener {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField filterField;
    private final JLabel pageInfoLabel;
    private int currentPage = 1;
    private final int pageSize = 5;
    private final List<TxDataModel> data = new ArrayList<>();
    private final Map<BigInteger, Integer> id2pos = new HashMap<>();
    private final List<TxDataModel> filteredData = new ArrayList<>();
    private int required;
    private Disposable logEventSubscription;
    private final Timer timer;

    public TxPage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel filterPanel = new JPanel();
        filterField = new JTextField(20);
        JButton filterButton = new JButton("过滤");
        filterPanel.add(new JLabel("过滤条件:"));
        filterPanel.add(filterField);
        filterPanel.add(filterButton);
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        add(filterPanel);

        tableModel = new DefaultTableModel(new Object[]{"序号", "发起者", "目标合约", "金额", "功能", "计划执行时间", "执行者", "操作", "说明"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMinWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setMinWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(60);
        table.getColumnModel().getColumn(2).setMinWidth(130);
        table.getColumnModel().getColumn(2).setMaxWidth(130);
        table.getColumnModel().getColumn(3).setMinWidth(60);
        table.getColumnModel().getColumn(3).setMaxWidth(60);
        table.getColumnModel().getColumn(4).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(150);
        table.getColumnModel().getColumn(5).setMaxWidth(150);
        table.getColumnModel().getColumn(6).setMinWidth(60);
        table.getColumnModel().getColumn(6).setMaxWidth(60);
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumnModel().getColumn(8).setMinWidth(50);
        table.getColumnModel().getColumn(8).setMaxWidth(50);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        JPanel pagePanel = new JPanel();
        JButton prevPageButton = new JButton("上一页");
        pageInfoLabel = new JLabel();
        JButton nextPageButton = new JButton("下一页");
        pagePanel.add(prevPageButton);
        pagePanel.add(pageInfoLabel);
        pagePanel.add(nextPageButton);
        pagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        add(pagePanel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 8) {
                    TxDataInfoDialog.show((TxDataModel) table.getValueAt(row, 7));
                }
            }
        });

        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPage = 1;
                filterTable();
            }
        });

        prevPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    updateTable();
                }
            }
        });

        nextPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalPages;
                if (filteredData.isEmpty()) {
                    totalPages = (int) Math.ceil((double) data.size() / pageSize);
                } else {
                    totalPages = (int) Math.ceil((double) filteredData.size() / pageSize);
                }
                if (currentPage < totalPages) {
                    currentPage++;
                    updateTable();
                }
            }
        });

        try {
            required = ContractModel.getInstance().getMultiSig().getRequired().intValue();
        } catch (Exception e) {
            required = 3;
        }

        ContractModel.getInstance().addListener(this);
        subscribeLogEvent();
        Runtime.getRuntime().addShutdownHook(new Thread(this::unsubscribeLogEvent));

        timer = new Timer(3000, e -> {
            for (int i = 0; i < table.getRowCount(); i++) {
                TxDataModel txData = (TxDataModel) table.getValueAt(i, 7);
                if (TimeUtil.getCurTimestamp() >= txData.timestamp.longValue()) {
                    JPanel panel = (JPanel) table.prepareRenderer(table.getCellRenderer(i, 7), i, 7);
                    if (panel.getComponents().length > 1) {
                        if (!filteredData.isEmpty()) {
                            filterTable();
                        } else {
                            updateTable();
                        }
                        tableModel.fireTableDataChanged();
                        table.repaint();
                        break;
                    }
                }
            }
        });
        timer.start();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("chainId".equals(evt.getPropertyName())) {
            timer.stop();
            data.clear();
            id2pos.clear();
            filterField.setText("");
            filteredData.clear();
            currentPage = 1;
            updateTable();
            unsubscribeLogEvent();
            subscribeLogEvent();
            timer.start();
        }
    }

    private void subscribeLogEvent() {
        Event submissionEvent = new Event("Submission", Arrays.asList(new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }));
        Event confirmationEvent = new Event("Confirmation", Arrays.asList(new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }));
        Event revocationEvent = new Event("Revocation", Arrays.asList(new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }));
        Event executionEvent = new Event("Execution", Arrays.asList(new TypeReference<Address>() {
        }, new TypeReference<Uint256>() {
        }));
        Event RequirementChangeEvent = new Event("RequirementChange", Collections.singletonList(new TypeReference<Uint256>() {
        }));
        EthFilter eventFilter = new EthFilter(DefaultBlockParameter.valueOf(BigInteger.valueOf(187370)), DefaultBlockParameter.valueOf("latest"), MultiSig.contractAddr.getValue()).addOptionalTopics(
                EventEncoder.encode(submissionEvent),
                EventEncoder.encode(confirmationEvent),
                EventEncoder.encode(revocationEvent),
                EventEncoder.encode(executionEvent),
                EventEncoder.encode(RequirementChangeEvent));
        logEventSubscription = ContractModel.getInstance().getWeb3j().ethLogFlowable(eventFilter)
                .subscribe(log -> {
                    switch (CommonUtil.getEventName(log.getTopics().get(0))) {
                        case "Confirmation":
                            handleConfirmation(new Address(log.getTopics().get(1)), Numeric.toBigInt(log.getTopics().get(2)));
                            break;
                        case "Revocation":
                            handleRevocation(new Address(log.getTopics().get(1)), Numeric.toBigInt(log.getTopics().get(2)));
                            break;
                        case "Submission":
                            handleSubmission(Numeric.toBigInt(log.getTopics().get(1)), log.getTransactionHash());
                            break;
                        case "Execution":
                            handleExecution(new Address(log.getTopics().get(1)), Numeric.toBigInt(log.getTopics().get(2)), log.getTransactionHash());
                            break;
                        case "RequirementChange":
                            handleRequirementChange(Numeric.toBigInt(log.getTopics().get(1)));
                        default:
                            break;
                    }
                });
    }

    private void unsubscribeLogEvent() {
        if (!logEventSubscription.isDisposed()) {
            logEventSubscription.dispose();
        }
    }

    private void handleConfirmation(Address owner, BigInteger txid) {
        if (!id2pos.containsKey(txid)) {
            return;
        }
        Integer pos = id2pos.get(txid);
        TxDataModel txData = data.get(pos);
        if (txData.confirmations.contains(owner)) {
            return;
        }
        txData.confirmations.add(owner);
        txData.confirmCount = txData.confirmCount.add(BigInteger.ONE);
        txData.isConfirmed = txData.confirmCount.intValue() >= required;
        if (!filteredData.isEmpty()) {
            filterTable();
        } else {
            updateTable();
        }
        tableModel.fireTableDataChanged();
    }

    private void handleRevocation(Address owner, BigInteger txid) {
        if (!id2pos.containsKey(txid)) {
            return;
        }
        Integer pos = id2pos.get(txid);
        TxDataModel txData = data.get(pos);
        if (!txData.confirmations.contains(owner)) {
            return;
        }
        txData.confirmations.remove(owner);
        txData.confirmCount = txData.confirmCount.subtract(BigInteger.ONE);
        txData.isConfirmed = txData.confirmCount.intValue() >= required;
        if (!filteredData.isEmpty()) {
            filterTable();
        } else {
            updateTable();
        }
        tableModel.fireTableDataChanged();
    }

    private void handleSubmission(BigInteger txid, String submitTxid) throws Exception {
        if (id2pos.containsKey(txid)) {
            return;
        }
        MultiSigTx tx = ContractModel.getInstance().getMultiSig().getTransaction(txid);
        boolean isConfirmed = 1 >= required;
        BigInteger confirmCount = BigInteger.ONE;
        List<Address> confirmations = new ArrayList<>();
        confirmations = ContractModel.getInstance().getMultiSig().getConfirmations(txid);
        addNewData(new TxDataModel(txid, tx, submitTxid, isConfirmed, confirmCount, confirmations));
        if (!filteredData.isEmpty()) {
            filterTable();
        } else {
            updateTable();
        }
        tableModel.fireTableDataChanged();
    }

    private void handleExecution(Address owner, BigInteger txid, String executeTxid) {
        if (!id2pos.containsKey(txid)) {
            return;
        }
        Integer pos = id2pos.get(txid);
        TxDataModel txData = data.get(pos);
        txData.executed = true;
        txData.executor = owner;
        txData.executeTxid = executeTxid;
        if (!filteredData.isEmpty()) {
            filterTable();
        } else {
            updateTable();
        }
        tableModel.fireTableDataChanged();
    }

    private void handleRequirementChange(BigInteger required) {
        this.required = required.intValue();
    }

    private void addNewData(TxDataModel txData) {
        data.add(txData);
        data.sort((a, b) -> (int) (b.txid.longValue() - a.txid.longValue()));
        id2pos.clear();
        for (int i = 0; i < data.size(); i++) {
            id2pos.put(data.get(i).txid, i);
        }
    }

    private void filterTable() {
        String filterText = filterField.getText().trim();
        if (filterText.isEmpty()) {
            filteredData.clear();
            updateTable();
            return;
        }

        int filterType; // 过滤txid
        int filterValue = -1;
        try {
            filterValue = Integer.parseInt(filterText);
            filterType = 0;
        } catch (NumberFormatException e) {
            filterType = 1;
        }

        filteredData.clear();
        for (TxDataModel row : data) {
            if (filterType == 0) {
                if (row.txid.longValue() == filterValue) {
                    filteredData.add(row);
                }
            } else {
                if (row.from.getValue().contains(filterText) ||
                        row.fromName.contains(filterText) ||
                        row.to.getValue().contains(filterText) ||
                        row.toName.contains(filterText) ||
                        row.function.contains(filterText) ||
                        row.executeDate.contains(filterText) ||
                        row.executor.getValue().contains(filterText) ||
                        row.executorName.contains(filterText)) {
                    filteredData.add(row);
                }
            }
        }
        updateTable(filteredData);
    }

    private void updateTable() {
        if (filteredData.isEmpty()) {
            updateTable(data);
        } else {
            updateTable(filteredData);
        }
    }

    private void updateTable(List<TxDataModel> data) {
        tableModel.setRowCount(0);
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, data.size());
        for (int i = start; i < end; i++) {
            TxDataModel txData = data.get(i);
            Object[] temp = new Object[9];
            temp[0] = txData.txid;
            temp[1] = txData.fromName;
            temp[2] = txData.toName;
            temp[3] = txData.value;
            temp[4] = txData.function;
            temp[5] = txData.executeDate;
            temp[6] = txData.executorName;
            temp[7] = txData;
            temp[8] = "详情 >>";
            tableModel.addRow(temp);
        }
        int totalPages = (int) Math.ceil((double) data.size() / pageSize);
        pageInfoLabel.setText("第 " + currentPage + " 页 / 共 " + totalPages + " 页");
    }

    private static class ButtonRenderer extends JPanel implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            TxDataModel txData = (TxDataModel) table.getValueAt(row, column);
            boolean isConfirmed;
            try {
                isConfirmed = ContractModel.getInstance().getMultiSig().isConfirmed(txData.txid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 确认期
            if (TimeUtil.getCurTimestamp() < txData.timestamp.longValue()) {
                add(new JButton("确认"));
                add(new JButton("撤销"));
            } else { // 执行期或失效期
                if (txData.executed) { // 已执行
                    add(new JLabel("已执行"));
                } else if (!isConfirmed) { // 确认数不足，已失效
                    add(new JLabel("已过期"));
                } else { // 等待执行
                    add(new JButton("执行"));
                }
            }
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private Object value;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            panel.setOpaque(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            this.value = value;

            TxDataModel txData = (TxDataModel) table.getValueAt(row, column);
            boolean isConfirmed;
            try {
                isConfirmed = ContractModel.getInstance().getMultiSig().isConfirmed(txData.txid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 确认期
            if (TimeUtil.getCurTimestamp() < txData.timestamp.longValue()) {
                JButton confirmButton = new JButton("确认");
                confirmButton.setOpaque(false);
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ConfirmDialog.show(txData.txid);
                    }
                });
                panel.add(confirmButton);
                JButton revokeButton = new JButton("撤销");
                revokeButton.setOpaque(false);
                revokeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        RevokeDialog.show(txData.txid);
                    }
                });
                panel.add(revokeButton);
            } else { // 执行期或失效期
                if (txData.executed) { // 已执行
                    panel.add(new JLabel("已执行"));
                } else if (!isConfirmed) { // 确认数不足，已失效
                    panel.add(new JLabel("已过期"));
                } else { // 等待执行
                    JButton executeButton = new JButton("执行");
                    executeButton.setOpaque(false);
                    executeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ExecuteDialog.show(txData.txid);
                        }
                    });
                    panel.add(executeButton);
                }
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return this.value;
        }
    }
}