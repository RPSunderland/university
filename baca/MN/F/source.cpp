//Rafal Plizga
#include <vector>
#include <iostream>


int main() {
    size_t N;
    size_t M;
    std::cin >> N >> M;
    std::vector<std::vector<double>> bands(M); 
    std::vector<double> diagonal(N);
    std::vector<double> y(N);
    std::vector<double> x(N);
    double w;
    size_t L;

    for(size_t i = 0; i < M; ++i) {
        bands[i] = std::vector<double>(N - M + i);
        for(size_t j = 0; j < N - M + i; ++j) {
            std::cin >> bands[i][j];
        }
    }
    for(size_t i = 0; i < N; ++i) {
        std::cin >> diagonal[i];
    }
    for(size_t i = 0; i < N; ++i) {
        std::cin >> y[i];
    }
    for(size_t i = 0; i < N; ++i) {
        std::cin >> x[i];
    }
    std::cin >> w >> L;



    for(size_t iter = 0; iter < L; ++iter) {
        for(size_t i = 0; i < N; ++i) {
            double s = y[i];
            size_t left, right;
            i + 1 + M < N ? right = i + 1 + M : right = N;
            i >= M ? left = i - M : left = 0;

            for(size_t j = left; j < i; ++j) {
                //lewe kolumny
                s -= x[j] * bands[(M - 1) - ((i - 1) - j)][j];
            }
            
            for(size_t j = i + 1; j < right; ++j) {
                //prawe kolumny
                s -= x[j] * bands[(M - 1) - (j - (i + 1))][i];
            }
            x[i] = (1 - w) * x[i] + (w * s) / diagonal[i];
        }
    }

    std::cout.precision(16);
	std::cout << std::scientific;

    for(size_t i = 0; i < N; ++i) {
        std::cout << x[i] << "\n";
    }

}