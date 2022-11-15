package com.mvts;

public class interseccion {
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public com.mvts.semaforos getSemaforos() {
        return semaforos;
    }

    public void setSemaforos(com.mvts.semaforos semaforos) {
        this.semaforos = semaforos;
    }

    String Id;
    String Latitud;
    String Longitud;
    String Clave;
    semaforos semaforos;
}
