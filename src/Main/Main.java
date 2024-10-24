package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    private static JFrame window;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }

    private static void createWindow() {
        window = new JFrame("CheckersGame");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);

        //setting icon;
        try {
            window.setIconImage(ImageIO.read(Main.class.getResource("/checkers_icon.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //adding game board to the frame
        window.add(GameBoard.getInstance());

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