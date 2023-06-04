package com.datadrivenmicroservices.pfa.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MessageProducer {
    @Value("${rdf.customer.file-path}")
    private String filePath;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendRdfFile() {
        String xml = "";
        try{
            xml = readRdfFile();
            rabbitTemplate.convertAndSend(
                    MQConfig.RDF_EXCHANGE,
                    MQConfig.CUSTOMER_RDF_ROUTING_KEY,
                    xml
            );
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private String readRdfFile() throws IOException {
        File f = new File(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        String line;
        String xml = "";
        while ((line = br.readLine()) != null){
            xml += line;
        }
        return xml;
    }
}
