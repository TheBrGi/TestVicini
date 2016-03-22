package com.prog.tlc.btexchange.gestione_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.provider.Settings;

import com.prog.tlc.btexchange.MainActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.UUID;

/**
 * Created by BrGi on 13/03/2016.
 */
public class AcceptThread extends Thread {
    private BluetoothServerSocket mmServerSocket;
    private Object obj;
    private String serviceName;
    private BluetoothAdapter mBluetoothAdapter;

    public AcceptThread(BluetoothAdapter mBluetoothAdapter, String serviceName) {
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        this.mBluetoothAdapter=mBluetoothAdapter;
        this.serviceName=serviceName;
        avviaServer();
    }
private void avviaServer(){
    BluetoothServerSocket tmp = null;
    try {
        // MY_UUID is the app's UUID string, also used by the client code
        tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(serviceName, BtUtil.myUUID);
    } catch (IOException e) {
    }
    mmServerSocket = tmp;
}
    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
                //if (!BtUtil.getBtAdapter().isEnabled()){
                //    BtUtil.enableBt();
                //    avviaServer();
                //}

                socket = mmServerSocket.accept();
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    manageConnectedSocket(socket);
                    mmServerSocket.close();
                    break;
                }
            } catch (IOException e) {
                break;
            } catch (NullPointerException ex){
                BtUtil.enableBt();
                avviaServer();
                continue;
            }

        }
    }

    public void manageConnectedSocket(BluetoothSocket s) {
        try {
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            obj = ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Object getAnswer() {
        try {
            this.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (obj == null)
            return "Nessuna risposta";
        return obj;
    }

    /**
     * Will cancel the listening socket, and cause the thread to finish
     */
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
        }
    }
}
