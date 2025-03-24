package com.anwang.ui;

import com.anwang.types.ContractModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class App extends JFrame implements PropertyChangeListener {

    public static App app;

    public App() {
        setTitle("多签工具 v1.0.1 ———— " + ContractModel.getInstance().getChainType());
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 主布局：左侧菜单栏 + 右侧内容
        Content content = new Content();
        Menu menu = new Menu(content);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(100);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);
        splitPane.setLeftComponent(menu);
        splitPane.setRightComponent(content);
        add(splitPane, BorderLayout.CENTER);

        ContractModel.getInstance().addListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("chainId".equals(evt.getPropertyName())) {
            setTitle("多签工具 v1.0.1 ———— " + ContractModel.getInstance().getChainType());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StartupPage startupPage = new StartupPage();
            startupPage.show();
            if (startupPage.closeType == 1) {
                app = new App();
                app.setVisible(true);
                JOptionPane.showMessageDialog(null, "当前工具将在 " + ContractModel.getInstance().getChainType() + " 中使用，如需更换请在设置中修改");
            }
        });
    }
}
