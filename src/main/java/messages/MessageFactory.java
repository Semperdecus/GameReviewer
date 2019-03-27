/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messages;

import java.io.Serializable;
import java.io.StringReader;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author teren
 */
@Stateless
public class MessageFactory implements Serializable{

    public MessageFactory() {
    }

    public String getMessageBody(String q) {
        JsonObject j = Json.createObjectBuilder()
                .add("query", q)
                .build();

        return j.toString();
    }

    public JsonObject fromString(String q) {
        JsonObject object;
        try (JsonReader jsonReader = Json.createReader(new StringReader(q))) {
            object = jsonReader.readObject();
        }
        return object;
    }
}
