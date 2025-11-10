package cl.edbray.pnb;

import cl.edbray.pnb.gui.LoginFrame;

public class PixelAndBeans {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
