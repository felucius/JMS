package messaging;

import com.google.gson.Gson;
import loanbroker.LoanBrokerFrame;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import model.bank.BankInterestRequest;
import model.jms.MessageSenderGateway;
import model.loan.LoanRequest;

/*
This class listens to the messages received from the client application.
 */
public class ClientMessageListener implements MessageListener {

    private final LoanBrokerFrame frame;

    public ClientMessageListener(LoanBrokerFrame frame) {
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
            LoanRequest loanRequest = new Gson().fromJson(message.getText(), LoanRequest.class);
            frame.add(loanRequest);

            // Practicum 2
            try {
                MessageSenderGateway messageSenderGateway = new MessageSenderGateway("FromBrokerToBank");
                BankInterestRequest interestRequest = new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime());
                
                messageSenderGateway.sendToClient(loanRequest, interestRequest, message.getJMSCorrelationID());
                frame.add(loanRequest, interestRequest);
            } catch (NamingException ex) {
                ex.printStackTrace();
            }

            //BrokerToBank sender = new BrokerToBank(frame);
            //BankInterestRequest interestRequest = new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime());
            //sender.sendToClient(loanRequest, interestRequest, message.getJMSMessageID());
            //frame.add(loanRequest, interestRequest);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }
}
