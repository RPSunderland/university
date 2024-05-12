/*
Problem znalezienia optymalnej/"rozsadnej" liczby ciezarowek dla danych zwiazkow chemicznych moze byc przeformulowany do jezyka teorii grafow jako znalezienie kolorowania dla podanego grafu.
Program implementuje dwa algorytmy: 
-optymalny, zwracajacy kolorowanie przy uzyciu minimalnej liczby kolorow, zlozonosc wykladnicza wzgledem liczby kolorow
-zachlanny, zwracajacy poprawne kolorowanie ktore nie musi byc optymalne, zlozonosc kwadratowa wzgledem liczby wierzcholkow
Dla liczby kolorów mniejszej niż 7, stosujemy kolorowanie optymalne, natomiast dla wiekszej zachłanne.
*/

#include <iostream>
#include <list>
#include <vector>
#include <algorithm>

std::vector<int> colour_list = {};  //tablica kolorow   
std::vector<std::list<int>> adj_list = {};  //listy sasiedztwa



void add_edge(int i, int j) {
    adj_list[i].push_back(j);
    adj_list[j].push_back(i);
}

bool is_colour_possible(int k, int col) {   //funkcja sprawdzajaca czy mozliwe jest kolorowanie danego wierzcholka ustalonym kolorem
    for(auto x : adj_list[k]) {
        if(colour_list[x] == col) return false; 
    }
    return true;
}

bool optimal_color_graph(int k, int colours) {  //optymalne kolorowanie grafu, algorytm z powracaniem o zlozonosci wykladniczej
    int V = colour_list.size();
    if(k == V) return true;
    for(int col = 1; col <= colours; ++col) {
        if(is_colour_possible(k, col)) {
            colour_list[k] = col;
            if(optimal_color_graph(k + 1, colours)) return true;
            colour_list[k] = 0;
        }
    }
    return false;
}

bool is_colorable(int colours) {    //funkcja sprawdzajaca czy dany graf da sie pokolorowac pewna ustalona liczba kolorow, uzywany do okreslenia liczby chromatycznej
    for(auto v : colour_list) {
        v = 0;
    }
    colour_list[0] = 1;
    if(optimal_color_graph(1, colours)) return true;
    return false;
}

int smallest_colour(int k) {    //funkcja pomocnicza zwracajaca najmniejsza liczbe naturalna nie wystepujaca w danym zbiorze kolorow, zlozonosc liniowa wzgledem liczby sasiadow
    int V = colour_list.size();
    std::vector<bool> is_colored = {};
    is_colored.resize(V);
    std::fill(is_colored.begin(), is_colored.end(), false);
    is_colored[0] = true;
    is_colored[colour_list[k]] = true;
    for(auto x : adj_list[k]) {
        is_colored[colour_list[x]] = true;  
    }
    int min = 0;
    for(auto colour : is_colored) {
        if(!colour) return min;
        ++min;
    }
    return V;
}

void greedy_color_graph() {     //algorytm zachlannego kolorowania grafu, zlozonosc O(V+E) 
    int V = colour_list.size();
    for(auto v : colour_list) {
        v = 0;
    }
    colour_list[0] = 1;
    for(int i = 1; i < V; ++i) {
        colour_list[i] = smallest_colour(i);
    }
}

int main() {
    int V;
    std::cin >> V;
    adj_list.resize(V);
    int x, y;
    while(std::cin >> x >> y) {
        add_edge(x - 1, y - 1);
    }
    colour_list.resize(V);

    int colours = 1;
    while(colours <= 6) {
        if(is_colorable(colours)) break;
        ++colours;
    }

    if(colours > 6) {
        greedy_color_graph();
        colours = *std::max_element(colour_list.begin(), colour_list.end());
    }

    std::cout << "Optymalna liczba samochodów: " << colours << "\n";

    std::vector<std::vector<int>> trucks = {};
    trucks.resize(colours);
    for(int i = 0; i < V; ++i) {
        trucks[colour_list[i] - 1].push_back(i + 1);
    }
    for(int i = 0; i < colours; ++i) {
        std::cout << "Samochód " << (i + 1) << ":";
        for(auto x : trucks[i]) {
            std::cout << " " << x;
        } 
        std::cout << "\n";
    }
    return 0;
}




