/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.EnrichedScoreDao;
import domain.EnrichedScore;
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
    private EnrichedScoreDao dao;

    public void addRating(EnrichedScore er) {
        er.setDate(new Date());
        dao.add(er);
    }

    public void editRating(EnrichedScore er) {
        er.setDate(new Date());
        dao.edit(er);
    }

    public List<EnrichedScore> getHistory() {
        return dao.findAll();
    }
}
