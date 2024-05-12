//Rafal Plizga
#include "vectalg.h"

Vector get_row(const Matrix& A, size_t i) {
    size_t n = A.size();
    Vector row(n);
    for(size_t j = 0; j < n; ++j) {
        row[j] = A(i, j);
    } 
    return row;
}

void swap(Matrix& A, Vector& b, size_t i_1, size_t i_2, size_t k) {
    size_t n = A.size();
    for(size_t j = k; j < n; ++j) {
        std::swap(A(i_1, j), A(i_2, j));
    }
    std::swap(b[i_1], b[i_2]);
}

void swap_max(Matrix& A, Vector& b, Vector& max_norms, size_t k) {
    size_t n = A.size();
    size_t max_i = k;
    double max_el = 0;
    for(size_t i = k; i < n; ++i) {
        if(max_el < std::abs(A(i, k)) / max_norms[i]) { //max_norms[i] != 0 poniewaz uklad ma 1 rozwiazanie
            max_el = std::abs(A(i, k)) / max_norms[i];
            max_i = i;
        }
    }
    swap(A, b, k, max_i, k);
}

void reduce_row(Matrix& A, Vector& b, size_t k, size_t i) {
    size_t n = A.size();
    double ratio = A(i, k) / A(k, k);
    for(int j = k; j < n; ++j) {
        A(i, j) -= ratio * A(k, j);
    }
    b[i] -= ratio * b[k];
}

void reduce_rows(Matrix& A, Vector& b, size_t k) {
    size_t n = A.size();
    for(int i = k + 1; i < n; ++i) {
        reduce_row(A, b, k, i);
    }
}

void gauss_elimination(Matrix& A, Vector& b, Vector& max_norms) {
    size_t n = A.size();
    for(size_t k = 0; k < n; ++k) {
        swap_max(A, b, max_norms, k);
        reduce_rows(A, b, k);
    }
}

void get_solution(Matrix& A, Vector& b, Vector& x) {
    size_t n = A.size();
    for(int i = n - 1; i >= 0; --i) {
        x[i] = b[i];
        for(size_t j = i + 1; j < n; ++j) {
            x[i] -= A(i, j) * x[j];
        }
        x[i] /= A(i, i);
    }
}

void solve(Matrix& A, Vector& b, Vector& x, Vector& max_norms) {
    size_t n = A.size();
    gauss_elimination(A, b, max_norms);
    get_solution(A, b, x);
}

Vector solveEquations(const Matrix& A_, const Vector& b_, double  eps) {     
    size_t n = A_.size();
    Matrix A(A_);   //macierz na ktorej bedziemy operowac
    Vector x(n);    //rozwiazanie, niezinicjalizowane
    Vector b(b_);    //wektor b
    Vector max_norms(n); //wektor norm maksimum dla wierszy
    Vector e(n);    //wektor pomocniczy
    Vector residual(n);    //wektor rezydualny

    for(size_t i = 0; i < n; ++i) { //wyznaczenie norm maksimum dla wierszy
        max_norms[i] = get_row(A, i).max_norm();
    }


    solve(A, b, x, max_norms);

    A = A_;
    b = b_;
    residual = residual_vector(A, b, x);
    while(residual.max_norm() >= eps) {
        solve(A, residual, e, max_norms);  
        for(size_t i = 0; i < n; ++i) {
            x[i] += e[i];
        }
        A = A_;
        b = b_;
        residual = residual_vector(A, b, x);
    }

   
    return x;
}


