package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Node {
    private String nome, MACAddress;
    private int sequenceNumber;
    private boolean vicino;


    public Node(String n, String MAC, int sq, boolean v) {
        nome = n;
        MACAddress = MAC;
        sequenceNumber = sq;
        vicino = v;
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

    public void setVicino(boolean vicino) {
        this.vicino = vicino;
    }
}
