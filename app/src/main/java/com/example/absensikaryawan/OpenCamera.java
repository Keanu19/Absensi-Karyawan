package com.example.absensikaryawan;

import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;


public class OpenCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRGBA, mRGREY, mRGBAT;
    File cascfile;
    CascadeClassifier faceDetector;

    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(OpenCamera.this) {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                {
                    javaCameraView.enableView();

                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                    File cascadeDir =  getDir("cascade",MODE_PRIVATE);
                    cascfile = new File(cascadeDir, "haarcascade_frontalface_default.xml");

                    try {
                        FileOutputStream fos = new FileOutputStream(cascfile);
                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = is.read(buffer)) !=-1)
                        {
                            fos.write(buffer, 0 ,bytesRead);
                        }
                        is.close();
                        fos.close();

                        faceDetector = new CascadeClassifier(cascfile.getAbsolutePath());

                        if(faceDetector.empty()){
                            faceDetector = null;
                        }
                        else cascadeDir.delete();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    javaCameraView.enableView();

                }
                break;
                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
            super.onManagerConnected(status);
        }
    };


//    static {
//        if(OpenCVLoader.initDebug()){
//            Log.d(TAG,"Open CV Sucessfully Running");
//        }
//        else{
//            Log.d(TAG,"OpenCV not working");
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_camera);

        javaCameraView = (JavaCameraView) findViewById(R.id.my_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(OpenCamera.this);
        //set kamera depan
        javaCameraView.setCameraIndex(1);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRGBA = new Mat(height,width, CvType.CV_8UC4);
        mRGREY = new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRGBA.release();
        mRGREY.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRGBA = inputFrame.rgba();
        mRGREY = inputFrame.gray();
        mRGBAT = mRGBA.t();
//        Core.flip(mRGBA.t(), mRGBAT, 1);
//        Imgproc.resize(mRGBAT, mRGBAT, mRGBAT.size());
//        return mRGBAT;
        Core.flip(mRGBA.t(), mRGBAT, -1);
        Imgproc.resize(mRGBAT, mRGBAT, mRGBAT.size());
        //Detect Face
        MatOfRect facedetections = new MatOfRect();
        faceDetector.detectMultiScale(mRGBAT,facedetections);

        //tambah text
        String text = "Wajah Terdeteksi, tunggu 3 detik";
        Point position = new Point(100, 100);
        Scalar color = new Scalar(0, 255, 0);
        int scale = 1;
        int thickness = 3;

        for (Rect rect: facedetections.toArray()){
            Imgproc.rectangle(mRGBAT, new Point(rect.x, rect.y),
                    new Point(rect.x+rect.width, rect.y+ rect.height),
                    new Scalar(0,255,0));
            Imgproc.putText(mRGBAT,text,position,2,scale,color,thickness);


        }

        return mRGBAT;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"Open CV Sucessfully Running");
            baseLoaderCallback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
        else{
            Log.d(TAG,"OpenCV not working");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, baseLoaderCallback);
        }
    }

}
