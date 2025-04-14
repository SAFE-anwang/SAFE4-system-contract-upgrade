package com.anwang.ui;

import com.anwang.ui.components.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpgradePage extends JPanel {
    public UpgradePage() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(createTable());
    }

    private JScrollPane createTable() {
        String[] columnName = {"合约名", "文件名", "操作方式"};
        Object[][] data = {
                {"系统参数合约", "Property", ""},
                {"账户管理合约", "AccountManager", ""},
                {"主节点存储合约", "MasterNodeStorage", ""},
                {"主节点逻辑合约", "MasterNodeLogic", ""},
                {"超级节点存储合约", "SuperNodeStorage", ""},
                {"超级节点逻辑合约", "SuperNodeLogic", ""},
                {"超级节点投票合约", "SNVote", ""},
                {"主节点状态合约", "MasterNodeState", ""},
                {"超级节点状态合约", "SuperNodeState", ""},
                {"提案合约", "Proposal", ""},
                {"系统奖励合约", "SystemReward", ""},
                {"Safe3迁移合约", "Safe3", ""},
                {"多签延时合约", "MultiSigWallet", ""},
                {"ProxyAdmin合约", "ProxyAdmin", ""}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnName) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setMinWidth(200);
        table.getColumnModel().getColumn(0).setMaxWidth(200);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));
        return new JScrollPane(table);
    }

    private static class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton upgradeButton;
        private final JButton addPropertyButton;
        private final JButton changeIsOfficialButton;
        private final JButton addOwnerButton;
        private final JButton removeOwnerButton;
        private final JButton replaceOwnerButton;
        private final JButton changeRequirementButton;
        private final JButton transferOwnershipButton;

        public ButtonRenderer() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            upgradeButton = new JButton("升级");
            addPropertyButton = new JButton("新增参数");
            changeIsOfficialButton = new JButton("配置官方节点");
            addOwnerButton = new JButton("新增管理员");
            removeOwnerButton = new JButton("移除管理员");
            replaceOwnerButton = new JButton("替换管理员");
            changeRequirementButton = new JButton("更改签名数");
            transferOwnershipButton = new JButton("更换合约所有者");
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();
            if (row == 12) {
                add(addOwnerButton);
                add(removeOwnerButton);
                add(replaceOwnerButton);
                add(changeRequirementButton);
            } else if (row == 13) {
                add(transferOwnershipButton);
            } else {
                add(upgradeButton);
                if (row == 0) {
                    add(addPropertyButton);
                }
                if (row == 3 || row == 5) {
                    add(changeIsOfficialButton);
                }
                add(transferOwnershipButton);
            }
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor {
        private final JButton upgradeButton;
        private final JButton addPropertyButton;
        private final JButton changeIsOfficialButton;
        private final JButton addOwnerButton;
        private final JButton removeOwnerButton;
        private final JButton replaceOwnerButton;
        private final JButton changeRequirementButton;
        private final JButton transferOwnershipButton;
        private final JPanel panel;
        private JTable table;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
            upgradeButton = new JButton("升级");
            addPropertyButton = new JButton("新增参数");
            changeIsOfficialButton = new JButton("配置官方节点");
            addOwnerButton = new JButton("新增管理员");
            removeOwnerButton = new JButton("移除管理员");
            replaceOwnerButton = new JButton("替换管理员");
            changeRequirementButton = new JButton("更改签名数");
            transferOwnershipButton = new JButton("更换合约所有者");

            upgradeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    UpgradeDialog.show(table.getValueAt(table.getSelectedRow(), 1).toString());
                }
            });

            addPropertyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddPropertyDialog.show();
                }
            });

            changeIsOfficialButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangeIsOfficialDialog.show(table.getValueAt(table.getSelectedRow(), 1).toString());
                }
            });

            addOwnerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddOwnerDialog.show();
                }
            });

            removeOwnerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RemoveOwnerDialog.show();
                }
            });

            replaceOwnerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ReplaceOwnerDialog.show();
                }
            });

            changeRequirementButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangeRequirementDialog.show();
                }
            });

            transferOwnershipButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    TransferOwnershipDialog.show(table.getValueAt(table.getSelectedRow(), 1).toString());
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();

            if (row != 12) {
                panel.add(upgradeButton);
                if (row == 0) {
                    panel.add(addPropertyButton);
                }
                if (row == 3 || row == 5) {
                    panel.add(changeIsOfficialButton);
                }
                panel.add(transferOwnershipButton);
            } else {
                panel.add(addOwnerButton);
                panel.add(removeOwnerButton);
                panel.add(replaceOwnerButton);
                panel.add(changeRequirementButton);
            }

            this.table = table;
            return panel;
        }
    }
}
