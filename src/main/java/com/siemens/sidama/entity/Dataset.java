package com.siemens.sidama.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "datasets")
public class Dataset {
    /**
     * Dataset unique id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Dataset unique id")
    Long id;

    /**
     * Dataset name.
     */
    @ApiModelProperty(notes = "Dataset name", required = true)
    String name;

    /**
     * Dataset description.
     */
    @ApiModelProperty(notes = "Dataset description", required = true)
    String description;

    /**
     * User uploads this dataset.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    User uploadedBy;

    /**
     * Last update date.
     */
    @ApiModelProperty(notes = "Last update date", required = true)
    Date lastUpdated;

    /**
     * Dataset tags.
     */
    @ApiModelProperty(notes = "Dataset tags", required = true)
    String tags;

    /**
     * Dataset format.
     */
    @ApiModelProperty(notes = "Dataset format", required = true)
    String format;

    /**
     * Dataset size.
     */
    @ApiModelProperty(notes = "Dataset size", required = true)
    Long sizeInKiloBytes;

    /**
     * Dataset categories
     */
    @ManyToMany
    @JoinTable(
            name="dataset_categories",
            joinColumns = @JoinColumn( name="dataset_id"),
            inverseJoinColumns = @JoinColumn( name="category_id")
    )
    @JsonIgnore
    Set<DatasetCategory> datasetCategories;

    public Dataset(String name, String description, User uploadedBy, Date lastUpdated, String tags, String format, Long sizeInKiloBytes, Set<DatasetCategory> datasetCategories) {
        this.name = name;
        this.description = description;
        this.uploadedBy = uploadedBy;
        this.lastUpdated = lastUpdated;
        this.tags = tags;
        this.format = format;
        this.sizeInKiloBytes = sizeInKiloBytes;
        this.datasetCategories = datasetCategories;
    }
}
