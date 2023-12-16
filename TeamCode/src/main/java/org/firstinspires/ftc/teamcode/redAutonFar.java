package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Red Far Auton")
public class redAutonFar extends LinearOpMode{
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor intake;

    //encoder variables
    double ticks = 537.7;
    double wheelCircumfrence = (3.75 * Math.PI);
    double robotCircumfrence = (17 * Math.PI);
    double reduction = 0.714285;
    double countsPerInch = (ticks * reduction)/wheelCircumfrence;

    public void runOpMode(){
        // set up motors
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();

        // drive to spike mark and outake pixel
        drive("forward", 36, 36);
        sleep(300);

        telemetry.addData("Drove to:", "spike mark");

        intake.setPower(0.33);
        sleep(500);
        intake.setPower(0);

        telemetry.addData("Outtaked:", "pixel");

        drive("reverse", 36, 36);
        sleep(300);

        telemetry.addData("Drove to:", "start");

        // turn and drive to backstage, then outake another pixel
        turnRight(1500);
        sleep(300);

        telemetry.addData("Turned:", "right");

        drive("forward", 96, 96);
        sleep(300);

        telemetry.addData("Drove to:", "backstage");

        intake.setPower(0.33);
        sleep(1000);
        intake.setPower(0);
        telemetry.addData("Outtaked:", "pixel");
    }


    // drive functions
    private void stopDrive(){
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }


    private void drive(String direction, int leftInches, int rightInches){
        if(direction == "forward") {
            leftDrive.setDirection(DcMotor.Direction.FORWARD);
            rightDrive.setDirection(DcMotor.Direction.REVERSE);
        }else if(direction == "reverse"){
            leftDrive.setDirection(DcMotor.Direction.REVERSE);
            rightDrive.setDirection(DcMotor.Direction.FORWARD);
        }else{
            telemetry.addData("Error:", "Wrong direction in drive parameter");
        }

        int newLeftTarget = leftDrive.getCurrentPosition() + (int)(leftInches * countsPerInch);
        int newRightTarget = rightDrive.getCurrentPosition() + (int)(rightInches * countsPerInch);

        leftDrive.setTargetPosition(newLeftTarget);
        rightDrive.setTargetPosition(newRightTarget);

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        while(leftDrive.isBusy() || rightDrive.isBusy()){
            telemetry.addData("Running to pos, left drive pos: ", leftDrive.getCurrentPosition());
        }

        stopDrive();

    }


    private void turnLeft(int duration){
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);


        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);


        sleep(duration);
        stopDrive();
    }


    private void turnRight(int duration){
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);


        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);


        sleep(duration);
        stopDrive();
    }


}
