package com.anwang.ui.components;

import com.anwang.types.ContractModel;
import com.anwang.ui.App;
import com.anwang.ui.components.filters.HexnumericFilter;
import com.anwang.utils.TimeUtil;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UpgradeDialog {
    public static void show(String contractName) {
        JDialog dialog = new JDialog(App.app, "合约升级", true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel bytecodeLabel = new JLabel("Bytecode：");
        JTextArea bytecodeArea = new JTextArea();
        bytecodeArea.setColumns(30);
        bytecodeArea.setRows(10);
        bytecodeArea.setLineWrap(true);
        bytecodeArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(bytecodeArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel dateTimeLabel = new JLabel("执行时间：");
        CustomDateTimePicker dateTimePicker = new CustomDateTimePicker();
        dateTimePicker.setDateTimePermissive(LocalDateTime.now());

        CustomPanel panel = new CustomPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        List<JComponent> components = new ArrayList<>();
        components.add(keyLabel);
        components.add(keyFiled);
        panel.addRow(components, 0);

        components.clear();
        components.add(bytecodeLabel);
        components.add(scrollPane);
        panel.addRow(components, 1);

        components.clear();
        components.add(dateTimeLabel);
        components.add(dateTimePicker);
        panel.addRow(components, 2);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String key = keyFiled.getText();
                String bytecode = bytecodeArea.getText();
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();
                if (key == null || key.trim().isEmpty() ||
                        bytecode == null || bytecode.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
//            try {
//                CommonUtil.compile(contractName);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(null, "升级/编译失败，" + ex.getMessage());
//                return;
//            }
//            try {
//                String ret = ContractModel.getInstance().getMultiSig().upgradeContract(key.trim(), contractName, TimeUtil.toTimestamp(dateTime));
//                JOptionPane.showMessageDialog(null, "提交'合约升级'成功，返回交易：" + ret);
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(null, "提交'合约升级'失败，" + ex.getMessage());
//            }
                try {
                    String ret = ContractModel.getInstance().getMultiSig().upgradeContract(key.trim(), contractName, bytecode.trim(), TimeUtil.toTimestamp(dateTime));
                    JOptionPane.showMessageDialog(null, "提交'合约升级'成功，返回交易：" + ret);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "提交'合约升级'失败，" + ex.getMessage());
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(App.app);
        dialog.setVisible(true);
    }
}
