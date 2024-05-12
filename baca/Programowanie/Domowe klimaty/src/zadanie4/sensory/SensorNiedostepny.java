// Rafal Plizga
package zadanie4.sensory;

public class SensorNiedostepny extends java.lang.Exception implements java.io.Serializable{
    public SensorNiedostepny(){}
    public SensorNiedostepny(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private String message;
}

