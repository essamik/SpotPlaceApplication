/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.services;

import ch.heig.comem.model.Comment;
import javax.ejb.Local;

/**
 *
 * @author louis-samuel
 */
@Local
public interface CommentManagerLocal {
    Comment createComment(String content, Long idSpot, Long idUser);
    
    Comment updateComment(String content, Long id);
    
    void deleteComment(Long id);
    
    Comment findCommentById(Long id, Class<Comment> className);
}
