package com.siemens.sidama.controller;

import com.siemens.sidama.entity.Dataset;
import com.siemens.sidama.service.DatasetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(value = "onlinestore-dataset")
public class DatasetController {

    @Autowired
    private DatasetService datasetService;

    // Request method to get list of all datasets
    @ApiOperation(value = "Request method to get list of all datasets", response = List.class)
    @CrossOrigin
    @RequestMapping(value = "/dataset/list", method = RequestMethod.GET)
    public ResponseEntity<?> listAllDatasets() {
        List<Dataset> allDatasets = datasetService.allDatasets();

        return new ResponseEntity<>(allDatasets, HttpStatus.OK);
    }
}
