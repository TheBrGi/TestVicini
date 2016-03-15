package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.Dispositivo;

/**
 * Created by Domenico on 15/03/2016.
 */
public class AODV {
    private Dispositivo myDev;

    /*dentro il costruttore, passo passo, svolgiamo le attività protocollari, appoggiandoci
    ad altri metodi privati della classe*/
    public AODV(Dispositivo d, long tempoAttesaAggVicini) {
        myDev = d;
        GestoreVicini gestoreVicini = new GestoreVicini(d,tempoAttesaAggVicini);
        gestoreVicini.start();
        new HandlerReq().start();
        new HandlerReply().start();
    }


    //TODO public static void cercaPercorso(String dest);

    private class HandlerReq extends Thread {
        public void run() {
            while(true) {
                //TODO RouteRequest rr = ascoltaRichiesta(); metodo statico del bt
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
