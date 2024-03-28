package hunter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PrimaryWindow extends JFrame {

    public PrimaryWindow() {
        configureWindowAttributes();
        addWindowCloseHandler();
    }

    private void configureWindowAttributes() {
        setTitle("The CHASE");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void addWindowCloseHandler() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manageWindowClose();
            }
        });
    }

    private void manageWindowClose() {
        if (confirmExitFromUser()) {
            dispose();
        }
    }

    private boolean confirmExitFromUser() {
        return JOptionPane.showConfirmDialog(
                this,
                "Do you really want to exit?",
                "Verification",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION;
    }
}
