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
public class HashtagDto implements Serializable {
    private static final Long serialVersionUID = 1L;
    
    private Long id;
    
    private String tag;
    private List<SpotDto> spots = new LinkedList<SpotDto>();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<SpotDto> getSpots() {
        return spots;
    }

    public void setSpots(List<SpotDto> spots) {
        this.spots = spots;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addSpot(SpotDto spot) {
        this.spots.add(spot);
    }
    
}
