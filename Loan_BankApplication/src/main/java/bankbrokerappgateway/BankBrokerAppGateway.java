/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankbrokerappgateway;

import java.util.HashMap;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.NamingException;
import bankserializer.BankSerializer;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;
import model.jms.MessageReceiverGateway;
import model.jms.MessageSenderGateway;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author M
 */
public class BankBrokerAppGateway {

    private MessageSenderGateway sender = null;
    private MessageReceiverGateway receiver = null;
    private BankSerializer serializer = null;
    public HashMap<String, BankInterestRequest> correlations = new HashMap<>();

    public BankBrokerAppGateway() throws NamingException, JMSException {
        serializer = new BankSerializer();
        sender = new MessageSenderGateway("FromBankToBroker");
        receiver = new MessageReceiverGateway("FromBrokerToBank");
    }
    
    public void onBankInterestReplyArrived(BankInterestRequest request, BankInterestReply reply) {
        String rep = serializer.replyToString(reply);
        String req = serializer.requestToString(request);
        //correlations.put
    }

    public void sendBankRequest(BankInterestReply reply){
        String req = serializer.replyToString(reply);
        Message message = sender.createTextMessge(req);
        sender.send(reply);//, req);
    }
}
