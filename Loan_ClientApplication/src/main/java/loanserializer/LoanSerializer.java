/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loanserializer;

import com.google.gson.Gson;
import loanbrokerappgateway.LoanBrokerAppGateway;
import model.loan.LoanReply;
import model.loan.LoanRequest;

/**
 *
 * @author M
 */
public class LoanSerializer{
    Gson gson = new Gson();
    
    public String requestToString(LoanRequest request){
        return gson.toJson(request);
    }
    
    public LoanRequest requestFromString(String str){
        return gson.fromJson(str, LoanRequest.class);
    }
    
    public String replyToString(LoanReply reply){
        return gson.toJson(reply);
    }
    
    public LoanReply replyFromString(String string){
        return gson.fromJson(string, LoanReply.class);
    }
}
