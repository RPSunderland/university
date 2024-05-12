// Rafal Plizga
#include <iostream>
#include <cstring>
#include <cstdlib>
#include <ctime>
using namespace std;

int main(){
int N,M,r,s1,s2,d,a,n,m,k;
cin >> N;
int** T = (int**)malloc(sizeof(int*)*N);                //stworzenie glownej tablicy
int* Tp = (int*)malloc(sizeof(int)*N);                  //stworzenie tablicy pomocniczej zliczajcej ilosc wierszy i elementow w wierszach
for(int i=0; i<N; i++){
    cin >> M;
    *(T+i)=(int*)malloc(sizeof(int)*M);                 //stworzenie poszczeg�lnych wers�w w tablcy g�ownej
    for(int j=0; j<M; j++){
        cin >> *(*(T+i)+j);
    }
    *(Tp+i)=M;
}
bool wykonaj_operacje = true;
char x;
cin >> x;
while(wykonaj_operacje){
    if(x == 'P'){                           //trywialna operacja wypisania
        for(int i=0; i<N; i++){
            for(int j=0; j<*(Tp+i); j++){
                cout << *(*(T+i)+j) << " ";
            }
            cout << endl;
        }
        cout << endl;
    }
    if(x == 'R'){                       //usuniecie ostatniego wierszza polegajace na poprzepinaniu wierszy i realokowaniu tablicy, uwaga!! mo�e byc inny wariant
        cin >> r;                         //korzystajacy z malloca, wtedy nulluje i zwalniam ostatni wers
        for(int i=r; i<N-1; i++){
            *(T+i)=*(T+i+1);
            *(Tp+i)=*(Tp+i+1);
        }
        T=(int**)realloc(T,sizeof(int*)*(N-1));
        Tp=(int*)realloc(Tp,sizeof(int)*(N-1));
        N--;
    }
    if(x == 'S'){
        cin >> s1 >> s2;                    //tryuwialne przepinanie wierszy, jest kilka kombinacji ale chodzi o zmiane rozmiarow wierszy i zwolnienie pamieci, realloc nie dzialal tu chyba
        int* temp1;
        int tempp1;
        int* temp2;
        temp1 = *(T+s1);
        temp2 = *(T+s2);
        tempp1=*(Tp+s1);
        *(T+s1)=(int*)malloc(sizeof(int)*(*(Tp+s2)));
        *(T+s2)=(int*)malloc(sizeof(int)*(*(Tp+s1)));
        *(T+s1)=temp2;
        *(Tp+s1)=*(Tp+s2);
        *(T+s2)=temp1;
        *(Tp+s2)=tempp1;
        temp1 =NULL;
        temp2 =NULL;
        free(temp1);
        free(temp2);

    }
    if(x=='D'){         //trywialne powiekszenie wiersza, potem dwuczesciowa petla aby wpisac wartosci, na koniec zwalnanie tempa
        cin >>d;
        int* temp;
        temp=*(T+d);
        *(T+d)=(int*)malloc(*(Tp+d)*sizeof(int)*2);
        for(int i=0; i<*(Tp+d);i++){
            *(*(T+d)+i)=*(temp+i);
        }
        for(int i=0; i<*(Tp+d);i++){
            *(*(T+d)+i+*(Tp+d))=*(temp+i);
        }
        *(Tp+d)=*(Tp+d)*2;
        temp =NULL;
        free(temp);
    }
    if(x=='A'){             //prosty realloc i nadpisanie ostatniego wiersza
        cin >> a;
        T=(int**)realloc(T,sizeof(int*)*(N+1));
        Tp=(int*)realloc(Tp,sizeof(int)*(N+1));
        N++;
        *(T+N-1)=*(T+a);
        *(Tp+N-1)=*(Tp+a);
    }
    if(x=='I'){
        cin >> n >> m >>k;     //wlasciwie polaczenie funkcji D i R, czyli jakies tempy, trzyczesciowe petle, malloc/realloc i zwalnianie
        int* temp2;
        temp2=*(T+m);
        *(T+m)=(int*)malloc(sizeof(int)*(*(Tp+m)+*(Tp+n)));
        for(int i=0; i<k; i++){
            *(*(T+m)+i)=*(temp2+i);
        }
        for(int i=0; i<*(Tp+n);i++){
            *(*(T+m)+i+k)=*(*(T+n)+i);
        }
        for(int i=k; i<*(Tp+m);i++){
            *(*(T+m)+i+*(Tp+n))=*(temp2+i);
        }
        *(Tp+m)=*(Tp+m)+*(Tp+n);


        for(int i=n; i<N-1; i++){
            *(T+i)=*(T+i+1);
            *(Tp+i)=*(Tp+i+1);
        }
        T=(int**)realloc(T,sizeof(int*)*(N-1));
        Tp=(int*)realloc(Tp,sizeof(int)*(N-1));
        N--;
        temp2 =NULL;
        free(temp2);
    }
    x='E';
    cin >> x;
    if(x=='P'||x=='R'||x=='S'||x=='D'||x=='A'){wykonaj_operacje = true;}
    else if(x =='E'){
        for(int i=0; i<N; i++){         //moze sie pojawic czasem zmienna max, oznaczala ona wszystkie operacje np.
            free(*(T+i));
        }
        free(T);
        free(Tp);
        return 0;
    }
}
return 0;
}
