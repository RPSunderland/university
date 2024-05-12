// Rafal Plizga
package zadanie4.czas;
public class PoraDoby extends java.lang.Object{
    public PoraDoby(){}
    public PoraDoby(int poczatekSekunda, int koniecSekunda){
        this.poczatekSekunda = poczatekSekunda;
        this.koniecSekunda = koniecSekunda;
    }

    public int dlugosc(){
        if(poczatekSekunda > koniecSekunda){
            return 86400 - poczatekSekunda + koniecSekunda;
        }
        return koniecSekunda - poczatekSekunda;
    }
    public int pobierzKoniec(){return koniecSekunda;}
    public int pobierzPoczatek(){return poczatekSekunda;}
    public void ustawKoniec(int koniecSekunda){
        this.koniecSekunda = koniecSekunda;
    }
    public void ustawPoczatek(int poczatekSekunda){
        this.poczatekSekunda = poczatekSekunda;
    }
    public boolean zawieraSie(int sekundaDnia){
        if(poczatekSekunda > koniecSekunda){
              if(poczatekSekunda <= sekundaDnia || sekundaDnia < koniecSekunda){
                  return true;
              }
              return false;
        }
        if(poczatekSekunda <= sekundaDnia && sekundaDnia < koniecSekunda){
            return true;
        }
        return false;
    }

    private int poczatekSekunda;
    private int koniecSekunda;
}
