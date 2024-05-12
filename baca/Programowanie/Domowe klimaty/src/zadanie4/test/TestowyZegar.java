package zadanie4.test;

import java.util.Date;

import zadanie4.sensory.SensorObserwowanyImpl;
import zadanie4.sensory.Zegar;


public class TestowyZegar extends SensorObserwowanyImpl implements Zegar{
	Date ustawionyCzas;
	@Override
	public Date pobierzCzas() {
		return ustawionyCzas;
	}
	
	public void ustawCzas(Date czas){
		ustawionyCzas = czas;
		powiadom();
	}
	/*public void dodajMinuty(int liczba){
		ustawionyCzas.setTime(ustawionyCzas.getTime() +  60000 * liczba);
		powiadom();
	}*/
	

}
