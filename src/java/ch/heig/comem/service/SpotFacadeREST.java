/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.service;

import ch.heig.comem.model.Comment;
import ch.heig.comem.model.Hashtag;
import ch.heig.comem.model.Spot;
import ch.heig.comem.model.Users;
import ch.heig.comem.model.dto.CommentDto;
import ch.heig.comem.model.dto.HashtagDto;
import ch.heig.comem.model.dto.SpotDto;
import ch.heig.comem.model.dto.UsersDto;
import ch.heig.comem.services.SpotManagerLocal;
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
@Path("spots")
public class SpotFacadeREST extends AbstractFacade<Spot> {

    @PersistenceContext(unitName = "SpotPlaceApplicationPU")
    private EntityManager em;
    @EJB
    private SpotManagerLocal spotManager;

    public SpotFacadeREST() {
        super(Spot.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Spot entity) {
        spotManager.createSpot(entity.getTitle(), entity.getDescription(), entity.getLatitude(), entity.getLongitude(), entity.getRating(), entity.getUrlPhoto(), entity.getHashtags(), entity.getUser().getId());
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Spot entity) {
        spotManager.updateSpot(entity.getId());
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        spotManager.deleteSpot(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public SpotDto find(@PathParam("id") Long id) {
        Spot spot = spotManager.findSpotById(id, Spot.class);
        SpotDto spotDto = new SpotDto();
        spotDto.setId(spot.getId());
        spotDto.setTitle(spot.getTitle());
        spotDto.setDescription(spot.getDescription());
        spotDto.setLatitude(spot.getLatitude());
        spotDto.setLongitude(spot.getLongitude());
        spotDto.setRating(spot.getRating());
        spotDto.setTitle(spot.getTitle());
        spotDto.setUrlPhoto(spot.getUrlPhoto());

        UsersDto userDto = new UsersDto();
        userDto.setId(spot.getUser().getId());
        userDto.setUserName(spot.getUser().getUserName());
        userDto.setPlayerURL(spot.getUser().getPlayerURL());
        spotDto.setUser(userDto);

        List<CommentDto> commentsDto = new LinkedList<CommentDto>();
        for (Comment comment : spot.getComments()) {
            CommentDto commentDto = new CommentDto();
            commentDto.setContent(comment.getContent());
            commentDto.setId(comment.getId());

            UsersDto userCommentDto = new UsersDto();
            userCommentDto.setId(comment.getUser().getId());
            userCommentDto.setUserName(comment.getUser().getUserName());
            commentDto.setUser(userCommentDto);

            commentsDto.add(commentDto);
        }
        spotDto.setComments(commentsDto);

        List<HashtagDto> tagsDto = new LinkedList<HashtagDto>();
        for (Hashtag tag : spot.getHashtags()) {
            HashtagDto tagDto = new HashtagDto();
            tagDto.setId(tag.getId());
            tagDto.setTag(tag.getTag());
            tagsDto.add(tagDto);
        }
        spotDto.setHashtags(tagsDto);
        return spotDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<SpotDto> findAllSpots() {
        List<Spot> spots = super.findAll();
        List<SpotDto> spotsDto = new LinkedList<SpotDto>();
        for (Spot spot : spots) {
            SpotDto spotDto = new SpotDto();
            spotDto.setId(spot.getId());
            spotDto.setTitle(spot.getTitle());
            spotDto.setDescription(spot.getDescription());
            spotDto.setRating(spot.getRating());
            spotDto.setUrlPhoto(spot.getUrlPhoto());
            spotsDto.add(spotDto);
        }
        return spotsDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Spot> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
