/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cocochat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
   
   int x=300;
   int y=105;
    
    Sign_in(){
        this.setSize(900, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.pink);  
        
        Tu.setBounds(x,y,300,50);
        Tu.setText("Username");
        Tu.setEditable(false);
        this.add(Tu);
        
        userName.setBounds(x,y+60,300,50);
        this.add(userName);
        
        Tp.setBounds(x,y+120,300,50);
        Tp.setText("Contraseña");
        Tp.setEditable(false);
        this.add(Tp);
        
        password1.setBounds(x,y+180,300,50);
        this.add(password1);
        
        Tpc.setBounds(x,y+240,300,50);
        Tpc.setText("Confirmar contraseña");
        Tpc.setEditable(false);
        this.add(Tpc);
        
        password2.setBounds(x,y+300,300,50);
        this.add(password2);
        
        signIn.setBounds(x+100,y+360,100,50);
        signIn.setText("Sign in");
        signIn.addActionListener(this);
        this.add(signIn);
    }
    
    public void actionPerformed(ActionEvent e) {
       
        if(!checkUser(userName.getText())){
            pop.showMessageDialog(null, "El usuario ya existe");
        }
        
        if(!checkPassword(password1.getText(),password2.getText())){
            pop.showMessageDialog(null, "Las contraseñas no coinciden");
        }
    }
    
    boolean checkPassword(String pass1,String pass2){
        if(pass1.equals(pass2)){
            return true;
        }else{
            return false;
        }
    }
    
    //Se checará en la BD si el usuario ya existe
    boolean checkUser(String user){
        return true;
    }
    
}
