package messaging;

import loanbroker.LoanBrokerFrame;
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
This class listens to the client if the Client application sended new messages.
*/
public class ClientToBrokerListener {

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;
    private LoanBrokerFrame frame = null;

    public ClientToBrokerListener(LoanBrokerFrame frame) {
        try {
            /*
            Creating properties to connect with ActiveMQ library and url.
            */
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            /*
            Properties to listen to if the Client application has send new messages.
            */
            properties.put(("queue.FromClientToBroker"), "FromClientToBroker");

            /*
            Creating connection factory.
            */
            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            /*
            Connect with the created JNDI connection factory.
            */
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            /*
            Listen to a specific destination. In our case messages from the Client
            or FromClientToBroker.
            */
            destination = (Destination) jndiContext.lookup("FromClientToBroker");
            consumer = session.createConsumer(destination);

            connection.start();

            /*
            Creating a litener to listen to the GUI frame if the Client application
            has sended new messages.
            */
            ClientMessageListener clientMessageListener = new ClientMessageListener(frame);
            consumer.setMessageListener(clientMessageListener);
        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }
}
