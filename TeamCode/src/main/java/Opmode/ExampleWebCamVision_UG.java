package Opmode;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Frame;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;


public class ExampleWebCamVision_UG {

    private LinearOpMode opMode;
    private VuforiaLocalizer vuforia;
    private Parameters parameters;
    private CameraDirection CAMERA_CHOICE = CameraDirection.BACK;

    private final int RED_THRESHOLD = 20;
    private final int GREEN_THRESHOLD = 20;
    private final int BLUE_THRESHOLD = 20;

    private final double widthFactor = 1.0;//3264;
    private final double heightFactor = 1.0;//1836;
    private final int xdiff = 650;
    private final int yheight = 300;



    private static final String VUFORIA_KEY = "AVFFxKT/////AAABmQTYeIgT6k6wv0phn1XaTKN+Z9RdP23vp3+6IEyv9haxqO0u2vStZKAjPLct97BEhaeSkeYivFGo2IDu8fWfJlBY+2JZ0FIf8M2N7yW5XExNYWbGNwwem7Wgzsl5ld4wr6xOeXqcwtVn1mgt5ELcypOfvRnnun3FWIBr7mx+AJRN1ZAnqVvfOphPVxNm9vpylN4d5nJu58aTxiXMCJadPhhyviOGVlI6tT//lTO5GJEBva9xN+SXpxsTnPEaegQNE+qzFxVzmtXabk+oAuMxDh1XR+6EbyZzZjQm3gI9DXkt7os7ZkM95GXEZN9MHRwPWdwbk1Bt/iGI3VcXp2VfUDhWYXaWJjvu/aZC2WqrhAef";

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> frame;

    public static String bitmapSkyStonePosition;

    public ExampleWebCamVision_UG(LinearOpMode opMode) { //5  idk why this five was here

        this.opMode = opMode;

        int cameraMonitorViewId = this.opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.opMode.hardwareMap.appContext.getPackageName());
        Parameters params = new Parameters(cameraMonitorViewId);

        params.vuforiaLicenseKey = VUFORIA_KEY;
        params.cameraDirection = CAMERA_CHOICE;
        vuforia = ClassFactory.getInstance().createVuforia(params);

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); //enables RGB565 format for the image
        vuforia.setFrameQueueCapacity(4); //tells VuforiaLocalizer to only store one frame at a time
        vuforia.enableConvertFrameToBitmap();

    }
