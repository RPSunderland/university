//Rafal Plizga
#include "Uczestnik.h"
#include<iostream>
class Wodzirej{
public:
    Wodzirej(){}
    ~Wodzirej(){
        Node* node=head.L;
        while(node!=&head){
        Node* temp = node;
        node=node->L;
        delete temp;
        }
        delete head_o;
    }
    unsigned int dolacz(Uczestnik* osoba);      // dziala
    unsigned int dolacz(Uczestnik* osoba, unsigned int pozycja); //dziala
    bool rozpocznij();  //dziala
    bool zakoncz(); //dziala
    bool zrezygnuj(Uczestnik* osoba);//dziala
    bool zrezygnuj(unsigned int n);//dziala
    bool przekaz(Uczestnik* osoba);//dziala
    bool przekaz(unsigned int);//dziala
    void uczestnicy() const;//dziala
    void uczestnicy(Uczestnik::Plec p) const;//dziala
    unsigned int liczba() const; //dziala
    const Uczestnik* wodzirej() const; //dziala
    private:
    struct Node{        //struktura osoby w koleczku
        Node(Uczestnik* uczestnik, Node* L, Node* R, unsigned int id): uczestnik(uczestnik),L(L),R(R),id(id) {}
        Uczestnik* uczestnik;
        Node* L;
        Node* R;
        unsigned int id;
    };
    bool is_uczestnik(Uczestnik* uczestnik);        //pomocnicza metoda do sprawdzania czy uczestnik jest w koleczku
    Uczestnik* head_o = new Uczestnik(Uczestnik::W);    //stworzenie wodzireja osoby
    Node head = Node(head_o,&head,&head,0);         //wstawienie wodzireja do koleczka
    Uczestnik* chusteczka = head.uczestnik;
    bool zabawa = false;                            //pomocnicze zmienne
    unsigned int ID=1;
    unsigned int licznik_k=0;
    unsigned int licznik_m=0;
};



unsigned int Wodzirej::dolacz(Uczestnik* osoba){
    if(osoba == nullptr || osoba->plec==Uczestnik::W || is_uczestnik(osoba)){return 0;}
    else{
        unsigned int id = Wodzirej::ID;
        Wodzirej::ID++;
        if(osoba->plec==Uczestnik::K){Wodzirej::licznik_k++;}
        else{Wodzirej::licznik_m++;}
        Node* temp = head.R;
        Node* n = new Node(osoba,&head,temp,id);
        head.R = n;
        temp->L = n;
    return id;
    }
}
unsigned int Wodzirej::dolacz(Uczestnik* osoba, unsigned int pozycja){
    if(osoba == nullptr || osoba->plec==Uczestnik::W || is_uczestnik(osoba)|| pozycja>=Wodzirej::licznik_k+Wodzirej::licznik_m+1){return 0;}
    else{
        unsigned int id = Wodzirej::ID;
        Wodzirej::ID++;
        if(osoba->plec==Uczestnik::K){Wodzirej::licznik_k++;}
        else{Wodzirej::licznik_m++;}
        Node* temp = head.L;
        for(int i=0; i<pozycja; i++){
            temp=temp->L;
        }
        Node* n = new Node(osoba,temp,temp->R,id);
        temp->R = n;
        n->R->L = n;
        return id;
    }
}
bool Wodzirej::rozpocznij(){
    if(Wodzirej::zabawa==true || Wodzirej::ID==1 || Wodzirej::licznik_k ==0 || Wodzirej::licznik_m==0){return false;}
    else{Wodzirej::zabawa=true; chusteczka=head.R->uczestnik; return true;}
}
bool Wodzirej::zakoncz(){
    if(zabawa==false){return false;}
    else{zabawa = false; chusteczka=head.uczestnik; return true;}
}
bool Wodzirej::zrezygnuj(Uczestnik* osoba){
    if(osoba == nullptr || osoba == head.uczestnik || osoba==chusteczka){return false;}
    else{
        Node* node=head.L;
        while(node!=&head){
            if(node->uczestnik==osoba){
                node->R->L=node->L;
                node->L->R=node->R;
                delete node;
                return true;
            }
            node=node->L;
        }
        return false;
    }
}
bool Wodzirej::zrezygnuj(unsigned int n){
    if(n==0){return false;}
    else{
        Node* node=head.L;
        while(node!=&head){
            if(node->id==n){
                if(node->uczestnik==chusteczka){return false;}
                else{
                    node->R->L=node->L;
                    node->L->R=node->R;
                    delete node;
                    return true;
                }
            }
            node=node->L;
        }
        return false;
    }

}
bool Wodzirej::przekaz(Uczestnik* osoba){
    if(zabawa==false || osoba == head.uczestnik){return false;}
    else{
        Node* node=head.L;
        while(node!=&head){
            if(node->uczestnik==osoba){
                if(chusteczka->plec==osoba->plec){return false;}
                else{chusteczka=osoba; return true;}
            }
            node=node->L;
        }
        return false;
    }

}
bool Wodzirej::przekaz(unsigned int n){
    if(zabawa==false || n==0){return false;}
    else{
        Node* node=head.L;
        while(node!=&head){
            if(node->id==n){
                if(chusteczka->plec==node->uczestnik->plec){return false;}
                else{chusteczka=node->uczestnik;return true;}
            }
            node=node->L;
        }
        return false;
    }

}
void Wodzirej::uczestnicy()const{
    const Node* node=head.L;
    std::cout <<"plec: 2, nr: 0"<<std::endl;
    while(node!=&head){
        if(node->uczestnik->plec==Uczestnik::K){std::cout <<"plec: 0, nr: " <<node->id<<std::endl;}
        else{std::cout <<"plec: 1, nr: " <<node->id<<std::endl;}
        node=node->L;
    }
}
void Wodzirej::uczestnicy(Uczestnik::Plec p)const{
    if(p==Uczestnik::W){std::cout << "nr: 0" << std::endl;}
    else if(p==Uczestnik::K){
        const Node* node=head.R;
        while(node!=&head){
            if(node->uczestnik->plec==Uczestnik::K){std::cout <<"nr: " <<node->id<<std::endl;}
            node=node->R;
        }
    }
    else if(p==Uczestnik::M){
        const Node* node=head.R;
        while(node!=&head){
            if(node->uczestnik->plec==Uczestnik::M){std::cout <<"nr: " <<node->id<<std::endl;}
            node=node->R;
        }
    }
}
unsigned int Wodzirej::liczba() const{
return Wodzirej::licznik_k+Wodzirej::licznik_m+1;
}
const Uczestnik* Wodzirej::wodzirej()const{
return head.uczestnik;
}
bool Wodzirej::is_uczestnik(Uczestnik* uczestnik){
    Node* node=head.L;
    while(node!=&head){
        if(node->uczestnik==uczestnik){return true;}
        node=node->L;
    }
return false;
}
