package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.*;

import java.sql.Time;
import java.util.LinkedList;

/**
 * Created by Domenico on 15/03/2016.
 */
public class GestoreVicini extends Thread {
    /*invia greetings ai vicini e gestisce la ricezione,
    aspetta per un periodo fissato T e ripete le operazioni ciclicamente
    [modifica la lista dei nodi del dispositivo]
     */

    private Dispositivo myDisp;
    private final long ATTESA;
    private LinkedList<Node> vicini;


    public GestoreVicini(Dispositivo d, long tempoAttesa) {
        myDisp = d;
        ATTESA = tempoAttesa;
        new Ascoltatore().start();
    }

    public void run() { //invia greetings ai vicini (temporizzato)
        while(true) {
            /*vicini = listaVicini()  TODO funzionalità bluetooth da fare ritorna la lista dei nodi dei vicini*/
            NeighborGreeting ng = new NeighborGreeting(myDisp.getMACAddress(),myDisp.getListaNodi());
            for(Node vicino: vicini) {
                //TODO inviaGreeting(ng);
            }
            try {
                this.sleep(ATTESA);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private class Ascoltatore extends Thread {
        public void run() {
            while(true) {
                LinkedList<Node> nuoveInfo; //TODO funzionalità bt che da un greating estrae la tabella di nodi interna
                // TODO mergeNodi(nuoveInfo); non devono esserci duplicati
            }
        }

       //TODO private void mergeNodi(LinkedList<Nodi> l) non devono esserci duplicati, nella lista di myDisp
    }
}
