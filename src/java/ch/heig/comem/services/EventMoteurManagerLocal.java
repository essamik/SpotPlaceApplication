/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Users;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Pascal
 */
@Local
public interface EventMoteurManagerLocal {
    public void createEventOnEngine(String type, Users user);
     public String getPlayerIdByUser(Users user);
      public long getPlayerPoints(Users user);
     public List<String> getBadgePlayer(String playerId);
     public void createPointsEventOnEngine(String type, Users user);
}
