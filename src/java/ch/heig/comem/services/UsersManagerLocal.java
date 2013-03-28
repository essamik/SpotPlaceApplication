/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Users;
import javax.ejb.Local;

/**
 *
 * @author louis-samuel
 */
@Local
public interface UsersManagerLocal {
    Users createUser(String userName, String password, String email, String canton);
    Users updateUser(long id, String oldUserName, String oldPassword, String oldEmail, String oldCanton);
    void deleteUser(long id);
    Users findUserById(Class<Users> className, long id);
    Users findUserByName(String name);
    String getPlayerIdByUser(Users user);
}