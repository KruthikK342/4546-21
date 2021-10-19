package Library;

import android.graphics.Bitmap;

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

    private final int YELLOW_RED_THRESHOLD = 225;
    private final int YELLOW_GREEN_THRESHOLD = 225;
    private final int YELLOW_BLUE_MAX = 130;

    private final int RED_RED_THRESHOLD = 200;
    private final int RED_GREEN_THRESHOLD = 35;
    private final int RED_BLUE_THRESHOLD = 35;


    private final int BLUE_RED_THRESHOLD = 100;
    private final int BLUE_GREEN_THRESHOLD = 95;
    private final int BLUE_BLUE_THRESHOLD = 245;


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
            } else {
                //opMode.telemetry.addLine("Didn't find correct RGB format");
                //opMode.telemetry.update();

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

    public int getBarcode() throws InterruptedException {
        Bitmap bitmap = getBitmap();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        boolean duckFound =false;
        int duckXPosition = 0;

        for(int y=0; y < height && !duckFound; y++) {
            int duckPixelCount = 0;
            for(int x=0; x<width; x++) {
                int pixel = bitmap.getPixel(x,y);
                int redValue = red(pixel);
                int blueValue  = blue(pixel);
                int greenValue = green(pixel);
                boolean isYellow = redValue >= YELLOW_RED_THRESHOLD && greenValue >=YELLOW_GREEN_THRESHOLD && blueValue <= YELLOW_BLUE_MAX;
                if(isYellow) {
                    opMode.telemetry.addData("R: ", red(pixel));
                    opMode.telemetry.addData("G: ", green(pixel));
                    opMode.telemetry.addData("B: ", blue(pixel));
                    duckPixelCount++;
                    duckXPosition = x;
                    duckFound = true;
                    break;
                }
            }
        }
        int barcode = 0;
        int section = width/3;
        if(duckFound) {

            if(duckXPosition >= 0 &&  duckXPosition <= section) {
                barcode = 1;
            }else if(duckXPosition > section && duckXPosition <= (section*2)) {
                barcode = 2;
            }else {
                barcode = 3;
            }

        }
        opMode.telemetry.addData("DuckxPosition: ", duckXPosition);
        opMode.telemetry.addData("section: ", section);
        opMode.telemetry.addData("width: ", width);
        opMode.telemetry.addData("Barcode: ", barcode);
        opMode.telemetry.update();
        return barcode;
    }
}


