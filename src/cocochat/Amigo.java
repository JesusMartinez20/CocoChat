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
public class Amigo {
    int id;
    String alias;
    ArrayList<Mensaje> mensajes;

    public Amigo(int id, String alias) {
        this.id = id;
        this.alias = alias;
        this.mensajes=new ArrayList<>();
    }

    public Amigo() {
        this.mensajes = new ArrayList<>();
    }
    
    
}
