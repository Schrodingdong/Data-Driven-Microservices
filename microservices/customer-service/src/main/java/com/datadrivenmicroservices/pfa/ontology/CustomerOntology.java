package com.datadrivenmicroservices.pfa.ontology;

import com.datadrivenmicroservices.pfa.model.Customer;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;


@Service
public class CustomerOntology {
    @Value("${rdf.customer.base-uri}")
    private String customerBaseUri;
    @Value("${rdf.customer.file-path}")
    private String customerOntologyFilePath;

    public Model initialiseModel(){
        // initialise Empty Model
        Model model = ModelFactory.createDefaultModel();
        // initialise Schema
        Resource customer = model.createResource(customerBaseUri)
                .addProperty(RDF.type, RDFS.Class);
        Resource customerFirstName = model.createResource(customerBaseUri+"#customerFirstName")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, customer)
                .addProperty(RDFS.range, XSD.xstring);
        Resource customerLastName = model.createResource(customerBaseUri+"#customerLastName")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, customer)
                .addProperty(RDFS.range, XSD.xstring);
        Resource customerEmail = model.createResource(customerBaseUri+"#customerEmail")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, customer)
                .addProperty(RDFS.range, XSD.xstring);
        Resource customerRegistrationDate = model.createResource(customerBaseUri+"#customerRegistrationDate")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, customer)
                .addProperty(RDFS.range, XSD.date);
        writeOntology(model);
        return model;
    }

    public void addCustomer(Customer customer){
        File f = new File(customerOntologyFilePath);
        Model model = readOntologyFromFile(f);
        model.createResource(customerBaseUri+"/"+customer.getCustomerId())
                .addProperty(RDF.type, model.getResource(customerBaseUri))
                .addProperty(model.getProperty(customerBaseUri+"#customerFirstName"), customer.getCustomerFirstName())
                .addProperty(model.getProperty(customerBaseUri+"#customerLastName"), customer.getCustomerLastName())
                .addProperty(model.getProperty(customerBaseUri+"#customerEmail"), customer.getCustomerEmail())
                .addProperty(model.getProperty(customerBaseUri+"#customerRegistrationDate"), customer.getCustomerRegistrationDate().toString());
        writeOntology(model);
    }

    public void addCustomersFromList(List<Customer> customerList){
        File f = new File(customerOntologyFilePath);
        Model model = readOntologyFromFile(f);
        for (Customer customer : customerList) {
            model.createResource(customerBaseUri+"/"+customer.getCustomerId())
                    .addProperty(RDF.type, model.getResource(customerBaseUri))
                    .addProperty(model.getProperty(customerBaseUri+"#customerFirstName"), customer.getCustomerFirstName())
                    .addProperty(model.getProperty(customerBaseUri+"#customerLastName"), customer.getCustomerLastName())
                    .addProperty(model.getProperty(customerBaseUri+"#customerEmail"), customer.getCustomerEmail())
                    .addProperty(model.getProperty(customerBaseUri+"#customerRegistrationDate"), customer.getCustomerRegistrationDate().toString());
        }
        writeOntology(model);
    }


    public void writeOntology(Model model) {
        File f = new File(customerOntologyFilePath);
        try {
            FileOutputStream out = new FileOutputStream(f);
            model.write(out);
        } catch (FileNotFoundException e) {
            System.err.println("File not found : " + e.getMessage());
            // create file and retry
            try {
                System.out.println("Creating file : " + f.getPath());
                f.createNewFile();
            } catch (IOException ex) {
                System.err.println("Error creating file : " + ex.getMessage());
            }
        }
    }

    /**
     * Only reads the ontology file and returns the model
     * */
    public Model readOntologyFromFile(File f) {
        Model readModel = ModelFactory.createDefaultModel();
        try {
            InputStream in = new FileInputStream(f);
            readModel.read(in, null);
            return readModel;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
