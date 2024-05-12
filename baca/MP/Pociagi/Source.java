//Rafal Plizga 5
import java.util.Scanner;
/*
Algorytm wykonujacy operacje na jednokierunkowej wiazanej liscie pojedynczej (lista pociagow) składającej się z dwukierunkowych
list wiazanych (listy wagonow). Wszystkie operacje za wyjątkiem Display oraz Trains odbywaja sie w czasie O(1) nie liczac
pomocniczego znalezienia danego pociagu.
 */
class Wagon{
    /*
    wagon (node), zawiera w sobie informacje o nazwie (name) oraz referencje do nastepnego/poprzedniego wagonu (next/prev)
    */
    public Wagon(String W){
        name = W;
    }
    public void Swap() {    //pomocnicza metoda zamieniajaca wskazniki
        Wagon tmpWagon = prev;
        prev = next;
        next = tmpWagon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrev(Wagon prev) {
        this.prev = prev;
    }

    public void setNext(Wagon next) {
        this.next = next;
    }

    public String getName(){
        return name;
    }

    public Wagon getNext() {
        return next;
    }

    public Wagon getPrev() {
        return prev;
    }

    private String name; //nazwa wagonu
    private Wagon prev;  //referencja na poprzedni wagon
    private Wagon next;  //referencja na nastepny wagon
}
class WagonList {
    /*
    jednostronna dwukierunkowa wiazna lista wagonow, podczas tworzenia nowego pociagu referencja first zostaje
    przypisana wagonHead
    */
    WagonList(String W){
        wagonHead = new Wagon(W);
        wagonHead.setNext(wagonHead);
        wagonHead.setPrev(wagonHead);
    }

    public void setWagonHead(Wagon wagonHead) {
        this.wagonHead = wagonHead;
    }

    public Wagon getWagonHead() {
        return wagonHead;
    }
    private Wagon wagonHead;         // head listy wagonow
}
class Train{
    /*
    pociag (node) zawierajcy w sobie informacje o nazwie (name), refernecje na nastepny pociag (next) oraz refernecje do listy
    wagonow (first)
     */
    public Train(String T, String W ){      //konstruktor pociagu
        name = T;                           //przypisanie nazwy
        next = null;                        //nastepnik narazie pusty;
        wagonList = new WagonList(W);
        first = wagonList.getWagonHead();    //tworzymy nowa liste i glowie przypisujemy ref first
    }
    public void setNext(Train next) {
        this.next = next;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setFirst(Wagon first) {
        this.first = first;
    }
    public void setWagonList(WagonList wagonList) {
        this.wagonList = wagonList;
    }
    public String getName(){
        return name;
    }
    public Train getNext() {
        return next;
    }
    public Wagon getFirst() {
        return first;
    }
    public WagonList getWagonList() {
        return wagonList;
    }
    public String toString() {
        StringBuilder res = new StringBuilder();
        Wagon tmpWagon1 = getFirst();
        Wagon tmpWagon2 = getFirst().getPrev();
        Wagon lastWagon = getFirst().getPrev();
        res.append(getName()).append(": ").append(getFirst().getName());
        while(!tmpWagon1.equals(lastWagon)){
            if(tmpWagon1.getPrev().equals(tmpWagon2)){
                tmpWagon2 = tmpWagon1;
                tmpWagon1 = tmpWagon1.getNext();
                res.append(" ").append(tmpWagon1.getName());
            }
            else{
                tmpWagon2 = tmpWagon1;
                tmpWagon1 = tmpWagon1.getPrev();
                res.append(" ").append(tmpWagon1.getName());
            }
        }
        return res.toString();
    }

    private String name;     //nazwa pociagu
    private Train next;      //referencja na nastepny pociag
    private Wagon first;     //refererncja na pierwszy wagon
    private WagonList wagonList;
}
class TrainList{
    /*
    jednostronna jednokierunkowa wiazana lista pociagow, referencja trains wskazuje na pierwszy pociag
    */

    /*
    poczatkowo nasza lista jest pusta
     */
    public TrainList(){
        trains = null;
    }

