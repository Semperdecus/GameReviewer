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
import messages.MessageDispatcher;
import messages.MessageFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Selector;

/**
 * gather data from IGN
 *
 * @author teren
 */
@Stateless
@Named
public class LookupBean {

    @Inject
    MessageFactory factory;

    @Inject
    MessageDispatcher dispatcher;

    private static final String URL = "https://www.ign.com/";

    public LookupBean() {
    }

    public void search(String query) throws IOException {
        String id = getId(query);
        String name = getName(id);
        Score score = getScore(id);

        System.out.println("NAME: " + name);
        System.out.println("SCORE: " + score);

        String messageBody;

        if (score == null) {
            messageBody = factory.notFoundMessageBody();
        } else {
            messageBody = factory.getMessageBody(score);
        }

        System.out.println(messageBody);
        // Dispatch the message
        dispatcher.dispatchMessage(messageBody);
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

        System.out.println(finalUrl);

        Document doc;

        try {
            doc = Jsoup.connect(finalUrl).get();
            Element firstSearchResult = doc.select("td[class=primary_photo]").first();

            String fullHTML = firstSearchResult.html();

            // It will throw java.lang.StringIndexOutOfBoundsException if not a movie or series
            id = fullHTML.substring(fullHTML.indexOf("tt"), fullHTML.indexOf("/?ref_"));
        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException | IOException exc) {
            Logger.getLogger(LookupBean.class.getName()).log(Level.SEVERE, null, "Exception in Metacritics Lookup.");
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
