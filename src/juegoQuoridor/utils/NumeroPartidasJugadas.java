/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import java.util.Comparator;

/**
 *
 * @author Jonna
 */
public class NumeroPartidasJugadas implements Comparator<JugadorRanking> {

    public NumeroPartidasJugadas() {
    }

    @Override
    public int compare(JugadorRanking j1, JugadorRanking j2) {
        if(j1.getPartidasJugadas() < j2.getPartidasJugadas()){
            return 1;
        }
        if(j1.getPartidasJugadas() > j2.getPartidasJugadas()){
            return -1;
        }
        return 0;
    }
    
}
