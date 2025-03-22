package com.anwang.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends JPanel {
    public JList<String> menuList;

    public Menu(Content content) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        String[] items = {"升级", "交易", "设置"};
        menuList = new JList<>(items);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        menuList.setSelectedIndex(0);
        menuList.setFixedCellHeight(30);
        menuList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedIndex = menuList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectItem = menuList.getModel().getElementAt(selectedIndex);
                    content.layout.show(content, selectItem);
                }
            }
        });
        add(new JScrollPane(menuList), BorderLayout.CENTER);
    }
}
