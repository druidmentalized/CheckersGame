#ifndef CHECKERSNATIVE_CHIP_H
#define CHECKERSNATIVE_CHIP_H

enum Color {
    WHITE,
    BLACK
};

class Chip {

private:
    Color color;
    bool King;
    bool Chosen;

public:
    Chip(Color color, bool King);

    Color getColor() const;

    bool isKing() const;
    bool isChosen() const;
};



#endif //CHECKERSNATIVE_CHIP_H