    /*
    Metoda New (dolaczajaca nowy pociag do listy). Zloznosc samego wstawienia wynosi O(1).
     */
    public void New(String T, String W){
        if(isEmpty()){                          //jesli lista jest pusta, dolacz pierwszy pociag
            trains = new Train(T, W);
            return;
        }
        Train tmpTrain = findActual(T);
        if(tmpTrain != null){
            System.out.println("Train " + T + " already exists");
        }
        else{
            tmpTrain = trains;
            trains = new Train(T, W);
            trains.setNext(tmpTrain);
        }
    }

    /*
    Metoda InsertFirst dolaczajaca nowy wagon na poczatek wskazanego pociagu. Zloznosc samego wstawienia wynosi O(1).
     */
    public void InsertFirst(String T, String W){

        Train tmpTrain = findActual(T);
        if(tmpTrain == null) {
            System.out.println("Train " + T + " does not exist");
        }
        else {
            Wagon wagon = new Wagon(W);         //tworzymy nowy wagon

            wagon.setNext(tmpTrain.getFirst());       //jego nastepnikiem jest first
            wagon.setPrev(tmpTrain.getFirst().getPrev());  //jego poprzednikiem jest last

            tmpTrain.getFirst().getPrev().setNext(wagon);   //nastepnikiem last jest wagon
            tmpTrain.getFirst().setPrev(wagon);      //poprzednikiem first jest wagon

            tmpTrain.setFirst(wagon);            //nowym first jest wagon
        }
    }

    /*
    Metoda InsertLast dolaczajaca nowy wagon na koniec wskazanego pociagu, algorytm jest identyczny jak InsertNew, ale
    nie ustawiamy nowego wagonu jako first. Zloznosc samego wstawienia wynosi O(1).
    */
    public void InsertLast(String T, String W){
        Train tmpTrain = findActual(T);
        if(tmpTrain == null){
            System.out.println("Train " + T + " does not exist");
        }
        else {
            Wagon wagon = new Wagon(W);

            wagon.setNext(tmpTrain.getFirst());
            wagon.setPrev(tmpTrain.getFirst().getPrev());

            tmpTrain.getFirst().getPrev().setNext(wagon);
            tmpTrain.getFirst().setPrev(wagon);       //nowym last jest wagon
        }
    }

    /*
    Metoda Trains wypisujaca aktualna liste pociagow. Zloznosc wynosi O(n), gdzie n to liczba pociagow.
   */
    public void Trains(){

        Train tmpTrain = trains;
        System.out.print("Trains: ");
        while(tmpTrain != null){
            System.out.print(tmpTrain.getName()+" ");
            tmpTrain = tmpTrain.getNext();
        }
        System.out.println();
    }

    /*
    Metoda Display wypisujaca aktualna liste wagonow w zadanym pociagu. Algorytm wykorzystuje fakt ze dowolny pociag,
    niewazne jak zmodyfikowany (Reverse, Union, itd.) ma pewne niezmienniki:
    -poprzednikiem first jest last
    -nastepnikiem last jest first
    Ustawienie wskaznikow pomiedzy first oraz last moze byc dowolne. Algorytm polega na tym, ze przechodzimy po liscie
    wagonow zapamietujac informacje o poprzednim wypisanym. Jesli wskaznik wagonu pokazuje na poprzedni wypisany, wiemy
    ze musimy przepiac go w druga strone, wypisac oraz przejsc dalej. Zloznosc wynosi O(n), gdzie n to liczba wagonow.
     */
    public void Display(String T){
        Train tmpTrain = findActual(T);
        if(tmpTrain == null){
            System.out.println("Train " + T + " does not exist");
            return;
        }
        System.out.println(tmpTrain);
    }

