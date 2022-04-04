package io.github.contractautomata.catlib.family.converters;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.github.contractautomata.catlib.family.Family;
import io.github.contractautomata.catlib.family.Feature;
import io.github.contractautomata.catlib.family.Product;

/**
 * Class implementing import/export of products generated by FeatureIDE in family
 * @author Davide Basile
 *
 */
public class FeatureIDEfamilyConverter implements FamilyConverter {

	/**
	 * loads the list of products generated through FeatureIDE
	 *
	 * @param filename the full path filename of model.xml FeatureIDE model file
	 * @return the imported set of products
	 * @throws IOException   io exception
	 * @throws SAXException  sax exception
	 * @throws ParserConfigurationException  parser exception
	 */
	@Override
	public Set<Product> importProducts(String filename) throws ParserConfigurationException, SAXException, IOException
	{
		File inputFile = new File(getSafeFileName(filename));
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		// to be compliant, completely disable DOCTYPE declaration:
		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		//doc.getDocumentElement().normalize();

		Set<String> features=parseFeatures(doc);
		String[][] eq = detectDuplicates(doc);


		for (String[] strings : eq) {
			if (features.contains(strings[0]))
				features.remove(strings[1]);
		}

		String safefilename=getSafeFileName(filename);

		File folder = new File(safefilename.substring(0, safefilename.lastIndexOf(File.separator))+File.separator+"products"+File.separator);

		File[] listFiles = folder.listFiles();
		if (listFiles==null)
			return Collections.emptySet();

		List<File> listOfFiles = Arrays.asList(listFiles);

		return listOfFiles.parallelStream()
				.map(f->{
					if (f.isFile()&&f.getName().contains("config"))
						return f.getAbsolutePath();//no sub-directory on products
					if (f.isDirectory())
					{
						File[] ff = f.listFiles();
						if (Objects.requireNonNull(ff).length>=1
								&& ff[0].isFile()
								&& ff[0].getName().contains("config"))//each product has its own sub-directory
							return ff[0].getAbsolutePath();
					}
					return "";
				})
				.filter(s->s.length()>0)
				.map(s->{
					try {
						return Files.readAllLines(Paths.get(s), StandardCharsets.UTF_8);//required features
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				})
				.map(l->new Product(features.parallelStream()
						.filter(l::contains)//required
						.map(Feature::new)
						.collect(Collectors.toSet()),
						features.parallelStream()
								.filter(s->!l.contains(s))//forbidden
								.map(Feature::new)
								.collect(Collectors.toSet())))
				.collect(Collectors.toSet());
	}


	private Set<String> parseFeatures(Document doc)
	{

		NodeList nodeList = doc.getElementsByTagName("feature");

		Set<String> features=new HashSet<>();
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node nNode = nodeList.item(i);
			//in this format, nodes tagged with "feature" are always of type Node.ELEMENT_NODE
			features.add(((Element) nNode).getAttribute("name"));
		}
		return features;
	}

	/**
	 * reads all iff constraints (eq node) and returns a table such that forall i table[i][0] equals table[i][1]
	 */
	private String[][] detectDuplicates(Document doc)
	{
		NodeList nodeList = doc.getElementsByTagName("eq");

		String[][] table= new String[nodeList.getLength()][2]; //exact length

		int ind =0;
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node nNode = nodeList.item(i);
			//in this format, nodes tagged with "eq" are always of type Node.ELEMENT_NODE
			NodeList childs = nNode.getChildNodes();
			Node first = childs.item(1);
			Node second = childs.item(3);
			table[ind][0]= first.getTextContent();
			table[ind][1]= second.getTextContent();
			ind++;
		}
		return table;
	}

	private String getSafeFileName(String filename) {
		Path path = FileSystems.getDefault().getPath(filename);
		return path.toString();
	}


	@Override
	public void exportFamily(String filename, Family fam) {
		throw new UnsupportedOperationException();
	}

}
