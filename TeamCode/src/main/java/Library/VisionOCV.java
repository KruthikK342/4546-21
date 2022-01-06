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
import org.openftc.easyopencv.OpenCvPipeline;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;


public class VisionOCV extends OpenCvPipeline{
    Telemetry telemetry;

    Mat mat = new Mat();
    public enum Location {
        LOC1,
        LOC2,
        LOC3
    }
    private Location location;

    public VisionOCV(Telemetry t) { telemetry = t; }

    static final Rect spot3 = new Rect(
            new Point(650, 50),
            new Point(1000, 350));
    static final Rect spot2 = new Rect(
            new Point(100, 50),
            new Point(420, 350));
    static double PERCENT_COLOR_THRESHOLD = 0.4; //the percent of the bounding box the need to be black

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV); //moves from rgb to hsv (hue(color), saturation(intensity), value(brightness))
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(23, 50, 70);
        Scalar highHSV = new Scalar(32, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat left = mat.submat(spot2);
        Mat right = mat.submat(spot3);

        double leftValue = Core.sumElems(left).val[0] / spot2.area() / 255; //finds amount of area that is black
        double rightValue = Core.sumElems(right).val[0] / spot3.area() / 255;
        left.release();
        right.release();

        boolean elementLeft = leftValue > PERCENT_COLOR_THRESHOLD;
        boolean elementRight = rightValue > PERCENT_COLOR_THRESHOLD;
        int elementLoc = Integer.parseInt(null); //will determine location

        if (!elementLeft && !elementRight) {
            location = Location.LOC1;
            telemetry.addData("Element Location", "location 1");
        }
        else if (elementLeft) {
            location = Location.LOC2;
            telemetry.addData("Skystone Location", "location 2");
        }
        else {
            location = Location.LOC3;
            telemetry.addData("Skystone Location", "location 3");
        }
        telemetry.update();


        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB); //this portion of code displays image
        Scalar background = new Scalar(255, 0, 0);
        Scalar element = new Scalar(0, 255, 0);

        Imgproc.rectangle(mat, spot2, location == Location.LOC2? element:background); //draws the rect
        Imgproc.rectangle(mat, spot3, location == Location.LOC3? element:background);

        return mat;
    }
    public Location getLocation() {
        return location;
    }
}