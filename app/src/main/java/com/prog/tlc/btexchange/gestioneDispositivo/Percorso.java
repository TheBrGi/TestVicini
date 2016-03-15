package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Percorso {
    private String destinazione, nextHop; //MACaddress di destinazione e nexthop
    private int numeroHop,sequenceNumber;

    public Percorso(String dest, String nextH, int nHop){
        destinazione = dest;
        nextHop = nextH;
        numeroHop = nHop;
    }
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sq) {
        sequenceNumber = sq;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public String getNextHop() {
        return nextHop;
    }

    public int getNumeroHop() {
        return numeroHop;
    }
}
