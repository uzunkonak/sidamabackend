package com.siemens.sidama.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Description of User.
 * 
 * @author FBG
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    /**
     * The database generated address id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated address id")
    Long id;

    /**
     * The application-specific address line1.
     */
    @ApiModelProperty(notes = "The application-specific address line1", required = true)
    String line1;

    /**
     * The application-specific address line2.
     */
    @ApiModelProperty(notes = "The application-specific address line2")
    String line2;

    /**
     * The application-specific city.
     */
    @ApiModelProperty(notes = "The application-specific city", required = true)
    String city;

    /**
     * Description of the user.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public Address(String line1, String line2, String city) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
    }
}
