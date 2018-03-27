/*package messaging;

import com.google.gson.Gson;
import loanbroker.LoanBrokerFrame;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import model.loan.LoanReply;

public class BrokerToClient {

    Connection connection = null;
    Session session = null;
    Destination destination = null;
    MessageProducer producer = null;
    LoanBrokerFrame frame = null;

    public BrokerToClient(LoanBrokerFrame frame) {
        this.frame = frame;
        try {

            Properties properties = new Properties();
            properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
            properties.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");


            properties.put(("queue.FromBrokerToClient"), "FromBrokerToClient");


            Context jndiContext = new InitialContext(properties);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");;

            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = (Destination) jndiContext.lookup("FromBrokerToClient");
            producer = session.createProducer(destination);

        } catch (NamingException | JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void send(LoanReply reply, String correlation) {
        try {

            Message message = session.createTextMessage(new Gson().toJson(reply));
            message.setJMSCorrelationID(correlation);
            producer.send(message);
            
            session.close();
            connection.close();
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}*/
