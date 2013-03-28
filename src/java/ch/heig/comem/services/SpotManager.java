/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Hashtag;
import ch.heig.comem.model.Spot;
import ch.heig.comem.model.Users;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author louis-samuel
 */
@Stateless
public class SpotManager implements SpotManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private SpotManagerLocal spotManager;
    @EJB
    private HashtagManagerLocal hashtagManager;
    @EJB
    private UsersManagerLocal userManager;
    @EJB
    private EventMoteurManagerLocal eventMoteurManager;

    public void persist(Object object) {
        em.persist(object);
    }

    private String getPlayerIdByUser(Users user) {
        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource r = client.resource(user.getPlayerURL());
            ClientResponse response = r.accept("application/json").get(ClientResponse.class);
            JSONObject player = response.getEntity(JSONObject.class);
            return player.getString("id");
        } catch (JSONException ex) {
            Logger.getLogger(CommentManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Spot createSpot(String title, String description, float latitude, float longitude, int rating, String urlPhoto, List<Hashtag> tags, Long idUser) {
        Spot spot = new Spot();
        spot.setDescription(description);
        spot.setLatitude(latitude);
        spot.setLongitude(longitude);
        spot.setRating(rating);
        spot.setUrlPhoto(urlPhoto);
        spot.setTitle(title);
        em.persist(spot);
        em.flush();
        Users user = userManager.findUserById(Users.class, idUser);
        spot.setUser(user);

        for (Hashtag tag : tags) {
            Hashtag tagTest = hashtagManager.findHashtagByTag(tag.getTag());
            if (tagTest == null) {
                tagTest = hashtagManager.createHashtag(tag.getTag());
            }
            spotManager.addHashtag(spot.getId(), tagTest.getId());
        }
        this.eventMoteurManager.createEventOnEngine("spot", user);

        user.addSpot(spot);

        if (user.getSpots().size() > 20) {
            this.eventMoteurManager.createEventOnEngine("spots", user);
        }

        em.persist(user);
        em.persist(spot);
        em.flush();
        return spot;
    }

    @Override
    public Spot updateSpot(Long id) {
        Spot oldSpot = this.findSpotById(id, Spot.class);
        oldSpot.setRating(oldSpot.getRating() + 1);
        this.eventMoteurManager.createEventOnEngine("like", oldSpot.getUser());
        em.persist(oldSpot);
        em.flush();
        return oldSpot;
    }

    @Override
    public void deleteSpot(Long id) {
        Spot spotToDelete;
        spotToDelete = this.findSpotById(id, Spot.class);
        em.remove(spotToDelete);
        em.flush();
    }

    @Override
    public Spot findSpotById(Long id, Class<Spot> className) {
        Spot spot = em.find(Spot.class, id);
        return spot;
    }

    @Override
    public Spot addHashtag(Long idSpot, Long idHashtag) {
        Hashtag hashtag = hashtagManager.findHashtagById(idHashtag, Hashtag.class);
        Spot spot = this.spotManager.findSpotById(idSpot, Spot.class);
        spot.addHashtag(hashtag);
        hashtag.addSpot(spot);
        this.em.persist(spot);
        return spot;
    }
}
