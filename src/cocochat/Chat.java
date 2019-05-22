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
import javax.swing.ImageIcon;
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
import java.sql.Timestamp;
import java.util.Date;
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
public class Chat extends JFrame implements Runnable{


   int nFriends=2, nGroups=2, nOnline=2, nOffline=2;
   int destination;

   ArrayList<Amigo> friendsList = new ArrayList<>();
   ArrayList<Grupo> groupsList = new ArrayList<>();
   ArrayList<Online> onlineList = new ArrayList<>();
   ArrayList<Offline> offlineList = new ArrayList<>();
   ArrayList<Solicitudes_Grupos> groupsRequests = new ArrayList<>();
   ArrayList<Solicitudes_Amigos> friendsRequests = new ArrayList<>();

   JLabel header=new JLabel();
   JTextField description=new JTextField();
   JTextField friends=new JTextField();
   JTextField groups=new JTextField();
   JTextField online=new JTextField();
   JTextField offline=new JTextField();
   JTextPane chatPanel=new JTextPane();
   JTextField message=new JTextField();
   JTextField groupsReq = new JTextField();
   JTextField friendsReq = new JTextField();
   int last = 0,friend = 0, group = 0, nofriend = 0, nofriendoff = 0;

   JButton send=new JButton("Enviar");
   JButton botonPanel = new JButton("");
   JButton nuevoGrupo = new JButton("Crear Grupo");
   JButton eliminarGrupo = new JButton("Eliminar Grupo");
    JButton eliminarPersona = new JButton("Eliminar Persona");
   
   Socket clientSocket = null;
   PrintStream os = null;

   JScrollPane scrollChat = new JScrollPane();
   JScrollPane scrollSide = new JScrollPane();

   JList list = new JList();
   
   JButton [] friendsButtons;
   JButton [] groupsButtons;
   JButton [] onlineButtons;
   JButton [] offlineButtons;
   JButton [] groupsRequestButtons;
   JButton [] friendsRequestsButtons;

   int y=0;


/**
 * Inicializa el chat  y carga la vista
 * @param clientSocket
 * @param os 
 */
    Chat(Socket clientSocket,PrintStream os) {
        this.clientSocket = clientSocket;
        this.os = os;
        friendsList();
        groupsList();
        groupsRequests();
        friendsRequests();
        onlineList();
        offlineList();
        friendsButtons=initButtonsFriends(this.friendsList);
        groupsButtons=initButtonsGroups(this.groupsList);
        onlineButtons=initButtonsOnline(this.onlineList);
        offlineButtons=initButtonsOffline(this.offlineList);
        groupsRequestButtons=initButtonsGroupsReq(this.groupsRequests);
        friendsRequestsButtons=initButtonsFriendsReq(this.friendsRequests);
        this.setPreferredSize(new Dimension(900,900));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.pink);
        header.setText("CocoChat");
        description.setText("chat");
        description.setEditable(false);
        friendsReq.setText("Solicitudes de Amigos");
        friendsReq.setEditable(false);
        groupsReq.setText("Solicitudes de Grupos");
        groupsReq.setEditable(false);
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
        initSide(friendsReq, friendsRequestsButtons);
        initSide(groupsReq, groupsRequestButtons);
        initSide(friends,friendsButtons);
        initSide(groups,groupsButtons);
        initSide(online,onlineButtons);
        initSide(offline,offlineButtons);
        y+=50;
        list.setPreferredSize(new Dimension(300,1000));
        scrollSide.setViewportView(list);
        scrollSide.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollChat.setViewportView(chatPanel);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        send.addActionListener((ActionEvent e) ->
            {
                sendNudes();
            });
        botonPanel.addActionListener((ActionEvent e) ->
            {
                varButton();
            });
        nuevoGrupo.addActionListener((ActionEvent e) ->
        {
            newGrupo();
        }
        );
        eliminarGrupo.addActionListener((ActionEvent e) ->
        {
            deleteGrupo();
        }
        );
        eliminarPersona.addActionListener((ActionEvent e) ->
        {
            deletePersona();
        }
        );
        GroupLayout view= new GroupLayout(this.getContentPane());

