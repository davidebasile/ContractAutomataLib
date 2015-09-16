package CA;
import java.util.Arrays;

import FSA.FSA;
import FSA.Simulator;
import FSA.Transition;


/**
 * @author Davide Basile
 *
 */
@SuppressWarnings("serial")
public class CA  extends FSA implements java.io.Serializable
{
	private int rank;
	private int[] initial;
	private int[] states;
	private int[][] finalstates; 
	//private CATransition[] tra;
	private static String message = "*** CA simulator ***\n";
	
	/**
	 * Invoke the super constructor and take in input the added new parameters of the automaton
	 */
	public CA()
	{
		super(message);
		try{
			System.out.println();
	        this.rank = 1;
	        this.states = new int[1];
	        this.states[0] = super.getStates();
	        this.initial = new int[1];
	        initial[0] = super.getInitial();
	        finalstates = new int[1][super.getFinalStates().length];
	        finalstates[0]= super.getFinalStates();
	       // this.tra=(CATransition[])super.getTransition();
	        super.write(this);
		}
		catch (Exception e){System.out.println("Errore inserimento");}
	}
	
	public CA(int rank, int[] initial, int[] states, int[][] finalstates,CATransition[] tra)
	{
		super(tra);
		this.rank=rank;
		this.initial=initial;
		this.states=states;
		this.finalstates=finalstates;
	}
	
	/**
	 * Print in output a description of the automaton
	 */
	public void print()
	{
		//super.print();
		/**
		 * Print in output a description of the automaton
		 */
		System.out.println("Contract automaton:");
		System.out.println("Rank: "+this.rank);
		System.out.println("Number of states: "+Arrays.toString(this.getStatesCA()));
		System.out.println("Initial state: " +Arrays.toString(this.getInitialCA()));
		System.out.print("Final states: [");
		for (int i=0;i<finalstates.length;i++)
			System.out.print(Arrays.toString(finalstates[i]));
		System.out.print("]\n");
		//System.out.println("Transitions: "+Arrays.toString(this.getTransition()));		
		System.out.println("Transitions: \n");
		Transition[] t = this.getTransition();
		for (int i=0;i<t.length;i++)
			System.out.println(t[i].toString());		
	}
	
	
	
	/**
	 * Create an instance of the simulator for an FMA
	 */
	protected Simulator createSim()
	{
		//return new FMASimulator(this);
		return null;
	}
	
	/**
	 * 
	 * @param i		the index of the transition to be showed as a message to the user
	 * @return		a new Transition for this automaton
	 */
	protected Transition createTransition(int i)
	{
		return new CATransition(i);
	}
	
	
	/**
	 * 
	 * @return	the array of final states
	 */
	public int[][] getFinalStatesCA()
	{
		return finalstates;
	}
	
	/**
	 * 
	 * @return	the array of states
	 */
	public int[] getStatesCA()
	{
		return states;
	}
	
	/**
	 * 
	 * @return	the array of initial states
	 */
	public int[] getInitialCA()
	{
		return initial;
	}
	
	/**
	 * 
	 * @return the rank of the Contract Automaton
	 */
	public int getRank()
	{
		return rank;
	}
	
	/**
	 * 
	 * @return	the array of transitions
	 */
	public CATransition[] getTransition()
	{
		Transition[] temp = super.getTransition();
		CATransition[] t = new CATransition[temp.length];
		for (int i=0;i<temp.length;i++)
				t[i]=(CATransition)temp[i];
		return t;
	}
	
	public int sumStates()
	{
		int numstates=0;
		for (int i=0;i<states.length;i++)
		{
			numstates+=states[i];
		}
		return numstates;
	}
	
	public int prodStates()
	{
		int prodstates=1;
		for (int i=0;i<states.length;i++)
		{
			prodstates*=states[i];
		}
		return prodstates;
	}
	
