package Library;

import android.graphics.Bitmap;

import java.util.Collections;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import java.util.ArrayList;

import static android.graphics.Color.blue;
import static android.graphics.Color.green;
import static android.graphics.Color.red;

public class DuckBarcodeBitmap {
    LinearOpMode opMode;
    int stackHeight = 0;
    int barcode = 0;

    private VuforiaLocalizer vuforia;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaLocalizer.CameraDirection CAMERA_CHOICE = VuforiaLocalizer.CameraDirection.BACK;
    private static final String VUFORIA_KEY = "AVFFxKT/////AAABmQTYeIgT6k6wv0phn1XaTKN+Z9RdP23vp3+6IEyv9haxqO0u2vStZKAjPLct97BEhaeSkeYivFGo2IDu8fWfJlBY+2JZ0FIf8M2N7yW5XExNYWbGNwwem7Wgzsl5ld4wr6xOeXqcwtVn1mgt5ELcypOfvRnnun3FWIBr7mx+AJRN1ZAnqVvfOphPVxNm9vpylN4d5nJu58aTxiXMCJadPhhyviOGVlI6tT//lTO5GJEBva9xN+SXpxsTnPEaegQNE+qzFxVzmtXabk+oAuMxDh1XR+6EbyZzZjQm3gI9DXkt7os7ZkM95GXEZN9MHRwPWdwbk1Bt/iGI3VcXp2VfUDhWYXaWJjvu/aZC2WqrhAef";

    private final int RED_THRESHOLD = 168;
    private final int GREEN_THRESHOLD = 118;
    private final int BLUE_THRESHOLD = 150;



    public DuckBarcodeBitmap (LinearOpMode opMode){

        this.opMode = opMode;

        int cameraMonitorViewId = this.opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", this.opMode.hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        params.vuforiaLicenseKey = VUFORIA_KEY;
        params.cameraDirection = CAMERA_CHOICE;
        params.cameraName = opMode.hardwareMap.get(WebcamName.class, "Webcam 1");
        vuforia = ClassFactory.getInstance().createVuforia(params);

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        vuforia.setFrameQueueCapacity(4);
        vuforia.enableConvertFrameToBitmap();

    }

    public Bitmap getBitmap() throws InterruptedException {

        VuforiaLocalizer.CloseableFrame picture;
        picture = vuforia.getFrameQueue().take();
        Image rgb = picture.getImage(1);

        long numImages = picture.getNumImages();

        //opMode.telemetry.addData("Num images", numImages);
        //opMode.telemetry.update();

        for (int i = 0; i < numImages; i++) {

            int format = picture.getImage(i).getFormat();
            if (format == PIXEL_FORMAT.RGB565) {
                rgb = picture.getImage(i);
                break;
            }
        }

        Bitmap imageBitmap = Bitmap.createBitmap(rgb.getWidth(), rgb.getHeight(), Bitmap.Config.RGB_565);
        imageBitmap.copyPixelsFromBuffer(rgb.getPixels());

        opMode.sleep(200);

        picture.close();


        return imageBitmap;
    }

    public double getImageHeight() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        return bitmap.getHeight();
    }

    public double getImageWidth() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        return bitmap.getWidth();
    }

    public int getBarcode(boolean isred) throws InterruptedException {
        Bitmap bitmap = getBitmap();
        int height = bitmap.getHeight();
        /*
            This ternary expression accounts for the bitmap resizing the image taken
            by the camera. We only need to do this for the blue side since the width
            has to be shortened there. Otherwise, if it's red, then we can just use the
            whole width.
        */
        int width = bitmap.getWidth();          //me when
        int teamElementXPosition = 0, teamElementPixelCount = 0;

        /*
            In this loop, we obtain pixel values from the bitmap to determine
            the average X position of the bitmap. Based on a threshold, we can
            then determine where the barcode is (1, 2, or 3). When looping over
            the bitmap, we skip every third y value and every second x value so
            that we can go over the entire bitmap in a timely manner. The height
            used here is 1/3 of the bitmap height (this avoids black pixels in
            the background from being included).
        */
        for(int y = 0; y < height; y += 3) {
            for(int x = 0; x<width; x += 2) {
                /*
                * Get the pixel value
                * Get red, blue, and green values for pixel
                * Check if the pixel falls within the threshold
                * If it  does, then we count that as a black pixel
                * and get its X position.
                */
                int pixel = bitmap.getPixel(x,y);
                int redValue = red(pixel);
                int blueValue  = blue(pixel);
                int greenValue = green(pixel);
                boolean isRed = 167 >= redValue && redValue < 170;
                boolean isGreen = 118 >= greenValue && greenValue < 125;
                boolean isBlue = 118 >= blueValue && blueValue < 150;
                if(isRed && isGreen && isBlue) {
                    ++teamElementPixelCount;
                    teamElementXPosition += x;
                }
            }
        }

        int barcode = 0;
        /*
            The section variable is half the width since only
            two of the barcodes are visible. The left half of the
            screen contains the second barcode and the right half contains
            the third barcode. If the minimum threshold of black pixels is
            not met, then we can assume that the barcode must be the first
            one. The threshold was decided through experimentation.
        */
        int section = width/2;
            if(teamElementXPosition <= section) {
                barcode = 2;
            }
            else if(teamElementXPosition > section) {
                barcode = 3;
            }
        else {
            barcode = 1;
        }

        opMode.telemetry.addData("TeamElementPixelCount: ", teamElementPixelCount);
        opMode.telemetry.addData("TeamElementXPosition: ", teamElementXPosition);
        opMode.telemetry.addData("Barcode ", barcode);
        opMode.telemetry.update();
        return barcode;
    }

    public int getTeamElementPixelCount() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth(); //bitmap.getWidth();
        int teamElementXPosition = 0, teamElementPixelCount = 0;

        for(int y = 0; y < height/3; y += 3) {
            for(int x= 0; x< bitmap.getWidth(); x += 2) {
                int pixel = bitmap.getPixel(x,y);
                int redValue = red(pixel);
                int blueValue  = blue(pixel);
                int greenValue = green(pixel);
                boolean isBlack = redValue >= RED_THRESHOLD && greenValue >= GREEN_THRESHOLD && blueValue >= BLUE_THRESHOLD;
                if(isBlack) {
                    ++teamElementPixelCount;
                    teamElementXPosition += x;
                }
            }
        }
        return teamElementPixelCount;
    }
}


