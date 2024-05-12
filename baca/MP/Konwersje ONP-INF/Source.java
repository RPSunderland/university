// Rafal Plizga 5
import java.util.Scanner;
class Source {
    public static Scanner sc = new Scanner(System.in);
    public static String convertToONP(String in) {
        /*
        Algorytm konwersji INF -> ONP. Przechodzimy w jednej petli po wyrazeniu
        i wykorzystujac stos zwracamy wyrazenie w postaci ONP. Metoda usuwa rowniez niepotrzebne znaki i sprawdza
        poprawnosc wyrazenia wejsciowego. Zlozonosc algorytmu wynosi O(n).
         */
        int n = in.length();    //dlugosc wyrazenia
        char k;                 //pojedynczy znak
        int state = 0;          //stan w automacie skonczonym sprawdzjacym poprawnosc
        String out = "";        // wyjscie
        StackC sC = new StackC(n);  //stos znakow
        StackI sI = new StackI(n);  //stos priorytetow operatorow
        final String operators = "=..|..&..?..<>.+-.*/%^..!~"; // priorytety kolejnych operatorow (od 0 do 8), nawiasy maja priorytet -1
        for (int i = 0; i < n; i++) {
            k = in.charAt(i);
            if (97 <= k && k <= 122) {
                /*
                sprawdzenie stanu automatu skonczonego
                 */
                switch (state){
                    case 0 :
                        state = 1;
                        break;
                    case 1 :
                        return " error";
                    case 2 :
                        state = 1;
                        break;
                }
                out += " " + k;   //poprawny znak (mala litera) wrzucamy od razu do wyjscia
                continue;
            }
            if (k == '^' || k == '*' || k == '/' || k == '%' || k == '+' || k == '-' || k == '<' ||
                    k == '>' || k == '?' || k == '&' || k == '|' || k == '=' || k == '~' || k == '!') {
                /*
                sprawdzenie stanu automatu skonczonego
                 */
                if(k == '~' || k == '!') {
                    switch (state) {
                        case 0:
                            state = 2;
                            break;
                        case 1:
                            return " error";
                    }
                }
                else{
                    switch (state){
                        case 0 :
                            return " error";
                        case 1 :
                            state = 0;
                            break;
                        case 2 :
                            return " error";
                    }
                }

                if (sC.isEmpty()) {         //jesli stos pusty, wrzucamy operator
                    int o1 = operators.indexOf(k) / 3;
                    sC.push(k);
                    sI.push(o1);
                    continue;
                }
                /*
                dopoki na stosie na znajdziemy nawiasu, zdejmujemy operatory o priorytecie wiekszym (prawostronnie laczny)
                oraz wiekszym badz rownym (lewostronnie laczy), a na koniec wrzucamy operator na stos
                 */
                while (sC.seek() != '(' || sC.seek() != ')') {
                    int o1 = operators.indexOf(k) / 3;    // priorytet o1
                    int o2 = sI.seek();                     // priorytet o2
                    if ((o1 == 0 || o1 == 7 || o1 == 8) && (o1 < o2)) {      // prawostronna lacznosc
                        out += " " + sC.pop();
                        sI.pop();
                        continue;
                    } else if ((o1 == 1 || o1 == 2 || o1 == 3 || o1 == 4 || o1 == 5 || o1 == 6) && (o1 <= o2)) {    //lewostronna lacznosc
                        out += " " + sC.pop();
                        sI.pop();
                        continue;
                    }
                    break;
                }
                int o1 = operators.indexOf(k) / 3;      //jesli nie ma operatorow na stosie, wrzucamy operator
                sC.push(k);
                sI.push(o1);
                continue;
            }
            if (k == '(') {
                /*
                sprawdzenie stanu automatu skonczonego
                 */
                switch (state){
                    case 1 : return " error";
                    case 2 : state = 0;
                        break;
                }
                sC.push(k);     //lewy nawias wrzucamy na stos
                sI.push(-1);
                continue;
            }
            if (k == ')') {
                /*
                sprawdzenie stanu automatu skonczonego
                 */
                switch (state){
                    case 0 :
                        return " error";
                    case 2 :
                        return " error";
                }
                while (sC.seek() != '(') {  //dopoki nie znajdziemy lewego nawiasu wrzucamy wszystko do wyjscia, jesli nie znaleziono nawiasu zwroc error
                    out += " " + sC.pop();
                    sI.pop();
                    if (sC.isEmpty()) {
                        return " error";
                    }
                }
                sC.pop();
                sI.pop();
            }
        }
        if(state != 1){             //jesli stan automatu skonczonego jest niepoprawny zwroc error
            return " error";
        }
        while (!sC.isEmpty()) {     //oprozniaj stos i wrzucaj wszystko do wyjscia, jesli na stosie na znajduje sie lewy nawias zwroc error
            if(sC.seek() == '('){
                return " error";
            }
            out += " " + sC.pop();
        }
        if(out.equals("")){
            return " error";
        }
        return out;             //zwroc wynik
    }
    public static String convertToInf(String in) {
        /*
        Algorytm konwersji ONP -> INF zwracajacy wyraznie w postaci infiksowej o minimalnej liczbie nawiasow gwarantujacej
        wejsciowa koljenosc dzialan. Metoda usuwa rowniez niepotrzebne znaki i sprawdza
        poprawnosc wyrazenia wejsciowego. Metoda wykorzystuje stos stringow do trzymania informacji o wyrazeniach.
        Zlozonosc algorytmu wynosi O(n).
         */
        int n = in.length();    // dlugosc wyrazenia
        char k;                 // pojedynczy znak
        String tmp1;            // prawy operand
        String tmp2;            // lewy operand
        StackS sS = new StackS(n);  //stos na wyrazenia
        StackI sI = new StackI(n);  //stos na priorytety operatorow
        int actualOp;               //priorytet aktualnego opeartora
        int prevPop;                //priorytet operatora prawego wyrazenia
        int prevLop;                //priorytet operatora lewewgo wyrazenia
        final String operators = "=..|..&..?..<>.+-.*/%^..!~"; // priorytety kolejnych operatorow (od 0 do 8), priorytet znakow ustawiony na 10
        for (int i=0; i<n; i++){
            k = in.charAt(i);
            tmp1 = Character.toString(k);
            if (97 <= k && k <= 122){       //znak wrzucamy na stos
                sS.push(" "+tmp1);
                sI.push(10);
                continue;
            }
            /*
            Gdy natrafiamy na operator sciagamy jedno (jednoargumentowy), lub dwa (dwuargumentowy) wyrazenia ze stosu.
            Nastepnie laczymy operator z wyrazeniem i w zaleznosci od priorytetow wyrazen oraz lacznosci operatora dodajemy
            nawiasy, lub pozostawiamy bez zmian. Na koniec wrzucamy cale wyrazenie na stos.
             */
            if(k == '!' || k == '~'){       //operatory jednoarumentowe
                if(sS.isEmpty()){           //jesli nie ma operandu zwroc error
                    return " error";
                }
                tmp1 = "";
                actualOp = operators.indexOf(k) / 3;
                prevPop = sI.pop();
                if(prevPop < actualOp){      // np. ~(a+b)
                    tmp1+=" "+k;
                    tmp1+=" ("+sS.pop()+" )";
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevPop >= actualOp){    // np. ~~~a
                    tmp1+=" "+k;
                    tmp1+=sS.pop();
                    sS.push( tmp1);
                    sI.push(actualOp);
                    continue;
                }
            }
            if(k == '=' || k == '^'){       //operatory dwuargumentowe, prawostronnie laczne
                if(sS.count()<2){           //jesli nie ma dwoch operandow, zwroc error
                    return " error";
                }
                actualOp = operators.indexOf(k) / 3;
                tmp1="";
                tmp2="";
                prevPop = sI.pop();
                prevLop = sI.pop();
                if(prevLop < actualOp && prevPop < actualOp){   // np. (a+b)^(c*d)
                    tmp2+=" ("+sS.pop()+" )";
                    tmp1+=" ("+sS.pop()+" )";
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop >= actualOp && prevPop < actualOp){  // np. a^b^(c+d)
                    tmp2+=" ("+sS.pop()+" )";
                    tmp1+=sS.pop();
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop < actualOp && prevPop >= actualOp){  // np. (a+b)^c^d
                    tmp2+=sS.pop();
                    tmp1+=" ("+sS.pop()+" )";
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop >= actualOp && prevPop >= actualOp){ // np. a=b=c+d
                    tmp2+=sS.pop();
                    tmp1+=sS.pop();
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
            }
            /*
            //operatory dwuargumentowe, lewostronnie laczne
             */
            if (k == '*' || k == '/' || k == '%' || k == '+' || k == '-' || k == '<' ||
                    k == '>' || k == '?' || k == '&' || k == '|'){
                if(sS.count()<2){
                    return " error";
                }
                actualOp = operators.indexOf(k) / 3;
                tmp1="";
                tmp2="";
                prevPop = sI.pop();
                prevLop = sI.pop();
                if(prevLop < actualOp && prevPop <= actualOp){  // np. (a+b)*(c*d)
                    tmp2+=" ("+sS.pop()+" )";
                    tmp1+=" ("+sS.pop()+" )";
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop >= actualOp && prevPop <= actualOp){ // np. a*b*(c+d)
                    tmp2+=" ("+sS.pop()+" )";
                    tmp1+=sS.pop();
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop < actualOp && prevPop > actualOp){ // np. (a+b)*c^d
                    tmp2+=sS.pop();
                    tmp1+=" ("+sS.pop()+" )";
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
                if(prevLop >= actualOp && prevPop > actualOp){ // np. a*b*c^d
                    tmp2+=sS.pop();
                    tmp1+=sS.pop();
                    tmp1+=" "+k;
                    tmp1+=tmp2;
                    sS.push(tmp1);
                    sI.push(actualOp);
                    continue;
                }
            }
        }
        in = sS.pop();      // zwracamy to co zostalo ze stosu
        if(sS.isEmpty()) {
            return in;
        }
        return " error"; //jesli stos nie jest pusty zwroc error
    }


    public static void main(String[] args) {
        int n = sc.nextInt();
        sc.nextLine();
        boolean flag;
        flag = sc.hasNext("INF: *");
        for (int i = 0; i < n; i++) {
            if (flag) {
                System.out.println("ONP:" + convertToONP(sc.nextLine()));
            } else {
                System.out.println("INF:" + convertToInf(sc.nextLine()));
            }
            flag = sc.hasNext("INF: *");
        }
    }

}
/*
Stos charow
 */
class StackC{
    public StackC(int n){
        T = new char[n];
        top = -1;
        max = n;
    }
    public void push(char x){
        if(top+1 >= max){
        //    System.out.println("StackC overflow");
            return;
        }
        ++top;
        T[top] = x;
    }
    public char pop(){
        char x;
        if(top>=0) {
            x = T[top];
            top--;
        }
        else{
        //    System.out.println("StackC underflow");
            return '0';
        }
        return x;
    }
    public char seek(){
        if(top > -1) {
            return T[top];
        }
        return '0';
    }
    public boolean isEmpty(){
        if(top == -1){
            return true;
        }
        return false;
    }
    private char[] T;
    private int top;
    private int max;
}
/*
Stos intow
 */
class StackI{
    public StackI(int n){
        T = new int[n];
        top = -1;
        max = n;
    }
    public void push(int x){
        if(top+1 >= max){
          //  System.out.println("StackI overflow");
            return;
        }
        ++top;
        T[top] = x;
    }
    public int pop(){
        int x;
        if(top>=0) {
            x = T[top];
            top--;
        }
        else{
         //   System.out.println("StackI underflow");
            return -2;
        }
        return x;
    }
    public int seek(){
        if(top > -1) {
            return T[top];
        }
        return -1;
    }
    public boolean isEmpty(){
        if(top == -1){
            return true;
        }
        return false;
    }
    public void print(){
        for(int i=0; i < top+1; i++){
            System.out.print(T[i]+",");
        }
        System.out.println();
    }
    private int[] T;
    private int top;
    private int max;
}
/*
Stos stringow
 */
class StackS{
    public StackS(int n){
        T = new String[n];
        top = -1;
        max = n;
    }
    public void push(String x){
        if(top+1 >= max){
          //  System.out.println("StackS overflow");
            return;
        }
        ++top;
        T[top] = x;
    }
    public String pop(){
        String x;
        if(top>=0) {
            x = T[top];
            top--;
        }
        else{
          //  System.out.println("StackS underflow");
            return "0";
        }
        return x;
    }
    public String seek(){
        if(top > -1) {
            return T[top];
        }
        return "0";
    }
    public void print(){
        for(int i=0; i < top+1; i++){
            System.out.print(T[i]+",");
        }
        System.out.println();
    }
    public int count(){
        return top+1;
    }
    public boolean isEmpty(){
        if(top == -1){
            return true;
        }
        return false;
    }
    private String[] T;
    private int top;
    private int max;
}

/*
test.in

wejscie:
11
INF: (p?(q&p))|((~p>q)|(r&p))
ONP: ab=!~~!
INF: a*b/(c*(a*b))
ONP: abcd^e-fgh*+^*+i-
INF: (((((y) + ~~x))))
ONP: xy+y+x+
INF: x^y*z-w<xu|w+x
ONP: abcde+++abc++abc++
INF: )(x+y)*(x-y)(
INF: (((((x-y)*x)/y)-y))
ONP: xyz*>!

test.out

wyjscie:
INF: ! ~ ~ ! ( a = b )
ONP: a b * c a b * * /
INF: a + b * ( c ^ d - e ) ^ ( f + g * h ) - i
ONP: y x ~ ~ +
INF: x + y + y + x
ONP: error
INF: error
ONP: error
ONP: x y - x * y / y -
INF: ! ( x > y * z )
 */