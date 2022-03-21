//package io.github.contractautomataproject.catlib.automaton.label;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.util.List;
//
//import io.github.contractautomataproject.catlib.automaton.label.action.Action;
//import io.github.contractautomataproject.catlib.automaton.label.action.OfferAction;
//import io.github.contractautomataproject.catlib.automaton.label.action.RequestAction;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//public class CMLabelTest {
//	CMLabel cm_of;
//	CMLabel cm_req;a
//
//	@Before
//	public void setup() {
//		cm_of = new CMLabel("Alice","Bob",new OfferAction("apple"));
//		cm_req = new CMLabel("Alice","Bob",new RequestAction("apple"));
//	}
//
//	@Test
//	public void testConstructor1() {
//		assertEquals(cm_of,CMLabel.parseCMLabel("Alice_Bob@!apple"));
//	}
//
//	@Test
//	public void testConstructor1Request() {
//		assertEquals(cm_req,CMLabel.parseCMLabel("Alice_Bob@?apple"));
//	}
//
////	@Test
////	public void testConstructor2() {
////		assertEquals(cm_of,new CMLabel(List.of(new Action("Alice_Bob@!apple"))));
////	}
//
//	@Test
//	public void testOffer(){
//		assertTrue(cm_of.isOffer());
//	}
//
//
//	@Test
//	public void testRequest(){
//		assertTrue(cm_req.isRequest());
//	}
//
//
//	@Test
//	public void testConstructorIdOf(){
//		assertEquals("Alice",cm_of.getId());
//	}
//
//	@Test
//	public void testConstructorPartnerOf(){
//		assertEquals("Bob", cm_of.getPartner());
//	}
//
//	@Test
//	public void testConstructorIdReq(){
//		assertEquals("Bob", cm_req.getId());
//	}
//
//
//	@Test
//	public void testConstructorPartnerReq(){
//		assertEquals("Alice", cm_req.getPartner());
//	}
//
//	@Test
//	public void testMatchTrue() {
//		assertTrue(cm_of.match(cm_req));
//	}
//
//	@Test
//	public void testMatchFalseSuper() {
//		Assert.assertFalse(cm_of.match(cm_of));
//	}
//
//	@Test
//	public void testMatchFalsePartner() {
//		Assert.assertFalse(cm_of.match(new CMLabel("Alice","Carl",new RequestAction("apple"))));
//	}
//
//	@Test
//	public void testMatchFalseId() {
//		Assert.assertFalse(cm_of.match(new CMLabel("Carl","Bob",new RequestAction("apple"))));
//	}
//
//
//	@Test
//	public void testMatchFalseReceiver() {
//		Assert.assertFalse(CMLabel.parseCMLabel("2_1@!m").match(CMLabel.parseCMLabel("2_0@?m")));
//	}
//
//
//	@Test
//	public void equalsSame() {
//		assertEquals(cm_of, cm_of);
//	}
//
//	@Test
//	public void equalsFalse() {
//		Assert.assertNotEquals(cm_of, cm_req);
//	}
//
//
//	@Test
//	public void testEqualsTrue() {
//		CMLabel equal = new CMLabel("Alice","Bob",new OfferAction("apple"));
//		assertEquals(equal, cm_of);
//	}
//
//	@Test
//	public void testEqualsFalsePartner() {
//		Assert.assertNotEquals(cm_of, new CMLabel("Alice","Carl",new OfferAction("apple")));
//	}
//
//	@Test
//	public void testEqualsFalseId() {
//		Assert.assertNotEquals(cm_of, new CMLabel("Carl","Bob",new OfferAction("apple")));
//	}
//
//	@Test
//	public void testHashCodeEquals() {
//		CMLabel equal = new CMLabel("Alice","Bob",new OfferAction("apple"));
//		assertEquals(cm_of.hashCode(),equal.hashCode());
//	}
//
//	@Test
//	public void testHashCodeNotEquals() {
//		CMLabel equal = new CMLabel("Carol","Bob",new OfferAction("apple"));
//		Assert.assertNotEquals(cm_of.hashCode(),equal.hashCode());
//	}
//
//	@Test
//	public void testToStringOffer() {
//		assertEquals("[Alice_Bob@!apple]", cm_of.toString());
//	}
//
//	@Test
//	public void testToStringRequest() {
//		assertEquals("[Alice_Bob@?apple]", cm_req.toString());
//	}
//
//	@Test
//	public void testMatchException() {
//		CALabel test = new CALabel(List.of(new RequestAction("a")));
//		Assert.assertThrows(IllegalArgumentException.class, ()->cm_of.match(test));
//	}
//
//	@Test
//	public void testConstructor1ExceptionNoSenderReceiver() {
//		Assert.assertThrows(IllegalArgumentException.class, ()->CMLabel.parseCMLabel("@?a"));
//	}
//
//	@Test
//	public void testConstructor1ExceptionNoSender() {
//		Assert.assertThrows(IllegalArgumentException.class, ()->CMLabel.parseCMLabel("_Bob@!a"));
//	}
//
//
//	@Test
//	public void testConstructor1ExceptionNoReceiver() {
//		Assert.assertThrows(IllegalArgumentException.class, ()->CMLabel.parseCMLabel("Alice_@?a"));
//	}
//
//	@Test
//	public void testConstructor1ExceptionNoPartner() {
//		Assert.assertThrows(IllegalArgumentException.class, ()->CMLabel.parseCMLabel("_Bob@?a"));
//	}
//
////	@Test
////	public void testConstructor2Exception() {
////		List<Action> test = List.of(new Action("Alice_Bob@!apple"),new Action("Alice_Bob@?apple"));
////		Assert.assertThrows(IllegalArgumentException.class, ()->new CMLabel(test));
////	}
//}
