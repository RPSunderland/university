// Rafal Plizga – 5
import java.util.Scanner;

public class MaxTab2D {
    public static Scanner scn = new Scanner(System.in);

    public static String maxTab1D(int[] T){

        /*
        Metoda pomocnicza implementująca algorytm Kadana do wyliczania sumy maksymalnej podtablicy
        jednowymiarowej. Metoda zwraca również indeksy tej podtablicy.
        Złozonosc metody to O(m)
         */

        int maxSum=T[0];       //zmienna przechowujaca wynik (suma maksymalnej podtablicy) //ewentualnie maxSum/actualSum = T[0];
        int actualSum=T[0];    //zmienna przechowujaca sume aktualnej podtablicy
        int n=T.length;      //dlugosc tablicy
        int beginIndex=0;   // index poczatkowy koncowej podtablicy
        int endIndex=0;     //index koncowy koncowej podatblicy
        int tmp=0;          //zmienna przechowujaca poczatkowy indeks aktualnej podtablicy
        boolean flag = true; //pomocnicza flaga dla sytuacji krancowych

        for(int i=0; i<n; i++) {
            actualSum += T[i];
            /*
            Przypadek, gdy nadpisujemy maksymalna sume
             */
            if (maxSum < actualSum) {
                maxSum = actualSum;
                beginIndex = tmp;
                endIndex = i;
            }

            /*
            Przypadek, gdy natrafiamy na tą samą sumę
             */
            if (actualSum == maxSum) {
                /*
                gdy natrafiamy na mniejszą podtablice
                 */
                if (endIndex - beginIndex > i - tmp) {
                    beginIndex = tmp;
                    endIndex = i;
                }
                /*
                gdy natrafiamy na pierwsze 0, np. w tablicy {-1,-1,0,3}
                 */
                else if(endIndex - beginIndex == i - tmp && maxSum == 0 && flag){
                    flag = false;
                    beginIndex = tmp;
                    endIndex = i;
                    tmp++;
                }
                /*
                gdy natrafiamy na kolejne 0 jak w tablicy {0,0,1}
                 */
                else if(endIndex - beginIndex == i - tmp && maxSum == 0 && !flag){
                    tmp++;
                }
            }
            /*
            Przypadek, gdy "resetujemy" aktualną podtablicę
             */
            if (actualSum <= 0) {
                actualSum = 0;
                tmp = i + 1;
            }

        }
        return maxSum+" "+beginIndex+" "+endIndex;
    }
    public static String maxTab2D(int[][]T){

        /*
        Metoda glowwna obliczjaca sume maksymalnej podtablicy 2D. Metoda zwraca równiez indexy poczatkowych i koncowych
        wierszy/kolumn. Glowny algorytm polega na porownywaniu wszystkich wierszy w petlach for. Dla kazdej porownywanej
        pary wierszy sumujemy kolumny i wrzucamy je do pomocniczej tablicy tmpSum. Następnie wykonujemy na niej
        metode maxTab1D i porownujemy z maxSum. Złozonosc metody wynosi O(n^2*m)<=O(max|n^3,m^3|)
         */

        int n=T.length;     //zmienna przechowująca długosc kolumny
        int m=T[0].length;  //zmienna przechowująca dlugosc wiersza
        boolean P = true;   //flaga do sprawdzenia czy wszystkie elemnty sa nieujemne
        boolean N = true;   //flaga do sprawdzenia czy wszystkie elementy sa ujemne
        int maxSum = 0;         //zmienna przechowujaca wynik (sume maksymalnej podtablicy)
        int beginRowIndex=0;    //index poczatkowy wiersza
        int endRowIndex=0;      //index koncowy wiersza
        int beginColumIndex=0;  //index poczatkowy kolumny
        int endColumnIndex=0;   //index koncowy kolumny
        int actualSum = 0;      //aktualna suma (dla pary wierszy)
        int actualBeginColumn = 0; //aktualny index poczatkowy kolumny(ddla pary wierszy)
        int actualEndColumn = 0;    //aktualny index koncowy kolumny (dla pary wierszy)
        int[] tmpSum = new int[m];  //tablica pomocnicza do przechowywania sum kolumn
        boolean flag = true; //flaga dla sytuacji krancowych

        /*
        Pojednycza petla do sprawdzenia flag,   O(m)
         */

        for(int j=0; j<m; j++){
            if(T[0][j]<=0){P = false;}
            if(T[0][j]>=0){N = false;}
        }


        /*
        Sprawdzenie flag w całej tablicy oraz przesumowanie poszczegolnych elementow w kolumnach i zapisanie
        tej informacji w elementach, by przyspieszyć obliczenia, O(n*m)
         */

        for(int i=1; i<n; i++){
            for(int j=0; j<m; j++){
                if(T[i][j]<=0){P = false;}
                if(T[i][j]>=0){N = false;}
                T[i][j]+=T[i-1][j];
            }
        }

        /*
        Sprawdzenie flag
         */
        if(N){return "X";}
        if(P){
            int s = 0;
            for(int i = 0; i<m; i++){

                s += T[n - 1][i];

            }
            return s+" "+0+" "+Integer.toString(n-1)+" "+0+" "+Integer.toString(m-1);
        }


        /*
        Przechodzenie po wierszach O(n^2)
         */

        for(int i = 0; i<n; i++){
            for(int j = i; j<n; j++){

                /*
                Sumowanie kolumn    O(m)
                 */

                for(int k=0; k<m; k++){
                    if(i>0){
                        tmpSum[k] = T[j][k]-T[i-1][k];
                    }
                    else{
                        tmpSum[k] = T[j][k];
                    }
                }
                actualSum = Integer.parseInt(maxTab1D(tmpSum).split(" ")[0]);
                actualBeginColumn = Integer.parseInt(maxTab1D(tmpSum).split(" ")[1]);
                actualEndColumn = Integer.parseInt(maxTab1D(tmpSum).split(" ")[2]);

                /*
                Przypadek, gdy nadpisujemy maksymalna podtablice
                 */
                if(actualSum>maxSum){
                    maxSum=actualSum;
                    beginRowIndex = i;
                    endRowIndex = j;
                    beginColumIndex = actualBeginColumn;
                    endColumnIndex = actualEndColumn;
                }
                /*
                Przypadek, gdy trafiamy na ta sama sume w mniejszej podtablicy
                 */
                else if(actualSum == maxSum){
                    if((endRowIndex-beginRowIndex)+(endColumnIndex-beginColumIndex)>(j-i)+(actualEndColumn-actualBeginColumn)) {
                        beginRowIndex = i;
                        endRowIndex = j;
                        beginColumIndex = actualBeginColumn;
                        endColumnIndex = actualEndColumn;
                    }
                    else if(endRowIndex-beginRowIndex == j-i && maxSum==0 && flag){
                        beginRowIndex = i;
                        endRowIndex = j;
                        beginColumIndex = actualBeginColumn;
                        endColumnIndex = actualEndColumn;
                        flag = false;
                    }
                }
            }
        }
        return maxSum+" "+beginRowIndex+" "+endRowIndex+" "+beginColumIndex+" "+endColumnIndex;
    }

