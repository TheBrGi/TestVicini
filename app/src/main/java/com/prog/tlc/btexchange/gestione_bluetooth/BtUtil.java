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
    public static final UUID myUUID = UUID.fromString("d7a628a4-e911-11e5-9ce9-5e5517507c66");
    private final static long ATTESA = 10000;
    public static final String GREETING = "greeting";
    private static Context context;
    private static BluetoothController bc = new BluetoothController();

    private BtUtil() {
    }

    public static BluetoothController getBluetoothController() {
        bc.build(getContext());
        return bc;
    }

    public static void setContext(Context c) {
        context = c;
    }

    public static Context getContext() {
        return context;
    }

    public static void enableBt() {
        if (!bc.isEnabled()) {
            bc.openBluetooth();
            //bc.setDiscoverable(0);
            while (!bc.isEnabled()) {
            }

        }
        /*Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(discoverableIntent);
        BluetoothAdapter btAdapter=getBtAdapter();
        while (!btAdapter.isEnabled()) {
        }*/
    }

    public static BluetoothAdapter getBtAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static LinkedList<Node> cercaVicini() {
        final LinkedList<Node> lista = new LinkedList<>();
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    lista.add(new Node(device.getName(), device.getAddress()));
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        Context context = getContext();
        context.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        getBtAdapter().startDiscovery();
        try {
            Thread.sleep(ATTESA);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.unregisterReceiver(mReceiver);
        return lista;
    }

    public static String riceviStringa() {
        BluetoothController bc = new BluetoothController();
        bc.build(getContext());

        enableBt();

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
    }

    public static void mandaStringa(final String s, final String addr) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Sender send = new Sender(addr, s);
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

    public static void inviaGreeting(NeighborGreeting greet, Node vicino) {
        BluetoothAdapter btAdapter = getBtAdapter();
        BluetoothDevice btDevice = btAdapter.getRemoteDevice(vicino.getMACAddress());
        ConnectThread connect = new ConnectThread(btAdapter, btDevice, greet);
        connect.start();
    }

    public static NeighborGreeting riceviGreeting() {
        BluetoothAdapter btAdapter = getBtAdapter();
        Object obj = null;
        NeighborGreeting ng = null;
        while (true) {
            AcceptThread accept = new AcceptThread(btAdapter, GREETING);
            accept.start();
            obj = accept.getAnswer();
            if (obj instanceof NeighborGreeting) {
                ng = (NeighborGreeting) obj;
                break;
            }
        }
        return ng;
    }

    public static String getMACMioDispositivo() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    private static class Sender extends Thread {
        private String address;
        private Object obj;
        private BluetoothController bc = new BluetoothController();

        public Sender(String address, Object obj) {
            this.address = address;
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                bc.build(getContext());
                enableBt();
                bc.connect(address);
                while (!(bc.getConnectionState() == com.prog.tlc.btexchange.lmbluetoothsdk.base.State.STATE_CONNECTED)) {
                }
                Log.d("stato connessione", String.valueOf(bc.getConnectionState()));
                bc.write(obj);
                while ((bc.getConnectionState() == com.prog.tlc.btexchange.lmbluetoothsdk.base.State.STATE_CONNECTED)) {
                }
                bc.disconnect();
                bc.release();
                bc=null;
            } catch (NullPointerException e) {
            }
        }

        @Override
        public void interrupt() {
            super.interrupt();
            try {
                bc.disconnect();
                bc.release();
                bc=null;
            } catch (NullPointerException e) {
            }
        }
    }
}
