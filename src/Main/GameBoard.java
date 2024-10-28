package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameBoard extends JPanel{

    //SINGLETON
    private static GameBoard instance;

    //MAP VARIABLES
    Graphics2D g2d;
    private final int tileSize = 96; //in pixels
    private final int boardSize = 8; //in squares
    private final int boardPixelSize = tileSize * boardSize;

    static {
        System.loadLibrary("libCheckersNative");
    }

    private GameBoard() {
        this.setPreferredSize(new Dimension(boardPixelSize, boardPixelSize));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = e.getX() / tileSize;
                int row = e.getY() / tileSize;

                handleClick(row, column);
                repaint();
            }
        });

        setupGame();
        repaint();
    }

    public void paintComponent(Graphics g) {
        g2d = (Graphics2D) g;
        drawBoard(g2d);
    }

    private void drawBoard(Graphics2D g2d) {
        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                //drawing tile on the map
                if ((row % 2 == 0 && column % 2 == 0) || (row % 2 == 1 && column % 2 == 1)) {
                    g2d.setColor(new Color(255,252,249));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }
                else {
                    g2d.setColor(new Color(180,163,137));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                int currentTileInformation = getTileInformation(row, column);
                int chipType = currentTileInformation % 10;
                currentTileInformation /= 10;
                int canChipTurn = currentTileInformation % 10;
                currentTileInformation /= 10;

                //drawing selected tile(if exists)
                if (currentTileInformation == 2) { //selected tile
                    g2d.setColor(new Color(113, 102, 86));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                if (canChipTurn == 2) {
                    g2d.setColor(Color.RED);
                    g2d.setStroke(new BasicStroke(5f));
                    g2d.drawRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                //drawing figure(if exists)
                switch (chipType) {
                    case 1 -> drawChip(g2d, column * tileSize, row * tileSize, true, false);
                    case 2 -> drawChip(g2d, column * tileSize, row * tileSize, true, true);
                    case 3 -> drawChip(g2d, column * tileSize, row * tileSize, false, false);
                    case 4 -> drawChip(g2d, column * tileSize, row * tileSize, false, true);
                }
            }
        }
    }

    private void drawChip(Graphics2D g2d, int x, int y, boolean isWhite, boolean isKing) {
        g2d.setColor(isWhite ? Color.WHITE : Color.BLACK);
        g2d.fillOval(x + 7, y + 7, tileSize - 14, tileSize - 14);
        if (isKing) {
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(x + 33, y + 33, tileSize - 66, tileSize - 66);
        }
    }

    //NATIVE METHODS
    private native void setupGame();
    private native void handleClick(int row, int column);
    private native int getTileInformation(int row, int column);

    //GETTERS & SETTERS
    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }
}
