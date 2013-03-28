/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Hashtag;
import ch.heig.comem.model.Spot;
import java.util.List;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class HashtagManager implements HashtagManagerLocal {

    @PersistenceContext
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public Hashtag createHashtag(String tag) {
        Hashtag hashtag = new Hashtag();
        hashtag.setTag(tag);
       
        em.persist(hashtag);
        em.flush();
        return hashtag;
    }

    @Override
    public Hashtag updateHashtag(Long id, String newTag) {
        Hashtag oldHashtag = this.findHashtagById(id, Hashtag.class);
        List<Spot> spots = oldHashtag.getSpots();
        em.remove(oldHashtag);

        Hashtag newHashtag = new Hashtag();
        newHashtag = this.createHashtag(newTag);
        newHashtag.setSpots(spots);
        em.persist(newHashtag);

        em.flush();
        return newHashtag;
    }

    @Override
    public void deleteHashtag(Long id) {
        Hashtag hashtagToDelete;
        hashtagToDelete = this.findHashtagById(id, Hashtag.class);
        List<Spot> spots = hashtagToDelete.getSpots();
        /*
         * Pour tout les spots qui se réfèrent à ce hashtag
         */
        for (Spot spot : spots) {
            if (spot.getHashtags().size() == 1) {
                throw new RuntimeException("Des spots se référant à ce Hashtag sont encore utilisés. Veuillez les supprimer ou les modier.");
            }
        }
        if (hashtagToDelete.getSpots().isEmpty()) {
            hashtagToDelete.setSpots(null);
            em.remove(hashtagToDelete);
            em.flush();
        } else {
            throw new RuntimeException("Des spots se référant à ce Hashtag sont encore utilisés. Veuillez les supprimer ou les modier.");
        }
    }

    @Override
    public Hashtag findHashtagById(Long id, Class<Hashtag> className) {
        return em.find(className, id);
    }
    
    @Override
    public Hashtag findHashtagByTag(String tag) {
        Query q = em.createNamedQuery("findHashtagByTag");
        q.setParameter("tag", tag);
        List<Hashtag> candidates = q.getResultList();
        if (candidates.size() == 0) {
            return null;
        } else {
            return candidates.get(0);
        }
    }
    
    

}