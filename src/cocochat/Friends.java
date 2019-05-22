/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

/**
 *
 * @author Jesús Martínez
 */
public class Friends {
    
    int id;
    String friend;
    
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
     *Obtiene el nombre del amigo 
     * @return 
     */
    public String getFriend() {
        return friend;
    }
    
    /**
     * Coloca el nombre del amigo 
     * @param friend 
     */
    public void setFriend(String friend) {
        this.friend = friend;
    }
    
    
    
}
