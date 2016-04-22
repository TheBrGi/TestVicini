package com.prog.tlc.btexchange.gestione_bluetooth;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.lmbluetoothsdk.BluetoothController;
import com.prog.tlc.btexchange.lmbluetoothsdk.base.BluetoothListener;
import com.prog.tlc.btexchange.lmbluetoothsdk.base.State;
import com.prog.tlc.btexchange.protocollo.NeighborGreeting;

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
    public static BluetoothController messContr = BluetoothController.getInstance();

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

    public static LinkedList<Node> cercaVicini() {
        //BtUtil.enableBt();
        final LinkedList[] lista = new LinkedList[1];
        lista[0] = new LinkedList<Node>();
        messContr.setBluetoothListener(new BluetoothListener() {
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
        messContr.startScan();
        try {
            Thread.sleep(ATTESA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        messContr.cancelScan();
        return lista[0];
    }

    public static String riceviStringa() {
        final String[] s = new String[1];
        final boolean[] isNull = new boolean[1];//nel caso sia true interrompe ciclo bloccante
        messContr.setBluetoothListener(new BluetoothListener() {
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
        messContr.startAsServer();
        while (s[0] == null && isNull[0] == false) {
        }
        return s[0];
    }

    public static void mandaStringa(final String s, final String addr) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Sender send = new Sender(addr, s, messContr);
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
    }

    public static void inviaGreeting(final NeighborGreeting greet, final Node vicino) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Sender send = new Sender(vicino.getMACAddress(), greet, messContr);
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
    }

    public static NeighborGreeting riceviGreeting() {

        //enableBt();
        Log.d("greeting server", "0000");
        final NeighborGreeting[] gr = new NeighborGreeting[1];
        final boolean[] isNull = new boolean[1];//nel caso sia true interrompe ciclo bloccante
        messContr.setBluetoothListener(new BluetoothListener() {

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
                Log.d("oggetto", data.toString());
                gr[0] = (NeighborGreeting) data;
                Log.d("greeting", gr[0].getMACOrigine());
                if (gr[0] == null) {
                    isNull[0] = true;
                }
            }
        });
        messContr.startAsServer();
        while (gr[0] == null && isNull[0] == false) {
            //Log.d("greeting server","0000");
        }

        return gr[0];
    }

    public static String getMACMioDispositivo() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    private static class Sender extends Thread {
        private String address;
        private Object obj;
        private BluetoothController controller;

        public Sender(String address, Object obj, BluetoothController controller) {
            this.controller = controller;
            this.address = address;
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                controller.disconnect();
                controller.connect(address);
                while (!(controller.getConnectionState() == com.prog.tlc.btexchange.lmbluetoothsdk.base.State.STATE_CONNECTED)) {
                }
                Log.d("stato connessione", String.valueOf(controller.getConnectionState()));
                controller.write(obj);
                while ((controller.getConnectionState() == com.prog.tlc.btexchange.lmbluetoothsdk.base.State.STATE_CONNECTED)) {
                }

                controller.startAsServer();
            } catch (NullPointerException e) {
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                controller.startAsServer();
            } catch (NullPointerException e) {
            }
        }
    }
}
