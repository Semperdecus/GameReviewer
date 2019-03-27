/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.json.JsonObject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Results from the lookup from reviewer sites
 *
 * @author teren
 */
@Entity
public class LookupResult implements Serializable {

    @Id
    @GeneratedValue
    private int id;

    private double score;
    private String link;

    public LookupResult() {
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LookupResult(JsonObject j) {

        try {
            this.score = j.getJsonNumber("score").doubleValue();
            this.link = j.getString("link");
        } catch (ClassCastException cce) {
            this.score = -1;
            this.link = null;
            System.out.println(cce.getMessage());
        }
    }
}
