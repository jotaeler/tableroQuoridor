/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.agentes;

import juegoQuoridor.utils.JugadorRanking;

/**
 *
 * @author Jonna
 */
public class PairJugadorPosicion {
    private JugadorRanking jugadorR;
    private int posicion;
    
    public PairJugadorPosicion(JugadorRanking jugador, int _posicion){
        jugadorR = jugador;
        posicion = _posicion;
    }

    /**
     * @return the jugadorR
     */
    public JugadorRanking getJugadorR() {
        return jugadorR;
    }

    /**
     * @return the posicion
     */
    public int getPosicion() {
        return posicion;
    }
    
    
}
