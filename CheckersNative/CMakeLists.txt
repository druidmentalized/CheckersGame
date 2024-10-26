cmake_minimum_required(VERSION 3.28)
project(CheckersNative)

set(CMAKE_CXX_STANDARD 20)

if(MINGW)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -static -static-libgcc -static-libstdc++")
endif()

add_library(CheckersNative SHARED Main_GameBoard.cpp
        Chip.cpp
        Chip.h
)

target_include_directories(CheckersNative PRIVATE "C:\\Users\\Dima\\.jdks\\openjdk-21.0.2\\include")

target_include_directories(CheckersNative PRIVATE "C:\\Users\\Dima\\.jdks\\openjdk-21.0.2\\include\\win32")