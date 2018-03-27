package messaging;

import com.google.gson.Gson;
import loanclient.LoanClientFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import model.loan.LoanReply;
import model.loan.LoanRequest;

public class BrokerMessageListener implements MessageListener {    
    private final LoanClientFrame frame;
    
    public BrokerMessageListener(LoanClientFrame frame) {
        this.frame = frame;
    }
    
    @Override
    public void onMessage(Message msg) {
        try {
            /*
            Retrieved in Json, from ActiveMQ a loanReply from the Bank application,
            through the Broker application. The the request with the reply and it's 
            correlation is drawed on the Client GUI application.
            */
            System.out.println("Received a reply from ABN AMRO");
            TextMessage message = (TextMessage) msg;
            LoanReply reply = new Gson().fromJson(message.getText(), LoanReply.class);
            LoanRequest request = frame.correlations.get(message.getJMSCorrelationID());
            frame.add(request, reply);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

}