    /*
    Metoda Reverse zamieniajaca kolejnosc wagonow w danym pociagu. Algorytm zamienia wskazniki pierwszego i ostatniego
    wagonu, a nastepnie przestawia first na ostatni wagon. Zloznosc samego odwrocenia wynosi O(1).
     */
    public void Reverse(String T){
        Train tmpTrain = findActual(T);
        if(tmpTrain == null){
            System.out.println("Train " + T + " does not exist");
            return;
        }
        Wagon lastWagon = tmpTrain.getFirst().getPrev();
        lastWagon.Swap();
        tmpTrain.getFirst().Swap();
        tmpTrain.setFirst(lastWagon);
    }

    /*
    Metoda Union laczaca dwa dane pociagi i usuwajaca jeden z nich. Zloznosc samego polaczenia wynosi O(1).
     */
    public void Union(String T1, String T2){

        /*
        warunki istnienia danych pociagow
        */
        Train tmpTrain1 = findActual(T1);
        Train tmpTrain2 = findPrev(T2); // potrzebujemy poprzednika, poniewaz musimy usunac T2
        if(tmpTrain1 == null) {
            System.out.println("Train " + T1 + " does not exist");
            return;
        }
        if(tmpTrain2 == null) {
            System.out.println("Train " + T2 + " does not exist");
            return;
        }

        /*
        warunek na wypadek gdyby istnial tylko jeden pociag
        */
        if(tmpTrain2.getNext() == null){
            trains = null;
            return;
        }

        /*
        przypadek gdy T2 nie znajduje sie na poczatku listy pociagow
         */
        if(!trains.getName().equals(T2)) {
            Wagon lastWagonT1 = tmpTrain1.getFirst().getPrev();     //ostatni wagon z lewego pociagu
            Wagon lastWagonT2 = tmpTrain2.getNext().getFirst().getPrev(); //ostatni wagon z prawego pociagu

            lastWagonT1.setNext(tmpTrain2.getNext().getFirst());
            lastWagonT2.setNext(tmpTrain1.getFirst());

            tmpTrain2.getNext().getFirst().setPrev(lastWagonT1);
            tmpTrain1.getFirst().setPrev(lastWagonT2);

            tmpTrain2.setNext(tmpTrain2.getNext().getNext());   //usuniecie T2 z listy pociagow
            return;
        }

        /*
        przypadek gdy T2 znajduje sie na poczatku listy pociagow
        */

        Wagon lastWagonT1 = tmpTrain1.getFirst().getPrev();  //ostatni wagon z lewego pociagu
        Wagon lastWagonT2 = tmpTrain2.getFirst().getPrev(); //ostatni wagon z prawego pociagu

        lastWagonT1.setNext(tmpTrain2.getFirst());
        lastWagonT2.setNext(tmpTrain1.getFirst());

        tmpTrain2.getFirst().setPrev(lastWagonT1);
        tmpTrain1.getFirst().setPrev(lastWagonT2);


        tmpTrain2.setNext(tmpTrain2.getNext());           //usuniecie T2 z listy pociagow
    }

