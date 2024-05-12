// Rafal Plizga
package zadanie4.ogrzewanie;
/*
Wyjatek rzucany gdy mamy dwa nakladajace sie okresy czasu majace zdefiniowana rozna temperature
 */
import zadanie4.czas.PoraDoby;

public class NiejednoznaczaTemperatura extends java.lang.Exception implements java.io.Serializable{
    NiejednoznaczaTemperatura(){}
    NiejednoznaczaTemperatura(PoraDoby konflikt, String message){
        this.konflikt = konflikt;
        this.message = message;
    }
    public PoraDoby pobierzKonflikt(){
        return konflikt;
    }
    @Override
    public String getMessage() {
        return message;
    }

    private PoraDoby konflikt;
    private String message;
}
