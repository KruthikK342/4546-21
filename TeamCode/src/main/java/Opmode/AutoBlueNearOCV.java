package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import Library.AutoVisionBlueFar;
import Library.AutoVisionBlueNear;
import Library.Carousel;
import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Outtake;

@Autonomous(name="AutoBlueNearOCV", group="4546")
public class AutoBlueNearOCV extends LinearOpMode {

    private Drivetrain drivetrain;
    private OpenCvCamera webcam;
    private AutoVisionBlueNear pipeline;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;


    public void carousel() {

        drivetrain.moveInches(3, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(90, 0.8, 0, 3000); // Turn right 90
        sleep(300);
        drivetrain.moveInches(50, 0.5); // Move forward
        sleep(450);
        drivetrain.turnPD(165, .8, 0, 3000);
        sleep(300);
        drivetrain.moveInches(23.5, .5);
        sleep(200);
        //start carousel spin

        carousel.spin(.5);
        sleep(3000);
        carousel.stop();

        //intake code

        intake.collect(.8);
        drivetrain.moveInches(1,.8);
        sleep(300);
        drivetrain.moveInches(3,.6);
        sleep(400);
        drivetrain.turnPI(200, .85, .1, 4000);
        sleep(250);
        drivetrain.moveInches(5,.5);
        sleep(1000);
        //drivetrain.turnPI(180, 1, .1, 3000);
        //sleep(250);

        intake.stop();

        //intake stopped

        //deposits

        drivetrain.moveInches(-3,1);
        drivetrain.turnPI(130, .9, .1, 3000);
        sleep(150);
        drivetrain.moveInches(60, -1);
        sleep(150);
        outake.highGoal();
    }
    public void duckPickup() {
        //preset code
        //collect duck
        drivetrain.moveInches(-2.5, 0.5);
        sleep(500);
        drivetrain.turnPI(130, .35, .3, 3000);
        sleep(500);
        drivetrain.moveInches(2.5, 0.4);
        sleep(500);
        //intake.collect();
        sleep(500);
        //set robot in proper pos to do shipping hub
        drivetrain.turnPI(-90, 0.3, 0.1, 3000); // Turn left 90
        sleep(500);
        drivetrain.moveInches(3, 0.5);
        sleep(500);
        drivetrain.turnPI(-45, .3, .3, 3000);
        sleep(500);
        drivetrain.moveInches(3, 0.5);
    }

    public void park() {
        drivetrain.moveInches(5, 0.5);
        sleep(500);
        drivetrain.turnPI(90, 0.25, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(30, 0.5);
        sleep(500);
        drivetrain.turnPI(0, .25, .1, 3000);
        sleep(500);
        drivetrain.moveInches(10, .5);
    }

    public void duckHighGoal() {
        sleep(500);
        drivetrain.turnPD(90, .8, .1, 3000);
        sleep(250);
        drivetrain.moveInches(28, -.5);
        sleep(500);
        outake.highGoal();
    }

    public void reliablePark() {
        drivetrain.moveInches(6.5, -.4);
    }


    public void highGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 2000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 2000);
        sleep(550);
        drivetrain.moveInches(10,-.5);
        sleep(600);
        outake.highGoal();
    }

    public void midGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 3000);
        sleep(550);
        drivetrain.moveInches(5,-.5);
        sleep(700);
        outake.highGoal();
    }

    public void lowGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 3000);
        sleep(550);
        drivetrain.moveInches(1,-.5);
        sleep(700);
        outake.lowGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {

        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        DcMotor fL  = hardwareMap.get(DcMotor.class, "fL");
        DcMotor bL  = hardwareMap.get(DcMotor.class, "bL");
        DcMotor fR  = hardwareMap.get(DcMotor.class, "fR");
        DcMotor bR  = hardwareMap.get(DcMotor.class, "bR");
        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addLine("Robot Initialized");
        telemetry.update();

        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        AutoVisionBlueNear.VisionPipeline detector = new AutoVisionBlueNear.VisionPipeline();
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
                webcam.setPipeline(detector);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        while (!isStarted())
        {
            telemetry.addData("Team element location: ", detector.getLocation());
            Scalar leftAvg  = detector.getLeftScalar();
            Scalar rightAvg = detector.getRightScalar();
            telemetry.addData("leftAvg: ", leftAvg);
            telemetry.addData("rightAvg: ", rightAvg);

            telemetry.update();
        }
        waitForStart();
        int location = detector.getLocation();
        switch(location){
            case 1: {
                telemetry.addData("Team element location: ", location);
                telemetry.update();
                lowGoal();
                break;
            }
            case 2: {
                telemetry.addData("Team element location: ", location);
                telemetry.update();
                midGoal();
                break;
            }
            case 3: {
                telemetry.addData("Team element location: ", location);
                telemetry.update();
                highGoal();
                break;
            }
            default: {
                telemetry.addData("Team element location: ", location);
                telemetry.update();
                highGoal();
            }
            park();
        }
        webcam.stopStreaming();
    }
}