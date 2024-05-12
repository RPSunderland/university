// Rafal Plizga  5

/*
Program sortujacy dane w postaci CSV po wybranej kolumnie. Poniewaz mamy na wejsciu dwa rodzaje danych do posortowania
(kolumne napisow, lub liczb) zostalo zaimplementowane dwa rodzaje sortowan (dla nazw wykorzystujacy metode compareTo oraz
dla liczb klasyczny porzadek). W obu metodach idea jest identyczna. Aby uniknac uzywania stosu w algorytmie QuickSort
korzystamy z faktu, ze liczby sa dodatnie zatem wartosc prawego przedzialu mozemy ustawic na ujemny i wrocic do niego
w nasteonym wywolaniu. Wartosc lewego przedzialu mozemy wyznaczyc przy pomocy prawego (korzystamy z metody Lomuto, wiec bedzie
to R+2). Zlozonosc metody jest identyczna co w przypadku zwyklego QuickSort (jest nieco wolniejszy, poniewaz musimy odnalezc
index prawego konca). Srednia zlozonosc czasowa wynosi O(n*log(n)), a pamieciowa O(1).
 */
import java.util.Scanner;
public class Source {
    public static Scanner sc = new Scanner(System.in);

    /*
    Metody pomocnicze (swapujace elementy w tablicy) oraz nadpisana metoda compareTo dla stringBuilderow
     */
    public static int compareTo(StringBuilder a, StringBuilder b){
        for(int i = 0; i < Math.min(a.length(), b.length()); i++){
            if(a.charAt(i) < b.charAt(i)){
                return -1;
            }
            if(a.charAt(i) > b.charAt(i)){
                return 1;
            }
        }
        if(a.length() > b.length()){
            return 1;
        }
        if(a.length() < b.length()){
            return -1;
        }
        return 0;
    }
    public static void swapS(String[] mainTab, StringBuilder[] stringsTab, int i, int j){
        StringBuilder tmp1 = stringsTab[i];
        stringsTab[i] = stringsTab[j];
        stringsTab[j] = tmp1;

        String tmp2 = mainTab[i];
        mainTab[i] = mainTab[j];
        mainTab[j] = tmp2;
    }
    public static void swapI(String[] mainTab, int[] numbersTab, int i, int j){
        int tmp1 = numbersTab[i];
        numbersTab[i] = numbersTab[j];
        numbersTab[j] = tmp1;

        String tmp2 = mainTab[i];
        mainTab[i] = mainTab[j];
        mainTab[j] = tmp2;
    }

