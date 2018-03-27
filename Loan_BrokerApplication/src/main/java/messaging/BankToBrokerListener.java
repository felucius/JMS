package messaging;

import loanbroker.LoanBrokerFrame;
import java.util.Properties;
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
This class listens to the messages from ActiveMQ to check if there are new messages
from the Bank.
*/
public class BankToBrokerListener {

    private Connection connection = null;
    private Session session = null;
    private Destination destination = null;
    private MessageConsumer consumer = null;
    private LoanBrokerFrame frame = null;

    public BankToBrokerListener(LoanBrokerFrame frame) {
        try {
            /*
            Creating properties to connect with ActiveMQ library and the url
            to connect to the portal website.
            */
            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

            /*
            Adding properties to listen to a specific queue.
            */
            properties.put(("queue.FromBankToBroker"), "FromBankToBroker");

            /*
            Creating a connection factory.
            */
            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            /*
            Connect with the created JNDI connection factory.
            */
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            /*
            Setting a destination to listen to. In our case messages from the Bank or
            FromBankToBroker.
            */
            destination = (Destination) jndiContext.lookup("FromBankToBroker");
            consumer = session.createConsumer(destination);

            connection.start();

            /*
            Creating a listener to check if new messages have been received from
            the Bank.
            */
            BankMessageListener bankMessageListener = new BankMessageListener(frame);
            consumer.setMessageListener(bankMessageListener);

        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }
}