/*
    public BitMapVision(LinearOpMode opMode) {
        this.opMode = opMode;
        int cameraMonitorViewId = this.opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.opMode.hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        params.vuforiaLicenseKey = VUFORIA_KEY;
        params.cameraDirection = CAMERA_CHOICE;
        vuforia = ClassFactory.getInstance().createVuforia(params);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true); //enables RGB565 format for the image
        vuforia.setFrameQueueCapacity(4); //tells VuforiaLocalizer to only store one frame at a time
    }*/

    public Bitmap getImage() throws InterruptedException {
        VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
        long numImages = frame.getNumImages();
        Image rgb = null;
        for (int i = 0; i < numImages; i++) {
            Image img = frame.getImage(i);
            int fmt = img.getFormat();
           if (fmt == PIXEL_FORMAT.RGB565) {
                rgb = frame.getImage(i);
                break;
            }
        }
        Bitmap bm = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
        bm.copyPixelsFromBuffer(rgb.getPixels());
        return bm;
    }

    public Bitmap getBitmap() throws InterruptedException {

        VuforiaLocalizer.CloseableFrame picture;
        picture = vuforia.getFrameQueue().take();
        Image rgb = picture.getImage(1);

        long numImages = picture.getNumImages();

        //opMode.telemetry.addData("Num Images", numImages);
        //opMode.telemetry.update();

        for (int i = 0; i < numImages; i++) {
            int format = picture.getImage(i).getFormat();
            if (format == PIXEL_FORMAT.RGB565) {
                rgb = picture.getImage(i);
                break;

            }

            else {
                opMode.telemetry.addLine("Didn't find correct RGB format oh no");
                opMode.telemetry.update();


            }

        }

        Bitmap imageBitmap = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
        imageBitmap.copyPixelsFromBuffer(rgb.getPixels());

        //opMode.telemetry.addData("Image width", imageBitmap.getWidth());
        //opMode.telemetry.addData("Image height", imageBitmap.getHeight());
        //opMode.telemetry.update();

        picture.close();

        //opMode.telemetry.addLine("Got bitmap");
        //opMode.telemetry.update();

        return imageBitmap;
    }

    public String sample() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        String bitmapCubePosition;

        ArrayList<Integer> xValues = new ArrayList<>();

        int avgX = 0;

        //top left = (0,0)
        for (int colNum = 0; colNum < bitmap.getWidth(); colNum +=2) {

            for (int rowNum = 0; rowNum < (int)(bitmap.getHeight() ); rowNum += 3) {
                int pixel = bitmap.getPixel(colNum, rowNum);

                int redPixel = red(pixel);
                int greenPixel = green(pixel);
                int bluePixel = blue(pixel);

                if (redPixel >= RED_THRESHOLD && greenPixel >= GREEN_THRESHOLD && bluePixel <= BLUE_THRESHOLD) {
                    xValues.add(colNum);

                }

            }

        }

        for (int x : xValues) {
            avgX+= x;
        }

        avgX /= xValues.size();

        if (avgX < (bitmap.getWidth() / 3.0)) {
            bitmapCubePosition = "left";

        }
        else if (avgX > (bitmap.getWidth() / 3.0) && avgX < (bitmap.getWidth() * 2.0/3)) {
            bitmapCubePosition = "center";

        }
        else {
            bitmapCubePosition = "right";

        }

        opMode.telemetry.addData("Cube Position", bitmapCubePosition);
        opMode.telemetry.update();
        return bitmapCubePosition;
    }

    public void initBitmap() throws InterruptedException
    {
        Bitmap bitmap = getBitmap();
    }
    public String rbgVals(int xCoord, int yCoord) throws InterruptedException
    {
        Bitmap bitmap = getBitmap();
        int stone1 = bitmap.getPixel((int)(xCoord * widthFactor), (int)(yCoord * heightFactor));//bitmap.getWidth() * 2/5, 20
        int redVal1 = red(stone1);
        int greenVal1 = green(stone1);
        int blueVal1 = blue(stone1);

        return "red: " + redVal1 + " blue: " + blueVal1 + " green: " + greenVal1;
    }

    public String floorAvg() throws InterruptedException
    {
        Bitmap bitmap = getBitmap();
        int floor1 = bitmap.getPixel((int)(953 * widthFactor), (int)(473 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor1 = red(floor1);
        int greenFloor1 = green(floor1);
        int blueFloor1 = blue(floor1);

        int floor2 = bitmap.getPixel((int)(840 * widthFactor), (int)(460 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor2 = red(floor2);
        int greenFloor2 = green(floor2);
        int blueFloor2 = blue(floor2);

        int floor3 = bitmap.getPixel((int)(810 * widthFactor), (int)(510 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor3 = red(floor3);
        int greenFloor3 = green(floor3);
        int blueFloor3 = blue(floor3);


        double floorAvgRed = (double)(redFloor1 + redFloor2 + redFloor3) / 3;
        double floorAvgBlue = (double)(blueFloor1 + blueFloor2 + blueFloor3) / 3;

        return "red floor: " + floorAvgRed + "blue floor: " + floorAvgBlue;
    }

    public int findStackHeight() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        int ringStackHeight;


        int ring1 = bitmap.getPixel((int)(359 * widthFactor), (int)(674 * heightFactor));//bitmap.getWidth() * 2/5, 20
        int redVal1 = red(ring1);
        int greenVal1 = green(ring1);
        int blueVal1 = blue(ring1);

        int ring2 = bitmap.getPixel((int)(367 * widthFactor), (int)(596 * heightFactor));//bitmap.getWidth()/2, 20
        int redVal2 = red(ring2);
        int greenVal2 = green(ring2);
        int blueVal2 = blue(ring2);

        if (blueVal2 > 110 && redVal2 < 200)
        {
            if (Math.abs(blueVal1 - blueVal2) > 50)
            {
                ringStackHeight = 1;
            }
            else
            {
                ringStackHeight = 0;
            }
        }
        else
        {
            ringStackHeight = 4;
        }


        return ringStackHeight;
    }

    public int findStackHeight2() throws InterruptedException
    {
        Bitmap bitmap = getBitmap();
        int ringStackHeight;


        int ring1 = bitmap.getPixel((int)(1210 * widthFactor), (int)(520 * heightFactor));//bitmap.getWidth() * 2/5, 20
        int redVal1 = red(ring1);
        int greenVal1 = green(ring1);
        int blueVal1 = blue(ring1);

        int ring2 = bitmap.getPixel((int)(1209 * widthFactor), (int)(451 * heightFactor));//bitmap.getWidth()/2, 20
        int redVal2 = red(ring2);
        int greenVal2 = green(ring2);
        int blueVal2 = blue(ring2);

        int floor1 = bitmap.getPixel((int)(953 * widthFactor), (int)(473 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor1 = red(floor1);
        int greenFloor1 = green(floor1);
        int blueFloor1 = blue(floor1);

        int floor2 = bitmap.getPixel((int)(840 * widthFactor), (int)(460 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor2 = red(floor2);
        int greenFloor2 = green(floor2);
        int blueFloor2 = blue(floor2);

        int floor3 = bitmap.getPixel((int)(810 * widthFactor), (int)(510 * heightFactor));//bitmap.getWidth()/2, 20
        int redFloor3 = red(floor3);
        int greenFloor3 = green(floor3);
        int blueFloor3 = blue(floor3);


        double floorAvgRed = (double)(redFloor1 + redFloor2 + redFloor3) / 3;
        double floorAvgBlue = (double)(blueFloor1 + blueFloor2 + blueFloor3) / 3;

        if (redVal1 > floorAvgRed && blueVal1 < floorAvgBlue)
        {
            if (redVal2 > floorAvgRed && blueVal1 < floorAvgBlue)
            {
                ringStackHeight = 4;
            }
            else
            {
                ringStackHeight = 1;
            }
        }
        else
        {
            ringStackHeight = 0;
        }


        return ringStackHeight;
    }
    public String findBlueSkystones() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        String bitmapCubePosition;

        int stone1 = bitmap.getPixel((int)(1210 * widthFactor), (int)(520 * heightFactor));//bitmap.getWidth() * 2/5, 20
        int redVal1 = red(stone1);

        int stone2 = bitmap.getPixel((int)(1209 * widthFactor), (int)(1677 * heightFactor));//bitmap.getWidth()/2, 20
        int redVal2 = red(stone2);

        int stone3 = bitmap.getPixel((int)(2500 * widthFactor), (int)(1677 * heightFactor));//bitmap.getWidth() * 3/5, 20
        int redVal3 = red(stone3);

        ArrayList<Integer> vals = new ArrayList<Integer>();
        vals.add(redVal1);
        vals.add(redVal2);
        vals.add(redVal3);

        int min = Collections.min(vals);
        int pos = vals.indexOf(min);

        if (pos == 0){
            bitmapCubePosition = "left";
        }

        else if (pos == 1){
            bitmapCubePosition = "center";
        }

        else if (pos == 2){
            bitmapCubePosition = "right";
        }
        else {
            bitmapCubePosition = "yikes";
        }
        /*
        telemetry.addData("redval1", redVal1);
        telemetry.addData("redval2", redVal2);
        telemetry.addData("redval3", redVal3);
        telemetry.addData("left", vals.get(0));
        telemetry.addData("center", vals.get(1));
        telemetry.addData("right", vals.get(2));
        telemetry.update();
        sleep(5000);*/
        return bitmapCubePosition;
    }

    public Bitmap vufConvertToBitmap(Frame frame) { return vuforia.convertFrameToBitmap(frame); }


}