// Rafal Plizga 5
import java.util.Scanner;
 class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int qPartition (int[] T, int L, int R) {
        /*
    Pomocnicza metoda qPartition do wyznaczania punktu podzialowego. W tej implementacji bierzemy zawsze prawy przedzial.
    W petli zamieniamy miejscami elementy tak, aby po zakonczeniu dzialania algorytmu wszystkie mniejsze elementy byly
    na lewo od punktu podzialowego.
     */
        int pivot = T[R];
        int i = L-1;
        int tmp;
        for (int j=L; j<R; j++)
        {
            if (T[j]<pivot)
            {
                i++;
                tmp=T[i];
                T[i]=T[j];
                T[j]=tmp;
            }
        }
        tmp=T[i+1];
        T[i+1]=T[R];
        T[R]=tmp;
        return i + 1;
    }
    public static void qSort(int[] T, int L, int R) {
        /*
    Algorytm QuickSort. Rekurencyjnie sortujemy lewa i prawa strone punktu podzialowego, az dojdziemy do tablic
    jednoelementowych, ktore sa juz posortowane (warunek stopu). Zlozonosc algorytmu wynosi srednio O(n*log(n)), a w
    przypadku pesymistycznym O(n^2).
     */
        if (L<R)
        {
            int q = qPartition(T,L,R);
            qSort(T,L,q-1);
            qSort(T,q+1,R);
        }
    }
    public static int binarySearch(int[] T, int begin, int end, int sum, int index){
        /*
        Algorytm BinarySearch. Szukamy najwiekszego indexu elementu o wartosci mneiszej niz zadana suma.
        Np.{1,2,2,2,3,4} dla sumy rownej 3 zwroci nam index 3. Zasada dzialania algorytmu opiera sie metodzie
        dziel i zwyciezaj. Korzystajac z faktu, ze tablica jest posortowana mozemy przechodzic rekurencyjnie
        na prawo lub lewo, az dojdziemy do tablicy jednoelementowej (warunek stopu). Zlozonosc algorytmu wynosi
        O(log(n)).
         */
        if(end >= begin) {
            if (end - begin == 0) {
                if (T[begin] < sum) {
                    index = begin;
                }
                return index;
            } else {
                int q = (end - begin) / 2;
                if (T[q] < sum) {
                    index = q;
                    return binarySearch(T, q + 1, end, sum, index);
                } else if (T[q] >= sum) {
                    return binarySearch(T, begin, q, sum, index);
                }
            }
            return -1;
        }
        return -1;
    }
    public static  String counterTriangles(int[] T){
        /*
        Glowny algorytm do zliczania i zwracania wspolrzednych kolejnych trojkatow. Na poczatku sortujemy tablice.
        Nastepnie przechodzimy w podwojnej petli (modyfikujemy dwa pierwsze punkty). Potem korzystajac z wyszukiwania
        binarnego mozemy znalezc najwiekszy index elementu o wartosci niz suma T[i]+T[j]. Wykorzystujaac dodatkowo
        fakt, ze wszystkie na lewo od tego indexu rowniez spelniaja warunek trojkata mozemy dokladnie obliczyc
        ilosc trojkatow oraz ich wspolrzedne. Wypisywanie wspolrzednych jest ograniczeno do max. 10, zatem jest to
        staly wspolczynnik i nie zwieksza istotnie zlozonosci.
        Zlozonosc pesymistyczna calego algorytmu to O(n*log(n)+O(n)+O(n)*O(n)*O(log(n))*10 = O(n^2*log(n)).
         */
        int n = T.length;
        int licznik = 0;
        int sum;
        int k;
        int tmp;
        String result="";
        qSort(T,0,n-1);

        /*
        Wypisanie posortowanej tablicy
         */
        for(int i=0 ;i<T.length; i++){
            if(i%25==0){
                result+="\n";
            }
            result+=T[i]+" ";
        }
        result+="\n";

        for(int i=0; i<n-2; i++){
            for(int j=i+1; j<n-1; j++){
                sum = T[i]+T[j];
                /*
                 przypadek gdy aktulna suma jest wieksza od ostatniego elementu w tablicy,
                 mozemy zczytac wszystkie pozsotałe indexy
                 */
                if(sum>T[n-1]){
                    tmp = licznik;
                    licznik+=n-j-1;
                    if(licznik<=10){
                       for(int a = j+1; a<n; a++){
                           result+="("+i+","+j+","+a+") ";
                       }
                    }
                    if(tmp<=10 && licznik >10){
                        for(int a = j+1; a<10-tmp+j+1; a++){
                            result+="("+i+","+j+","+a+") ";
                        }
                    }
                    sum = T[j+1];
                }
                /*
                przypadek gdy przynajmniej jeden element jest mniejszy od aktualnej sumy
                 */
                if(sum>T[j+1]) {
                    k = binarySearch(T, j + 1, n - 1, sum, n - 1);
                    tmp = licznik;
                    licznik+=k-j;
                    if(licznik<=10){
                        for(int a = j+1; a<k+1; a++){
                            result+="("+i+","+j+","+a+") ";
                        }
                    }
                    if(tmp<=10 && licznik >10){
                        for(int a = j+1; a<10-tmp+j+1; a++){
                            result+="("+i+","+j+","+a+") ";
                        }
                    }
                }
            }
        }
        if(licznik == 0){
            result+="Triangles cannot be built";
            result+="\n";
            return result;
        }
        result+="\n";
        result+="Number of triangles: "+licznik+"\n";

        return result;
    }



    public static void main(String[] args) {

        int n,m;
        int[] T;
        n = sc.nextInt();
        for(int i=1; i<=n; i++){
            m = sc.nextInt();
            T = new int[m];
            for(int j=0; j<m; j++){
                T[j] = sc.nextInt();
            }
            System.out.print(i+": n= "+m);
            System.out.print(counterTriangles(T));
        }
    }
}

/*
Przykladowy test
test0.in

7
3
1 1 1
10
1 2 3 4 5 6 7 8 9 10
4
2 2 1 5
5
2 3 2 3 1
6
1 2 1 2 2 1
7
5 4 3 3 3 2 1
7
1 2 2 3 3 3 3

test0.out

1: n=3
1 1 1
(0,1,2)
Number of triangles: 1
2: n=10
1 2 3 4 5 6 7 8 9 10
(1,2,3) (1,3,4) (1,4,5) (1,5,6) (1,6,7) (1,7,8) (1,8,9) (2,3,4) (2,3,5) (2,4,5)
Number of triangles: 50
3: n=4
1 2 2 5
(0,1,2)
Number of triangles: 1
4: n=5
1 2 2 3 3
(0,1,2) (0,3,4) (1,2,3) (1,2,4) (1,3,4) (2,3,4)
Number of triangles: 6
5: n=6
1 1 1 2 2 2
(0,1,2) (0,3,4) (0,3,5) (0,4,5) (1,3,4) (1,3,5) (1,4,5) (2,3,4) (2,3,5) (2,4,5)
Number of triangles: 11
6: n=7
1 2 3 3 3 4 5
(0,2,3) (0,2,4) (0,3,4) (1,2,3) (1,2,4) (1,2,5) (1,3,4) (1,3,5) (1,4,5) (1,5,6)
Number of triangles: 20
 */