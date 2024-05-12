// Rafal Plizga 5
import java.util.Scanner;
/*
Program implementujacy dwie funkcje (rekurencyjna oraz iteracyjna bedaca symulacja tej pierwszej, wykorzystujaca stos)
rozwiazujace problem wyznaczania zadanej sumy podzbioru. Argumentami funkcji jest tablica z elementami o poszczegolnych
wagach, pomocnicza tablica zapisujaca elementy ktore spelniaja rozwiazanie, index aktualnie rozpatrywanego elementu w
tablicy oraz oczekiwana suma.
 */

public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static boolean recPakuj(int[] T, boolean[] tmp, int index, int sum){
        /*
    Rekurencyjna wersja funkcji. Mechanizm dzialania jest nastepujacy:
    -bierzemy dany element
    -sprawdzamy czy pozostala suma wynosi 0 (jesli tak to zwroc true i zakoncz dzialanie programu)
    -sprawdzamy czy jest to ostatni element (jesli tak i jednoczesnie rozni sie od sumy to oznacza ze nie znalezlismy
    takiego ciagu elementow, wiec zwracamy false (jednoczesnie ustawiamy elementy w tablicy pomocniczej)
    -jesli nasz element jest wiekszy niz zadana suma, przejdz dalej
    -jesli nasz element jest mniejszy niz zadana suma, sprawdzamy czy pozostaly ciag zawierajacy ten element bedzie
    poprawny:
        a) jesli tak (zwroc true)
        b) jesli nie (przejdz dalej)
    Po wykonaniu programu mamy dostep do tablicy pomocniczej, w ktorej wartosci true oznaczaja elementy ktore sa
    rozwiazaniem. Zlozonosc metody, jako ze potencjalnie musimy sprawdzic wszystkie kombinacje wynosi O(2^n).
     */
        if(sum == 0){ //stage 0
            return true;
        }
        if(T.length - index == 1){
            if(T[index] == sum){
                tmp[index] = true;
                return true;
            }
            tmp[index] = false;
            return false;
        }
        if(T[index] > sum){
            tmp[index] = false;
            return recPakuj(T,tmp,index+1,sum); // stage 1
        }
        if(recPakuj(T,tmp,index+1,sum-T[index])){ //stage 2
            tmp[index] = true;
            return true;
        }
        return recPakuj(T,tmp,index+1,sum); //stage 1
    }
    public static boolean iterPakuj(int[] T, boolean[] tmp, int index, int sum){
        /*
        Iteracyjna wersja funkcji. Korzystamy w niej z wczesniej zdefiniowanej struktury (Parameters) zawierajacej
        informacje o indexie, sumie i aktualnym stanie. Nastepnie tworzymy stos i wrzucamy wejsciowe parametry.
        Dopoki nie oproznimy stosu, parametry beda sie zmieniac.
         */
        boolean retVal = false; //wartosc zwracana przez funkcje (w polaczeniu z continue/break symulacja return)
        Stack snapshotStack = new Stack(100);  //stos parametrow
        Parameters currentParameters = new Parameters();    //dane poczatkowe
        currentParameters.index = index;
        currentParameters.sum = sum;
        currentParameters.stage = 0;
        snapshotStack.push(currentParameters);  //wstawienie poczatkowego parametru na stos

        while(!snapshotStack.isEmpty()){    //dopoki stos nie jest pusty wykonuj:
            currentParameters = snapshotStack.pop();    //zdejmujemy parametr ze stosu i sprawdzamy stan
            switch (currentParameters.stage) {
                case 0: //stage 0, stan po wywolaniu metody
                    if(currentParameters.sum == 0){
                        retVal = true;
                        continue;
                    }
                    if(T.length - currentParameters.index == 1){
                        if(T[currentParameters.index] == currentParameters.sum){
                            tmp[currentParameters.index] = true;
                            retVal = true;
                            continue;
                        }
                        tmp[currentParameters.index] = false;
                        retVal = false;
                        continue;
                    }

                    if(T[currentParameters.index] > currentParameters.sum){
                        tmp[currentParameters.index] = false;
                        currentParameters.stage = 1;    //zmiana stanu (wywolanie rekurencyjne)
                        snapshotStack.push(currentParameters);  //wstawiamy parametr na stos
                        Parameters nextParameters = new Parameters(); //tworzymy nowy paramter, ustawiamy wspolczynniki i wrzucamy na stos
                        nextParameters.index = currentParameters.index+1;
                        nextParameters.sum = currentParameters.sum;
                        nextParameters.stage = 0;
                        snapshotStack.push(nextParameters);
                        continue;
                    }
                    currentParameters.stage = 2;    //zmiana stanu (wywolanie rekurencyjne w if-ie)
                    snapshotStack.push(currentParameters);
                    Parameters nextParameters = new Parameters();
                    nextParameters.index = currentParameters.index+1;
                    nextParameters.sum = currentParameters.sum - T[currentParameters.index];
                    nextParameters.stage = 0;
                    snapshotStack.push(nextParameters);
                   break;
                case 1: //stage 1, w tym stanie po prostu przechodzimy dalej
                    break;
                case 2: //stage 2, w tym stanie sprawdzamy if-a, w ktorego weszlismy w stanie 0.
                    if(retVal){
                        tmp[currentParameters.index] = true;
                        continue;
                    }
                    currentParameters.stage = 1;    //wywolaj sie
                    snapshotStack.push(currentParameters);
                    nextParameters = new Parameters();
                    nextParameters.index = currentParameters.index+1;
                    nextParameters.sum = currentParameters.sum;
                    nextParameters.stage = 0;
                    snapshotStack.push(nextParameters);
                    break;
            }
        }
        return retVal;
    }
    public static void main(String[] args) {
        int n;
        int k;
        int S;
        int[] T;
        boolean[] tRec;
        boolean[] tIter;
        n = sc.nextInt();
        for(int i=0; i<n; i++){
            S = sc.nextInt();
            k = sc.nextInt();
            T = new int[k];
            tRec = new boolean[k];
            tIter = new boolean[k];
            String res1 = "";
            String res2 = " ";
            for(int j=0; j<k; j++){ //inicjalizacja tablic
                T[j] = sc.nextInt();
                tIter[j] = false;
                tRec[j] = false;
            }
            if(recPakuj(T,tRec,0,S)){
                for(int j =0 ; j < k; j++){
                    if(tRec[j]){
                        res1 += " " + T[j];
                    }
                }
                iterPakuj(T,tIter,0,S);
                for(int j =0 ; j < k; j++){
                    if(tIter[j]){
                        res2 += " " + T[j];
                    }
                }
                System.out.println("REC: " + S + " =" + res1);
                System.out.println("ITER: " + S + " =" + res2);
                continue;
            }
            System.out.println("BRAK");
        }
    }
    static class Parameters{
        public int index;
        public int sum;
        public int stage;
    }
    static class Stack{
        public Stack(int n){
            T = new Parameters[n];
            top = -1;
            max = n;
        }
        public void push(Parameters x){
            if(top+1 >= max){
                //System.out.println("StackC overflow");
                return;
            }
            ++top;
            T[top] = x;
        }
        public Parameters pop(){
            Parameters x;
            if(top>=0) {
                x = T[top];
                top--;
            }
            else{
                //System.out.println("StackC underflow");
                return null;
            }
            return x;
        }
        public Parameters seek(){
            if(top > -1) {
                return T[top];
            }
            return null;
        }
        public boolean isEmpty(){
            if(top == -1){
                return true;
            }
            return false;
        }
        private Parameters[] T;
        private int top;
        private int max;
    }
}
/*

test.in
7
1
5
2 3 4 5 6
13
4
3 2 4 1
10
4
1 2 3 4
13
4
3 8 7 6
4
5
1 4 5 2 3
39
8
10 6 1 9 7 3 4 5
7
3
4 5 1

test.out
BRAK
BRAK
REC: 10 = 1 2 3 4
ITER: 10 = 1 2 3 4
REC: 13 = 7 6
ITER: 13 = 7 6
REC: 4 = 1 3
ITER: 4 = 1 3
REC: 39 = 10 6 9 7 3 4
ITER: 39 = 10 6 9 7 3 4
BRAK

 */
