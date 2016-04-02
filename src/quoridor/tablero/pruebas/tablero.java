/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quoridor.tablero.pruebas;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

import javax.swing.JLabel;

/**
 *
 * @author Hacker
 */
public class tablero extends JFrame {

    JPanel panel = new JPanel();
    JLabel labels[][] = new JLabel[17][17];
    GridBagLayout grid;

    public void addComponentsToPane(Container pane) {

        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.setLayout(grid);

        GridBagConstraints c = new GridBagConstraints();
        String path = "tablero.png";
        URL url = this.getClass().getResource(path);
        ImageIcon casilla = new ImageIcon(url);
        
        path = "separacion-vertical.png";
        url = this.getClass().getResource(path);
        ImageIcon separacionV = new ImageIcon(url);
        
        path = "separacion-horizontal.png";
        url = this.getClass().getResource(path);
        ImageIcon separacionH = new ImageIcon(url);
        
        
        for (int i = 0; i < 17; i++) {
            if (i % 2 != 0) {
                for (int j = 0; j < 17; j++) {
                    JLabel label;
                    if (j % 2 == 0) {
                        label=new JLabel(separacionV);
                        label.setText("");
                    } else {
                        label=new JLabel();
                        label.setText("");
                    }
                    labels[i][j] = label;
                    //labels[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = i;
                    c.gridy = j;
                    panel.add(labels[i][j], c);
                }
            } else {
                for (int j = 0; j < 17; j++) {
                    JLabel label;
                    if (j % 2 == 0) {
                        label=new JLabel(casilla);
                    } else {
                        label=new JLabel(separacionH);
                    }
                    labels[i][j] = label;
                    //labels[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = i;
                    c.gridy = j;
                    panel.add(labels[i][j], c);
                }
            }

        }

    }

    public static void main(String args[]) {
        new tablero();
    }

    public tablero() {
        grid = new GridBagLayout();
        setSize(800, 800);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addComponentsToPane(getContentPane());

        add(panel);
        setVisible(true);
    }
}


//public class tablero extends JFrame {
//
//    JPanel panel = new JPanel();
//    JLabel casillas[][] = new JLabel[17][17];
//    JLabel murosHorizontales[][]= new JLabel[17][17]; // 8 filas y 9 columnas
//    JLabel murosVerticales[][]= new JLabel[17][17]; // 8 filas y 9 columnas
//    GridBagLayout grid;
//
//    public void addComponentsToPane(Container pane) {
//
//        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//        panel.setLayout(grid);
//
//        GridBagConstraints c = new GridBagConstraints();
//        String path = "tablero.png";
//        URL url = this.getClass().getResource(path);
//        ImageIcon casilla = new ImageIcon(url);
//        
//        path = "separacion-vertical.png";
//        url = this.getClass().getResource(path);
//        ImageIcon separacionV = new ImageIcon(url);
//        
//        path = "separacion-horizontal.png";
//        url = this.getClass().getResource(path);
//        ImageIcon separacionH = new ImageIcon(url);
//        
//        
//        for (int i = 0; i < 17; i++) {
//            if (i % 2 != 0) {
//                for (int j = 0; j < 9; j++) {
//                    JLabel label;
//                    if (j % 2 == 0) {
//                        label=new JLabel(separacionV);
//                        label.setText("");
//                        murosVerticales[i][j] = label;
//                    } else {
//                        //cuadradito pequeño
//                        label=new JLabel();
//                        label.setText("");
//                    }
//                    c.fill = GridBagConstraints.HORIZONTAL;
//                    c.gridx = i;
//                    c.gridy = j;
//                    panel.add(label, c);
//                }
//            } else {
//                for (int j = 0; j < 17; j++) {
//                    JLabel label;
//                    if (j % 2 == 0) {
//                        label=new JLabel(casilla);
//                        casillas[i][j] = label;
//                    } else {
//                        label=new JLabel(separacionH);
//                        murosHorizontales[i][j] = label;
//                    }
//                    
//                    //labels[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
//                    c.fill = GridBagConstraints.HORIZONTAL;
//                    c.gridx = i;
//                    c.gridy = j;
//                    panel.add(label, c);
//                }
//            }
//
//        }
//
//    }
//
//    public static void main(String args[]) {
//        new tablero();
//    }
//
//    public tablero() {
//        grid = new GridBagLayout();
//        setSize(800, 800);
//        setResizable(false);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//
//        addComponentsToPane(getContentPane());
//
//        add(panel);
//        setVisible(true);
//    }
//}