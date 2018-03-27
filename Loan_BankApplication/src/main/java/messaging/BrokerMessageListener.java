package messaging;

import com.google.gson.Gson;
import loanbank.JMSBankFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.bank.BankInterestRequest;

public class BrokerMessageListener implements MessageListener {

    private final JMSBankFrame frame;

    public BrokerMessageListener(JMSBankFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message msg) {
        try {
            /*
            Retrieving in Json format, from ActiveMQ, a bankInterestRequest from 
            the Client application, through the Broker application. This bankinterestrequest
            is added to the Bank GUI application.
             */
            System.out.println("Received a loan request from a user.");
            TextMessage message = (TextMessage) msg;
            BankInterestRequest interestRequest = new Gson().fromJson(message.getText(), BankInterestRequest.class);
            frame.add(interestRequest, message.getJMSCorrelationID());
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

}
