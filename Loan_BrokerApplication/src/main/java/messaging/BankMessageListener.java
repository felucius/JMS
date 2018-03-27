package messaging;

import com.google.gson.Gson;
import loanbroker.LoanBrokerFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.bank.BankInterestReply;
import model.jms.MessageSenderGateway;
import model.loan.LoanReply;
import model.loan.LoanRequest;

public class BankMessageListener implements MessageListener {

    private final LoanBrokerFrame frame;

    public BankMessageListener(LoanBrokerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message msg) {
        try {
            /*
            Creating a message from the retrieved Google Json format of LoanRequest
            object. The GUI frame adds the loanrequest to the Broker GUI frame.
            After that the Broker sends the request to the bank.
             */
            TextMessage message = (TextMessage) msg;
            BankInterestReply bankInterestReply = new Gson().fromJson(message.getText(), BankInterestReply.class);
            LoanRequest request = frame.correlations.get(message.getJMSCorrelationID());

            // Practicum 2
            try {
                MessageSenderGateway messageSenderGateway = new MessageSenderGateway("FromBrokerToClient");
                LoanReply reply = new LoanReply(bankInterestReply.getInterest(), bankInterestReply.getQuoteId());
                
                messageSenderGateway.sendToBank(reply, message.getJMSCorrelationID());
                frame.add(request, bankInterestReply);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //BrokerToClient sender = new BrokerToClient(frame);
            //LoanReply reply = new LoanReply(bankInterestReply.getInterest(), bankInterestReply.getQuoteId());
            //sender.sendToClient(reply, message.getJMSCorrelationID());
            //frame.add(request, bankInterestReply);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

}
