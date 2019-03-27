/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import bean.LookupBean;
import domain.Score;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.MessageFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Playground to check if data gathering works
 *
 * @author teren
 */
public class MessageTest {

    MessageFactory messageFactory;

    public MessageTest() {
        messageFactory = new MessageFactory();
    }

    @Test
    public void testGetMessage() {
        Score s = new Score(8.5, "link.com");
        String result = messageFactory.getMessageBody(s);

        assertEquals("{\"score\":8.5,\"link\":\"link.com\",\"source\":\"MetaCritics\"}", result);
    }

    @Test
    public void testLookupName() throws IOException {
        String query = "Super mario 64";
        String baseUrl = "http://www.metacritic.com/search/game/";
        String name = null;

        String finalUrl = baseUrl + query + "/results";
        System.out.println(finalUrl);

        Document doc;
        try {
            doc = Jsoup.connect(finalUrl)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            Element firstSearchResult = doc.select("h3[class=product_title basic_stat]").first();

            name = firstSearchResult.select("a").first().text();

            System.out.println("Name: " + name);

        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException | IOException exc) {
            System.out.println(exc);
        }

        assertEquals("Super Mario 64", name);
    }

    @Test
    public void testLookupScore() throws IOException {
        String query = "Super mario 64";
        String baseUrl = "http://www.metacritic.com/search/game/";
        String link = null;
        Double score = null;

        String finalUrl = baseUrl + query + "/results";
        System.out.println(finalUrl);

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
            score = Double.parseDouble(scoreText) / 10;

            System.out.println("Link: " + link);
            System.out.println("Score: " + score);

        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException | IOException exc) {
            System.out.println(exc);
        }

        assertEquals("https://www.metacritic.com/game/nintendo-64/super-mario-64", link);
        assertEquals(9.4, score, 1);
    }
}
