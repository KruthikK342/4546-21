package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.DuckBarcodeBitmap;
import Library.Outtake;
import Library.VisionOCV;

@Autonomous(name="TestOCV", group="4546")
public class TestOCV extends LinearOpMode {


    OpenCvCamera botCam;

    @Override
    public void runOpMode() throws InterruptedException {
        int cameraMonitorViewId = hardwareMap.appContext
                .getResources().getIdentifier("cameraMonitorViewId",
                        "id", hardwareMap.appContext.getPackageName());
        botCam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
        VisionOCV detector = new VisionOCV(telemetry);
        botCam.setPipeline(detector);
        botCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                botCam.startStreaming(1280, 720, OpenCvCameraRotation.SIDEWAYS_LEFT );
            }
        });
                waitForStart();
        switch (detector.getLocation()) {
            case LOC1:
                // ...
                break;
            case LOC2:
                // ...
                break;
            case LOC3:
                // ...
        }
        botCam.stopStreaming();
    }
}
