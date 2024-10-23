package Main;

//COLOR FOR DARK SQUARES: rgba(180,163,137,255)

//COLOR FOR LIGHT SQUARES: rgba(255,252,249,255)

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JLayeredPane{

    //SINGLETON
    private static GameBoard instance;

    //MAP LAYERS
    private final JPanel tilesLayer = new JPanel();
    private final JPanel piecesLayer = new JPanel();
    private final JPanel popupLayer = new JPanel();

    //MAP VARIABLES

    private GameBoard() {
        this.setLayout(null);

        //setting tiles layer
        tilesLayer.setLayout(new GridBagLayout());
        tilesLayer.setOpaque(false);
        setTiles();

        //setting pieces layer
        piecesLayer.setLayout(null);
        piecesLayer.setOpaque(false);

        //setting popup layer
        popupLayer.setLayout(null);
        popupLayer.setOpaque(false);

        this.add(tilesLayer, Integer.valueOf(1));
        this.add(tilesLayer, Integer.valueOf(2));
        this.add(tilesLayer, Integer.valueOf(3));
    }

    private void setTiles() {

    }

    public static GameBoard getInstance() {
        if (instance == null) {
            instance = new GameBoard();
        }
        return instance;
    }
}
