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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Pascal
 */
@Stateless
public class EventMoteurManager implements EventMoteurManagerLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @EJB
    private UsersManagerLocal userManager;

    
    @Override
    public void createPointsEventOnEngine(String type, Users user) {
         String playerId = this.getPlayerIdByUser(user);

        ClientConfig config = new DefaultClientConfig();
        Client client2 = Client.create(config);
        WebResource r2 = client2.resource("http://127.0.0.1:8080/SpotPlaceMoteur-master/webresources/events");
        String jsonObject = "";
        jsonObject = "{\"application\": {\"id\": \"1\"},\"player\": {\"id\": \"" + playerId + "\"},\"type\": \"" + type + "\"}";

        ClientResponse response2 = r2.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonObject);
    }

    @Override
    public void createEventOnEngine(String type, Users user) {

        String playerId = this.getPlayerIdByUser(user);

        ClientConfig config = new DefaultClientConfig();
        Client client2 = Client.create(config);
        WebResource r2 = client2.resource("http://127.0.0.1:8080/SpotPlaceMoteur-master/webresources/events");
        String jsonObject = "";
        jsonObject = "{\"application\": {\"id\": \"1\"},\"player\": {\"id\": \"" + playerId + "\"},\"type\": \"" + type + "\"}";

        ClientResponse response2 = r2.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonObject);


        long pts = this.getPlayerPoints(user); //OK
        
        List<String> badges = this.getBadgePlayer(playerId);
        
        if (pts >= 20 && pts < 45) {
           if(!badges.contains("Medaille de chocolat")){
            this.createPointsEventOnEngine("20", user);
            }
        }
         if (pts >= 45 && pts < 70) {
            if(!badges.contains("Medaille de bronze")){
           this.createPointsEventOnEngine("45", user);
            }
        }
         if (pts >= 70 && pts < 100) {
            if(!badges.contains("Medaille d'argent")){
            this.createPointsEventOnEngine("70", user);
            }
        }
        if (pts >= 100) {
            if(!badges.contains("Medaille d'or")){
            this.createPointsEventOnEngine("100", user);
            }
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

    @Override
    public long getPlayerPoints(Users user) {
        long total = 0;
        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource r = client.resource(user.getPlayerURL());
            ClientResponse response = r.accept("application/json").get(ClientResponse.class);
            JSONObject player = response.getEntity(JSONObject.class);
            return player.getLong("score");
        } catch (JSONException ex) {
            Logger.getLogger(CommentManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public List<String> getBadgePlayer(String playerId) {

        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            WebResource r = client.resource("http://127.0.0.1:8080/SpotPlaceMoteur-master/webresources/players/" + playerId);
            ClientResponse response = r.accept("application/json").get(ClientResponse.class);
            JSONObject player = response.getEntity(JSONObject.class);

            JSONArray badges = player.getJSONArray("badges");

            List<String> listeBadges = new LinkedList<String>();

            for (int i = 0; i <= badges.length(); i++) {
                JSONObject jo = badges.getJSONObject(i);
                listeBadges.add(jo.getString("name"));
            }

            return listeBadges;



        } catch (JSONException ex) {
            Logger.getLogger(CommentManager.class.getName()).log(Level.SEVERE, null, ex);
            return new LinkedList<String>();
        }

    }
}
