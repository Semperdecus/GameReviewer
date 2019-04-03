/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import domain.LookupResult;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.JsonObject;
import messages.MessageFactory;
import service.ResultService;

/**
 * bean to listen to JMS replies
 *
 * @author teren
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/gameReviewerReplyClient")
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
        System.out.println("<<< RECEIVED CLIENT SIDE MESSAGE");

        try {
            TextMessage t = (TextMessage) message;

            JsonObject ob = factory.fromString(t.getText());
            
            LookupResult result = new LookupResult(ob);
            result.setSessionId(t.getJMSCorrelationID());

            resultService.addResult(result);
        } catch (JMSException ex) {
            //Logger.getLogger(ClientMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
