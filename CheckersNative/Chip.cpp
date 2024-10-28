#include "Chip.h"


Chip::Chip(bool White, bool King) : White(White), King(King) {}

bool Chip::isKing() const { return King; }

bool Chip::isWhite() const { return White; }
