/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import jade.core.AID;
import java.util.ArrayList;
import java.util.LinkedList;
import juegosTablero.elementos.Ficha;
import juegosTablero.elementos.Jugador;
import juegosTablero.elementos.Partida;

/**
 *  Esta clase nos ayuda a gestionar las partidas guardando la informacion de cada una.
 * @author jalr0005
 */
public class PartidaActiva {
    
    private Partida partida;
    //Array con las posiciones en cada momento de los jugadores
    private ArrayList<Casilla> posJugador;
    private ArrayList<Jugador> jugadores;
    //Fichas disponibles (todas al empezar la partida)
    private LinkedList<Ficha> fichasDisponibles;
    //Variable incremental para gestionar los turnos
    private int turno = 0;
    
    /**
     * Recibe un objeto partida y añade las 4 fichas como disponibles
     * @param _partida 
     */
    public PartidaActiva(Partida _partida) {
        posJugador = new ArrayList<Casilla>();
        partida = _partida;
        jugadores = new ArrayList<Jugador>();

        fichasDisponibles = new LinkedList<Ficha>();
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4));

    }
    
    /**
     * Redibe un array con los jugadores y les asigna una casilla del tablero segun el color de su ficha
     * @param _jugadores 
     */
    public void setJugadores(ArrayList<Jugador> _jugadores) {
        if (_jugadores.size() == 2) {
            for (Jugador _jugador : _jugadores) {
                jugadores.add(_jugador);
                if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1)) {
                    Casilla c = new Casilla(4, 0);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2)) {
                    Casilla c = new Casilla(4, 8);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                }
            }
        } else {
            for (Jugador _jugador : _jugadores) {
                jugadores.add(_jugador);
                if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1)) {
                    Casilla c = new Casilla(4, 0);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2)) {
                    Casilla c = new Casilla(8, 4);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3)) {
                    Casilla c = new Casilla(4, 8);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor().equals(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4)) {
                    Casilla c = new Casilla(0, 4);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                }
            }
        }
    }
    
    /**
     * Dado el identificador del jugador devolvemos la casilla en la que se encuentra
     * @param _jugador
     * @return Casilla en la que se encuentra el jugador
     */
    public Casilla getPosicionJugador(AID _jugador) {
        Casilla r = null;
        for (Casilla pos : posJugador) {
            if (pos.getJugador().getAgenteJugador().equals(_jugador)) {
                r = pos;
//                posJugador.remove(pos);
            }
        }
        return r;
    }
    
    /**
     * Cambia la posicion de un jugador, este metodo se llama cuando un jugador hace un movimiento
     * @param _jugador
     * @param x
     * @param y 
     */
    public void setPosicionJugador(AID _jugador, int x, int y) {
        for (Casilla pos : posJugador) {
            if (pos.getJugador().getAgenteJugador().equals(_jugador)) {
                pos.setXY(x, y);
            }
        }
    }
    
    /**
     * Para saber a que jugador le toca y mandarle el turno utilizamos este metodo
     * @return 
     */
    public Jugador getSiguienteTurno() {
        Jugador j = posJugador.get(turno % partida.getNumeroJugadores()).getJugador();
        turno++;
        return j;
    }
    
    /**
     * 
     * @return lista de jugadores
     */
    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    
    /**
     * Devuelve la siguiente ficha a asignar a un jugador
     * @return ficha
     */
    public Ficha getSiguienteFicha() {
        return fichasDisponibles.pollFirst();
    }
    
    /**
     * 
     * @return partida
     */
    public Partida getPartida() {
        return partida;
    }

    /**
     * @return the posJugador
     */
    public ArrayList<Casilla> getPosJugadores() {
        return posJugador;
    }
    
    /**
     * Añade al array de posiciones de jugador la casilla
     * @param casilla 
     */
    public void setposJugador(Casilla casilla) {
        posJugador.add(casilla);
    }

}
