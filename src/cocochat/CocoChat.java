/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lghhs
 */
public class CocoChat
{
    /**
     * @param args the command line arguments
     */
    static String server="192.168.84.123";
    static int port=5000;
    public static void main(String[] args) 
    {   
        try {
            Socket clientSocket=new Socket(server,port);
            PrintStream os=new PrintStream(clientSocket.getOutputStream());
            Log_in login = new Log_in(clientSocket,os);
            login.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(CocoChat.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}
