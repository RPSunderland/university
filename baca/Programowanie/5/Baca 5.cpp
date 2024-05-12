//Rafa³ Plizga
#include <iostream>
#include <cstdio>
#include <cmath>
#include <cstring>
using namespace std;
char koniunkcja(char x, char y){                //pomocnicze funkjce operatorów
    int a,b,c;
    a=x-'0';
    b=y-'0';
    c=a&&b;
    if(c==0){return'0';}
    else{return'1';}
}
char implikacja(char x, char y){
    int a,b,c;
    a=x-'0';
    b=y-'0';
    if(a==1 && b==0){c=0;}
    else{c=1;}
    if(c==0){return'0';}
    else{return'1';}
}
char alternatywa(char x, char y){
    int a,b,c;
    a=x-'0';
    b=y-'0';
    c=a||b;
    if(c==0){return'0';}
    else{return'1';}
}
char rownowaznosc(char x, char y){
    int a,b,c;
    a=x-'0';
    b=y-'0';
    if(a==b){c=1;}
    else{c=0;}
    if(c==0){return'0';}
    else{return'1';}

}

string Convert(string input){                               //funkcja konwertujaca dane wejsciowe do odwroconej notacji polskiej
char stos[1000];
char output[1000];
int i=0;
int j=0;
int k=0;
while(input[i]!='\0'){
    if(input[i]<=122 && input[i]>=97){                      //zwykle znaki ida na output, specjalne ida na stos, gdy trafimy na ) wyrzucamy w
        output[j]=input[i];                                 // wszytsko na output, ew gdy jest tylda za lewym nawiasem to ja tez wyrzucamy,
        j++;                                                //na koniec przerzucamy stos na output
    }
    else if(input[i]=='~'){stos[k]=input[i];
        k++;
    }
    else if(input[i]=='('){stos[k]=input[i];
        k++;
    }
    else if(input[i]==')'){
        while(stos[k-1]!='('){
            output[j]=stos[k-1];
            j++;
            k--;
            }
        k--;
        while(stos[k-1]=='~'){
            output[j]=stos[k-1];
            j++;
            k--;
        }
    }
    else if(input[i]=='|' || input[i]=='&'||input[i]=='>'||input[i]=='='){
        stos[k]=input[i];
        k++;
    }
    i++;
}
while(stos[k-1]!='('){
    output[j]=stos[k-1];
    j++;
    k--;
    }
return output;
}
bool IsTautology(string input){                                     //funkcja obliczaj¹ca tautologie //sa bledy dla testu (p->q) <-> (~p v q) bledy z negacja
    char stos[1000];
    char a,b,c;
    bool wynik;
    int i=0;
    int j=0;
    while(input[i]!='\0'){
        if(input[i]=='0'||input[i]=='1'){           //proste przerzucenie na output
            stos[j]=input[i];
            j++;
        }
        else if(input[i]=='|'||input[i]=='&'||input[i]=='>'||input[i]=='='){       //operacje dwuargumentowe, wez mu na tbalicy narysuj jak cos
            char a = stos[j-1];
            char b = stos[j-2];
            if(input[i]=='|'){c=alternatywa(b,a);}
            else if(input[i]=='&'){c=koniunkcja(b,a);}
            else if(input[i]=='>'){c=implikacja(b,a);}
            else if(input[i]=='='){c=rownowaznosc(b,a);}
            stos[j-2]=c;
            j--;
        }
        else if(input[i]=='~'){                                 //tak dziala negacja
            if(stos[j-1]=='0'){stos[j-1]='1';}
            else{stos[j-1]='0';}
        }
        i++;
    }
    wynik=stos[0]-'0';
    return wynik;
}
string Switch(string input, char x, char q){                   //pomocnicza funkcja podstawiajaca znak do formu³y , dodac paramter q
    int i=0;
    char tmp;
    while(!(input[i]>=97 && input[i]<=122)||input[i]==q){
            i++;
    }
    tmp = input[i];
    while(input[i]!='\0'){
        if(input[i]==tmp){input[i]=x;}
        i++;
    }
    return input;
}
string Switch(string input, char* x, char q){               //funkcja podstwaijaca ciagi {0,1} do formu³y
    while(*x!='\0'){
       input = Switch(input,*x, q);
        x++;
    }
    return input;
}
int Licznik(string input){                          //pomocnicza funkcja zliczajaca zmienne w formule
    int i=0;
    int result=0;
    while(input[i]!='\0'){
        if(input[i]>=97 && input[i]<=122){
                result ++;
                input = Switch(input,'0','@');
        }
        i++;
    }
    return result;
}
bool GetSequence(char T[], int k)                   //pomocnicza funkcja zwracjaca kolejne ciagi {0,1}
{
    int p = k - 1;
    while (T[p] == '1'){            //warunek stopu, konczymy na ciagu 11111...
        p--;
    }

    if (p < 0){return false;}

    if(T[p]=='0'){T[p]='1';}    //przykladowo 000->001->010->011->100->101->110->111, idziemy jedynkami coraz bardziej w lewo, wsm sa to kolejne cyfry
    else{T[p]='0';}               //systemu binarnego, wchodzimy 1 tyle ile sie da i zerujemy resztę
    for(int i = p + 1; i < k; i++){
        T[i] = '0';
    }
    return true;
}




