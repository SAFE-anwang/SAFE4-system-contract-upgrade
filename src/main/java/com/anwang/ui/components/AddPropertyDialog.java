package com.anwang.ui.components;

import com.anwang.types.ContractModel;
import com.anwang.ui.App;
import com.anwang.ui.components.filters.AlphanumericFilter;
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

public class AddPropertyDialog {
    public static void show() {
        JDialog dialog = new JDialog(App.app, "新增系统参数", true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel nameLabel = new JLabel("属性名：");
        JTextField nameField = new JTextField(30);
        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new AlphanumericFilter());

        JLabel valueLabel = new JLabel("属性值：");
        JTextField valueField = new JTextField(30);
        ((AbstractDocument) valueField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());

        JLabel descriptionLabel = new JLabel("描述信息：");
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setColumns(30);
        descriptionArea.setRows(4);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
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
        components.add(nameLabel);
        components.add(nameField);
        panel.addRow(components, 1);

        components.clear();
        components.add(valueLabel);
        components.add(valueField);
        panel.addRow(components, 2);

        components.clear();
        components.add(descriptionLabel);
        components.add(scrollPane);
        panel.addRow(components, 3);

        components.clear();
        components.add(dateTimeLabel);
        components.add(dateTimePicker);
        panel.addRow(components, 4);

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
                String name = nameField.getText();
                String value = valueField.getText();
                String description = descriptionArea.getText();
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();

                if (key == null || key.trim().isEmpty() ||
                        name == null || name.trim().isEmpty() ||
                        value == null || value.trim().isEmpty() ||
                        description == null || description.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
                try {
                    String ret = ContractModel.getInstance().getMultiSig().addProperty(key.trim(), name.trim(), new BigInteger(value.trim()), description.trim(), TimeUtil.toTimestamp(dateTime));
                    JOptionPane.showMessageDialog(null, "提交'新增参数'成功，返回交易：" + ret);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "提交'新增参数'失败，" + ex.getMessage());
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
