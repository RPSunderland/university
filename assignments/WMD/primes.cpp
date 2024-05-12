#include <iostream>
#include <vector>
#include <memory>

void printPrimesInfo(int N) {
    auto T = std::make_unique<bool[]>(N + 1);
    std::vector<std::pair<int, int>> twin_primes;
    int  max_gap = 0;
    int actual_gap = 0;

    for(int i = 0; i < N + 1; ++i) {
        T[i] = false;
    }

    for(int i = 2; i * i < N + 1; ++i) {
        if(!T[i]) {
            for(int j = i * i; j < N + 1; j += i) {
                T[j] = true;
            }
        }
    }

    for(int i = 2; i < N + 1; ++i) {
        if(!T[i]) {
            std::cout << i << "\n";
            max_gap = std::max(max_gap, actual_gap);
            if(i + 2 < N + 1 && !T[i + 2]) {
                twin_primes.emplace_back(i, i + 2);
            }
            actual_gap = 0;
            continue;
        }
        ++actual_gap;
    }

    for(auto pair : twin_primes) {
        std::cout << "(" << pair.first << ", " << pair.second << ")\n";
    }
    std::cout << max_gap << "\n";
}

int main() {
    int N;
    std::cin >> N; 
    printPrimesInfo(N);
    return 0;
}