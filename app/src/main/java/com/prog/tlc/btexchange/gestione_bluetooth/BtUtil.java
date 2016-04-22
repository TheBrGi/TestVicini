package com.prog.tlc.btexchange.gestione_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.lmbluetoothsdk.BluetoothController;
import com.prog.tlc.btexchange.lmbluetoothsdk.base.BluetoothListener;
import com.prog.tlc.btexchange.protocollo.NeighborGreeting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.util.LinkedList;
import java.util.UUID;

/**
 * Created by BrGi on 16/03/2016.
 */
public class BtUtil {
    public static final UUID GREETINGS = UUID.fromString("91771218-057b-11e6-b512-3e1d05defe78");
    public static final UUID REQ = UUID.fromString("987f7afa-057b-11e6-b512-3e1d05defe78");
    public static final UUID REP = UUID.fromString("a39baba2-057b-11e6-b512-3e1d05defe78");
    public static final UUID MESSAGE = UUID.fromString("aa407b04-057b-11e6-b512-3e1d05defe78");
    public static final UUID RICERCA = UUID.fromString("352054a8-058e-11e6-b512-3e1d05defe78");
    private static final long ATTESA = 3000;
    private static Context context;
    public static BluetoothController sender = new BluetoothController();

    private BtUtil() {
    }

    public static String getMioNome() {
        return getBtAdapter().getName();
    }

    public static void setContext(Context c) {
        context = c;
    }

    public static Context getContext() {
        return context;
    }

    public static void enableBt() {
        BluetoothController bc = new BluetoothController();
        bc.build(getContext());
        if (!bc.isEnabled()) {
            bc.openBluetooth();
            //bc.setDiscoverable(0);
            while (!bc.isEnabled()) {
            }
        }
        bc.release();
    }

    public static BluetoothAdapter getBtAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static String getMACMioDispositivo() {
        String MAC = BluetoothAdapter.getDefaultAdapter().getAddress();
        return MAC;
    }

    public static LinkedList<Node> cercaVicini() {
        BluetoothController controllerRicerca = new BluetoothController();
        controllerRicerca.build(getContext());
        controllerRicerca.setAppUuid(BtUtil.RICERCA);
        //BtUtil.enableBt();
        controllerRicerca.startScan();
        final LinkedList[] lista = new LinkedList[1];
        lista[0] = new LinkedList<Node>();
        controllerRicerca.setBluetoothListener(new BluetoothListener() {

            @Override
            public void onActionStateChanged(int preState, int state) {
                // Callback when bluetooth power state changed.
            }

            @Override
            public void onActionDiscoveryStateChanged(String discoveryState) {
                // Callback when local Bluetooth adapter discovery process state changed.
            }

            @Override
            public void onActionScanModeChanged(int preScanMode, int scanMode) {
                // Callback when the current scan mode changed.
            }

            @Override
            public void onBluetoothServiceStateChanged(int state) {
                // Callback when the connection state changed.
            }

            @Override
            public void onActionDeviceFound(BluetoothDevice device, short rssi) {
                // Callback when found device.
                Node nodo = new Node(device.getName(), device.getAddress());
                lista[0].add(nodo);
            }

            @Override
            public void onReadData(final BluetoothDevice device, final Object data) {
                // Callback when remote device send data to current device.
            }
        });
        try {
            Thread.sleep(ATTESA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        controllerRicerca.cancelScan();
        controllerRicerca.release();
        return lista[0];
    }

    /*public static String riceviStringa() {
        BluetoothController bc = new BluetoothController();
        bc.build(getContext());

        //enableBt();

        bc.startAsServer();
        final String[] s = new String[1];
        final boolean[] isNull = new boolean[1];//nel caso sia true interrompe ciclo bloccante
        bc.setBluetoothListener(new BluetoothListener() {

            @Override
            public void onActionStateChanged(int preState, int state) {
                // Callback when bluetooth power state changed.
            }

            @Override
            public void onActionDiscoveryStateChanged(String discoveryState) {
                // Callback when local Bluetooth adapter discovery process state changed.
            }

            @Override
            public void onActionScanModeChanged(int preScanMode, int scanMode) {
                // Callback when the current scan mode changed.
            }

            @Override
            public void onBluetoothServiceStateChanged(int state) {
                // Callback when the connection state changed.
            }

            @Override
            public void onActionDeviceFound(BluetoothDevice device, short rssi) {
                // Callback when found device.
            }

            @Override
            public void onReadData(final BluetoothDevice device, final Object data) {
                // Callback when remote device send data to current device.
                //bc.disconnect();
                s[0] = (String) data;
                if (s[0] == null) {
                    isNull[0] = true;
                }
            }
        });
        while (s[0] == null && isNull[0] == false) {
        }
        bc.disconnect();
        bc.release();
        return s[0];
    } */

    /*public static void mandaStringa(final String s, final String addr) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Sender send = new Sender(addr, s, MESSAGE);
                send.start();
                try {
                    long attesaMax = 5000;
                    send.join(attesaMax);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (send.isAlive())
                        send.interrupt();
                }
            }
        };
        new Thread(r).start();
    } */


        public static void inviaGreeting(NeighborGreeting greet, Node vicino) {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bd = ba.getRemoteDevice(vicino.getMACAddress());
        try {
            BluetoothSocket bs = bd.createInsecureRfcommSocketToServiceRecord(GREETINGS);
            bs.connect();
            Log.d("CoNNNN",String.valueOf(bs.isConnected()));
            ObjectOutputStream oos = new ObjectOutputStream(bs.getOutputStream());
            oos.writeObject(greet);
            Log.d("invio!!!!",greet.getNodiNoti().toString());
            oos.flush();
            bs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NeighborGreeting riceviGreeting() {
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        try {
            BluetoothServerSocket bss = ba.listenUsingInsecureRfcommWithServiceRecord("BtEx", GREETINGS);
            BluetoothSocket bs = bss.accept();
            Log.d("CONNESSIONE AVVE", String.valueOf(bs.isConnected()));
            InputStream a = bs.getInputStream();
            ObjectInputStream ois=null;
            while(ois==null && bs.isConnected()) {
                try {
                    ois = new ObjectInputStream(a);
                }catch (NullPointerException ne) {
                    ne.printStackTrace();
                }
            }
            if(ois==null) {
                bs.close();
                bss.close();
                Log.d("connessione caduta","5456");
                return null;
            }
            Object o = ois.readObject();
            Log.d("Lett avv", String.valueOf(bs.isConnected()));
            NeighborGreeting ng = (NeighborGreeting) o;
            Log.d("Ogg ricevuto", "8878787");
            bs.close();
            bss.close();
            return ng;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
