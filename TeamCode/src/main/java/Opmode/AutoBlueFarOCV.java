package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous; //standard imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName; //easyOpenCV imports
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.opencv.core.Scalar;

import Library.Carousel; //team imports
import Library.Drivetrain;
import Library.Intake;
import Library.Outtake;
import Library.AutoVision;


/* Ports:
0-Intake
1-carousel
2-arm
3-outake
 */

@Autonomous(name="AutoBlueFarOCV", group="4546")
public class AutoBlueFarOCV extends LinearOpMode {

    private Drivetrain drivetrain;
    private OpenCvCamera webcam;
    private AutoVision pipeline;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;
    //public int[] BoundingBoxABF1 = new int[]{50, 25, 280, 320};
    //public int[] BoundingBoxABF2 = new int[]{700, 25, 920, 320};



    public void highGoal() {
        drivetrain.moveInches(10, 0.5);
        sleep(500);
        drivetrain.turnPD(-90, 0.6, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(33, -0.5);
        sleep(500);
        drivetrain.turnPD(-180, .5, 0, 3000);
        sleep(500);
        drivetrain.moveInches(9, -.5);
        sleep(500);
        outake.highGoal();
        sleep(500);
    }

    public void midGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);
        // drivetrain.turnPI(-255, 0.05, 0, 2000);
        drivetrain.turnPD(-135, 0.45, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.3, -0.45);
        sleep(500);
        outake.midGoal();
        sleep(500);
    }

    public void lowGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);

        drivetrain.turnPI(-135, 0.25, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(30, -0.45);
        sleep(500);
        outake.lowGoal();
        sleep(500);
    }

    public void park() {
        drivetrain.moveInches(2.5, 0.5);
        sleep(800);

        drivetrain.turnPI(0, 0.4, 0.1,3000);
        sleep(500);
        drivetrain.turnPI(80,.4,.1,3000);

        drivetrain.moveInches(7, -.8);
        sleep(1000);
        drivetrain.moveInches(60, -1);
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
        AutoVision.VisionPipeline detector = new AutoVision.VisionPipeline();
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
            telemetry.addData("pos 2 confidence: ", detector.getLeftValue());
            telemetry.addData("pos 3 confidence: ", detector.getRightValue());
            double[] hsv = detector.getPixelHsv();

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