        view.setHorizontalGroup(
                view.createParallelGroup()
                    .addComponent(header,700,700,700)
                    .addGroup(view.createSequentialGroup()
                            .addComponent(scrollSide,200,200,200)
                            .addGroup(view.createParallelGroup()
                                    .addGroup(view.createSequentialGroup()
                                            .addComponent(description,300,300,300)
                                            .addComponent(botonPanel,100,100,100)
                                            .addComponent(nuevoGrupo, 150, 150, 150)
                                            .addComponent(eliminarGrupo, 150, 150, 150)
                                            .addComponent(eliminarPersona, 150, 150, 150)
                                    )
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
                                    .addGroup(view.createParallelGroup()
                                            .addComponent(description,50,50,50)
                                            .addComponent(botonPanel,50,50,50)
                                            .addComponent(nuevoGrupo, 50, 50, 50)
                                            .addComponent(eliminarGrupo, 50, 50, 50)
                                            .addComponent(eliminarPersona, 50, 50, 50)
                                    )
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
    /**
     *Manda el mensae escrito por el usuario al server 
     */

    public void sendNudes()
    {
        if(last == 1)
        {
            os.print("mensaje<s>amigo<s>"+friendsList.get(friend).id+"<s>"+message.getText());
            Mensaje mensaje = new Mensaje();
            mensaje.origen = "0";
            mensaje.texto = message.getText();
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            mensaje.tiempo = ts.toString();
            if(friendsList.get(friend).mensajes == null)
            {
                friendsList.get(friend).mensajes = new ArrayList<Mensaje>();
            }
            friendsList.get(friend).mensajes.add(mensaje);
            showMensajesAmigo(friend);
            message.setText("");
        }
        else if(last == 2)
        {
            os.print("mensaje<s>grupo<s>"+groupsList.get(group).id+"<s>"+message.getText());
            Mensaje mensaje = new Mensaje();
            mensaje.origen = "0";
            mensaje.texto = message.getText();
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            mensaje.tiempo = ts.toString();
            if(groupsList.get(group).mensajes == null)
            {
                groupsList.get(group).mensajes = new ArrayList<Mensaje>();
            }
            groupsList.get(group).mensajes.add(mensaje);
            showMensajesGrupo(group);
            message.setText("");
        }
        else if (last == 3)
        {
            os.print("mensaje<s>noamigo<s>"+onlineList.get(nofriend).id+"<s>"+message.getText());
            Mensaje mensaje = new Mensaje();
            mensaje.origen = "0";
            mensaje.texto = message.getText();
            Date date= new Date();
            long time = date.getTime();
            Timestamp ts = new Timestamp(time);
            mensaje.tiempo = ts.toString();
            if(onlineList.get(nofriend).mensajes == null)
            {
               onlineList.get(nofriend).mensajes = new ArrayList<Mensaje>(); 
            }
            onlineList.get(nofriend).mensajes.add(mensaje);
            showMessagesOnline(nofriend);
            message.setText("");
        }
    }
    
    /**
     * Manda al server el comando para crear un nuevo grupo con el nombre de este 
     */
    
    
    public void newGrupo(){
        String input = JOptionPane.showInputDialog(null,"Nombre del grupo");
        os.print("nuevo<s>"+input);
    }
    /**
     * Manda el comando para eliminar un rupo específico 
     */
    public void deleteGrupo()
    {
        os.print("eliminar<s>grupo<s>"+groupsList.get(group).id);
        for (int i = 0; i < groupsList.size(); i++)
                                    {
                                        if(groupsList.get(i).id == groupsList.get(group).id)
                                        {
                                            groupsList.remove(i);
                                        }          
                                    }
                                    groupsButtons = initButtonsGroups(groupsList);
                                    restartButtons();
    }
    
    /**
     * Manda el comando para borrar a una persona especifica de un grupo 
     */
    public void deletePersona()
    {
        String input = JOptionPane.showInputDialog(null,"Nombre de la persona a eliminar");
        os.print("eliminar<s>persona<s>"+input+"<s>"+groupsList.get(group).id);
    }
    
    /**
     *Ejecuta la opcion correspondiente segun la vista que se este cargando, cambia de alias si es un amigo,
     * invita  a un amigo si esta en un grupo 
     * y le manda solicitud a un usuario ya sea que este offline u online
     */
    public void varButton()
    {
        if(last==1){
                String input = JOptionPane.showInputDialog(null,"Cambiar alias");
                friendsList.get(friend).alias = input;
                friendsButtons = initButtonsFriends(friendsList);
                restartButtons();
                os.print("alias<s>"+friendsList.get(friend).id+"<s>"+input);
            }else if(last==2){
                String input = JOptionPane.showInputDialog(null,"Invitar amigo");
                os.print("solicitud<s>grupo<s>"+groupsList.get(group).id+"<s>"+input);
            }else if(last==3){
                os.print("solicitud<s>amigo<s>"+onlineList.get(nofriend).id);
            }else if (last == 4){
                os.print("solicitud<s>amigo<s>"+offlineList.get(nofriendoff).id);
            }
    }
    /**
     * Inicializa la barra lateral
     * @param label etiqueta a agregar 
     * @param buttons conjunto de botones a agregar 
     */
    
    
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
    /**
     * Inicializa los botones de la lista de amigos
     * @param friendsList lista de amigos
     * @return conjunto de botones inicializados
     */

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

/**
 * Muestra la lista de mensajes de un amigo
 * @param id id del amigo
 */
    public void showMensajesAmigo(int id)
    {
        chatPanel.setVisible(true);
        send.setVisible(true);
        botonPanel.setText("Alias");
        chatPanel.setText("");
        Amigo amigo = friendsList.get(id);
        if(amigo.mensajes != null)
        {
            for(int i = 0; i < amigo.mensajes.size(); i++)
            {
                if(amigo.mensajes.get(i).origen.equals("0"))
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
        last = 1;
        friend = id;
    }
/**
 * Inicializa la lista de botones de grupos
 * @param groupsList lista de grupos
 * @return lista de botones ya inicializados
 */
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
    /**
     * Inicializa la lista de botones de usuarios online
     * @param onlineList lista de usuariois online
     * @return lista de botones ya inicializadoss
     */
    public JButton[] initButtonsOnline(ArrayList<Online> onlineList){
        JButton [] botones = new JButton[onlineList.size()];
        for (int i = 0; i < onlineList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( onlineList.get(i).name);
            botones[i].addActionListener((ActionEvent e) ->
            {
                showMessagesOnline(id);
            });

        }
        return botones;
    }
    /**
     * Muestra la lista de mensajes con la persona online
     * @param id id del usuario con el que se esta platicando
     */
    public void showMessagesOnline(int id)
    {
        chatPanel.setVisible(true);
        send.setVisible(true);
        botonPanel.setText("Amigo");
        chatPanel.setText("");
        Online online = onlineList.get(id);
        last = 3;
        nofriend = id;
        if(online.mensajes != null)
        {
            for(int i = 0; i < online.mensajes.size(); i++)
            {
                if(online.mensajes.get(i).origen.equals("0"))
                {
                    chatPanel.setText(chatPanel.getText()+"\nYo:\n");
                }
                else
                {
                    chatPanel.setText(chatPanel.getText()+"\n"+online.name+":\n");
                }
                chatPanel.setText(chatPanel.getText()+online.mensajes.get(i).texto+"\n");
                chatPanel.setText(chatPanel.getText()+online.mensajes.get(i).tiempo+"\n");
            }
        }
    }
    /**
     * Inicializa la lista de botones de usuarios offline
     * @param offlineList lista de usuariois offline
     * @return lista de botones ya inicializadoss
     */
    public JButton[] initButtonsOffline(ArrayList<Offline> offlineList){
        JButton [] botones = new JButton[offlineList.size()];
        for (int i = 0; i < offlineList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( offlineList.get(i).name);
            botones[i].addActionListener((ActionEvent e) ->
            {
                showOffline(id);
            });

        }
        return botones;
    }
    /**
     *Cambia los aspectos del chat si se abre un hat de una persona offline
     * @param id id de la persona offline
     */
    public void showOffline(int id)
    {
        botonPanel.setText("Amigo");
        chatPanel.setVisible(false);
        chatPanel.setText("");
        send.setVisible(false);
        last = 4;
        nofriendoff = id;
    }
    
    /**
     * Inicializa la lista de botones solicitudes de grupo
     * @param groupsReqList lista de soicitdes de grupo
     * @return lista de botones ya inicializados
     */
    public JButton[] initButtonsGroupsReq(ArrayList<Solicitudes_Grupos> groupsReqList){
        JButton [] botones = new JButton[groupsReqList.size()];
        for (int i = 0; i < groupsReqList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( groupsReqList.get(i).nombre);
            botones[i].addActionListener((ActionEvent e) ->
            {
                groupReqOption(id);
            });

        }
        return botones;
    }
    
    /**
     * Abre un pop up en caso de que se presione una soicitud para unirse un grupo 
     * @param i id de la solicitud
     */
    public void groupReqOption(int i)
    {
        ImageIcon icon = new ImageIcon("src/cocochat/turtle.jpeg");
        String[] options = new String[] {"Aceptar", "Rechazar", "Cancelar"};
        int response = JOptionPane.showOptionDialog(null, "", "Solicitud de Grupo",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        icon, options, options[0]);
        if(response == 0)
        {
            os.print("aceptar<s>grupo<s>"+groupsRequests.get(i).id);
        }
        else if (response == 1)
        {
            os.print("rechazar<s>grupo<s>"+groupsRequests.get(i).id);
        }
    }
    
    /**
     * Inicializa la lista de botones de las solicitudes de amistad
     * @param friendsReqList lista de solicitudes
     * @return lista de botones ya inicializados
     */
    public JButton[] initButtonsFriendsReq(ArrayList<Solicitudes_Amigos> friendsReqList){
        JButton [] botones = new JButton[friendsReqList.size()];
        for (int i = 0; i < friendsReqList.size(); i++)
        {
            final int id = i;
            botones[i] = new JButton( friendsReqList.get(i).nombre);
            botones[i].addActionListener((ActionEvent e) ->
            {
                friendReqOption(id);
            });

        }
        return botones;
    }
    /**
     * Abre un pop up en caso de que se presione una soicitud para establecer una amistad 
     * @param i id de la solicitud
     */
    private void friendReqOption(int i)
    {
        ImageIcon icon = new ImageIcon("src/cocochat/turtle.jpeg");
        String[] options = new String[] {"Aceptar", "Rechazar", "Cancelar"};
        int response = JOptionPane.showOptionDialog(null, "", "Solicitud de Amistad",
        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
        icon, options, options[0]);
        if(response == 0)
        {
            os.print("aceptar<s>amigo<s>"+friendsRequests.get(i).id);
        }
        else if (response == 1)
        {
            os.print("rechazar<s>amigo<s>"+friendsRequests.get(i).id);
        }
    }
    /**
     * Muestra los mensajes de un grupo
     * @param id id del grupp
     */
    public void showMensajesGrupo(int id)
    {
        chatPanel.setVisible(true);
        send.setVisible(true);
        botonPanel.setText("Nuevo");
        chatPanel.setText("");
        Grupo grupo = groupsList.get(id);
        if (grupo.mensajes != null)
        {
            for(int i = 0; i < grupo.mensajes.size(); i++)
            {
                if(grupo.mensajes.get(i).origen.equals("0"))
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
        last = 2;
        group = id;
    }
/**
 * Recibe la lista de amigos del usuario
 */
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
                        mensaje.origen=(mensajeNode.item(0).getTextContent());
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
/**
 * Recibe la lista de grupos a la que el usuario pertenece
 */
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
                }
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
                        mensaje.origen=(mensajeNode.item(0).getTextContent());
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
    
    /**
     * Recibe la lista de solicitudes de grupos
     */
    public void groupsRequests(){
            byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try
            {              
                os.print("requests");
                while(!command.contains("</grupos>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                }
                doc = convertStringToXMLDocument(command);
                NodeList grupos=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < grupos.getLength(); i++) {
                    Solicitudes_Grupos request = new Solicitudes_Grupos();
                    NodeList grupoNode = grupos.item(i).getChildNodes();
                    request.id=Integer.parseInt(grupoNode.item(0).getTextContent());
                    request.nombre=grupoNode.item(1).getTextContent();
                    groupsRequests.add(request);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    /**
     * Recibe la lista de solicitudes de amistad
     */
    public void friendsRequests(){
            byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try
            {              
                os.print("requestsAmigo");
                while(!command.contains("</amigos>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                }
                doc = convertStringToXMLDocument(command);
                NodeList amigos=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < amigos.getLength(); i++) {
                    Solicitudes_Amigos request = new Solicitudes_Amigos();
                    NodeList amigoNode = amigos.item(i).getChildNodes();
                    request.id=Integer.parseInt(amigoNode.item(0).getTextContent());
                    request.nombre=amigoNode.item(1).getTextContent();
                    friendsRequests.add(request);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    /**
     * Recibe la lista de personas online
     */
    public void onlineList(){
        byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try
            {
                os.print("online");
                while(!command.contains("</online>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                }
                doc = convertStringToXMLDocument(command);
                NodeList onlinePeople=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < onlinePeople.getLength(); i++) {
                    Online online = new Online();
                    NodeList onlineNode = onlinePeople.item(i).getChildNodes();
                    online.id=Integer.parseInt(onlineNode.item(0).getTextContent());
                    online.name=onlineNode.item(1).getTextContent();
                    this.onlineList.add(online);
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
/**
 * Recibe la lista de personas offline
 */
    public void offlineList(){
        byte[] bytes;
            String command="";
            String[] splitted;
            Document doc = null;
            try
            {
                os.print("offline");
                while(!command.contains("</offline>")){
                while(clientSocket.getInputStream().available()==0);
                   bytes=new byte[clientSocket.getInputStream().available()];
                   clientSocket.getInputStream().read(bytes);
                   command+= new String(bytes);
                }
                doc = convertStringToXMLDocument(command);
                NodeList offlinePeople=doc.getFirstChild().getChildNodes();
                for (int i = 0; i < offlinePeople.getLength(); i++) {
                    Offline offline = new Offline();
                    NodeList offlineNode = offlinePeople.item(i).getChildNodes();
                    offline.id=Integer.parseInt(offlineNode.item(0).getTextContent());
                    offline.name=offlineNode.item(1).getTextContent();
                    this.offlineList.add(offline);
                }
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
    /**
     * Actualiza la barra lateral del chat
     */
    public void restartButtons()
    {
        y = 0;
        list.removeAll();
        initSide(friendsReq, friendsRequestsButtons);
        initSide(groupsReq, groupsRequestButtons);
        initSide(friends,friendsButtons);
        initSide(groups,groupsButtons);
        initSide(online,onlineButtons);
        initSide(offline,offlineButtons);
        list.setPreferredSize(new Dimension(300,1000));
        scrollSide.setViewportView(list);
        y += 50;
    }

    @Override
    /**
     * Ejecuta el hilo 
     */
    public void run() {
        while(true);
    }
 }
