package com.prog.tlc.btexchange.gestioneDispositivo;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Node {
    private String nome, MACAddress;


    public Node(String n, String MAC) {
        nome = n;
        MACAddress = MAC;
    }

    public String getMACAddress() {
        return MACAddress;
    }
    public String getNome() {
        return nome;
    }

}
