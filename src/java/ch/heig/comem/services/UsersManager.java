/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Users;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author louis-samuel
 */
@Stateless
public class UsersManager implements UsersManagerLocal {

    @PersistenceContext
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
   
    
    private String createPlayerOnEngine(String userName, String email) {
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        WebResource r = client.resource("http://localhost:8088/SpotPlaceMoteur-master/webresources/players");
        String jsonObject = "{\"userName\" : \"" + userName + "\", \"mail\" : \"" + email + "\"}";
        
        ClientResponse response = r.type(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonObject);
        return response.getLocation().toString();
    }

    @Override
    public Users createUser(String userName, String password, String email, String canton) {
        Users user = new Users();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.setCanton(canton);
        
        String playerURL2 = createPlayerOnEngine(userName, email);
        
        user.setPlayerURL(playerURL2);
        
        em.persist(user);
        em.flush();

        return user;
    }
    
    

    @Override
    public Users updateUser(long id, String newUserName, String newPassword, String newEmail, String newCanton) {
        Users oldUser = this.findUserById(Users.class, id);
        oldUser.setCanton(newCanton);
        oldUser.setEmail(newEmail);
        oldUser.setId(id);
        oldUser.setPassword(newPassword);
        //oldUser.setPlayerURL(newPlayerURL);
        oldUser.setUserName(newUserName);
        em.persist(oldUser);
        em.flush();
        return oldUser;
    }

    @Override
    public void deleteUser(long id) {
        Users oldUser;
        oldUser = this.findUserById(Users.class, id);
        em.remove(oldUser);
        em.flush();
    }

    @Override
    public Users findUserById(Class<Users> className, long id) {
        Users user = em.find(className, id);
        user.getSpots();
        return user;
    }

    @Override
    public Users findUserByName(String name) {
        Query q = em.createNamedQuery("findUserByName");
        q.setParameter("name", name);
        List<Users> candidates = q.getResultList();
        if (candidates.isEmpty()) {
            return null;
        } else {
            return candidates.get(0);
        }
    }

    @Override
    public String getPlayerIdByUser(Users user) {
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
    
    // getAssiciatedPlayersBadges
    // getAssiciatedPlayersScore
    // getAssiciatedPlayersEvents

    
    
    
}
