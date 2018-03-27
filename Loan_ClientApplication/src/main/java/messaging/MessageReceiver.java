package messaging;

import loanclient.LoanClientFrame;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class MessageReceiver {

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;
    private LoanClientFrame frame = null;
    
    public MessageReceiver(LoanClientFrame frame){
        this.frame = frame;
        try {
            /*
            Creating properties to set the url mapping to ActiveMQ
            and adding context factory from the libraries.
            */
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            /*
            Adding the property queue to receive the message from the broker from 
            ActiveMQ queue.
            */
            properties.put(("queue.FromBrokerToClient"), "FromBrokerToClient");

            /*
            Looking up the existing JNDI context to create a connection later on.
            */
            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

            /*
            Creating a connection with a destination to listen to. In this case
            the messages from the Broker to the client.
            */
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = (Destination) jndiContext.lookup("FromBrokerToClient");
            
            /*
            Consuming the messages from the given destination (Broker)
            */
            consumer = session.createConsumer(destination);

            connection.start();

            /*
            When a reply has been returned from the broker the frame updates itself
            with the given json object from ActiveMQ.
            */
            BrokerMessageListener brokerMessageListener = new BrokerMessageListener(frame);
            
            /*
            Consumer listening to the GUI frame to update the given reply from
            the broker
            */
            consumer.setMessageListener(brokerMessageListener);
        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }
}
