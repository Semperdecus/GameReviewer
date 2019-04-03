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

        assertEquals("{\"score\":8.5,\"link\":\"link.com\",\"source\":\"IGN\"}", result);
    }

    @Test
    public void testLookupName() throws IOException {
        String query = "Super mario 64";
        String finalUrl = "https://www.ign.com/games/";
        String name = null;

        for (String word : query.toLowerCase().split(" ")) {
            finalUrl = finalUrl + word + "-";
        }

        finalUrl = finalUrl.substring(0, finalUrl.length() - 1);

        System.out.println(finalUrl);
        Document doc;

        try {
            doc = Jsoup.connect(finalUrl)
                    .data("query", "Java")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(3000)
                    .get();

            Element nameHTML = doc.select("h1[class=jsx-2881975397]").first();

            System.out.println("NAME: " + nameHTML);

            name = nameHTML.html();
            System.out.println("FULLNAME: " + name);
        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException exc) {
            System.out.println(exc.getMessage());
        }

        assertEquals("Super Mario 64", name);
    }

    @Test
    public void testLookupScore() throws IOException {
        Score score = null;
        String pageUrl = "https://www.ign.com/games/super-mario-64";

        try {
            Document doc = Jsoup.connect(pageUrl).get();

            Element scoreValue = doc.select("span[class=jsx-3796817351 hexagon-content]").first();
            String link = doc.select("a.jsx-2881975397").first().attr("abs:href");
            System.out.println("LINK: " + link);
            
            String scoreHTML = scoreValue.html();
            Double scoreDouble = Double.parseDouble(scoreHTML);
            System.out.println("SCORE: " + scoreDouble);

            score = new Score(scoreDouble, link);
        } catch (java.lang.StringIndexOutOfBoundsException | NullPointerException exc) {
            System.out.println(exc.getMessage());
        }
        
        assertEquals("https://www.ign.com/articles/1996/09/26/super-mario-64", score.getLink());
        assertEquals(9.8, score.getScore(), 1);
    }
}
