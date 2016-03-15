package com.prog.tlc.btexchange.gestioneDispositivo;

import android.bluetooth.BluetoothAdapter;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Dispositivo {
    private LinkedList<Node> listaNodi;
    private HashMap<String, Percorso> tabellaDiRouting;
    private String nome, MACAddress;
    private int sequenceNumber;//si incrementa dopo l'avvio di una route discovery o di una reply

    public Dispositivo(String n) {
        nome = n;
        String MACAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        int sequenceNumber = 1;
        listaNodi = new LinkedList<>();
        tabellaDiRouting = new HashMap<>();
    }

    public void incrementaSeqNum() {
        sequenceNumber++;
    }

    public void aggiungiNodo(Node n) {
        listaNodi.addFirst(n);
    }

    public void aggiungiPercorso(Percorso p) {
        String destinazione = p.getDestinazione().getMACAddress();
        tabellaDiRouting.put(destinazione, p);
    }

    public boolean esistePercorso(String destinazione) {
        return tabellaDiRouting.containsKey(destinazione);
    }

    public Percorso getPercorso(String destinazione) {
        if (esistePercorso(destinazione))
            return tabellaDiRouting.get(destinazione);
        return null;
    }

}
