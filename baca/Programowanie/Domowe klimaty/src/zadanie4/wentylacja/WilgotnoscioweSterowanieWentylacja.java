// Rafal Plizga
package zadanie4.wentylacja;

import zadanie4.UzywajacyZegara;
import zadanie4.czas.PoraDoby;
import zadanie4.czas.PrzelicznikCzasu;
import zadanie4.sensory.Higrometr;
import zadanie4.sensory.Zegar;

import java.util.Vector;

public class WilgotnoscioweSterowanieWentylacja extends SterowanieWentylacja implements UzywajacyZegara {
    public WilgotnoscioweSterowanieWentylacja(){
        okresyCzasu = new Vector<>();
        okresyPracy = new Vector<>();
        isSetZegar = false;
        isSetHigrometr = false;
        isSetMaksymalnaPozadanaWilgotnosc = false;
    }
    public void ustawHigrometr(Higrometr higrometr){
        this.higrometr = higrometr;
        isSetHigrometr = true;
    }
    public void ustawZegar(Zegar zegar){
        this.zegar = zegar;
        isSetZegar = true;
    }
    public double getMaksymalnaPozadanaWilgotnosc(){return maksymalnaPozadanaWilgotnosc;}
    public void ustawMaksymalnaPozadanaWilgotnosc(double maksymalnaPozadanaWilgotnosc){
        this.maksymalnaPozadanaWilgotnosc = maksymalnaPozadanaWilgotnosc;
        isSetMaksymalnaPozadanaWilgotnosc = true;
    }
    public void sprawdzSensory(){
        if(!isSterowanieUstawione()){
            return;
        }
        aktualnyCzas = PrzelicznikCzasu.sekundaDnia(zegar.pobierzCzas());
        if(okresyCzasu.isEmpty()){
            koncowyCzas = (aktualnyCzas + 899) % 86399;
            okresyCzasu.add(new PoraDoby(aktualnyCzas, aktualnyCzas));
        }
        else{
            okresyCzasu.add(new PoraDoby(okresyCzasu.lastElement().pobierzKoniec(), aktualnyCzas));
        }
        if(sterownikWentylatora.jestWlaczony()){
            okresyPracy.add(true);
        }
        else{
            okresyPracy.add(false);
        }
        try {
            aktualnaWilgotnosc = higrometr.pobierzWilgotnosc();
            if(aktualnaWilgotnosc > maksymalnaPozadanaWilgotnosc){
                sterownikWentylatora.ustawWlaczenie(true);
                koncowyCzas = (aktualnyCzas + 899) % 86399;
                return;
            }
            if(!sterownikWentylatora.jestWlaczony()){
                int sumaOkresowCzasu = 0;   //zmienna pomocnicza zliczajaca sume wszystkich pobrane okresy czasu
                int sumaOkresowPracy = 0;   //zmienna pomocnicza zliczajaca sume okresow pracy wentylatora
                for(int i = okresyCzasu.size() - 1; i >= 0; i--){
                    sumaOkresowCzasu += okresyCzasu.get(i).dlugosc();
                    if(sumaOkresowCzasu >= 10800){
                        sumaOkresowPracy += sumaOkresowCzasu - 10800;
                        break;
                    }
                    if(okresyPracy.get(i)){
                        sumaOkresowPracy += okresyCzasu.get(i).dlugosc();
                    }
                }
                if(sumaOkresowPracy < 3600){
                    sterownikWentylatora.ustawWlaczenie(true);
                    koncowyCzas = (aktualnyCzas + 899)% 86399;
                    return;
                }
            }
            if(sterownikWentylatora.jestWlaczony()){
                if(aktualnyCzas == koncowyCzas || okresyCzasu.lastElement().zawieraSie(koncowyCzas)){
                    sterownikWentylatora.ustawWlaczenie(false);
                    return;
                }
            }
        } catch (zadanie4.sensory.SensorNiedostepny sensorNiedostepny){
            sterownikWentylatora.ustawWlaczenie(true);
            okresyPracy.add(true);
            System.out.println(sensorNiedostepny.getMessage());
        }
}

    private boolean isSterowanieUstawione(){
        if(isSetZegar && isSetHigrometr && isSetMaksymalnaPozadanaWilgotnosc){
            return true;
        }
        return false;
    }

    private Higrometr higrometr;
    private Zegar zegar;
    private double maksymalnaPozadanaWilgotnosc;
    private double aktualnaWilgotnosc;
    private int koncowyCzas;    //zmienna pomocnicza okreslajca kiedy ma sie wylaczyc wentylator (po 15 - minutach)
    private int aktualnyCzas;
    private Vector<PoraDoby> okresyCzasu;
    private Vector<Boolean> okresyPracy;

    private boolean isSetHigrometr;
    private boolean isSetZegar;
    private boolean isSetMaksymalnaPozadanaWilgotnosc;
}



