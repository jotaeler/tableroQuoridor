/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.GUI;

import jade.content.ContentManager;
import java.awt.ComponentOrientation;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import juegoQuoridor.elementos.Movimiento;
import juegoQuoridor.elementos.MovimientoRealizado;
import juegoQuoridor.elementos.Muro;
import juegoQuoridor.utils.Casilla;
import juegosTablero.elementos.Ficha;
import juegosTablero.elementos.Jugador;
import juegosTablero.elementos.Posicion;

/**
 *
 * @author hacker
 */
public class GUI extends javax.swing.JFrame {
    //private final AgenteTablero myAgent;

    JLabel labels[][] = new JLabel[17][17];
    GridBagLayout grid;
    ContentManager manager;

    /**
     * Creates new form GUI
     */
    public GUI(ContentManager _manager) {
        grid = new GridBagLayout();
        initComponents();
        addComponentsToPane();
        add(panelTablero);
        manager = _manager;
    }

    public void addComponentsToPane() {

        panelTablero.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelTablero.setLayout(grid);

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
                        label = new JLabel(separacionV);
                        label.setText("");
                    } else {
                        label = new JLabel();
                        label.setText("");
                    }
                    labels[i][j] = label;
                    //labels[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = i;
                    c.gridy = j;
                    panelTablero.add(labels[i][j], c);
                }
            } else {
                for (int j = 0; j < 17; j++) {
                    JLabel label;
                    if (j % 2 == 0) {
                        label = new JLabel(casilla);
                    } else {
                        label = new JLabel(separacionH);
                    }
                    labels[i][j] = label;
                    //labels[i][j].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.gridx = i;
                    c.gridy = j;
                    panelTablero.add(labels[i][j], c);
                }
            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelTablero = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 700));
        setMinimumSize(new java.awt.Dimension(700, 700));
        setPreferredSize(new java.awt.Dimension(800, 700));

        panelTablero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        panelTablero.setMaximumSize(new java.awt.Dimension(700, 700));
        panelTablero.setMinimumSize(new java.awt.Dimension(700, 700));
        panelTablero.setName(""); // NOI18N
        panelTablero.setPreferredSize(new java.awt.Dimension(700, 700));

        javax.swing.GroupLayout panelTableroLayout = new javax.swing.GroupLayout(panelTablero);
        panelTablero.setLayout(panelTableroLayout);
        panelTableroLayout.setHorizontalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelTableroLayout.setVerticalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 698, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTablero, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTablero, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new GUI().setVisible(true);
//            }
//        });
//    }
    public void representarMovimiento(MovimientoRealizado _m, Casilla _posAnterior) {
        try {
            Movimiento mov = _m.getMovimiento();
            Jugador jugador = _m.getJugador();
            Ficha ficha = jugador.getFicha();
            Posicion pos = mov.getPosicion();
            Object elementoJuego = mov.getElementoJuego();

            if (elementoJuego instanceof Muro) {
                if (elementoJuego != null) {
                    if (((Muro) (elementoJuego)).getAlineacion() == juegoQuoridor.OntologiaQuoridor.ALINEACION_HORIZONTAL) {
                        String path = "muro-horizontal.png";
                        URL url = this.getClass().getResource(path);
                        ImageIcon muro = new ImageIcon(url);
                        labels[pos.getCoorX() * 2][(pos.getCoorY() * 2) + 1].setIcon(muro);
                        labels[(pos.getCoorX() * 2) + 2][(pos.getCoorY() * 2) + 1].setIcon(muro);
                    } else {
                        //Vertical
                        String path = "muro-vertical.png";
                        URL url = this.getClass().getResource(path);
                        ImageIcon muro = new ImageIcon(url);
                        labels[(pos.getCoorX() * 2) + 1][(pos.getCoorY() * 2)].setIcon(muro);
                        labels[(pos.getCoorX() * 2) + 1][(pos.getCoorY() * 2) + 2].setIcon(muro);
                    }
                }
            } else {//Es un movimiento normal
                URL url = null;
                String path = null;
                if (ficha.getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1) {
                    path = "ficha-rojo.png";

                } else if (ficha.getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2) {
                    path = "ficha-azul.png";

                } else if (ficha.getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3) {
                    path = "ficha-verde.png";

                } else if (ficha.getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4) {
                    path = "ficha-negro.png";
                    
                }
                url = this.getClass().getResource(path);
                ImageIcon imgFicha = new ImageIcon(url);
                labels[pos.getCoorX() * 2][pos.getCoorY() * 2].setIcon(imgFicha);
                
                //Cambio la imagen de la posicion anterior que tenia la ficha por una vacia
                path = "tablero.png";
                url = this.getClass().getResource(path);
                ImageIcon casilla = new ImageIcon(url);
                labels[_posAnterior.getX() * 2][_posAnterior.getY()* 2].setIcon(casilla);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelTablero;
    // End of variables declaration//GEN-END:variables
}
