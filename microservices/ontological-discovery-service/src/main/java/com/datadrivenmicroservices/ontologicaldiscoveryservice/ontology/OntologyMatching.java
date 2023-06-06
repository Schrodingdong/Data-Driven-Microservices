package com.datadrivenmicroservices.ontologicaldiscoveryservice.ontology;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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

/**
 * Match the request with the right ontology
 * */
@Service
public class OntologyMatching {
    public static final List<String> ONTOLOGY_LIST = Arrays.asList("customer", "order", "product");
    @Value("${rdf.product.file-path}")
    private String productOntologyPath;
    @Value("${rdf.customer.file-path}")
    private String customerOntologyPath;
    @Value("${rdf.order.file-path}")
    private String orderOntologyPath;


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



    public String matchRequestWithOntology(String jsonRequest, Map<String, String> requestParams, String requestMethod){
        if (jsonRequest.equals("{}")) {
            // meaning it's either a get or a delete request
            // we will have to analyse the request params
            // and match them with the right ontology
            String key = (String) requestParams.keySet().toArray()[0];
            String value = requestParams.get(key);
            System.out.println("key: " + key + " value: " + value);
            if(requestMethod.equals("GET") || requestMethod.equals("DELETE")){
                for (String s : ONTOLOGY_LIST){
                    if(key.contains(s)) return s;
                }
            }
            return "";
        }
        Map<String, File> fileCollection = new HashMap<>();
        Map<String, HashSet<String>> filesKeywords = new HashMap<>();

        // get the collection of relevant keywords of the request
        HashSet<String> requestKeys = getKeysFromJsonString(jsonRequest);
        System.out.println('[' + String.join(", ", requestKeys) + ']');

        // get the collection of relevant keywords for each ontology
        File customerOntology = new File(customerOntologyPath);
        File productOntology = new File(productOntologyPath);
        File orderOntology = new File(orderOntologyPath);

        fileCollection.put("customer", customerOntology);
        fileCollection.put("product",productOntology);
        fileCollection.put("order", orderOntology);

        //map the keywords sets with the according service [fileCollection contains the RDF files with their service names]
        for (Map.Entry<String, File> entry : fileCollection.entrySet()) {
            filesKeywords.put(entry.getKey(), parseAndExtractKeywords(entry.getValue()));
        }

        //compare the two collections
        Map<String, Integer> scoreMap = new HashMap<>();
        scoreMap.put("customer", 0);
        scoreMap.put("product", 0);
        scoreMap.put("order", 0);
        for(Map.Entry<String, HashSet<String>> entry : filesKeywords.entrySet()){
            System.out.println("[OntologyMatching] - [ " + entry.getKey() + " : " + entry.getValue() + " ]");
           if( entry.getValue().equals(requestKeys)){
               return entry.getKey(); //return the service name that has a matching ontology
           } else {
//               System.out.println("[OntologyMatching] - does contain the request keys" + entry.getValue().contains(requestKeys));
                for(String s : entry.getValue()){
                     if(requestKeys.contains(s)){
                          scoreMap.put(entry.getKey(), scoreMap.get(entry.getKey()) + 1);
                     }
                }
           }
        }
        System.out.println("[OntologyMatching] - scoreMap: " + scoreMap);
        int max = 0;
        String maxKey = "";
        for(Map.Entry<String, Integer> entry : scoreMap.entrySet()){
            if(entry.getValue() > max){
                max = entry.getValue();
                maxKey = entry.getKey();
            }
        }
        if(maxKey != "") return maxKey;

        return "";
    }
}
