package com.prog.tlc.btexchange.gestione_conn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by BrGi on 22/04/2016.
 */
public class ServerAsyncTask extends AsyncTask {

    private Context context;

    public ServerAsyncTask(Context context) {
        this.context = context;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null) {
            String result = (String) o;
            Toast t = Toast.makeText(context, result, Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
            ServerSocket serverSocket = new ServerSocket(8000);
            Socket client = serverSocket.accept();

            InputStream inputstream = client.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(inputstream);
            String s = (String) ois.readObject();
            ois.close();
            serverSocket.close();
            return s;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


}

//class
