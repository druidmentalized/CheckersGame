#ifndef CHECKERSNATIVE_CHIP_H
#define CHECKERSNATIVE_CHIP_H

class Chip {

private:
    bool White;
    bool King;

public:
    Chip(bool White, bool King);

    bool isWhite() const;

    bool isKing() const;
};



#endif //CHECKERSNATIVE_CHIP_H
