package zadanie4.efektory;

public class UdawanySterownikKotla implements SterownikKotla {
	int temperatura;
	@Override
	public void ustawZadanaTemperature(int tempWStCelc) {
		temperatura = tempWStCelc;
		//System.out.println("Ustawiono temperaturę " + tempWStCelc);

	}

	@Override
	public int pobierzZadanaTemperature() {
		return temperatura;
	}

	@Override
	public int pobierzMinimalnaTemperatureWlaczenia() {
		return 40;
	}

}
