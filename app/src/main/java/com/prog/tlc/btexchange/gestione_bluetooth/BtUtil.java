package com.prog.tlc.btexchange.gestione_bluetooth;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.protocollo.NeighborGreeting;

import java.util.LinkedList;

/**
 * Created by BrGi on 16/03/2016.
 */
public class BtUtil {
    private final static long ATTESA = 10000;

    private BtUtil() {
    }

    public static Context getContext() {
        Application app = new Application();
        return app.getApplicationContext();
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

    public static void inviaGreeting(NeighborGreeting greet, Node vicino) {
        BluetoothAdapter btAdapter=getBtAdapter();
        BluetoothDevice btDevice=btAdapter.getRemoteDevice(vicino.getMACAddress());
        ConnectThread connect=new ConnectThread(btAdapter,btDevice,greet);
        connect.start();
    }

    public static NeighborGreeting riceviGreeting(){
        BluetoothAdapter btAdapter=getBtAdapter();
        Object obj=null;
        AcceptThread accept=new AcceptThread(btAdapter,obj);
        accept.start();
        try {
            accept.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NeighborGreeting ng=null;
        if(obj instanceof NeighborGreeting)
            ng=(NeighborGreeting)obj;
        return ng;
    }
}
