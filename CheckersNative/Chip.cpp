#include "Chip.h"


Chip::Chip(bool White, bool King) : White(White), King(King), Turn(false), Winner(false) {}
Chip::Chip(bool White, bool King, bool Winner) : White(White), King(King), Turn(false), Winner(Winner) {};

bool Chip::isKing() const { return King; }
bool Chip::isWhite() const { return White; }
bool Chip::canTurn() const { return Turn; }
bool Chip::isWinner() const { return Winner; }

void Chip::setKing(bool king) { King = king; }
void Chip::setTurn(bool turn) { Turn = turn; }
