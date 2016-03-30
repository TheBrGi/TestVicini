package com.prog.tlc.btexchange.protocollo;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Domenico on 15/03/2016.
 */
public class NeighborGreeting implements Serializable {
    private String MACOrigine;
    private List<Node> nodiNoti;

    public NeighborGreeting(String MAC, List<Node> nodi) {
        MACOrigine = MAC;
        nodiNoti = nodi;
    }

    public String getMACOrigine() {
        return MACOrigine;
    }

    public List<Node> getNodiNoti() {
        return nodiNoti;
    }
}
