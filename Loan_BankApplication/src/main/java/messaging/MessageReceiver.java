package messaging;

import loanbank.JMSBankFrame;
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

/*
This class listens to the ActiveMQ that is connected with the Broker application
to receive messages.
*/
public class MessageReceiver {

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;
    private BrokerMessageListener loanMessageListener = null;
    private JMSBankFrame frame = null;

    public MessageReceiver(JMSBankFrame frame) {
        this.frame = frame;
        try {
            /*
            Creating properties to connect with ActiveMQ and it's URL.
            */
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            /*
            Listen to the queue FromBrokerToBank which is in our case the Broker.
            */
            properties.put(("queue.FromBrokerToBank"), "FromBrokerToBank");

            /*
            Creating a connection factory.
            */
            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            /*
            Connect with the created connection factory.
            */
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            /*
            Setting the destination to listen to. In our case the Broker or 
            FromBrokerToBank.
            */
            destination = (Destination) jndiContext.lookup("FromBrokerToBank");
            consumer = session.createConsumer(destination);

            connection.start();

            /*
            Listing to incoming messages from the Broker application.
            */
            loanMessageListener = new BrokerMessageListener(frame);
            consumer.setMessageListener(loanMessageListener);
        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }
}
