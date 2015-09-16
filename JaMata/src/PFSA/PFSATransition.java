package PFSA;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import FSA.Transition;



/**
 * Override transition by changing the label
 * 
 * @author Davide Basile
 *
 */
@SuppressWarnings("serial")
public class PFSATransition extends Transition implements java.io.Serializable{ 
	private int sigma;
	private int delta;
	private int Z;
	private int zeta;
	/**
	 * 
	 * @param initial	initial state
	 * @param fina		arrival state
	 * @param sigma		function sigma
	 * @param Z			function Z
	 * @param delta		function delta
	 * @param zeta		function zeta
	 */
	public PFSATransition(int initial, int fina, int sigma,  int Z, int delta, int zeta)
	{
		super(initial, -1, fina);
		this.sigma = sigma;
		this.delta = delta;
		this.Z= Z;
		this.zeta = zeta;
	}
	
	/**
	 * Create a new Transition by taking in input the user parameters
	 * 
	 * @param i the index of the transition to be showed as a message to the user
	 */
	public PFSATransition(int i)
	{
		super(i,false);
		InputStreamReader reader = new InputStreamReader (System.in);
        BufferedReader myInput = new BufferedReader (reader);
		try {
			/**
			 * 	The symbol T is converted to -1 
			 *	The square is converted to -2  
			 * 	The symbol ? is converted to -3  
			 * 
			 *  For the function delta:
			 *  The symbol square is converted to 0
			 *  The value of delta is consecutively shifted to
			 *  	+1 if is positive
			 *  	-1 if is negative
			 */
			String s;
			System.out.println("Insert value of sigma for the transition "+i);
			s=myInput.readLine();
			if (s.equals("T"))
				this.sigma=-1;
			else if (s.equals("square"))
				this.sigma=-2;
			else
				this.sigma = Integer.parseInt(s);
			System.out.println("Insert value of Z for the transition "+i);
			s=myInput.readLine();
			if (s.equals("?"))
				this.Z=-3;
			else if (s.equals("square"))
				this.Z=-2;
			else
				this.Z = Integer.parseInt(s);
			System.out.println("Insert value of delta for the transition "+i+", pay attention: write -0 for pop on register of index 0, or +0 for push on register of index 0, do the same for the other indexes.");
			s=myInput.readLine();
			if (s.equals("square"))
				this.delta=0;
			else if (s.startsWith("-"))
				this.delta = Integer.parseInt(s)-1;
			else if (s.startsWith("+"))
				this.delta = Integer.parseInt(s)+1;
			System.out.println("Insert value of Zeta for the transition "+i);
			s=myInput.readLine();
			if (s.equals("square"))
				this.zeta=-2;
			else
				this.zeta = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	/**
	 * 
	 * @return value for sigma
	 */
	public int getSigma()
	{
		return sigma;
	}
	
	/**
	 * 
	 * @return value for delta
	 */
	public int getDelta()
	{
		return delta;
	}
	
	/**
	 * 
	 * @return value for Z
	 */
	public int getZ()
	{
		return Z;
	}
	
	/**
	 * 
	 * @return value for Zeta
	 */
	public int getZeta()
	{
		return zeta;
	}
	
	/**
	 * Override
	 */
	public String toString()
	{
		String sigma,Z,delta,zeta;
		if (this.getSigma()==-2)
			sigma="[]";
		else if (this.getSigma()==-1)
			sigma="T";
		else
			sigma=Integer.toString(this.getSigma());
		if (this.getZ()==-2)
			Z="[]";
		else if (this.getZ()==-3)
			Z="?";
		else 
			Z= Integer.toString(this.getZ());
		if (this.getDelta()==0)
			delta="[]";
		else if(this.getDelta()==-1)
			delta="-0";
		else if (this.getDelta()<-1)
			delta = Integer.toString(this.getDelta()+1);
		else if (this.getDelta()==1)
			delta ="+0";
		else 
			delta = "+"+ Integer.toString(this.getDelta()-1);
		
		if (this.getZeta()==-2)
			zeta="[]";
		else
			zeta = Integer.toString(this.getZeta());
			
		return "("+super.getInitial()+","+sigma+","+Z+","+delta+","+zeta+","+super.getFinal()+")";
	}
	
}
