// Rafal Plizga
#include "Morfologia.h"
#include <iostream>
#include <stdio.h>
#include <string.h>


//DEKLARACJE
class BitmapaExt : public Bitmapa{  //pochodna klasy Bitmapa realizujaca wyswietlanie bitmapy w okreslony sposob
public:
    BitmapaExt();   //konstruktor domyslny
    BitmapaExt(unsigned int, unsigned int, unsigned int);   //konstruktor
    BitmapaExt(const BitmapaExt&);  //konstruktor kopiujacy
    ~BitmapaExt() override; //destruktor
    BitmapaExt& operator=(const Bitmapa&);  //operator przypisania do Bitmapy
    BitmapaExt& operator=(const BitmapaExt&);   //operator przypisania do BitmapyExt
    unsigned sx() const override;   //metoda zwracajaca wymiar x
    unsigned sy() const override;   //metoda zwracajaca wymiar y
    unsigned sz() const override;   //metoda zwracajaca wymiar z
    bool& operator()(unsigned int, unsigned int, unsigned int) override;    //operator pozwalajacy modyfikowac wartosci komorek w bitmapie
    bool operator()(unsigned int, unsigned int, unsigned int) const override;   //operator pozwalajacy zczytywac wartosci komorek w bitmpaie
    int bialeotoczenieX(unsigned int, unsigned int, unsigned int) const;    //metoda pomocnicza zliczajaca biale komorki w sasiedztwie punktu na osi x
    int bialeotoczenieY(unsigned int, unsigned int, unsigned int) const;    //metoda pomocnicza zliczajaca biale komorki w sasiedztwie punktu na osi y
    int bialeotoczenieZ(unsigned int, unsigned int, unsigned int) const;    //metoda pomocnicza zliczajaca biale komorki w sasiedztwie punktu na osi z
    int czarneotoczenieX(unsigned int, unsigned int, unsigned int) const;   //metoda pomocnicza zliczajaca czarne komorki w sasiedztwie punktu na osi x
    int czarneotoczenieY(unsigned int, unsigned int, unsigned int) const;   //metoda pomocnicza zliczajaca czarne komorki w sasiedztwie punktu na osi y
    int czarneotoczenieZ(unsigned int, unsigned int, unsigned int) const;   //metoda pomocnicza zliczajaca czarne komorki w sasiedztwie punktu na osi z
protected:
    unsigned int x = 0; //rozmiar wspolrzednej x-owej bitmapy
    unsigned int y = 0; //rozmiar wspolrzednej y-owej bitmapy
    unsigned int z = 0; //rozmiar wspolrzednej z-owej bitmapy
    bool* T = nullptr;    //tablica przechowujaca wartosci komorek w bitmapie
};
std::ostream& operator<<(std::ostream&, const Bitmapa&); //przeladowany operator wypisania bitmpay do strumienia
class Inwersja : public Przeksztalcenie{    //pochodna klasy Przeksztalcenie relaizujaca operacje inwersji
public:
    void przeksztalc(Bitmapa&) override;
};
class Erozja : public Przeksztalcenie{  //pochodna klasy Przeksztalcenie relaizujaca operacje erozji
public:
    void przeksztalc(Bitmapa&) override;
};
class Dylatacja : public Przeksztalcenie{ //pochodna klasy Przeksztalcenie relaizujaca operacje dylatacji
public:
    void przeksztalc(Bitmapa&) override;
};
class Zerowanie : public Przeksztalcenie{   //pochodna klasy Przeksztalcenie relaizujaca operacje zerowania
public:
    void przeksztalc(Bitmapa&) override;
};
class Usrednianie : public Przeksztalcenie{ //pochodna klasy Przeksztalcenie relaizujaca operacje usredniania
public:
    void przeksztalc(Bitmapa&) override;
};
class ZlozeniePrzeksztalcen : public Przeksztalcenie{   //pochodna klasy Przeksztalcenie realizujaca skladanie operacji
public:
    ZlozeniePrzeksztalcen();    //konstruktor domyslny (operacja identycznosciowa)
    ~ZlozeniePrzeksztalcen() override;  //destruktor
    explicit ZlozeniePrzeksztalcen(const ZlozeniePrzeksztalcen&);   //konstruktor kopiujacy
    ZlozeniePrzeksztalcen& operator=(const ZlozeniePrzeksztalcen&); //operator przypisania
    void przeksztalc(Bitmapa&) override;    //metoda wykonujaca przeksztalcenie danej bitmapy przy pomocy zlozenia przeksztalcen
    void dodajPrzeksztalcenie(Przeksztalcenie*);    //metoda dodajaca przeksztalcenie do zlozenia
private:
    Przeksztalcenie** przeksztalcenia;  //tablica przechowujaca przeksztalcenia
    unsigned int l; //licznik przeksztalcen
    unsigned int MAX;   //maksymalna ilosc przeksztalcen w tablicy (jesli limit zostanie osiagniety, podwajamay MAX)
};


