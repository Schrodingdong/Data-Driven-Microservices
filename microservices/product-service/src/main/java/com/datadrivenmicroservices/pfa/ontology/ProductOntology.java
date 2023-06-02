package com.datadrivenmicroservices.pfa.ontology;

import com.datadrivenmicroservices.pfa.model.Product;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;
import org.apache.jena.vocabulary.XSD;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductOntology {
    @Value("${rdf.product.base-uri}")
    private String productBaseUri;
    @Value("${rdf.product.file-path}")
    private String productOntologyFilePath;

    /**
     * To do at startup
     * */
    public Model initialiseModel() {
        // initialise Empty Model
        Model model = ModelFactory.createDefaultModel();
        // initialise Schema
        Resource product = model.createResource(productBaseUri)
                .addProperty(RDF.type, RDFS.Class);
        Resource productName = model.createResource(productBaseUri + "#productName")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, product)
                .addProperty(RDFS.range, XSD.xstring);
        Resource productDescription = model.createResource(productBaseUri + "#productDescription")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, product)
                .addProperty(RDFS.range, XSD.xstring);
        Resource productPrice = model.createResource(productBaseUri + "#productPrice")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, product)
                .addProperty(RDFS.range, XSD.xfloat);
        writeOntology(model);
        return model;
    }

    public void addProduct(Product product) {
        File f = new File(productOntologyFilePath);
        Model model = readOntologyFromFile(f);
        model.createResource(productBaseUri + "/product/" + product.getId())
                .addProperty(RDF.type, model.getResource(productBaseUri))
                .addProperty(model.getProperty(productBaseUri + "#productName"), product.getProductName())
                .addProperty(model.getProperty(productBaseUri + "#productDescription"), product.getProductDescription())
                .addProperty(model.getProperty(productBaseUri + "#productPrice"), String.valueOf(product.getProductPrice()));
        writeOntology(model);
    }

    public void addProductsFromList(List<Product> productList){
        File f = new File(productOntologyFilePath);
        Model model = readOntologyFromFile(f);
        for (Product product : productList) {
            model.createResource(productBaseUri + "/product/" + product.getId())
                    .addProperty(RDF.type, model.getResource(productBaseUri))
                    .addProperty(model.getProperty(productBaseUri + "#productName"), product.getProductName())
                    .addProperty(model.getProperty(productBaseUri + "#productDescription"), product.getProductDescription())
                    .addProperty(model.getProperty(productBaseUri + "#productPrice"), String.valueOf(product.getProductPrice()));
        }
        writeOntology(model);
    }

    public void writeOntology(Model model) {
        File f = new File(productOntologyFilePath);
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
