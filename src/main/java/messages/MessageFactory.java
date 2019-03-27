/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import domain.Score;
import java.io.Serializable;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author teren
 */
@Stateless
public class MessageFactory implements Serializable {

    public MessageFactory() {
    }

    public String getMessageBody(Score s) {
        JsonObject j = Json.createObjectBuilder()
                .add("score", s.getScore())
                .add("link", s.getLink())
                .add("source", "MetaCritics")
                .build();

        return j.toString();
    }

    public String notFoundMessageBody() {
        JsonObject j = Json.createObjectBuilder()
                .add("score", "Not found")
                .add("link", "Not found")
                .add("source", "MetaCritics")
                .build();

        return j.toString();
    }

    // debugging
    public String getQueryFromRequestMessageBody(TextMessage m) {
        String s = null;
        try {
            JsonReader jsonReader;
            jsonReader = Json.createReader(new StringReader(m.getText()));
            JsonObject j = jsonReader.readObject();
            s = j.getString("query");
            jsonReader.close();
        } catch (JMSException ex) {
            Logger.getLogger(MessageFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
}
