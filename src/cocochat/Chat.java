/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.io.StringReader;
import javax.swing.ScrollPaneConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Jesús Martínez
 */
public class Chat extends JFrame{
   int nFriends=2, nGroups=2, nOnline=2, nOffline=2;
   
   JLabel header=new JLabel();
   JTextField description=new JTextField();
   JTextField friends=new JTextField();
   JTextField groups=new JTextField();
   JTextField online=new JTextField();
   JTextField offline=new JTextField();
   JTextField chatPanel=new JTextField();
   JTextField message=new JTextField();
   
   JButton send=new JButton("Enviar");
   JButton requests=new JButton("Solicitudes");
   JButton [] friendsButtons=initButtons(nFriends);
   JButton [] groupsButtons=initButtons(nGroups);
   JButton [] onlineButtons=initButtons(nOnline);
   JButton [] offlineButtons=initButtons(nOffline);
   Socket clientSocket = null;
   PrintStream os = null;
   
   JScrollPane scrollChat = new JScrollPane();
   JScrollPane scrollSide = new JScrollPane();
   
   JList list = new JList();
   
   int y=0;
   String user="jesus";
   ArrayList<Amigo> friendsList = new ArrayList<>();
   byte [] groupsList;
   byte [] messages;
   byte [] requestList;
    
    Chat(Socket clientSocket,PrintStream os){
        this.clientSocket = clientSocket;
        this.os = os;
        friendsList();
      //  groupsList();
        //messages();
        //requestList();
        this.setPreferredSize(new Dimension(900,900));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.pink);  
        requests.setSize(200,50);
        header.setText("CocoChat");
        description.setText("chat");
        description.setEditable(false);
        friends.setText("Amigos");
        friends.setEditable(false);
        groups.setText("Grupos");
        groups.setEditable(false);
        online.setText("Online");
        online.setEditable(false);
        offline.setText("Offline");
        offline.setEditable(false);
        chatPanel.setText("mensajes");
        chatPanel.setEditable(false);
        scrollChat.setViewportView(chatPanel);
        scrollSide.setViewportView(list);
        initSide(friends,friendsButtons);
        initSide(groups,groupsButtons);
        initSide(online,onlineButtons);
        initSide(offline,offlineButtons);
        y+=50;
        list.add(requests);
        list.setPreferredSize(new Dimension(300,1000));
        scrollSide.setViewportView(list);
        scrollSide.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setViewportView(chatPanel);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        
        GroupLayout view= new GroupLayout(this.getContentPane());
               
        view.setHorizontalGroup(
                view.createParallelGroup()
                    .addComponent(header,700,700,700)
                    .addGroup(view.createSequentialGroup()
                            .addComponent(scrollSide,200,200,200)
                            .addGroup(view.createParallelGroup()
                                    .addComponent(description,500,500,500)
                                    .addComponent(scrollChat,500,500,500)
                                    .addGroup(view.createSequentialGroup()
                                            .addComponent(message,400,400,400)
                                            .addComponent(send,100,100,100)
                                    )
                            )
                            
                    )
        );
        
        view.setVerticalGroup(
                view.createSequentialGroup()
                    .addComponent(header,100,100,100)
                    .addGroup(view.createParallelGroup()
                            .addComponent(scrollSide)
                            .addGroup(view.createSequentialGroup()
                                    .addComponent(description,50,50,50)
                                    .addComponent(scrollChat)
                                    .addGroup(view.createParallelGroup()
                                            .addComponent(message,50,50,50)
                                            .addComponent(send,50,50,50)
                                    )
                            )
                            
                    )
        );
        
        this.setLayout(view);
        this.pack();
        view.setAutoCreateContainerGaps(true);
        view.setAutoCreateGaps(true);
    }
    
   public void initSide(JTextField label, JButton[] buttons){
        y+=50;
        label.setBounds(0,y,200, 50);
        list.add(label);
        
        for ( int i = 0; i < buttons.length; i++ ) {
            y+=50;
            buttons[i].setBounds(0, y, 200, 50);
                list.add(buttons[i]);
              
        }
    }
    
    public JButton[] initButtons(int n){
        JButton [] botones = new JButton[n];
        for (int i = 0; i < n; i++) 
        {
                botones[i] = new JButton("hola");
              
        }
        return botones;
    }
    
    
    public void friendsList(){
            byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try 
            {
                os.print("friends");
                while(!command.contains("</amigos>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                }
                System.out.println(command); 
                doc = convertStringToXMLDocument(command);
                NodeList amigos=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < amigos.getLength(); i++) {
                    Amigo amigo = new Amigo();
                    NodeList amigoNode = amigos.item(i).getChildNodes();
                    amigo.id=Integer.parseInt(amigoNode.item(0).getTextContent());
                    amigo.alias=amigoNode.item(1).getTextContent();
                    NodeList mensajes = amigoNode.item(2).getChildNodes();
                    for (int e = 0; e < mensajes.getLength(); e++) {
                        Mensaje mensaje = new Mensaje();
                        NodeList mensajeNode= mensajes.item(e).getChildNodes();
                        mensaje.origen=Integer.parseInt(mensajeNode.item(0).getTextContent());
                        mensaje.texto=mensajeNode.item(1).getTextContent();
                        mensaje.tiempo=mensajeNode.item(2).getTextContent();
                        amigo.mensajes.add(mensaje);
                        System.out.println(mensaje.origen);
                        System.out.println(mensaje.texto);
                    }
                    friendsList.add(amigo);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void messages(){
            try 
            {
                os.print("friends");
                while(clientSocket.getInputStream().available()==0);
                   this.messages=new byte[clientSocket.getInputStream().available()];
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void groupsList(){
            try 
            {
                os.print("friends");
                while(clientSocket.getInputStream().available()==0);
                   this.groupsList=new byte[clientSocket.getInputStream().available()];
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public void requestList(){
            try 
            {
                os.print("friends");
                while(clientSocket.getInputStream().available()==0);
                   this.requestList=new byte[clientSocket.getInputStream().available()];
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
 }
    
    