    /*
    Sortowanie tablicy z nazwami
     */
    public static int qPartitionString(String[] mainTab, StringBuilder[] stringsTab, int L, int R, int order){
        /*
        Algorytm wyznacznia podzialu w wersji Lomuto zastosowany dla stringow, metoda sprawdza uwzgeldnia rowniez
        porzadek (malejacy/rosnacy)
         */
        StringBuilder pivot = stringsTab[R];
        int i = L-1;
        if(order == 1) {
            for (int j = L; j < R; j++) {
                if (compareTo(stringsTab[j],pivot) <= 0) {
                    i++;
                    swapS(mainTab, stringsTab, i, j);
                }
            }
            swapS(mainTab, stringsTab, i + 1, R);
            return i + 1;
        }
        else{
            for (int j = L; j < R; j++) {
                if (compareTo(stringsTab[j],pivot) >= 0) {
                    i++;
                    swapS(mainTab, stringsTab, i, j);
                }
            }
            swapS(mainTab, stringsTab, i + 1, R);
            return i + 1;
        }
    }
    public static void QuickSortString(String[] mainTab, StringBuilder[] stringsTab, int L, int R, int order){
        int i = 0;  //ilosc elementow na "stosie"
        while(L < R || i > 0) // dopoki istnieje podzial, lub sa elementy na "stosie" wykonuj
        {
            //(wejscie do metody)
            if(R-L>=5) //jesli podzial ma wiecej niz 5 elementow
            {
                int q = qPartitionString(mainTab, stringsTab, L, R, order); //wyznaczenie punktu podzialu
                if(q < R) { //zaznaczenie prawego konca (dopisanie na poczatku 0)
                    stringsTab[R].append("0");
                }
                R = q - 1;  //aktualizacja prawego konca i ponowne wywolanie
                i++;
                continue;
            }
            else    //jesli podzial ma mniej niz 6 elementow
            {
                SelectionSortString(mainTab,stringsTab,L,R,order); //wywolanie SelectionSort aby posortowac podzial
                L = R + 2; //zaktualizowanie lewego konca (q+1)
                R = L;  //poszukiwanie prawego konca
                while(R < stringsTab.length) {
                    if (stringsTab[R].charAt(stringsTab[R].length()-1) == '0') {
                        stringsTab[R].deleteCharAt(stringsTab[R].length()-1);
                        break;
                    }
                    R++;
                }
                if(R == stringsTab.length){
                    R--;
                }
                i--;    //wyrzucenie elementu ze "stosu"
            }
        }
        // tablica jest juz posortowana
    }
    public static void SelectionSortString(String[] mainTab, StringBuilder[] stringsTab, int L, int R, int order){
        /*
        SelectionSort zastosowany dla stringow, metoda uwzgeldnia rowniez porzadek (rosnacy/malejacy)
         */
        if(L < R) {
            int min;
            if (order == 1) {
                for (int i = L; i < R + 1; i++) {
                    min = i;
                    for (int j = i + 1; j < R + 1; j++) {
                        if (compareTo(stringsTab[j],stringsTab[min]) < 0) {
                            min = j;
                        }
                    }
                    swapS(mainTab,stringsTab, i, min);
                }
            } else {
                for (int i = L; i < R + 1; i++) {
                    min = i;
                    for (int j = i + 1; j < R + 1; j++) {
                        if (compareTo(stringsTab[min],stringsTab[j]) < 0) {
                            min = j;
                        }
                    }
                    swapS(mainTab,stringsTab, i, min);
                }
            }
        }
    }

    /*
    Sortowanie tablicy z liczbami
     */
    public static int qPartitionInt(String[] mainTab, int[] numbersTab, int L, int R, int order){
        /*
        Algorytm wyznacznia podzialu w wersji Lomuto zastosowany dla liczb, metoda sprawdza uwzgeldnia rowniez
        porzadek (malejacy/rosnacy)
         */
        int pivot = numbersTab[R];
        int i = L-1;
        if(order == 1) {
            for (int j = L; j < R; j++) {
                if (numbersTab[j] <= pivot) {
                    i++;
                    swapI(mainTab,numbersTab, i, j);
                }
            }
            swapI(mainTab,numbersTab, i + 1, R);
            return i + 1;
        }
        else{
            for (int j = L; j < R; j++) {
                if (numbersTab[j] >= pivot) {
                    i++;
                    swapI(mainTab,numbersTab, i, j);
                }
            }
            swapI(mainTab,numbersTab, i + 1, R);
            return i + 1;
        }
    }
    public static void QuickSortInt(String[] mainTab, int[] numbersTab, int L, int R, int order){
        int i = 0;  //liczba elementow na "stosie"
        while(L < R || i > 0)   // dopoki istnieje podzial, lub sa elementy na "stosie" wykonuj
        {
            //(wejscie do metody)
            if(R-L >= 5) //jesli podzial ma wiecej niz 5 elementow
            {
                int q = qPartitionInt(mainTab,numbersTab, L, R, order); //wyznaczenie punktu podzialu
                if(q < R) { //zaznaczenie prawego konca (zmiana znaku na przeciwny)
                    numbersTab[R] = -numbersTab[R];
                }
                R = q - 1;  //aktualizacja prawego konca i ponowne wywolanie
                i++;
            }
            else    //jesli podzial ma mniej niz 6 elementow
            {
                SelectionSortInt(mainTab,numbersTab,L,R,order); //wywolanie SelectionSort, aby posortowac podzial
                L = R + 2; //zaktualizowanie lewego konca (q+1)
                R = L;  //poszukiwanie prawego konca
                while(R < numbersTab.length) {
                    if (numbersTab[R] < 0) {
                        numbersTab[R] = -numbersTab[R];
                        break;
                    }
                    R++;
                }
                if(R == numbersTab.length){
                    R--;
                }
                i--;    //wyrzucenie elemntu ze "stosu"
            }
        }
        // tablica jest juz posortowana
    }
    public static void SelectionSortInt(String[] mainTab, int[] numbersTab,int L, int R, int order) {
         /*
        SelectionSort zastosowany dla liczb, metoda uwzglednia rowniez porzadek (rosnacy/malejacy)
         */
        if (L < R) {
            int min;
            if (order == 1) {
                for (int i = L; i < R + 1; i++) {
                    min = i;
                    for (int j = i + 1; j < R + 1; j++) {
                        if (numbersTab[j] < numbersTab[min]) {
                            min = j;
                        }
                    }
                    swapI(mainTab,numbersTab, i, min);
                }
            } else {
                for (int i = L; i < R + 1; i++) {
                    min = i;
                    for (int j = i + 1; j < R + 1; j++) {
                        if (numbersTab[min] < numbersTab[j]) {
                            min = j;
                        }
                    }
                    swapI(mainTab,numbersTab, i, min);
                }
            }
        }
    }

