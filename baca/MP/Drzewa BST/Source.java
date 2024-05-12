//Rafal Plizga 5
import java.util.Scanner;

/*
Program implementujacy kolejke priorytetowa przedstawiona jako drzewo BST. Program sklada sie z trzech modulow:
-edycji (create, delete)
-kolejkowania (enque, dequemax, dequemin, next, prev)
-raporotwania (inorder, preorder, postorder, height)
Operacja create tworzy rekurencyjnie nowe drzewo (na podstawie listy preorder/postorder). Zlozonosc jest liniowa poniewaz
operujemy na obiekcie index ktory nie jest zwyklym intem i bedzie inkrementowany z kazdym wywolaniee az do przejscia po tablicy.
 */
public class Source {
    public static Scanner sc = new Scanner(System.in);

    static class Person{
        /*
        Definicja klasy Person
         */
        Person(int key, String name, String surname){
            this.key = key;
            this.name = name;
            this.surname = surname;
        }
        public int key;     //priorytet danej osoby
        public String name; //imie danej osoby
        public String surname;  //nazwisko danej osoby
    }
    static class Node{
        /*
        Definicja klasy Node
         */
        Node(Person info, Node L, Node R){
            this.info = info;
            this.L = L;
            this.R = R;
        }
        public Person info; //informacja o osobie w danym wezle
        public Node L;  //referencja na lewego potomka
        public Node R;  //referencja na prawego potomka
    }
    static class Tree{

        /*
        Definicja klasy tree
         */
        public Tree(){
            root = null;
        }

        /*
        Modul edycji
         */
        public Node getPreOrderRoot(Person persons[], Index preIndex, Person person, int min, int max, int size) {
            /*
            Metoda zwracajaca node'a bedacego rootem nowego drzewa na podstawie listy osob w porzadku preorder.
            W liscie preorder pierwszy element jest rootem, nastepnie wszystkie kolejne az do napotkania wiekszego klucza
            beda w lewym poddrzewie a pozostale w prawym. Metoda sprawdza czy dany node znajduje sie podanym przedziale, ktory
            ustawiamy na poczatku na (-inf, +inf). Jesli sie nie miesci to mozemy go zwrocic nie zmieniajac indexu. Zlzonosc metody
            wynosi O(n).
             */
            if (preIndex.index >= size) {
                return null;
            }
            Node root = null;
            if (person.key > min && person.key < max) {
                root = new Node(person,null,null);
                preIndex.index = preIndex.index + 1;
                if (preIndex.index < size) {
                    root.L = getPreOrderRoot(persons, preIndex, persons[preIndex.index], min, person.key, size);
                }
                if(preIndex.index < size) {
                    root.R = getPreOrderRoot(persons, preIndex, persons[preIndex.index], person.key, max, size);
                }
            }
            return root;
        }
        public void createPreOrder(Person persons[], int size) {
            /*
            Metoda przypisujaca root drzewa do node'a zwroconego w metodzie getPreOrder z odpowiednim indexem poczatkowym
             */
            Index index = new Index();
            root = getPreOrderRoot(persons, index, persons[0], Integer.MIN_VALUE, Integer.MAX_VALUE, size);
        }
        public Node getPostOrderRoot(Person persons[], Index postIndex, Person person, int min, int max, int size){
            /*
            Analogiczna metoda do getPreeOrderRoot tylko dzialajca na liscie postorder. Root jest na koncu wiec idziemy
            od konca, nastepnie idac w lewo mamy prawepoddrzewo az do znalezienia elementu o mniejszym kluczu, a na koncu
            tworzymy lewe poddrzewo. Zlozonosc metody wynosi O(n).
             */
            if (postIndex.index < 0) {
                return null;
            }
            Node root = null;
            if (person.key > min && person.key < max) {
                root = new Node(person,null,null);
                postIndex.index = postIndex.index - 1;
                if (postIndex.index >= 0) {
                    root.R = getPostOrderRoot(persons, postIndex, persons[postIndex.index], person.key, max, size);
                }
                if(postIndex.index >= 0){
                    root.L = getPostOrderRoot(persons, postIndex, persons[postIndex.index], min, person.key, size);
                }
            }
            return root;
        }
        public void createPostOrder(Person persons[], int size) {
            /*
            Metoda przypisujaca root drzewa do node'a zwroconego w metodzie getPostOrder z odpowiednim indexem poczatkowym.
             */
            Index index = new Index();
            index.index = size - 1;
            root = getPostOrderRoot(persons, index, persons[size - 1], Integer.MIN_VALUE, Integer.MAX_VALUE, size);
        }
        public Node delete(int key){
            /*
            Iteracyjna metoda usuwajaca wezel o danym kluczu z drzewa, zlozonosc metody wynosi O(n)
             */
            if(root == null){   //jesli drzewo jest puste
                return null;
            }
            Node prev = root;
            Node actual = root;
            while(actual != null){
                if(key < actual.info.key){        //przechodzenie na lewe poddrzewo
                    prev = actual;
                    actual = actual.L;
                    continue;
                }
                if(key > actual.info.key){        //przechodzenie na prawe poddrzewo
                    prev = actual;
                    actual = actual.R;
                    continue;
                }
                if(key == actual.info.key){       //znalezienie duplikatu
                    break;
                }
            }
            if(actual == null){         //wezel o danym kluczu nie istnieje
                return null;
            }

            /*
            Przypadek gdy usuwany wezel nie ma potomkow
             */
            if(actual.L == null && actual.R == null){
                if(actual.info.key < prev.info.key){  //jesli usuwany wezel jest lewym potomkiem
                    prev.L = null;
                }
                else{   //jesli usuwany wezel jest prawym potomkiem
                    prev.R = null;
                }
                if(actual == root){
                    root = null;
                }
                return actual;
            }

            /*
            Przypadek gdy usuwany wezel ma dokladnie jednego potomka
             */
            if(actual.L == null){
                if(actual.info.key < prev.info.key){  //jesli usuwany wezel jest lewym potomkiem
                    prev.L = actual.R;
                }
                else{       //jesli usuwany wezel jest prawym potomkiem
                    prev.R = actual.R;
                }
                if(actual == root){
                    root = actual.R;
                }
                return actual;
            }
            if(actual.R == null){
                if(actual.info.key < prev.info.key){  //jesli usuwany wezel jest lewym potomkiem
                    prev.L = actual.L;
                }
                else{       //jesli usuwany wezel jest prawym potomkiem
                    prev.R = actual.L;
                }
                if(actual == root){
                    root = actual.L;
                }
                return actual;
            }

            /*
            Przypadek gdy usuwany wezel ma dwoch potomkow (zamieniamy go z jego nastepnikiem)
             */
            Node parentNext = null;
            Node next;
            next = actual.R;
            while (next.L != null){
                parentNext = next;
                next = next.L;
            }
            if(parentNext != null){
                parentNext.L = next.R;
            }
            else{
                actual.R = next.R;
            }
            actual.info = next.info;
            return root;
        }

