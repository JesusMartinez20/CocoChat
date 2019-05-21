/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author lghhs
 */
public class ListenThread extends Thread{

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
        while(true)
        {
        try {
            byte[] bytes;
            String command;
            String[] splitted;
            if(clientSocket.getInputStream().available()!=0){
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
                                        if(Integer.parseInt(splitted[2]) == amigo.id)
                                        {
                                            Mensaje mensaje = new Mensaje();
                                            mensaje.origen = amigo.alias;
                                            mensaje.texto = splitted[3];
                                            mensaje.tiempo = splitted[4];
                                            if(amigo.mensajes == null)
                                            {
                                                amigo.mensajes = new ArrayList<Mensaje>();
                                            }
                                            amigo.mensajes.add(mensaje);
                                        }
                                    }
                                    break;
                                }
                                case "grupo":{
                                    for(int i = 0; i < groupsList.size(); i++)
                                    {
                                        Grupo grupo = new Grupo();
                                        grupo= groupsList.get(i);
                                        if(Integer.parseInt(splitted[2]) == grupo.id)
                                        {
                                            Mensaje mensaje = new Mensaje();
                                            mensaje.origen = splitted[3];
                                            mensaje.texto = splitted[4];
                                            mensaje.tiempo = splitted[5];
                                            if(grupo.mensajes == null)
                                            {
                                                grupo.mensajes = new ArrayList<Mensaje>();
                                            }
                                            grupo.mensajes.add(mensaje);
                                        }
                                    }
                                    break;
                                }
                                case "noamigo":{
                                    for(int i = 0; i < groupsList.size(); i++)
                                    {
                                        Online online = new Online();
                                        online = chat.onlineList.get(i);
                                        if(Integer.parseInt(splitted[2]) == online.id)
                                        {
                                            Mensaje mensaje = new Mensaje();
                                            mensaje.origen = online.name;
                                            mensaje.texto = splitted[3];
                                            Date date= new Date();
                                            long time = date.getTime();
                                            Timestamp ts = new Timestamp(time);
                                            mensaje.tiempo = ts.toString();
                                            if(online.mensajes == null)
                                            {
                                                online.mensajes = new ArrayList<Mensaje>();
                                            }
                                            online.mensajes.add(mensaje);
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        case "solicitud":{
                            switch(splitted[1]){
                                case "amistad":
                                {
                                    Solicitudes_Amigos solicitud = new Solicitudes_Amigos();
                                    solicitud.id = Integer.parseInt(splitted[2]);
                                    solicitud.nombre = splitted[3];
                                    chat.friendsRequests.add(solicitud);
                                    chat.friendsRequestsButtons = chat.initButtonsFriendsReq(chat.friendsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                                case "grupo":{
                                    Solicitudes_Grupos solicitud = new Solicitudes_Grupos();
                                    solicitud.id = Integer.parseInt(splitted[2]);
                                    solicitud.nombre = splitted[3];
                                    chat.groupsRequests.add(solicitud);
                                    chat.groupsRequestButtons = chat.initButtonsGroupsReq(chat.groupsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                                case "amigoaceptada":{
                                    Amigo amigo = new Amigo();
                                    amigo.id = Integer.parseInt(splitted[2]);
                                    amigo.alias = splitted[3];
                                    chat.friendsList.add(amigo);
                                    chat.friendsButtons = chat.initButtonsFriends(chat.friendsList);
                                    chat.restartButtons();
                                }
                            }
                            break;
                        }
                        case "online":{
                            Online online = new Online();
                            online.id = Integer.parseInt(splitted[1]);
                            online.name = splitted[2];
                            chat.onlineList.add(online);
                            for (int i = 0; i < chat.offlineList.size(); i++)
                            {
                                if(chat.offlineList.get(i).id == Integer.parseInt(splitted[1]))
                                {
                                    chat.offlineList.remove(i);
                                }          
                            }
                            chat.onlineButtons = chat.initButtonsOnline(chat.onlineList);
                            chat.offlineButtons = chat.initButtonsOffline(chat.offlineList);
                            chat.restartButtons();
                            break;
                        }
                        case "aceptado":{
                            switch(splitted[1]){
                                case "amistad":
                                {
                                    Amigo amigo = new Amigo();
                                    for (int i = 0; i < chat.friendsRequests.size(); i++)
                                    {
                                        if(chat.friendsRequests.get(i).id == Integer.parseInt(splitted[2]))
                                        {
                                            amigo.id = Integer.parseInt(splitted[2]);
                                            amigo.alias = chat.friendsRequests.get(i).nombre;
                                            chat.friendsRequests.remove(i);
                                        }          
                                    }
                                    chat.friendsList.add(amigo);
                                    chat.friendsButtons = chat.initButtonsFriends(chat.friendsList);
                                    chat.friendsRequestsButtons = chat.initButtonsFriendsReq(chat.friendsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                                case "grupo":{
                                    Grupo grupo = new Grupo();
                                    for (int i = 0; i < chat.groupsRequests.size(); i++)
                                    {
                                        if(chat.groupsRequests.get(i).id == Integer.parseInt(splitted[2]))
                                        {
                                            grupo.id = Integer.parseInt(splitted[2]);
                                            grupo.nombre = chat.groupsRequests.get(i).nombre;
                                            chat.groupsRequests.remove(i);
                                        }          
                                    }
                                    chat.groupsList.add(grupo);
                                    chat.groupsButtons = chat.initButtonsGroups(chat.groupsList);
                                    chat.groupsRequestButtons = chat.initButtonsGroupsReq(chat.groupsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                            }
                            break;
                        }
                        case "rechazado":{
                            switch(splitted[1]){
                                case "amistad":
                                {
                                    for (int i = 0; i < chat.friendsRequests.size(); i++)
                                    {
                                        if(chat.friendsRequests.get(i).id == Integer.parseInt(splitted[2]))
                                        {
                                            chat.friendsRequests.remove(i);
                                        }          
                                    }
                                    chat.friendsRequestsButtons = chat.initButtonsFriendsReq(chat.friendsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                                case "grupo":{
                                    for (int i = 0; i < chat.groupsRequests.size(); i++)
                                    {
                                        if(chat.groupsRequests.get(i).id == Integer.parseInt(splitted[2]))
                                        {
                                            chat.groupsRequests.remove(i);
                                        }          
                                    }
                                    chat.groupsRequestButtons = chat.initButtonsGroupsReq(chat.groupsRequests);
                                    chat.restartButtons();
                                    break;
                                }
                            }
                            break;
                        }
                        case "encontrado":{
                            JOptionPane.showMessageDialog(chat, "Solicitud mandada");
                            break;
                        }
                        case "noencontrado":{
                            JOptionPane.showMessageDialog(chat, "Usuario no encontrado");
                            break;
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
                    else if(chat.last == 3)
                    {
                        chat.showMessagesOnline(chat.nofriend);
                    }
                }
        } catch (IOException ex) {
            Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    
}
