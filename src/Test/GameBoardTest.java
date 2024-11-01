package Test;

import Main.GameBoard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GameBoardTest {

    private GameBoard testGameBoard;

    static {
        System.loadLibrary("libCheckersNative");
    }

    @BeforeEach
    void setUp() {
        testGameBoard = GameBoard.getInstance();
        testGameBoard.setupEmptyGame();
    }

    @Test
    void testSetupGame() {
        testGameBoard.setupGame();
        //checking that every tile of the board contains(or not) correct chip
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                    if (row < 3) {
                        Assertions.assertEquals(13, testGameBoard.getTileInformation(row, column), "Black unable to turn chip should be there");
                    }
                    else if (row == 5) {
                        Assertions.assertEquals(21, testGameBoard.getTileInformation(row, column), "White able to turn chip should be there");
                    }
                    else if (row > 5) {
                        Assertions.assertEquals(11, testGameBoard.getTileInformation(row, column), "White unable to turn chip should be there");
                    }
                    else Assertions.assertEquals(10, testGameBoard.getTileInformation(row, column), "Empty tile should be there");
                }
                else Assertions.assertEquals(10, testGameBoard.getTileInformation(row, column), "Empty tile should be there");
            }
        }
    }

    //TILE CHOOSING:
    @Test
    void testCorrectChoosingOfWhiteChip() {
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.handleClick(3, 4);
        Assertions.assertEquals(31, testGameBoard.getTileInformation(3, 4), "White chosen chip should be there");
    }

    @Test
    void testIncorrectChoosingOfWhiteChip() {
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.handleClick(5, 2);
        Assertions.assertEquals(21, testGameBoard.getTileInformation(3, 4), "White not chosen but able to move chip should be there");
    }

    @Test
    void testCorrectChoosingOfBlackChip() {
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(0, 1, false, false);
        testGameBoard.handleClick(3, 4);
        testGameBoard.handleClick(2, 5);
        testGameBoard.handleClick(0, 1);
        Assertions.assertEquals(33, testGameBoard.getTileInformation(0, 1), "Black chosen chip should be there");
    }

    @Test
    void testIncorrectChoosingOfBlackChip() {
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(0, 1, false, false);
        testGameBoard.handleClick(3, 4);
        testGameBoard.handleClick(2, 5);
        testGameBoard.handleClick(4, 4);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(0, 1), "Black chosen chip should be there");
    }

    //WHITE CHIPS REGULAR MOVES:
    @Test
    void testWhiteChipValidMoveInForwardLeftDiagonal() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 3); //making move
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(3, 3), "White chip should be there");
    }

    @Test
    void testWhiteChipValidMoveInForwardRightDiagonal() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 5); //making move
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(3, 5), "White chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInBackwardLeftDiagonal() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 3); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 3), "No chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInBackwardRightDiagonal() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 5); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 5), "No chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInForwardDirection() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 4); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInBackwardDirection() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 4); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 4), "No chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInRightDirection() {
        testGameBoard.insertChip(4,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(4, 5); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
    }

    @Test
    void testWhiteChipInvalidMoveInLeftDirection() {
        testGameBoard.insertChip(4 ,4, true, false);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(4, 3); //making move
        Assertions.assertEquals(21, testGameBoard.getTileInformation(4, 4), "White chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
    }

    //WHITE KING CHIP MOVES:
    @Test
    void testWhiteKingChipValidMoveInLeftDiagonal() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 3);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(3, 3), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipValidMoveInForwardRightDiagonal() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 5); //making move
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(3, 5), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipValidMoveInBackwardLeftDiagonal() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 3); //making move
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(5, 3), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipValidMoveInBackwardRightDiagonal() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 5); //making move
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 4), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(5, 5), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipInvalidMoveInForwardDirection() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(3, 4); //making move
        Assertions.assertEquals(22, testGameBoard.getTileInformation(4, 4), "White king chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
    }

    @Test
    void testWhiteKingChipInvalidMoveInBackwardDirection() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(5, 4); //making move
        Assertions.assertEquals(22, testGameBoard.getTileInformation(4, 4), "White king chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 4), "No chip should be there");
    }

    @Test
    void testWhiteKingChipInvalidMoveInRightDirection() {
        testGameBoard.insertChip(4,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(4, 5); //making move
        Assertions.assertEquals(22, testGameBoard.getTileInformation(4, 4), "White king chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
    }

    @Test
    void testWhiteKingChipInvalidMoveInLeftDirection() {
        testGameBoard.insertChip(4 ,4, true, true);
        testGameBoard.handleClick(4,4); //choosing chip
        testGameBoard.handleClick(4, 3); //making move
        Assertions.assertEquals(22, testGameBoard.getTileInformation(4, 4), "White king chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
    }

    //BLACK REGULAR CHIP MOVES:
    @Test
    void testBlackChipValidMoveInBackwardLeftDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(4, 2), "Black chip should be there");
    }

    @Test
    void testBlackChipValidMoveInBackwardRightDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(4, 4), "Black chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInForwardLeftDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 2);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 2), "No chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInForwardRightDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 4);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to turn should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 4), "No chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInForwardDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 3);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInBackwardDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 3);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInRightDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(3, 4);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
    }

    @Test
    void testBlackChipInvalidMoveInLeftDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(3, 2);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(3, 3), "Black chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 2), "No chip should be there");
    }

    //BLACK KING CHIP MOVES:
    @Test
    void testBlackKingChipValidMoveInBackwardLeftDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(4, 2), "Black chip should be there");
    }

    @Test
    void testBlackKingChipValidMoveInBackwardRightDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(4, 4), "Black chip should be there");
    }

    @Test
    void testBlackKingChipValidMoveInForwardLeftDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(2, 2), "Black king chip should be there");
    }

    @Test
    void testBlackKingChipValidMoveInForwardRightDiagonal() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(2, 4), "Black king chip should be there");
    }

    @Test
    void testBlackKingChipInvalidMoveInForwardDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(2, 3);
        Assertions.assertEquals(24, testGameBoard.getTileInformation(3, 3), "Black king chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
    }

    @Test
    void testBlackKingChipInvalidMoveInBackwardDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(4, 3);
        Assertions.assertEquals(24, testGameBoard.getTileInformation(3, 3), "Black king chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
    }

    @Test
    void testBlackKingChipInvalidMoveInRightDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(3, 4);
        Assertions.assertEquals(24, testGameBoard.getTileInformation(3, 3), "Black king chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
    }

    @Test
    void testBlackKingChipInvalidMoveInLeftDirection() {
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.insertChip(3, 3, false, true);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1); //making move with white to change turn
        testGameBoard.handleClick(3,3);
        testGameBoard.handleClick(3, 2);
        Assertions.assertEquals(24, testGameBoard.getTileInformation(3, 3), "Black king chip able to trun should be there as it didn't move");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 2), "No chip should be there");
    }

    //WHITE CHIPS CAPTURE, MULTIPLE CAPTURE
    @Test
    void testWhiteChipRegularCapture() {
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(2, 3, false, false);
        testGameBoard.handleClick(3, 4); //choosing chip
        testGameBoard.handleClick(1, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there as it was taken");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(1, 2), "White chip should be there");
    }

    @Test
    void testWhiteChipMultipleCaptureInOneDirection() {
        testGameBoard.insertChip(5, 6, true, false);
        testGameBoard.insertChip(4, 5, false, false);
        testGameBoard.insertChip(2, 3, false, false);
        testGameBoard.handleClick(5, 6);
        testGameBoard.handleClick(1, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 6), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(1, 2), "White chip should be there");
    }

    @Test
    void testWhiteChipMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(5, 6, true, false);
        testGameBoard.insertChip(4 ,5, false, false);
        testGameBoard.insertChip(2, 5, false, false);
        testGameBoard.handleClick(5, 6);
        testGameBoard.handleClick(1, 6);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 6), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 5), "No chip should be there");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(1, 6), "White chip should be there");
    }

    @Test
    void testWhiteChipInvalidMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(5, 6, true, false);
        testGameBoard.insertChip(4, 5, false, false);
        testGameBoard.insertChip(4, 3, false, false);
        testGameBoard.handleClick(5, 6);
        testGameBoard.handleClick(5, 2);
        Assertions.assertEquals(21, testGameBoard.getTileInformation(5, 6), "White chip should be there as no capture was made");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(4, 5), "Black chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(4, 3), "Black chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 2), "Black chip should be there");
    }

    @Test
    void testWhiteKingChipRegularCapture() {
        testGameBoard.insertChip(3, 4, true, true);
        testGameBoard.insertChip(2, 3, false, false);
        testGameBoard.handleClick(3, 4); //choosing chip
        testGameBoard.handleClick(1, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(3, 4), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there as it was taken");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(1, 2), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipMultipleCaptureInOneDirection() {
        testGameBoard.insertChip(5, 0, true, true);
        testGameBoard.insertChip(4 ,1, false, false);
        testGameBoard.insertChip(2, 3, false, false);
        testGameBoard.handleClick(5, 0);
        testGameBoard.handleClick(1, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 0), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 1), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(1, 4), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(5, 0, true, true);
        testGameBoard.insertChip(4 ,1, false, false);
        testGameBoard.insertChip(2, 1, false, false);
        testGameBoard.handleClick(5, 0);
        testGameBoard.handleClick(1, 0);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 0), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 1), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 1), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(1, 0), "White king chip should be there");
    }

    @Test
    void testWhiteKingChipMultipleCaptureInMultipleDirections2() {
        testGameBoard.insertChip(5, 0, true, true);
        testGameBoard.insertChip(4 ,1, false, false);
        testGameBoard.insertChip(4, 3, false, false);
        testGameBoard.handleClick(5, 0);
        testGameBoard.handleClick(5, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(5, 0), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 1), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
        Assertions.assertEquals(12, testGameBoard.getTileInformation(5, 4), "White king chip should be there");
    }

    //BLACK CHIPS CAPTURE, MULTIPLE CAPTURE
    @Test
    void testBlackChipRegularCapture() {
        testGameBoard.insertChip(1, 2, false, false);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(3, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(3, 4), "Black chip should be there");
    }

    @Test
    void testBlackChipMultipleCaptureInOneDirection() {
        testGameBoard.insertChip(1, 2, false, false);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(4, 5, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(5, 6);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(5, 6), "Black chip should be there");
    }

    @Test
    void testBlackChipMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(1, 2, false, false);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(4, 3, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(5, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
        Assertions.assertEquals(13, testGameBoard.getTileInformation(5, 2), "Black chip should be there");
    }

    @Test
    void testBlackChipInvalidMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(1, 2, false, false);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(2, 5, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(1, 6);
        Assertions.assertEquals(23, testGameBoard.getTileInformation(1, 2), "Black chip should be there as no capture was made");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(2, 3), "White chip should be there");
        Assertions.assertEquals(11, testGameBoard.getTileInformation(2, 5), "White chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 6), "White chip should be there");
    }

    @Test
    void testBlackKingChipRegularCapture() {
        testGameBoard.insertChip(1, 2, false, true);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(3, 4);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(3, 4), "Black king chip should be there");
    }

    @Test
    void testBlackKingChipMultipleCaptureInOneDirection() {
        testGameBoard.insertChip(1, 2, false, true);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(4, 5, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(5, 6);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 5), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(5, 6), "Black king chip should be there");
    }

    @Test
    void testBlackKingChipMultipleCaptureInMultipleDirections() {
        testGameBoard.insertChip(1, 2, false, true);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(4, 3, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(5, 2);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(4, 3), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(5, 2), "Black king chip should be there");
    }

    @Test
    void testBlackKingChipMultipleCaptureInMultipleDirections2() {
        testGameBoard.insertChip(1, 2, false, true);
        testGameBoard.insertChip(3, 4, true, false);
        testGameBoard.insertChip(2, 5, true, false);
        testGameBoard.handleClick(3,4);
        testGameBoard.handleClick(2, 3);
        testGameBoard.handleClick(1,2);
        testGameBoard.handleClick(1, 6);
        Assertions.assertEquals(10, testGameBoard.getTileInformation(1, 2), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 3), "No chip should be there");
        Assertions.assertEquals(10, testGameBoard.getTileInformation(2, 5), "No chip should be there");
        Assertions.assertEquals(14, testGameBoard.getTileInformation(1, 6), "Black king chip should be there");
    }

    //PROMOTION
    @Test
    void testPromotionForWhiteChip() {
        testGameBoard.insertChip(1, 4, true, false);
        testGameBoard.handleClick(1, 4);
        testGameBoard.handleClick(0, 5);
        Assertions.assertEquals(12, testGameBoard.getTileInformation(0, 5), "White chip on this tile should become a king chip");
    }

    @Test
    void testPromotionForBlackChip() {
        testGameBoard.insertChip(6, 3, false, false);
        testGameBoard.insertChip(7, 0, true, false);
        testGameBoard.handleClick(7, 0);
        testGameBoard.handleClick(6, 1);
        testGameBoard.handleClick(6, 3);
        testGameBoard.handleClick(7, 2);
        Assertions.assertEquals(14, testGameBoard.getTileInformation(7, 2), "Black chip on this tile should become a king chip");
    }

    //WIN
    @Test
    void testWhiteWin() {
        testGameBoard.insertChip(4, 3, true, false);
        testGameBoard.insertChip(3, 2, false, false);
        testGameBoard.handleClick(4, 3);
        testGameBoard.handleClick(2, 1);
        Assertions.assertEquals(-1, testGameBoard.getTileInformation(0, 0), "Code from tile 0 0 should be -1 when white wins");
    }

    @Test
    void testBlackWin() {
        testGameBoard.insertChip(5, 4, true, false);
        testGameBoard.insertChip(3, 2, false, false);
        testGameBoard.handleClick(5, 4);
        testGameBoard.handleClick(4, 3);
        testGameBoard.handleClick(3, 2);
        testGameBoard.handleClick(5, 4);
        Assertions.assertEquals(-2, testGameBoard.getTileInformation(0, 0), "Code from tile 0 0 should be -2 when black wins");
    }
}
