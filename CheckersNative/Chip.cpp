#include "Chip.h"


Chip::Chip(Color color, bool King) : color(color), King(King), Chosen(false) {}

Color Chip::getColor() const { return color; }

bool Chip::isKing() const { return King; }

bool Chip::isChosen() const { return Chosen; }
