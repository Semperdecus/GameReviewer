/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import domain.Score;
import java.io.IOException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import messages.MessageDispatcher;
import messages.MessageFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

    public String search(String query) throws IOException {
        Score score = getScore(query);
        String messageBody;
        
        if (score == null) {
            messageBody = factory.notFoundMessageBody();
        } else {
            messageBody = factory.getMessageBody(score);
        }

        return messageBody;
    }

    public Score getScore(String query) {
        Score score = null;
        String baseUrl = "http://www.metacritic.com/search/game/";
        String link = null;

        String finalUrl = baseUrl + query + "/results";

        Document doc;
        try {
            doc = Jsoup.connect(finalUrl)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            Element firstSearchResult = doc.select("div[class=main_stats]").first();

            link = firstSearchResult.select("a").first().attr("abs:href");
            String scoreText = firstSearchResult.select("span").first().text();
            Double scoreDouble = Double.parseDouble(scoreText) / 10;

            score = new Score(scoreDouble, link);

        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException | IOException exc) {
            System.out.println(exc);
        }

        return score;
    }
}
