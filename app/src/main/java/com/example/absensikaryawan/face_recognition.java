package com.example.absensikaryawan;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.schema.WhileOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class face_recognition extends AppCompatActivity {
    CascadeClassifier cascadeClassifier;
    Interpreter interpreter;
    int INPUT_SIZE;
    int height = 0;
    int width = 0;
    GpuDelegate gpuDelegate= null;


    face_recognition(AssetManager assetManager, Context context, String modelpath, int input_size) throws IOException{
        INPUT_SIZE = input_size;
        Interpreter.Options options = new Interpreter.Options();
        gpuDelegate= new GpuDelegate();
        //options.addDelegate(gpuDelegate);
        options.setNumThreads(4);
        interpreter = new Interpreter(loadmodel(assetManager, modelpath),options);
        Log.d("face Recognition","Model is Loaded");
        //define classifier

        try {
            InputStream inputstream = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir,"haarcascade_frontalface_alt.xml");
            FileOutputStream outputStream = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int byteRead;
            while ((byteRead=inputstream.read(buffer)) != -1){
                outputStream.write(buffer,0,byteRead);
            }
            inputstream.close();
            outputStream.close();
            cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            cascadeClassifier.load(mCascadeFile.getAbsolutePath());
            if (cascadeClassifier.empty()) {
                Log.e("Face Recognition", "Failed to load cascade classifier");
                cascadeClassifier = null;
            } else
                Log.i("Face Recognition", "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());


        }
        catch (IOException e){
            e.printStackTrace();
        }

        //
        //
//        Core.flip(mRGBA.t(), mRGBAT, 1);
//        Imgproc.resize(mRGBAT, mRGBAT, mRGBAT.size());
//        return mRGBAT;

//        //Detect Face
//
//        //tambah text
//        String text = "Wajah Terdeteksi, tunggu 3 detik";
//        Point position = new Point(100, 100);
//        Scalar color = new Scalar(0, 255, 0);
//        int scale = 1;
//        int thickness = 3;
//        for (int i=0; i<faceArray.length;i++) {
//            Imgproc.rectangle(mRGBAT, faceArray[i].tl(),
//                    faceArray[i].br(),
//                    new Scalar(0, 255, 0), 2);
//            Imgproc.putText(mRGBAT, text, position, 2, scale, color, thickness);
//
//            Rect roi = new Rect((int) faceArray[i].tl().x, (int) faceArray[i].br().y,
//                    ((int) faceArray[i].br().x) - ((int) faceArray[i].tl().x),
//                    ((int) faceArray[i].br().y) - ((int) faceArray[i].tl().y));
//            Mat cropped_rgb = new Mat(mRGBAT, roi);
//            Bitmap bitmap = null;
//            bitmap = Bitmap.createBitmap(cropped_rgb.cols(), cropped_rgb.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(cropped_rgb, bitmap);
//            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
//            ByteBuffer byteBuffer = convertedBitmaptoBuffer(scaledBitmap);
//            //now create output
//            float[][] face_value = new float[1][1];
//            interpreter.run(byteBuffer, face_value);
//        }
    }

    private ByteBuffer convertedBitmaptoBuffer(Bitmap scaledBitmap) {
        ByteBuffer byteBuffer;
        int input_size = 96;
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

        Imgproc.resize(mat_image, mat_image, mat_image.size());
        height=grayscaleImage.height();
        width=grayscaleImage.width();
        int absoluteFaceSize=(int) (height*0.1);
        MatOfRect faces = new MatOfRect();
        if(cascadeClassifier != null){
            cascadeClassifier.detectMultiScale(grayscaleImage,faces,1.1,2,2,
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
            Imgproc.putText(mat_image, text, position, 2, scale, color, thickness);

            Rect roi = new Rect((int) faceArray[i].tl().x, (int) faceArray[i].br().y,
                    ((int) faceArray[i].br().x) - ((int) faceArray[i].tl().x),
                    ((int) faceArray[i].br().y) - ((int) faceArray[i].tl().y));
            Mat cropped_rgb = new Mat(mat_image, roi);
            Bitmap bitmap = null;
            bitmap = Bitmap.createBitmap(cropped_rgb.cols(), cropped_rgb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped_rgb, bitmap);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
            ByteBuffer byteBuffer = convertedBitmaptoBuffer(scaledBitmap);
            //now create output
            float[][] face_value = new float[1][1];
            interpreter.run(byteBuffer, face_value);
            Log.d("face_recognition","Out :"+ Array.get(Array.get(face_value,0),0));
            float read_face = (float) Array.get(Array.get(faceArray,0),0);
            //now we will read face value
            String face_name=get_face_name(read_face);
            Imgproc.putText(mat_image, ""+face_name, position, 2, scale, color, thickness);

        }

        return mat_image;
    }

    private String get_face_name(float read_face) {
        String val="";
        if (read_face>=0 & read_face<0.5){
            val = "Keanu";
        }
        else if(read_face>=0 & read_face<0.5){
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
}
