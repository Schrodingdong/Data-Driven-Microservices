package com.datadrivenmicroservices.orderservice.ontology;

import com.datadrivenmicroservices.orderservice.messaging.MessageProducer;
import com.datadrivenmicroservices.orderservice.model.OrderEntity;
import com.datadrivenmicroservices.orderservice.model.OrderProductEntity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class OrderOntology {
    @Value("${rdf.order.base-uri}")
    private String orderBaseUri;
    @Value("${rdf.customer.base-uri}")
    private String customerBaseUri;
    @Value("${rdf.product.base-uri}")
    private String productBaseUri;
    @Value("${rdf.order.file-path}")
    private String orderOntologyFilePath;
    @Autowired
    private MessageProducer rabbitMessageProducer;

    public Model initialiseModel(){
        // initialise Empty Model
        Model model = ModelFactory.createDefaultModel();
        // initialise Schema
        Resource order = model.createResource(this.orderBaseUri)
                .addProperty(RDF.type, RDFS.Class);
//        Resource orderCreationDate = model.createResource(this.orderBaseUri+ "#orderCreationDate")
//                .addProperty(RDF.type, RDF.Property)
//                .addProperty(RDFS.domain, order)
//                .addProperty(RDFS.range, XSD.date);
        Resource forCustomer = model.createResource(this.orderBaseUri+ "#forCustomer")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, order)
                .addProperty(RDFS.range, customerBaseUri);
        Resource hasProduct = model.createResource(this.orderBaseUri+ "#hasProduct")
                .addProperty(RDF.type, RDF.Property)
                .addProperty(RDFS.domain, order)
                .addProperty(RDFS.range, productBaseUri);
        writeOntology(model);
        return model;
    }

    public void addOrder(OrderEntity order){
        File f = new File(orderOntologyFilePath);
        Model model = readOntologyFromFile(f);
        model.createResource(orderBaseUri + "/" + order.getOrderId())
//                .addProperty(model.getProperty(orderBaseUri + "#orderCreationDate"), order.getOrderCreationDate().toString())
                .addProperty(model.getProperty(orderBaseUri + "#forCustomer"), customerBaseUri + order.getForCustomer());

        // loop on each product and add it to the order
        List<OrderProductEntity> orderProducts = order.getHasProduct();
        for(OrderProductEntity orderProduct : orderProducts){
            model.getResource(orderBaseUri + "/" + order.getOrderId())
                    .addProperty(model.getProperty(orderBaseUri + "#hasProduct"), productBaseUri + "/" + orderProduct.getProductId());
        }
        writeOntology(model);
    }

    public void addOrdersFromList(List<OrderEntity> orders){
        File f = new File(orderOntologyFilePath);
        Model model = readOntologyFromFile(f);
        for(OrderEntity order : orders){
            model.createResource(orderBaseUri + "/" + order.getOrderId())
//                    .addProperty(model.getProperty(orderBaseUri + "#orderCreationDate"), order.getOrderCreationDate().toString())
                    .addProperty(model.getProperty(orderBaseUri + "#forCustomer"), customerBaseUri + order.getHasProduct());

            // loop on each product and add it to the order
            List<OrderProductEntity> orderProducts = order.getHasProduct();
            for(OrderProductEntity orderProduct : orderProducts){
                model.getResource(orderBaseUri + "/" + order.getOrderId())
                        .addProperty(model.getProperty(orderBaseUri + "#hasProduct"), productBaseUri + "/" + orderProduct.getProductId());
            }
        }
        writeOntology(model);
    }


    public void writeOntology(Model model) {
        File f = new File(orderOntologyFilePath);
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
        } finally {
            rabbitMessageProducer.sendRdfFile();
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
