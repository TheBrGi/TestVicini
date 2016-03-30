package com.prog.tlc.btexchange;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prog.tlc.btexchange.gestioneDispositivo.Dispositivo;
import com.prog.tlc.btexchange.gestioneDispositivo.Node;
import com.prog.tlc.btexchange.gestione_bluetooth.AcceptThread;
import com.prog.tlc.btexchange.gestione_bluetooth.BtUtil;
import com.prog.tlc.btexchange.gestione_bluetooth.ConnectThread;
import com.prog.tlc.btexchange.lmbluetoothsdk.BluetoothController;
import com.prog.tlc.btexchange.lmbluetoothsdk.base.BluetoothListener;
import com.prog.tlc.btexchange.lmbluetoothsdk.base.State;
import com.prog.tlc.btexchange.protocollo.AODV;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private BluetoothController bc;
    private ListView lv;
    private ArrayAdapter<String> adapter = null;

    private AODV protocollo;
    private Dispositivo mioDispositivo;
    private final long TEMPO_ATTESA_VICINI = 7500;


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BtUtil.getBluetoothController().build(this);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        BtUtil.setContext(this.getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BtUtil.getBluetoothController().build(this);
        bc = new BluetoothController();
        bc.build(this);
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
                adapter.add(device.getName() + "\n" + device.getAddress());
            }

            @Override
            public void onReadData(final BluetoothDevice device, final Object data) {
                // Callback when remote device send data to current device.
            }
        });
        lv = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view;
                String s = text.getText().toString();
                String[] split = s.split("\n");
                Toast t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
                t.show();
                String obj = "Ciao da " + BtUtil.getBtAdapter().getName();
                BtUtil.mandaStringa(obj, split[1]);

            }
        });
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    final String data = BtUtil.riceviStringa();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast t = Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT);
                            t.show();
                        }
                    });
                }
            }
        };
        new Thread(r).start();


    }

    private void stampaNodiAVideo() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    adapter.clear(); //TODO controllare che stampi i nodi giusti
                    LinkedList<Node> tuttiNodi = new LinkedList<>(mioDispositivo.getListaNodi());
                    tuttiNodi.removeFirst();
                    adapter.addAll((Collection) tuttiNodi);
                    tuttiNodi = null;
                    try {
                        wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(r).start();
    }

    private void enableBt() {
        //Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        //startActivityForResult(turnOn, BLUETOOTH_ON);
        /*Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(discoverableIntent);*/
        bc.openBluetooth();
        bc.setDiscoverable(0);
        while (!bc.isEnabled()) {
        }
    }

    public void scan(View v) {
        if (!bc.isEnabled()) {
            enableBt();
        }
        load();
    }

    void load() {
        /*if (mReceiver.isOrderedBroadcast()) unregisterReceiver(mReceiver);
        btAdapter.cancelDiscovery();
        adapter.clear();
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
        btAdapter.startDiscovery();*/
        adapter.clear();
        bc.cancelScan();
        bc.startScan();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ON && resultCode == RESULT_OK) {
            load();
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
