package zadanie4.efektory;

public interface SterownikKotla {
	void ustawZadanaTemperature(int tempWStCelc);
	int pobierzZadanaTemperature();
	/**@returns int - minimalna ustawiona temperatura, która oznacza włączenie kotła.
	 * Wszystko, co poniżej oznacza jego wyłączenie.*/
	int pobierzMinimalnaTemperatureWlaczenia();
}
