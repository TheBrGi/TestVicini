package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Node {
    private String nome, MACAddress;
    private int broadcast_id;//si incrementa per ogni RREQ inviata
    private int sequenceNumber;//si incrementa dopo l'avvio di una route discovery o di una reply
    private boolean vicino;


    public Node(String n, String MAC, int b_id, int sq, boolean v) {
        nome = n;
        MACAddress = MAC;
        broadcast_id = b_id;
        sequenceNumber = sq;
        vicino = v;
    }

    public int getBroadcast_id() {
        return broadcast_id;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public boolean vicino() {
        return vicino;
    }

    public String getNome() {
        return nome;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public void incrementBroadcastId() {
        broadcast_id++;
    }

    public void setVicino(boolean vicino) {
        this.vicino = vicino;
    }
}
