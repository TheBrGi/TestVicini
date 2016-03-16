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
class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private Object obj;

    public AcceptThread(BluetoothAdapter mBluetoothAdapter, Object obj) {
        this.obj = obj;
        // Use a temporary object that is later assigned to mmServerSocket,
        // because mmServerSocket is final
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(mBluetoothAdapter.getName(), MainActivity.myUUID);
        } catch (IOException e) {
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (true) {
            try {
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
