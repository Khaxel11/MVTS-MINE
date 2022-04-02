package com.mvts;

public class Mapainicio {


    private double latitud;
    private double longitud;
    private String description;
    public Mapainicio(){
        
    }
    public Mapainicio(double latitud, double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
        this.description = "Semaforo";
    }
    public Mapainicio(double latitud, double longitud, String description){
        this.latitud = latitud;
        this.longitud = longitud;
        this.description = description;
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
}
