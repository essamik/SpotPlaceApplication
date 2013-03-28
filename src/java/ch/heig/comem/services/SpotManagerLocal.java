/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Hashtag;
import ch.heig.comem.model.Spot;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author louis-samuel
 */
@Local
public interface SpotManagerLocal {

    Spot createSpot(String title, String description, float latitude, float longitude, int rating, String urlPhoto, List<Hashtag> tags, Long idUser);
    
    Spot updateSpot(Long id);
    
    void deleteSpot(Long id);
    
    Spot findSpotById(Long id, Class<Spot> className);

    Spot addHashtag(Long idSpot, Long idHashtag);
}
