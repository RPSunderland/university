//Rafal Plizga
#include <iostream>
#include <cmath>
#include <iomanip>
#include "funkcja.h"

enum Var {
    X1, X2, CONST
};

class Function_Pack {
public:
    Function_Pack(double x, Var var) : f(x), f_x(0), f_y(0), f_xx(0), f_xy(0), f_yy(0) {
        switch (var) {
        case X1:
            f_x = 1;
            return;
        case X2:
            f_y = 1;
            return;
        }
    }
    Function_Pack() : f(0), f_x(0), f_y(0), f_xx(0), f_xy(0), f_yy(0) { }
    Function_Pack(const Function_Pack& other) : f(other.f), f_x(other.f_x), f_y(other.f_y), f_xx(other.f_xx), f_xy(other.f_xy), f_yy(other.f_yy) { }
    Function_Pack& operator=(Function_Pack& other) {
        if (this == &other) return *this;
        f = other.f;
        f_x = other.f_x;
        f_y = other.f_y;
        f_xx = other.f_xx;
        f_xy = other.f_xy;
        f_yy = other.f_yy;
        return *this;
    }
    friend std::ostream& operator<<(std::ostream& str, const Function_Pack& FP) {
        str << FP.f << " " << FP.f_x << " " << FP.f_y << " " << FP.f_xx << " " << FP.f_xy << " " << FP.f_yy << "\n";
        return str;
    }
    friend Function_Pack operator+(const Function_Pack& FP1, const Function_Pack& FP2) { 
        Function_Pack FP;
        FP.f = FP1.f + FP2.f;
        FP.f_x = FP1.f_x + FP2.f_x;
        FP.f_y = FP1.f_y + FP2.f_y;
        FP.f_xx = FP1.f_xx + FP2.f_xx;
        FP.f_xy = FP1.f_xy + FP2.f_xy;
        FP.f_yy = FP1.f_yy + FP2.f_yy;
        return FP;
    }
    friend Function_Pack operator-(const Function_Pack& FP1, const Function_Pack& FP2) { 
        Function_Pack FP;
        FP.f = FP1.f - FP2.f;
        FP.f_x = FP1.f_x - FP2.f_x;
        FP.f_y = FP1.f_y - FP2.f_y;
        FP.f_xx = FP1.f_xx - FP2.f_xx;
        FP.f_xy = FP1.f_xy - FP2.f_xy;
        FP.f_yy = FP1.f_yy - FP2.f_yy;
        return FP;
    }
    friend Function_Pack operator*(const Function_Pack& FP1, const Function_Pack& FP2) { 
        Function_Pack FP;
        FP.f = FP1.f * FP2.f;
        FP.f_x = FP2.f * FP1.f_x + FP1.f * FP2.f_x;
        FP.f_y = FP2.f * FP1.f_y + FP1.f * FP2.f_y;
        FP.f_xx = 2 * FP1.f_x * FP2.f_x + FP2.f * FP1.f_xx
                    + FP1.f * FP2.f_xx;
        FP.f_xy = FP2.f_y * FP1.f_x + FP1.f_y * FP2.f_x
                    + FP2.f * FP1.f_xy + FP1.f * FP2.f_xy;
        FP.f_yy = 2 * FP1.f_y * FP2.f_y + FP2.f * FP1.f_yy 
                    + FP1.f * FP2.f_yy;
        return FP;
    }
    friend Function_Pack operator/(const Function_Pack& FP1, const Function_Pack& FP2) {
        Function_Pack FP;
        FP.f = FP1.f / FP2.f;
        FP.f_x = FP1.f_x / FP2.f - (FP1.f * FP2.f_x) / std::pow(FP2.f, 2);
        FP.f_y = FP1.f_y / FP2.f - (FP1.f * FP2.f_y) / std::pow(FP2.f, 2);
        FP.f_xx = -1 * (2 * FP1.f_x * FP2.f_x) / std::pow(FP2.f, 2)
                    + (2 * FP1.f * std::pow(FP2.f_x, 2)) / std::pow(FP2.f, 3) 
                    + FP1.f_xx / FP2.f
                    - (FP1.f * FP2.f_xx) / std::pow(FP2.f, 2);
        
        FP.f_xy = -1 * (FP2.f_y * FP1.f_x) / std::pow(FP2.f, 2)
                    - (FP1.f_y * FP2.f_x) / std::pow(FP2.f, 2)
                    + (2 * FP1.f * FP2.f_y * FP2.f_x) / std::pow(FP2.f, 3)
                    + FP1.f_xy / FP2.f
                    - (FP1.f * FP2.f_xy) / std::pow(FP2.f, 2);
        FP.f_yy = -1 * (2 * FP1.f_y * FP2.f_y) / std::pow(FP2.f, 2)
                    + (2 * FP1.f * std::pow(FP2.f_y, 2)) / std::pow(FP2.f, 3) 
                    + FP1.f_yy / FP2.f
                    - (FP1.f * FP2.f_yy) / std::pow(FP2.f, 2);
        return FP;
    }
    
