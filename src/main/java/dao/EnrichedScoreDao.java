/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import domain.EnrichedScore;
import domain.LookupResult;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Persist enriched scores
 * @author teren
 */
@Stateless
public class EnrichedScoreDao {

    @PersistenceContext(unitName="GameReviewerClientPU")
    private EntityManager em;
    
    public List<EnrichedScore> findAll(){
        return em.createNamedQuery("EnrichedRating.findAll").getResultList();
    }
    
    public EnrichedScore find(String query){
        return em.find(EnrichedScore.class, query);
    }
    
    public void add(EnrichedScore er){
        for (LookupResult lr : er.getQueryResult().getLookupResults()) {
            em.persist(lr);
        }
        
        em.persist(er.getQueryResult());
        em.persist(er);
    }
    
    public void edit(EnrichedScore er){
        em.merge(er);
    }
    
    public void remove(EnrichedScore er){
        em.remove(er);
}
}
