package com.siemens.sidama.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class DatasetCategory {
    /**
     * Dataset category unique id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Dataset category id")
    Long id;

    /**
     * Dataset category name.
     */
    @ApiModelProperty(notes = "Dataset category name", required = true)
    String name;

    /**
     * Dataset categories
     */
    @ManyToMany
    @JoinTable(
            name="dataset_categories",
            joinColumns = @JoinColumn( name="category_id"),
            inverseJoinColumns = @JoinColumn( name="dataset_id")
    )
    @JsonIgnore
    Set<Dataset> datasets;

    public DatasetCategory(String name, Set<Dataset> datasets) {
        this.name = name;
        this.datasets = datasets;
    }
}
