/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.model.dto;

import ch.heig.comem.model.Comment;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
/**
 *
 * @author louis-samuel
 */
@XmlRootElement
public class UsersDto implements Serializable {
    private static final Long serialVersionUID = 1L;
    
    private Long id;
    private String userName;
    private String password;
    private String email;
    private String canton;
    private String playerURL;
    private int score;
    
    private List<SpotDto> spots = new LinkedList<SpotDto>();

    private List<CommentDto> comments = new LinkedList<CommentDto>();

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCanton() {
        return canton;
    }

    public void setCanton(String caton) {
        this.canton = caton;
    }

    public String getPlayerURL() {
        return playerURL;
    }

    public void setPlayerURL(String playerURL) {
        this.playerURL = playerURL;
    }

    public List<SpotDto> getSpots() {
        return spots;
    }

    public void setSpots(List<SpotDto> spots) {
        this.spots = spots;
    }
    
    public void addSpot(SpotDto spot){
        this.spots.add(spot);
    }
    
    public void addComment(CommentDto comment){
        this.comments.add(comment);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
