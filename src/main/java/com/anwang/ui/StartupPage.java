package com.anwang.ui;

import com.anwang.types.ContractModel;
import com.anwang.ui.components.CustomPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class StartupPage {
    public JDialog dialog;
    public int closeType;

    public StartupPage() {
        dialog = new JDialog((JFrame) null, "选择网络", true);
        dialog.setLayout(new BorderLayout());
        dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        closeType = 0;
    }

    public void show() {
        JLabel netTypeLabel = new JLabel("网络类型：");
        JComboBox<String> netTypeComboBox = new JComboBox<>();
        netTypeComboBox.addItem("测试网");
        netTypeComboBox.addItem("主网");

        JLabel rpcLabel = new JLabel("Http Endpoint：");
        JTextField rpcFiled = new JTextField(30);
        rpcFiled.setText("http://139.162.40.90:8545");

        netTypeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (netTypeComboBox.getSelectedIndex()) {
                        case 0:
                            rpcFiled.setText("http://139.162.40.90:8545");
                            break;
                        case 1:
                            rpcFiled.setText("http://127.0.0.1:8545");
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        CustomPanel panel = new CustomPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        List<JComponent> components = new ArrayList<>();
        components.add(netTypeLabel);
        components.add(netTypeComboBox);
        panel.addRow(components, 0);

        components.clear();
        components.add(rpcLabel);
        components.add(rpcFiled);
        panel.addRow(components, 1);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        JButton okButton = new JButton("确定");
        JButton exitButton = new JButton("退出");
        buttonPanel.add(okButton);
        buttonPanel.add(exitButton);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long chainId = netTypeComboBox.getSelectedIndex() == 0 ? 6666666 : 6666665;
                String url = rpcFiled.getText();
                if (url == null || url.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "请输入相关信息");
                    return;
                }
                String ret = ContractModel.getInstance().init(chainId, url.trim());
                if (ret == null) {
                    closeType = 1;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, ret);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
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