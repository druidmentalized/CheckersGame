#ifndef CHECKERSNATIVE_CHIP_H
#define CHECKERSNATIVE_CHIP_H

class Chip {

private:
    bool White;
    bool King;
    bool Turn;

public:
    Chip(bool White, bool King);

    bool isWhite() const;
    bool isKing() const;
    bool canTurn() const;

    void setKing(bool king);
    void setTurn(bool turn);
};



#endif //CHECKERSNATIVE_CHIP_H
