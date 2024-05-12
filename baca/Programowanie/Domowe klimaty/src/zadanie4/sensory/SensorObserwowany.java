// Rafal Plizga
package zadanie4.sensory;

import zadanie4.SprawdzajacySensory;

public interface SensorObserwowany {
    void dodajObserwartoraSensorow(SprawdzajacySensory sprawdzajacy);
    void wyczyscObserwatorowSensorow();
}
