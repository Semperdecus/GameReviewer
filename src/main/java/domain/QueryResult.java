/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Query to sent to the message service
 *
 * @author teren
 */
@Entity
public class QueryResult implements Serializable {

    //same query's should be looked up - this is a unique key (ID)
    @Id
    private String query;
    
    @OneToMany
    private List<LookupResult> lookupResult;

    public QueryResult() {
        this.lookupResult = new ArrayList<>();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<LookupResult> getLookupResults() {
        return lookupResult;
    }

    public void setLookupResults(List<LookupResult> lookupResult) {
        this.lookupResult = lookupResult;
    }

    public LookupResult getLookupResult(int index) {
        if ((this.lookupResult.size() - 1) >= index) {
            return this.lookupResult.get(index);
        }
        return null;
    }
}
