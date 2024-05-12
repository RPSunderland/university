#include <iostream>
#include <cstdarg>
#include<cstdio>        //sa dwie wersje tej bacy, pod mainem napisze alternatywna korzystajaca z funkcji L
using namespace std;
void usunZnak(char* s, char k){     //prosta funckja usuwajaca znak, latwe przepinanie wskaznikow
    char* tmp1=s;
    char* tmp2=s;
    while(*tmp1!='\0') {
        if(!(*tmp1==k)){
            *tmp2=*tmp1;
            tmp2++;
        }
        tmp1++;
    }
    *tmp2='\0';
}
void przeksztalc(char* s, char(*f)(char x)){        //trywialne
    if(s==0){return;}
    while(*s!='\0'){
        *s=f(*s);
        s++;
    }
}
bool filtruj(char* s, bool (*f)(char x)){       //analogicznie do usunznak
    if(s==0){return false;}
    bool wynik =false;
    char* tmp=s;
    while(*tmp!='\0') {
        if(!f(*tmp)){
            *s=*tmp;
            s++;
            wynik=true;
        }
        tmp++;
    }
    *s='\0';
    return wynik;
}
void filtruj(char* s, char* k){
    if(s==0 || k==0){return;}  //dosc prosta funkcja
    char* temp;
    while(*k!='\0'){
        temp=s;
        usunZnak(s,*k);
        s=temp;
        k++;
    }
}
bool filtruj(char** b, char** e, bool(*f)(char x)){
    if(b==0 || f==0){return false;}
    bool temp=false;
    while(*b!=*e){          //tu chyba trzeba by³o porownywac **b z **e
    if(*b==0){return false;}
    if(filtruj(*b,f)){temp=true;};
    b++;
    }
    return temp;
}
char dodawanieM(char x, char T){        //dodawanie mod 26
    char s;
    if('a'<=x && x<='z'){       //operacje na ascii
        s=(x-97)+(T-65);
        if(s<26){s+=97;}
        else{s+=71;}
    }
    else if('A'<=x && x<='Z'){
        s=(x-65)+(T-65);
        if(s<26){s+=65;}
        else{s+=39;}
    }
    return s;
}
void szyfruj(char* s, char* klucz){     //ten szyfr to dodawanie mod 26
    if(s==0){return;}
    char* temp=klucz;
    while(*s!='\0'){
       if(*klucz=='\0'){klucz=temp;}
        if((65<=*s && *s<=90) ||(97<=*s && *s<=122)){
            *s=dodawanieM(*s,*klucz);
            klucz++;
        }
        s++;
    }
}
void przetwarzaj(char* k, ...){
    if(k==0){return;}
    va_list ap;
    va_start (ap,k);        //inicjalizacja listy
    while(*k!='\0'){
        if(*k=='f'){
            char* s= va_arg(ap,char*);                      //najpierw deklaracja zmiennych
            bool(*f)(char x)=va_arg(ap,bool(*)(char));      //w va_arg dajemy tylko sygnatury, powiedziec ze dlugo kminnilem nad tym
            filtruj(s,f);
        }
        if(*k=='p'){
            char* s= va_arg(ap,char*);
            char(*f)(char x)=va_arg(ap,char(*)(char));
            przeksztalc(s,f);
        }
        if(*k=='s'){
            char* napis= va_arg(ap,char*);
            char* kod= va_arg(ap,char*);
            szyfruj(napis,kod);
        }
        k++;
    }
    va_end(ap);
}
char zmienNaWielkaLitere(char c){
    return (c>='a' and c<='z') ? c-32 : c;
}
bool zostawMalaLitere(char c){
  return (c<'a' or c>'z');
}
int main(){
  char s1[]="";
  filtruj(s1,zostawMalaLitere);
  printf(s1);
return 0;
}
//w alternatywnej wersji mielismy funkcje przesun L, która trywialnie przesuwa³a wszystkie znakio w lewo, korzystlaismy w niej
//w funkcjach filtrujl generalnie latwo wykminic jak dzialaly
