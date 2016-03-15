package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Domenico on 15/03/2016.
 */
public class NeighborGreeting implements Serializable {
    private String MACOrigine;
    private LinkedList<Node> nodiNoti;

    public NeighborGreeting(String MAC, LinkedList<Node> nodi) {
        MACOrigine = MAC;
        nodiNoti = nodi;
    }

    public String getMACOrigine() {
        return MACOrigine;
    }

    public LinkedList<Node> getNodiNoti() {
        return nodiNoti;
    }
}
