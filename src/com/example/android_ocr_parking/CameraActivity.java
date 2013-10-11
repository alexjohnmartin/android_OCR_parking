package com.example.android_ocr_parking;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {
    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;
    private Camera camera;
    private int cameraId = 0;
    private static final int CAMERA_REQUEST = 1888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        imageView = (ImageView) findViewById(R.id.result);
        imageView.setImageResource(R.drawable.ic_launcher);

        // do we have a camera?
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG).show();
            DisableCameraButton();
        } else {
            cameraId = findRearFacingCamera();
            if (cameraId < 0) {
//                Toast.makeText(this, "No rear facing camera found.", Toast.LENGTH_LONG).show();
                DisableCameraButton();
            }
        }
    }

    private void DisableCameraButton() {
        Button scan_button = (Button)findViewById(R.id.scan_receipt_btn);
        scan_button.setEnabled(false);
    }

    public void onPickImageClick(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onScanImageClick(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        InputStream stream = null;

        if (resultCode != Activity.RESULT_OK || data == null) return;

        if (requestCode == CAMERA_REQUEST) {
            if (bitmap != null) { bitmap.recycle(); }
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        } else if (requestCode == REQUEST_CODE) {
            try {
                if (bitmap != null) { bitmap.recycle(); }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Toast.makeText(CameraActivity.this.getApplicationContext(), "FileNotFound: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private int findRearFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            cameraId = i;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                Toast.makeText(ImagePickActivity.this.getApplicationContext(), "Camera found", Toast.LENGTH_SHORT).show();
                break;
            }
        }
        return cameraId;
    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onPause();
    }
}
