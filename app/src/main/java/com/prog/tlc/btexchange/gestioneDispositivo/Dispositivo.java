package com.prog.tlc.btexchange.gestioneDispositivo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Domenico on 10/03/2016.
 */
public class Dispositivo {
    private LinkedList<Node> listaNodi;
    private HashMap<String, Percorso> tabellaDiRouting;
    private Node mioDispositivo;

    public Dispositivo(String n) {
        String nome = n;
        String address = BluetoothAdapter.getDefaultAdapter().getAddress();
        int sequenceNumber = 1;
        int broadcast_id = 1;
        listaNodi = new LinkedList<>();
        tabellaDiRouting = new HashMap<>();
        mioDispositivo = new Node(nome, address, broadcast_id, sequenceNumber, false);
    }

    public void incrementaSeqNum() {
        mioDispositivo.incrementBroadcastId();
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
        if (esistePercorso(destinazione)) return tabellaDiRouting.get(destinazione);
        return null;
    }

}
