//Rafal Plizga
#include <iostream>
#include <cmath>
using namespace std;



double findZero(
    double (*f)(double),  // funkcja ktĂłrej zera szukamy w [a, b] 
    double a,             // lewy koniec przedziaĹ‚u
    double b,             // prawy koniec przedziaĹ‚u
    int M,                // maksymalna dozwolona liczba wywoĹ‚aĹ„ funkcji f
    double eps,           // spodziewana dokĹ‚adnoĹ›Ä‡ zera
    double delta          // wystarczajÄ…cy bĹ‚Ä…d bezwzglÄ™dny wyniku
);



bool run_cond(double (*f)(double), double x_p, double x_a, double f_xa, double eps, double delta, int counter, int M) {  //waruenk stopu 
    if(std::abs(f_xa) < eps || std::abs(x_a - x_p) < delta || counter >= M) return false;
    return true;
}

double bisectionStep(double (*f)(double), double x_p, double x_a) {
    double x_n = x_p / 2.0 + x_a / 2.0;
    return x_n;
}

double secantStep(double (*f)(double), double x_p, double x_a, double f_xp, double f_xa) {
    double x_n = x_a - f_xa * (x_a - x_p) / (f_xa - f_xp);
    return x_n;
}




double findZero(double (*f)(double), double a, double b, int M, double eps, double delta) { 
    double x_p = a;
    double x_a = b;
    double x_n;
    double y_a;
    double y_n;
    double f_xp;
    double f_xa;
    double f_xn;
    double f_ya;
    double f_yn;
    int counter = 0;

    f_xp = f(x_p); ++counter;
    if(!run_cond(f, x_a, x_p, f_xp, eps, delta, counter, M)) return x_p;  //przypadek gdyby x_p bylo msc zerowym  
    
    f_xa = f(x_a); ++counter;
    if(!run_cond(f, x_p, x_a, f_xa, eps, delta, counter, M)) return x_a;  //przypadek gdyby x_a bylo msc zerowym  

    //te same znaki (sieczne)
    while(f_xp * f_xa > 0) {
        x_n = secantStep(f, x_p, x_a, f_xp, f_xa); 
        x_p = x_a;
        x_a = x_n;
        f_xp = f_xa;
        f_xa = f(x_a);  ++counter; 
        if(!run_cond(f, x_p, x_a, f_xa, eps, delta, counter, M)) return x_a;
    }


    //mamy przeciwne znaki (dekkert)
        //x_p oraz x_a maja przeciwne znaki
    if(std::abs(f_xa) <= std::abs(f_xp)) {
        y_a = x_p;
        f_ya = f_xp;
    }
    else {
        y_a = x_a;
        f_ya = f_xa;
        x_a = x_p;
        f_xa = f_xp;
    }
    x_p = y_a;
    f_xp = f_ya;

    double m, s;

    while(run_cond(f, x_p, x_a, f_xa, eps, delta, counter, M)) {
       
       // 1.
        m = bisectionStep(f, x_a, y_a);
        if(f_xa == f_xp) {
            s = m;
        }
        else {
            s = secantStep(f, x_p, x_a, f_xp, f_xa);
        }

        // 2.
        if(std::abs(x_a - x_p) >= 0.1 || (!((x_a <= s && s <= m) || (m <= s && s <= x_a)))) {
            x_n = m;
        }
        else {
            x_n = s;
        }
    

        f_xn = f(x_n); ++counter;
        
        //3
        if(f_xn * f_ya >= 0) {
            y_n = x_a;
            f_yn = f_xa;
        }
        else {
            y_n = y_a;
            f_yn = f_ya;
        }

        //4
        if(y_n != x_a && std::abs(f_yn) < std::abs(f_xn)) {
            std::swap(x_n, y_n);
            std::swap(f_xn, f_yn);
        }

        //5
        
        x_p = x_a;
        f_xp = f_xa;

        x_a = x_n;
        f_xa = f_xn;

        y_a = y_n;
        f_ya = f_yn;

    }
    return x_a;
}

