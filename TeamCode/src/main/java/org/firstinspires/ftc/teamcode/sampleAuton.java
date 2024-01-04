package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Sample Auton")
public class sampleAuton extends LinearOpMode{

    DcMotor left = hardwareMap.get(DcMotor.class, "leftDrive");
    DcMotor right = hardwareMap.get(DcMotor.class, "rightDrive");
    DcMotor intake = hardwareMap.get(DcMotor.class, "intake");
    robotDrive bot = new robotDrive(left, right, intake);
    public void runOpMode() throws InterruptedException {

        waitForStart();

        bot.drive("forward", 0.9, 1000);
        sleep(300);

        bot.drive("reverse", 0.9, 1000);
        sleep(300);

        bot.spinIntake("forward", 0.9, 500);
        sleep(300);

        bot.turn("left", 0.5, 500);
        sleep(300);
        bot.turn("right", 0.5, 500);
        
    }

}
