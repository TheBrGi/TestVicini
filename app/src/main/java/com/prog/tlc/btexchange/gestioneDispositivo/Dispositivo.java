package com.prog.tlc.btexchange.gestioneDispositivo;

import java.util.LinkedList;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Dispositivo {
    private LinkedList<n> listaNodi;
    private LinkedList<Percorso> tabellaDiRouting;
    private int sequenceNumber;
    private String nome, MACAddress;

    public  Dispositivo(String n) {
        nome = n;
        sequenceNumber = 1;
        listaNodi = new LinkedList<>();
        tabellaDiRouting = new LinkedList<>();
        /* TODO MACAddress = metodo statico del package bluetooth */
    }

    public void incrementaSeqNum() { sequenceNumber++; }

    public void aggiungiNodo(n n) {
        listaNodi.addFirst(n);
    }

    public void aggiungiPercorso(Percorso p) {
        tabellaDiRouting.addFirst(p);
    }

    public boolean esistePercorso(String destinazione) {
        for(Percorso p : tabellaDiRouting) {
            if(p.getDestinazione().getNome().equals(destinazione))
                return true;
        }
        return false;
    }






}
