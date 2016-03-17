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
                else { // potrebbe andare male
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
            if(myDev.getMACAddress().equals(rr.getDest_addr()))  //se siamo noi la destinazione
                reply(rr);
            else if(myDev.esistePercorso(rr.getDest_addr())) //se conosciamo un percorso fino alla destinazione lo inviamo
                reply(rr,myDev.getPercorso(rr.getSource_addr()));
            else
                rilanciaRREQ(rr);
        }

        private void estrapolaPercorsoRREQ(RouteRequest rr) {
            Percorso p = new Percorso(rr.getSource_addr(),rr.getLast_sender(),rr.getHop_cnt(),rr.getSource_sequence_number());
            myDev.aggiungiPercorso(p);
        }

        private void rilanciaRREQ(RouteRequest rr) {
            LinkedList<Node> vicini = gestoreVicini.getVicini();
            RouteRequest nuovoRR = new RouteRequest(rr.getSource_addr(),rr.getSource_sequence_number(),rr.getDest_addr(),rr.getDest_sequence_number(),rr.getHop_cnt(),rr.getLast_sender());
            nuovoRR.incrementaHop_cnt();
            nuovoRR.setLast_sender(myDev.getMACAddress());
            for(Node n: vicini){
                if(!rr.getLast_sender().equals(n)) {
                    //TODO inviaRREQ(nuovoRR,MAC)           ******BT
                }
            }
            rr = null;
        }

        private void reply(RouteRequest rr, Percorso p) { //il source sarà sempre tale sia in un verso che nell'altro
            int seqDest = p.getSequenceNumber();
            int numHopDaQuiADest = p.getNumeroHop();
            RouteReply routeRep = new RouteReply(rr.getSource_addr(),rr.getDest_addr(),seqDest,numHopDaQuiADest);
            //TODO inviaReply(routeRep, rr.getLast_sender());
            rr = null;
        }

        private void reply(RouteRequest rr) {
            RouteReply routeRep = new RouteReply(rr.getSource_addr(),rr.getDest_addr(),myDev.getSequenceNumber(),1);
            /*TODO funzionalita bt che invia il nuovo route reply al nodo che ha inviato la richiesta(last sender) specificato nei
                parametri
                inviaReply(routeRep, rr.getLast_sender());
             */
            rr = null;
        }
    }

    private class HandlerReply extends Thread { //TODO gestire incremento di hop nel reply
        public void run() {
            while(true) {
                //TODO RouteReply rr = ascoltaRichiesta(); metodo statico del bt
            }
        }
    }
}
