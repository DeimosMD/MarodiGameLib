package control;

import javax.swing.*;

public class Screen extends JFrame {

    Screen(RuntimeSettings runtimeSettings) {
        super(runtimeSettings.getWindowTitle());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(runtimeSettings.getScreenWidth(), runtimeSettings.getScreenHeight());
        this.setResizable(isResizable());
    }
}
