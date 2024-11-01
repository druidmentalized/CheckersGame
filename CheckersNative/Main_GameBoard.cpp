#include <jni.h>
#include <vector>
#include <memory>
#include <cmath>
#include <iostream>
#include <algorithm>

#include "Main_GameBoard.h"
#include "Chip.h"

std::vector<std::vector<std::unique_ptr<Chip>>> gameBoard; //8x8 board where all the information about chips stored
int chosenRow = -1;
int chosenColumn = -1;
bool whiteTurn = true;
std::vector<std::vector<int>> deletedChips; //coordinates of chips that will be deleted during execution of the "makeMove" method
std::vector<std::vector<int>> tileForChipToMove;
bool captureAvailable = false;
bool sideCanTurn = true;

bool canMakeMove (int, int, int, int, const Chip&);
bool isMovingInCorrectDirection(int, int, int, int, const Chip&);
void makeMove (int, int, int, int);
bool isCaptureMove(int, int, int, int, const Chip&);
bool isDesiredTileInBounds(int, int);
void determineWhoCanTurn();
void determineWhereCanGo(int, int, const Chip&);

JNIEXPORT void JNICALL Java_Main_GameBoard_setupGame
        (JNIEnv *, jobject){
    //setting all game variables to a start state
    chosenRow = -1;
    chosenColumn = -1;
    whiteTurn = true;

    //initializing game board
    gameBoard.resize(8);
    for (int row = 0; row < 8; row++) {
        gameBoard[row].resize(8);
        for (int column = 0; column < 8; column++) {
            if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                if (row < 3) {
                    gameBoard[row][column] = std::make_unique<Chip>(false, false);
                }
                else if (row > 4) {
                    gameBoard[row][column] = std::make_unique<Chip>(true, false);
                }
                else gameBoard[row][column] = nullptr;
            }
            else gameBoard[row][column] = nullptr;
        }
    }

    determineWhoCanTurn();
}

JNIEXPORT void JNICALL Java_Main_GameBoard_setupEmptyGame
        (JNIEnv *, jobject) {
    //setting all game variables to a start state
    chosenRow = -1;
    chosenColumn = -1;
    whiteTurn = true;

    gameBoard.resize(8);
    for (auto &row : gameBoard) {
        row.resize(8);
        for (auto &tile : row) {
            tile = nullptr;
        }
    }
}

JNIEXPORT void JNICALL Java_Main_GameBoard_insertChip
        (JNIEnv *, jobject, jint row, jint column, jboolean isWhite, jboolean isKing) {
    gameBoard[row][column] = std::make_unique<Chip>(isWhite, isKing);

    determineWhoCanTurn();
}

JNIEXPORT void JNICALL Java_Main_GameBoard_handleClick
        (JNIEnv *, jobject, jint row, jint column) {
    if (chosenRow == -1 && chosenColumn == -1) //no chosen tile
    {
        //can choose tile only if it is of our color and not empty
        if (gameBoard[row][column] && gameBoard[row][column] -> canTurn() && gameBoard[row][column] -> isWhite() == whiteTurn) {
            chosenRow = row;
            chosenColumn = column;
        }
    }
    else if (gameBoard[chosenRow][chosenColumn] -> canTurn()
             && canMakeMove(chosenRow, chosenColumn, row, column, *gameBoard[chosenRow][chosenColumn])) //some tile is already chosen
    {
        makeMove(chosenRow, chosenColumn, row, column);
    }
    else {
        chosenRow = -1;
        chosenColumn = -1;
    }


    if (chosenRow == -1 && chosenColumn == -1) {
        determineWhoCanTurn();
    }
    else {
        determineWhereCanGo(chosenRow, chosenColumn, *gameBoard[chosenRow][chosenColumn]);
    }
}

JNIEXPORT jint JNICALL Java_Main_GameBoard_getTileInformation
        (JNIEnv *, jobject, jint row, jint column) {
    auto returnInt = int(10);

    if (gameBoard[row][column]) {

        //win condition satisfied
        if (gameBoard[row][column] -> isWinner()) {
            return (gameBoard[row][column] -> isWhite() ? -1 : -2);
        }

        //determining whether chosen
        returnInt = ((gameBoard[row][column] -> canTurn() ? 1 : 0) ? 2 : 1);

        //determining whether this tile can turn
        returnInt += ((chosenRow == row && chosenColumn == column) ? 1 : 0);

        returnInt *= 10;

        //determining type of chip
        returnInt += gameBoard[row][column] -> isWhite() ? 1 : 3;

        //determining whether king chip
        returnInt += gameBoard[row][column] -> isKing() ? 1 : 0;
    }
    else {
        for (auto &coordinate : tileForChipToMove) {
            if (coordinate[0] == row && coordinate[1] == column) returnInt = 20;
        }
    }

    return returnInt;
}

bool canMakeMove(int startRow, int startColumn, int desiredRow, int desiredColumn, const Chip& checkedChip) {
    if (isDesiredTileInBounds(desiredRow, desiredColumn)
        && isMovingInCorrectDirection(startRow, startColumn, desiredRow, desiredColumn, checkedChip)
        && !gameBoard[desiredRow][desiredColumn]) {
        if (!captureAvailable
            && std::abs(desiredRow - startRow) == 1
            && std::abs(desiredColumn - startColumn) == 1) { //for casual movement
            return true;
        }
        else if (std::abs(desiredRow - startRow) % 2 == 0
            && std::abs(desiredColumn - startColumn) % 2 == 0
            && isCaptureMove(startRow, startColumn, desiredRow, desiredColumn, checkedChip)) { //for capturing opponent's chips
            return true;
        }
    }

    //we can't make a move
    return false;
}

