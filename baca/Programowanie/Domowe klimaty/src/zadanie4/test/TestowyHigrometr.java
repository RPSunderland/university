package zadanie4.test;


import zadanie4.sensory.Higrometr;
import zadanie4.sensory.SensorNiedostepny;
import zadanie4.sensory.SensorObserwowanyImpl;

public class TestowyHigrometr 
extends SensorObserwowanyImpl implements Higrometr{
	float ustawionaWilgotnosc;
	@Override
	public float pobierzWilgotnosc() throws SensorNiedostepny {
		return ustawionaWilgotnosc;
	}
	
	public void ustawWilgotnosc(double wilgotnosc){
		ustawionaWilgotnosc = (float) wilgotnosc;
		powiadom();
	}

	

}
