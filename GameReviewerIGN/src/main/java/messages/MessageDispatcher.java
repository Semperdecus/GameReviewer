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

    @EJB
    private MessageDispatcher instance;

    @Resource(lookup = "jms/gameReviewerFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/gameReviewerReplyClient")
    private Queue q;

    public MessageDispatcher() {
    }

    public void dispatchMessage(final String messageBody, final String sessionId) {
        instance.postToJMS(new Runnable() {
            @Override
            public void run() {
                try (JMSContext context = connectionFactory.createContext()) {
                    JMSProducer producer = context.createProducer();

                    String contents = messageBody;

                    TextMessage replyMessage = context.createTextMessage();
                    replyMessage.setText(contents);
                    replyMessage.setJMSCorrelationID(sessionId);
                    
                    System.out.println(">>> IGN - Message dispatched " + replyMessage);
                    producer.send(q, replyMessage);
                } catch (JMSException ex) {
                    Logger.getLogger(MessageDispatcher.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );
    }

    @Asynchronous
    public void postToJMS(Runnable r) {
        r.run();
    }

}
