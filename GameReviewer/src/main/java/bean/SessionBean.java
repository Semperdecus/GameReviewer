/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import domain.LookupResult;
import domain.QueryResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import messages.MessageDispatcher;
import messages.MessageFactory;
import service.ScoreService;
import service.ResultService;

/**
 * session bean
 *
 * @author teren
 */
@SessionScoped
@Named("sessionBean")
@ManagedBean
public class SessionBean implements Serializable {

    @Inject
    MessageFactory factory;

    @Inject
    MessageDispatcher dispatcher;

    @Inject
    ResultService resultService;

    @Inject
    ScoreService scoreService;

    private String sessionId;

    public SessionBean() {

    }

    public void btnPressed() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        String in = ec.getRequestParameterMap().get("inputForm:in");

        HttpSession session = (HttpSession) ec.getSession(false);
        sessionId = session.getId();
        System.out.println("SESSIONID = " + sessionId);

        QueryResult qr = new QueryResult();
        qr.setQuery(in);
        resultService.getResults().add(0, qr);

        String messageBody = factory.getMessageBody(in, sessionId);
        dispatcher.dispatchMessage(messageBody);
    }

    public List<QueryResult> getResults() {
        List<QueryResult> result = new ArrayList<>();
        
        for (QueryResult q : resultService.getResults()) {
            for (int i = 0; i < q.getLookupResults().size(); i++) {
                if (q.getLookupResult(i).getSessionId().equals(sessionId)) {
                    result.add(q);
                    break;
                }
            }
        }
        return result;
    }

    public String calculateAverage(QueryResult qr) {
        double totalRating = 0;
        int totalReviewers = 0;

        for (LookupResult cr : qr.getLookupResults()) {
            if (cr.getScore() != 0) {
                totalRating = totalRating + cr.getScore();
                totalReviewers += 1;
            }
        }

        double calculatedRating = totalRating / totalReviewers;

        return String.valueOf(calculatedRating).substring(0, 3) + " by " + totalReviewers + " reviewers";
    }

}
