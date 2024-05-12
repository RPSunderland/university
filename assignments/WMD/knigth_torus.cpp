/*
"Nalozenie" szachownicy na torus mozna zinterpretowac jako zrzutowanie szachownicy na plaszczyzne, tak aby otrzymac nieskonczona ilosc plaskich szachownic obok siebie, ruch (1, 1) − (n, 3) mozna
wtedy zrozumiec jako po prostu przeskoczenie na inna szachownice. Pola szachownic zostaly napisane w jezyku grafow tak aby mozna bylo do nich zastosowac algorytm znajdowania cyklu Hamiltona.
Dla N >= 5, sasiadami (x, y) sa : (x+2, y+1), (x+1, y+2), (x+2, y-1), (x+1, y-2), (x-2, y+1), (x-1, y+2), (x-2, y-1), (x-1, y-2), mod N.
W przypadku N < 5, pojawia sie powtorki z uwagi na maly rozmiar szachownic, liczba sasiadow jest wtedy odpowiednio mniejsza.
Wierzcholki indeksuje od 0 z uwagi na latwosc konwersji do indexu w jednowymiarowej tablicy (x, y) <-> (x * N + y).
*/

#include <iostream>
#include <list>

const int N = 8;              // liczba wierzcholkow
std::list<int> adj_list[N * N];     // listy sasiedztwa, wierzcholki numerujemy od 0
bool allowed[N * N];               //tablica pol dopuszczalnych
int path[N * N];                  //sciezka 

void add_edge (int u, int v) {  
    adj_list[u].push_back(v); 
}

int mod(int index) { //pomocnicza funkcja redukujaca indexy mod N
    if(index < 0) {
        index += N;
    }
    return (index % N);
}

void construct_graph() {
    if(N >= 5) {
        for(int i = 0; i < N; i++) {   //wiersze
            for(int j = 0; j < N; j++) {   //kolumny
                add_edge(i * N + j, mod(i + 2) * N + mod(j + 1));  
                add_edge(i * N + j, mod(i + 1) * N + mod(j + 2));  
                add_edge(i * N + j, mod(i - 2) * N + mod(j + 1));  
                add_edge(i * N + j, mod(i - 1) * N + mod(j + 2));  
                add_edge(i * N + j, mod(i + 2) * N + mod(j - 1));  
                add_edge(i * N + j, mod(i + 1) * N + mod(j - 2));  
                add_edge(i * N + j, mod(i - 2) * N + mod(j - 1));  
                add_edge(i * N + j, mod(i - 1) * N + mod(j - 2));  
            }
        }
        return;
    }

    if(N == 2) {
        for(int i = 0; i < N; i++) {   
            for(int j = 0; j < N; j++) {   
                add_edge(i * N + j, mod(i + 2) * N + mod(j + 1));  
                add_edge(i * N + j, mod(i + 1) * N + mod(j + 2));  
            }
        }
        return;
    }

    if(N == 3) {
        for(int i = 0; i < N; i++) {   
            for(int j = 0; j < N; j++) {   
                add_edge(i * N + j, mod(i + 2) * N + mod(j + 1));  
                add_edge(i * N + j, mod(i + 1) * N + mod(j + 2));  
                add_edge(i * N + j, mod(i + 2) * N + mod(j - 1));
            }
        }
        return;
    }

    if(N == 4) {
        for(int i = 0; i < N; i++) {   
            for(int j = 0; j < N; j++) {   
                add_edge(i * N + j, mod(i + 2) * N + mod(j + 1));  
                add_edge(i * N + j, mod(i + 1) * N + mod(j + 2));  
                add_edge(i * N + j, mod(i + 2) * N + mod(j - 1));
                add_edge(i * N + j, mod(i - 1) * N + mod(j - 2)); 
            }
        }
        return;
    } 
}

bool hamilton(int k) {
    if(k == N * N) {
        for(auto u : adj_list[path[0]]) {
            if(u == path[N * N - 1]) return true;
        }
        return false;
    }

    for(auto u : adj_list[path[k - 1]]) {
        if(allowed[u]) {
            path[k] = u;
            allowed[u] = false;
            if(hamilton(k + 1)) return true;
            allowed[u] = true;
        }
    }
    return false;
}

void initialize() {
    for (int i = 0; i < N; ++i) {
        for(int j = 0; j < N; ++j) {
            allowed[i * N + j] = true;
        }
    }
    allowed[0] = false;
    path[0] = 0;
}

void hamilton_cycle() {
    if(N == 1) {
        std::cout << "Nie istnieje taka sciezka\n";
        return;
    }

    construct_graph();
    initialize();
    for(int k = 0; k < adj_list[0].size(); ++k) {
        if(hamilton(1)) {
            for(int i = 0; i < N * N; ++i) {
                std::cout << "(" << path[i] / N + 1 << ", " << path[i] % N + 1 << ")";
            }
            std::cout << "(" << path[0] / N + 1 << ", " << path[0] % N + 1 << ")\n";
        }
        else {
            std::cout << "Nie istnieje taka sciezka\n";
        }
        int u = adj_list[0].front();
        adj_list[0].pop_front();
        adj_list[0].push_back(u);
        initialize();
    }
}

int main() {
    hamilton_cycle();   
    return 0;
}