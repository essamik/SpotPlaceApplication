/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.heig.comem.model.dto;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author louis-samuel
 */
@XmlRootElement
public class SpotDto implements Serializable {
    private static final Long serialVersionUID = 1L;
    
    private Long id;
    
    private String title;
    private String description;
    private Float latitude;
    private Float longitude;
    private Integer rating;
    private String urlPhoto;
    
    private List<CommentDto> comments = new LinkedList<CommentDto>();
    
    private List<HashtagDto> hashtags = new LinkedList<HashtagDto>();
    
    private UsersDto user;

    public UsersDto getUser() {
        return user;
    }

    public void setUser(UsersDto user) {
        this.user = user;
    }
    
    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public List<HashtagDto> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<HashtagDto> hashtags) {
        this.hashtags = hashtags;
    }
    
    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }
    
    public void addComment(CommentDto comment){
        this.comments.add(comment);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void addHashtag(HashtagDto hashtag) {
        this.hashtags.add(hashtag);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
