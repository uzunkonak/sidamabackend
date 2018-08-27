package com.siemens.sidama.service;

import com.siemens.sidama.entity.Dataset;
import com.siemens.sidama.entity.DatasetCategory;
import com.siemens.sidama.repository.DatasetCategoryRepository;
import com.siemens.sidama.repository.DatasetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasetService {

    @Autowired
    DatasetRepository datasetRepository;

    @Autowired
    DatasetCategoryRepository datasetCategoryRepository;

    public List<Dataset> allDatasets() {
        return datasetRepository.findAll();
    }

    public List<DatasetCategory> allDatasetCategories() {
        return datasetCategoryRepository.findAll();
    }

    public Dataset saveDataset(Dataset dataset) {
        return datasetRepository.save(dataset);
    }

    public DatasetCategory saveDatasetCategory(DatasetCategory datasetCategory) {
        return datasetCategoryRepository.save(datasetCategory);
    }
}
