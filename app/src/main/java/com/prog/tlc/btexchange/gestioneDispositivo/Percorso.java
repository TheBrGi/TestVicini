package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Percorso {
    private Node destinazione, nextHop;
    private int numeroHop;

    public Percorso(Node dest, Node nextH, int nHop){
        destinazione = dest;
        nextHop = nextH;
        numeroHop = nHop;
    }

    public Node getDestinazione() {
        return destinazione;
    }

    public Node getNextHop() {
        return nextHop;
    }

    public int getNumeroHop() {
        return numeroHop;
    }
}