        /*
        Modul kolejkowania
         */
        public void insert(Node node){
            /*
            Iteracyjna metoda wstawiajaca dany wezel do drzewa, zlozonosc metody wynosi O(n)
             */
            if(root == null){
                /*
                Jesli drzewo jest puste niech root bedzie wstawianym wezlem
                 */
                root = node;

                return;
            }
            Node prev = root;
            Node actual = root;
            while(actual != null){
                prev = actual;
                if(node.info.key < actual.info.key){        //przechodzenie na lewe poddrzewo
                    actual = actual.L;
                    continue;
                }
                if(node.info.key >= actual.info.key){        //przechodzenie na prawe poddrzewo
                    actual = actual.R;
                    continue;
                }
            }
            if(node.info.key < prev.info.key){  //jesli poprzednik wiekszy od wstawianego to wstawiany jest lewym potomkiem
                prev.L = node;
            }
            else{       //jesli poprzednik mniejszy od wstawianego to wstawiany jest prawym potomkiem
                prev.R = node;
            }
        }
        public Node deleteMinimum(){
            /*
            Iteracyjna metoda usuwajaca najmniejszy element. Zlozonosc metody wynosi O(n)
             */
            if(root == null){       //jesli drzewo puste to brak el. najmniejszego
                return null;
            }
            Node actual = root;
            Node prev = root;
            if(root.L == null){         //jesli lewe poddrzewo puste to el. najmniejszym jest root
                root = root.R;          //nowym rootem jest prawy potomek (jesli prawe podrzewo puste to drzewo zostaje usuniete)
                return actual;
            }
            while(actual.L != null){        //przechodz w lewo do znalezienia el. najmniejszego i go usun (moze miec max 1 potomka)
                prev = actual;
                actual = actual.L;
            }
            prev.L = actual.R;
            return actual;
        }
        public Node deleteMaximum(){
             /*
            Iteracyjna metoda usuwajaca najwiekszy element. Zlozonosc metody wynosi O(n)
             */
            if(root == null){       //jesli drzewo puste to brak el. najwiekszego
                return null;
            }
            Node actual = root;
            Node next = root;
            if(root.R == null){         //jesli prawe poddrzewo puste to el. najwiekszym jest root
                root = root.L;          //nowym rootem jest lewy potomek (jesli lewe podrzewo puste to drzewo zostaje usuniete)
                return actual;
            }
            while(actual.R != null){        //przechodz w prawo do znalezienia el. najwiekszego i go usun (moze miec max 1 potomka)
                next = actual;
                actual = actual.R;
            }
            next.R = actual.L;
            return actual;
        }
        public Node getPrev(int key){
            /*
            Iteracyjna metoda zwracajaca poprzednik (w porzadku inorder) danego elementu.
             */
            if(root == null){       //jesli drzewo puste to brak poprzednika
                return null;
            }
            Node actual = root;
            Node prev = null;
            while (actual != null){
                if(key == actual.info.key){         //jesli wartosc node'a jest rowna kluczowi to poprzednik bedzie w lewym poddrzewie (o ile istnieje)
                    if(actual.L != null){
                        prev = actual.L;
                        while (prev.R != null) {
                            prev = prev.R;
                        }
                        return prev;
                    }
                    return prev;        //brak lewego poddrzewa
                }
                if(key < actual.info.key){      //jesli wartosc node'a jest wieksza od klucza to przechodzimy do lewego poddrzewa
                    actual = actual.L;
                    continue;
                }
                if(key > actual.info.key){      //jesli wartosc node'a jest mniejsza od klucza to przechodzimy do prawego poddrzewa i nadpisujemy poprzednik
                    prev = actual;
                    actual = actual.R;
                    continue;
                }
            }
            return null;        //zwroc poprzednik lub null, jesli nie istnieje
        }
        public Node getNext(int key){
            /*
            Iteracyjna metoda zwracajaca nastepnik (w porzadku inorder) danego elementu.
             */
            if(root == null){       //jesli drzewo puste to brak nastepnika
                return null;
            }
            Node actual = root;
            Node next = null;
            while (actual != null){
                if(key == actual.info.key){         //jesli wartosc node'a jest rowna kluczowi to nastepnik bedzie w prawym poddrzewie (o ile istnieje)
                    if(actual.R != null){
                        next = actual.R;
                        while (next.L != null) {
                            next = next.L;
                        }
                        return next;
                    }
                    return next;        //brak prawego poddrzewa
                }
                if(key < actual.info.key){          //jesli wartosc node'a jest wieksza od klucza to przechodzimy do lewego poddrzewa i nadpisujemy nastepnik
                    next = actual;
                    actual = actual.L;
                    continue;
                }
                if(key > actual.info.key){          //jesli wartosc node'a jest mniejsza od klucza to przechodzimy do prawego poddrzewa
                    actual = actual.R;
                    continue;
                }
            }
            return null;        //zwroc nastepnik lub null, jesli nie istnieje
        }
        /*
        Modul raporotwania
         */
        public void inOrderDisplay(){
            /*
            Iteracyjna metoda wypisujaca drzewo w postaci inorder (lewe poddrzewo, root, prawe poddrzewo).
            Zlozonosc metody wynosi O(n)
             */
            System.out.print("INORDER: ");
            if(root != null){
                StackN stack = new StackN();
                Node actual = root;
                while (actual != null || !stack.isEmpty()){
                    while(actual != null){
                        stack.push(actual);
                        actual = actual.L;
                    }
                    actual = stack.pop();
                    if(stack.isEmpty() && actual.R == null){
                        System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname);
                        System.out.println();
                        return;
                    }
                    System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname + ", ");
                    actual = actual.R;
                }
            }
            System.out.println();
        }
        public void postOrderDisply(){
            /*
            Iteracyjna metoda wypisujaca drzewo w postaci postorder  (lewe poddrzewo, prawe poddrzewo, root).
            Zlozonosc metody wynosi O(n)
             */
            System.out.print("POSTORDER: ");
            if(root != null){
                StackN stack1 = new StackN();
                StackN stack2 = new StackN();
                Node actual = root;
                stack1.push(actual);
                while (!stack1.isEmpty()){
                    actual = stack1.pop();
                    stack2.push(actual);

                    if(actual.L != null){
                        stack1.push(actual.L);
                    }
                    if(actual.R != null){
                        stack1.push(actual.R);
                    }
                }
                while(!stack2.isEmpty()){
                    actual = stack2.pop();
                    if(stack2.isEmpty()){
                        System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname);
                        System.out.println();
                        return;
                    }
                    System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname + ", ");
                }
            }
            System.out.println();
        }
        public void preOrderDisplay(){
            /*
            Iteracyjna metoda wypisujaca drzewo w postaci preorder (root, lewe poddrzewo, prawe poddrzewo).
            Zlozonosc metody wynosi O(n)
             */
            System.out.print("PREORDER: ");
            if(root != null) {
                StackN stack = new StackN();
                Node actual = root;
                stack.push(actual);
                while (!stack.isEmpty()) {
                    actual = stack.pop();
                    if((stack.isEmpty() && actual.R == null) && actual.L == null){
                        System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname);
                        System.out.println();
                        return;
                    }
                    System.out.print(actual.info.key + " - " + actual.info.name + " " + actual.info.surname + ", ");
                    if (actual.R != null) {
                        stack.push(actual.R);
                    }
                    if (actual.L != null) {
                        stack.push(actual.L);
                    }
                }
            }
            System.out.println();
        }
        public int getHeight(Node node){
            /*
            Rekurencyjna metoda zwracajaca wysokosc drzewa. Zlozonosc metody wynosi O(n).
             */
            if(node == null){       //jesli istnieje korzen to wysokosc wyniesie 0
                return -1;
            }
            return 1 + Math.max(getHeight(node.L),getHeight(node.R));   //przechodzimy rekurenycjnie po lewym i prawym poddrzewie
        }
        private Node root;  //korzen drzewa
        private class Index {
            /*
            Klasa pomocnicza implementujaca obiekt imitujacy index (potrzebna w metodach create)
             */
            int index = 0;
        }
    }
    public static void main(String[] args) {
        Tree tree = new Tree();
        int n = sc.nextInt();
        int m;
        String operation;
        String order;
        int key;
        String name;
        String surname;
        Node tmp;
        for(int i = 0; i < n; i++){
            m = sc.nextInt();
            sc.nextLine();
            System.out.println("ZESTAW " + (i+1));
            for(int j = 0; j < m; j++){
                operation = sc.next();
                switch (operation){
                    case ("CREATE") : {
                        tree = new Tree();
                        order = sc.next();
                        int noPersons = Integer.parseInt(sc.next());
                        Person[] persons = new Person[noPersons];
                        for (int k = 0; k < noPersons; k++){
                            key = Integer.parseInt(sc.next());
                            name = sc.next();
                            surname = sc.next();
                            persons[k] = new Person(key,name,surname);
                        }
                        if(order.equals("PREORDER")){
                            tree.createPreOrder(persons,noPersons);
                        }
                        else{
                            tree.createPostOrder(persons, noPersons);
                        }
                        break;
                    }
                    case ("DELETE") : {
                        key = Integer.parseInt(sc.next());
                        tmp = tree.delete(key);
                        if(tmp == null) {
                            System.out.println("DELETE " + key + ": BRAK");
                        }
                        break;
                    }
                    case ("ENQUE") : {
                        key = Integer.parseInt(sc.next());
                        name = sc.next();
                        surname = sc.next();
                        tmp = new Node(new Person(key,name,surname),null,null);
                        tree.insert(tmp);
                        break;
                    }
                    case ("DEQUEMAX") : {
                        tmp = tree.deleteMaximum();
                        if(tmp == null){
                            System.out.println("DEQUEMAX: BRAK");
                            break;
                        }
                        System.out.println("DEQUEMAX: " + tmp.info.key + " - " + tmp.info.name + " " + tmp.info.surname);
                        break;
                    }
                    case ("DEQUEMIN") : {
                        tmp = tree.deleteMinimum();
                        if(tmp == null){
                            System.out.println("DEQUEMIN: BRAK");
                            break;
                        }
                        System.out.println("DEQUEMIN: " + tmp.info.key + " - " + tmp.info.name + " " + tmp.info.surname);
                        break;
                    }
                    case ("NEXT") : {
                        key = Integer.parseInt(sc.next());
                        tmp = tree.getNext(key);
                        if(tmp == null){
                            System.out.println("NEXT " + key + ": BRAK");
                            break;
                        }
                        System.out.println("NEXT " + key + ": " + tmp.info.key + " - " + tmp.info.name + " " + tmp.info.surname);
                        break;
                    }
                    case ("PREV") : {
                        key = Integer.parseInt(sc.next());
                        tmp = tree.getPrev(key);
                        if(tmp == null){
                            System.out.println("PREV " + key + ": BRAK");
                            break;
                        }
                        System.out.println("PREV " + key + ": " + tmp.info.key + " - " + tmp.info.name + " " + tmp.info.surname);
                        break;
                    }
                    case ("PREORDER") : {
                        tree.preOrderDisplay();
                        break;
                    }
                    case ("INORDER") : {
                        tree.inOrderDisplay();
                        break;
                    }
                    case ("POSTORDER") : {
                        tree.postOrderDisply();
                        break;
                    }
                    case ("HEIGHT") : {
                        int height = tree.getHeight(tree.root);
                        System.out.println("HEIGHT: " + height);
                        break;
                    }
                }
            }
        }
    }
    static class sNode{
        sNode(Node node){
            /*
            Klasa pomocnicza potrzebna do stowrzenia stosu Nodow
             */
            this.node = node;
            prev = null;
        }
        private Node node;
        private sNode prev;
    }
    static class StackN{
        /*
       Klasa pomocnicza implementujaca stos wiazany. Uzywany w metodach wypisujacych.
        */
        public StackN(){
            top = null;
        }
        public void push(Node x){
            sNode node = new sNode(x);
            node.prev = top;
            top = node;
        }
        public Node pop(){
            if(top != null){
                sNode tmp = top;
                top = top.prev;
                return tmp.node;
            }
            return null;
        }
        public boolean isEmpty(){
            if(top == null){
                return true;
            }
            return false;
        }
        private sNode top;
    }
}

