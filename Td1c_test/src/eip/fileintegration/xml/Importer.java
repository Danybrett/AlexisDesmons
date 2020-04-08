package eip.fileintegration.xml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author ernestoexposito
 */
public class Importer implements ErrorHandler {
    // the DOM structure representingthe imported XML document
    private Document doc;
    // the file system document name
    private String filename;
    // constants for SCHEMA validation
    public static final String 
        JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    public static final String
        W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    // the list of all the <Product> elements
    private NodeList elements;
    // the current element being imported
    private int nbElement = 0;
    // a flag of the potential detected erros
    private boolean errorDetected =false;

    public Importer(String filename) throws Exception {
        this.filename = filename;
        // a builder factory pattern
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // the factory is configured to check XML namespaces
        factory.setNamespaceAware(true);
        // the XSD validation is activated
        factory.setValidating(true);
        // The attributes for XSD declaration are added
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        // a document builder is created from the configured factory
        DocumentBuilder builder = factory.newDocumentBuilder();
        // in case of errors during the document parsing, the error handler methods
        // implemented by the importer will be invoked
        builder.setErrorHandler(this);
        // the XML document is parsed and if no errors are found
        // the DOM tree will be populated
        doc = builder.parse(new File(filename));
        // the root <Products> element is retrieved
        Element rootElement = (Element) doc.getElementsByTagName("Products").item(0);
        // all the <Product> elements that are part of the root element are retrieved
        elements = rootElement.getElementsByTagName("Product");
    }

    public Object importObject() {
        Product result = null;
        // if there are elements to be imported and no errors are detected
        if (elements != null && !errorDetected)
            // a new empty Product is created
            result = new Product(0, "", 0, 0);
            //  a <Product> element is retrived at the nbElement position
            Element currentElement = (Element) elements.item(nbElement++);
            // the <ProductId> element is retrieved and its value stored into the product object
            Element e = (Element) currentElement.getElementsByTagName("ProductId").item(0);
            result.setProductId(Integer.parseInt(e.getTextContent()));
            // the <ProductDescription> element is retrieved and its value stored into the product object
            e = (Element) currentElement.getElementsByTagName("ProductDescription").item(0);
            result.setProductDescription(e.getTextContent());
            // the <ProductPrice> element is retrieved and its value stored into the product object
            e = (Element) currentElement.getElementsByTagName("ProductPrice").item(0);
            result.setProductPrice(Float.parseFloat(e.getTextContent()));
            // the <ProductAmount> element is retrieved and its value stored into the product object
            e = (Element) currentElement.getElementsByTagName("ProductAmount").item(0);
            result.setProductAmount(Integer.parseInt(e.getTextContent()));
            // if this is the last element, the list is initialized to null
            if (nbElement >= elements.getLength()) {
                elements = null;
            }
       
        // the product object is returned
        return result;
    }
    
     public void close() {
        // the imported file is deleted
        File f = new File(filename);
        f.delete();
    }
     
     // the error handler interface method are implemented\
    // in our case the identified warning o errors are printed
    // in case of error, the errorDetect flag is activated 
    // to avoid continuing importing the XML document
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("Warning : " + exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        System.out.println("Error! XML file will not be used : " + exception);
        errorDetected = true;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("Fatal Error! XML file will not be used : " + exception);
         errorDetected = true;
    }
}