	/**
	 * compute the product automaton of the CA given in aut
	 * @param aut the operands of the product
	 * @return the composition of aut
	 */
	public static CA product(CA[] aut)
	{

		if (aut.length==1)
			return aut[0];
		/**
		 * compute rank, states, initial states, final states
		 */
		int prodrank =0;
		for (int i=0;i<aut.length;i++)
		{
			prodrank = prodrank+(aut[i].getRank()); 
		}
		int[] statesprod = new int[prodrank];
		int[][] finalstatesprod = new int[prodrank][];
		int[] initialprod = new int[prodrank];
		int totnumstates=0;
		int pointerprodrank=0;
		for (int i=0;i<aut.length;i++)
		{
			for (int j=0;j<aut[i].getRank();j++)
			{
				statesprod[pointerprodrank]= aut[i].getStatesCA()[j];		
				totnumstates += statesprod[pointerprodrank];
				finalstatesprod[pointerprodrank] = aut[i].getFinalStatesCA()[j];
				initialprod[pointerprodrank] = aut[i].getInitialCA()[j];
				pointerprodrank++;
			}
		}
		
		/**
		 * compute transitions, non associative
		 * 
		 * scan all pair of transitions, if there is a match
		 * then generate the match in all possible context		 
		 * it generates also the independent move, then clean from invalid transitions 
		 */
		Transition[][] prodtr = new CATransition[aut.length][];
		int trlength = 0;
		for(int i=0;i<aut.length;i++)
		{
			prodtr[i]= aut[i].getTransition();
			trlength += prodtr[i].length;
		}
		Transition[] transprod = new CATransition[trlength*(trlength-1)*totnumstates]; //upper bound to the total transitions
		int pointertemp = 0;
		int pointertransprod = 0;
		boolean match=false;
		for (int i=0;i<prodtr.length;i++)
		{
			Transition[] t = prodtr[i];
			for (int j=0;j<t.length;j++)
			{
				CATransition[][] temp = new CATransition[trlength*(trlength-1)][];
				Transition[] trtemp = new CATransition[trlength*(trlength-1)];//stores the other transition involved in the match in temp
				pointertemp=0; //reinitialized each new transition
				for (int ii=0;ii<prodtr.length;ii++)
				{
					if (ii!=i)
					{
						Transition[] tt = prodtr[ii];
						for (int jj=0;jj<tt.length;jj++)
						{
							if (match( ((CATransition)t[j]).getLabelP() ,((CATransition) tt[jj]).getLabelP() )) //match found
							{
								match=true;
								CATransition[] gen;
								if (i<ii)
									 gen = generateTransitions(t[j],tt[jj],i,ii,aut);
								else
									gen = generateTransitions(tt[jj],t[j],ii,i,aut);
								temp[pointertemp]=gen; //temp is temporary used for comparing matches and offers/requests
								trtemp[pointertemp]=tt[jj];
								pointertemp++;
								for (int ind=0;ind<gen.length;ind++)
								{
									boolean copy=true;
									for (int ind2=0;ind2<pointertransprod;ind2++)
									{
										if (transprod[ind2].equals(gen[ind]))
										{
											copy=false;
											break;
										}
									}
									if(copy)
									{
										transprod[pointertransprod]=gen[ind]; 
										//copy all the matches in the transition of the product automaton, if not already in !
										pointertransprod++;
									}
								}
							}
						}
					}
				}
				CATransition[] gen = generateTransitions(t[j],null,i,-1,aut);
				//insert only valid transitions of gen, that is a principle moves independently in a state only if it is not involved
				// in matches
					
				if ((match)&&(gen!=null))		
				{
					/**
					 * extract the first transition of gen to check the principal who move 
					 * and its state 
					 */
					CATransition tra = gen[0];
					int[] lab = tra.getLabelP(); 
					int pr1=-1;
					for (int ind2=0;ind2<lab.length;ind2++)
					{
						if (lab[ind2]!=0)
						{
							pr1=ind2; //principal
						}
					}
					int label = tra.getLabelP()[pr1];  //the action of the principal who moves
					for (int ind3=0;ind3<gen.length;ind3++)
					{
						if(gen[ind3]!=null)
						{
							for (int ind=0;ind<pointertemp;ind++)
								for(int ind2=0;ind2<temp[ind].length;ind2++)
								{		
									if (Arrays.equals(gen[ind3].getInitialP(),temp[ind][ind2].getInitialP()) &&  //the state is the same
												label==temp[ind][ind2].getLabelP()[pr1]) //pr1 makes the same move
									{
												gen[ind3]=null;
									}
								}
						}	
					}
					
					/**
					 * 
					 *
					for (int ind=0;ind<pointertemp;ind++)
					{
						/**
						 * extract the first transition of temp[ind] to check the two principals who move in the match
						 * and their state (they are equal in all inner transitions)
						 *
						CATransition tra = temp[ind][0];
						int[] lab = tra.getLabelP(); 
						int pr1=-1;int pr2=-1;
						for (int ind2=0;ind2<lab.length;ind2++)
						{
							if (lab[ind2]!=0)
							{
								if (pr1==-1)
									pr1=ind2; //principal 1
								else
									pr2=ind2; //principal 2
							}
						}
						int[] source = tra.getInitialP();
						int pr1s = source [pr1];  //principal 1 state
						int pr2s = source [pr2];  //principal 2 state
						/**
						 * in all the non-match transitions generated in gen, if the two principals are in the initial state, then
						 * in that particular state the non-match transition of one of the two was not allowed and it is removed
						 *
						for (int ind2=0;ind2<gen.length;ind2++)
						{
							if(gen[ind2]!=null)
							{
								if ((gen[ind2].getInitialP()[pr1]==pr1s)&&(gen[ind2].getInitialP()[pr2]==pr2s))
									gen[ind2]=null;
							}
						}
					}
					*/					
				}
				/**
				 * finally insert only valid independent moves in transprod
				 */
				for (int ind=0;ind<gen.length;ind++)
				{
					if (gen[ind]!=null)
					{
						transprod[pointertransprod]=gen[ind];
						pointertransprod++;
					}
				}
			}
		}
		/**
		 * remove all unused space in transProd
		 */
		CATransition[] finalTr = new CATransition[pointertransprod];
		for (int ind=0;ind<pointertransprod;ind++)
			finalTr[ind]= (CATransition)transprod[ind];
		
		CA prod =  new CA(prodrank,initialprod,statesprod,finalstatesprod,finalTr);
		
		/**
		 * remove unreachable transitions  !!
		 */
		int removed=0;
		for (int ind=0;ind<finalTr.length;ind++)
		{
			CATransition t=(CATransition)finalTr[ind];
			int[] s = t.getInitialP();
			if(!amIReachable(s,prod,prod.getInitialCA(),new int[totnumstates][],0))
			{
				finalTr[ind]=null;
				removed++;
			}
		}
		
		/**
		 * remove null 
		 */
		int pointer=0;
		CATransition[] finalTr2 = new CATransition[pointertransprod-removed];
		for (int ind=0;ind<finalTr.length;ind++)
		{
			if (finalTr[ind]!=null)
			{
				finalTr2[pointer]=finalTr[ind];
				pointer++;
			}
		}
		
		return new CA(prodrank,initialprod,statesprod,finalstatesprod,finalTr2);
	}
	
