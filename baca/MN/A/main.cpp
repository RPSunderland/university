//Rafał Plizga

#include <iostream>
#include <iomanip>
#include <cmath>
#include <cfloat>


template <typename T> int sgn(T val) {
    return (T(0) < val) - (val < T(0));
}

//pomocnicza struktura przechowujaca trojki floatow
struct triple {
	triple() : x1(0), x2(0), x3(0) { }
	triple(float x1, float x2, float x3) : x1(x1), x2(x2), x3(x3) { }
	float x1, x2, x3;
};

triple get_seq(float P, float S) {
	if(P != P || S != S) return triple(0, 0, 0);	//nan
	if(std::abs(P) > FLT_MAX || std::abs(S) > FLT_MAX) return triple(0, 0, 0);	//zbyt duza
	if(std::abs(P) < FLT_MIN || std::abs(S) < FLT_MIN) return triple(0, 0, 0);	//zbyt mala

	if(P == 0) {	//iloczyn równy 0
		if(S > 0) {		
			return triple(S, 0, 0);
		}
		else {		
			return triple(0, 0, 0);
		}
	}	

	if(S == 0) {
		return triple(0, 0, 0); // delta < 0
	}

	/*
	x1 * x2 * x3 = P -> x2 = P^(1/3)
	x1 + x2 + x3 = S -> x2/q + x2 + x2*q = S -> x2(1/q + 1 + q) = S -> 1/q + 1 + q = S / x2 -> 1 + q + q^2 = q*(S / x2) -> q^2 + q(1 - S/x2) +1 = q^2 + q*C +1 == 0
	delta_s = (C^2 - 4)^(1/2) = ((C+2)(C-2))^(1/2)
	q1 = (-C +- delta_s) / 2; dobieramy takie q, korzystajac z funkcji sgn() aby nie zredukowaly sie bity znaczace
	sprawdzamy czy q1 poprawnie okresla ciag geometryczny, jesli nie to kladziemy q2 = 1/q1 i sprawdzamy ponownie
	zwracamy trojke (x1, x2, x3)
	*/
	
	float x1, x2, x3, delta_s, q, C;
	x2 = cbrt(P);
	C = 1 - S / x2;	//wspolczynnik przy q w rownaniu kwadratowym
	delta_s = sqrtf((C - 2) * (C + 2)); 
	q = (-C - sgn(C) * delta_s) / 2;
	x1 = x2 / q;
	x3 = x2 * q;

	if(x1 >= x3) return triple(x1, x2, x3);

	q = 1 / q;
	x1 = x2 / q;
	x3 = x2 * q;
	if(x1 >= x3) return triple(x1, x2, x3);

	return triple(0, 0, 0);
}

int main() {
	int N;
	float P, S;
	triple seq;
	std::cin >> N;
	std::cout.precision(10);
	std::cout << std::scientific;
	for (int i = 0; i < N; ++i) {
		std::cin >> P >> S;
		seq = get_seq(P, S);
		std::cout << seq.x1 << " " << seq.x2 << " " << seq.x3 << "\n";
	}	
	return 0;
}