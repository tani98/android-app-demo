package com.chocoyo.labs.app.demo.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chocoyo.labs.app.demo.DatabaseUtil;
import com.chocoyo.labs.app.demo.MyWelcomeActivity;
import com.chocoyo.labs.app.demo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stephentuso.welcome.WelcomeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 300;


    private FirebaseDatabase mDatabase;

    @BindView(R.id.open) Button buttonOpen;

    WelcomeHelper welcomeScreen;

    private Button create;
    private EditText cardNumber;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // welcome message
        welcomeScreen = new WelcomeHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);

        // database
        // Write a message to the database
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = DatabaseUtil.getDatabase();
        myRef = mDatabase.getReference("message");

        //myRef.setValue("Hello, World!");

        // initialize
        initCreateNumber();

        // set button text
        buttonOpen.setText(getString(R.string.app_name));

        validatePermission();

    }

    private void actionDoneExample() {
        Log.i(TAG, "Permiso consedido");
    }

    private void validatePermission() {

        int writeExternalStorage = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (writeExternalStorage == PackageManager.PERMISSION_GRANTED) {
            actionDoneExample();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @OnClick(R.id.open)
    public void open(View view) {
        openJuan();
    }
    private void openJuan() {
        Intent intent = new Intent(
                getApplicationContext(),
                JuanActivity.class);
        startActivity(intent);
    }

    private void initCreateNumber() {
        create = (Button) findViewById(R.id.create);
        cardNumber = (EditText) findViewById(R.id.card_number);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (cardNumber.getText().toString().isEmpty()) {
                   Toast.makeText(getApplicationContext(),
                           "Loco, no has puesto tu numero",
                           Toast.LENGTH_LONG).show();
               } else {
                   myRef.setValue(cardNumber.getText().toString());
               }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    actionDoneExample();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        welcomeScreen.onSaveInstanceState(outState);
    }
}