	/**
	 * true if state is reachable from  from[]  in aut
	 * @param state
	 * @param aut
	 * @param visited
	 * @param pointervisited
	 * @return
	 */
	private static boolean amIReachable(int[] state, CA aut,int[] from, int[][] visited, int pointervisited)
	{
		if (Arrays.equals(state,from))
			return true;
		for (int j=0;j<pointervisited;j++)
		{
			if (visited[j]==state)
			{
				return false;
			}
		}
		visited[pointervisited]=state;
		pointervisited++;
		CATransition[] t = aut.getTransition();
		for (int i=0;i<t.length;i++)
		{
			if (t[i]!=null)
			{
				if (Arrays.equals(state,t[i].getFinalP()))
				{
					if (amIReachable(t[i].getInitialP(),aut,from,visited,pointervisited))
						return true;
				}
			}
		}
		return false;
	}
	/**
	 * check if labels l and ll are in match
	 * @param l
	 * @param ll
	 * @return true if there is a match, false otherwise
	 */
	private static boolean match(int[] l,int[] ll)
	{
		int m=-1000; int mm=-1000;
		for (int i=0;i<l.length;i++)
		{
			if (l[i]!=0)
			{
				if (m==-1000)
					m=l[i];
				else
					return false; //l is a match
			}
		}
		for (int i=0;i<ll.length;i++)
			if (ll[i]!=0)
			{
				if(mm==-1000)
					mm=ll[i];
				else
					return false; // ll is a match
			}
		return ((m+mm) == 0)&&(m!=-1000)&&(mm!=-1000); 
	}
	
