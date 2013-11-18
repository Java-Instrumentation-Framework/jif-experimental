/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.miglayout;

/**
 *
 * @author GBH
 */
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
public class SampleLogin extends JFrame{
 
 public SampleLogin(){
  super("MigLayout Basic");
  setLayout(new MigLayout("", "[grow]", "[grow]"));
  add(new JLabel("Username:"), "right");
  add(new JTextField(), "growx, left, wrap, w 100");
  add(new JLabel("Password:"), "right");
  add(new JPasswordField(), "growx, left, wrap, w 100");
  add(new JCheckBox("Remember Me"), "center, wrap, span");
  add(new JButton("Login"), "split 2, span 2, center");
  add(new JButton("Close"));
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  pack();
  setLocationRelativeTo(null);
  setVisible(true);
 }
 public static void main(String args[]){
  new SampleLogin();
 }
}