// Rafal Plizga
package zadanie4.ogrzewanie;

import zadanie4.UzywajacyZegara;
import zadanie4.czas.PoraDoby;
import zadanie4.czas.PrzelicznikCzasu;
import zadanie4.sensory.SensorNiedostepny;
import zadanie4.sensory.Zegar;

import java.util.Vector;

public class ZmiennotemperaturoweSterowanieOgrzewaniem extends StalotemperaturoweSterowanieOgrzewaniem implements UzywajacyZegara {
    public ZmiennotemperaturoweSterowanieOgrzewaniem(){
        super();
        poryDoby = new Vector<>();
        okresyTemperatury = new Vector<>();
        isSetZegar = false;
    }
    public void dodajOkresZezmienionaTemperatura(PoraDoby poraDoby, double okresowaTemperatura) throws NiejednoznaczaTemperatura{
        for(int i = 0; i < poryDoby.size(); i++){
            if(poryDoby.get(i).zawieraSie(poraDoby.pobierzPoczatek()) || poryDoby.get(i).zawieraSie(poraDoby.pobierzKoniec())){
                if(okresyTemperatury.get(i) != okresowaTemperatura){
                    throw new NiejednoznaczaTemperatura(poryDoby.get(i), "");
                }
            }
        }
        poryDoby.add(poraDoby);
        okresyTemperatury.add(okresowaTemperatura);
    }       //nie wiadomo co wpisac w polu message exception!!!
    public void ustawZegar(Zegar zegar){
        this.zegar = zegar;
        isSetZegar = true;
    }

    @Override
    protected boolean isSterowanieUstawione() {
        if(isSetZegar){
            return super.isSterowanieUstawione();
        }
        return false;
    }
    @Override public void sprawdzSensory(){
        if(!isSterowanieUstawione()){
            return;
        }
        try{
            temperaturaZewnetrzna = termometrZewnetrzny.pobierzTemperature();
            temperaturaWewnetrzna = termometrWewnetrzny.pobierzTemperature();
            aktualnyCzas = PrzelicznikCzasu.sekundaDnia(zegar.pobierzCzas());
            okresowaTemperatura = docelowaTemperatura;
            for(int i = 0; i < poryDoby.size(); i++){
                if(poryDoby.get(i).zawieraSie(aktualnyCzas)){
                    okresowaTemperatura = okresyTemperatury.get(i);
                    break;
                }
            }
            if(temperaturaWewnetrzna >= okresowaTemperatura && okresowaTemperatura - temperaturaZewnetrzna < minimalnaRoznica){
                sterownikKotla.ustawZadanaTemperature(sterownikKotla.pobierzMinimalnaTemperatureWlaczenia() - 1); //byc moze musi byc ostro mniejsza
                return;
            }
            if(temperaturaWewnetrzna < okresowaTemperatura && okresowaTemperatura - temperaturaZewnetrzna < minimalnaRoznica){
                sterownikKotla.ustawZadanaTemperature((int)((okresowaTemperatura - temperaturaWewnetrzna) * wspolczynnik + sterownikKotla.pobierzMinimalnaTemperatureWlaczenia()));
                return;
            }
            sterownikKotla.ustawZadanaTemperature((int)((okresowaTemperatura - temperaturaZewnetrzna - minimalnaRoznica) * wspolczynnik + sterownikKotla.pobierzMinimalnaTemperatureWlaczenia()));
        }catch (SensorNiedostepny sensorNiedostepny){
            System.out.println(sensorNiedostepny.getMessage());
        }
    }

    private int aktualnyCzas;
    private Zegar zegar;
    private boolean isSetZegar;
    private double okresowaTemperatura;
    private Vector<PoraDoby> poryDoby;
    private Vector<Double> okresyTemperatury;
}
