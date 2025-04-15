package com.anwang.ui;

import com.anwang.types.ContractModel;
import com.anwang.ui.components.CustomPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SettingPage extends JPanel {
    public SettingPage() {
        JLabel netTypeLabel = new JLabel("网络类型：");
        JComboBox<String> netTypeComboBox = new JComboBox<>();
        netTypeComboBox.addItem("测试网");
        netTypeComboBox.addItem("主网");
        netTypeComboBox.setSelectedItem(ContractModel.getInstance().getChainType());

        JLabel rpcLabel = new JLabel("Http Endpoint：");
        JTextField rpcFiled = new JTextField(30);
        rpcFiled.setText(ContractModel.getInstance().getUrl());

        JButton confirmButton = new JButton("更新");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long chainId = netTypeComboBox.getSelectedIndex() == 0 ? 6666666 : 6666665;
                String url = rpcFiled.getText();
                if (url == null || url.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(App.app, "请输入相关信息");
                    return;
                }
                String ret = ContractModel.getInstance().update(chainId, url);
                if (ret == null) {
                    JOptionPane.showMessageDialog(App.app, "当前已切换到 " + ContractModel.getInstance().getChainType() + "中");
                } else {
                    JOptionPane.showMessageDialog(App.app, ret);
                }
            }
        });

        CustomPanel panel = new CustomPanel();
        List<JComponent> components = new ArrayList<>();
        components.add(netTypeLabel);
        components.add(netTypeComboBox);
        panel.addRow(components, 0);

        components.clear();
        components.add(rpcLabel);
        components.add(rpcFiled);
        panel.addRow(components, 1);

        CustomPanel panel1 = new CustomPanel();
        components.clear();
        components.add(confirmButton);
        panel1.addRow(components, 0);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 设置边距

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        add(panel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        add(panel1, gbc);
    }
}