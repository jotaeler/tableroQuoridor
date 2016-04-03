/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.GUI;

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
import juegoQuoridor.agentes.AgenteTablero;

/**
 *
 * @author Hacker
 */
public class GUItablero extends JFrame {
    private final AgenteTablero myAgent;
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

    public GUItablero(AgenteTablero _agente) {
        myAgent=_agente;
        
        grid = new GridBagLayout();
        setSize(800, 800);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addComponentsToPane(getContentPane());

        add(panel);
        setVisible(true);
    }
}