    public static void main(String[] args){
        int l;
        int n,m;
        String tmp;
        l = scn.nextInt();
        for(int i=0; i<l; i++) {

            scn.next();
            scn.next();
            n = scn.nextInt();
            m = scn.nextInt();
            int[][] T = new int[n][m];

            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    T[j][k] = scn.nextInt();
                }
            }


            tmp = maxTab2D(T);
            if(tmp.equals("X")){
                System.out.println((i+1)+": n="+n+" m="+m+", ms="+0+", mstab is empty");
            }
            else{
                int s = Integer.parseInt(tmp.split(" ")[0]);
                int a = Integer.parseInt(tmp.split(" ")[1]);
                int b = Integer.parseInt(tmp.split(" ")[2]);
                int c = Integer.parseInt(tmp.split(" ")[3]);
                int d = Integer.parseInt(tmp.split(" ")[4]);
                System.out.println((i+1)+": n = "+n+" m = "+m+", s = "+s+", mst = a["+a+".."+b+"]["+c+".."+d+"]");
            }
        }
        scn.close();
    }
}

/*
Przykladowy input oraz output:
Wejscie:
20

1 : 1 1
-1

2 : 1 5
-1 -1 -2 -3 -4

3 : 3 5
 -2 -1 -1 -1 -1
 -4 -2 -2 -1 -1
 -3 -4 -5 -6 -7

4 : 1 1
 0

5 : 1 4
 0 0 0 0

 6 : 5 1
 0
 0
 0
 0
 0

7 : 3 6
-1 -2 -3 -4 -5 -1
-2 0 -3 -2 -4 0
0 0 0 0 0 0

8 : 3 4
-1 -1 -1 -891
0 0 -2 -3
0 0 -2 0

9 : 1 1
69

10 : 1 6
0 0 0 1 0 0

11 : 4 1
0
0
1
0

12 : 2 4
-1 -1 1 0
1 0 0 0

13 : 3 4
-1 1 1 1
1 1 -1 -1
1 1 1 1

14 : 4 4
1 1 1 -2
1 1 1 -3
1 1 1 -4
-2 -3 -4 9

15 : 4 6
-1 1 1 1 8 -8
-2 3 -4 5 -6 4
-1 0 0 0 -2 3
-1 4 5 -6 7 8

16 : 4 5
-1 -1 -3 -4 -5
-2 -2 1 1 1
1 1 1 1 -4
1 1 -3 -3 -3

17 : 2 2
-1 0
0 0

18 : 2 4
-3 2 2 -7
-4 0 1 -5

19 : 6 6
1 0 0 0 0 0
0 1 0 0 0 0
0 0 1 0 0 0
0 0 0 1 0 0
0 0 0 0 1 0
0 0 0 0 0 1

20 : 5 4
3 0 -1 2
2 -1 3 -4
-3 4 3 -1
-1 -2 4 -5
-2 2 -2 2


Wyjscie:
1: n=1 m=1, ms=0, mstab is empty
2: n=1 m=5, ms=0, mstab is empty
3: n=3 m=5, ms=0, mstab is empty
4: n = 1 m = 1, s = 0, mst = a[0..0][0..0]
5: n = 1 m = 4, s = 0, mst = a[0..0][0..0]
6: n = 5 m = 1, s = 0, mst = a[0..0][0..0]
7: n = 3 m = 6, s = 0, mst = a[1..1][1..1]
8: n = 3 m = 4, s = 0, mst = a[1..1][0..0]
9: n = 1 m = 1, s = 69, mst = a[0..0][0..0]
10: n = 1 m = 6, s = 1, mst = a[0..0][3..3]
11: n = 4 m = 1, s = 1, mst = a[2..2][0..0]
12: n = 2 m = 4, s = 1, mst = a[0..0][2..2]
13: n = 3 m = 4, s = 6, mst = a[0..2][0..3]
14: n = 4 m = 4, s = 9, mst = a[3..3][3..3]
15: n = 4 m = 6, s = 24, mst = a[0..3][1..5]
16: n = 4 m = 5, s = 4, mst = a[1..2][2..3]
17: n = 2 m = 2, s = 0, mst = a[0..0][1..1]
18: n = 2 m = 4, s = 5, mst = a[0..1][1..2]
19: n = 6 m = 6, s = 6, mst = a[0..5][0..5]
20: n = 5 m = 4, s = 11, mst = a[1..3][1..2]

 */