/*

test.in
1
22
CREATE PREORDER 11 15 Mike S 6 Artur K 3 Jimmy L 2 Calig La 4 Donald MC 7 Jake Mac 13 Tokio Man 9 Pivot Har 18 Wu ZI 17 Frank T 20 Han Chan
INORDER
PREORDER
POSTORDER
HEIGHT
DELETE 14
DELETE 15
INORDER
PREORDER
ENQUE 15 Mike Sr
ENQUE 10 Wike Chi
PREV 17
PREV 7
NEXT 2
NEXT 20
DEQUEMAX
DEQUEMIN
HEIGHT
PREORDER
DELETE 6
INORDER
HEIGHT

test.out
ZESTAW 1
INORDER: 2 - Calig La, 3 - Jimmy L, 4 - Donald MC, 6 - Artur K, 7 - Jake Mac, 9 - Pivot Har, 13 - Tokio Man, 15 - Mike S, 17 - Frank T, 18 - Wu ZI, 20 - Han Chan
PREORDER: 15 - Mike S, 6 - Artur K, 3 - Jimmy L, 2 - Calig La, 4 - Donald MC, 7 - Jake Mac, 13 - Tokio Man, 9 - Pivot Har, 18 - Wu ZI, 17 - Frank T, 20 - Han Chan
POSTORDER: 2 - Calig La, 4 - Donald MC, 3 - Jimmy L, 9 - Pivot Har, 13 - Tokio Man, 7 - Jake Mac, 6 - Artur K, 17 - Frank T, 20 - Han Chan, 18 - Wu ZI, 15 - Mike S
HEIGHT: 4
DELETE 14: BRAK
INORDER: 2 - Calig La, 3 - Jimmy L, 4 - Donald MC, 6 - Artur K, 7 - Jake Mac, 9 - Pivot Har, 13 - Tokio Man, 17 - Frank T, 18 - Wu ZI, 20 - Han Chan
PREORDER: 17 - Frank T, 6 - Artur K, 3 - Jimmy L, 2 - Calig La, 4 - Donald MC, 7 - Jake Mac, 13 - Tokio Man, 9 - Pivot Har, 18 - Wu ZI, 20 - Han Chan
PREV 17: 15 - Mike Sr
PREV 7: 6 - Artur K
NEXT 2: 3 - Jimmy L
NEXT 20: BRAK
DEQUEMAX: 20 - Han Chan
DEQUEMIN: 2 - Calig La
HEIGHT: 5
PREORDER: 17 - Frank T, 6 - Artur K, 3 - Jimmy L, 4 - Donald MC, 7 - Jake Mac, 13 - Tokio Man, 9 - Pivot Har, 10 - Wike Chi, 15 - Mike Sr, 18 - Wu ZI
INORDER: 3 - Jimmy L, 4 - Donald MC, 7 - Jake Mac, 9 - Pivot Har, 10 - Wike Chi, 13 - Tokio Man, 15 - Mike Sr, 17 - Frank T, 18 - Wu ZI
HEIGHT: 4

 */