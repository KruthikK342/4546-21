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
import Library.AutoVisionRedNear;
import Library.Carousel;
import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Outtake;

@Autonomous(name="AutoRedNearOCV", group="4546")
public class AutoRedNearOCV extends LinearOpMode {

    private Drivetrain drivetrain;
    private OpenCvCamera webcam;
    private AutoVisionRedNear pipeline;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;
    private DcMotor spinner;
    LinearOpMode opMode;

    public void carousel() {

        drivetrain.moveInches(4.5, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(-106.5, 0.63, .1, 3000);
        sleep(300);
        drivetrain.moveInches(60, 0.45); // Move forward
        sleep(450);

        carousel.spin(-.4);
        //drivetrain.startMotors(.05,.05);
        sleep(3300);
        carousel.stop();
        sleep(300);

    }



    public void park() {
        drivetrain.moveInches(3, -0.5);
        sleep(800);

        drivetrain.turnPD(110, 0.8, 0.1, 2000);
        sleep(800);
        drivetrain.moveInches(23, -.5);

    }


    public void highGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(250);
        drivetrain.turnPI(-120, .25, .1, 3000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(19, -.5);
        sleep(250);
        drivetrain.turnPI(200,.38,.08, 3000);
        sleep(300);
        drivetrain.moveInches(2.5, -.4);
        sleep(400);
        outake.highGoal();


    }

    public void midGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(250);
        drivetrain.turnPI(-120, .25, .1, 3000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(14, -.5);
        sleep(250);
        drivetrain.turnPI(200,.25,.1, 2000);
        sleep(300);
        //drivetrain.moveInches(3.5, -.4);
        sleep(400);
        outake.midGoal();
    }



    public void lowGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(250);
        drivetrain.turnPI(-120, .25, .1, 3000); // Turn towards warehouse
        sleep(250);
        drivetrain.moveInches(15, -.5);
        sleep(250);
        drivetrain.turnPI(200,.38,.08, 3000); //
        sleep(300);
        drivetrain.moveInches(4, -.4);
        sleep(400);
        outake.lowGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {


        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        carousel = new Carousel(this);
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
        AutoVisionRedNear.VisionPipeline detector = new AutoVisionRedNear.VisionPipeline();
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