/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserializer;

import com.google.gson.Gson;
import model.bank.BankInterestReply;
import model.bank.BankInterestRequest;

/**
 *
 * @author M
 */
public class BankSerializer{
    Gson gson = new Gson();
    
    public String requestToString(BankInterestRequest request){
        return gson.toJson(request);
    }
    
    public BankInterestRequest requestFromString(String str){
        return gson.fromJson(str, BankInterestRequest.class);
    }
    
    public String replyToString(BankInterestReply reply){
        return gson.toJson(reply);
    }
    
    public BankInterestReply replyFromString(String string){
        return gson.fromJson(string, BankInterestReply.class);
    }
}
