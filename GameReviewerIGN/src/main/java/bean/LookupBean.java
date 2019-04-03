/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import domain.Score;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import messages.MessageFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Selector;

/**
 * gather data from IGN session bean
 *
 * @author teren
 */
@Stateless
@Named
public class LookupBean {

    @Inject
    MessageFactory factory;

    private static final String URL = "https://www.ign.com/";

    public LookupBean() {
    }

    public String search(String query) throws IOException {
        String id = getId(query);
        Score score = getScore(id);

        String messageBody;

        if (score == null) {
            messageBody = factory.notFoundMessageBody();
        } else {
            messageBody = factory.getMessageBody(score);
        }

        // Return name + score + source
        return messageBody;
    }

    public String getId(String query) {
        String result = "";

        for (String word : query.toLowerCase().split(" ")) {
            result = result + word + "-";
        }

        result = result.substring(0, result.length() - 1);

        return result;
    }

    public String getName(String id) throws IOException {
        String finalUrl = URL + "games/" + id;
        String name = null;

        Document doc;

        try {
            doc = Jsoup.connect(finalUrl)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            Element nameHTML = doc.select("h1[class=jsx-2881975397]").first();

            String fullName = nameHTML.html();

            name = fullName;
        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException | IOException exc) {
            Logger.getLogger(LookupBean.class.getName()).log(Level.SEVERE, null, "Exception in IGN lookup.");
        }

        return name;
    }

    public Score getScore(String id) {
        Score score = null;

        String pageUrl = URL + "games/" + id;

        try {
            Document doc = Jsoup.connect(pageUrl).get();

            Element scoreValue = doc.select("span[class=jsx-3796817351 hexagon-content]").first();
            String link = doc.select("a.jsx-2881975397").first().attr("abs:href");

            String scoreHTML = scoreValue.html();
            Double scoreDouble = Double.parseDouble(scoreHTML);

            score = new Score(scoreDouble, link);

        } catch (IOException | NumberFormatException | Selector.SelectorParseException ex) {
            Logger.getLogger(LookupBean.class.getName()).log(Level.SEVERE, null, "Exception in IGN Lookup.");
        }

        return score;
    }
}
