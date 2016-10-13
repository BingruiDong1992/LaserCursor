package edu.ucsb.ece.ece150.pickture;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class MyLazerDrawer extends Activity implements CameraBridgeViewBase.CvCameraViewListener2
{

    Mat frame;
    List<Mat> mChannels;
    Mat frameH;
    Mat frameV;
    Mat frameS;
    Mat Original;
    Mat image;

    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            super.onManagerConnected(status);
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OK","OpenCV success");
                    mOpenCvCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                }
            }
        }
    };
    public JavaCameraView mOpenCvCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.MainActivityCameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    public void onDestory()
    {
        super.onDestroy();
        if (mOpenCvCameraView != null)
        {
            mOpenCvCameraView.disableView();
        }
    }


    protected void onPause()
    {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, this, mLoaderCallback);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        frame = new Mat();
        mChannels = new ArrayList<Mat>();
        frameH = new Mat();
        frameV = new Mat();
        frameS = new Mat();
        Original = new Mat();
        image = new Mat();

  /*      frame = new Mat();
        mChannels = new ArrayList<Mat>();
        frameH = new Mat();
        frameV = new Mat();
        frameS = new Mat();
        Original = new Mat();
        MatOfPoint2f image = new MatOfPoint2f();*/




    }

    @Override
    public void onCameraViewStopped() {
        frame.release();
//        mChannels.get(0).release();
//        mChannels.get(1).release();
//        mChannels.get(2).release();
//        frameV.release();
//        frameH.release();
    //    mChannels.clear();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        // Grab the video frame

        inputFrame.rgba().copyTo(frame);
       // inputFrame.rgba().copyTo(image);
      //  inputFrame.rgba().copyTo(Original);
        // Convert it to HSV
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV);


        // Split the frame into individual components (separate images for H, S,
        // and V)
        mChannels.clear();
        Core.split(frame, mChannels); // Split channels: 0-H, 1-S, 2-V
        frameH = mChannels.get(0);
      //  Original = mChannels.get(0);
     //   frameS = mChannels.get(1);
        frameV = mChannels.get(2);


        // Apply a threshold to each component
        Imgproc.threshold(frameH, frameH, 55, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(frameH, frameH, 65, 255, Imgproc.THRESH_BINARY_INV);
      //  Imgproc.threshold(frameS, frameS, 100, 255, Imgproc.THRESH_BINARY);
  /*       Imgproc.threshold(frameH, frameH, 10, 255, Imgproc.THRESH_BINARY);
         Imgproc.threshold(frameS, frameS, 10, 255, Imgproc.THRESH_BINARY_INV);
        Imgproc.threshold(frameV, frameV, 10, 255, Imgproc.THRESH_BINARY_INV);*/

        Imgproc.threshold(frameV, frameV, 230, 255, Imgproc.THRESH_BINARY);
      //  Imgproc.threshold(frameS, frameS, 100, 255, Imgproc.THRESH_BINARY);

        // Perform an AND operation
 //       Core.bitwise_and(Original, frameH, frame);
  //      Core.bitwise_and(frameS, frame, frame);
   //     Core.bitwise_and(frameV, frame, frame);
        Core.bitwise_and(frameH, frameV, frame);

        //Core.bitwise_and(frame, frameS, frame);
        //frame.copyTo(Original);


     //   Core.bitwise_and(frame, frameS, frame);
        Scalar nnn = new Scalar(255,255,255) ;


       List<MatOfPoint> contours =  new ArrayList<MatOfPoint>();
        Imgproc.findContours(frame, contours, frameS, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Imgproc.threshold(frame, frame, 0, 0, Imgproc.THRESH_BINARY);
        Imgproc.drawContours(frame, contours, 0, nnn);



        Moments thePoint = Imgproc.moments(frame,false);
        int x = (int) (thePoint.get_m10()/thePoint.get_m00());
        int y = (int) (thePoint.get_m01()/thePoint.get_m00());

         Core.circle(frame, new Point(x,y), 5, nnn);
 //       image = (MatOfPoint2f) (frame);
     /*   center = new Point();
        float radius[] = new float[5000];

       Imgproc.minEnclosingCircle(new MatOfPoint2f(image.toArray()), center, radius);
       */


        // Display the result.
     //   frameH.release();
        // frameS.release();
     //   frameV.release();
     //   frame.release();
        frameV.release();
        frameH.release();
       // frameS.release();


      //  image.release();
    //    image.release();





  //      frameS.release();
  //      Original.release();
  //      mChannels.get(0).release();
 //       mChannels.get(1).release();
 //       mChannels.get(2).release();
  //      mChannels.clear();

      //  return image;
     //   Runnable r = new Mythread(x,y);
    //    new Thread(r).start();

        if(x != 0 && y!=0)
        {SendPosition(x,y);}

        return frame;

    }

    public void SendPosition(int x, int y)
    {
        try{
            DatagramSocket socket = new DatagramSocket(1234);

            InetAddress serveraddress = InetAddress.getByName("169.231.139.80");

        //    String str = Integer.toString(x)+Integer.toString(y);
        //    byte[] data = str.getBytes();
            byte[] data = toBytes(x,y);

            DatagramPacket packet = new DatagramPacket(data,data.length,serveraddress,1234);
            socket.send(packet);

//                Toast.makeText(getApplicationContext(), "Send", Toast.LENGTH_SHORT).show();
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] toBytes(int i,int y)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i % 32);
        result[1] = (byte) (i / 32);
        result[2] = (byte) (y % 32);
        result[3] = (byte) (y / 32);
        return result;
    }

}

