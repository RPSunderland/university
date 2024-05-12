//Rafal Plizga
#include <stdio.h>
#include <cmath>
#include <iostream>

typedef void (*FuncPointer)(const double* x, double* y, double* Df);

void printVector(const double* x, unsigned N){
  for(unsigned i=0;i<N;++i)
    printf("%17.17f ",x[i]);
  printf("\n");
}

enum function_type {
  curve, surface, fixed_points
};

void make_inverse(double* Df) {
  //{a, b, c, d}^-1 == 1/(ad - bc) * {d, -b, -c, a}
  double a, b, c, d, L;
  a = Df[0];
  b = Df[1];
  c = Df[2];
  d = Df[3];
  L = 1 / (a * d - b * c); //DZIELENIE PRZEZ 0!

  Df[0] = L * d;
  Df[1] = -L * b;
  Df[2] = -L * c;
  Df[3] = L * a; 
}




bool newton_method(FuncPointer f, double* x, function_type type) {
  if(type == curve) {
    /*
      x = {x0, x1, x2}, x_ = {x0, x1}
      y = {y0, y1}, df = 3 x 2 
    */

    double y[2];
    double Df[6];
    f(x, y, Df);

    double x_[2] = {x[0], x[1]};
    double Df_[4] = {Df[0], Df[1], Df[3], Df[4]};

    double x_prev[2];
    double diff;
    double diff_prev = INFINITY;

    while(std::max(std::abs(y[0]), std::abs(y[1])) >= 1e-14) {
      
      x_prev[0] = x_[0];
      x_prev[1] = x_[1];

      if(Df_[0] * Df_[3] - Df_[1] * Df_[2] == 0) return false;
      make_inverse(Df_);
      x_[0] -= (Df_[0] * y[0] + Df_[1] * y[1]);
      x_[1] -= (Df_[2] * y[0] + Df_[3] * y[1]);


      diff = std::max(std::abs(x_prev[0] - x_[0]), std::abs(x_prev[1] - x_[1]));
      if(diff >= 5 * diff_prev) return false;
      diff_prev = diff;

      x[0] = x_[0];
      x[1] = x_[1];
      f(x, y, Df);

      x_[0] = x[0];
      x_[1] = x[1];

      Df_[0] = Df[0];
      Df_[1] = Df[1];
      Df_[2] = Df[3];
      Df_[3] = Df[4];
    }

    return true;
  
  }

  if(type == surface) {
    
      /*
      x = {x0, x1, x2}, x_ = {x0}
      y = {y0}, df = 3 x 1
      */

    double y[1]; 
    double Df[3];
    f(x, y, Df);

    double x_ = x[0]; 
    double Df_ = Df[0];

    double x_prev;
    double diff;
    double diff_prev = INFINITY;

    while(std::abs(y[0]) >= 1e-14) {
      x_prev = x_;

      if(Df_ == 0) return false;
      x_ -= y[0] / Df_; //DZIELENIE PRZEZ 0!


      diff = std::abs(x_prev - x_);
      if(diff >= 5 * diff_prev) return false;
      diff_prev = diff;

      x[0] = x_;
      f(x, y, Df);
      x_ = x[0];
      Df_ = Df[0];
    }

    return true;
  }

  if(type == fixed_points) {
      /*
      x = {x0, x1, x2, x3}, x_ = {x0, x1}
      y = {y0, y1}, df = 4 x 2

      szukamy miejsca zerowego g = f(x,y) - (x,y) 
      */

    double y[2];
    double Df[8];
    f(x, y, Df);     

    double x_[2] = {x[0], x[1]};
    double Df_[4] = {Df[0] - 1, Df[1], Df[4], Df[5] - 1};

    double x_prev[2];
    double diff;
    double diff_prev = INFINITY;

    while(std::max(std::abs(y[0] - x[0]), std::abs(y[1] - x[1])) >= 1e-14) {
      x_prev[0] = x_[0];
      x_prev[1] = x_[1];

      if(Df_[0] * Df_[3] - Df_[1] * Df_[2] == 0) return false;
      make_inverse(Df_); //DZIELENIE PRZEZ 0!
      x_[0] -= (Df_[0] * (y[0] - x[0]) + Df_[1] * (y[1] - x[1]));
      x_[1] -= (Df_[2] * (y[0] - x[0]) + Df_[3] * (y[1] - x[1]));

      diff = std::max(std::abs(x_prev[0] - x_[0]), std::abs(x_prev[1] - x_[1]));
      if(diff >= 5 * diff_prev) return false;
      diff_prev = diff;

      x[0] = x_[0];
      x[1] = x_[1];
      f(x, y, Df);

      x_[0] = x[0];
      x_[1] = x[1];

      Df_[0] = Df[0] - 1;
      Df_[1] = Df[1];
      Df_[2] = Df[4];
      Df_[3] = Df[5] - 1;
    }

    return true;
  
  }
  return false;
}



int findCurve(FuncPointer f, double* x, unsigned k, double h) {
  //f:R^3 -> R^2, x[2] ustalone
  double init_x[3] = {x[0], x[1], x[2]};
  for(int i = 1; i <= k; ++i) {
    x[0] = init_x[0];
    x[1] = init_x[1];
    x[2] += h;
    if(!newton_method(f, x, curve)) return i; 
    printVector(x, 3);
  }
  return 0;
}

int findSurface(FuncPointer f, double* x, unsigned k1, unsigned k2, double h1, double h2) {
  //f:R^3 -> R, x[1] oraz x[2] ustalone
  double init_x[3] = {x[0], x[1], x[2]};
  for(int i = 1; i <= k1; ++i) {
    x[1] += h1;
    for(int j = 1; j <= k2; ++j) {
      x[0] = init_x[0];
      x[2] += h2;
      if(!newton_method(f, x, surface)) return i * k1 + j; 
      printVector(x, 3);
    }
    printf("\n");
    x[2] = init_x[2];
  }
  return 0;
}

int findFixedPoints(FuncPointer f, double* x, unsigned k1, unsigned k2, double h1, double h2) {
  //f:R^4 -> R^2
  //x[2] oraz x[3] ustalone
  double init_x[4] = {x[0], x[1], x[2], x[3]};
  for(int i = 1; i <= k1; ++i) {
    x[2] += h1;
    for(int j = 1; j <= k2; ++j) {
      x[0] = init_x[0];
      x[1] = init_x[1];
      x[3] += h2;
      if(!newton_method(f, x, fixed_points)) return i * k1 + j;
      printVector(x, 4);
    }
    printf("\n");
    x[3] = init_x[3];
  }

  return 0;
}
