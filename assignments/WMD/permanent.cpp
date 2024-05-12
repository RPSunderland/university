#include <iostream>
#include <vector>
#include <fstream>

/*
Idea algorytmu wynika wprost z definicji permanentu. Wyznaczamy wszystkie injekcje m -> n, czyli
wszystkie podzbiory m - elementowe oraz ich permutacje. Następnie zgodnie ze wzorem sumujemy po injekcjach i mnożymy
odpowiednie elementy macierzy.
Aby wyznaczyć permanent_k, należy znaleźć wszystkie k - elementowe podzbiory zbioru n - elementowego oraz wyznaczyć
permanent każdej z podmacierzy.
 */

//pomocnicze zmienne globalne (unikamy przenoszenia referencji/wskaźnika)
std::vector<std::vector<int>> injections;
std::vector<int> injection;
std::vector<int> subset;

void swap(int i,int j) {    //f. pomocnicza
    int tmp = injection[i];
    injection[i] = injection[j];
    injection[j] = tmp;
}

void reverse(int start, int stop) { //f. pomocnicza
    int i = start;
    int j = stop;

    while (i < j)
    {
        swap(i, j);
        ++i;--j;
    }
}

int nextPermutation(int m) {    //f. wyznaczjaca nastepna permuatcje w porzadku leksykograficznym
  int k = 0, l = -1;
  for(int i = m - 1; i >= 1; --i) {
    if(i == 1 || injection[i] < injection[i+1]) {
      k = i;
      break;
    }
  }

  for(int i = k + 1; i <= m; ++i) {
    if(injection[i] > injection[k]) {
      l = i;
    }
  }

  if(l == -1) {
    return 0;
  }
  swap(k, l);
  reverse(k + 1, m);

  return 1;
}
void generateAllPermutations(int m) { //f. wyznaczjaca wszystkie permutacje zbioru m - elementoweg
  do {
    injections.push_back(injection);
  }
  while(nextPermutation(m));
}

void generateInjections(int m, int n) { //f. wyznaczajaca wszystkie injekcje ze m -> n
    std::vector<int> tmp_injection;
    injection.resize(m+1);
    for(int i = 1; i <= m; i++) {
        injection[i] = i;
    }
    if(n == m) {
        generateAllPermutations(m);
        return;
    }
    int p = m;
    while(p >= 1) {
        tmp_injection = injection;
        generateAllPermutations(m);
        injection = tmp_injection;
        if (injection[m] == n) {
            --p;
        } else {
            p = m;
        }
        if (p >= 1) {
            for (int i = m; i >= p; --i) {
                injection[i] = injection[p] + (i - p + 1);
            }
        }
    }
}

int computePermanent(int** matrix, int m, int n) {  //f. wyznaczajaca permanent danej macierzy
    int permanent = 0;
    int row;

    for(auto injection : injections) {
        row = 1;
        for(int i = 1; i <= m; i++) {
            row *= matrix[subset[i]][injection[i]];
        }
        permanent += row;
    }

    return permanent;
}



int computePermanentK(int** matrix, int k, int n) { //f. wyznaczajaca permanent_k danej macierzy 
    generateInjections(k, n);
    int permanent = 0;
    subset.resize(k+1);
    for(int i = 1; i <= k; i++) {
        subset[i] = i;
    }
    if(k == n) {
        return computePermanent(matrix, k, n);
    }
    int p = k;
    while(p >= 1) {
        permanent += computePermanent(matrix, k, n);
        if (subset[k] == n) {
            --p;
        }
        else {
            p = k;
        }
        if (p >= 1) {
            for (int i = k; i >= p; --i) {
                subset[i] = subset[p] + (i - p + 1);
            }
        }
    }
    return permanent;
}

int main(int argc, char** argv) {
    if(argc != 4) {
        return 1;
    }
    std::fstream input;
    int k = atoi(argv[1]);
    int n = atoi(argv[2]);
    input.open(argv[3]);

    int** matrix = new int*[n+1];
    for(int i = 1; i <= n; i++) {
        matrix[i] = new int[n+1];
    }
    std::string tmp;
    for(int i = 1; i <= n; i++) {
        input >> tmp;
        for(int j = 1; j <= n; j++) {
            matrix[i][j] = tmp[j-1] - '0';
        }
    }
    input.close();

    std::cout << computePermanentK(matrix, k, n);

    for(int i = 1; i <= n; i++) {
        delete[] matrix[i];
    }
    delete[] matrix;

    return 0;
}