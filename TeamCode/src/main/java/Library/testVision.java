package org.firstinspires.ftc.teamcode.ultimategoal2020;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "testVision", group = "Autonomous")
public class testVision extends LinearOpMode {

    hardwareMap robot = new hardwareMap();
    public ElapsedTime runtime = new ElapsedTime();
    //WebCamVision vision = null;
    //ElapsedTime time = new ElapsedTime();
    //WebCamVision vision = new WebCamVision(this);

    // Testing
    @Override
    public void runOpMode() throws InterruptedException {

        //stuff that happens after init is pressed
        WebCamVision vision = new WebCamVision(this);
        robot.init(this);


        waitForStart();

        while (opModeIsActive()) {

            //call functions

            //robot.strafeLeft(20, 0.5);

            String ring1 = vision.rbgVals(1264, 570);
            String ring2 = vision.rbgVals(1261, 499);
            String floor = vision.floorAvg();

            int height = vision.findStackHeight();
            telemetry.addData("stack height: ", height);
            telemetry.addData("ring 1: ", ring1);
            telemetry.addData("ring 2: ", ring2);
            telemetry.addData("floor ", floor);
            telemetry.update();


            sleep(3000000);

            // strafe left
            // turn right
            //dump.setPosition(1);
            // go backwards
            // strafe left


        }
    }
}



