#include <iostream>
using namespace std;

int dzielnik(int a, int b){
    int temp;
	while(b!=0)
    {
		temp = b;
		b = a%b;
		a = temp;
	}
    return a;
}
int podzial (int T[], int L, int P) {           //uzyskanie punktu podzialowego
    int pivot = T[P];                           //wartosc prawego przedzialu
    int i = L-1;                                // koñcowy i++ to miejsce gdzie trafi nasz punkt podzialowy
    int temp2;
    for (int j=L; j<P; j++)
    {
        if (T[j]<pivot)                         //jesli jest mniejszy to inkrementujemy i oraz zamieniamy miejscami, dzieki temu elementy mniejsze beda na lewo od punktu podzialowego
        {
            i++;
             temp2=T[i];
             T[i]=T[j];
             T[j]=temp2;
        }
    }                                   //jesli wieksze to nie ruszamy, bo i tak  na koncu zaminiamy pivot  z inddexem podzialowym
    temp2=T[i+1];
    T[i+1]=T[P];
    T[P]=temp2;                            //umiejscowienie pivota (jest juz posortowany)
    return (i + 1);
}
void Sort(int T[], int L, int P) {
    if (L<P)                            //warunek stopu, gdyby nie on sort dzia³al by dalej dla tablic np 1 elemntowych
    {
        int k = podzial(T,L,P);     //na k-tym miejscu tablica jest posortowana, po lewej sa mniejsze a po prawej wieksze
        Sort(T,L,k-1);              //rekurencyjnie sortujemy lew¹ i praw¹ czesc tablicy
        Sort(T,k+1,P);
    }
}
bool expval(unsigned int n, const double* x,const double* p, double (*f)(double), double &r){   //trywialne stosowanie sie do tresci zadania
    double suma=0;
    for(int i=0; i<n; i++){
        if(p[i]<0){return false;}
    }
    for(int i=0; i<n; i++){
        suma+=p[i];
        if(suma>1){return false;}
    }
    r=0.0;
    double sumaU=1;
    for(int i=0; i<n; i++){
        r+=f(x[i])*p[i];
        sumaU-=p[i];
    }
    r+=f(x[n])*sumaU;
    return true;
}
bool median(unsigned int n,const int* t, int(*f)(int), bool(*p)(int), double &r){
    int Z[n];
    int temp=0;
    for(int i=0; i<n; i++){
        if(p(t[i])){Z[temp]=f(t[i]); temp++;}       //sortowanie
    }
    n=temp;
    temp=0;
    if(n==0){return false;}
    Sort(Z,0,n-1);
    int Zp[n];                      //tu bedzie odwolanie do niestniejacej pamieci!!!!!!!
    temp=0;
    for(int i=0; i<n; i++){
        if(Z[i]!=Z[i+1]){Zp[temp]=Z[i]; temp++;}            //usuwanie dupliaktow
    }
    n=temp;
    if(n%2==0){r=(Zp[(n-1)/2]+Zp[n/2])/2.0;}
    else{r=Zp[n/2];}
    return true;
}
unsigned int gcd(unsigned int n,const int* t, int* r=nullptr){
    int Z[n];
    int m=0;
    for(int i=0; i<n; i++){
        if(t[i]!=0){Z[m]=t[i]; m++;}
    }
    if(m==0){ return 0;}
    unsigned int actual_gcd;
    actual_gcd=Z[0];
    for(int i=1; i<m; i++){
        if(Z[i]%actual_gcd!=0){
            if(Z[i]>=0){actual_gcd=dzielnik(Z[i],actual_gcd);}      //nic specjalnego, latwy wzor na gcd
            else{actual_gcd=dzielnik(-Z[i],actual_gcd);}
        }
    }
    if(r!=nullptr){
        for(int i=0; i<n; i++){
            r[i]=t[i]/actual_gcd;
        }
    }
    return actual_gcd;
}
unsigned int count(unsigned int n, const int* t, bool(*p)(int,int)=nullptr){
     int Z[n];
     int t2[n];
     for(int i=0; i<n; i++){
         t2[i]=t[i];
     }
     Sort(t2,0,n-1);
     int temp=0;
     for(int i=0; i<n; i++){
        if(t2[i]!=t2[i+1]){Z[temp]=t2[i]; temp++;}  //analogicznie jak w medianie ale nie bedzie odwolania do niestniejacej pamieci
        }
    n=temp;
    unsigned int suma=0;
    if(p==nullptr){return n*n;}
    for(int i=0; i<n; i++){
        for(int j=0; j<n; j++){
            if(p(Z[i],Z[j])){suma++;}
        }
    }
    return suma;
}

int f(int x) { return x*(x-10); }
bool TRUE(int x) { return true; }
bool EVEN(int x) { return x%2==0; }
bool ASYMMETRIC_REL(int a, int b) { return 3*a<b; }
double g(double x) { return 7*x-2; }
int main(){
double r=0.0;
int t[] = {6,30,12,-81,9,-9,15,6,30,33,21,18};
unsigned n = sizeof(t)/sizeof(int);
cout << boolalpha;
cout << median(n,t,f,TRUE,r) << endl;
cout << "median_all=" << r << endl;
cout << median(n,t,f,EVEN,r) << endl;
cout << "median_even=" << r << endl;
cout << "gcd=" << gcd(n,t) << endl;
cout << "count=" << count(n,t,ASYMMETRIC_REL) << endl;
double x[] = {4,3,2,1};
double p[] = {0.125,0.25,0.125};
cout << expval(3,x,p,g,r) << endl;
cout << "expval=" << r << endl;
}







