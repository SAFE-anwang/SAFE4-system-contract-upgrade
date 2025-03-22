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

public class RemoveOwnerDialog {
    public static void show() {
        JDialog dialog = new JDialog(App.app, "移除管理员", true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel ownerLabel = new JLabel("管理员地址：");
        JTextField ownerField = new JTextField(30);
        ((AbstractDocument) ownerField.getDocument()).setDocumentFilter(new HexnumericFilter());

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
        components.add(ownerLabel);
        components.add(ownerField);
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
                String owner = ownerField.getText();
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();
                if (key == null || key.trim().isEmpty() ||
                        owner == null || owner.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
                try {
                    String ret = ContractModel.getInstance().getMultiSig().removeOwner(key.trim(), owner.trim(), TimeUtil.toTimestamp(dateTime));
                    JOptionPane.showMessageDialog(null, "提交'移除管理员'成功，返回交易：" + ret);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "提交'移除管理员'失败，" + ex.getMessage());
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