//DEFINICJE
BitmapaExt::BitmapaExt() : x(0),y(0),z(0) {
    T = nullptr;
}
BitmapaExt::BitmapaExt(unsigned int x, unsigned int y, unsigned int z) : x(x), y(y), z(z){
    T = new bool[x*y*z];
    if(x*y*z < 1000000){    //jesli x*y*z < 1000000 wyzeruj tablice, w przeciwnym razie pozostaw niezinicjalizowana
        memset(T, false,(x*y*z)*sizeof (bool));
    }
}
BitmapaExt::BitmapaExt(const BitmapaExt& bitmapa){
    x = bitmapa.sx();
    y = bitmapa.sy();
    z = bitmapa.sz();
    T = new bool[x*y*z];
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                T[i + (j*x) + (k*x*y)] = bitmapa(i,j,k);    //wzor na (i,j,k) wspolrzedna w bitmapie
            }
        }
    }
}
BitmapaExt::~BitmapaExt(){
    delete[] T;
    T = nullptr;
}
BitmapaExt& BitmapaExt::operator=(const BitmapaExt& bitmapa) {
    if(this != &bitmapa) {
        delete[] T;
        x = bitmapa.sx();
        y = bitmapa.sy();
        z = bitmapa.sz();
        T = new bool[x*y*z];
        for(unsigned int i = 0; i < x; i++){
            for(unsigned int j = 0; j < y; j++){
                for(unsigned int k = 0; k < z; k++){
                    T[i + (j*x) + (k*x*y)] = bitmapa(i,j,k);
                }
            }
        }
    }
    return *this;
}
BitmapaExt& BitmapaExt::operator=(const Bitmapa& bitmapa){
    if(this != &bitmapa) {
        delete[] T;
        x = bitmapa.sx();
        y = bitmapa.sy();
        z = bitmapa.sz();
        T = new bool[x*y*z];
        for(unsigned int i = 0; i < x; i++){
            for(unsigned int j = 0; j < y; j++){
                for(unsigned int k = 0; k < z; k++){
                    T[i + (j*x) + (k*x*y)] = bitmapa(i,j,k);
                }
            }
        }
    }
    return *this;
}
unsigned int BitmapaExt::sx() const {return x;}
unsigned int BitmapaExt::sy() const {return y;}
unsigned int BitmapaExt::sz() const {return z;}
bool& BitmapaExt::operator()(unsigned int i, unsigned int j, unsigned int k) {
        return T[i + (j*x) + (k*x*y)];
}
bool BitmapaExt::operator()(unsigned int i, unsigned int j, unsigned int k) const {
        return T[i + (j*x) + (k*x*y)];
}
std::ostream& operator<<(std::ostream& o, const Bitmapa& bitmapa){
    unsigned int x = bitmapa.sx();
    unsigned int y = bitmapa.sy();
    unsigned int z = bitmapa.sz();
    o << "{" << std::endl;
    for(unsigned int i = 0; i < x; i++){
        o << " {" << std::endl;
        for(unsigned int j = 0; j < y; j++){
            o << "  {";
            for(unsigned int k = 0; k < z; k++){  //???
                if(k < z-1) {
                    o << bitmapa(i, j, k) << ",";
                }
                else{
                    o << bitmapa(i, j, k);
                }
            }
            if(j < y-1) {
                o << "}," << std::endl;
            }
            else{
                o << "}" << std::endl;
            }
        }
        if( i < x-1) {
            o << " }," << std::endl;
        }
        else{
            o << " }" << std::endl;
        }
    }
    o << "}";
    return  o;
}
int BitmapaExt::bialeotoczenieX(unsigned int i, unsigned int j, unsigned int k) const {
    if(x == 0 || x == 1){
        return 0;
    }
    if(i == 0){
        if(!T[i+1 + (j*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    if(i == x-1){
        if(!T[i-1 + (j*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(!T[i-1 + (j*x) + (k*x*y)]){
        l++;
    }
    if(!T[i+1 + (j*x) + (k*x*y)]){
        l++;
    }
    return l;
}
int BitmapaExt::bialeotoczenieY(unsigned int i, unsigned int j, unsigned int k) const {
    if(y == 0 || y == 1){
        return 0;
    }
    if(j == 0){
        if(!T[i + ((j+1)*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    if(j == y-1){
        if(!T[i + ((j-1)*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(!T[i + ((j-1)*x) + (k*x*y)]){
        l++;
    }
    if(!T[i + ((j+1)*x) + (k*x*y)]){
        l++;
    }
    return l;
}
int BitmapaExt::bialeotoczenieZ(unsigned int i, unsigned int j, unsigned int k) const {
    if(z == 0 || z == 1){
        return 0;
    }
    if(k == 0){
        if(!T[i + (j*x) + ((k+1)*x*y)]){
            return 1;
        }
        return 0;
    }
    if(k == z-1){
        if(!T[i + (j*x) + ((k-1)*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(!T[i + (j*x) + ((k-1)*x*y)]){
        l++;
    }
    if(!T[i + (j*x) + ((k+1)*x*y)]){
        l++;
    }
    return l;
}
int BitmapaExt::czarneotoczenieX(unsigned int i, unsigned int j, unsigned int k) const {
    if(x == 0 || x == 1){
        return 0;
    }
    if(i == 0){
        if(T[i+1 + (j*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    if(i == x-1){
        if(T[i-1 + (j*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(T[i-1 + (j*x) + (k*x*y)]){
        l++;
    }
    if(T[i+1 + (j*x) + (k*x*y)]){
        l++;
    }
    return l;
}
int BitmapaExt::czarneotoczenieY(unsigned int i, unsigned int j, unsigned int k) const {
    if(y == 0 || y == 1){
        return 0;
    }
    if(j == 0){
        if(T[i + ((j+1)*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    if(j == y-1){
        if(T[i + ((j-1)*x) + (k*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(T[i + ((j-1)*x) + (k*x*y)]){
        l++;
    }
    if(T[i + ((j+1)*x) + (k*x*y)]){
        l++;
    }
    return l;
}
int BitmapaExt::czarneotoczenieZ(unsigned int i, unsigned int j, unsigned int k) const {
    if(z == 0 || z == 1){
        return 0;
    }
    if(k == 0){
        if(T[i + (j*x) + ((k+1)*x*y)]){
            return 1;
        }
        return 0;
    }
    if(k == z-1){
        if(T[i + (j*x) + ((k-1)*x*y)]){
            return 1;
        }
        return 0;
    }
    int l = 0;
    if(T[i + (j*x) + ((k-1)*x*y)]){
        l++;
    }
    if(T[i + (j*x) + ((k+1)*x*y)]){
        l++;
    }
    return l;
}
void Inwersja::przeksztalc(Bitmapa& bitmapa) {
    unsigned int x = bitmapa.sx();
    unsigned int y = bitmapa.sy();
    unsigned int z = bitmapa.sz();
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                bitmapa(i,j,k) = !bitmapa(i,j,k);
            }
        }
    }
} //+
void Erozja::przeksztalc(Bitmapa& bitmapa) {
    unsigned x = bitmapa.sx();
    unsigned y = bitmapa.sy();
    unsigned z = bitmapa.sz();
    BitmapaExt tmp;
    tmp = bitmapa;
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                if(!tmp(i,j,k)){
                    continue;
                }
                if(tmp.bialeotoczenieX(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
                if(tmp.bialeotoczenieY(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
                if(tmp.bialeotoczenieZ(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
            }
        }
    }
}
void Dylatacja::przeksztalc(Bitmapa& bitmapa) {
    unsigned x = bitmapa.sx();
    unsigned y = bitmapa.sy();
    unsigned z = bitmapa.sz();
    BitmapaExt tmp;
    tmp = bitmapa;
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                if(tmp(i,j,k)){
                    continue;
                }
                if(tmp.czarneotoczenieX(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
                if(tmp.czarneotoczenieY(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
                if(tmp.czarneotoczenieZ(i,j,k) > 0) {
                    bitmapa(i, j, k) = !bitmapa(i, j, k);
                    continue;
                }
            }
        }
    }
}
void Zerowanie::przeksztalc(Bitmapa& bitmapa){
    unsigned x = bitmapa.sx();
    unsigned y = bitmapa.sy();
    unsigned z = bitmapa.sz();
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                bitmapa(i,j,k) = false;
            }
        }
    }
}
void Usrednianie::przeksztalc(Bitmapa& bitmapa){
    unsigned x = bitmapa.sx();
    unsigned y = bitmapa.sy();
    unsigned z = bitmapa.sz();
    BitmapaExt tmp;
    tmp = bitmapa;
    for(unsigned int i = 0; i < x; i++){
        for(unsigned int j = 0; j < y; j++){
            for(unsigned int k = 0; k < z; k++){
                int l = 0;
                if(bitmapa(i,j,k)) {
                    l+=tmp.bialeotoczenieX(i,j,k);
                    l+=tmp.bialeotoczenieY(i,j,k);
                    if(l > 3){
                        bitmapa(i, j, k) = false;
                        continue;
                    }
                    l+=tmp.bialeotoczenieZ(i,j,k);
                    if(l > 3){
                        bitmapa(i, j, k) = false;
                        continue;
                    }
                }
                l+=tmp.czarneotoczenieX(i,j,k);
                l+=tmp.czarneotoczenieY(i,j,k);
                if(l > 3){
                    bitmapa(i, j, k) = true;
                    continue;
                }
                l+=tmp.czarneotoczenieZ(i,j,k);
                if(l > 3){
                    bitmapa(i, j, k) = true;
                    continue;
                }
            }
        }
    }
}
ZlozeniePrzeksztalcen::ZlozeniePrzeksztalcen(){
    l = 0;
    MAX = 20;
    przeksztalcenia = new Przeksztalcenie*[MAX];
}
ZlozeniePrzeksztalcen::~ZlozeniePrzeksztalcen(){
    delete[] przeksztalcenia;
}
ZlozeniePrzeksztalcen::ZlozeniePrzeksztalcen(const ZlozeniePrzeksztalcen& zp){
    l = zp.l;
    MAX = zp.MAX;
    przeksztalcenia = new Przeksztalcenie*[MAX];
    for(unsigned int i = 0; i < l; i++){
        przeksztalcenia[i] = zp.przeksztalcenia[i];
    }
}
ZlozeniePrzeksztalcen& ZlozeniePrzeksztalcen::operator=(const ZlozeniePrzeksztalcen& zp){
    if(this != &zp){
        l = zp.l;
        MAX = zp.MAX;
        delete[] przeksztalcenia;
        przeksztalcenia = new Przeksztalcenie*[MAX];
        for(unsigned int i = 0; i < l; i++){
            przeksztalcenia[i] = zp.przeksztalcenia[i];
        }
    }
    return *this;
}
void ZlozeniePrzeksztalcen::przeksztalc(Bitmapa& bitmapa) {
    for(unsigned int i = 0; i < l; i++){
        przeksztalcenia[i]->przeksztalc(bitmapa);
    }
}
void ZlozeniePrzeksztalcen::dodajPrzeksztalcenie(Przeksztalcenie* p) {
    if(l < MAX){
        przeksztalcenia[l] = p;
        l++;
    }
    else{
        Przeksztalcenie** tmp = new Przeksztalcenie*[2*MAX];
        for(unsigned int i = 0; i < l; i++){
            tmp[i] = przeksztalcenia[i];
        }
        delete[] przeksztalcenia;
        przeksztalcenia = tmp;  //???
        MAX*=2;
    }
}
