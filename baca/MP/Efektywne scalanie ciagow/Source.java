// Rafal Plizga 5
import java.util.Scanner;
/*
Program sortujacy podane na wejsciu n posortowanych ciagow majacych co najwyzej m elementow. Idea algorytmu polega na
wykorzystaniu kopca MIN w ktorym wierzcholek ma najmniejsza wartosc(reprezentowanego prze tablice).
Zaimplementowano rowniez kolejke do efektywnego operowania na ciagach roznych dlugosci. Ogolna zlozonsoc algorytmu
wynosi O(m*nlog(n)). Algorytm polega na tym ze wstawiamy pierwsze wyrazy wyrazu ciagow i tworzymy z nich kopiec wielkosci n.
Nastepnie wyciagamy wierzcholek (odczytujemy z jakiego ciagu pochodzil) wrzucamy do wynikowej tablicy i wstawiamy do kopca
jego nastepnik (jesli brak to +inf). Nastepnie przywracamy strukture kopca O(log n) i ponownie wyciagamy wierzcholek i
wstawiamy do wynikowej tablicy. Robimy to max. n*m razy. Na koniec wystarczy wypisywac po kolei elementy z wynikowej kolejki.
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);

    public static class Node{
        /*
        Pomocnicza klasa reprezentujaca sortowany element (zawiera informacje o nastepniku oraz ciagu do ktorego nalezy)
         */
        Node(int value, int index, Node next){
            this.value = value;
            this.index = index;
            this.next = next;
        }
        private int value;  //wartosc elementu
        private int index;  //index ciagu do ktorego element nalezy
        private Node next;  //referencja na nastepny element w tym ciagu
    }
    public static class Queue {
        /*
        Wiazana kolejka FIFO, wszystkie metody na niej sa czasu O(1)
         */
        public Queue(){
            front = back = null;
        }
        public void enqueue(Node node) {
            if (back == null) {
                front = back = node;
                return;
            }
            back.next = node;
            back = node;
        }
        public Node dequeue() {
            if (this.front == null){
                return null;
            }
            Node temp = front;
            front = front.next;
            if (front == null) {
                back = null;
            }
            return temp;
        }
        Node front, back;
    }
    public static void swap(Node[] T, int i, int j){
        /*
        pomocnicza metoda zamieniajca dwa elementy w tablicy
         */
        Node tmp = T[i];
        T[i] = T[j];
        T[j] = tmp;
    }

    public static int getLeft(Node[] T, int i){
        /*
        Pomocnicza metoda zwracajaca index lewego potomka w kopcu
         */
        if(2*i + 1 < T.length) {
            return 2*i + 1;
        }
        return T.length;
    }
    public static int getRight(Node[] T, int i){
         /*
        Pomocnicza metoda zwracajaca index prawego potomka w kopcu
         */
        if(2*i + 2 < T.length) {
            return 2*i + 2;
        }
        return T.length;
    }

    public static void minHeapify(Node[] T, int i, int heapSize){
        /*
        Pomocnicza metoda przywracajaca wlasnosc kopca (zakladamy ze lewe i prawe poddrzewo elemntu o indexie i sa poprawnymi
        kopcami). Zloznosc metody wynosi O(log(n))
         */
        int L = getLeft(T,i);
        int R = getRight(T,i);
        int max = i;
        if(L < T.length && T[L].value < T[i].value){
            max = L;
        }
        if(R < T.length && T[R].value < T[max].value){
            max = R;
        }
        if(max != i){
            swap(T,i,max);
            minHeapify(T, max, heapSize);
        }
    }
    public static void buildMinHeap(Node[] T){
        /*
        Metoda rekurencyjnie budujaca kopiec z podanej nieuporzadkowanej tablicy elementow. Zlozonosc metody wynosi O(n)/
         */
        for(int i = (T.length - 1)/2; i >= 0; i--){
            minHeapify(T, i, T.length);
        }
    }



    public static void main(String[] args) {
        int z = sc.nextInt();
        int n; //liczba ciagow
        int max;  //element maksymalny
        int tmp;    //pomocniczy int
        Node node;  //pomocniczy Node
        int[] L; //pomocnicza tablica zawierajaca dlugosci ciagow
        Node[] K;   //tablica reprezentujaca kopiec
        Queue[] T;   //tablica ciagow (kolejek)
        Queue res = new Queue(); //kolejka zawierajaca posortowane elementy
        for(int i = 0; i < z; i++){
            n = sc.nextInt();
            K = new Node[n];
            T = new Queue[n];
            L = new int[n];

            /*
            Odczyt najwiekszego elementu
             */
            max = sc.nextInt();
            L[0] = max;
            for(int j = 1; j < n; j++){
                tmp = sc.nextInt();
                L[j] = tmp;
                if(tmp > max){
                    max = tmp;
                }
            }

            /*
            Stworzenie tablicy ciagow
             */
            for(int j = 0; j < n; j++){
                T[j] = new Queue();
            }
            for(int j = 0; j < n; j++){
                for(int k = 0; k < L[j]; k++){
                    tmp = sc.nextInt();
                    T[j].enqueue(new Node(tmp,j,null));
                }
            }

            /*
            Wrzucenie pierwszego zestawu elementow do tablicy reprezentujacej kopiec
             */
            for(int j = 0; j < n; j++){
                K[j] = T[j].dequeue();
            }

            /*
            Budowa kopca
             */
            buildMinHeap(K);
            while(true) {
                node = K[0];
                res.enqueue(node);
                if(node.value == 1001){
                    break;
                }
                if(node.next == null){
                    K[0] = new Node(1001,-1,null);  //obiekt reprezentujacy nieskonczonosc
                    minHeapify(K,0);
                    continue;
                }
                K[0] = T[node.next.index].dequeue();
                minHeapify(K,0);
            }

            /*
            Wypisanie wyniku
             */
            node = res.dequeue();
            System.out.print(node.value);
            while(true){
                node = res.dequeue();
                if(node.value == 1001){
                    break;
                }
                System.out.print(" " + node.value);
            }
            System.out.println();
        }
    }
}

/*
test.in
5
3
1 1 1
3
2
1
5
1 2 3 4 5
5
4 5
3 4 5
2 3 4 5
1 2 3 4 5
3
5 5 5
0 0 0 0 0
0 0 0 0 0
0 0 0 0 0
5
3 5 2 7 4
1 1 7
3 5 7 8 8
0 100
2 3 5 5 7 90 101
2 3 3 3
5
4 4 9 1 9
1 1 1 1
2 5 6 8
1 3 4 6 7 8 8 8 13
1
2 2 2 3 3 3 4 4 6

test.out
1 2 3
1 2 2 3 3 3 4 4 4 4 5 5 5 5 5
0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
0 1 1 2 2 3 3 3 3 3 5 5 5 7 7 7 8 8 90 100 101
1 1 1 1 1 1 2 2 2 2 3 3 3 3 4 4 4 5 6 6 6 7 8 8 8 8 13

 */