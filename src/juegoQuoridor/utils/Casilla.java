/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import juegosTablero.elementos.Jugador;

/**
 *
 * @author Hacker
 */
public class Casilla {
    private int x;
    private int y;
    private boolean muroDerecha=false;
    private boolean muroAbajo=false;
    private Jugador jugador;
    
    public Casilla(int _x, int _y){
        x=_x;
        y=_y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the muroDerecha
     */
    public boolean hayMuroDerecha() {
        return muroDerecha;
    }

    /**
     * @param muroDerecha the muroDerecha to set
     */
    public void setMuroDerecha(boolean muroDerecha) {
        this.muroDerecha = muroDerecha;
    }

    /**
     * @return the muroAbajo
     */
    public boolean hayMuroAbajo() {
        return muroAbajo;
    }

    /**
     * @param muroAbajo the muroAbajo to set
     */
    public void setMuroAbajo(boolean muroAbajo) {
        this.muroAbajo = muroAbajo;
    }

    /**
     * @return the jugador
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * @param jugador the jugador to set
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
