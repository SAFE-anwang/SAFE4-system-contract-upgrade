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

public class ReplaceOwnerDialog {
    public static void show() {
        JDialog dialog = new JDialog(App.app, "替换管理员", true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel oldOwnerLabel = new JLabel("旧管理员地址：");
        JTextField oldOwnerField = new JTextField(30);
        ((AbstractDocument) oldOwnerField.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel newOwnerLabel = new JLabel("新管理员地址：");
        JTextField newOwnerField = new JTextField(30);
        ((AbstractDocument) newOwnerField.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel dateTimeLabel = new JLabel("计划执行时间：");
        CustomDateTimePicker dateTimePicker = new CustomDateTimePicker();
        dateTimePicker.setDateTimePermissive(LocalDateTime.now());

        CustomPanel panel = new CustomPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        List<JComponent> components = new ArrayList<>();
        components.add(keyLabel);
        components.add(keyFiled);
        panel.addRow(components, 0);

        components.clear();
        components.add(oldOwnerLabel);
        components.add(oldOwnerField);
        panel.addRow(components, 1);

        components.clear();
        components.add(newOwnerLabel);
        components.add(newOwnerField);
        panel.addRow(components, 2);

        components.clear();
        components.add(dateTimeLabel);
        components.add(dateTimePicker);
        panel.addRow(components, 3);

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
                String oldOwner = oldOwnerField.getText();
                String newOwner = newOwnerField.getText();
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();
                if (key == null || key.trim().isEmpty() ||
                        oldOwner == null || oldOwner.trim().isEmpty() ||
                        newOwner == null || newOwner.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(dialog, "请输入相关信息");
                    return;
                }
                try {
                    String ret = ContractModel.getInstance().getMultiSig().replaceOwner(key.trim(), oldOwner.trim(), newOwner.trim(), TimeUtil.toTimestamp(dateTime));
                    JOptionPane.showMessageDialog(dialog, "提交'替换管理员'成功，返回交易：" + ret);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "提交'替换管理员'失败，" + ex.getMessage());
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