	/**
	 * 
	 * @param t  first transition made by one CA
	 * @param tt second transition if it is a match, otherwise null
	 * @param i  the index of the CA whose transition is t
	 * @param ii the index of the CA whose transition is tt or -1
	 * @param aut all the CA to be in the transition
	 * @return an array of transitions where i (and ii) moves and the other stays idle in each possible state 
	 */
	private static CATransition[] generateTransitions(Transition t, Transition tt, int i, int ii, CA[] aut)
	{
		/**
		 * preprocessing to the recursive method recgen:
		 * it computes  the values firstprinci,firstprincii,numtransitions,states
		 */
		int prodrank = 0; //the sum of rank of each CA in aut, except i and ii
		int firstprinci=-1; //index of first principal in aut[i] in the list of all principals in aut
		int firstprincii=-1; //index of first principal in aut[ii] in the list of all principals in aut
		int[] states=null; //the number of states of each principal, except i and ii
		int numtransitions=1; //contains the product of the number of states of each principals, except for those of i and ii
		if (tt!= null) //if is a match
		{			
			/**
			 * first compute prodrank, firstprinci,firstprincii
			 */
			for (int ind=0;ind<aut.length;ind++)
			{
				if ((ind!=i)&&(ind!=ii))
					prodrank += (aut[ind].getRank()); 
				else 
				{
					if (ind==i)
						firstprinci=prodrank; //these values are handled inside generateATransition static method
					else 
						firstprincii=prodrank; //note that firstprinci and firstprincii could be equal
				}
					
			}
			if (prodrank!=0)
			{
				states = new int[prodrank]; 
				int indstates=0;
				//filling the array states with number of states of all principals of CA in aut except of i and ii
				for (int ind=0;ind<aut.length;ind++) 
				{
					if ((ind!=i)&&(ind!=ii))
					{
						int[] statesprinc=aut[ind].getStatesCA();
						for(int ind2=0;ind2<statesprinc.length;ind2++)
							{						
								states[indstates]=statesprinc[ind2];
								numtransitions*=states[indstates];
								indstates++;
							}
					}
				}		
			}
		}
		else	//is not a match
		{
			for (int ind=0;ind<aut.length;ind++)
			{
				if (ind!=i)
					prodrank = prodrank+(aut[ind].getRank()); 
				else if (ind==i)
					firstprinci=prodrank;					
			}
			if(prodrank!=0)
			{
				states = new int[prodrank]; //the number of states of each principal except i 
				int indstates=0;
				//filling the array states
				for (int ind=0;ind<aut.length;ind++)
				{
					if (ind!=i)
					{
						int[] statesprinc=aut[ind].getStatesCA();
						for(int ind2=0;ind2<statesprinc.length;ind2++)
							{						
								states[indstates]=statesprinc[ind2];
								numtransitions*=states[indstates];
								indstates++;
							}
					}
				}	
			}
		}
		CATransition[] tr = new CATransition[numtransitions];
		if(prodrank!=0)
		{
			int[] insert= new int[states.length];
			//initialize insert to zero in all component
			for (int ind=0;ind<insert.length;ind++)
				insert[ind]=0;
			recGen(t,tt,firstprinci, firstprincii,tr,states,0, states.length-1, insert);
		}
		else
			tr[0]=generateATransition(t,tt,0,0,new int[0]);
		return tr;
	}
	
	
	/**
	 * 
	 * recursive methods that generates all combinations of transitions with all possible states of principals that are idle 
	 * it must start from the end of array states
	 * 
	 * @param t		first transition who moves
	 * @param tt	second transition who moves or null if it is not a match
	 * @param fi	offset of first CA who moves in list of principals
	 * @param fii	offset of second CA who moves in list of principals or empty
	 * @param cat	side effect: modifies cat by adding the generated transitions
	 * @param states	the number of states of each idle principals
	 * @param indcat	pointer in the array cat
	 * @param indstates	pointer in the array states
	 * @param insert    it is used to generate all the combinations of states of idle principals, the first must be all zero
	 */
	private static void recGen(Transition t, Transition tt, int fi, int fii, CATransition[] cat,  int[] states, int indcat, int indstates, int[] insert)
	{
		/**
		 * se sono in fondo,
		 * 		se non ho raggiunto il max stati inserisco la combinazione e richiamo+1 
		 * 		se ho raggiunto il max , azzero faccio un passo indietro e richiamo
		*/
		/**
		 * se non sono in fondo
		 * 		se non ho raggiunto il max, aumento di 1 e vado in fondo
		 * 		se ho raggiunto il max, azzero e faccio un passo indietro
		 * se -1 termino
		 */
		if (indstates==-1)
			return;
		if (insert[indstates]==states[indstates])
		{
			insert[indstates]=0;
			indstates--;
			recGen(t,tt,fi,fii,cat,states,indcat,indstates,insert);
		}
		else
		{
			if (indstates==states.length-1)
			{
				cat[indcat]=generateATransition(t,tt,fi,fii,insert);
				indcat++;
				insert[indstates]++;
				recGen(t,tt,fi,fii,cat,states,indcat,indstates,insert);
			}
			else
			{
				insert[indstates]++; 
				if (insert[indstates]!=states[indstates])
					indstates=states.length-1;
				recGen(t,tt,fi,fii,cat,states,indcat,indstates,insert);				
			}
		}
	}
	