int main(){
    string formule;
    char x;
    cin >> x;
    while(x!='W'){
        if(x=='T'){
            bool tmp=true;
            string temp;
            cin >> formule;
            formule = Convert(formule);     //konwersja na notacje polska
            int k = Licznik(formule);          //licznik zmiennych
            char* T = new char[k];              //tablica na kolejne ciagi{0,1}
            for(int i=0; i<k; i++){
                T[i]='0';                       //inicjalizacja tablicy
            }
            while(true){
                T[k]='\0';
                temp= Switch(formule, T);                   //podmiana zmiennych w formule na ciag {0,1{
                if(IsTautology(temp)==0){tmp=false; break;}; //trywialne
                if(GetSequence(T, k)==false){ break;}   //pobieramy kolejny ciąg, az sie skonczą
            }
            if(tmp){cout << "TAK" << endl;}
            else{cout << "NIE";}
            delete[] T;
        }
        else if(x=='P'){        //analogiczne do tautologii, jest licznik prawd
            int tmp=0;
            string temp;
            cin >> formule;
            formule = Convert(formule);
            int k = Licznik(formule);
            char* T = new char[k];
            for(int i=0; i<k; i++){
                T[i]='0';
            }
            while(true){
                T[k]='\0';
                temp= Switch(formule, T, '@');
                if(IsTautology(temp)==1){tmp++;}
                if(GetSequence(T, k)==false){break;}
            }
            cout << tmp;
            delete[] T;
        }
    /*    else if(x=='F'){
            int tmp=0;
            string temp;
            cin >> formule;
            formule = Convert(formule);
            int k = Licznik(formule);       // w bacy mam to zle, mysle ze latwo wytlumacze
            char* T = new char[k];
            for(int i=0; i<k; i++){
                T[i]='0';
            }
            while(true){
                T[k]='\0';
                temp= Switch(formule, T, '@');
                if(IsTautology(temp)==0){tmp++;}
                if(GetSequence(T, k)==false){break;}
            }
            cout << tmp;
            delete[] T;
        }
        else if(x=='1'){
            bool tmp=true;
            string temp;
            char w;
            cin >> w;
            cin >> formule;
            formule = Convert(formule);
            int k = Licznik(formule);
            char* T = new char[k];
            for(int i=0; i<k-1; i++){
                T[i]='0';
            }
            while(true){
                T[k]='\0';
                temp= Switch(formule, T, w);
                temp=Switch(temp,'1','@');
                if(IsTautology(temp)==0){tmp=false; break;};
                if(GetSequence(T,k-1)==false){break;}
            }
            if(tmp){cout << "TAK" << endl;}
            else{cout << "NIE";}
            delete[] T;
        }
        else if(x=='0'){
        bool tmp=true;
            string temp;
            char w;
            cin >> w;
            cin >> formule;
            formule = Convert(formule);
            int k = Licznik(formule);
            char* T = new char[k];;
            for(int i=0; i<k-1; i++){
                T[i]='0';
            }
            while(true){
                T[k]='\0';
                temp= Switch(formule, T, w);
                temp=Switch(temp,'1','@');
                if(IsTautology(temp)==0){tmp=false; break;};
                if(GetSequence(T,k-1)==false){break;}
            }
            if(tmp){cout << "TAK" << endl;}
            else{cout << "NIE";}
        }*/
        x='W';
        cin >> x;
    }
  return 0;
}
