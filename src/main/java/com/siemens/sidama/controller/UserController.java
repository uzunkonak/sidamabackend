package com.siemens.sidama.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.sidama.entity.User;
import com.siemens.sidama.service.UserService;
import com.siemens.sidama.util.CustomErrorType;
import com.siemens.sidama.util.SidamaUtil;

import org.springframework.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author FBG
 *
 */
@Slf4j
@RestController
@RequestMapping("user")
@Api(value = "onlinestore-user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Request method to create a new account by a guest
    @ApiOperation(value = "Request method to create a new account by a guest", response = User.class)
    @CrossOrigin
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        String newUsername = newUser.getUsername();
        if (StringUtils.isEmpty(newUsername)) {
            log.error("username should not be empty");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("username should not be empty"),
                    HttpStatus.BAD_REQUEST);
        }
        if (!SidamaUtil.validateEmail(newUsername)) {
            log.error("not a valid e-mail address");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("not a valid e-mail address"),
                    HttpStatus.BAD_REQUEST);
        }
        if (userService.find(newUsername) != null) {
            log.error("user with username " + newUsername + " already exists");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("user with username " + newUsername + " already exists"),
                    HttpStatus.CONFLICT);
        }
        newUser.setRole("USER");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return new ResponseEntity<User>(userService.save(newUser), HttpStatus.CREATED);
    }

    // Request method to get details of a user
    @ApiOperation(value = "Request method to get a user", response = User.class)
    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User theUser = null;
        try {
            theUser = userService.find(id);
        } catch (NoSuchElementException e) {
            log.error("user with id " + id + " does not exist");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("user with id " + id + " does not exist"),
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<User>(theUser, HttpStatus.OK);
    }

    // Request method to get list of all users
    @ApiOperation(value = "Request method to get list of all users", response = List.class)
    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<?> listAllUsers() {
        List<User> allUsers = userService.list();
        if (allUsers == null) {
            log.error("WEIRD: empty list");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("WEIRD: empty list"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<User>>(allUsers, HttpStatus.OK);
    }

    // Request method to delete a user
    @ApiOperation(value = "Request method to delete a user", response = Void.class)
    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.find(id);
        } catch (NoSuchElementException e) {
            log.error("user with id " + id + " does not exist");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("user with id " + id + " does not exist"),
                    HttpStatus.NOT_FOUND);
        }
        userService.delete(id);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // Request method to get list of all users
    @ApiOperation(value = "Request method to update a user's information", response = User.class)
    @CrossOrigin
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        User theUser = null; 
        try {
            theUser = userService.find(id);
        } catch (NoSuchElementException e) {
            log.error("user with id " + id + " does not exist");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("user with id " + id + " does not exist"),
                    HttpStatus.NOT_FOUND);
        }
        newUser.setId(theUser.getId());
        //newUser.setAddress(theUser.getAddress());
        newUser.setRole(theUser.getRole());

        return new ResponseEntity<User>(userService.update(newUser), HttpStatus.OK);
    }
}
