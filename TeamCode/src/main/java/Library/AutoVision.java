package Library;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


public class AutoVision {

    static final Rect spot2 = new Rect(
            new Point(60, 65),
            new Point(300, 370));
    static final Rect spot3 = new Rect(
            new Point(650, 50),
            new Point(910, 400));

    static double PERCENT_COLOR_THRESHOLD = 0.3; //the percent of the bounding box the need to be black (0 = 0%, 1 = 100%)

    public static class VisionPipeline extends OpenCvPipeline {
        Mat mat = new Mat();
        private int location = 0;
        double leftValue;
        double rightValue;

        @Override
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV); //moves from rgb to hsv (hue(color), saturation(intensity), value(brightness))
            Scalar lowHSV = new Scalar(150, 0, 0);
            Scalar highHSV = new Scalar(170, 0, 45);
            Mat hsvMat = new Mat();

            Core.inRange(mat, lowHSV, highHSV, hsvMat);
            if (hsvMat.height() > 0 && hsvMat.width() > 0) {
                Mat left = hsvMat.submat(spot2);
                Mat right = hsvMat.submat(spot3);

                leftValue = ((double)Core.sumElems(left).val[0] / spot2.area()) / 255; //finds amount of area that is black
                rightValue = ((double)Core.sumElems(right).val[1] / spot3.area()) / 255;
                left.release();
                right.release();

                boolean elementLeft = leftValue > PERCENT_COLOR_THRESHOLD;
                boolean elementRight = rightValue > PERCENT_COLOR_THRESHOLD;
                if (!elementLeft && !elementRight) { //these if statements output the location to OpModes
                    location = 1;
                }
                else if (elementLeft) {
                    location = 2;
                }
                else {
                    location = 3;
                }
            }

            return mat;
        }

        public double getLeftValue() {
            return leftValue;
        }

        public double getRightValue() {
            return rightValue;
        }

        public int getLocation() { return location; }
    }
}