// Rafal Plizga
package zadanie4.ogrzewanie;

import zadanie4.SprawdzajacySensory;
import zadanie4.efektory.SterownikKotla;

public abstract class SterowanieOgrzewaniem extends java.lang.Object implements SprawdzajacySensory {
    public SterowanieOgrzewaniem(){}
    public SterownikKotla pobierzSterownikKotla(){return sterownikKotla;}
    public void ustawSterownikPieca(SterownikKotla sterownikKotla){
        this.sterownikKotla = sterownikKotla;
    }

    protected SterownikKotla sterownikKotla;
}
