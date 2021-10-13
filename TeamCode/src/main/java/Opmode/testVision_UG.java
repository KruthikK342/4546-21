package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import Library.DuckBarcodeBitmap;

@Autonomous(name = "testVision", group = "Autonomous")
public class testVision_UG extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    //ElapsedTime time = new ElapsedTime();
    //WebCamVision vision = new WebCamVision(this);

    // Testing
    @Override
    public void runOpMode() throws InterruptedException {

        //stuff that happens after init is pressed
        DuckBarcodeBitmap db = new DuckBarcodeBitmap(this);
        waitForStart();
        while (opModeIsActive())  {
            db.getBarcode();
            sleep(5000);
        }
    }
}



