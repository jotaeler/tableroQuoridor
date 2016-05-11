/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import jade.core.AID;

/**
 *
 * @author Jonna
 */
public class JugadorRanking {
    private AID jugador;
    private int partidasJugadas;
    private int posicionAceptados;
    private int partidasGanadas;
    
    public JugadorRanking(AID _jugador, int pos){
        jugador = _jugador;
        partidasJugadas = 1;        
        posicionAceptados = pos;
        partidasGanadas = 0;
    }
    
    public JugadorRanking(AID _jugador){
        jugador = _jugador;
        partidasJugadas = 1;
        partidasGanadas = 0;
    }

    /**
     * @return the jugador
     */
    public AID getJugador() {
        return jugador;
    }

    /**
     * @return the partidasGanadas
     */
    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    /**
     * @param partidasGanadas the partidasJugadas to set
     */
    public void incrementarPartida() {
        this.partidasJugadas++;
    }
    
    public void incrementarPartidaGanada() {
        this.partidasGanadas++;
    }
    
    public int getPartidasGanadas(){
        return partidasGanadas;
    }

    /**
     * @return the posicionAceptados
     */
    public int getPosicionAceptados() {
        return posicionAceptados;
    }    
}
