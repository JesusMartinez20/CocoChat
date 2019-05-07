package cocochat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jessica
 */
public class Log_in extends JFrame implements ActionListener{

    JLabel userLabel = new JLabel("Usuario");
    JTextField userName = new JTextField(20);
    JLabel passwordLabel = new JLabel("Contraseña");
    JPasswordField passwordText = new JPasswordField(20);
    JButton loginButton = new JButton("Iniciar sesión");
    JButton registerButton = new JButton("Registrarme");

    public Log_in() {
        
        this.setVisible(true); 
        this.setSize(900, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.getContentPane().setBackground(Color.pink); 
        
        Elementos();
    
    }
    
   
    void Elementos(){
        
        userLabel.setBounds(300, 105, 80, 35);
        this.add(userLabel);

        userName.setBounds(300, 135, 310, 35);
        this.add(userName);

        passwordLabel.setBounds(300, 175, 80, 35);
        this.add(passwordLabel);

        passwordText.setBounds(300, 205, 310, 35);
        this.add(passwordText);

        loginButton.setBounds(300, 260, 110, 35);
        this.add(loginButton);
        loginButton.addActionListener(this);

        registerButton.setBounds(500, 260, 110, 35);
        this.add(registerButton);
        registerButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if(!checkUser(userName.getText())){
            JOptionPane.showMessageDialog(null, "El usuario no existe");
        }
        else if(!checkPassword(userName.getText(),passwordText.getText())){
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
        }
        else
        {
            //Insert en la bd
        }
    }
    
    //Se checará en la BD si el usuario ya existe en la base de datos
    boolean checkUser(String user){
        return true;
    }
    
    
    //Se verifica que la contraseña pertenezca al usuario ingresado
    boolean checkPassword(String user,String pass){
       return true;
    }
    

}
