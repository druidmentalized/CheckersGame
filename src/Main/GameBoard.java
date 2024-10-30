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
        this.setLayout(null);

        //defining keyboard input
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("W"), "moveUp");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "moveDown");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "moveLeft");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "moveRight");

        //making actions in response to the input
/*        getActionMap().put("moveUp", new AbstractAction() {

        })*/


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
                    g2d.setColor(new Color(60,60,60));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }
                else {
                    g2d.setColor(new Color(30,30,30));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                int currentTileInformation = getTileInformation(row, column);

                if (currentTileInformation < 0) {
                    drawWinnerWindow(g2d, currentTileInformation);
                    return;
                }

                int chipType = currentTileInformation % 10;
                currentTileInformation /= 10;
                int canChipTurn = currentTileInformation % 10;
                currentTileInformation /= 10;

                if (canChipTurn == 2) {
                    g2d.setColor(new Color(200, 255, 200));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
                }

                //drawing selected tile(if exists)
                if (currentTileInformation == 2) { //selected tile
                    g2d.setColor(new Color(160, 120, 40));
                    g2d.fillRect(column * tileSize, row * tileSize, tileSize, tileSize);
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

    private void drawWinnerWindow(Graphics2D g2d, int winnerId) {
        //drawing background
        g2d.setColor(new Color(0, 0, 0, 160));
        int posX = (int)(tileSize * 1.5);
        int posY = (int)(tileSize * 1.25);
        g2d.fillRoundRect(posX, posY, boardPixelSize - posX * 2, boardPixelSize - posY * 2, 10, 10);

        //drawing frame
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(5f));
        g2d.drawRoundRect(posX, posY, boardPixelSize - posX * 2, boardPixelSize - posY * 2, 10, 10);

        //drawing inscription
        String wonString = (winnerId == -1 ? "White" : "Black") + " is winner!";
        g2d.setColor(Color.WHITE);
        g2d.setFont(g2d.getFont().deriveFont(50f));
        int stringWidth = g2d.getFontMetrics().stringWidth(wonString);
        int stringHeight = g2d.getFontMetrics().getHeight();
        int winWindowLength = boardPixelSize - posX * 2;
        int winWindowHeight = boardPixelSize - posY * 2;
        int stringPosX = (winWindowLength - stringWidth) / 2 + posX;
        int stringPosY = (winWindowHeight - stringHeight) / 2;
        g2d.drawString(wonString, stringPosX, stringPosY);

        //restart button
        JButton restartButton = makeButton("Restart");
        restartButton.addActionListener(e -> {
            this.removeAll();
            setupGame();
            repaint();
        });
        int buttonPosY = (winWindowHeight - stringHeight) / 2 + posY;
        restartButton.setBounds(new Rectangle(stringPosX, buttonPosY, stringWidth, stringHeight)); //String metrics is used to reduce amount of variables
        this.add(restartButton);

        //exit button
        JButton exitButton = makeButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        buttonPosY = (winWindowHeight - stringHeight) / 2 + posY * 2;
        exitButton.setBounds(new Rectangle(stringPosX, buttonPosY, stringWidth, stringHeight)); //String metrics used for the same reason
        this.add(exitButton);
    }

    private void drawChip(Graphics2D g2d, int x, int y, boolean isWhite, boolean isKing) {
        //Drawing shadow
        g2d.setColor(new Color(0, 0, 0, 80)); // Semi-transparent black for shadow
        g2d.fillOval(x + 10, y + 10, tileSize - 14, tileSize - 14);

        //Drawing outer outline
        g2d.setColor(isWhite ? new Color(180, 150, 110) : new Color(15, 15, 15));
        g2d.fillOval(x + 5, y + 5, tileSize - 10, tileSize - 10);

        //Drawing inner gradient for 3D effect
        Color baseColor = isWhite ? new Color(210, 180, 140) : new Color(20, 20, 20);
        Color highlightColor = isWhite ? new Color(240, 220, 190) : new Color(50, 50, 50);

        RadialGradientPaint gradient = new RadialGradientPaint(
                new Point(x + tileSize / 2, y + tileSize / 2),
                tileSize / 2f,
                new float[]{0f, 1f},
                new Color[]{highlightColor, baseColor}
        );
        g2d.setPaint(gradient);
        g2d.fillOval(x + 7, y + 7, tileSize - 14, tileSize - 14);

        //Drawing inner shadow for added depth
        g2d.setColor(isWhite ? new Color(190, 160, 120) : new Color(10, 10, 10));
        g2d.fillOval(x + 10, y + 10, tileSize - 20, tileSize - 20);

        //Drawing king marker if necessary
        if (isKing) {
            g2d.setColor(isWhite ? new Color(120, 80, 40) : new Color(200, 160, 0)); // Darker golden color for king indicator
            drawCrown(g2d, x + tileSize / 2, y + tileSize / 2, tileSize / 4);
        }
    }

    private void drawCrown(Graphics2D g2d, int centerX, int centerY, int size) {
        int crownWidth = size;
        int crownHeight = size / 2;

        //Drawing three triangles for the crown peaks
        int[] xPoints1 = {centerX - crownWidth / 2, centerX - crownWidth / 4, centerX};
        int[] yPoints1 = {centerY, centerY - crownHeight, centerY};
        g2d.fillPolygon(xPoints1, yPoints1, 3);

        int[] xPoints2 = {centerX, centerX + crownWidth / 4, centerX + crownWidth / 2};
        int[] yPoints2 = {centerY, centerY - crownHeight, centerY};
        g2d.fillPolygon(xPoints2, yPoints2, 3);

        //Drawing the base of the crown as a rectangle
        g2d.fillRect(centerX - crownWidth / 2, centerY, crownWidth, crownHeight / 3);

        //Drawing small circles at the top of each peak to represent jewels
        g2d.fillOval(centerX - crownWidth / 2 - 2, centerY - crownHeight - 2, 4, 4);
        g2d.fillOval(centerX - 2, centerY - crownHeight - 2, 4, 4);
        g2d.fillOval(centerX + crownWidth / 2 - 2, centerY - crownHeight - 2, 4, 4);
    }

    private JButton makeButton(String buttonName) {
        JButton returnButton = new JButton(buttonName);
        returnButton.setBackground(Color.DARK_GRAY);
        returnButton.setForeground(Color.WHITE);
        returnButton.setBorderPainted(false);
        returnButton.setFont(new Font("Times New Roman", Font.BOLD, 25));
        returnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                returnButton.setBackground(Color.GRAY); // Change to a lighter gray on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                returnButton.setBackground(Color.DARK_GRAY); // Change back to dark gray when not hovering
            }
        });
        return returnButton;
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