    friend Function_Pack operator+(const Function_Pack& FP1, double C) { 
        Function_Pack FP2(C, CONST);
        return FP1 + FP2;
    }
    friend Function_Pack operator-(const Function_Pack& FP1, double C) {
        Function_Pack FP2(C, CONST);
        return FP1 - FP2;
    }
    friend Function_Pack operator*(const Function_Pack& FP1, double C) {
        Function_Pack FP2(C, CONST);
        return FP1 * FP2;
    }
    friend Function_Pack operator/(const Function_Pack& FP1, double C) {
        Function_Pack FP2(C, CONST);
        return FP1 / FP2;
    }
    
    friend Function_Pack operator+(double C, const Function_Pack& FP1) {  
        Function_Pack FP2(C, CONST);
        return FP2 + FP1;
    }
    friend Function_Pack operator-(double C, const Function_Pack& FP1) {    
        Function_Pack FP2(C, CONST);
        return FP2 - FP1;
    }
    friend Function_Pack operator*(double C, const Function_Pack& FP1) {    
        Function_Pack FP2(C, CONST);
        return FP2 * FP1;
    }
    friend Function_Pack operator/(double C, const Function_Pack& FP1) {    
        Function_Pack FP2(C, CONST);
        return FP2 / FP1;
    }

    friend Function_Pack operator-(const Function_Pack& FP1) { 
        Function_Pack FP2(-1., CONST);
        return FP2 * FP1;
    }

    friend Function_Pack sin(const Function_Pack& FP1) {
        Function_Pack FP;
        FP.f = std::sin(FP1.f);
        FP.f_x = std::cos(FP1.f) * FP1.f_x;
        FP.f_y = std::cos(FP1.f) * FP1.f_y;
        FP.f_xx = -1 * std::sin(FP1.f) * std::pow(FP1.f_x, 2)
                    + std::cos(FP1.f) * FP1.f_xx;
        FP.f_xy = -1 * std::sin(FP1.f) * FP1.f_y * FP1.f_x 
                    + std::cos(FP1.f) * FP1.f_xy;
        FP.f_yy = -1 * std::sin(FP1.f) * std::pow(FP1.f_y, 2)
                    + std::cos(FP1.f) * FP1.f_yy;
        return FP;
    }

    friend Function_Pack cos(const Function_Pack& FP1) { 
        Function_Pack FP;
        FP.f = std::cos(FP1.f);
        FP.f_x = -1 * std::sin(FP1.f) * FP1.f_x;
        FP.f_y = -1 * std::sin(FP1.f) * FP1.f_y;
        FP.f_xx = -1 * std::cos(FP1.f) * std::pow(FP1.f_x, 2)
                    - std::sin(FP1.f) * FP1.f_xx;
        FP.f_xy = -1 * std::cos(FP1.f) * FP1.f_y * FP1.f_x 
                    - std::sin(FP1.f) * FP1.f_xy;
        FP.f_yy = -1 * std::cos(FP1.f) * std::pow(FP1.f_y, 2)
                    - std::sin(FP1.f) * FP1.f_yy;
        return FP;
    }

    friend Function_Pack exp(const Function_Pack& FP1) {
        Function_Pack FP;
        FP.f = std::exp(FP1.f);
        FP.f_x = std::exp(FP1.f) * FP1.f_x;
        FP.f_y = std::exp(FP1.f) * FP1.f_y;
        FP.f_xx = std::exp(FP1.f) * std::pow(FP1.f_x, 2) 
                    + std::exp(FP1.f) * FP1.f_xx;
        FP.f_xy = std::exp(FP1.f) * FP1.f_y * FP1.f_x
                    + std::exp(FP1.f) * FP1.f_xy;
        FP.f_yy = std::exp(FP1.f) * std::pow(FP1.f_y, 2) 
                    + std::exp(FP1.f) * FP1.f_yy;
        return FP;
    }

private:
    long double f;
    long double f_x;
    long double f_y;
    long double f_xx;
    long double f_xy;
    long double f_yy;
};

int main() {
    std::cout.precision(15);
    int M; 
    std::cin >> M;
    long double x, y;
    for(int i = 0; i < M; ++i) {
        std::cin >> x >> y;
        Function_Pack X(x, X1);
        Function_Pack Y(y, X2);
        std::cout << funkcja(X, Y);
    }
    return 0;
}