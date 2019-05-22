package cocochat;


import cocochat.Mensaje;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica
 */
public class Solicitudes_Grupos {
    int id;
    String nombre;

    /**
     * Constructor que asigna el id y el nombre del grupo
     * @param id
     * @param nombre
     */
    public Solicitudes_Grupos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Constructor vacío que instancia la clase
     */
    public Solicitudes_Grupos() {
    }  
}
