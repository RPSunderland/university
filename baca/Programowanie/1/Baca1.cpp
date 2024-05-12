// RafaL Plizga
#include<iostream>
#include <string>
using namespace std;    //jesli zapyta jak usprawnic operacje to powiedziec o pomocniczej tablicy do zapisywania wartosci, ew funkca rekurencyjan

int main(){
int M;
cin >> M;
int Plansza[1024][1024];                //incijalizacja planszy
for(int i=0; i<1024; i++){
    for(int j=0; j<1024; j++){
        Plansza[i][j]=0;
    }
}
bool wykonaj_operacje = true;
string operacja;
cin >> operacja;
while(wykonaj_operacje){                    //glowny while
    if(operacja[0]=='N'){
        int x,y,w;
    cin >> x >> y >> w;                     //trwyialne wypisanie
    Plansza[x][y]=w;
    }
    if(operacja[0]=='L'){
        for(int i=0; i<M; i++){             //przechodzimy for-em po wierszach      !!uwaga czasem moge najpierw sumowac ale idea ta sama!
        bool znacznikL = true;
        while(znacznikL){                   //warunek stopu
            bool znacznikLp = true;         // czujnik ktory sprawdza czy na koncu s
            for(int j=0; j<M; j++){
                while(Plansza[i][j]==0 && znacznikLp){
                    int temp=Plansza[i][j];
                    for(int k=j; k<M-1; k++){               //przesuwanie wiersza w lewo, tj. gdy trafimy zero to nadpisujemy wszystkie elementy po tym zerze
                        Plansza[i][k]=Plansza[i][k+1];
                    }
                    Plansza[i][M-1]=temp;
                    znacznikLp=false;                           //znacznik sprawdzajacy czy przypadkiem nie ma juz dalej samych zer
                    for(int l=j; l<M; l++){
                        if(Plansza[i][l]!=0){znacznikLp=true;}
                    }
                }
            }
            for(int j=0; j<M-1; j++){
                if(Plansza[i][j]==Plansza[i][j+1]){Plansza[i][j]=Plansza[i][j]+Plansza[i][j+1]; Plansza[i][j+1]=0;} //sumowanie wiersza
            }
            znacznikL=false;
            for(int j=0; j<M-1; j++){
                if(Plansza[i][j]==Plansza[i][j+1]&&Plansza[i][j]!=0){znacznikL=true;}
            }                                                                               //warunki stopu czyli gdy sa jeszcze elementy do zsumowania
            for(int j=0; j<M-1 ;j++){
                if(Plansza[i][j]==0 && Plansza[i][j+1]!=0){znacznikL=true;}                 //tutaj gdy jest jeszcze zero a po nim cos roznego od zera np 1024
            }
       }
    }
    }
    if(operacja[0]=='P'){
        for(int i=0; i<M; i++){                                 //analogicznie tylko w prawo dzialamy
        bool znacznikP = true;
        while(znacznikP){
            bool znacznikPp = true;
            for(int j=M-1; j>0; j--){
                while(Plansza[i][j]==0 && znacznikPp){
                    int temp=Plansza[i][j];
                    for(int k=j; k>0; k--){
                        Plansza[i][k]=Plansza[i][k-1];
                    }
                    Plansza[i][0]=temp;
                    znacznikPp=false;
                    for(int k=M-1; k>0; k--){
                        if(Plansza[i][k]==0 && Plansza[i][k-1]!=0){znacznikPp=true;}
                    }
                }
            }
            for(int j=M-1; j>0; j--){
                if(Plansza[i][j]==Plansza[i][j-1]){Plansza[i][j]=Plansza[i][j]+Plansza[i][j-1]; Plansza[i][j-1]=0;}
            }
            znacznikP=false;
            for(int j=M-1; j>0; j--){
                if(Plansza[i][j]==Plansza[i][j-1]&&Plansza[i][j]!=0){znacznikP=true;}
            }
            for(int j=M-1; j>0 ;j--){
                if(Plansza[i][j]==0 && Plansza[i][j-1]!=0){znacznikP=true;}
            }

        }

    }
    }
    if(operacja[0]=='G'){
        for(int j=0; j<M; j++){                                     //dzialamy po kolumnach
        bool znacznikG = true;
        while(znacznikG){
            bool znacznikGp = true;
            for(int i=0; i<M; i++){
                while(Plansza[i][j]==0 && znacznikGp){
                    int temp=Plansza[i][j];
                    for(int k=i; k<M-1; k++){
                        Plansza[k][j]=Plansza[k+1][j];
                    }
                    Plansza[M-1][j]=temp;
                    znacznikGp=false;
                    for(int l=i; l<M; l++){
                        if(Plansza[l][j]!=0){znacznikGp=true;}
                    }
                }
            }
            for(int i=0; i<M-1; i++){
                if(Plansza[i][j]==Plansza[i+1][j]){Plansza[i][j]=Plansza[i][j]+Plansza[i+1][j]; Plansza[i+1][j]=0;}
            }
            znacznikG=false;
            for(int i=0; i<M-1; i++){
                if(Plansza[i][j]==Plansza[i+1][j]&&Plansza[i][j]!=0){znacznikG=true;}
            }
            for(int i=0; i<M-1 ;i++){
                if(Plansza[i][j]==0 && Plansza[i+1][j]!=0){znacznikG=true;}
            }
       }
    }
    }
    if(operacja[0]=='D'){
        for(int j=0; j<M; j++){
        bool znacznikD = true;
        while(znacznikD){
            bool znacznikDp = true;
            for(int i=M-1; i>0; i--){
                while(Plansza[i][j]==0 && znacznikDp){
                    int temp=Plansza[i][j];
                    for(int k=i; k>0; k--){
                        Plansza[k][j]=Plansza[k-1][j];
                    }
                    Plansza[0][j]=temp;
                    znacznikDp=false;
                    for(int k=M-1; k>0; k--){
                        if(Plansza[k][j]==0 && Plansza[k-1][j]!=0){znacznikDp=true;}
                    }
                }
            }
            for(int i=M-1; i>0; i--){
                if(Plansza[i][j]==Plansza[i-1][j]){Plansza[i][j]=Plansza[i][j]+Plansza[i-1][j]; Plansza[i-1][j]=0;}
            }
            znacznikD=false;
            for(int i=M-1; i>0; i--){
                if(Plansza[i][j]==Plansza[i-1][j]&&Plansza[i][j]!=0){znacznikD=true;}
            }
            for(int i=M-1; i>0 ;i--){
                if(Plansza[i][j]==0 && Plansza[i-1][j]!=0){znacznikD=true;}
            }

        }

    }
    }
    if(operacja[0]=='S'){       //trywialna suma
        long long int suma=0;
    for(int i=0; i<M; i++){
        for(int j=0; j<M; j++){
            suma=suma+Plansza[i][j];
        }
    }
    cout << suma << endl;
    }
    if(operacja[0]=='W'){       //trywialne wypisanie
        for (int i=0; i<M; i++){
    for(int j=0; j<M; j++){
        cout << Plansza[i][j] << " ";
    }
    cout << endl;
    }
    }
    if(operacja[0]=='C'){
        int temp = M;
        int M_;
        cin >> M_;
        M=M_;
        if(M>=temp){
             for(int i=0; i<M; i++){            //rozbicie na dwa przypadki, gdy nowa plansza jest mniejsza lub wieksza od starej i rozpatrzenie obu przypadkow
                for(int j=0; j<M; j++){
                    cin >> Plansza[i][j];
                }
            }
        }
        if(M<temp){
            for(int i=0; i<M; i++){
                for(int j=0; j<M; j++){
                    cin >> Plansza[i][j];
                }
            }
            for(int i=M; i<temp; i++){
                for(int j=M; j<temp; j++){
                    Plansza[i][j]=0;
                }
            }
         }
    }
    if(operacja[0]=='K'){
       long long int suma=0;
        for(int i=0; i<M; i++){
            for(int j=0; j<M; j++){
                suma=suma+Plansza[i][j];    //zakonczenie i wypisanie sumy
            }
         }
    cout << suma << endl;
    return 0;
    }
wykonaj_operacje = false;
operacja="X";
cin >> operacja;
    if(operacja[0]=='N' || operacja[0]=='L' || operacja[0]=='P' || operacja[0]=='G' || operacja[0]=='D' || operacja[0]=='S' || operacja[0]=='W' || operacja[0]=='C' || operacja[0]=='K'){
    wykonaj_operacje=true;
    }
}
return 0;
}

