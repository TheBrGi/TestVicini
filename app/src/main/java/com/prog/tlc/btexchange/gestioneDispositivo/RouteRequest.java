package com.prog.tlc.btexchange.gestioneDispositivo;

import java.io.Serializable;

/**
 * Created by BrGi on 12/03/2016.
 */
public class RouteRequest implements Serializable{
    private String source_addr;
    private int source_sequence_number;
    private int broadcast_id;
    private String dest_addr;
    private int dest_sequence_number;
    private int hop_cnt;

    public RouteRequest(String source_addr, int source_sequence_number, int broadcast_id, String dest_addr, int dest_sequence_number, int hop_cnt) {
        this.source_addr = source_addr;
        this.source_sequence_number = source_sequence_number;
        this.broadcast_id = broadcast_id;
        this.dest_addr = dest_addr;
        this.dest_sequence_number = dest_sequence_number;
        this.hop_cnt = hop_cnt;
    }

    public String getSource_addr() {
        return source_addr;
    }

    public int getSource_sequence_number() {
        return source_sequence_number;
    }

    public int getBroadcast_id() {
        return broadcast_id;
    }

    public String getDest_addr() {
        return dest_addr;
    }

    public int getDest_sequence_number() {
        return dest_sequence_number;
    }

    public int getHop_cnt() {
        return hop_cnt;
    }
}
