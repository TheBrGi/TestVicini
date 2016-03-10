package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Percorso {
    private n destinazione, nextHop;
    private int numeroHop;

    public Percorso(n dest, n nextH, int nHop){
        destinazione = dest;
        nextHop = nextH;
        numeroHop = nHop;
    }

    public n getDestinazione() {
        return destinazione;
    }

    public n getNextHop() {
        return nextHop;
    }

    public int getNumeroHop() {
        return numeroHop;
    }
}
