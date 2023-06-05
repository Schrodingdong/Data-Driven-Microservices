package com.datadrivenmicroservices.ontologicaldiscoveryservice.ontology;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

@Service
public class OntologyMatching {

    /**
 * Match the request with the right ontology
 * */
    static OntologyMatching ontologyMatching = new OntologyMatching();



    //main for testing purpose
    public static void main(String[] args ){

        //ontologyMatching.parseAndExtractKeywords("C:\\Users\\hp\\Desktop\\Data-Driven-Microservices\\microservices\\ontological-discovery-service\\src\\main\\resources\\customer-sample.rdf");
        String jsonString = "{\"customerFirstName\": \"John Doe\", \"customerLastName\": 30, \"customerEmail\": \"New York\", \"customerRegistrationDate\":\"idk\"}";
        //System.out.println(ontologyMatching.getKeysFromJsonString(jsonString));
        System.out.println(ontologyMatching.matchRequestWithOntology(jsonString));
    }




    //get keywords of the request
    public HashSet<String> getKeysFromJsonString(String jsonString) {
        HashSet<String> keys = new HashSet<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> iterator = jsonObject.keys();

            while (iterator.hasNext()) {
                String key = iterator.next();
                keys.add(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return keys;
    }


    //parsing the RDF files and extracting the keywords
    public HashSet<String> parseAndExtractKeywords(File ontologyFile) {
        try {
            // Create a DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            HashSet<String> keywords = new HashSet<>();
            // Parse the RDF file
            Document document = builder.parse(ontologyFile);

            // Get the root element
            Element root = document.getDocumentElement();

            // Find all rdf:Description elements that represent the ontology metadata
            NodeList descriptionNodes = root.getElementsByTagName("rdf:Description");


            for (int i = 0; i < descriptionNodes.getLength(); i++) {
                Node descriptionNode = descriptionNodes.item(i);

                // Get the rdf:about attribute value
                String about = descriptionNode.getAttributes().getNamedItem("rdf:about").getNodeValue();

                // Check if the rdf:about value is related to the ontology itself
                if ((about.startsWith("http://www.datadrivenmicroservices.com/pfa/ontology/Product#")) ||
                (about.startsWith("http://www.datadrivenmicroservices.com/pfa/ontology/customer#")) ||
                        (about.startsWith("http://www.datadrivenmicroservices.com/pfa/ontology/order#"))
                ) {

                    // Print the rdf:about value
                    //System.out.println("rdf:about: " + about);

                    // Extract keywords from the rdf:type value
                    String[] typeParts = about.split("#");
                    String keyword = typeParts[typeParts.length - 1];
                   // System.out.println("Keyword: " + keyword);

                    keywords.add(keyword);
                    //System.out.println(keywords);

                    // Print the rdf:type value if available
                    NodeList typeNodes = ((Element) descriptionNode).getElementsByTagName("rdf:type");
                    if (typeNodes.getLength() > 0) {
                        Node typeNode = typeNodes.item(0);
                        String type = typeNode.getAttributes().getNamedItem("rdf:resource").getNodeValue();
                       // System.out.println("rdf:type: " + type);



                    }
                }
            }
            return keywords; //return the set of keywords
        } catch (ParserConfigurationException | IOException | SAXException e) {//exceptions that are usually encountered ive found
            e.printStackTrace();
        }
        return null;
    }



    public String matchRequestWithOntology(String jsonRequest){
        //List<File> fileCollection = new ArrayList<>();

        Map<String, File> fileCollection = new HashMap<>();
        Map<String, HashSet<String>> filesKeywords = new HashMap<>();

        // get the collection of relevant keywords of the request
        HashSet<String> requestKeys = getKeysFromJsonString(jsonRequest);

        // get the collection of relevant keywords for each ontology
        File customerOntology = new File("src\\main\\resources\\customer-sample.rdf");
        File productOntology = new File("src\\main\\resources\\product-sample.rdf");
        File orderOntology = new File("src\\main\\resources\\order-sample.rdf");

        fileCollection.put("customer", customerOntology);
        fileCollection.put("product",productOntology);
        fileCollection.put("order", orderOntology);




        //map the keywords sets with the according service [fileCollection contains the RDF files with their service names]
        for (Map.Entry<String, File> entry : fileCollection.entrySet()) {
            filesKeywords.put(entry.getKey(), parseAndExtractKeywords(entry.getValue()));
        }



        //compare the two collections
        for(Map.Entry<String, HashSet<String>> entry : filesKeywords.entrySet()){
           if( entry.getValue().equals(requestKeys)){
               return entry.getKey(); //return the service name that has a matching ontology
           }
        }

        return "no service has an ontology that matches the request";
    }
}
