// Rafal Plizga
package zadanie4.wentylacja;

import zadanie4.SprawdzajacySensory;
import zadanie4.efektory.SterownikWentylatora;

public abstract class SterowanieWentylacja extends java.lang.Object implements SprawdzajacySensory {
    public SterowanieWentylacja(){};
    public SterownikWentylatora jakiSterownikWentylatora(){return sterownikWentylatora;}
    public void ustawSterownikWentylatora(SterownikWentylatora sterownikWentylatora){
        this.sterownikWentylatora = sterownikWentylatora;
    }

    protected SterownikWentylatora sterownikWentylatora;
}

//uwaga bo moze byc wyjatek, ze chcemy pobrac sterownik wentylatora nie ustawiajac go najpierw
