/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

/**
 *
 * @author lghhs
 */
public class Solicitudes_Amigos {
    int id;
    String nombre;


    /**
     * Constructor que asigna el id y el nombre del amigo
     * @param id
     * @param nombre
     */
    public Solicitudes_Amigos(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Constructor vacío que instancia la clase
     */
    public Solicitudes_Amigos() {
    }  
}
