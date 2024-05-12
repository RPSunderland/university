// Rafal Plizga
package zadanie4.sensory;

import zadanie4.SprawdzajacySensory;
import java.util.Vector;

public abstract class SensorObserwowanyImpl extends java.lang.Object implements SensorObserwowany {
    public SensorObserwowanyImpl(){
        sprawdzajacy = new Vector<>();
    }

    public void dodajObserwartoraSensorow(SprawdzajacySensory sprawdzajacySensory){
        sprawdzajacy.add(sprawdzajacySensory);
    }
    public void wyczyscObserwatorowSensorow(){
        sprawdzajacy.clear();
    }
    protected void powiadom(){
        for(SprawdzajacySensory sprawdzajacySensory : sprawdzajacy){
            sprawdzajacySensory.sprawdzSensory();
        }
    }

    private Vector<SprawdzajacySensory> sprawdzajacy;
}
