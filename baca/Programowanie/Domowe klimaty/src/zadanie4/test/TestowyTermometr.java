package zadanie4.test;


import zadanie4.sensory.SensorNiedostepny;
import zadanie4.sensory.SensorObserwowanyImpl;
import zadanie4.sensory.Termometr;

public class TestowyTermometr extends SensorObserwowanyImpl implements Termometr{
	float ustawionyTemperatura;
	@Override
	public float pobierzTemperature() throws SensorNiedostepny {
		return ustawionyTemperatura;
	}
	
	public void ustawTemperature(double temperatura){
		ustawionyTemperatura = (float)temperatura;
		powiadom();
	}

	

}
