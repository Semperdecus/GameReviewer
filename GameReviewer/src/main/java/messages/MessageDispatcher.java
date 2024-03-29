/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.jms.*;

/**
 *
 * @author teren
 */
@Singleton
public class MessageDispatcher {

    private Session session;
    
    @EJB
    private MessageDispatcher instance;

    @Resource(lookup = "jms/gameReviewerFactory")
    private ConnectionFactory connectionFactory;

    // Topic created through payara interface
    @Resource(lookup = "jms/gameReviewerRequest")
    private Topic t;

    public MessageDispatcher() {
    }

    public void dispatchMessage(final String messageBody) {
        instance.postToJMS(new Runnable() {
            @Override
            public void run() {
                try (JMSContext context = connectionFactory.createContext()) {
                    TextMessage requestMessage = context.createTextMessage();
                    requestMessage.setText(messageBody);
                    
                    JMSProducer producer = context.createProducer();
                    producer.send(t, requestMessage);

                    System.out.println(">>> Client- Message dispatched");
                } catch (JMSException ex) {
                    Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
                // JMScontext auto closes
            }
        }
        );
    }

    @Asynchronous
    public void postToJMS(Runnable r) {
        r.run();
    }
}
