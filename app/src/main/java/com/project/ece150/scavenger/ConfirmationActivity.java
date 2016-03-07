package com.project.ece150.scavenger;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.DMatch;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.opencv.core.Core.flip;
import static org.opencv.core.Core.transpose;
import static org.opencv.imgproc.Imgproc.line;
import static org.opencv.imgproc.Imgproc.resize;

public class ConfirmationActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;

    private Bitmap mOriginalImageBmp;
    private Mat mOriginalImageMat;
    private Mat mCurrentFrame;

    FeatureDetector mDetector;
    DescriptorExtractor mDescriptor;
    DescriptorMatcher mMatcher;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();

                    // Convert image.
                    mOriginalImageMat = new Mat();
                    Utils.bitmapToMat(mOriginalImageBmp, mOriginalImageMat);

                    // Init Matching objects
                    mDetector = FeatureDetector.create(FeatureDetector.ORB);
                    mDescriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
                    mMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public ConfirmationActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_confirmation);

        // Decode Bitmap from Intent
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            mOriginalImageBmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Display original image
        ImageView imageView = (ImageView) findViewById(R.id.OriginalImageView);
        Bitmap originalImageResized = resizeBmp(mOriginalImageBmp, 300, 300);
        imageView.setImageBitmap(originalImageResized);

        // Start image capture
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        // Set Button Callback
        Button button = (Button) findViewById(R.id.buttonSubmit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchImages(mOriginalImageMat, mCurrentFrame);
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat m1 = inputFrame.rgba();
        mCurrentFrame = m1;

        /*
        // Resize Original Image.
        if(mOriginalImageResized == null) {
            mOriginalImageResized = new Mat();
            //resize(mOriginalImageMat, mOriginalImageResized, new Size(m1.cols(), m1.rows()));
        }

        int thickness = 2;
        int lineType = 8;
        line( mOriginalImageResized,
                new Point(0, 0),
                new Point(400, 400),
                new Scalar( 200, 200, 0 ),
                thickness,
                lineType,
                0);
        */

        return m1;
    }

    // taken from http://stackoverflow.com/questions/15440647/scaled-bitmap-maintaining-aspect-ratio
    private Bitmap resizeBmp(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private void matchImages(Mat originalImage, Mat currentFrame) {
        Toast.makeText(ConfirmationActivity.this, "Matching Images...", Toast.LENGTH_SHORT).show();

        // (1) Resize images
        int target_rows;
        if(originalImage.rows() > currentFrame.rows()) {
            target_rows = currentFrame.rows();
        } else {
            target_rows = originalImage.rows();
        }

        int target_cols;
        if(originalImage.cols() > currentFrame.cols()) {
            target_cols = currentFrame.cols();
        } else {
            target_cols = originalImage.cols();
        }

        Mat originalImage_resized = new Mat();
        resize(originalImage, originalImage_resized, new Size(target_rows, target_cols));

        Mat currentFrame_resized = new Mat();
        resize(currentFrame, currentFrame_resized, new Size(target_rows, target_cols));

        // (2) Compute ORB Features


        // img1
        Mat img1 = originalImage_resized;
        Mat descriptors1 = new Mat();
        MatOfKeyPoint keypoints1 = new MatOfKeyPoint();

        mDetector.detect(img1, keypoints1);
        mDescriptor.compute(img1, keypoints1, descriptors1);

        // img2
        Mat img2 = currentFrame_resized;
        Mat descriptors2 = new Mat();
        MatOfKeyPoint keypoints2 = new MatOfKeyPoint();

        mDetector.detect(img2, keypoints2);
        mDescriptor.compute(img2, keypoints2, descriptors2);

        // (3) Match Features
        MatOfDMatch matches = new MatOfDMatch();
        mMatcher.match(descriptors1, descriptors2, matches);

        // (4) Compute Score
        List<DMatch> matchesList = matches.toList();

        // Sort by distance
        Collections.sort(matchesList, new Comparator<DMatch>() {
            @Override
            public int compare(DMatch lhs, DMatch rhs) {
                return Float.compare(lhs.distance, rhs.distance);
            }
        });

        // Sum of first n elements
        float sum = 0;
        int iteration = 0;
        for(DMatch match : matchesList) {
            if(iteration > 10)
                break;

            sum += match.distance;
            iteration++;
        }
        sum = sum / 10;

        // (5) Evaluate
        if(sum < 30.0) {
            finish();
        } else {
            Toast.makeText(ConfirmationActivity.this, "Score: " + sum + " Please try again!", Toast.LENGTH_SHORT).show();
        }

    }
}
