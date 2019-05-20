package com.example.achmad.dolanansenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private CameraManager cameraManager;
    private String cameraId;
    private ImageButton imageButtonOnOff;
    private Boolean isTorchOn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButtonOnOff = findViewById(R.id.imageButton_on_off);
        isTorchOn = false;

        //untuk cek HP apakah sudah mempunyai fitur flash yg mendukung atau tidak
        Boolean isFlashAvaible = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!isFlashAvaible) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
            alert.setTitle("Error !!");
            alert.setMessage("Device anda tidak mendukung flashlight !!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
        }

        //code dibawah ini untuk mendapatkan akses flashlight
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                cameraId = cameraManager.getCameraIdList()[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //aksi dijalankan ketika klik
        imageButtonOnOff.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                try {
                    if (isTorchOn) {
                        turnOffFlashLight();
                        isTorchOn = false;
                    } else {
                        turnOnFlashLight();
                        isTorchOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //untuk menyalakan flashlight
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOnFlashLight() {
        try {
            cameraManager.setTorchMode(cameraId, true);
            imageButtonOnOff.setImageResource(R.drawable.btn_switch_on);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnOffFlashLight() {
        try {
            cameraManager.setTorchMode(cameraId, false);
            imageButtonOnOff.setImageResource(R.drawable.btn_switch_off);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
