package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Drivetrain {

    LinearOpMode opMode;

    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;
    private Sensors sensor;
    ElapsedTime time;

    public Drivetrain(LinearOpMode opMode) {
        this.opMode = opMode;
        motorFL = this.opMode.hardwareMap.dcMotor.get("motorFL");
        motorFR = this.opMode.hardwareMap.dcMotor.get("motorFR");
        motorBL = this.opMode.hardwareMap.dcMotor.get("motorBL");
        motorBR = this.opMode.hardwareMap.dcMotor.get("motorFL");

        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBR.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void startMotors(double left, double right) {
        motorFL.setPower(left);
        motorFR.setPower(right);
        motorBL.setPower(left);
        motorBR.setPower(right);
    }

    public void stopMotors() {
        motorFL.setPower(0);
        motorFR.setPower(0);
        motorBL.setPower(0);
        motorBR.setPower(0);
    }

    public void moveForward(double power) {
        startMotors(power, power);
    }

    public void turnP(double angle, double p) {
        double kP = p;
        final double startPos = sensor.getGyroYaw(); // initial angle
        final double angleDiff = angle - startPos; // desired angle - initial angle = difference
        double deltaAngle = sensor.getTrueDiff(angle); // difference in current and desired angle (negative if need to turn left and positive if need to turn right)
        double changePID = 0; // the power we will apply to our motors
        while(Math.abs(deltaAngle) > .3){
            deltaAngle = sensor.getTrueDiff(angle);
            changePID = ((deltaAngle/Math.abs(angleDiff)) * kP); // As we get closer to the desired angle, the value that is multiplied with kP is decreased (we won't overshoot)
            if (changePID < 0){ // if this value is negative (need to turn left)
                startMotors(changePID - .075 , -changePID + .075); // turn method for going left (left side turns backwards and right side turns forward)
            }
            else{ // if changePID is positive (need to turn right)
                startMotors(changePID + .075, -changePID - .075); // turn method for going right (left side turns forward and right side turns backward)
            }
            // the constants that are added or subtracted with changePID in startMotors allow the motors to turn when the value of changePID becomes small
            opMode.telemetry.addData("Current position", sensor.getGyroYaw());
            opMode.telemetry.addData("Current Difference",deltaAngle);
            opMode.telemetry.addData("changePID", changePID);
            opMode.telemetry.addData("angleDiff: ", angleDiff);
            opMode.telemetry.update();
        }
        stopMotors();
    }

    public void turnPD(double angle, double p, double d, double timeout){
        time.reset();
        double kP = p / 90;
        double kD = d;
        double currentTime = time.milliseconds();
        double pastTime = 0;
        double prevAngleDiff = sensor.getTrueDiff(angle);
        double angleDiff = prevAngleDiff;
        double changePID = 0;
        while (Math.abs(angleDiff) > .5 && time.milliseconds() < timeout && opMode.opModeIsActive()) {
            pastTime = currentTime;
            currentTime = time.milliseconds();
            double dT = currentTime - pastTime;
            angleDiff = sensor.getTrueDiff(angle);
            changePID = (angleDiff * kP) + ((angleDiff - prevAngleDiff) / dT * kD); // second part is rate of change of error (derivative)
            if (changePID < 0) {
                startMotors(changePID - .10, -changePID + .10);
            } else {
                startMotors(changePID + .10, -changePID - .10);
            }
            opMode.telemetry.addData("P", (angleDiff * kP));
            opMode.telemetry.addData("D", ((Math.abs(angleDiff) - Math.abs(prevAngleDiff)) / dT * kD));
            opMode.telemetry.update();
            prevAngleDiff = angleDiff;
        }
        stopMotors();
    }

    public void turnPI(double angle, double p, double i, double timeout) {
        time.reset();
        double kP = p / 90;
        double kI = i / 100000;
        double currentTime = time.milliseconds();
        double pastTime = 0;
        double P = 0;
        double I = 0;
        double angleDiff = sensor.getTrueDiff(angle);
        double changePID = 0;
        while (Math.abs(angleDiff) > .5 && time.milliseconds() < timeout) {
            pastTime = currentTime;
            currentTime = time.milliseconds();
            double dT = currentTime - pastTime;
            angleDiff = sensor.getTrueDiff(angle); // error
            P = angleDiff * kP; // power is proportional to error
            I += dT * angleDiff * kI; // sum of error throughout entire duration of the turn
            changePID = P;
            changePID += I;
            opMode.telemetry.addData("PID: ", changePID);
            opMode.telemetry.addData("diff", angleDiff);
            opMode.telemetry.addData("P", P);
            opMode.telemetry.addData("I", I);
            opMode.telemetry.addData("motorBR power: ", motorBR.getPower());
            opMode.telemetry.update();
            if (changePID < 0) {
                startMotors(changePID - .1, -changePID + .1);
            } else {
                startMotors(changePID + .1, -changePID - .1);
            }
        }
        stopMotors();
    }




}