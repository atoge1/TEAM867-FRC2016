
//default imports

package org.usfirst.frc.team867.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


//additional imports

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;



import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Servo;


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
    	int camSes0;
    	int camSes1;
    	Image frame;
    	
    	//controller axes
        final int leftY = 1;
        final int rightY = 5;
        final int yButton = 4;
        
        
        //state variables
        boolean firstRun;
        boolean useCam0;
        
    	
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
        
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        
        //camSes0
        try 
        {
        	camSes0 = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        }
        catch(VisionException e)
        {
        	SmartDashboard.putString("Camera error @ camSes0 open", e.toString());
        }
        
        try 
        {
        	NIVision.IMAQdxConfigureGrab(camSes0);
        }
        catch(VisionException e)
        {
        	SmartDashboard.putString("Camera error @ camSes0 configGrab", e.toString());
        }
        
        
        
        //camSes1
        try 
        {
        	camSes1 = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        }
        catch(VisionException e)
        {
        	SmartDashboard.putString("Camera error @ camSes1 open", e.toString());
        }
        
        try 
        {
        	NIVision.IMAQdxConfigureGrab(camSes1);
        }
        catch(VisionException e)
        {
        	SmartDashboard.putString("Camera error @ camSes1 configGrab", e.toString());
        }
    //state variables
        firstRun = true;
        useCam0 = true;
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
    		
    		
    		//logitech controllers, no rumble.
    		/*
        		for(int i = 0; i<5; i++)
        		{
        			joyDrive.setRumble(RumbleType.kLeftRumble, i%2 + 1);
        			joyDrive.setRumble(RumbleType.kRightRumble, i%2);
        			Timer.delay(.1);
        		}
        	
        		joyDrive.setRumble(RumbleType.kLeftRumble, 0);
        		joyDrive.setRumble(RumbleType.kRightRumble, 0);
        		joyManip.setRumble(RumbleType.kLeftRumble, 1);
        		joyManip.setRumble(RumbleType.kRightRumble, 1);
        		Timer.delay(.5);
        		joyDrive.setRumble(RumbleType.kLeftRumble, 0);
        		joyDrive.setRumble(RumbleType.kRightRumble, 0);
        	*/
        	
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
      	
    	//starts up cameras on first teleop cycle
    	if(firstRun)
    	{
            
            //to draw a rect, uncomment and modify the line below; also for one more right after image grab ***
            //NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

    	}
    	
    	//toggle for cameras
    	if(joyDrive.getRawButton(yButton))
    	{
    		useCam0 = !useCam0;
    		Timer.delay(.03);
    	}
      	
    	//grabs image
    	
    	if(useCam0)
    	{
    		
    		try
    		{
    			NIVision.IMAQdxStopAcquisition(camSes1);
    		}
    		catch(VisionException e)
    		{
            	SmartDashboard.putString("Camera error @ camSes1 stopAcq", e.toString());
    		}
    		
    		
    		try
    		{
    			NIVision.IMAQdxStartAcquisition(camSes0);
    		}
    		catch(VisionException e)
    		{
            	SmartDashboard.putString("Camera error @ camSes0 startAcq", e.toString());
    		}
    		
    	}
    	else
    	{
    		
    		try
    		{
    			NIVision.IMAQdxStopAcquisition(camSes0);
    		}
    		catch(VisionException e)
    		{
            	SmartDashboard.putString("Camera error @ camSes0 stopAcq", e.toString());
    		}
    		
    		
    		try
    		{
    			NIVision.IMAQdxStartAcquisition(camSes1);
    		}
    		catch(VisionException e)
    		{
            	SmartDashboard.putString("Camera error @ camSes1 startAcq", e.toString());
    		}
    	}
    	
    	
    	
    	
    	
    	
    	
    	try
    	{
    		NIVision.IMAQdxGrab(useCam0 ? camSes0 : camSes1, frame, 1);
    	}
    	catch(VisionException e)
    	{
        	SmartDashboard.putString("Camera error @ grabImage", e.toString());

    	}
    	
		//***uncomment to draw predefined rectangle
		// NIVision.imaqDrawShapeOnImage(frame, frame, rect,DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
		
		
		//sends to camera
		CameraServer.getInstance().setImage(frame);
    	
		//joystick code
    	leftDrive = joyDrive.getRawAxis(leftY); 
        rightDrive = joyDrive.getRawAxis(rightY);
        
        motorlist[0].set(-1 * leftDrive);
        motorlist[1].set(-1 * leftDrive);
        motorlist[2].set(-1 * rightDrive);
        motorlist[3].set(-1 * rightDrive);
        
        
        
        firstRun = false;
        
        //if necessary for some reason, stop acq code
        //NIVision.IMAQdxStopAcquisition(camSes0);
        //NIVision.IMAQdxStopAcquisition(camSes1);

    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
  	
    }   
}
