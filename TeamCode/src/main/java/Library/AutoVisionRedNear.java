package Library;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class AutoVisionRedNear { //camera resolution(x, y):
    //the following comments are the bounding boxes that the different autos use
    static final Rect spot1 = new Rect(
            new Point(1, 25),
            new Point(285, 325));
    static final Rect spot2 = new Rect(
            new Point(650, 25),
            new Point(840, 350));

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

            if (mat.height() > 0 && mat.width() > 0) {
                Mat left = mat.submat(spot1);
                Mat right = mat.submat(spot2);
                leftAvg = Core.mean(left);
                rightAvg = Core.mean(right);

                if (meetsReqirements(leftAvg)) {
                    location = 2;
                } else if (meetsReqirements(rightAvg)) {
                    location = 3;
                } else {
                    location = 1;
                }
            }

            hsv_colors = mat.get(200, 800);
            Imgproc.rectangle(mat, spot2, new Scalar(238, 240, 125));
            Imgproc.rectangle(mat, spot1, new Scalar(238, 240, 125));

            return mat;
        }

        public boolean meetsReqirements(Scalar position) {
            return position.val[2] >= 10 && position.val[2] <= 50; //these are the accepted brightness values
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

        //these are a lot better for telemetry vals, but gives some error if used
        public double getLeftDouble() { return leftAvg.val[2]; }

        public double getRightDouble() {
            return rightAvg.val[2];
        }

        public int getLocation() { return location; }
    }
}