package com.pcl.ocr.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pcl.ocr.R;
import com.pcl.ocr.scanner.ScannerOptions;
import com.pcl.ocr.scanner.ScannerView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

public class LPRActivity extends AppCompatActivity {

    private ScannerView scannerView;

    public static final int REQUEST_LPR_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpr);
        scannerView = findViewById(R.id.scanner_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView = null;
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        Log.w("Max","startCamera");

        ScannerOptions builder = new ScannerOptions.Builder()
                .setTipText("请将识别车牌放入框内")
                .setFrameCornerColor(0xFF26CEFF)
                .setLaserLineColor(0xFF26CEFF)
                .build();

        scannerView.setScannerOptions(builder);
        scannerView.setOnScannerOCRListener(cardNum -> {
            Log.d("OCRListener", cardNum);
            Log.d("OCRListener", Thread.currentThread().getName());
            new AlertDialog.Builder(LPRActivity.this)
                    .setMessage(cardNum)
                    .setNegativeButton("重新识别", (dialogInterface, i) -> {
                        scannerView.start();
                    })
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        finishValue(cardNum);
                    })
                    .show();
        });
    }

    private void finishValue(String card) {
        Intent intent = new Intent();
        intent.putExtra("card", card);
        setResult(RESULT_OK, intent);
        finish();
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @SuppressLint("StaticFieldLeak")
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                System.loadLibrary("lpr");
            } else {
                super.onManagerConnected(status);
            }
        }
    };
}