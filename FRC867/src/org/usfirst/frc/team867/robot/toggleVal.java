/**
 * 
 */
package org.usfirst.frc.team867.robot;

/**
 * @author atoge
 *
 */
public class toggleVal {
	
	boolean currentState;
	boolean toggleEnabled = true;
	
	public toggleVal(boolean initialValue)
	{
		this.currentState = initialValue;
	}
	
	public void checkForToggle(boolean trigger)
	{
		if(trigger)
		{
			if(toggleEnabled)
			{
				currentState = !currentState;	//activate lift, if button is still held down, it will not toggle anymore, and will not re-enable toggle
				toggleEnabled = false; //on first press, disable toggle
			}
		}
		else //when button is finally released, re-enable toggle
		{
			toggleEnabled = true;
		}
		
	}
	
	public boolean get()
	{
		return currentState;
	}
	
	public void set(boolean setVal)
	{
		currentState = setVal;
	}
}
