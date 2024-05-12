#include <iostream>
// Jeżeli odkomentujemy poniższą linię to indeksy (assert) nie będą sprawdzane
// #define  NDEBUG
#include "vectalg.h"
using namespace std;

Vector Jacobi(const Matrix &A, const Vector &b, const Vector & _x0, int M) {// Jacobi
    Vector x0 = _x0;
    auto n = A.size();
    Vector x1(n);
    for(int iter=0; iter < M; iter++) {
        for (int i = 0; i < n; i++) {
            auto s = b[i];
            for (int m = 0; m < i; m++) {   //pomijamy i-ty element
                s -= A(i, m) * x0[m];
            }
            for (int m = i + 1; m < n; m++) {
                s -= A(i, m) * x0[m];
            }
            x1[i] = s / A(i, i);
        }
        cout << x1 << endl;
        auto r = residual_vector(A,b,x1);
        cout <<" r = " << r.max_norm() << endl;
        x0 = x1;

    }
    return x1;
}
Vector GaussSeidel(const Matrix &A, const Vector &b, const Vector & _x0, int M) {// Jacobi
    Vector x0 = _x0;
    auto n = A.size();

    for(int iter=0; iter < M; iter++) {
        for (int i = 0; i < n; i++) {
            auto s = b[i];
            for (int m = 0; m < i; m++) {
                s -= A(i, m) * x0[m];
            }
            for (int m = i + 1; m < n; m++) {
                s -= A(i, m) * x0[m];
            }
            x0[i] = s / A(i, i);
        }
        cout << x0 << endl;
        auto r = residual_vector(A,b,x0);
        cout <<" r = " << r.max_norm() << endl;
    }
    return x0;
}
Vector SOR(const Matrix &A, const Vector &b, const Vector & _x0, int M, double w ) {// Jacobi
    Vector x0 = _x0;
    auto n = A.size();

    for(int iter=0; iter < M; iter++) {
        for (int i = 0; i < n; i++) {
            auto s = b[i];
            for (int m = 0; m < i; m++) {
                s -= A(i, m) * x0[m];
            }
            for (int m = i + 1; m < n; m++) {
                s -= A(i, m) * x0[m];
            }
            x0[i] = (1-w) * x0[i] + w* s / A(i, i);
        }
        cout << x0 << endl;
        auto r = residual_vector(A,b,x0);
        cout <<" r = " << r.max_norm() << endl;
    }
    return x0;
}
int main() {
    Matrix A {{4., 2., -3},
              {2,-8, 5},
              {3, -8, 7}};
    Vector b {8, 4, 5};
    cout << A << endl;
    cout << b << endl;
    Vector x0 {1, 0, -2};

    int M = 30;
 //   Jacobi(A, b, x0, M); // 1.8323 -0.312503 -0.429979
  //  GaussSeidel(A, b, x0, M); //1.83698 -0.302956 -0.419229
    SOR(A, b, x0, M, 1.3);
    return 0;
}
