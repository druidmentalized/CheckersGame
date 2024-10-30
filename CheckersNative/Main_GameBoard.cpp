#include <jni.h>
#include <vector>
#include <memory>
#include <cmath>
#include <iostream>

#include "Main_GameBoard.h"
#include "Chip.h"

std::vector<std::vector<std::unique_ptr<Chip>>> gameBoard; //8x8 board where all the information about chips stored
int chosenRow = -1;
int chosenColumn = -1;
bool whiteTurn = true;
std::vector<std::vector<int>> deletedChips; //coordinates of chips that will be deleted during execution of the "makeMove" method
bool captureAvailable = false;
bool sideCanTurn = true;

bool canMakeMove (int, int, int, int);
bool isMovingInCorrectDirection(int, int, int, int, const Chip&);
void makeMove (int, int, int, int);
bool isCaptureMove(int, int, int, int, const Chip&);
bool isMoveInBounds(int, int);
void determineWhoCanTurn();

JNIEXPORT void JNICALL Java_Main_GameBoard_setupGame
        (JNIEnv *, jobject){
    //setting all game variables to a start state
    chosenRow = -1;
    chosenColumn = -1;
    whiteTurn = true;

    //initializing game board
    gameBoard.resize(8);
    for (auto row = 0; row < 8; row++) {
        gameBoard[row].resize(8);
        for (auto column = 0; column < 8; column++) {
            if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                if (row < 3) {
                    gameBoard[row][column] = std::make_unique<Chip>(false, false);
                }
                else if (row > 4) {
                    gameBoard[row][column] = std::make_unique<Chip>(true, false);
                }
            }
            else {
                gameBoard[row][column] = nullptr;
            }
        }
    }
    determineWhoCanTurn();
}

JNIEXPORT void JNICALL Java_Main_GameBoard_handleClick
        (JNIEnv *, jobject, jint row, jint column) {

    if (chosenRow == -1 && chosenColumn == -1) //no chosen tile
    {
        //can choose tile only if it is of our color and not empty
        if (gameBoard[row][column] && gameBoard[row][column] -> isWhite() == whiteTurn) {
            chosenRow = row;
            chosenColumn = column;
        }
    }
    else if (canMakeMove(chosenRow, chosenColumn, row, column)) //some tile is already chosen
    {
        makeMove(chosenRow, chosenColumn, row, column);
    }
    determineWhoCanTurn();
}

JNIEXPORT jint JNICALL Java_Main_GameBoard_getTileInformation
        (JNIEnv *, jobject, jint row, jint column) {
    auto returnInt = int(100);

    if (gameBoard[row][column]) {

        //win condition satisfied
        if (gameBoard[row][column] -> isWinner()) {
            return (gameBoard[row][column] -> isWhite() ? -1 : -2);
        }

        //determining whether chosen
        returnInt = ((chosenRow == row && chosenColumn == column) ? 2 : 1) * 10;

        //determining whether this tile can turn
        returnInt = (returnInt + (gameBoard[row][column] -> canTurn() ? 2 : 1)) * 10;

        //determining type of chip
        returnInt += gameBoard[row][column] -> isWhite() ? 1 : 3;

        //determining whether king chip
        returnInt += gameBoard[row][column] -> isKing() ? 1 : 0;
    }
    return returnInt;
}

bool canMakeMove(int startRow, int startColumn, int desiredRow, int desiredColumn) {
    if (gameBoard[startRow][startColumn] -> canTurn()
        && isMovingInCorrectDirection(startRow, startColumn, desiredRow, desiredColumn, *gameBoard[startRow][startColumn])
        && !gameBoard[desiredRow][desiredColumn]) {
        if (!captureAvailable
            && std::abs(desiredRow - startRow) == 1
            && std::abs(desiredColumn - startColumn) == 1) { //for casual movement
            return true;
        }
        else if (std::abs(desiredRow - startRow) % 2 == 0
            && std::abs(desiredColumn - startColumn) % 2 == 0
            && isCaptureMove(startRow, startColumn, desiredRow, desiredColumn, *gameBoard[startRow][startColumn])) { //for capturing opponent's chips
            return true;
        }
    }

    //we can't make a move, so we reset the chosen tile data
    chosenRow = -1;
    chosenColumn = -1;
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
    for (auto &vec : deletedChips) {
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
        for (auto &direction : directions) {
            tempRow = startRow + direction[0];
            tempColumn = startColumn + direction[1];
            if (isMoveInBounds(tempRow, tempColumn) //next step must be in bounds
                && isMovingInCorrectDirection(startRow, startColumn, tempRow, tempColumn, checkedChip) //next step must be in correct direction
                && isCaptureMove(startRow, startColumn, tempRow, tempColumn, checkedChip)) { // whether we can capture in this direction
                if ((tempRow == desiredRow) && (tempColumn == desiredColumn)) { //chip would move to the destination after this capture
                    return true;
                }
                else if (isCaptureMove(tempRow, tempColumn, desiredRow, desiredColumn, checkedChip)) { //sequence of captures leads to desired tile
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

bool isMoveInBounds(int row, int column) {
    return(((0 <= row) && (row < 8)) && ((0 <= column) && (column < 8)));
}

void determineWhoCanTurn() {
    sideCanTurn = false;
    captureAvailable = false;
    int tempRow;
    int tempColumn;
    static int captureDirection[4][2] = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
    static int movingDirection[4][2] = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    //checking for possible captures
    for (int row = 0; row < 8; row++) {
        for (int column = 0; column < 8; column++) {
            if (gameBoard[row][column]) {
                gameBoard[row][column] -> setTurn(false);
                if (gameBoard[row][column] -> isWhite() == whiteTurn) {
                    for (auto &direction : captureDirection) {
                        tempRow = row + direction[0];
                        tempColumn = column + direction[1];
                        if (isMoveInBounds(tempRow, tempColumn) //next step must be in bounds
                            && isMovingInCorrectDirection(row, column, tempRow, tempColumn, *gameBoard[row][column]) //next step must be in correct direction
                            && !gameBoard[tempRow][tempColumn]
                            && isCaptureMove(row, column, tempRow, tempColumn, *gameBoard[row][column])) {
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
                    for (auto &direction : movingDirection) {
                        tempRow = row + direction[0];
                        tempColumn = column + direction[1];

                        if (isMoveInBounds(tempRow, tempColumn)
                            && isMovingInCorrectDirection(row, column, tempRow, tempColumn, *gameBoard[row][column])
                            && !gameBoard[tempRow][tempColumn]) {
                            gameBoard[row][column] -> setTurn(true);
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