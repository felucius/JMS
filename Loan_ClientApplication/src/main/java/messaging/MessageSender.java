package messaging;

import com.google.gson.Gson;
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

/*
This class sends a message to the Broker application that is needed for the Bank
application.
*/
public class MessageSender {

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageProducer producer = null;
    
    private HashMap<String, LoanRequest> correlations = null;

    public MessageSender(HashMap<String, LoanRequest> correlations) {
        this.correlations = correlations;
        try {
            // Creating properties to connect with ActiveMQ and it's URL.
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            /* 
            Location of the queue where the message will be stored. In this case
            the Broker or FromClientToBroker.
             */
            properties.put(("queue.FromClientToBroker"), "FromClientToBroker");

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
            destination = (Destination) jndiContext.lookup("FromClientToBroker");
            producer = session.createProducer(destination);

        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method allows a client to send a loanrequest to the Broker application.
     * @param loanRequest is the loanrequest the Client GUI has entered.
     */
    public void send(LoanRequest loanRequest) {
        try {
            /*
            Send the loanRequest to ActiveMQ in Json format and adding the 
            correlation ID of the loanrequest to check later on for the reply
            of the Bank application.
            */
            Message message = session.createTextMessage(new Gson().toJson(loanRequest));
            producer.send(message);
            correlations.put(message.getJMSMessageID(), loanRequest);

            session.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
