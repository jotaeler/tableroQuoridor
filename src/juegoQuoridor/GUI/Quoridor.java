/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.GUI;

import juegoQuoridor.agentes.AgenteTablero;

/**
 *
 * @author hacker
 */
public class Quoridor extends javax.swing.JFrame {
    
    AgenteTablero agenteTablero;
    
    /**
     * Creates new form Quoridor
     */
    public Quoridor(AgenteTablero _at) {
        initComponents();
        agenteTablero=_at;
        grupo_botones.add(boton_2_jugadores);
        grupo_botones.add(boton_4_jugadores);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo_botones = new javax.swing.ButtonGroup();
        btnStart = new javax.swing.JButton();
        boton_2_jugadores = new javax.swing.JRadioButton();
        boton_4_jugadores = new javax.swing.JRadioButton();
        btnRanking = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnStart.setText("Jugar");
        btnStart.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnStartMousePressed(evt);
            }
        });

        boton_2_jugadores.setText("2 jugadores");

        boton_4_jugadores.setText("4 jugadores");
        boton_4_jugadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_4_jugadoresActionPerformed(evt);
            }
        });

        btnRanking.setText("Ranking");
        btnRanking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnRankingMousePressed(evt);
            }
        });
        btnRanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRankingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(boton_2_jugadores)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(boton_4_jugadores)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boton_4_jugadores)
                    .addComponent(boton_2_jugadores))
                .addGap(18, 18, 18)
                .addComponent(btnStart, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnStartMousePressed
        int jugadores=2;
        if(boton_4_jugadores.isSelected()){
            jugadores=4;
        }
        agenteTablero.empezarPartida(jugadores);
    }//GEN-LAST:event_btnStartMousePressed

    private void boton_4_jugadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_4_jugadoresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_boton_4_jugadoresActionPerformed

    private void btnRankingMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRankingMousePressed
        Ranking vistaRanking=new Ranking();
        vistaRanking.setVisible(true);
    }//GEN-LAST:event_btnRankingMousePressed

    private void btnRankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRankingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRankingActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton boton_2_jugadores;
    private javax.swing.JRadioButton boton_4_jugadores;
    private javax.swing.JButton btnRanking;
    private javax.swing.JButton btnStart;
    private javax.swing.ButtonGroup grupo_botones;
    // End of variables declaration//GEN-END:variables
}
