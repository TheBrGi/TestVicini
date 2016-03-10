package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Percorso {
    private Device destinazione, nextHop;
    private int numeroHop;

    public Percorso(Device dest, Device nextH, int nHop){
        destinazione = dest;
        nextHop = nextH;
        numeroHop = nHop;
    }

    public Device getDestinazione() {
        return destinazione;
    }

    public Device getNextHop() {
        return nextHop;
    }

    public int getNumeroHop() {
        return numeroHop;
    }
}
