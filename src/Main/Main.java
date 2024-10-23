package Main;

import javax.swing.*;
import java.awt.*;

public class Main {

    private static JFrame window;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }

    private static void createWindow() {
        window = new JFrame("CheckersGame");
        window.setResizable(false);
        window.getContentPane().setLayout(new CardLayout());

        //TODO: add different cards for menu and actual playable zone

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }


    private static void changeWindow(String changedPanel) {
        CardLayout cardLayout = (CardLayout) window.getContentPane().getLayout();
        cardLayout.show(window.getContentPane(), changedPanel);

        window.revalidate();
        window.repaint();
        window.pack();
    }
}