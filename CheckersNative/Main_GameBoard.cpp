#include <jni.h>
#include <vector>
#include <memory>
#include <iostream>

#include "Main_GameBoard.h"
#include "Chip.h"

std::vector<std::vector<std::unique_ptr<Chip>>> gameBoard;

JNIEXPORT void JNICALL Java_Main_GameBoard_setupGame
        (JNIEnv *, jobject){

    //initializing game board
    gameBoard.resize(8);
    for (auto row = 0; row < 8; row++) {
        gameBoard[row].resize(8);
        for (auto column = 0; column < 8; column++) {
            if ((row % 2 == 0 && column % 2 == 1) || (row % 2 == 1 && column % 2 == 0)) {
                if (row < 3) {
                    gameBoard[row][column] = std::make_unique<Chip>(Color::BLACK, false);
                }
                else if (row > 4) {
                    gameBoard[row][column] = std::make_unique<Chip>(Color::WHITE, false);
                }
            }
            else {
                gameBoard[row][column] = nullptr;
            }
        }
    }
}

JNIEXPORT void JNICALL Java_Main_GameBoard_handleClick
        (JNIEnv *, jobject, jint row, jint column) {

}

JNIEXPORT jint JNICALL Java_Main_GameBoard_getTileInformation
        (JNIEnv *, jobject, jint row, jint column) {
    auto returnInt = int(10);

    if (gameBoard[row][column] != nullptr) {
        //determining whether chosen
        returnInt = (gameBoard[row][column] -> isChosen() ? 2 : 1) * 10;

        //determining type of chip
        switch (gameBoard[row][column] -> getColor()) {
            case Color::WHITE:
                returnInt += 1;
                break;

            case Color::BLACK:
                returnInt += 3;
                break;

        }

        //determining whether king chip
        returnInt += gameBoard[row][column] -> isKing() ? 1 : 0;
    }


    return returnInt;
}