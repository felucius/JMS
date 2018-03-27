/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanbrokerappgateway;

import java.util.HashMap;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;
import loanserializer.LoanSerializer;
import model.jms.MessageReceiverGateway;
import model.jms.MessageSenderGateway;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author M
 */
public class LoanBrokerAppGateway {

    private MessageSenderGateway sender = null;
    private MessageReceiverGateway receiver = null;
    private LoanSerializer serializer = null;
    public HashMap<String, LoanRequest> correlations = new HashMap<>();

    public LoanBrokerAppGateway() throws NamingException, JMSException {
        serializer = new LoanSerializer();
        sender = new MessageSenderGateway("FromClientToBroker");
        receiver = new MessageReceiverGateway("FromBrokerToClient");
    }

    public void applyForLoan(LoanRequest request) throws JMSException {
        String req = serializer.requestToString(request);
        Message message = sender.createTextMessge(req);
        message.getJMSCorrelationID();
        System.out.println("Correlation ID: " + message.getJMSCorrelationID());
        correlations.put(message.getJMSCorrelationID(), request);
        sender.send(message);
    }

    public void onLoanReplyArrived(LoanRequest request, LoanReply reply) {
        String rep = serializer.replyToString(reply);
        String req = serializer.requestToString(request);
        //correlations.put
    }
}
