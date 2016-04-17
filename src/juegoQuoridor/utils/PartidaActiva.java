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
 *
 * @author Hacker
 */
public class PartidaActiva {

    private Partida partida;
    private ArrayList<Casilla> posJugador;
    private ArrayList<Jugador> jugadores;
    private LinkedList<Ficha> fichasDisponibles;
    private int turno = 0;

    public PartidaActiva(Partida _partida) {
        posJugador = new ArrayList<Casilla>();
        partida = _partida;

        fichasDisponibles = new LinkedList<Ficha>();
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3));
        fichasDisponibles.add(new Ficha(juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4));

    }

    public void setJugadores(ArrayList<Jugador> _jugadores) {
        if (_jugadores.size() == 2) {
            for (Jugador _jugador : _jugadores) {
                jugadores.add(_jugador);
                if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1) {
                    Casilla c = new Casilla(4, 0);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2) {
                    Casilla c = new Casilla(4, 8);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                }
            }
        } else {
            for (Jugador _jugador : _jugadores) {
                jugadores.add(_jugador);
                if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1) {
                    Casilla c = new Casilla(4, 0);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2) {
                    Casilla c = new Casilla(4, 8);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3) {
                    Casilla c = new Casilla(0, 4);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                } else if (_jugador.getFicha().getColor() == juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4) {
                    Casilla c = new Casilla(8, 4);
                    c.setJugador(_jugador);
                    posJugador.add(c);
                }
            }
        }

    }

    public Casilla getPosicionJugador(AID _jugador) {
        Casilla r = null;
        for (Casilla pos : posJugador) {
            if (pos.getJugador().getAgenteJugador() == _jugador) {
                r = pos;
            }
        }
        return r;
    }

    public Jugador getSiguienteTurno() {
        turno++;
        return posJugador.get(turno).getJugador();
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public Ficha getSiguienteFicha() {
        return fichasDisponibles.pollFirst();
    }

    public Partida getPartida() {
        return partida;
    }
}
