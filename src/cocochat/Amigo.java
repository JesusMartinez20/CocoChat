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
public class Amigo {
    private int id;
    private String alias;
    
    /**
     * Constructor vacio para inicializar las variables
     */
    public Amigo()
    {
        id = 0;
        alias = "";
    }
  
    /**
     * Constructor que recibe el id y el alias de un amigo para inicializarlo
     * @param id
     * @param alias 
     */
    public Amigo (int id, String alias)
    {
        this.id = id;
        this.alias= alias;
    }

    /**
     * Obtiene el id del amigo
     * @return 
     */
    public int getId() {
        return id;
    }

    /**
     * Coloca el id del amigo
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el alias del amigo
     * @return 
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Obtiene el ID del amigo
     * @param alias 
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
}