    /*
   Metoda DelFirst usuwajaca pierwszy wagon z pociagu T1 i tworzaca z niego pociag T1. Zlozonosc samego usuniecia wynosi O(1)
    */
    public void DelFirst(String T1, String T2){

        /*
        warunki istnienia
         */
        Train tmpTrain1 = findPrev(T1);     //poprzednik bo moze byc konieczne usuniecie T1
        Train tmpTrain2 = findActual(T2);
        if(tmpTrain1 == null) {
            System.out.println("Train " + T1 + " does not exist");
            return;
        }
        if(tmpTrain2 != null) {
            System.out.println("Train " + T2 + " already exists");
            return;
        }
        /*
        przypadek gdy T1 nie znajduje sie na poczatku listy pociagow
         */

        if(!T1.equals(trains.getName())) {
            /*
            przypadek gdy w T1 znajduje sie jeden wagon
             */
            if (tmpTrain1.getNext().getFirst().equals(tmpTrain1.getNext().getFirst().getNext())) {
                New(T2, tmpTrain1.getNext().getFirst().getName());
                tmpTrain1.setNext(tmpTrain1.getNext().getNext());
                return;
            }
            /*
            przypadek gdy w T1 jest wiecej wagonow
             */
            Wagon firstWagon = tmpTrain1.getNext().getFirst().getNext();    //nastepnik first, ktory bedzie nowym first
            Wagon lastWagon = tmpTrain1.getNext().getFirst().getPrev();    //ostatni wagon
            /*
            sprawdznie czy poprzednik nowego first wskazywal na first, jesli nie to zamien wskazniki
             */
            if(tmpTrain1.getNext().getFirst().equals(tmpTrain1.getNext().getFirst().getNext().getNext())) {
                firstWagon.Swap();
            }
            firstWagon.setPrev(lastWagon);
            lastWagon.setNext(firstWagon);

            New(T2, tmpTrain1.getNext().getFirst().getName()); //stworzenie nowego pociagu
            tmpTrain1.getNext().setFirst(firstWagon);  //ustanowienie firstWagon jako poczatek listy wagonow
            return;
        }
        /*
        przypadek gdy T1 znajduje sie na poczatku listy, algorytm jest identyczny ale inaczej odwolujemy sie do T1
         */
        if(T1.equals(tmpTrain1.getName())) {

            if (tmpTrain1.getFirst().equals(tmpTrain1.getFirst().getNext())) {
                trains = tmpTrain1.getNext();
                New(T2, tmpTrain1.getFirst().getName());
                return;
            }

            Wagon firstWagon = tmpTrain1.getFirst().getNext();
            Wagon lastWagon = tmpTrain1.getFirst().getPrev();
            if(tmpTrain1.getFirst().equals(tmpTrain1.getFirst().getNext().getNext())) {
                firstWagon.Swap();
            }
            firstWagon.setPrev(lastWagon);
            lastWagon.setNext(firstWagon);

            New(T2, tmpTrain1.getFirst().getName());
            tmpTrain1.setFirst(firstWagon);
        }
    }

    /*
    Metoda DelLast usuwajaca ostatni wagon z pociagu T1 i tworzaca z niego pociag T1. Zlozonosc samego usuniecia wynosi O(1)
    */
    public void DelLast(String T1, String T2){

        /*
        warunki istnienia
         */
        Train tmpTrain1 = findPrev(T1);
        Train tmpTrain2 = findActual(T2);
        if(tmpTrain1 == null) {
            System.out.println("Train " + T1 + " does not exist");
            return;
        }
        if(tmpTrain2 != null) {
            System.out.println("Train " + T2 + " already exists");
            return;
        }

        /*
        przypadek gdy T1 nie znajduje sie na poczatku listy
         */
        if(!tmpTrain1.getName().equals(T1)) {

            /*
            przypadek gdy w T1 znajduje sie jeden wagon
             */
            if (tmpTrain1.getNext().getFirst().equals(tmpTrain1.getNext().getFirst().getNext())) {
                New(T2, tmpTrain1.getNext().getFirst().getName());
                tmpTrain1.setNext(tmpTrain1.getNext().getNext());
                return;
            }

             /*
            przypadek gdy w T1 jest wiecej wagonow
             */
            New(T2, tmpTrain1.getNext().getFirst().getPrev().getName());    //stworzenie nowego pociagu
            Wagon firstWagon = tmpTrain1.getNext().getFirst();     //pierwszy wagon
            Wagon tmpWagon2 = tmpTrain1.getNext().getFirst().getPrev().getPrev();  //poprzednik ostatniego wagonu

            /*
            sprawdznie czy poprzednik nowego first wskazywal na first, jesli nie to zamien wskazniki
             */
            if(tmpTrain1.getNext().getFirst().getPrev().getPrev().getPrev().equals(tmpTrain1.getNext().getFirst().getPrev())) {
                tmpWagon2.Swap();
            }

            firstWagon.setPrev(tmpWagon2);
            tmpWagon2.setNext(firstWagon);
            return;
        }

        /*
        przypadek gdy T1 znajduje sie na poczatku listy, algorytm jest identyczny ale inaczej odwolujemy sie do T1
         */
        if (tmpTrain1.getFirst().equals(tmpTrain1.getFirst().getNext())) {
            trains = tmpTrain1.getNext();
            New(T2, tmpTrain1.getFirst().getName());
            return;
        }
        New(T2, tmpTrain1.getFirst().getPrev().getName());
        Wagon tmpWagon1 = tmpTrain1.getFirst();
        Wagon tmpWagon2 = tmpTrain1.getFirst().getPrev().getPrev();

        if(tmpTrain1.getFirst().getPrev().getPrev().getPrev().equals(tmpTrain1.getFirst().getPrev())) {
            tmpWagon2.Swap();
        }

        tmpWagon1.setPrev(tmpWagon2);
        tmpWagon2.setNext(tmpWagon1);
    }

