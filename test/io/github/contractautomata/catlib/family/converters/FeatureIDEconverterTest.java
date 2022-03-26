package io.github.contractautomata.catlib.family.converters;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Set;
import java.util.function.UnaryOperator;

import org.junit.Test;

import io.github.contractautomata.catlib.family.Family;
import io.github.contractautomata.catlib.family.PartialProductGenerator;
import io.github.contractautomata.catlib.family.Product;


public class FeatureIDEconverterTest {
	private final String dir = System.getProperty("user.dir")+File.separator+"test_resources"+File.separator;	
	private final FamilyConverter ffc = new FeatureIDEfamilyConverter();
	private final FamilyConverter pfc = new ProdFamilyConverter();
	
	@Test
	public void testImportFamily() throws Exception {
		UnaryOperator<Set<Product>> spg = new PartialProductGenerator();
		Family f1= new Family(spg.apply(ffc.importProducts(dir+"FeatureIDEmodel"+File.separator+"model.xml")));
		Family f2= new Family(pfc.importProducts(dir +"ValidProducts.prod"));
		assertEquals(f1.getProducts(),f2.getProducts());
	}
	
//	@Test
//	public void testImportFamilyWithSubfolderAndException() throws Exception, ParserConfigurationException, SAXException
//	{
//		UnaryOperator<Set<Product>> spg = new PartialProductGenerator();
//		new Family(spg.apply(ffc.importProducts(dir+"FeatureIDEmodel2"+File.separator+"model.xml")));
//
////		this test provokes an IOException for covering the catch block, however nor Travis neither GithubAction do raise the throwable
//
//		final RandomAccessFile raFile = new RandomAccessFile(dir+"FeatureIDEmodel2"+File.separator+
//				"products"+File.separator+"00003.config", "rw");
//		raFile.getChannel().lock();
//		assertThatThrownBy(()->ffc.importProducts(dir+"FeatureIDEmodel2"+File.separator+"model.xml"))
//		.isInstanceOf(IllegalArgumentException.class);
//		raFile.close();
//
//	}
	
//	@Test
//	public void testImportFamilyException() throws Exception, ParserConfigurationException, SAXException
//	{
//		Family f1= 
//				new Family(ffc.importProducts(dir+"FeatureIDEmodel"+File.separator+"model.xml"));
//		System.out.println(f1.getPo());//.values().iterator().next());
//	}
	
	@Test
	public void testExportException() throws Exception
	{
		Family fam = new Family(pfc.importProducts(dir +"maximalProductsTest.prod"));
		assertThatThrownBy(() -> ffc.exportFamily("", fam))
		.isInstanceOf(UnsupportedOperationException.class);
	}
}