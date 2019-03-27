/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.EnrichedRatingDao;
import domain.EnrichedRating;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Do ratings on site
 *
 * @author teren
 */
@Stateless
public class ScoreService implements Serializable {

    @Inject
    private EnrichedRatingDao dao;

    public void addRating(EnrichedRating er) {
        er.setDate(new Date());
        dao.add(er);
    }

    public void editRating(EnrichedRating er) {
        er.setDate(new Date());
        dao.edit(er);
    }

    public List<EnrichedRating> getHistory() {
        return dao.findAll();
    }
}
