package com.prog.tlc.btexchange.protocollo;
import com.prog.tlc.btexchange.gestioneDispositivo.Dispositivo;

/**
 * Created by Domenico on 15/03/2016.
 */
public class AODV {
    private Dispositivo myDev;

    /*dentro il costruttore, passo passo, svolgiamo le attività protocollari, appoggiandoci
    ad altri metodi privati della classe*/
    public AODV(Dispositivo mD, int tempoAttesaAggVicini) {
        myDev = mD;

        GestoreVicini gestoreVicini = new GestoreVicini(mD,tempoAttesaAggVicini);

    }
}
