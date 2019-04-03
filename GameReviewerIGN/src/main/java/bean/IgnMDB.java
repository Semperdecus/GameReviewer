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
import messages.MessageDispatcher;
import messages.MessageFactory;

/**
 * Listen to requests from request topic (service activator)
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
public class IgnMDB implements MessageListener {
    
    @Inject
    LookupBean lookup;
    
    @Inject
    MessageFactory factory;

    @Inject
    MessageDispatcher dispatcher;

    @Override
    public void onMessage(Message msg) {
        TextMessage t = (TextMessage) msg;
        
        String q = factory.getQueryFromRequestMessageBody(t);
        String sessionId = factory.getSessionIdFromRequestMessageBody(t);
        
        System.out.println("<<< IGN - Received Query: " + q);

        try {
            String messageBody = lookup.search(q);
            dispatcher.dispatchMessage(messageBody, sessionId);
        } catch (IOException ex) {
            Logger.getLogger(IgnMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
