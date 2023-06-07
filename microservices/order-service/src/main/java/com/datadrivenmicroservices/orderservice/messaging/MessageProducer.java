package com.datadrivenmicroservices.orderservice.messaging;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class MessageProducer {
    @Value("${rdf.order.file-path}")
    private String filePath;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendRdfFile() {
        String xml = "";
        try{
            xml = readRdfFile();
            sendWorkerRunnable sendWorkerRunnable = new sendWorkerRunnable(rabbitTemplate, xml);
            Thread thread = new Thread(sendWorkerRunnable);
            thread.start();
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

@AllArgsConstructor
class sendWorkerRunnable implements Runnable {
    RabbitTemplate rabbitTemplate;
    String xml;

    @SneakyThrows
    @Override
    public void run() {
        Thread.sleep(2000);
        rabbitTemplate.convertAndSend(
                MQConfig.RDF_EXCHANGE,
                MQConfig.ORDER_RDF_ROUTING_KEY,
                xml
        );
    }
}
