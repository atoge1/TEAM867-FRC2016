
//default imports

package org.usfirst.frc.team867.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


//additional imports

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CameraServer;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
    //global variables
    	
    	//motors - note: make sure CAN devices are numbered properly in the web interface (connect roborio and go to 172.22.11.2). 
    	//Start device IDs from 1. Leave 0 free for new devices.
    	//code refers to device 1 with an index of 0
    	CANTalon[] motorlist; //stores all motors
    		
    	//joysticks - note: buttons start at 1
    	Joystick joyDrive; //driver joystick
    	Joystick joyManip; //manipulator joystick
    	
    		//joystick values
    		double leftDrive; //stores left joystick y axis value
    		double rightDrive; //stores right joystick y axis value
    		
    				
    	//compressor
    	Compressor comp;
    
    	//valves
    
    	//camera
    	CameraServer camser;
    	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
    
        
        
    //motorlist
        motorlist = new CANTalon[6];
        for(int i=0; i<=5; i++)
        {
        	motorlist[i] = new CANTalon(i+1);
        }
        
        //enable brake mode for drive motors
        for(int i=0; i<=3; i++)
        {
        	motorlist[i].enableBrakeMode(true);
        }
        
    //joysticks
        joyDrive = new Joystick(0);
        joyManip = new Joystick(1);
        
        
    //compressor
        comp = new Compressor();
        //enable
        comp.start();
        
    //valves
        
    //camera
        camser= CameraServer.getInstance();
        camser.setQuality(50);
    	camser.startAutomaticCapture("cam0");
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        leftDrive = joyDrive.getY(Hand.kLeft); //point of error
        rightDrive = joyDrive.getY(Hand.kRight); //point of error
        
        motorlist[0].set(leftDrive);
        motorlist[1].set(leftDrive);
        motorlist[2].set(rightDrive);
        motorlist[3].set(rightDrive);
        
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	joyDrive.setRumble(RumbleType.kLeftRumble, 1);
    	joyDrive.setRumble(RumbleType.kRightRumble, 1);
    	Timer.delay(1);
    	joyDrive.setRumble(RumbleType.kLeftRumble, 0);
    	joyDrive.setRumble(RumbleType.kRightRumble, 0);
    	joyManip.setRumble(RumbleType.kLeftRumble, 1);
    	joyManip.setRumble(RumbleType.kRightRumble, 1);
    	Timer.delay(1);
    	joyDrive.setRumble(RumbleType.kLeftRumble, 0);
    	joyDrive.setRumble(RumbleType.kRightRumble, 0);
    	
    }
    
}



































