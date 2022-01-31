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
            new Point(700, 20),
            new Point(960, 350));

    static double PERCENT_COLOR_THRESHOLD = 0.3; //the percent of the bounding box the need to be black (0 = 0%, 1 = 100%)

    public static class VisionPipeline extends OpenCvPipeline {
        Mat mat = new Mat();
        private int location = 0;
        private double[] hsv_colors = new double[3];
        double leftValue;
        double rightValue;
        Scalar leftAvg;
        Scalar rightAvg;

        @Override
        public Mat processFrame(Mat input) {

            Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV); //moves from rgb to hsv (hue(color), saturation(intensity), value(brightness))

          /*  Scalar lowHSV = new Scalar(0, 0, 16);
            Scalar highHSV = new Scalar(60, 60, 22);
            Mat hsvMat = new Mat();

            Core.inRange(mat, lowHSV, highHSV, hsvMat); */
            if (mat.height() > 0 && mat.width() > 0) {
                Mat left = mat.submat(spot2);
                Mat right = mat.submat(spot3);
                leftAvg = Core.mean(left);
                rightAvg = Core.mean(right);

                if (leftAvg.val[2] >= 35 && leftAvg.val[2] <= 50) {
                    location = 2;
                } else if (rightAvg.val[2] >= 35 && rightAvg.val[2] <= 50) {
                    location = 3;
                } else {
                    location = 1;
                }
            }

                /*

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
            */
            hsv_colors = mat.get(200, 800);
            Imgproc.rectangle(mat, spot3, new Scalar(180, 255, 0));

            return mat;
        }

        public double getLeftValue() {
            return leftValue;
        }

        public double getRightValue() {
            return rightValue;
        }

        public double[] getPixelHsv() {
            return hsv_colors;
        }

        public Scalar getLeftScalar() {
            return leftAvg;
        }

        public Scalar getRightScalar() {
            return rightAvg;
        }

        public int getLocation() { return location; }
    }
}