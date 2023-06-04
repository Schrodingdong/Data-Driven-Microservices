package com.datadrivenmicroservices.pfa.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String RDF_EXCHANGE = "rdf.exchange";
    public static final String CUSTOMER_RDF_QUEUE = "customer.rdf.queue";
    public static final String CUSTOMER_RDF_ROUTING_KEY = "customer.rdf.key";

    @Bean
    public Queue queue(){
        return new Queue(CUSTOMER_RDF_QUEUE);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(RDF_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CUSTOMER_RDF_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new SimpleMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }




}