	/**
	 * 
	 * @param t				first transition to move
	 * @param tt			second transition to move only in case of match
	 * @param firstprinci  the index to start to copy the principals in t
	 * @param firstprincii the index to start to copy the principals in tt
	 * @param insert		the states of all other principals who stays idle
	 * @return				a new transition where only principals in t (and tt) moves while the other stays idle in their state given in insert[]
	 */
	private static CATransition generateATransition(Transition t, Transition tt, int firstprinci, int firstprincii,int[] insert)
	{
		if (tt!=null)
		{
			int[] s=((CATransition) t).getInitialP();
			int[] l=((CATransition) t).getLabelP();
			int[] d=((CATransition) t).getFinalP();
			int[] ss = ((CATransition) tt).getInitialP();
			int[] ll=((CATransition) tt).getLabelP();
			int[] dd =((CATransition) tt).getFinalP();
			int[] initial = new int[insert.length+s.length+ss.length];
			int[] dest = new int[insert.length+s.length+ss.length];
			int[] label = new int[insert.length+s.length+ss.length];
			int counter=0;
			for (int i=0;i<insert.length;i++)
			{
				if (i==firstprinci)
				{
					for (int j=0;j<s.length;j++)
					{
						initial[i+j]=s[j];
						label[i+j]=l[j];
						dest[i+j]=d[j];
					}
					counter+=s.length; //record the shift due to the first CA 
					i--;
					firstprinci=-1;
				}
				else 
				{
					if (i==firstprincii)
					{
						for (int j=0;j<ss.length;j++)
						{
							initial[i+counter+j]=ss[j];
							label[i+counter+j]=ll[j];
							dest[i+counter+j]=dd[j];
						}
						counter+=ss.length;//record the shift due to the second CA 
						i--;
						firstprincii=-1;
					}	
					else 
					{
						initial[i+counter]=insert[i];
						dest[i+counter]=insert[i];
						label[i+counter]=0;
					}
				}
			}
			if (firstprinci==insert.length)//case limit, the first CA was the last of aut
			{
				for (int j=0;j<s.length;j++)
				{
					initial[insert.length+j]=s[j];
					label[insert.length+j]=l[j];
					dest[insert.length+j]=d[j];
				}
				counter+=s.length; //record the shift due to the first CA 
			}
			if (firstprincii==insert.length) //case limit, the second CA was the last of aut
			{
				for (int j=0;j<ss.length;j++)
				{
					initial[insert.length+counter+j]=ss[j];
					label[insert.length+counter+j]=ll[j];
					dest[insert.length+counter+j]=dd[j];
				}
			}
			return new CATransition(initial,label,dest);	
		}
		else
		{
			int[] s=((CATransition) t).getInitialP();
			int[] l=((CATransition) t).getLabelP();
			int[] d=((CATransition) t).getFinalP();
			int[] initial = new int[insert.length+s.length];
			int[] dest = new int[insert.length+s.length];
			int[] label = new int[insert.length+s.length];
			int counter=0;
			for (int i=0;i<insert.length;i++)
			{
				if (i==firstprinci)
				{
					for (int j=0;j<s.length;j++)
					{
						initial[i+j]=s[j];
						label[i+j]=l[j];
						dest[i+j]=d[j];
					}
					counter+=s.length; //record the shift due to the first CA 
					i--;
					firstprinci=-1;
				}
				else
				{
					initial[i+counter]=insert[i];
					dest[i+counter]=insert[i];
					label[i+counter]=0;
				}
			}
			if (firstprinci==insert.length)//case limit, the first CA was the last of aut
			{
				for (int j=0;j<s.length;j++)
				{
					initial[insert.length+j]=s[j];
					label[insert.length+j]=l[j];
					dest[insert.length+j]=d[j];
				}
				counter+=s.length; //record the shift due to the first CA 
			}
			return new CATransition(initial,label,dest);	
		}
	}
	
