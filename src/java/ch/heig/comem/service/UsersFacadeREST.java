/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.service;

import ch.heig.comem.model.Spot;
import ch.heig.comem.services.UsersManagerLocal;
import ch.heig.comem.model.Users;
import ch.heig.comem.model.dto.SpotDto;
import ch.heig.comem.model.dto.UsersDto;
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
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @EJB
    private UsersManagerLocal userManager;
    @PersistenceContext(unitName = "SpotPlaceApplicationPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Users entity) {
        if (userManager.findUserByName(entity.getUserName()) == null) {
            userManager.createUser(entity.getUserName(), entity.getPassword(), entity.getEmail(), entity.getCanton());
        }
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Users entity) {
        userManager.updateUser(entity.getId(), entity.getUserName(), entity.getPassword(), entity.getEmail(), entity.getCanton());
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        userManager.deleteUser(id);
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public UsersDto find(@PathParam("id") Long id) {
        Users user = userManager.findUserById(Users.class, id);
        //String playerId = this.userManager.getPlayerIdByUser(user);


        UsersDto userDto = new UsersDto();

        userDto.setCanton(user.getCanton());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPlayerURL(user.getPlayerURL());
        userDto.setUserName(user.getUserName());

        List<SpotDto> spotsDto = new LinkedList<SpotDto>();
        for (Spot spot : user.getSpots()) {
            SpotDto spotDto = new SpotDto();
            spotDto.setId(spot.getId());
            spotDto.setTitle(spot.getTitle());
            spotsDto.add(spotDto);
        }
        userDto.setSpots(spotsDto);
        return userDto;
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<UsersDto> findAllUsers() {
        List<Users> users = super.findAll();
        List<UsersDto> usersDto = new LinkedList<UsersDto>();
        for (Users user : users) {
            UsersDto userDto = new UsersDto();
            userDto.setUserName(user.getUserName());
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            usersDto.add(userDto);
        }
        return usersDto;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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