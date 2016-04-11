/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import jade.core.AID;
import java.util.ArrayList;
import juegosTablero.elementos.Jugador;
import juegosTablero.elementos.Partida;

/**
 *
 * @author Hacker
 */
public class PartidaActiva {
    
    private Partida partida;
    private ArrayList<Casilla> posJugador;
    
    public PartidaActiva(Partida _partida, ArrayList<Jugador> _jugadores){
        posJugador=new ArrayList<Casilla>();
        partida=_partida;
        for (Jugador _jugador : _jugadores) {
            if(_jugador.getFicha().getColor()==juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_1){
                Casilla  c=new Casilla(4, 0);
                c.setJugador(_jugador);
                posJugador.add(c);
            }else if(_jugador.getFicha().getColor()==juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_2){
                Casilla  c=new Casilla(8, 4);
                c.setJugador(_jugador);
                posJugador.add(c);
            }else if(_jugador.getFicha().getColor()==juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_3){
                Casilla  c=new Casilla(4, 8);
                c.setJugador(_jugador);
                posJugador.add(c);
            }else if(_jugador.getFicha().getColor()==juegoQuoridor.OntologiaQuoridor.COLOR_FICHA_4){
                Casilla  c=new Casilla(0, 4);
                c.setJugador(_jugador);
                posJugador.add(c);
            }
        }
    }
    
    public Casilla getPosicionJugador(AID _jugador){
        Casilla r=null;
        for (Casilla pos : posJugador) {
            if(pos.getJugador().getAgenteJugador()==_jugador){
                r=pos;
            }
        }
        return r;
    }
    
    public Partida getPartidaActual(){
        return partida;
    }
    
}
