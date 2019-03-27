/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import messages.MessageFactory;

/**
 * 
 * @author teren
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/gameReviewerRequest")
    ,
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/gameReviewerRequest")
    ,
    @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/gameReviewerRequest")
    ,
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic")
})
public class MetaCriticsMDB implements MessageListener {
    
    @Inject
    LookupBean lookup;
    
    @Inject
    MessageFactory factory;

    @Override
    public void onMessage(Message msg) {

        TextMessage t = (TextMessage) msg;

        String q = factory.getQueryFromRequestMessageBody(t);
        System.out.println("<<< MetaCritics - Received Query: " + q);

        try {
            lookup.search(q);
        } catch (IOException ex) {
            Logger.getLogger(MetaCriticsMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
