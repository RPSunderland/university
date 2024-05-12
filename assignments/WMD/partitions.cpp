#include <iostream>
#include <vector>

void printPartitions(std::vector<std::vector<std::vector<int>>>& partitions) {
    std::cout << "{\n";
    for(auto partition : partitions) {
        std::cout << "{";
        for(auto set : partition) {
            std::cout << "{";
            for(int i = 0; i < set.size(); i++) {
                std::cout << set[i];
            }
            std::cout << "}";
        }
        std::cout << "}\n";
    }
    std::cout << "}\n";
}

std::vector<std::vector<std::vector<int>>> generatePartitions(int n) {
    std::vector<std::vector<std::vector<int>>> new_partitions{};
    if(n >= 1) {
        if(n == 1) {    //warunek stopu, istnieje tylko jeden podzial zbioru jednoelementowego - {{1}}
            std::vector<std::vector<int>> partition = {std::vector<int>{1}};       
            new_partitions = {partition};
            return new_partitions;  
        }

        std::vector<std::vector<std::vector<int>>> old_partitions = generatePartitions(n-1);   
        for(auto partition : old_partitions) {  //dorzucanie elementu n to podzialow zbioru n-1 elementowego
            partition.push_back(std::vector<int>{n});           
            new_partitions.push_back(partition);
        }

        for(auto partition : old_partitions) {  //dorzucanie elementu n po kolei do wszystkich zbiorow we wszystkich podzialach i stworzenie w ten sposob nowych podzialow
            for(int i = 0; i < partition.size(); i++) {
                partition[i].push_back(n);
                new_partitions.push_back(partition);
                partition[i].pop_back();
            }
        }
    }
    return new_partitions;
}


int main() {
    int n;
    std::cin >> n;
    std::vector<std::vector<std::vector<int>>> partitions = generatePartitions(n);
    printPartitions(partitions);
    return 0;
}

