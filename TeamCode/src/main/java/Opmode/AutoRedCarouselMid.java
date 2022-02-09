package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import Library.AutoVisionBlueFar;
import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.Outtake;

@Autonomous(name="AutoRedCarouselMid", group="4546")
public class AutoRedCarouselMid extends LinearOpMode {
    // front left, front right, back left, back right motors
    private OpenCvCamera webcam;
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;


    public void carousel() {
        drivetrain.moveInches(3, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(-90, 0.5, 0.1, 3000); // Turn right 90
        sleep(450);
        drivetrain.moveInches(33, 0.5); // Move forward
        sleep(450);
        carousel.spin(-.4);
        sleep(3000);
        carousel.stop();
    }
    public void highGoal() {

        drivetrain.turnPD(-180, 0.5, 0.05, 3000);
        sleep(450);
        drivetrain.moveInches(44.5, -0.5);

        sleep(450);

        drivetrain.turnPD(-90, 0.45, 0.25, 2000);
        sleep(300);
        drivetrain.moveInches(36.5, -0.45);
        sleep(450);


        outake.highGoal();
        sleep(400);
    }

    public void midGoal() {

        drivetrain.turnPD(-180, 0.5, 0.05, 3000);
        sleep(450);
        drivetrain.moveInches(44.5, -0.5);

        sleep(450);

        drivetrain.turnPD(-90, 0.45, 0.25, 2000);
        sleep(300);
        drivetrain.moveInches(36.5, -0.45);
        sleep(450);


        outake.midGoal();
        sleep(400);
    }

    public void lowGoal() {

        drivetrain.turnPD(-180, 0.5, 0.05, 3000);
        sleep(450);
        drivetrain.moveInches(44.5, -0.5);

        sleep(450);

        drivetrain.turnPD(-90, 0.45, 0.25, 2000);
        sleep(300);
        drivetrain.moveInches(36.5, -0.45);
        sleep(450);


        outake.lowGoal();
        sleep(400);
    }


    public void park() {
        drivetrain.moveInches(35, 0.6);
        sleep(300);
        drivetrain.turnPD(-180, 0.45, 0.25, 2000);
        sleep(300);
        drivetrain.moveInches(15, 0.5);

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
        AutoVisionBlueFar.VisionPipeline detector = new AutoVisionBlueFar.VisionPipeline();
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
        carousel();
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


