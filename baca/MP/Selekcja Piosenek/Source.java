// Rafal Plizga 5
/*
Program rozwiazujacy problem znalezienia i-tego pod wzgledem wartosci elementu w danej tablic (pierwszy oznacza najmniejsza,
ostatni najwieksza, srodkowy mediane, itd.). Idea programu opiera sie na algorytmie magicznych piatek. Najwazniejsza jest
metoda Select, jezeli tablica ma mniej niz 51 elementow wykonywane jest sortowanie przez wstawianie i zwracana jest
wartosc danego indexu. W przypadku wiekszej tablicy chcemy znalezc element rozdzielajacy, aby wywolac metode ponownie w
mniejszej podtablicy. Elementem rozdzielajacym jest tzw. mediana median, czyli dzielimy na tablice na [n/5] czesci,
sortujemy je i znajdujemy ich mediany, a nastepnie ponownie wywolujemy metode Select na zbiorze median. Z uwagi na to
ze w tablicy moga wystepowac duplikaty, wyznaczamy rowniez elementy rowne wartosci punktu podzialowego i dzielimy
wzgledem mediany tych elementow tak aby uzyskac optymalne podzialy dla zbiorow z duza iloscia duplikatow.
Mozna pokazac, ze zawsze metoda partition podzieli tablice w taki sposob, ze conajmniej 3/4 elementow bedzie dobrze polozonych.
Wyznaczanie mediany median jest podzadaniem wielkosci n/5, a pozostale operacje sa wykonywane w czasie liniowym, mamy wiec:
T(n) <= c1 * n, dla n <= 50
T(n) <= T(n/5)+T(3n/4)+c2*n, dla n > 50
Mozna pokazac ze T(n)<=20*c*n, gdzie c = max(c1,c2), zatem nasz algorytm ma pesymistyczna zlozonosc O(n). Program
nie wykorzystuje rowniez dodatkowej pamieci poza wywolywaniem funkcji rekurencyjnej, zatem zlozonosc pamieciowa wynosi O(1).
 */
import java.util.Scanner;
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static void swap(int[] T, int i, int j){
        /*
        Metoda pomocnicza swapujaca elementy
         */
        int tmp = T[i];
        T[i] = T[j];
        T[j] = tmp;
    }
    public static int Select(int[] T, int L, int R, int index){
        /*
        Jesli tablica ma jeden element zwroc wartosc tego elementu
         */
        if(L == R){
            return T[index];
        }
        /*
        Jesli tablica ma mniej niz 51 elementow posortuj przez wstawianie, a nastepnie zwroc wartosc w danym indexie, O(1)
         */
        if(R-L < 50){ //
            InsertionSort(T,L,R);
            return T[index];
        }
        /*
        Jesli tablica ma conajmniej 51 elementow, podziel ja na n/5 czesci, posortuj je przez wstawianie i swapuj
        z poczatkowymi elementami tablicy, aby otrzymac mediany na poczatku. Zlozonosc wynosi O(n).
         */
        for(int i = L; i < R + 1; i+=5){
            if(i+5 < R){
                InsertionSort(T,i,i+4);
                swap(T,i+2,L + (i-L)/5);
            }
            else{
                InsertionSort(T,i,R);
                swap(T,(i+R)/2,L + (i-L)/5);
            }
        }
        int medianValue = Select(T,L,L+(R-L)/5,L+(R-L)/10); //mediana median
        int q = qPartition(T,L,R,medianValue,index);  //punkt podzialu
        if (index == q) {   //jesli punkt podzialu pokrywa sie z indexem to zwroc mediane median
            return medianValue;
        }
        if (index < q) {
            return Select(T, L, q - 1, index);  //wywolaj rekurencyjnie lewa strone tablicy
        }
        else {
            return Select(T, q + 1, R, index);  //wywolaj rekurenycjnie prawa strone tablicy
        }
    }
    public static int qPartition(int[] T, int L, int R, int medianValue, int index){
        for(int i = L; i < R; i++){
            if(T[i] == medianValue){
                swap(T,i,R);    //wstaw mediane na koniec
                break;
            }
        }
        for(int i = L; i < R; i++){
            if(T[i] == medianValue){
                swap(T,i,R);    //wstaw mediane na koniec
                break;
            }
        }
        //wykonuj podzial wzgledem mediany, O(n)
        int pivot = T[R];
        int i = L-1;
        for(int j = L; j < R; j++){     //wyznaczanie elementow mniejszych od mediany
            if(T[j] < pivot){
                i++;
                swap(T,i,j);
            }
        }
        int k = i;
        for(int j = k; j < R; j++){     //wyznaczanie elementow rownych medianie
            if(T[j] == pivot){
                k++;
                swap(T,k,j);
            }
        }
        swap(T,k+1, R);
        if(index < i){              //element jest w grupie mniejszych od mediany
            return i + 1;
        }
        if(index <= k){             //element jest w grupie rownych medianie
            return (k+i)/2 + 1;
        }
        return k + 1;               //element jest w grupie wiekszych od mediany
    }
    public static void InsertionSort(int[] T, int L, int R){
        int j;
        int tmp;
        for(int i = L; i < R + 1; i+=1){
            j  = i - 1;
            tmp = T[j+1];
            while(j >= L && T[j] > tmp){
                T[j+1] = T[j];
                j--;
            }
            T[j+1] = tmp;
        }
    }
    public static void main(String[] args) {
        int l = sc.nextInt(); //liczba zestawow danych
        int n,m;
        int res,tmp;
        int[] T;
        for(int i = 0; i < l; i++){
            n = sc.nextInt();
            T = new int[n];
            for(int j = 0; j < n; j++){
                T[j] = sc.nextInt();
            }
            m = sc.nextInt();
            for(int j = 0; j < m; j++){
                tmp = sc.nextInt();
                if(n < tmp || tmp < 1){
                    System.out.println(tmp + " brak");
                    continue;
                }
                res = Select(T,0,n-1,tmp - 1);
                System.out.println(tmp + " " + res);
            }
        }
    }
}


/*
test.in
5
20
161 124 136 170 41 66 180 109 254 253 106 155 179 131 31 10 17 79 114 62
22
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21
10
0 0 0 0 0 0 0 0 0 0
3
1 5 10
20
20 19 18 17 16 15 14 13 12 11 11 12 13 14 15 16 17 18 19 20
5
1 5 10 15 20
10
1 1 1 2 2 2 3 3 3 0
11
15 1 2 3 4 5 6 7 8 9 10
30
18 20 31 18 12 25 12 49 25 49 47 1 50 7 35 8 49 46 4 22 43 2 16 40 29 26 25 49 39 36
30
1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30

test.out
0 brak
1 10
2 17
3 31
4 41
5 62
6 66
7 79
8 106
9 109
10 114
11 124
12 131
13 136
14 155
15 161
16 170
17 179
18 180
19 253
20 254
21 brak
1 0
5 0
10 0
1 11
5 13
10 16
15 18
20 20
15 brak
1 0
2 1
3 1
4 1
5 2
6 2
7 2
8 3
9 3
10 3
1 1
2 2
3 4
4 8
5 8
6 12
7 12
8 18
9 18
10 20
11 22
12 25
13 25
14 25
15 25
16 26
17 29
18 31
19 35
20 36
21 39
22 40
23 43
24 46
25 47
26 49
27 49
28 49
29 49
30 50
 */