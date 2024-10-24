cmake_minimum_required(VERSION 3.28)
project(CheckersNative)

set(CMAKE_CXX_STANDARD 20)

add_library(CheckersNative SHARED library.cpp)
