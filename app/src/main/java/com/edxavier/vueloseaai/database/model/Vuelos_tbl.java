package com.edxavier.vueloseaai.database.model;

/**
 * Created by Eder Xavier Rojas on 20/05/2016.
 */

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;


@Table(database = Vuelos_DB.class)
public class Vuelos_tbl extends BaseModel{
    public Vuelos_tbl() {
    }

    @PrimaryKey(autoincrement = true)
    long id; // package-private recommended, not required

    @Column
    private String logo;
    @Column
    private String linea;
    @Column
    private String vuelo;
    @Column
    private String ciudad;
    @Column
    private String hora;
    @Column
    private String estatus;
    @Column
    private int tipo_vuelo;
    @Column
    private int flujo_vuelo;
    @Column
    private String last_update = new Date().toLocaleString();

    @Column
    private String msg = "PRUEBA";

    public static  int NACIONAL = 0;
    public static  int INTERNACIONAL = 1;
    public static  int LLEGADA = 3;
    public static  int SALIDA = 4;


    public String getAerolinea(String num){
        String linea = "";
        if(num.equals("302.jpg"))
            linea = "American Airlines";
        if(num.equals("303.jpg"))
            linea = "United Airlines";
        if(num.equals("304.jpg") || num.equals("318.jpg"))
            linea = "Copa Airlines";
        if(num.equals("306.jpg"))
            linea = "Avianca Star Alliance";
        if(num.equals("308.jpg"))
            linea = "Delta Airlines";
        if(num.equals("309.jpg"))
            linea = "Air Transat";
        if(num.equals("310.jpg"))
            linea = "Aeromexico";
        if(num.equals("311.jpg"))
            linea = "Spirit Airlines";
        if(num.equals("312.jpg"))
            linea = "Avianca Star Alliance";
        if(num.equals("313.jpg"))
            linea = "CanJet";
        if(num.equals("315.jpg"))
            linea = "Nature Air";
        if(num.equals("317.jpg") || num.equals("348.jpg"))
            linea = "Conviasa";
        if(num.equals("319.jpg"))
            linea = "La Costeña";
        if(num.equals("325.jpg"))
            linea = "Cubana de Aviación";
        if(num.equals("333.jpg"))
            linea = "VECA Airlines";
        if(num.equals("334.jpg")|| num.equals("336.jpg") || num.equals("342.jpg"))
            linea = "Avianca Star Alliance";
        if(num.equals("338.jpg"))
            linea = "Volaris";
        return linea;
    }
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String getVuelo() {
        return vuelo;
    }

    public void setVuelo(String vuelo) {
        this.vuelo = vuelo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public int getTipo_vuelo() {
        return tipo_vuelo;
    }

    public void setTipo_vuelo(int tipo_vuelo) {
        this.tipo_vuelo = tipo_vuelo;
    }

    public int getFlujo_vuelo() {
        return flujo_vuelo;
    }

    public void setFlujo_vuelo(int flujo_vuelo) {
        this.flujo_vuelo = flujo_vuelo;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
