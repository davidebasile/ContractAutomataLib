package io.github.davidebasile.contractautomatatest.operatorsTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.github.davidebasile.contractautomata.automaton.ModalAutomaton;
import io.github.davidebasile.contractautomata.automaton.label.CALabel;
import io.github.davidebasile.contractautomata.converters.DataConverter;
import io.github.davidebasile.contractautomata.operators.CompositionSpecCheck;
import io.github.davidebasile.contractautomata.operators.MSCACompositionFunction;
import io.github.davidebasile.contractautomata.operators.OrchestrationSynthesisOperator;
import io.github.davidebasile.contractautomata.requirements.Agreement;
import io.github.davidebasile.contractautomatatest.MSCATest;

public class CompositionTest {

	private final String dir = System.getProperty("user.dir")+File.separator+"test_resources"+File.separator;
	private final DataConverter bdc = new DataConverter();

	//***********************************testing impl against spec on scenarios **********************************************
	
	
	@Test
	public void scico2020Test() throws Exception{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"Hotel.data"));
		aut.add(bdc.importMSCA(dir+"EconomyClient.data"));
		assertTrue(new CompositionSpecCheck().test(aut,new MSCACompositionFunction(aut).apply(null,100)));
	}

	@Test
	public void lmcs2020Test() throws Exception{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"Client.data"));
		aut.add(bdc.importMSCA(dir+"Client.data"));
		aut.add(bdc.importMSCA(dir+"Broker.data"));
		aut.add(bdc.importMSCA(dir+"HotelLMCS.data"));
		aut.add(bdc.importMSCA(dir+"PriviledgedHotel.data"));
		assertTrue(new CompositionSpecCheck().test(aut,new MSCACompositionFunction(aut).apply(null,100)));
	}



	@Test
	public void lmcs2020Test2() throws Exception{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"Client.data"));
		aut.add(bdc.importMSCA(dir+"PriviledgedClient.data"));
		aut.add(bdc.importMSCA(dir+"Broker.data"));
		aut.add(bdc.importMSCA(dir+"HotelLMCS.data"));
		aut.add(bdc.importMSCA(dir+"HotelLMCS.data"));
		assertTrue(new CompositionSpecCheck().test(aut,new MSCACompositionFunction(aut).apply(null,100)));
	}
	
	//**********************************SCICO2020 case study*******************************************************************


	@Test
	public void compositionTestSCP2020_BusinessClientxHotel_open() throws Exception {
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"Hotel.data"));

		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(null,100);
		ModalAutomaton<CALabel> test = bdc.importMSCA(dir+"BusinessClientxHotel_open.data");
		assertEquals(MSCATest.checkTransitions(comp,test),true);
	}

	@Test
	public void compositionTestSCP2020_nonassociative() throws Exception {
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"BusinessClientxHotel_open.data"));

		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(null,100);
		assertEquals(new OrchestrationSynthesisOperator(new Agreement()).apply(comp),null);
	}

	@Test
	public void compositionTestSCP2020_BusinessClientxHotel_closed() throws Exception {
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"Hotel.data"));

		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(l->l.isRequest(),100);
		ModalAutomaton<CALabel> test = bdc.importMSCA(dir+"BusinessClientxHotel_closed.data");
		assertEquals(MSCATest.checkTransitions(comp,test),true);
	}



	@Test
	public void compositionTestSCP2020_BusinessClientxHotelxEconomyClient_open_transitions() throws Exception {
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"Hotel.data"));
		aut.add(bdc.importMSCA(dir+"EconomyClient.data"));
		ModalAutomaton<CALabel> comp = new MSCACompositionFunction(aut).apply(null,100);
		ModalAutomaton<CALabel> test= bdc.importMSCA(dir+"BusinessClientxHotelxEconomyClient.data");
		assertEquals(MSCATest.checkTransitions(comp,test),true);	
	}

	@Test
	public void compAndOrcTestSCP2020_BusinessClientxHotelxEconomyClient() throws Exception
	{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"BusinessClient.data"));
		aut.add(bdc.importMSCA(dir+"Hotel.data"));
		aut.add(bdc.importMSCA(dir+"EconomyClient.data"));
		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(l->l.isRequest(),100);

		ModalAutomaton<CALabel> test= bdc.importMSCA(dir+"Orc_(BusinessClientxHotelxEconomyClient).data");
		assertEquals(MSCATest.checkTransitions(new OrchestrationSynthesisOperator(new Agreement()).apply(comp),test),true);

		//		assertEquals(comp.orchestration().getNumStates(),14);
	}	

	///////////////

	@Test
	public void compTestSimple() throws Exception
	{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"A.data"));
		aut.add(bdc.importMSCA(dir+"B.data"));

		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(null,100);
		ModalAutomaton<CALabel> test = bdc.importMSCA(dir+"(AxB).data");

		assertEquals(MSCATest.checkTransitions(comp,test),true);
	}


	@Test
	public void compTestEmptySimple() throws Exception
	{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"forNullClosedAgreementComposition.data"));
		aut.add(bdc.importMSCA(dir+"forNullClosedAgreementComposition.data"));

		ModalAutomaton<CALabel> comp=new MSCACompositionFunction(aut).apply(l->l.isRequest(),100);

		assertEquals(comp,null);
	}

	@Test
	public void compTestBound_noTransitions() throws Exception
	{
		List<ModalAutomaton<CALabel>> aut = new ArrayList<>(2);

		aut.add(bdc.importMSCA(dir+"forNullClosedAgreementComposition.data"));
		aut.add(bdc.importMSCA(dir+"forNullClosedAgreementComposition.data"));

		assertThatThrownBy(() -> new MSCACompositionFunction(aut).apply(null,0))
		.isInstanceOf(IllegalArgumentException.class)
		.hasMessageContaining("No transitions");
	}

}


