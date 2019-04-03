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
                .add("source", "IGN")
                .build();

        return j.toString();
    }

    public String notFoundMessageBody() {
        JsonObject j = Json.createObjectBuilder()
                .add("score", 0)
                .add("link", "Not found")
                .add("source", "IGN")
                .build();

        return j.toString();
    }

    public String getQueryFromRequestMessageBody(TextMessage m) {
        String s = null;
        try {
            System.out.println("MESSAGE TO GET QUEUE FORM: " + m.getText());
            JsonReader jsonReader;
            jsonReader = Json.createReader(new StringReader(m.getText()));
            JsonObject j = jsonReader.readObject();
            s = j.getString("query");
            jsonReader.close();
            System.out.println("QUERY: " + s);
        } catch (JMSException ex) {
            System.out.println("ERROR");
        }
        return s;
    }
}
