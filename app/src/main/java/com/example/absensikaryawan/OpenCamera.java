package com.example.absensikaryawan;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.FaceDetector;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.ximgproc.Ximgproc;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.internal.Util;


public class OpenCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static String TAG = "MainActivity";
    JavaCameraView javaCameraView;
    Mat mRGBA, mRGREY, mRGBAT;
    File cascfile;
    CascadeClassifier faceDetector;
    //face_recognition face_recognition;
    //face_recognition file
    Interpreter interpreter;
    int INPUT_SIZE = 224;
    int height = 0;
    int width = 0;
    GpuDelegate gpuDelegate= null;
    AssetManager assetManager;
    String modelpath = "model.tflite";


    BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(OpenCamera.this) {

        @Override
        public void onManagerConnected(int status)
        {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                {
                    //javaCameraView.enableView();
                    assetManager = getAssets();
                    Interpreter.Options options = new Interpreter.Options();
                    gpuDelegate = new GpuDelegate();
                    //options.addDelegate(gpuDelegate);
                    options.setNumThreads(4);
                    try {
                        interpreter = new Interpreter(loadmodel(assetManager, modelpath),options);
                        Log.d("face Recognition", "Model is Loaded");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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

                        Log.d("OpenCamera", "Classifier is Loaded");
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

//        try {
//            int input_size =96;
//            face_recognition(getAssets(),
//                    OpenCamera.this,
//                    "model.tflite",
//                    input_size);
//        }
//        catch (IOException e){
//            e.printStackTrace();
//            Log.d("OpenCamera", "Model is not Loaded");
//        }

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

        mRGBA=recognizeImage(mRGBA);

        return mRGBA;
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

    ////////
    ///////
    //////
    //////
















    ///////////////




    private ByteBuffer convertedBitmaptoBuffer(Bitmap scaledBitmap) {
        ByteBuffer byteBuffer;
        int input_size = INPUT_SIZE;
        byteBuffer = ByteBuffer.allocateDirect(4 * 1 * input_size * input_size * 3);
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[input_size * input_size];
        scaledBitmap.getPixels(intValues, 0, scaledBitmap.getWidth(), 0, 0, scaledBitmap.getWidth(),
                scaledBitmap.getHeight());
        int pixels = 0;
        for (int i = 0; i < input_size; ++i) {
            for (int j = 0; j < input_size; ++j) {
                final int val = intValues[pixels++];
                byteBuffer.putFloat((((val >> 16) & 0xFF)) / 255.0f);
                byteBuffer.putFloat((((val >> 8) & 0xFF)) / 255.0f);
                byteBuffer.putFloat(((val & 0xFF)) / 255.0f);
            }

        }
        return byteBuffer;
    }

    public Mat recognizeImage(Mat mat_image){
        Core.flip(mat_image.t(), mat_image, -1);
        Mat grayscaleImage = new Mat();

        Imgproc.cvtColor(mat_image,grayscaleImage,Imgproc.COLOR_RGBA2GRAY);
        //Imgproc.resize(mat_image, mat_image, mat_image.size());
        height=grayscaleImage.height();
        width=grayscaleImage.width();
        int absoluteFaceSize=(int) (height*0.1);
        MatOfRect faces = new MatOfRect();
        if(faceDetector != null){
            faceDetector.detectMultiScale(grayscaleImage,faces,1.1,2,2,
                    new Size(absoluteFaceSize, absoluteFaceSize),new Size());
            Log.d("Face Recognition", "Classifier is Loaded");
        }
        else {Log.d("Face Recognition", "Classifier is not Loaded");}

        Rect[] faceArray = faces.toArray();
        String text = "Wajah Terdeteksi, tunggu 3 detik";
        Point position = new Point(100, 100);
        Scalar color = new Scalar(0, 255, 0);
        int scale = 1;
        int thickness = 3;
        for (int i=0; i<faceArray.length;i++) {
            Imgproc.rectangle(mat_image, faceArray[i].tl(),
                    faceArray[i].br(),
                    new Scalar(0, 255, 0,255), 2);
            //imgproc.putText(mat_image, text, position, 2, scale, color, thickness);

            Rect roi = new Rect((int) faceArray[i].tl().x,(int)faceArray[i].tl().y,
                    ((int) faceArray[i].br().x) - ((int) faceArray[i].tl().x),
                    ((int) faceArray[i].br().y) - ((int) faceArray[i].tl().y));
            Mat cropped_rgb = new Mat(mat_image, roi);
            Bitmap bitmap = null;
            bitmap = Bitmap.createBitmap(cropped_rgb.cols(), cropped_rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped_rgb, bitmap);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
            ByteBuffer byteBuffer = convertedBitmaptoBuffer(scaledBitmap);
            //now create output
            //float[][] face_value = new float[1][2];
            float[][] face_value = new float[1][2];
            interpreter.run(byteBuffer, face_value);
            Log.d("face_recognition","Out :"+ Array.get(Array.get(face_value,0),0));
            Log.d("face_recognition","Out :"+ Array.get(Array.get(face_value,0),1));
            // read_face = (float) Array.get(face_value,0);
//            Log.d("face_recognition","Out :"+ Array.get(Array.get(face_value,0),0));
//            float read_face = (float) Array.get(Array.get(face_value,0),0);
            //now we will read face value
            String face_name=get_face_name(face_value);
            Imgproc.putText(mat_image, ""+face_name, position, 2, scale, color, thickness);

        }
        return mat_image;
    }

    private String get_face_name(float read_face[][]) {
        String val="";
        int max = findMaximumIndex(read_face);
        if ( max == 0){
            val = "Keanu";
        }
        else if(max == 1){
            val = "hilda";
        }
        return val;
    }


    //Load model
    private MappedByteBuffer loadmodel(AssetManager assetManager, String modelpath) throws IOException {
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(modelpath);
        FileInputStream inputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long declaredLength = assetFileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public int findMaximumIndex(float[ ][ ] a)
    {
        int largest = -1;
        for(int row = 0; row < a.length; row++)
        {
            for(int col = 0; col < a[row].length; col++)
            {
                if(a[row][col] > largest)
                {
                    largest = col;
                }
            }
        }
        return largest;
    }
}
