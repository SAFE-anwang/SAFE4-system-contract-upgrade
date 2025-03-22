package com.anwang.ui;

import com.anwang.contracts.MultiSig;
import com.anwang.types.ContractModel;
import com.anwang.ui.components.CustomPanel;
import org.web3j.abi.datatypes.Address;

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
        netTypeComboBox.addItem("主网");
        netTypeComboBox.addItem("测试网");
        netTypeComboBox.setSelectedIndex(1);

        JLabel rpcLabel = new JLabel("Http Endpoint：");
        JTextField rpcFiled = new JTextField(30);
        rpcFiled.setText("http://139.162.40.90:8545");

        JButton confirmButton = new JButton("更新");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MultiSig.contractAddr = netTypeComboBox.getSelectedIndex() == 0 ? new Address("0x0000000000000000000000000000000000001102") : new Address("0x62b31F0eCFF7e26786593118eC25DAD8C9BCB161");
                long chainId = netTypeComboBox.getSelectedIndex() == 0 ? 6666665 : 6666666;
                String url = rpcFiled.getText();
                if (url == null || url.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入相关信息");
                    return;
                }
                ContractModel.getInstance().update(chainId, url);
                JOptionPane.showMessageDialog(null, "当前已切换到 " + netTypeComboBox.getSelectedItem() + "中");
            }
        });

        JButton resetButton = new JButton("重置");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                netTypeComboBox.setSelectedItem(ContractModel.getInstance().getChainType());
                rpcFiled.setText(ContractModel.getInstance().getUrl());
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
        components.add(resetButton);
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