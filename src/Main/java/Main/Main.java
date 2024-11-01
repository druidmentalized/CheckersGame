package Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }

    private static void createWindow() {
        JFrame window = new JFrame("CheckersGame");
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
}