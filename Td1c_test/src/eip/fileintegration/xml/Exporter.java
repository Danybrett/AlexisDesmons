package eip.fileintegration.xml;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/*
/**
 *
 * @author ernestoexposito
 */
public class Exporter {

    // File system document name
    private String filenane;
    // DOM tree structure of the document to be created
    private Document doc;
    // Root of the DOM tree
    private Element rootElement;
    // Various constant strings allowing to add xML declarations
    private static final String decl11 = "xmlns:xsi";
    private static final String decl12 = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String decl21 = "xsi:noNamespaceSchemaLocation";
    private static final String decl22 = "http://eexposit.perso.univ-pau.fr/content/xsd/Product.xsd";
    
    public Exporter(String filename) throws Exception {
        this.filenane = filename;
        // Document builder factory pattern
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        // The factory allows to create a document builder
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // The document builder allows to create an Empty DOM document
        doc = dBuilder.newDocument();
        // Product elements will be created and it will be referenced as the root of the DOM tree
        rootElement = doc.createElement("Products");
        // The empty document will be added to the empty DOM tree
        doc.appendChild(rootElement);
        // Two attributes will be added to the Product element
        // The xmlns:xsi attribute and its value
        rootElement.setAttribute(decl11, decl12);
        // The xsi:noNamespaceSchemaLocation attribute and its value
        rootElement.setAttribute(decl21, decl22);
    }

    public void exportObject(Object object) {
        Product data = (Product) object;
        // Product element
        Element product = doc.createElement("Product");
        rootElement.appendChild(product);
        // ProductID element
        Element productId = doc.createElement("ProductId");
        productId.appendChild(doc.createTextNode(Integer.toString(data.getProductId())));
        product.appendChild(productId);
        // ProductDescription element
        Element productDesc = doc.createElement("ProductDescription");
        productDesc.appendChild(doc.createTextNode(data.getProductDescription()));
        product.appendChild(productDesc);
        // ProductPrice element
        Element productPrice = doc.createElement("ProductPrice");
        productPrice.appendChild(doc.createTextNode(Float.toString(data.getProductPrice())));
        product.appendChild(productPrice);
        // ProductAmount element
        Element productAmount = doc.createElement("ProductAmount");
        productAmount.appendChild(doc.createTextNode(Integer.toString(data.getProductAmount())));
        product.appendChild(productAmount);
    }
    
    public void close() {
        // Transformer factory pattern
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // The factory allows to create a tranformer
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            // The transformer is configured to use UTF-8 encoding type
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // To indent the document
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // To produce an xml format
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            // A DOM source object is created based on the previously created DOM document
            DOMSource source = new DOMSource(doc);
            // A stream result is instanciated as an empty file using the provided filename
            StreamResult result = new StreamResult(new File(filenane));
            // Finally the transformer will transform the DOM to an XML file
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            System.out.println("Error");
        } catch (TransformerException ex) {
            System.out.println("Error");
        }
    }

}

