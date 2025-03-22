package com.anwang.ui.components;

import com.anwang.types.ContractModel;
import com.anwang.ui.App;
import com.anwang.ui.components.filters.HexnumericFilter;
import com.anwang.ui.components.filters.PositiveIntegerFilter;
import com.anwang.utils.TimeUtil;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChangeRequirementDialog {
    public static void show() {
        JDialog dialog = new JDialog(App.app, "更改签名数", true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel requirementLabel = new JLabel("签名数：");
        JTextField requirementFiled = new JTextField(30);
        ((AbstractDocument) requirementFiled.getDocument()).setDocumentFilter(new PositiveIntegerFilter());

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
        components.add(requirementLabel);
        components.add(requirementFiled);
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
                String requirement = requirementFiled.getText();
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();
                if (key == null || key.trim().isEmpty() ||
                        requirement == null || requirement.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
                try {
                    String ret = ContractModel.getInstance().getMultiSig().changeRequirement(key.trim(), new BigInteger(requirement.trim()).longValue(), TimeUtil.toTimestamp(dateTime));
                    JOptionPane.showMessageDialog(null, "提交'更改签名数'成功，返回交易：" + ret);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "提交'更改签名数'失败，" + ex.getMessage());
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
