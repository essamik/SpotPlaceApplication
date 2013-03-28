/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.service;

import ch.heig.comem.model.Users;
import ch.heig.comem.services.UsersManagerLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author louis-samuel
 */
@Stateless
@Path("auth")
public class AuthREST {

    @PersistenceContext(unitName = "SpotPlaceApplicationPU")
    private EntityManager em;
    
    @EJB
    private UsersManagerLocal userManager;
    
    @POST
    @Consumes({"application/xml", "application/json"})
    @Produces({"text/plain"})
    public String verifExisitingUsers(Users entity){
        String idUser = "0";
        Users user = userManager.findUserByName(entity.getUserName());
        if (user != null){
            if (entity.getPassword().equals(user.getPassword())){
                idUser = user.getId().toString();
            }
        }
        return idUser;
    }
}
