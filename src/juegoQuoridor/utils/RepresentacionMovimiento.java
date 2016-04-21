/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juegoQuoridor.utils;

import juegoQuoridor.elementos.MovimientoRealizado;
import juegosTablero.elementos.Posicion;

/**
 *
 * @author hacker
 */
public class RepresentacionMovimiento {
    
    private MovimientoRealizado mr;
    private Posicion posAnterior; 
    
    public RepresentacionMovimiento(MovimientoRealizado _mr, Posicion _p){
        mr=_mr;
        posAnterior=_p;
    }

    /**
     * @return the mr
     */
    public MovimientoRealizado getMr() {
        return mr;
    }

    /**
     * @return the posAnterior
     */
    public Posicion getPosAnterior() {
        return posAnterior;
    }
}
