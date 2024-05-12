//Rafal Plizga
#pragma once
#include <iostream>
#include <vector>

class spline {
public:
    spline(int m);
    void set_points(double x[], double y[]);
    double operator() (double t) const;
    double divided_differences(const std::vector<double>& X, const std::vector<double>& Y, int begin, int end); 
    void compute();
    void compute_A();
    void compute_B();
    void compute_C();
    void compute_D();
    void compute_H();
    void compute_U();
    void compute_W();
    void compute_V();
    

private:
    int n; 
    std::vector<double> X; 
    std::vector<double> Y; 

    std::vector<double> A;  
    std::vector<double> B;
    std::vector<double> C;
    std::vector<double> D;

    std::vector<double> H;
    std::vector<double> U;
    std::vector<double> V;
    std::vector<double> W;
};