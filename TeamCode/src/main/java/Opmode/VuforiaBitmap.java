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
public class VuforiaBitmap {
    private LinearOpMode opMode;
    private VuforiaLocalizer vuforia;
    private Parameters parameters;
    private CameraDirection CAMERA_CHOICE = CameraDirection.BACK;

    private final int RED_THRESHOLD = 255;
    private final int GREEN_THRESHOLD = 255;
    private final int BLUE_THRESHOLD = 0;

    private final double widthFactor = 1.0;//3264;
    private final double heightFactor = 1.0;//1836;
    private final int xdiff = 650;
    private final int yheight = 300;

    private static final String VUFORIA_KEY = "AVFFxKT/////AAABmQTYeIgT6k6wv0phn1XaTKN+Z9RdP23vp3+6IEyv9haxqO0u2vStZKAjPLct97BEhaeSkeYivFGo2IDu8fWfJlBY+2JZ0FIf8M2N7yW5XExNYWbGNwwem7Wgzsl5ld4wr6xOeXqcwtVn1mgt5ELcypOfvRnnun3FWIBr7mx+AJRN1ZAnqVvfOphPVxNm9vpylN4d5nJu58aTxiXMCJadPhhyviOGVlI6tT//lTO5GJEBva9xN+SXpxsTnPEaegQNE+qzFxVzmtXabk+oAuMxDh1XR+6EbyZzZjQm3gI9DXkt7os7ZkM95GXEZN9MHRwPWdwbk1Bt/iGI3VcXp2VfUDhWYXaWJjvu/aZC2WqrhAef";

    private BlockingQueue<VuforiaLocalizer.CloseableFrame> frame;

    public static String duckPos;

    public VuforiaBitmap() {
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

    public Bitmap getDuckImage() throws InterruptedException {
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
            picture.close();
            return imageBitmap;
    }

    



}
