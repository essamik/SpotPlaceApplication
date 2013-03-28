/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Hashtag;
import javax.ejb.Local;

/**
 *
 * @author louis-samuel
 */
@Local
public interface HashtagManagerLocal {
    
    Hashtag createHashtag(String tag);
    
    Hashtag updateHashtag(Long id, String newTag);
    
    void deleteHashtag(Long id);
    
    Hashtag findHashtagById(Long id, Class<Hashtag> className);
    
    Hashtag findHashtagByTag(String tag);

}