	public CA proj(int i)
	{
		if ((i<0)||(i>rank)) //check if the parameter i is in the rank of the CA
			return null;
		CATransition[] tra = this.getTransition();
		int[] init = new int[1];
		init[0]=initial[i];
		int[] st= new int[1];
		st[0]= states[i];
		int[][] fi = new int[1][];
		fi[0]=finalstates[i];
		CATransition[] t = new CATransition[tra.length];
		int pointer=0;
		for (int ind=0;ind<tra.length;ind++)
		{
			CATransition tt= ((CATransition)tra[ind]);
			int label = tt.getLabelP()[i];
			if(label!=0)
			{
				int source =  tt.getInitialP()[i];
				int dest = tt.getFinalP()[i];
				int[] sou = new int[1];
				sou[0]=source;
				int[] des = new int[1];
				des[0]=dest;
				int[] lab = new int[1];
				lab[0]=label;
				CATransition selected = new CATransition(sou,lab,des);
				boolean skip=false;
				for(int j=0;j<pointer;j++)
				{
					if (t[j].equals(selected))
					{
						skip=true;
						break;
					}
				}
				if (!skip)
				{
					t[pointer]=selected;
					pointer++;
				}
			}
		}
		
		tra = new CATransition[pointer];
		for (int ind=0;ind<pointer;ind++)
			tra[ind]=t[ind];
		//public CA(int rank, int[] initial, int[] states, int[][] finalstates,CATransition[] tra)
		return new CA(1,init,st,fi,tra);
	}
	
	public static CA aproduct(CA[] a)
	{
		int tot=0;
		for (int i=0;i<a.length;i++)
			tot+=a[i].getRank();
		if (tot==a.length)
			return product(a);
		else
		{
			CA[] a2=new CA[tot];
			int pointer=0;
			for(int i=0;i<a.length;i++)
			{
				if(a[i].getRank()>1)
				{
					for (int j=0;j<a[i].getRank();j++)
					{
						a2[pointer]=a[i].proj(j);
						pointer++;
					}
				}
				else
				{
					a2[pointer]=a[i];
					pointer++;
				}
			}
			return product(a2);
		}
			
	}
	
	public boolean branchingCondition()
	{
		return false;
	}
	
	public boolean mixedChoice()
	{
		return false;
	}
}
