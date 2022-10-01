package com.example.tabletapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class MyOrder_page2 extends AppCompatActivity {
    Button printMyOrder, cancelMy;
    TextView orderIdDisplay;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    ImageView ivOutput;
    DatabaseReference reference;
    sessionManagement sessionManagement;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_page2);

        /// FULL SCREEN NO NAVIGATION AND SCROLL NAVIGATION
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        loadingDialog = new LoadingDialog( this);
        //String username = getIntent().getStringExtra("username_key");
        printMyOrder =findViewById(R.id.printMyOrder);
        cancelMy = findViewById(R.id.cancelMy);
        orderIdDisplay = findViewById(R.id.orderIdDisplay);
        ivOutput = findViewById(R.id.iv_output);

        sessionManagement = new sessionManagement(getApplicationContext());
        String orderID = sessionManagement.getOrderID();
        String randomnumberr = sessionManagement.getRandomnumberr();
        /*printer*/

        //Display QR =====--------------------
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(orderID, BarcodeFormat.QR_CODE, 350 , 350);

            BarcodeEncoder encoder = new BarcodeEncoder();

            Bitmap bitmap = encoder.createBitmap(matrix);

            ivOutput.setImageBitmap(bitmap);

            InputMethodManager manager = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE
            );

            manager.hideSoftInputFromWindow(orderIdDisplay.getApplicationWindowToken(), 0);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        String summary =
                "Queue Number: " +randomnumberr + "\n\n" +
                sessionManagement.getSummary()+ "\n\n"+
                "Total Amount:  Php " + sessionManagement.getTotal() + "\n" +
                 "THANKYOU FOR ORDERING!";

        orderIdDisplay.setText(summary);




        cancelMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyOrder_Page.class);
                startActivity(intent);
                finish();
            }
        });

        printMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();

                //set number
                String randomnumberr = sessionManagement.getRandomnumberr();
                if(randomnumberr.equals("999")){
                    sessionManagement.setRandomnumberr("1");
                }
                //Get Session name
                String orderid=sessionManagement.getOrderID();

                HashMap arca= new HashMap();
                arca.put("status", "preset");
                arca.put("userId", "tablet01");
                arca.put("queue_number",randomnumberr);
                arca.put("total", sessionManagement.getTotal());
                arca.put("summary",sessionManagement.getSummary());

                sessionManagement.setOrdering(false);
                sessionManagement.setOrderID("");
                sessionManagement.setTotalString("Php 0.00");
                int bruja = Integer.valueOf(randomnumberr);
                bruja++;
                sessionManagement.setRandomnumberr(String.valueOf(bruja));

                reference = FirebaseDatabase.getInstance().getReference("preset_tablet");
                reference.child(orderid).child("info").updateChildren(arca).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        /* THERMAL PRINTER FUNCTIONS*/

                        Bitmap qrBit = printQRCode(orderID);
                        try {
                            findBT();
                            openBT();
                            sendData(qrBit);

                            closeBT();

                        } catch (IOException ex) {
                            ex.printStackTrace();
               /*Toast.makeText(getApplicationContext(), "nandito", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(MyOrder_page2.this, MyOrder_page3.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               loadingDialog.dismissDialog();
               startActivity(intent);

               finish();*/
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                next();
                            }
                        }, 500);
                    }
                });





            }
        });

    }
    //=============================== Functions ================

    public void next(){
        //Toast.makeText(getApplicationContext(), "pepa", Toast.LENGTH_SHORT).show();
        loadingDialog.dismissDialog();
        Intent intent = new Intent(this, MyOrder_page3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private Bitmap printQRCode (String orderID) {
    MultiFormatWriter writer = new MultiFormatWriter();
    try {
        BitMatrix matrix = writer.encode(orderID, BarcodeFormat.QR_CODE, 300, 300);
        BarcodeEncoder encoder = new BarcodeEncoder();

        Bitmap bitmap = encoder.createBitmap(matrix);
        return bitmap;

    } catch (WriterException e) {
        e.printStackTrace();
    }
    return null;
}

/*THERMAL PRINTER METHODS*/
        void findBT() {

            try {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if(mBluetoothAdapter == null) {
                   /*No bluetooth adapter available**/
                }

                if(!mBluetoothAdapter.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, 0);
                }

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if(pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {

                        // PB-58 is the name of the bluetooth printer device
                        // we got this name from the list of paired devices
                        if (device.getName().equals("PB-58")) {
                            mmDevice = device;
                            break;
                        }
                    }
                }



            }catch(Exception e){
                e.printStackTrace();
            }
        }
    void openBT() throws IOException {
        try {
            // Standard SerialPortService ID//
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {

                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   void sendData(Bitmap qRBit) throws IOException {
        try {

            // the text typed by the user
            PrintPic printPic1 = PrintPic.getInstance();
            printPic1.init(qRBit);
            byte[] bitmapdata2 = printPic1.printDraw();
            mmOutputStream.write(bitmapdata2);
            String msg = orderIdDisplay.getText().toString();


            msg += "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";

            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}