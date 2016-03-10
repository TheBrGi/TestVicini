package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Percorso {
    private Nodo destinazione, nextHop;
    private int numeroHop;

    public Percorso(Nodo dest, Nodo nextH, int nHop){
        destinazione = dest;
        nextHop = nextH;
        numeroHop = nHop;
    }

    public Nodo getDestinazione() {
        return destinazione;
    }

    public Nodo getNextHop() {
        return nextHop;
    }

    public int getNumeroHop() {
        return numeroHop;
    }
}
