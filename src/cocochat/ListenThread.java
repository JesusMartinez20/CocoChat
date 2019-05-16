/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lghhs
 */
public class ListenThread implements Runnable{

    Socket clientSocket;
    ArrayList<Amigo> friendsList;
    ArrayList<Grupo> groupsList;
    Chat chat;
    
    public ListenThread(Socket clientSocket, ArrayList<Amigo> friendsList, ArrayList<Grupo> groupsList, Chat chat)
    {
        this.clientSocket = clientSocket;
        this.friendsList = friendsList;
        this.groupsList = groupsList;
        this.chat = chat;
    }
    
    @Override
    public void run() {
        try {
            byte[] bytes;
            String command;
            String[] splitted;
            if(clientSocket.getInputStream().available()==0){
            bytes=new byte[clientSocket.getInputStream().available()];
                    clientSocket.getInputStream().read(bytes);
                    command= new String(bytes);
                    splitted=command.split("<s>");
                    switch(splitted[0]){
                        case "mensaje":{
                            switch(splitted[1]){
                                case "amigo":{
                                    for(int i = 0; i < friendsList.size(); i++)
                                    {
                                        Amigo amigo = new Amigo();
                                        amigo = friendsList.get(i);
                                        if(splitted[2].equals(amigo.alias))
                                        {
                                            Mensaje mensaje = new Mensaje();
                                            mensaje.origen = amigo.alias;
                                            mensaje.texto = splitted[3];
                                            mensaje.tiempo = splitted[4];
                                            amigo.mensajes.add(mensaje);
                                        }
                                    }
                                }
                                case "grupo":{
                                    for(int i = 0; i < groupsList.size(); i++)
                                    {
                                        Grupo grupo = new Grupo();
                                        grupo= groupsList.get(i);
                                        if(splitted[2].equals(grupo.id))
                                        {
                                            Mensaje mensaje = new Mensaje();
                                            mensaje.origen = splitted[3];
                                            mensaje.texto = splitted[4];
                                            mensaje.tiempo = splitted[5];
                                            grupo.mensajes.add(mensaje);
                                        }
                                    }
                                }
                            }
                        }
                        case "solicitud":{
                            switch(splitted[1]){
                                case "amistad":
                                {
                                    
                                }
                                case "grupo":{
                                    
                                }
                            }
                        }
                    }
                    if(chat.last == 1)
                    {
                        chat.showMensajesAmigo(chat.friend);
                    }
                    else if(chat.last == 2)
                    {
                        chat.showMensajesGrupo(chat.group);
                    }
            }
        } catch (IOException ex) {
            Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}