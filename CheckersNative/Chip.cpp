#include "Chip.h"


Chip::Chip(bool White, bool King) : White(White), King(King), Turn(false) {}

bool Chip::isKing() const { return King; }
bool Chip::isWhite() const { return White; }
bool Chip::canTurn() const { return Turn; }

void Chip::setKing(bool king) { King = king; }
void Chip::setTurn(bool turn) { Turn = turn; }
