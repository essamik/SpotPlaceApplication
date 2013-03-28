/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.service;

import ch.heig.comem.services.HashtagManagerLocal;
import ch.heig.comem.model.Hashtag;
import ch.heig.comem.model.Spot;
import ch.heig.comem.model.dto.HashtagDto;
import ch.heig.comem.model.dto.SpotDto;
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
@Path("hashtags")
public class HashtagFacadeREST extends AbstractFacade<Hashtag> {
    
    @EJB
    private HashtagManagerLocal hashtagManager;
    
    @PersistenceContext(unitName = "SpotPlaceApplicationPU")
    private EntityManager em;

    public HashtagFacadeREST() {
        super(Hashtag.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Hashtag entity) {
        if(hashtagManager.findHashtagByTag(entity.getTag())==null){
            hashtagManager.createHashtag(entity.getTag());
        }
    }

    
    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Hashtag entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public HashtagDto find(@PathParam("id") Long id) {
        Hashtag tag = super.find(id);
        HashtagDto tagDto = new HashtagDto();
        tagDto.setId(tag.getId());
        tagDto.setTag(tag.getTag());
        List<SpotDto> spots = new LinkedList<SpotDto>();
        for(Spot spot : tag.getSpots()){
            SpotDto spotDto = new SpotDto();
            spotDto.setId(spot.getId());
            spotDto.setTitle(spot.getTitle());
            spots.add(spotDto);
        }
        tagDto.setSpots(spots);
        
        return tagDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<HashtagDto> findAllHashtags() {
        List<Hashtag> tags = super.findAll();
        List<HashtagDto> tagsDto = new LinkedList<HashtagDto>();
        for (Hashtag tag : tags){
            HashtagDto tagDto = new HashtagDto();
            tagDto.setId(tag.getId());
            tagDto.setTag(tag.getTag());
            tagsDto.add(tagDto);
        }

        return tagsDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Hashtag> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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