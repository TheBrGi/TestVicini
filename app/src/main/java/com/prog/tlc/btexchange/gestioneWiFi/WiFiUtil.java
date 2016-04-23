package com.prog.tlc.btexchange.gestioneWiFi;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;
import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.protocollo.NeighborGreeting;
import com.prog.tlc.btexchange.protocollo.RouteReply;
import com.prog.tlc.btexchange.protocollo.RouteRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


public class WiFiUtil {
    private Activity myActivity;
    private ConcurrentLinkedQueue<RouteRequest> rreqs = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<RouteReply> rreps = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<Object> messages = new ConcurrentLinkedQueue<>(); //concretizzare poi cosa inviare
    private ConcurrentLinkedQueue<NeighborGreeting> greetings = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap dispConnessi = new ConcurrentHashMap();


    public WiFiUtil(Activity myActivity) {
        this.myActivity=myActivity;
        new AscoltatoreGenerale().start();
    }

    public void enableWiFi() {
        //TODO
    }

    public static LinkedList<Node> cercaVicini() {
        return null;
    }

    public static void inviaGreeting(NeighborGreeting greet, Node vicino) {
        //TODO connettersi al vicino in Utils
        try {
            Socket s = new Socket(Utils.getIPFromMac(vicino.getMACAddress()),8000); //la porta sarà la stessa in ogni disp
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(greet);
            oos.flush();
            oos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NeighborGreeting riceviGreeting() {
        while (true) {
            if (!greetings.isEmpty()) {
                NeighborGreeting ng = greetings.poll();
                return ng;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class AscoltatoreGenerale extends Thread {
        public void run() {
            //TODO ascoltare tutti i messaggi in ingresso e smistarli alle strutture opportune
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(8000);
                while (true) {
                    Socket client = serverSocket.accept();
                    new AscoltatoreConn(client).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private class AscoltatoreConn extends Thread {
            Socket s = null;

            public AscoltatoreConn(Socket sock) {
                s = sock;
            }

            public void run() {
                try {
                    InputStream inputstream = s.getInputStream();
                    ObjectInputStream ois = new ObjectInputStream(inputstream);
                    Log.d("stream ric","2000");
                    /*Object ric = ois.readObject();
                    if(ric instanceof NeighborGreeting) {
                        greetings.add((NeighborGreeting) ric);
                    }
                    else if(ric instanceof RouteReply) {
                        rreps.add((RouteReply) ric);
                    }
                    else if(ric instanceof RouteRequest) {
                        rreqs.add((RouteRequest) ric);
                    }
                    else if(ric instanceof Object) {
                        messages.add(ric);
                    } */
                    String mex = (String) ois.readObject();
                    Log.d("mex ric",mex);
                    gestisciMess(mex);
                    ois.close();
                    s.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            private void gestisciMess(final String mess) {
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast t = Toast.makeText(myActivity.getApplicationContext(), mess, Toast.LENGTH_SHORT);
                        t.show();
                    }
                });
            }
        }
    }


}
