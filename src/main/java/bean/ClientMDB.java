/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import domain.LookupResult;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.JsonObject;
import messages.MessageDispatcher;
import messages.MessageFactory;
import service.ResultService;

/**
 * bean to listen to JMS replies
 * 
 * @author teren
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/gameReviewerReply")
    ,
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class ClientMDB implements MessageListener {

    @Inject
    ResultService resultService;

    @Inject
    MessageFactory factory;

    public ClientMDB() {
    }

    @Override
    public void onMessage(Message message) {
        System.out.println("<<< RECEIVED CLIENT SIDE MESSAGE:");
        try {
            TextMessage t = (TextMessage) message;

            System.out.println(t.getText());

            JsonObject ob = factory.fromString(t.getText());

            resultService.addResult(new LookupResult(ob));

        } catch (JMSException ex) {
            Logger.getLogger(ClientMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
