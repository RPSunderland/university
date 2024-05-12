//Rafal Plizga 5
/*
Rekurencyjny algorytm tasujacy podany na wejsciu ciag wyrazow. Idea bazuje na algorytmie QuickSort, mozna utozsamic problem
potasowania piosnek z posortowaniem danej tablicy {0,2,4,6,...,2n,1,3,5,7,...,2n+1}, lub {0,2,4,6,...,2n,2n+2,1,3,5,7,...,2n+1}.
Jako punkt podzialu bierzemy punkt na dlugosci 3/4 tablicy. Dzieki czemu bedziemy miec w przyblizeniu rowne podzialy, co oznacza
ze zlozonosc bedzie O(n*log(n)). Pomysl wykorzytsuje rowniez fakt ze tablice po podzieleniu zachowuja taka sama strukture jak
tablica wyjsciowa (sa postaci {2k,2k+2,...,2k+2i,2k+1,2k+3,...,2k+2i+1}, lub {2k,2k+2,...,2k+2i,2k+2i+2,2k+1,2k+3,...,2k+2i+1}).
 */
import java.util.Scanner;
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static int minTwo(int a, int b){
        /*
         Metoda pomocnicza zwracajaca minimum z dwoch podanych liczb. Zlozonosc metody O(1).
         */
        if(a <= b){
            return a;
        }
        return b;
    }
    public static int minThree(int a, int b, int c){
        /*
        Metoda pomocnicza zwracajaca minimum z trzech podanych liczb. Zlozonosc metody O(1).
         */
        if(a <= b){
            if(a <= c){
                return a;
            }
            return c;
        }
        if(b <= c){
            return b;
        }
        return c;
    }
    public static void swap(String[] T, int i, int j){
        /*
        Metoda pomocnicza zamieniajaca elementy w danej tablicy. Zlozonosc metody wynosi O(1).
         */
        String tmp = T[i];
        T[i] = T[j];
        T[j] = tmp;
    }
    public static void insert(String[] T, int i, int j){
        /*
        Metoda pomocnicza wstawiajaca j-ty element w tablicy przed i-ty, zakladamy ze i < j. Zlozonosc metody wynosi O(n).
         */
        if(i < j){
            String tmp = T[j];
            while(i < j){
                T[j] = T[j-1];
                j--;
            }
            T[i] = tmp;
        }
    }
    public static int qPartition(String[] T, int L, int R){
        /*
        Metoda pomocnicza wyznaczajaca punkt podzialu oraz sortujaca tablice w taki sposob, ze elementy mniejsze od pivota
        beda po lewej stronie, a elementy wieksze po prawej. Metoda opiera sie na wyznaczeniu pewnych punktow szczegolnych
        (pivot, mid, start). Nastepnie przechodzimy od srodka do pivota zamieniajac elementy (wieksze od pivota z lewej strony tablicy
        z mniejszymi od pivota z prawej strony tablicy. Na koniec korzystamy z metody insert, aby wstawic pivot we wlasciwe miejsce.
        Ustawiamy nowa wartosc pivota i go zwracamy. Zlozonosc metody wynosi O(n), poniewaz przechodzimy po jednej petli a metoda
        insert wywola sie max 2 razy.
         */
       if(R-L == 1){    //przypadek gdy tablica ma 2 elementy, np. {0,1} -> {0,1}, pivot = 1
           return R;
       }
       if(R-L == 2){    //przypadek gdy tablica ma 3 elementy, np. {0,2,1} -> {0,1,2}, pivot = 1
           swap(T,L+1,R);
           return L+1;
       }
       if(R-L == 3){    //przypadek, gdy tablica ma 4 elementy, np. {0,2,1,3} -> {0,2,1,3}, pivot = 3
           return R;
       }
       int pivot = L + ((R-L)*3)/4 + 1; //punkt podzialu, zawsze po prawej stronie w 3/4 dlugosci tablicy
       int mid = L + (R-L)/2;           //punkt srodkowy (ostatni element po lewej stronie)
       int start = L + pivot - mid;     //punkt startowy (element od ktorego zaczynamy swapowanie)
       int i =0;
       while(mid+1+i != pivot-1){         //dopoki nie dojdziemy do poprzednika pivota zamieniaj elementy
          swap(T,start+i,mid+1+i);
          i++;
       }
       if(start+i > mid){               //jesli przekroczylismy z lewej strony, to wstaw ostatni element
           insert(T,start+i,mid+1+i);
           i++;
       }
       else {                           //jesli nie przekroczylismy zamien ostatnie elementy
           swap(T,start+i,mid+1+i);
           i++;
       }
       if(start+i > mid){               //jesli przekroczylismy z lewej strony, to wstaw pivot we wlasciwe miejsce
           insert(T,start+i,pivot);
       }
       else{                            //jesli nie przekroczylismy zamien pivot
           swap(T, start+i,pivot);
       }
        pivot = start+i;                //zaktualizuj i zwroc pivot
        return pivot;
    }
    public static String qSort(String[] T, int L, int R){
        /*
        Rekurencyjna metoda tasujaca podane liste stringow oraz zwracajaca ich najkrotszy wspolny przedrostek. Korzystajac z metody
        qPartition dzielimy nasza tablice na w przyblizeniu rowne przedzialy. I wyznaczamy najkrotszy prefiks lewej czesci,
        punktu podzialowego oraz prawej czesci. Dla tablicy jednoelementowej najkrotszym wspolnym prefiksem jest ten element.
        W przypadku niewywolania sie metody qPartition zwracamy znak specjalny, aby uniknac bledow (pusty string oznacza brak prefiksu).
        Zlozonosc metody jest podobna jak QuickSort w przypadku optymistycznym, zatem O(n*log(n)).
         */
        if(L == R){ //dla tablicy jednoelementowej zwroc ten element
            return T[L];
        }
        if(L < R) { //jesli tablica ma wiecej elementow
            int q = qPartition(T, L, R);    //punkt podzialu
            String res0 = T[q]; //prefiks punktu podzialu (zawsze istnieje)
            String res1 = qSort(T, L, q - 1); //prefiks lewej czesci tablicy
            String res2 = qSort(T, q + 1, R); //prefiks prawej czesci tablicy
            String res = "";    //zmienna porzechowujaca koncowy prefiks
            int l;              //dlugosc najkrotszego prefiksu z dostepnych
            if(res1.equals("") || res2.equals("")){ //jesli jakis prefiks jest pusty, zwroc pusty prefiks
                return "";
            }
            if(res1.equals("#")){   //znak specjalny z lewej strony, np. res1 = qSort(T,0,-1)
                l = minTwo(res0.length(),res2.length());
                for(int i =0 ;i<l; i++){
                    if(res0.charAt(i)!=res2.charAt(i)){ //przerwij jesli znajdziemy roznice w prefiksach
                        break;
                    }
                    res+=res0.charAt(i);
                }
                return res;
            }
            if(res2.equals("#")){       //znak specjalny z prawej strony, np. res1 = qSort(T,3,2)
                l = minTwo(res0.length(),res1.length());
                for(int i =0 ;i<l; i++){
                    if(res0.charAt(i)!=res1.charAt(i)){  //przerwij jesli znajdziemy roznice w prefiksach
                        break;
                    }
                    res+=res1.charAt(i);
                }
                return res;
            }
            //przypadek gdy wszystkie trzy prefiksy istnieja
            l = minThree(res0.length(),res1.length(),res2.length());
            for(int i =0 ;i<l; i++){
                if(res0.charAt(i)!=res1.charAt(i) || res0.charAt(i)!=res2.charAt(i) || res1.charAt(i)!=res2.charAt(i)){  //przerwij jesli znajdziemy roznice w prefiksach
                    break;
                }
                res+=res1.charAt(i);
            }
            return res;
        }
        return "#"; //jesli nie ma elementow w tablicy zwroc znak specjalny
   }
    public static void main(String[] args) {
        int n = sc.nextInt();
        int m;
        String[] T;
        String res;
        for(int i=0; i<n; i++){
            m = sc.nextInt();
            T = new String[m];
            for(int j=0; j<m; j++){
                T[j] = sc.next();
            }
            res = qSort(T,0,m-1);
            System.out.print(T[0]);
            for(int j=1; j<m; j++){
                System.out.print(" "+T[j]);
            }
            System.out.println();
            System.out.println(res);
        }
    }
}
/*
test.in
3
20
0 2 4 6 8 10 12 14 16 18 1 3 5 7 9 11 13 15 17 19
5
Ala Aleksanader Albert Al Albatros
6
Sunderland Sernik Slowacja Sanki Kruk Ser

test.out
0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19

Ala Al Aleksanader Albatros Albert
Al
Sunderland Sanki Sernik Kruk Slowacja Ser

 */
