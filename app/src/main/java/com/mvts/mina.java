package com.mvts;

public class mina {
    private String nombre;
    private double latitud;
    private double longitud;
    public mina(){

    }
    public mina(double latitud, double longitud){
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = "MINA";
    }
    public mina(double latitud, double longitud, String nombre){
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Desc){this.nombre = Desc;}
}
