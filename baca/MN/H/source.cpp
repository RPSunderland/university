//Rafal Plizga
#include "source.h"

spline::spline(int m) : n(m - 1), X(n + 1), Y(n + 1), A(n), B(n), C(n + 1), D(n), H(n), U(n), V(n), W(n - 1) { }

void spline::set_points(double x[], double y[]) {
    for(int i = 0; i <= n; ++i) {
        X[i] = x[i];
        Y[i] = y[i];
    }
    compute();
}

double spline::operator() (double t) const {
    int i = 0;
    while(i < n && t > X[i + 1]) {
        ++i;
    }
    double diff = t - X[i];
    return A[i] + B[i] * diff + C[i] / 2 * diff * diff + D[i] / 6 * diff * diff * diff;
}

double spline::divided_differences(const std::vector<double>& X, const std::vector<double>& Y, int begin, int end) {
    if(begin == end) return Y[begin];
    return (divided_differences(X, Y, begin + 1, end) - divided_differences(X, Y, begin, end - 1)) / (X[end] - X[begin]);
}

void spline::compute() {
    compute_H();
    compute_U();
    compute_W();
    compute_V();
    compute_C();
    compute_A();
    compute_B();
    compute_D();
}

void spline::compute_A() {
    for(int i = 0; i <= n - 1; ++i) {
        A[i] = Y[i];
    }
}

void spline::compute_B() {
    for(int i = 0; i <= n - 1; ++i) {
        B[i] = divided_differences(X, Y, i, i + 1) - H[i] * (C[i + 1] + 2 * C[i]) / 6;
    }
}

void spline::compute_C() {
    std::vector<double> beta(n + 1);
    std::vector<double> gamma(n + 1);
    beta[1] = -W[1] / 2;
    gamma[1] = V[1] / 2;
    for (int i = 2; i <= n - 1; ++i) {
        beta[i] = -W[i] / (U[i] * beta[i - 1] + 2);
    }
    for (int i = 2; i <= n - 1; ++i) {
        gamma[i] = (V[i] - U[i] * gamma[i - 1]) / (U[i] * beta[i - 1] + 2);
    }
    C[n - 1] = gamma[n - 1];
    for(int i = n - 2; i >= 1; --i) {
        C[i] = beta[i] * C[i + 1] + gamma[i];
    }
    for(int i = 1; i <= n - 1; ++i) {
        C[i] *= 6;
    }
    C[0] = 0;
    C[n] = 0;
}

void spline::compute_D() {
    for(int i = 0; i <= n - 1; ++i) {
        D[i] = (C[i + 1] - C[i]) / H[i];
    }
}

void spline::compute_H() {
    for(int i = 0; i <= n - 1; ++i) {
        H[i] = X[i + 1] - X[i];
    }
}

void spline::compute_U() {
    for(int i = 2; i <= n - 1; ++i) {
        U[i] = H[i - 1] / (H[i - 1] + H[i]);
    }
}

void spline::compute_W() {
    for(int i = 1; i <= n - 2; ++i) {
        W[i] = H[i] / (H[i - 1] + H[i]);
    }
}

void spline::compute_V() {
    for(int i = 1; i <= n - 1; ++i) {
        V[i] = divided_differences(X, Y, i - 1, i + 1);
    }
}
