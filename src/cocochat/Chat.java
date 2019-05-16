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
import javax.swing.Icon;
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
import javax.swing.JTextPane;
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
public class Chat extends JFrame implements ActionListener{
   int nFriends=2, nGroups=2, nOnline=2, nOffline=2;
   int destination;

   ArrayList<Amigo> friendsList = new ArrayList<>();
   ArrayList<Grupo> groupsList = new ArrayList<>();
   ArrayList<Amigo> onlineList = new ArrayList<>();
   ArrayList<Amigo> offlineList = new ArrayList<>();

   JLabel header=new JLabel();
   JTextField description=new JTextField();
   JTextField friends=new JTextField();
   JTextField groups=new JTextField();
   JTextField online=new JTextField();
   JTextField offline=new JTextField();
   JTextPane chatPanel=new JTextPane();
   JTextField message=new JTextField();

   JButton send=new JButton("Enviar");
   JButton requests=new JButton("Solicitudes");


   Socket clientSocket = null;
   PrintStream os = null;

   JScrollPane scrollChat = new JScrollPane();
   JScrollPane scrollSide = new JScrollPane();

   JList list = new JList();

   int y=0;



    Chat(Socket clientSocket,PrintStream os) {
        this.clientSocket = clientSocket;
        this.os = os;
        friendsList();
        groupsList();
        JButton [] friendsButtons=initButtonsFriends(this.friendsList);
        JButton [] groupsButtons=initButtonsGroups(this.groupsList);
        //JButton [] onlineButtons=initButtonsOnline(this.onlineList);
        //JButton [] offlineButtons=initButtonsOffline(this.offlineList);
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
        chatPanel.setEditable(false);
        scrollChat.setViewportView(chatPanel);
        scrollSide.setViewportView(list);
        initSide(friends,friendsButtons);
        initSide(groups,groupsButtons);
        //initSide(online,onlineButtons);
        //initSide(offline,offlineButtons);
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

    public JButton[] initButtonsFriends(ArrayList<Amigo> friendsList){
        JButton [] botones = new JButton[friendsList.size()];
        for (int i = 0; i < friendsList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( friendsList.get(i).alias);
            botones[i].addActionListener((ActionEvent e) ->
            {
                showMensajesAmigo(id);
            });

        }
        return botones;
    }
    public JButton[] initButtons0(ArrayList<Grupo> friendsList){
        JButton [] botones = new JButton[friendsList.size()];
        for (int i = 0; i < friendsList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( friendsList.get(i).nombre);
            botones[i].addActionListener((ActionEvent e) ->
            {
                showMensajesAmigo(id);
            });

        }
        return botones;
    }

    public void showMensajesAmigo(int id)
    {
        Amigo amigo = friendsList.get(id);
        for(int i = 0; i < amigo.mensajes.size(); i++)
        {
            if(amigo.mensajes.get(i).origen == 0)
            {
                chatPanel.setText(chatPanel.getText()+"\nYo:\n");
            }
            else
            {
                chatPanel.setText(chatPanel.getText()+"\n"+amigo.alias+":\n");
            }
            chatPanel.setText(chatPanel.getText()+amigo.mensajes.get(i).texto+"\n");
            chatPanel.setText(chatPanel.getText()+amigo.mensajes.get(i).tiempo+"\n");
        }
    }

    public JButton[] initButtonsGroups(ArrayList<Grupo> groupsList){
        JButton [] botones = new JButton[groupsList.size()];
        for (int i = 0; i < groupsList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( groupsList.get(i).nombre);
            botones[i].addActionListener((ActionEvent e) ->
            {
                showMensajesGrupo(id);
            });

        }
        return botones;
    }
    
    public void showMensajesGrupo(int id)
    {
        Grupo grupo = groupsList.get(id);
        for(int i = 0; i < grupo.mensajes.size(); i++)
        {
            if(grupo.mensajes.get(i).origen == 0)
            {
                chatPanel.setText(chatPanel.getText()+"\nYo:\n");
            }
            else
            {
                chatPanel.setText(chatPanel.getText()+"\n"+grupo.mensajes.get(i).origen+":\n");
            }
            chatPanel.setText(chatPanel.getText()+grupo.mensajes.get(i).texto+"\n");
            chatPanel.setText(chatPanel.getText()+grupo.mensajes.get(i).tiempo+"\n");
        }
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
                    }
                    friendsList.add(amigo);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void groupsList(){
            byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try
            {
                os.print("groups");
                while(!command.contains("</grupos>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                    System.out.println(command);
                }
                System.out.println(command);
                doc = convertStringToXMLDocument(command);
                NodeList grupos=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < grupos.getLength(); i++) {
                    Grupo grupo = new Grupo();
                    NodeList grupoNode = grupos.item(i).getChildNodes();
                    grupo.id=Integer.parseInt(grupoNode.item(0).getTextContent());
                    grupo.nombre=grupoNode.item(1).getTextContent();
                    NodeList mensajes = grupoNode.item(2).getChildNodes();
                    for (int e = 0; e < mensajes.getLength(); e++) {
                        Mensaje mensaje = new Mensaje();
                        NodeList mensajeNode= mensajes.item(e).getChildNodes();
                        mensaje.origen=Integer.parseInt(mensajeNode.item(0).getTextContent());
                        mensaje.texto=mensajeNode.item(1).getTextContent();
                        mensaje.tiempo=mensajeNode.item(2).getTextContent();
                        grupo.mensajes.add(mensaje);
                    }
                    groupsList.add(grupo);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public void requestList(){
            try
            {
                os.print("friends");
                while(clientSocket.getInputStream().available()==0);
                   //this.requestList=new byte[clientSocket.getInputStream().available()];
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==send){
           sendMessage();
        }
    }

    public void sendMessage(){
        try
            {
                os.print("message"+"<s>"+this.message.getText()+"<s>"+this.destination);
                while(clientSocket.getInputStream().available()==0);
                   //this.requestList=new byte[clientSocket.getInputStream().available()];
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
