/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.model.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author louis-samuel
 */
@XmlRootElement
public class CommentDto implements Serializable {
    private static final Long serialVersionUID = 1L;
    
    private Long idDto;
    private String contentDto;
    private UsersDto userDto;
    private SpotDto spotDto;

    public UsersDto getUser() {
        return userDto;
    }

    public void setUser(UsersDto user) {
        this.userDto = user;
    }

    public SpotDto getSpot() {
        return spotDto;
    }

    public void setSpot(SpotDto spot) {
        this.spotDto = spot;
    }

    public String getContent() {
        return contentDto;
    }

    public void setContent(String content) {
        this.contentDto = content;
    }
    
    public Long getId() {
        return idDto;
    }

    public void setId(Long id) {
        this.idDto = id;
    }

    public CommentDto(){
        
    }    
}
