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

public class ChangeIsOfficialDialog {
    public static void show(String contractName) {
        String nodeType;
        if (contractName.equals("MasterNodeLogic")) {
            nodeType = "主节点";
        } else if (contractName.equals("SuperNodeLogic")) {
            nodeType = "超级节点";
        } else {
            return;
        }

        JDialog dialog = new JDialog(App.app, "配置官方" + nodeType, true);
        dialog.setLayout(new BorderLayout());

        JLabel keyLabel = new JLabel("私钥：");
        JTextField keyFiled = new JTextField(30);
        ((AbstractDocument) keyFiled.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel addrLabel = new JLabel(nodeType + "地址：");
        JTextField addrField = new JTextField(30);
        ((AbstractDocument) addrField.getDocument()).setDocumentFilter(new HexnumericFilter());

        JLabel flagLabel = new JLabel("官方属性：");
        JComboBox<String> flagComboBox = new JComboBox<>();
        flagComboBox.addItem("增加");
        flagComboBox.addItem("移除");

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
        components.add(addrLabel);
        components.add(addrField);
        panel.addRow(components, 1);

        components.clear();
        components.add(flagLabel);
        components.add(flagComboBox);
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
                String addr = addrField.getText();
                boolean flag = flagComboBox.getSelectedIndex() == 0;
                LocalDateTime dateTime = dateTimePicker.getDateTimePermissive();

                if (key == null || key.trim().isEmpty() ||
                        addr == null || addr.trim().isEmpty() ||
                        dateTime == null) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
                try {
                    String ret;
                    if (nodeType.equals("主节点")) {
                        ret = ContractModel.getInstance().getMultiSig().changeIsOfficial4MN(key, addr, flag, TimeUtil.toTimestamp(dateTime));
                    } else {
                        ret = ContractModel.getInstance().getMultiSig().changeIsOfficial4SN(key, addr, flag, TimeUtil.toTimestamp(dateTime));
                    }
                    JOptionPane.showMessageDialog(null, "提交'配置官方" + nodeType + "'成功，返回交易：" + ret);
                    dialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "提交'配置官方" + nodeType + "'失败，" + ex.getMessage());
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
