package com.anwang.ui.components;

import com.anwang.types.TxDataModel;
import com.anwang.utils.CommonUtil;
import com.anwang.utils.TimeUtil;
import org.web3j.abi.datatypes.Address;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TxDataInfoDialog {
    public static void show(TxDataModel txData) {
        CustomPanel panel = new CustomPanel();
        List<JComponent> components = new ArrayList<>();

        int index = 0;
        components.add(new JLabel("序号："));
        components.add(new JLabel(txData.txid.toString()));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("发起交易Hash："));
        components.add(new CopyPanel(txData.submitTxid));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("发起者："));
        components.add(new CopyPanel(txData.fromName + " (" + txData.from.getValue() + ")"));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("目标合约："));
        components.add(new CopyPanel(txData.toName + " (" + txData.to.getValue() + ")"));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("使用金额："));
        components.add(new JLabel(txData.value.toString()));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("功能："));
        components.add(new JLabel(txData.function));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("计划执行时间："));
        components.add(new JLabel(txData.executeDate));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("当前状态："));
        String status;
        if (TimeUtil.getCurTimestamp() < txData.timestamp.longValue()) {
            if (!txData.isConfirmed) {
                status = "待确认";
            } else {
                status = "待执行";
            }
        } else { // 执行期或失效期
            if (txData.executed) { // 已执行
                status = "已执行";
            } else if (!txData.isConfirmed) { // 确认数不足，已失效
                status = "已过期";
            } else {
                status = "待执行";
            }
        }
        components.add(new JLabel(status));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("确认数:"));
        components.add(new JLabel(String.valueOf(txData.confirmations.size())));
        panel.addRow(components, index++);

        for (int i = 0; i < txData.confirmations.size(); i++) {
            components.clear();
            if (i == 0) {
                components.add(new JLabel("确认者："));
            } else {
                components.add(new JLabel(""));
            }
            components.add(new CopyPanel(CommonUtil.getOpertor(txData.confirmations.get(i).getValue()) + " (" + txData.confirmations.get(i).getValue() + ")"));
            panel.addRow(components, index++);
        }

        components.clear();
        components.add(new JLabel("执行交易Hash："));
        components.add(new CopyPanel(txData.executeTxid));
        panel.addRow(components, index++);

        components.clear();
        components.add(new JLabel("执行者："));
        if (!Objects.equals(txData.executor, new Address(BigInteger.valueOf(0)))) {
            components.add(new CopyPanel(txData.executorName + " (" + txData.executor.getValue() + ")"));
        } else {
            components.add(new JLabel(txData.executorName));
        }
        panel.addRow(components, index++);

        JOptionPane.showConfirmDialog(
                null,
                panel,
                "升级",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
