/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Comment;
import ch.heig.comem.model.Spot;
import ch.heig.comem.model.Users;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.lang.Class;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author louis-samuel
 */
@Stateless
public class CommentManager implements CommentManagerLocal {

    @PersistenceContext
    private EntityManager em;
    @EJB
    private UsersManagerLocal userManager;
    @EJB
    private EventMoteurManagerLocal eventMoteurManager;

    public void persist(Object object) {
        em.persist(object);
    }    

    @Override
    public Comment createComment(String content, Long idSpot, Long idUser) {
        Comment comment = new Comment();
        comment.setContent(content);
        Spot spot = em.find(Spot.class, idSpot);
        
        comment.setSpot(spot);
        Users user = em.find(Users.class, idUser);
        comment.setUser(user);

        this.eventMoteurManager.createEventOnEngine("comment", user);
        spot.addComment(comment);
        user.addComment(comment);
        
        if(user.getComments().size()>20){
        this.eventMoteurManager.createEventOnEngine("comments", user);
        }
        
        em.persist(user);
        em.persist(comment);
        em.flush();

        return comment;
    }

    @Override
    public Comment updateComment(String content, Long id) {
        Comment oldComment = this.findCommentById(id, Comment.class);
        oldComment.setContent(content);
        em.persist(oldComment);
        em.flush();
        
        return oldComment;
    }

    @Override
    public void deleteComment(Long id) {
        Comment commentToDelete;
        commentToDelete = this.findCommentById(id, Comment.class);
        em.remove(commentToDelete);
        em.flush();
    }

    @Override
    public Comment findCommentById(Long id, Class<Comment> ClassName) {
        Comment comment = em.find(ClassName, id);
        //comment.getSpot();
        //comment.getUser();
        return comment;
    }
}
