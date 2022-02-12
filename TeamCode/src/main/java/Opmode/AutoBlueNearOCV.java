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
    private DcMotor spinner;
    LinearOpMode opMode;



    public void carousel() {
        drivetrain.moveInches(3, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(100, 0.8, 0, 3000); // Turn right 90
        sleep(300);
        drivetrain.moveInches(50, 0.4); // Move forward
        sleep(450);
        drivetrain.turnPD(160, .8, 0, 3000);
        sleep(300);
        drivetrain.moveInches(17, .5);
        sleep(200);

        //start carousel spin
        spinner.setPower(.4);
        drivetrain.startMotors(.05,.05);
        sleep(3300);
        spinner.setPower(0);


    }

    public void park() {
        drivetrain.moveInches(5, -0.5);
        sleep(500);
        drivetrain.turnPI(185, 0.25, 0.3, 3000);
        sleep(500);
        drivetrain.moveInches(26, -0.5);
        sleep(500);
        drivetrain.turnPI(180, .7, .1, 1000);
        sleep(500);
    }



    public void highGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(300);
        drivetrain.turnPI(90, .25, .1, 2000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(18, -.5);
        sleep(350);
        drivetrain.turnPI(160,.4, .1, 2000);
        sleep(250);
        drivetrain.moveInches(6.5,-.5);
        sleep(500);
        outake.highGoal();
    }

    public void midGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(300);
        drivetrain.turnPI(90, .25, .1, 2000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(20, -.5);
        sleep(350);
        drivetrain.turnPI(160,.4, .1, 2000);
        sleep(250);
        drivetrain.moveInches(6.5,-.5);
        sleep(500);
        outake.midGoal();
    }

    public void lowGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(300);
        drivetrain.turnPI(90, .25, .1, 2000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(20, -.5);
        sleep(350);
        drivetrain.turnPI(160,.4, .1, 2000);
        sleep(250);
        drivetrain.moveInches(8,-.5);
        sleep(500);
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
        spinner = hardwareMap.get(DcMotor.class,"carousel");
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
        }
        carousel();
        park();
        webcam.stopStreaming();
    }
}