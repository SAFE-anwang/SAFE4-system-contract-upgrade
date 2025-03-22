package com.anwang.ui;

import javax.swing.*;
import java.awt.*;

public class Content extends JPanel {
    public CardLayout layout;

    public Content() {
        layout = new CardLayout();
        setLayout(layout);

        UpgradePage upgradePage = new UpgradePage();
        TxPage txPage = new TxPage();
        SettingPage settingPage = new SettingPage();

        add(upgradePage, "升级");
        add(txPage, "交易");
        add(settingPage, "设置");
    }
}