    /*
    main
     */
    public static void main(String[] args) {
        int n = sc.nextInt(); //liczba zestawow danych
        String headLine; //informacja na temat liczby wierszy, kolumn, kolumnie sortowanej oraz porzadku
        String[] mainTab; //tablica do przechowywania wierszy
        StringBuilder[] stringsTab; //tablica do przechowywania sortowanej kolumny nazw
        int[] numbersTab;   //tablica do przechowywyania sortowanej kolumny liczb
        int rows; //liczba wierszy w zestawie
        int col; //kolumna po ktorej sortujemy
        int order; //porzadek (rosnacy/malejacy)
        int rowLength; //dlugosc wiersza
        boolean isNumber; //flaga sprawdzjaca czy string jest liczba
        sc.nextLine();
        for(int i=0; i<n; i++){

            /*
            Zapisanie informacji o liczbie wierszy, kolumn, kolumnie sortowanej oraz porzadku
             */
            headLine = sc.nextLine();
            mainTab = headLine.split(",");
            rows = Integer.parseInt(mainTab[0]);
            col = Integer.parseInt(mainTab[1]);
            order = Integer.parseInt(mainTab[2]);

            /*
            Zczytanie pierwszych dwoch lini i sprawdzenie czy bedziemy sortowac nazwy, czy liczby
             */
            mainTab = new String[rows];
            headLine = sc.nextLine();
            rowLength = headLine.split(",").length;
            mainTab[0] = sc.nextLine();
            try{
                Integer.parseInt(mainTab[0].split(",")[col - 1]);
                isNumber = true;
            }
            catch (NumberFormatException e){
                isNumber = false;
            }

            /*
            Sortowanie kolumny z liczbami
             */
            if(isNumber){
                numbersTab = new int[rows];
                numbersTab[0] = Integer.parseInt(mainTab[0].split(",")[col - 1]);
                for (int j = 1; j < rows; j++) {
                    mainTab[j] = sc.nextLine();
                    numbersTab[j] = Integer.parseInt(mainTab[j].split(",")[col - 1]);
                }
                QuickSortInt(mainTab,numbersTab, 0, numbersTab.length - 1, order);
                System.out.print(headLine.split(",")[col - 1]);
                for (int j = 0; j < col - 1; j++) {
                    System.out.print("," + headLine.split(",")[j]);
                }
                for (int j = col; j < rowLength; j++) {
                    System.out.print("," + headLine.split(",")[j]);
                }
                System.out.println();

                for (int j = 0; j < rows; j++) {
                    System.out.print(numbersTab[j]);
                    for (int k = 0; k < col - 1; k++) {
                        System.out.print("," + mainTab[j].split(",")[k]);
                    }
                    for (int k = col; k < rowLength; k++) {
                        System.out.print("," + mainTab[j].split(",")[k]);
                    }
                    System.out.println();
                }
            }

             /*
            Sortowanie kolumny z nazwami
             */
            else {
                stringsTab = new StringBuilder[rows];
                stringsTab[0] = new StringBuilder(mainTab[0].split(",")[col - 1]);
                for (int j = 1; j < rows; j++) {
                    mainTab[j] = sc.nextLine();
                    stringsTab[j] = new StringBuilder(mainTab[j].split(",")[col - 1]);
                }
                QuickSortString(mainTab, stringsTab, 0, stringsTab.length - 1, order);
                System.out.print(headLine.split(",")[col - 1]);
                for (int j = 0; j < col - 1; j++) {
                    System.out.print("," + headLine.split(",")[j]);
                }
                for (int j = col; j < rowLength; j++) {
                    System.out.print("," + headLine.split(",")[j]);
                }
                System.out.println();

                for (int j = 0; j < rows; j++) {
                    System.out.print(stringsTab[j]);
                    for (int k = 0; k < col - 1; k++) {
                        System.out.print("," + mainTab[j].split(",")[k]);
                    }
                    for (int k = col; k < rowLength; k++) {
                        System.out.print("," + mainTab[j].split(",")[k]);
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }
}

/*
test.in
5
1,1,1
A,B,C,D,E,F,G,H,I,J
test,1,1,1,1,1,1,1,1,test
5,5,-1
A,B,C,D,E
cat,krzak,13,dog,4
int,glass,1,math,10
topologia,analiza,4,algebra,2
geometria,java,2,python,3
quant,kula,13,ergo,2048
10,2,1
Litery,Liczby
a,10
b,9
c,8
d,7
e,6
f,5
g,4
h,3
i,2
j,1
10,2,-1
Imie,Nazwisko,Id
John,Oshoe,64
Mike,Sunderland,50
Frank,Travis,53
The,Architect,32
Christopher,Falcone,26
Mr,Cali,1
Arthur,Crat,17
Donald,McGain,44
John,Kane,19
Jimmy,Livero,77
10,3,1
Imie,Nazwisko,Id
John,Oshoe,64
Mike,Sunderland,50
Frank,Travis,53
The,Architect,32
Christopher,Falcone,26
Mr,Cali,1
Arthur,Crat,17
Donald,McGain,44
John,Kane,19
Jimmy,Livero,77

test.out
A,B,C,D,E,F,G,H,I,J
test,1,1,1,1,1,1,1,1,test

E,A,B,C,D
2048,quant,kula,13,ergo
10,int,glass,1,math
4,cat,krzak,13,dog
3,geometria,java,2,python
2,topologia,analiza,4,algebra

Liczby,Litery
1,j
2,i
3,h
4,g
5,f
6,e
7,d
8,c
9,b
10,a

Nazwisko,Imie,Id
Travis,Frank,53
Sunderland,Mike,50
Oshoe,John,64
McGain,Donald,44
Livero,Jimmy,77
Kane,John,19
Falcone,Christopher,26
Crat,Arthur,17
Cali,Mr,1
Architect,The,32

Id,Imie,Nazwisko
1,Mr,Cali
17,Arthur,Crat
19,John,Kane
26,Christopher,Falcone
32,The,Architect
44,Donald,McGain
50,Mike,Sunderland
53,Frank,Travis
64,John,Oshoe
77,Jimmy,Livero

 */
