package com.prog.tlc.btexchange.gestioneWiFi;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.protocollo.NeighborGreeting;
import java.util.Collections;
import java.util.LinkedList;


public class WiFiUtil {
    private static LinkedList<NeighborGreeting> greetings = (LinkedList<NeighborGreeting>) Collections.synchronizedList(new LinkedList<NeighborGreeting>());

    private WiFiUtil() {
        new Ascoltatore().start();
    }

    public static void enableWiFi() {
    //TODO
    }

    public static LinkedList<Node> cercaVicini() {
        return null;
    }

    public static void inviaGreeting(NeighborGreeting greet, Node vicino) {

    }

    public static NeighborGreeting riceviGreeting() {
        while(true) {
            if(!greetings.isEmpty()) {
                NeighborGreeting ng = greetings.removeLast();
                return ng;
            }
            try {
                Thread.sleep(500);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Ascoltatore extends Thread {
        public  void run() {
            //TODO ascoltare tutti i messaggi in ingresso e smistarli alle strutture opportune

        }
    }
}
