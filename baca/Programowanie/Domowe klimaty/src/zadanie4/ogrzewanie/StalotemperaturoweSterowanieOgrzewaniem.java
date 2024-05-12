// Rafal Plizga
package zadanie4.ogrzewanie;

import zadanie4.SprawdzajacySensory;
import zadanie4.sensory.SensorNiedostepny;
import zadanie4.sensory.Termometr;

public class StalotemperaturoweSterowanieOgrzewaniem extends SterowanieOgrzewaniem {
    public StalotemperaturoweSterowanieOgrzewaniem(){
        isSetTermometrZewnetrzny = false;
        isSetTermometrWewnetrzny = false;
        isSetDocelowaTemperatura = false;
        isSetMinimalnaRoznica = false;
        isSetWspolczynnik = false;
    }
    public void sprawdzSensory(){
       if(!isSterowanieUstawione()){
           return;
       }
       try {
           temperaturaZewnetrzna = termometrZewnetrzny.pobierzTemperature();
           temperaturaWewnetrzna = termometrWewnetrzny.pobierzTemperature();
           if(temperaturaWewnetrzna >= docelowaTemperatura && docelowaTemperatura - temperaturaZewnetrzna < minimalnaRoznica){
               sterownikKotla.ustawZadanaTemperature(sterownikKotla.pobierzMinimalnaTemperatureWlaczenia() - 1); //byc moze musi byc ostro mniejsza
               return;
           }
           if(temperaturaWewnetrzna < docelowaTemperatura && docelowaTemperatura - temperaturaZewnetrzna < minimalnaRoznica){
               sterownikKotla.ustawZadanaTemperature((int)((docelowaTemperatura - temperaturaWewnetrzna) * wspolczynnik + sterownikKotla.pobierzMinimalnaTemperatureWlaczenia()));
               return;
           }
           sterownikKotla.ustawZadanaTemperature((int)((docelowaTemperatura - temperaturaZewnetrzna - minimalnaRoznica) * wspolczynnik + sterownikKotla.pobierzMinimalnaTemperatureWlaczenia()));
       }catch (SensorNiedostepny sensorNiedostepny){
           System.out.println(sensorNiedostepny.getMessage());
       }
    }
    public Termometr pobierzTermometrZewnetrzny(){return termometrZewnetrzny;}
    public void ustawTermometrZewnetrzny(Termometr termometrZewnetrzny){
        this.termometrZewnetrzny = termometrZewnetrzny;
        isSetTermometrZewnetrzny = true;
    }
    public Termometr pobierzTermometrWewnetrzny(){return termometrWewnetrzny;}
    public void ustawTermometrWewnetrzny(Termometr termometrWewnetrzny){
        this.termometrWewnetrzny = termometrWewnetrzny;
        isSetTermometrWewnetrzny = true;
    }
    public double pobierzDocelowaTemperatura(){return docelowaTemperatura;}
    public void ustawDocelowaTemperatura(double docelowaTemperatura){
        this.docelowaTemperatura = docelowaTemperatura;
        isSetDocelowaTemperatura = true;
    }
    public double pobierzMinimalnaRoznica(){return minimalnaRoznica;}
    public void ustawMinimalnaRoznica(double minimalnaRoznica){
        this.minimalnaRoznica = minimalnaRoznica;       //wyjatek (ujemna roznica)
        isSetMinimalnaRoznica = true;
    }
    public double pobierzWspolczynnik(){return wspolczynnik;}
    public void ustawWspolczynnik(double wspolczynnik){
        this.wspolczynnik = wspolczynnik;
        isSetWspolczynnik = true;
    }
    protected boolean isSterowanieUstawione(){
        if(isSetTermometrZewnetrzny && isSetTermometrWewnetrzny && isSetDocelowaTemperatura && isSetMinimalnaRoznica && isSetWspolczynnik){
            return true;
        }
        return false;
    }

    protected Termometr termometrZewnetrzny;
    protected Termometr termometrWewnetrzny;
    protected double temperaturaZewnetrzna;
    protected double temperaturaWewnetrzna;
    protected double docelowaTemperatura;
    protected double minimalnaRoznica;
    protected double wspolczynnik;

     boolean isSetTermometrZewnetrzny;
    protected boolean isSetTermometrWewnetrzny;
    protected boolean isSetDocelowaTemperatura;
    protected boolean isSetMinimalnaRoznica;
    protected boolean isSetWspolczynnik;

}
/*
Byc moze nie jest konieczny termometr wewnetrzny (narazie implementuje ze wszystkie sensory musza byc ustawione)
 */
