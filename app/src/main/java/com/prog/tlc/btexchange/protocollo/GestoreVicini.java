package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.*;

import java.util.LinkedList;

/**
 * Created by Domenico on 15/03/2016.
 */
public class GestoreVicini extends Thread {
    /*invia greetings ai vicini e gestisce la ricezione
    aspetta per un periodo fissato T e ripete le operazioni cliclicamente
    [modifica la lista dei nodi del dispositivo]
     */

    private Dispositivo myDisp;
    private final int ATTESA;

    public GestoreVicini(Dispositivo d, int tempoAttesa) {
        myDisp = d;
        ATTESA = tempoAttesa;
        LinkedList<String> vicini; /* funzionalità bluetooth da fare ritorna la lista dei MACAddress dei vicini*/

    }
}
