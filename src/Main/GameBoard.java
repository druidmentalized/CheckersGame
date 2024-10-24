package Main;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel{

    //SINGLETON
    private static GameBoard instance;

    //MAP VARIABLES
    Graphics2D g2d;
    private final int tileSize = 96; //in pixels
    private final int boardSize = 8; //in squares
    private final int boardPixelSize = tileSize * boardSize;

    private GameBoard() {
        this.setPreferredSize(new Dimension(boardPixelSize, boardPixelSize));
        repaint();
    }

    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        setTiles(g2d);
        setChips(g2d);
    }

    private void setTiles(Graphics2D g2d) {
        //filling the map with tiles
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {

                //adding tiles to map
                if ((row % 2 == 0 && column % 2 == 0) || (row % 2 == 1 && column % 2 == 1)) {
                    g2d.setColor(new Color(255,252,249));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }
                else {
                    g2d.setColor(new Color(180,163,137));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }
            }
        }
    }


    //TODO:get rid of this method when using JNI
    private void setChips(Graphics2D g2d) {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                    if (row < 3) {
                        drawChip(g2d, column * tileSize, row * tileSize, false);
                    }
                    else if (row > 4) {
                        drawChip(g2d, column * tileSize, row * tileSize, true);
                    }
                }
            }
        }
    }

    private void drawChip(Graphics2D g2d, int x, int y, boolean isWhite) {
        g2d.setColor(isWhite ? Color.WHITE : Color.BLACK);
        g2d.fillOval(x + 7, y + 7, tileSize - 14, tileSize - 14);
    }

    //GETTERS & SETTERS
    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }
}
