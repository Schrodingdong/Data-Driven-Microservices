package com.datadrivenmicroservices.ontologicaldiscoveryservice.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

@Component
public class RdfListener {
    @Value("${rdf.order.file-path}")
    private String orderFilePath;
    @Value("${rdf.product.file-path}")
    private String productFilePath;
    @Value("${rdf.customer.file-path}")
    private String customerFilePath;

    @RabbitListener(queues = MQConfig.ORDER_RDF_QUEUE)
    public void listenOrderQueue(String xmlString){
        saveRdfFile(xmlString, orderFilePath);
    }

    @RabbitListener(queues = MQConfig.PRODUCT_RDF_QUEUE)
    public void listenProductQueue(String xmlString){
        saveRdfFile(xmlString, productFilePath);
    }

    @RabbitListener(queues = MQConfig.CUSTOMER_RDF_QUEUE)
    public void listenCustomerQueue(String xmlString){
        saveRdfFile(xmlString, customerFilePath);
    }

    private void saveRdfFile(String xmlString, String filePath){
        File f = new File(filePath);
        try {
            FileOutputStream out = new FileOutputStream(f);
            out.write(xmlString.getBytes());
            // Create a DocumentBuilder
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // Parse the XML string
            InputSource inputSource = new InputSource(new StringReader(xmlString));
            Document document = documentBuilder.parse(inputSource);

            saveAsFormattedXmlFile(document, filePath);
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            System.err.println("File not found : " + e.getMessage());
            try {
                System.out.println("Creating file : " + f.getPath());
                f.createNewFile();
            } catch (IOException ex) {
                System.err.println("Error creating file : " + ex.getMessage());
            }
        }

    }

    private void saveAsFormattedXmlFile(Document document, String filePath) throws TransformerException {
        // Create a Transformer
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        // Set output properties to indent and format the XML
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}line-separator", "\n");

        // Create a DOMSource from the Document
        DOMSource source = new DOMSource(document);

        // Create a StreamResult to write the XML to a file
        StreamResult result = new StreamResult(new File(filePath));

        // Transform the DOMSource to the StreamResult
        transformer.transform(source, result);
    }
}
