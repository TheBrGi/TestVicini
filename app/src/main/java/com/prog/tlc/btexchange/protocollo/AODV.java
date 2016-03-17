package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.*;
import java.util.LinkedList;

/**
 * Created by Domenico on 15/03/2016.
 */
public class AODV {
    private Dispositivo myDev;
    private GestoreVicini gestoreVicini;

    /*dentro il costruttore, passo passo, svolgiamo le attività protocollari, appoggiandoci
    ad altri metodi privati della classe*/
    public AODV(Dispositivo d, long tempoAttesaAggVicini) {
        myDev = d;
        gestoreVicini = new GestoreVicini(d,tempoAttesaAggVicini);
        gestoreVicini.start();
        new HandlerReq().start();
        new HandlerReply().start();
    }


    //TODO public static void cercaPercorso(String dest);

    private class HandlerReq extends Thread {
        public void run() {
            while(true) {
                RouteRequest rr=null;//TODO RouteRequest rr = ascoltaRichiesta(); metodo statico del bt
                if(!myDev.getRREQRicevuti().containsKey(rr.getSource_addr())) {
                    gestisciRREQ(rr);
                    myDev.aggiungiRREQ(rr);
                }
                else {
                    int sequenceNumberSource = myDev.getRREQRicevuti().get(rr.getSource_addr());
                    if(sequenceNumberSource<rr.getSource_sequence_number()) {
                        gestisciRREQ(rr);
                        myDev.aggiungiRREQ(rr);
                    }
                }
            }
        }

        private void gestisciRREQ(RouteRequest rr) {
            estrapolaPercorsoRREQ(rr);
            if(myDev.getMACAddress().equals(rr.getDest_addr()) || myDev.esistePercorso(rr.getDest_addr()))
                ;
                //TODO reply(myDev.getPercorso(rr.getSource_addr()));
            else
                rilanciaRREQ(rr);
        }

        private void estrapolaPercorsoRREQ(RouteRequest rr) {
            Percorso p = new Percorso(rr.getSource_addr(),rr.getLast_sender(),rr.getHop_cnt(),rr.getSource_sequence_number());
            myDev.aggiungiPercorso(p);
        }

        private void rilanciaRREQ(RouteRequest rr) {
            LinkedList<Node> vicini = gestoreVicini.getVicini();
            rr.incrementaHop_cnt();
            rr.setLast_sender(myDev.getMACAddress());
            for(Node n: vicini){
                if(!rr.getLast_sender().equals(n)) {
                    //TODO inviaRREQ(rr,MAC)           ******BT
                }
            }
        }
    }

    private class HandlerReply extends Thread {
        public void run() {
            while(true) {
                //TODO RouteReply rr = ascoltaRichiesta(); metodo statico del bt
            }
        }
    }
}
