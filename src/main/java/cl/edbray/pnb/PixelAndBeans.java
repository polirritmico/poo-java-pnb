package cl.edbray.pnb;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import cl.edbray.pnb.gui.LoginFrame;

public class PixelAndBeans {

    public static void main(String[] args) {
        FlatLaf.registerCustomDefaultsSource("themes");
        FlatDarkLaf.setup();

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
