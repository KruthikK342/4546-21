package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Drivetrain {

    LinearOpMode opMode;

    public double countsPerInch;

    private DcMotor motorFL;
    private DcMotor motorFR;
    private DcMotor motorBL;
    private DcMotor motorBR;
    private Sensors sensor;
    ElapsedTime time;

    public Drivetrain(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        motorFL = this.opMode.hardwareMap.dcMotor.get("fL");
        motorFR = this.opMode.hardwareMap.dcMotor.get("fR");
        motorBL = this.opMode.hardwareMap.dcMotor.get("bL");
        motorBR = this.opMode.hardwareMap.dcMotor.get("bR");

        motorFL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFR.setDirection(DcMotorSimple.Direction.FORWARD);
        motorBL.setDirection(DcMotorSimple.Direction.REVERSE);
        motorBR.setDirection(DcMotorSimple.Direction.FORWARD);
        countsPerInch = EncodersPerInch(560, .5, (75/25.4));
        time = new ElapsedTime();
        sensor = new Sensors(this.opMode);
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

    public double EncodersPerInch(double encoders, double gearReduction, double wheelDiameter){
        return ((encoders * gearReduction) /(wheelDiameter * Math.PI) );
    }

    public void resetEncoders(){
        motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();
        motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        opMode.idle();

        motorFL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorFR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorBL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
        motorBR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        opMode.idle();
    }

    public void setDTBrake(){
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opMode.idle();
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opMode.idle();
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opMode.idle();
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opMode.idle();
    }

    public void setDTFloat(){
        motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        opMode.idle();
        motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        opMode.idle();
        motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        opMode.idle();
        motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        opMode.idle();
    }

    public double getEncoderAvg(){
        int count = 4;
        if (motorFL.getCurrentPosition() == 0){
            count--;
        }
        if (motorFR.getCurrentPosition() == 0){
            count--;
        }
        if (motorBL.getCurrentPosition() == 0){
            count--;
        }
        if (motorBR.getCurrentPosition() == 0){
            count--;
        }
        if (count == 0) count++;
        return (Math.abs(motorFL.getCurrentPosition()) +
                Math.abs(motorFR.getCurrentPosition()) +
                Math.abs(motorBL.getCurrentPosition()) +
                Math.abs(motorBR.getCurrentPosition())) / count;
    }

    public void moveInches(double inches, double power){
        resetEncoders();
        while (getEncoderAvg() < inches * countsPerInch && opMode.opModeIsActive()){
            startMotors(power, power);
        }
        stopMotors();
    }

    public void moveTime(int millis, double power){
        time.reset();
        while (time.milliseconds() < millis){
            startMotors(power, power);
        }
        stopMotors();
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
            /*opMode.telemetry.addData("Current position", sensor.getGyroYaw());
            opMode.telemetry.addData("Current Difference",deltaAngle);
            opMode.telemetry.addData("changePID", changePID);
            opMode.telemetry.addData("angleDiff: ", angleDiff);
            opMode.telemetry.update();
             */
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
            opMode.telemetry.addData("Gyro Roll: ", sensor.getGyroRoll());
            opMode.telemetry.addData("Gyro Yaw: ", sensor.getGyroYaw());
            opMode.telemetry.addData("Gyro Pitch: ", sensor.getGyroPitch());
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
        while (Math.abs(angleDiff) > .5 && time.milliseconds() < timeout && opMode.opModeIsActive()) {
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
            opMode.telemetry.addData("Gyro Yaw: ", sensor.getGyroYaw());
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

    public void turnPID(double angle, double p, double i, double d, double timeout) {
        time.reset();
        double kP = p / 90;
        double kI = i / 100000;
        double kD = d;
        double currentTime = time.milliseconds();
        double pastTime = 0;

    }




}