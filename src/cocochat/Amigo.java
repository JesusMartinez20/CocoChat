/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.util.ArrayList;

/**
 *
 * @author lghhs
 */
public class Amigo {
    int id;
    String alias;
    ArrayList<Mensaje> mensajes;
    
    /**
     * Constructor que recibe los parametros necesarios para crear un amigo
     * @param id
     * @param alias 
     */
    public Amigo(int id, String alias) {
        this.id = id;
        this.alias = alias;
        this.mensajes=new ArrayList<>();
    }

    /**
     * Constructor vacío que solo inicializa la lista de mensajes vacía
     */
    public Amigo() {
        this.mensajes = new ArrayList<>();
    }  
}
