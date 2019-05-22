/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class Grupo {
    int id;
    String nombre;
    ArrayList<Mensaje> mensajes;

    /**
     * Constructor que recibe los parametros necesarios para crear un grupo
     * @param id
     * @param nombre
     */
    public Grupo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.mensajes=new ArrayList<>();
    }

    /**
     * Constructor vacío que inicializa la lista de mensajes vacía
     */
    public Grupo() {
        this.mensajes = new ArrayList<>();
    }  
}
