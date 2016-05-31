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
import java.util.ArrayList;
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
 *  Esta clase es la encargada de mostrar la interfaz del tablero
 * @author jalr0005
 */
public class GUI extends javax.swing.JFrame {
    //private final AgenteTablero myAgent;

    JLabel labels[][] = new JLabel[17][17];
    GridBagLayout grid;
    ContentManager manager;

    /**
     * Crea una nueva ventana de tablero
     * 
     */
    public GUI() {
        grid = new GridBagLayout();
        initComponents();
        addComponentsToPane();
        add(panelTablero);
        
        panelJugador1.setEnabled(false);
        panelJugador2.setEnabled(false);
        panelJugador3.setEnabled(false);
        panelJugador4.setEnabled(false);
    }
    
    /**
     * Añade a la ventana las imagenes que forman el tablero
     */
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
        infoJugadores = new javax.swing.JPanel();
        panelJugador1 = new javax.swing.JPanel();
        imgJugador1 = new javax.swing.JLabel();
        nombreJugador1 = new javax.swing.JLabel();
        murosJugador1 = new javax.swing.JLabel();
        panelJugador2 = new javax.swing.JPanel();
        nombreJugador2 = new javax.swing.JLabel();
        murosJugador2 = new javax.swing.JLabel();
        imgJugador2 = new javax.swing.JLabel();
        panelJugador3 = new javax.swing.JPanel();
        nombreJugador3 = new javax.swing.JLabel();
        murosJugador3 = new javax.swing.JLabel();
        imgJugador3 = new javax.swing.JLabel();
        panelJugador4 = new javax.swing.JPanel();
        nombreJugador4 = new javax.swing.JLabel();
        murosJugador4 = new javax.swing.JLabel();
        imgJugador4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(730, 700));
        setMinimumSize(new java.awt.Dimension(730, 700));
        setPreferredSize(new java.awt.Dimension(730, 700));
        setResizable(false);

        panelTablero.setMaximumSize(new java.awt.Dimension(500, 500));
        panelTablero.setMinimumSize(new java.awt.Dimension(500, 500));
        panelTablero.setName(""); // NOI18N
        panelTablero.setPreferredSize(new java.awt.Dimension(500, 500));

        javax.swing.GroupLayout panelTableroLayout = new javax.swing.GroupLayout(panelTablero);
        panelTablero.setLayout(panelTableroLayout);
        panelTableroLayout.setHorizontalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        panelTableroLayout.setVerticalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        infoJugadores.setMaximumSize(new java.awt.Dimension(450, 135));
        infoJugadores.setMinimumSize(new java.awt.Dimension(450, 135));

        panelJugador1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelJugador1.setMaximumSize(new java.awt.Dimension(170, 121));
        panelJugador1.setMinimumSize(new java.awt.Dimension(170, 121));
        panelJugador1.setPreferredSize(new java.awt.Dimension(170, 121));

        imgJugador1.setMaximumSize(new java.awt.Dimension(40, 40));
        imgJugador1.setMinimumSize(new java.awt.Dimension(40, 40));
        imgJugador1.setPreferredSize(new java.awt.Dimension(40, 40));

        nombreJugador1.setText("jLabel1");

        murosJugador1.setText("Muros: 10");

        javax.swing.GroupLayout panelJugador1Layout = new javax.swing.GroupLayout(panelJugador1);
        panelJugador1.setLayout(panelJugador1Layout);
        panelJugador1Layout.setHorizontalGroup(
            panelJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador1Layout.createSequentialGroup()
                .addGroup(panelJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJugador1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(murosJugador1))
                    .addGroup(panelJugador1Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(imgJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelJugador1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelJugador1Layout.setVerticalGroup(
            panelJugador1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imgJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreJugador1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(murosJugador1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelJugador2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelJugador2.setMaximumSize(new java.awt.Dimension(170, 121));
        panelJugador2.setMinimumSize(new java.awt.Dimension(170, 121));
        panelJugador2.setPreferredSize(new java.awt.Dimension(170, 121));

        nombreJugador2.setText("jLabel1");

        murosJugador2.setText("Muros: 10");

        imgJugador2.setMaximumSize(new java.awt.Dimension(40, 40));
        imgJugador2.setMinimumSize(new java.awt.Dimension(40, 40));
        imgJugador2.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout panelJugador2Layout = new javax.swing.GroupLayout(panelJugador2);
        panelJugador2.setLayout(panelJugador2Layout);
        panelJugador2Layout.setHorizontalGroup(
            panelJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador2Layout.createSequentialGroup()
                .addGroup(panelJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJugador2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(murosJugador2))
                    .addGroup(panelJugador2Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(imgJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelJugador2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panelJugador2Layout.setVerticalGroup(
            panelJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(nombreJugador2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(murosJugador2)
                .addGap(23, 23, 23))
        );

        panelJugador3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelJugador3.setMaximumSize(new java.awt.Dimension(170, 121));
        panelJugador3.setMinimumSize(new java.awt.Dimension(170, 121));
        panelJugador3.setPreferredSize(new java.awt.Dimension(170, 121));

        nombreJugador3.setText("Inactivo");

        murosJugador3.setText("Muros: 10");

        imgJugador3.setMaximumSize(new java.awt.Dimension(40, 40));
        imgJugador3.setMinimumSize(new java.awt.Dimension(40, 40));
        imgJugador3.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout panelJugador3Layout = new javax.swing.GroupLayout(panelJugador3);
        panelJugador3.setLayout(panelJugador3Layout);
        panelJugador3Layout.setHorizontalGroup(
            panelJugador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador3Layout.createSequentialGroup()
                .addGroup(panelJugador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJugador3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(imgJugador3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelJugador3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(murosJugador3))
                    .addGroup(panelJugador3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreJugador3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelJugador3Layout.setVerticalGroup(
            panelJugador3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgJugador3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreJugador3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(murosJugador3)
                .addGap(23, 23, 23))
        );

        panelJugador4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelJugador4.setMaximumSize(new java.awt.Dimension(170, 121));
        panelJugador4.setMinimumSize(new java.awt.Dimension(170, 121));
        panelJugador4.setPreferredSize(new java.awt.Dimension(170, 121));

        nombreJugador4.setText("Inactivo");

        murosJugador4.setText("Muros: 10");

        imgJugador4.setMaximumSize(new java.awt.Dimension(40, 40));
        imgJugador4.setMinimumSize(new java.awt.Dimension(40, 40));
        imgJugador4.setPreferredSize(new java.awt.Dimension(40, 40));

        javax.swing.GroupLayout panelJugador4Layout = new javax.swing.GroupLayout(panelJugador4);
        panelJugador4.setLayout(panelJugador4Layout);
        panelJugador4Layout.setHorizontalGroup(
            panelJugador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador4Layout.createSequentialGroup()
                .addGroup(panelJugador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelJugador4Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(imgJugador4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelJugador4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(murosJugador4))
                    .addGroup(panelJugador4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(nombreJugador4, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelJugador4Layout.setVerticalGroup(
            panelJugador4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelJugador4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgJugador4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreJugador4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(murosJugador4)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout infoJugadoresLayout = new javax.swing.GroupLayout(infoJugadores);
        infoJugadores.setLayout(infoJugadoresLayout);
        infoJugadoresLayout.setHorizontalGroup(
            infoJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoJugadoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(panelJugador3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelJugador4, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        infoJugadoresLayout.setVerticalGroup(
            infoJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoJugadoresLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(infoJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelJugador4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(infoJugadoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(panelJugador1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(panelJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(panelJugador3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoJugadores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(infoJugadores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    /**
     * Recibe un movimiento realizado por un jugador y la casilla anterior en la que se encontraba.
     * Cambia la ficha a la posicion indicada en movimientoRealizado y en la posicion
     * anterior coloca la imagen de la casilla vacia.
     * @param _m MovimientoRealizado movimiento realizado por el jugador
     * @param _posAnterior Casilla del tablero en la que se encontraba el jugador antes de moverse
     */
    public void representarMovimiento(MovimientoRealizado _m, Casilla _posAnterior) {
        try {
            Movimiento mov = _m.getMovimiento();
            Jugador jugador = _m.getJugador();
            Ficha ficha = jugador.getFicha();
            Posicion pos = mov.getPosicion();
            Object elementoJuego = mov.getElementoJuego();

            if (elementoJuego instanceof Muro) {
                if (elementoJuego != null) {
                    if (((Muro) (elementoJuego)).getAlineacion().equals(juegoQuoridor.OntologiaQuoridor.ALINEACION_HORIZONTAL)) {
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
            } else {//Es una Ficha
                URL url = null;
                String path = null;
                switch (ficha.getColor()) {
                    case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1:
                        path = "casilla-ficha-rojo.png";
                        break;
                    case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2:
                        path = "casilla-ficha-azul.png";
                        break;
                    case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3:
                        path = "casilla-ficha-verde.png";
                        break;
                    case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4:
                        path = "casilla-ficha-negro.png";
                        break;
                    default:
                        break;
                }
                url = this.getClass().getResource(path);
                ImageIcon imgFicha = new ImageIcon(url);
                labels[pos.getCoorX() * 2][pos.getCoorY() * 2].setIcon(imgFicha);

                //Cambio la imagen de la posicion anterior que tenia la ficha por una vacia
                path = "tablero.png";
                url = this.getClass().getResource(path);
                ImageIcon casilla = new ImageIcon(url);
                labels[_posAnterior.getX() * 2][_posAnterior.getY() * 2].setIcon(casilla);
                System.out.println("He cambiado de posicion");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Cambia las imagenes de las casillas en las que estarán los jugadores al inicio del juego
     * de la casilla vacia a la imagen de la ficha
     * @param _c Posiciones de los jugadores
     */
    public void cargaFichas(ArrayList<Casilla> _c) {
        URL url;
        String path = null;
        String path2=null;
        ImageIcon imgFicha;
        for(Casilla c:_c){
            switch (c.getJugador().getFicha().getColor()) {
                case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1:
                    path = "casilla-ficha-rojo.png";
                    path2 = "ficha-rojo.png";
                    url=this.getClass().getResource(path2);
                    imgFicha = new ImageIcon(url);
                    panelJugador1.setEnabled(true);
                    imgJugador1.setIcon(imgFicha);
                    nombreJugador1.setText(c.getJugador().getAgenteJugador().getName());
                    murosJugador1.setText("Muros: 10");
                    break;
                case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2:
                    path = "casilla-ficha-azul.png";
                    path2 = "ficha-azul.png";
                    url=this.getClass().getResource(path2);
                    imgFicha = new ImageIcon(url);
                    panelJugador2.setEnabled(true);
                    imgJugador2.setIcon(imgFicha);
                    nombreJugador2.setText(c.getJugador().getAgenteJugador().getName());
                    murosJugador2.setText("Muros: 10");
                    break;
                case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3:
                    path = "casilla-ficha-verde.png";
                    path2 = "ficha-verde.png";
                    url=this.getClass().getResource(path2);
                    imgFicha = new ImageIcon(url);
                    panelJugador3.setEnabled(true);
                    imgJugador3.setIcon(imgFicha);
                    nombreJugador3.setText(c.getJugador().getAgenteJugador().getName());
                    murosJugador3.setText("Muros: 10");
                    break;
                case juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4:
                    path = "casilla-ficha-negro.png";
                    path2 = "ficha-negro.png";
                    url=this.getClass().getResource(path2);
                    imgFicha = new ImageIcon(url);
                    panelJugador4.setEnabled(true);
                    imgJugador4.setIcon(imgFicha);
                    nombreJugador4.setText(c.getJugador().getAgenteJugador().getName());
                    murosJugador4.setText("Muros: 10");
                    break;
                default:
                    break;
            }
            
            url = this.getClass().getResource(path);
            imgFicha = new ImageIcon(url);
            labels[c.getX()*2][c.getY()*2].setIcon(imgFicha);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imgJugador1;
    private javax.swing.JLabel imgJugador2;
    private javax.swing.JLabel imgJugador3;
    private javax.swing.JLabel imgJugador4;
    private javax.swing.JPanel infoJugadores;
    private javax.swing.JLabel murosJugador1;
    private javax.swing.JLabel murosJugador2;
    private javax.swing.JLabel murosJugador3;
    private javax.swing.JLabel murosJugador4;
    private javax.swing.JLabel nombreJugador1;
    private javax.swing.JLabel nombreJugador2;
    private javax.swing.JLabel nombreJugador3;
    private javax.swing.JLabel nombreJugador4;
    private javax.swing.JPanel panelJugador1;
    private javax.swing.JPanel panelJugador2;
    private javax.swing.JPanel panelJugador3;
    private javax.swing.JPanel panelJugador4;
    private javax.swing.JPanel panelTablero;
    // End of variables declaration//GEN-END:variables

}
