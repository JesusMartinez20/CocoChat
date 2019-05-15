/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Jesús Martínez
 */

public class Sign_in extends JFrame implements ActionListener{
    
   JTextField userName=new JTextField();
   JPasswordField  password1=new JPasswordField ();
   JPasswordField  password2=new JPasswordField ();
   JTextField Tu=new JTextField();
   JTextField Tp=new JTextField();
   JTextField Tpc=new JTextField();
   JButton signIn=new JButton();
   JOptionPane pop = new JOptionPane();
   Socket clientSocket;
   PrintStream os;
   
   int x=300;
   int y=105;
    
    public Sign_in(Socket clientSocket,PrintStream os)
    {
        this.clientSocket=clientSocket;
        this.os=os;
        Start();
    }
    
    public final void Start ()
    {

        this.setLayout(null);
        this.setSize(900, 900);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground(Color.pink);  
        this.add(Tu);
        this.add(userName);
        this.add(Tp);
        this.add(password1);
        this.add(Tpc);
        this.add(password2);
        this.add(signIn);
        
        Tu.setBounds(x,y,300,50);
        Tu.setText("Username");
        Tu.setEditable(false);
        
        userName.setBounds(x,y+60,300,50);
        
        Tp.setBounds(x,y+120,300,50);
        Tp.setText("Contraseña");
        Tp.setEditable(false);
        
        password1.setBounds(x,y+180,300,50);
        
        Tpc.setBounds(x,y+240,300,50);
        Tpc.setText("Confirmar contraseña");
        Tpc.setEditable(false);
        
        password2.setBounds(x,y+300,300,50);
        
        signIn.setBounds(x+100,y+360,100,50);
        signIn.setText("Sign in");
        signIn.addActionListener(this);
    }
    
   @Override
    public void actionPerformed(ActionEvent e) {
        if(!checkPassword(password1.getPassword(),password2.getPassword())){
            JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden");
        }
        else
        {
           try 
            {
                if(userName.getText().contains(" "))
                {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario no puede contener espacios");
                }
                else
                {
                    os.print("signIn "+this.userName.getText()+" "+new String(this.password1.getPassword()));
                    while(clientSocket.getInputStream().available()==0);
                    if(clientSocket.getInputStream().read()=='0')
                    {
                        JOptionPane.showMessageDialog(this, "Revisa tu usuario y contraseña");
                    }
                    else
                    {
                        new Chat(clientSocket, os).setVisible(true);
                        this.setVisible(false);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(Log_in.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    boolean checkPassword(char[] pass1,char[] pass2){
       return new String(pass1).equals(new String(pass2));
    }
}
