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
@Table(name = "files")
public class File {
    /**
     * The database generated address id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "The database generated file id")
    Long id;

    /**
     * The application-specific file name.
     */
    @ApiModelProperty(notes = "The application-specific file name", required = true)
    String filename;

    /**
     * The application-specific file type.
     */
    @ApiModelProperty(notes = "The application-specific file type")
    String filetype;

    /**
     * The application-specific city.
     */
    @ApiModelProperty(notes = "The application-specific file size", required = false)
    Long filesize;

    /**
     * Description of the user.
     */
    //@ManyToOne
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public File(String name, String type, Long size, User user) {
        this.filename = name;
        this.filetype = type;
        this.filesize = size;
        this.user = user;
    }
}
