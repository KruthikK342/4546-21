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

    private final int BLACK_RED_THRESHOLD = 25;
    private final int BLACK_GREEN_THRESHOLD = 25;
    private final int BLACK_BLUE_THRESHOLD = 25;



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
        int width = (!isred) ? (int) (940.0/1280 * bitmap.getWidth()) : bitmap.getWidth();
        int teamElementXPosition = 0, teamElementPixelCount = 0;

        for(int y = 0; y < height/3; y += 3) {
            for(int x= 0; x<width; x += 2) {
                int pixel = bitmap.getPixel(x,y);
                int redValue = red(pixel);
                int blueValue  = blue(pixel);
                int greenValue = green(pixel);
                boolean isBlack = redValue <= BLACK_RED_THRESHOLD && greenValue <= BLACK_GREEN_THRESHOLD && blueValue <= BLACK_BLUE_THRESHOLD;
                if(isBlack) {
                    ++teamElementPixelCount;
                    teamElementXPosition += x;
                }
            }
        }
        int barcode = 0;
        int section = width/2;
        if (teamElementPixelCount >= 4500) {
            teamElementXPosition /= teamElementPixelCount;
            if(teamElementXPosition <= section) {
                barcode = 2;
            }else if(teamElementXPosition > section) {
                barcode = 3;
            }
        } else {
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
                boolean isBlack = redValue <= BLACK_RED_THRESHOLD && greenValue <= BLACK_GREEN_THRESHOLD && blueValue <= BLACK_BLUE_THRESHOLD;
                if(isBlack) {
                    ++teamElementPixelCount;
                    teamElementXPosition += x;
                }
            }
        }
        return teamElementPixelCount;
    }
}


