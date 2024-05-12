#include<iostream>
#include <tuple>
using namespace std;


std::tuple<int, int, int> euk(int a, int b) {
	if(b == 0) {
		return std::make_tuple(a, 1, 0);
	}
	std::tuple<int, int, int> old = euk(b, a % b);
	return std::make_tuple(std::get<0>(old), std::get<2>(old), std::get<1>(old) - (a/b) * std::get<2>(old));
}


int main()
{
	int a, b;
	
	cout<<"Podaj liczby: ";
	cin>>a>>b;
	
	euk(a, b);

	std::tuple<int, int, int> x = euk(a ,b);

	cout<<"nwd("<<a<<", "<<b<<") = "
	<<a<<" * "<<std::get<1>(x)<<" + "<<b <<" * " << std::get<2>(x) <<" = "
	<<std::get<0>(x)<<endl;

	return 0;
}
