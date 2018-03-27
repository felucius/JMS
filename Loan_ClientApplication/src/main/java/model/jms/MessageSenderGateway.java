/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.jms;

import com.google.gson.Gson;
import com.oracle.jrockit.jfr.Producer;
import java.util.HashMap;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import model.loan.LoanRequest;

/**
 *
 * @author M
 */
public class MessageSenderGateway {

    Connection connection = null;
    Session session = null;
    Destination destination = null;
    MessageProducer producer = null;

    public MessageSenderGateway(String channelName) throws NamingException, JMSException {
        // Creating properties to connect with ActiveMQ and it's URL.
        Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

        /* 
            Location of the queue where the message will be stored. In this case
            the Broker or FromClientToBroker.
         */
        properties.put(("queue." + channelName/*"queue.FromClientToBroker"*/), channelName/*"FromClientToBroker"*/);

        /*
            Creating a connection factory.
         */
        Context jndiContext = new InitialContext(properties);
        ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

        /*
            Connect with the JNDI connection factory.
         */
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /*
            Setting the destination to send the message. In our case the Broker
         */
        destination = (Destination) jndiContext.lookup(channelName/*"FromClientToBroker"*/);
        producer = session.createProducer(destination);
    }

    public Message createTextMessge(String body) {
        try {
            return session.createTextMessage(body);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void send(Message msg) {
        try {
            producer.send(msg);

            session.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
