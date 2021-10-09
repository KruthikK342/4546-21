package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import Library.Vision;

@Autonomous(name = "testVision", group = "Autonomous")
public class testVision_UG extends LinearOpMode {

    public ElapsedTime runtime = new ElapsedTime();
    //ElapsedTime time = new ElapsedTime();
    //WebCamVision vision = new WebCamVision(this);

    // Testing
    @Override
    public void runOpMode() throws InterruptedException {

        //stuff that happens after init is pressed
        Vision vision = new Vision(this);
        waitForStart();
        while (opModeIsActive())  {
            vision.checkRGB();
            sleep(5000);
        }
    }
}



