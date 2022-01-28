package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous; //standard imports
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName; //easyOpenCV imports
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import Library.Carousel; //team imports
import Library.Drivetrain;
import Library.Intake;
import Library.Outtake;
import Library.VisionPipeline;


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
    private VisionPipeline pipeline;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;


    public void highGoal() {
        drivetrain.moveInches(10, 0.5);
        sleep(500);
        drivetrain.turnPD(-90, 0.6, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(30, -0.5);
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
        drivetrain.moveInches(8, -0.45);
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
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        VisionPipeline detector = new VisionPipeline(telemetry);
        webcam.setPipeline(detector);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_LEFT);
                webcam.setPipeline(pipeline);
            }

            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();
        switch(detector.getLocation()){
            case (1): {
                lowGoal();
                break;
            }
            case (2): {
                midGoal();
                break;
            }
            case (3): {
                highGoal();
                break;
            }
            default: {
                lowGoal();
            }
            park();
        }
        webcam.stopStreaming();
    }
}
