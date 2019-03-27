/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 * class for merging lookup rating and results
 *
 * @author teren
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "EnrichedRating.findAll", query = "SELECT e FROM EnrichedRating e")
})
public class EnrichedRating implements Serializable{

    @OneToOne
    @Id
    private QueryResult queryResult;

    private int userRating;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date date;

    public EnrichedRating() {
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public QueryResult getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(QueryResult queryResult) {
        this.queryResult = queryResult;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
