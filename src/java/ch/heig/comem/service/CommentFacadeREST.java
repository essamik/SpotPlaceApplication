/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.service;

import ch.heig.comem.model.Comment;
import ch.heig.comem.model.dto.CommentDto;
import ch.heig.comem.model.dto.SpotDto;
import ch.heig.comem.model.dto.UsersDto;
import ch.heig.comem.services.CommentManagerLocal;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author louis-samuel
 */
@Stateless
@Path("comments")
public class CommentFacadeREST extends AbstractFacade<Comment> {
    
    @PersistenceContext(unitName = "SpotPlaceApplicationPU")
    private EntityManager em;
    
    @EJB
    private CommentManagerLocal commentManager;

    public CommentFacadeREST() {
        super(Comment.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Comment entity) {
        commentManager.createComment(entity.getContent(), entity.getSpot().getId(), entity.getUser().getId());
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Comment entity) {
        commentManager.updateComment(entity.getContent(), entity.getId());
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        commentManager.deleteComment(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public CommentDto find(@PathParam("id") Long id) {
        
        Comment comment = super.find(id);
        CommentDto commentDto = new CommentDto();
        UsersDto userDto = new UsersDto();
        userDto.setUserName(comment.getUser().getUserName());
        userDto.setId(comment.getUser().getId());
        commentDto.setUser(userDto);
        commentDto.setContent(comment.getContent());
        commentDto.setId(comment.getId());
        
        SpotDto spotDto = new SpotDto();
        spotDto.setId(comment.getSpot().getId());
        spotDto.setTitle(comment.getSpot().getTitle());
        commentDto.setSpot(spotDto);
        
        return commentDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<CommentDto> findAllComments() {
        
        List<Comment> comments = super.findAll();
        List<CommentDto> commentsDto = new LinkedList<CommentDto>();
        
        for(Comment comment : comments){
            CommentDto commentDto = new CommentDto();
            commentDto.setContent(comment.getContent());
            commentDto.setId(comment.getId());
            commentsDto.add(commentDto);
        }
        return commentsDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Comment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
