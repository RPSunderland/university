//Rafal Plizga

#include <iostream>
#include <vector>
#include <unordered_map>
#include <cmath>

using ullong = unsigned long long;

long double factorial (int n) {
    long double result = 1;
    for(int i = 1; i <= n; ++i) {
        result *= i;
    }
    return result;
}

long double divided_differences(const std::vector<long double>& X, const std::vector<long double>& Y, const std::unordered_map<long double, int>& first_positions, int begin, int end) { 
    if(begin == end) return Y[first_positions.at(X[begin])]; 
    if(X[begin] == X[end]) return Y[first_positions.at(X[begin]) + (end - begin)] / factorial(end - begin); 
    return (divided_differences(X, Y, first_positions, begin + 1, end) - divided_differences(X, Y, first_positions, begin, end - 1)) / (X[end] - X[begin]);
}

long double horner(const std::vector<long double>& X, const std::vector<long double>& W, long double t) {
    long double result = W.back();
    for(int i = W.size() - 2; i >= 0; --i) {
        result = W[i] + result * (t - X[i]);
    }
    return result;
}

int main() {
    int M;  
    int N;  
    std::cin >> M >> N;
    std::vector<long double> X(M);   //wezly interpolacji
    std::vector<long double> Y(M);   //wartosci (funkcji lub pochodnych) w wezlach
    std::vector<long double> T(N);   //punkty w ktorych nalezy wyznaczyc wielomian interpolacyjny
    std::vector<long double> W(M);   //wspolczynniki wielomianu
    std::vector<long double> F(N);   //wartosci wielomianu w punktach T_i
    std::unordered_map<long double, int> first_positions; //mapa zwracajaca pierwsze wystapienia danej wartosci

    for(int i = 0; i < M; ++i) {
        std::cin >> X[i];
        if(i == 0) first_positions[X[i]] = 0;
        if(X[i] != X[i - 1] && i > 0) first_positions[X[i]] = i;   
    }
    for(int i = 0; i < M; ++i) {
        std::cin >> Y[i];
    }
    for(int i = 0; i < N; ++i) {
        std::cin >> T[i];
    }

    //wyznaczenie wspolczynnikow wielomianu
    for(int i = 0; i < M; ++i) {
        W[i] = divided_differences(X, Y, first_positions, 0, i);
    }

    //wyznaczenie wartosci wielomianu
    for(int i = 0; i < N; ++i) {
        F[i] = horner(X, W, T[i]);
    }

    std::cout.precision(17);    
    std::cout << W[0];
    for(int i = 1; i < M; ++i) {
        std::cout << " " << W[i];
    }
    std::cout << "\n";
    std::cout << F[0];
    for(int i = 1; i < N; ++i) {
        std::cout << " " << F[i];
    }
    std::cout << "\n";

    return 0;
}