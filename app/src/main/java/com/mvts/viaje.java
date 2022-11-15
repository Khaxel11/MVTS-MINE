package com.mvts;

public class viaje {
    public String getNombreUser() {
        return NombreUser;
    }

    public void setNombreUser(String nombreUser) {
        NombreUser = nombreUser;
    }

    public String getMateriales() {
        return Materiales;
    }

    public void setMateriales(String materiales) {
        Materiales = materiales;
    }

    public String getInicioRuta() {
        return InicioRuta;
    }

    public void setInicioRuta(String inicioRuta) {
        InicioRuta = inicioRuta;
    }

    public String getFinalRuta() {
        return FinalRuta;
    }

    public void setFinalRuta(String finalRuta) {
        FinalRuta = finalRuta;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    //int idViaje;
    String NombreUser;
    String Materiales;
    String InicioRuta;
    String FinalRuta;
    String Fecha;



    public viaje(String NombreUse, String Materiale, String InicioRut, String FinalRut, String Fech) {

        NombreUser = NombreUse;
        Materiales = Materiale;
        InicioRuta = InicioRut;
        FinalRuta = FinalRut;
        Fecha = Fech;
    }
}