    /*
    pomocnicza metoda findPrev zwracajaca poprzednik zadanego pociagu, lub trains jesli zadany pociag jest na poczatku listy,
    szczegolnie pomocna przy usuwaniu pociagu z listy
     */
    private Train findPrev(String T){
        if(isEmpty()){
            return null;
        }
        if(trains.getName().equals(T)){
            return trains;
        }
        Train tmpTrain = trains;
        while(tmpTrain.getNext() != null){
            if(tmpTrain.getNext().getName().equals(T)){
                return tmpTrain;
            }
            tmpTrain = tmpTrain.getNext();
        }
        return null;
    }

    /*
    pomocnicza metoda findActual zwracajaca zadany pociag
     */
    private Train findActual(String T){
        Train tmpTrain = trains;
        while(tmpTrain != null){
            if(tmpTrain.getName().equals(T)){
                return tmpTrain;
            }
            tmpTrain = tmpTrain.getNext();
        }
        return null;
    }

    /*
    pomocnicza metoda isEmpty sprawdzajaca czy pociag jest pusty
     */
    private boolean isEmpty(){
        if (trains == null){
            return true;
        }
        return false;
    }


    private Train trains;  //referencja wskazujaca na pierwszy pociag na liscie
}
public class Source {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        TrainList T = new TrainList();
        int z = sc.nextInt();
        String tmp;
        TrainList trainList = new TrainList();
        for(int i = 0; i<z; i++){
            int n = sc.nextInt();
            trainList = new TrainList();
            for(int j=0; j<n+1; j++){
                tmp = sc.nextLine();
                switch (tmp.split(" ")[0]){
                    case "New" : trainList.New(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                    case "InsertFirst" : trainList.InsertFirst(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                    case "InsertLast" : trainList.InsertLast(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                    case "Display" : trainList.Display(tmp.split(" ")[1]); break;
                    case "Trains" : trainList.Trains(); break;
                    case "Reverse" : trainList.Reverse(tmp.split(" ")[1]); break;
                    case "Union" : trainList.Union(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                    case "DelFirst" : trainList.DelFirst(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                    case "DelLast" : trainList.DelLast(tmp.split(" ")[1],tmp.split(" ")[2]); break;
                }
            }
        }
    }
}

/*

test.in
wejscie:
1
26
New T1 a2
InsertFirst T1 a1
InsertFirst T1 a0
InsertLast T1 a3
InsertLast T1 a4
InsertLast T1 a5
Display T1
New T1 b
New T2 b1
InsertFirst T2 b0
InsertLast T2 b2
InsertLast T2 b3
Display T2
Trains
Reverse T1
Reverse T2
Display T1
Display T2
Union T1 T2
Display T1
Reverse T1
Display T1
DelFirst T1 T2
DelLast T1 T3
Display T1
Trains

wyjscie:
T1: a0 a1 a2 a3 a4 a5
Train T1 already exists
T2: b0 b1 b2 b3
Trains: T2 T1
T1: a5 a4 a3 a2 a1 a0
T2: b3 b2 b1 b0
T1: a5 a4 a3 a2 a1 a0 b3 b2 b1 b0
T1: b0 b1 b2 b3 a0 a1 a2 a3 a4 a5
T1: b1 b2 b3 a0 a1 a2 a3 a4
Trains: T3 T2 T1

 */