/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import domain.LookupResult;
import domain.QueryResult;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 * used to update page when MDB is called
 *
 * @author teren
 */
@Stateless
public class ResultService implements Serializable {

    private List<QueryResult> results;

    public ResultService() {
    }

    @PostConstruct
    public void init() {
        results = new ArrayList<>();
    }

    public List<QueryResult> getResults() {
        return results;
    }

    public void setResults(List<QueryResult> results) {
        this.results = results;
    }

    public void addResult(LookupResult result) {
        System.out.println(result.getSessionId());
        this.results.get(0).getLookupResults().add(result);
    }
}