bool isMovingInCorrectDirection(int startRow, int startColumn, int desiredRow, int desiredColumn, const Chip& checkedChip) {;
    if (checkedChip.isKing()) { return true; } //king chip can move in any direction
    else if (std::abs(desiredRow - startRow) >= std::abs(desiredColumn - startColumn)) { //for captures
        if ((checkedChip.isWhite()) && (desiredRow - startRow < 0) //for white chip
            ||
            (!checkedChip.isWhite()) && (desiredRow - startRow > 0)) //for black chip
            return true;
    }
    return false;
}

void makeMove(int startRow, int startColumn, int desiredRow, int desiredColumn) {
    //deleting eaten chips
    for (auto const &vec : deletedChips) {
        gameBoard[vec[0]][vec[1]].reset();
    }
    deletedChips.clear();

    //promotion to king
    gameBoard[desiredRow][desiredColumn] = std::move(gameBoard[startRow][startColumn]);
    if ((gameBoard[desiredRow][desiredColumn] -> isWhite() && desiredRow == 0)
        || (!gameBoard[desiredRow][desiredColumn] -> isWhite() && desiredRow == 7)) {
        gameBoard[desiredRow][desiredColumn] -> setKing(true);
    }

    //resetting data and changing turn
    chosenRow = -1;
    chosenColumn = -1;
    whiteTurn = !whiteTurn;
}

bool isCaptureMove(int startRow, int startColumn, int desiredRow, int desiredColumn, const Chip& checkedChip) {
    //finding way to the desired tile by checking all viable paths
    if (std::abs(desiredRow - startRow) > 2 || std::abs(desiredColumn - startColumn) > 2) {
        //checking every direction to which chip can go
        static int directions[4][2] = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        int tempRow;
        int tempColumn;
        for (auto const &direction : directions) {
            tempRow = startRow + direction[0];
            tempColumn = startColumn + direction[1];
            if (canMakeMove(startRow, startColumn, tempRow, tempColumn, checkedChip)) { // whether we can capture in this direction
                if ((tempRow == desiredRow) && (tempColumn == desiredColumn)) { //chip would move to the destination after this capture
                    return true;
                }
                else if (canMakeMove(startRow, startColumn, tempRow, tempColumn, checkedChip)) { //sequence of captures leads to desired tile
                    return true;
                }
                else { //sequence of captures after this won't lead to the desired tile
                    deletedChips.pop_back();
                    return false;
                }
            }
        }
    }
    //checking regular single capture
    else {
        int middleRow = (startRow + desiredRow) / 2;
        int middleColumn = (startColumn + desiredColumn) / 2;
        if (gameBoard[middleRow][middleColumn]
            && gameBoard[middleRow][middleColumn] -> isWhite() != checkedChip.isWhite()) {
            deletedChips.push_back({middleRow, middleColumn});
            return true;
        }
    }
    return false;
}

bool isDesiredTileInBounds(int row, int column) {
    return(((0 <= row) && (row < 8)) && ((0 <= column) && (column < 8)));
}

void determineWhoCanTurn() {
    tileForChipToMove.clear();
    sideCanTurn = false;
    captureAvailable = false;
    int tempRow;
    int tempColumn;
    static int captureDirections[4][2] = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    static int movingDirections[4][2] = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    //checking for possible captures
    for (int row = 0; row < 8; row++) {
        for (int column = 0; column < 8; column++) {
            if (gameBoard[row][column]) {
                gameBoard[row][column] -> setTurn(false);
                if (gameBoard[row][column] -> isWhite() == whiteTurn) {
                    for (auto const &direction : captureDirections) {
                        tempRow = row + direction[0];
                        tempColumn = column + direction[1];

                        if (canMakeMove(row, column, tempRow, tempColumn, *gameBoard[row][column])) {
                            captureAvailable = true;
                            sideCanTurn = true;
                            gameBoard[row][column] -> setTurn(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    //checking for casual movement(won't be checked if capture possible)
    if (!captureAvailable) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (gameBoard[row][column] && gameBoard[row][column] -> isWhite() == whiteTurn) {
                    for (auto const &direction : movingDirections) {
                        tempRow = row + direction[0];
                        tempColumn = column + direction[1];
                        if (canMakeMove(row, column, tempRow, tempColumn, *gameBoard[row][column])) {
                            gameBoard[row][column]->setTurn(true);
                            sideCanTurn = true;
                            break;
                        }
                    }
                }
            }
        }
    }
    deletedChips.clear();

    //if no turns available, setting the first tile as a special chip to indicate win when Java side will gather information
    if (!sideCanTurn) {
        gameBoard[0][0] = std::make_unique<Chip>(!whiteTurn, true, true);
    }
}

void determineWhereCanGo(int row, int column, const Chip& checkedChip) {
    static std::vector<std::vector<int>> directions;
    if (captureAvailable) directions = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    else directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
    int tempRow;
    int tempColumn;
    for (auto const &vectorRow : gameBoard) {
        for (auto  &tile : vectorRow) {
            if (tile && tile.get() != &checkedChip) tile -> setTurn(false);
        }
    }

    for (auto const &direction : directions) {
        tempRow = row + direction[0];
        tempColumn = column + direction[1];
        if (canMakeMove(row, column, tempRow, tempColumn, checkedChip)
            && !std::any_of(tileForChipToMove.begin(), tileForChipToMove.end(), [tempRow, tempColumn](const std::vector<int>& coord) { return coord[0] == tempRow && coord[1] == tempColumn; })) {
            tileForChipToMove.push_back({tempRow, tempColumn});
            if (captureAvailable) determineWhereCanGo(tempRow, tempColumn, checkedChip);
        }
    }
}
