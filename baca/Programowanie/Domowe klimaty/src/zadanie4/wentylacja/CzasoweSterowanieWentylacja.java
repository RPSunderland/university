// Rafal Plizga
package zadanie4.wentylacja;

import zadanie4.SprawdzajacySensory;
import zadanie4.UzywajacyZegara;
import zadanie4.czas.*;
import zadanie4.efektory.SterownikWentylatora;
import zadanie4.sensory.Zegar;

import java.util.Vector;

public class CzasoweSterowanieWentylacja extends SterowanieWentylacja implements UzywajacyZegara {
    public CzasoweSterowanieWentylacja(){
        poryDoby = new Vector<>();
    }
    public void sprawdzSensory(){
        aktualnyCzas = PrzelicznikCzasu.sekundaDnia(zegar.pobierzCzas());
        for(PoraDoby poraDoby : poryDoby){
            if(poraDoby.zawieraSie(aktualnyCzas)){
                sterownikWentylatora.ustawWlaczenie(true);
                return;
            }
        }
        sterownikWentylatora.ustawWlaczenie(false);
    }
    public void ustawZegar(Zegar zegar){
        this.zegar = zegar;
    }
    public void dodajOkresWlaczenia(PoraDoby okresWlaczenia){
        for(PoraDoby poraDoby : poryDoby){
            if(poraDoby.pobierzPoczatek() == okresWlaczenia.pobierzPoczatek() && poraDoby.pobierzKoniec() == okresWlaczenia.pobierzKoniec() ){
                return;
            }
        }
        poryDoby.add(okresWlaczenia);
    }
    public PoraDoby[] pobierzOkresyWlaczenia(){
        return poryDoby.toArray(new PoraDoby[0]);
    }
    public void usunOkresWlaczenia(PoraDoby okres){
        poryDoby.remove(okres);
    }

    private Vector<PoraDoby> poryDoby;
    private Zegar zegar;
    private int aktualnyCzas;
}

//w metodzie dodajOkresWlaczenia moga byc duble