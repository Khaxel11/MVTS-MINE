package com.mvts;

public class semaforo {
    private double latitud;
    private double longitud;
    private String description;
    private int estado;
    public semaforo(){

    }
    public semaforo(double latitud, double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
        this.description = "TU UBICACIÓN";
    }
    public semaforo(double latitud, double longitud, String description, int estado){
        this.latitud = latitud;
        this.longitud = longitud;
        this.description = description;
        this.estado = estado;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Desc){this.description = Desc;}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado){this.estado = estado;}
}
