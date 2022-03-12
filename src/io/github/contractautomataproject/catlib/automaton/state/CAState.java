package io.github.contractautomataproject.catlib.automaton.state;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing a state of a Contract Automaton
 * 
 * @author Davide Basile
 * 
 */
public class CAState extends State<List<BasicState<String>>> {

	/**
	 * Construct a new CAState from
	 * - a list of BasicStates
	 * - a list of CAStates by flattening them into  a list of basic states
	 * @param lstate states the list of castates or basicstates
	 */
	@SuppressWarnings("unchecked")
	public <T extends State<?>> CAState(List<T> lstate)
	{
		super((lstate.get(0) instanceof CAState)?
				lstate.stream()
				.map(s->(CAState) s)
				.map(CAState::getState)
				.reduce(new ArrayList<BasicState<String>>(), (x,y)->{x.addAll(y); return x;})
				:(lstate.get(0) instanceof BasicState<?>) && (lstate.get(0).getState() instanceof String)?
					lstate.stream()
					.map(s-> (BasicState<String>)s)
					.collect(Collectors.toList())
					:null);
		if (lstate.isEmpty())
			throw new IllegalArgumentException();
	}

//	/**
//	 * Construct a new CAState from a list of CAStates by flattening them into 
//	 * a list of basic states
//	 * @param states the list of castates
//	 */
//	public CAState(List<CAState> states)
//	{
//		super(states.stream()
//		.map(CAState::getState)
//		.reduce(new ArrayList<BasicState>(), (x,y)->{x.addAll(y); return x;}));
//		
////		this.x=0;
////		this.y=0;
//	}


	@Override
	public Integer getRank() {
		return this.getState().size();
	}

	@Override
	public boolean isInitial() {
		return this.getState().stream().allMatch(BasicState<String>::isInitial);
	}
	
	public void setInitial(boolean initial) {
		this.getState().forEach(s->s.setInitial(initial));
	}

	@Override
	public boolean isFinalstate() {
		return this.getState().stream().allMatch(BasicState<String>::isFinalstate);
	}
	
	public void setFinalstate(boolean fin) {
		this.getState().forEach(s->s.setFinalstate(fin));
	}
	
	@Override
	public  List<BasicState<String>> getState() {
		return new ArrayList<>(super.getState());
		//return super.getState();
	}

	/**
	 * 
	 * @return an encoding of the object as comma separated values
	 */
	@Override
	public String toCSV()
	{
		return "[state=["+this.getState().stream()
		.map(bs->bs.toCSV())
		.collect(Collectors.joining())+"]]";
	}


	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (this.isInitial())
			sb.append(" Initial ");
		if (this.isFinalstate())
			sb.append(" Final ");

		sb.append(this.getState().toString());

		return sb.toString();
	}
}
	
// equals could cause errors of duplication of states in transitions to go undetected. 	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		CAState other = (CAState) obj;
//		if ((this.getState() == null)&&(other.getState() != null))
//				return false;
//		return (this.getState().equals(other.getState())); 
//	}
	
//	/**
//	 * this method shall not be invoked, because BasicStates are usually shared 
//	 * with other CAStates
//	 */
//	@Override
//	public CAState getCopy() {
//		return new CAState(new ArrayList<>(this.getState()));
//	}

	
//	public boolean hasSameBasicStateLabelsOf(CAState s) {
//		if (s.getState().size()!=this.state.size())
//				return false;
//		return IntStream.range(0, this.state.size())
//		.allMatch(i->state.get(i).getLabel().equals(s.getState().get(i).getLabel()));
//	}


