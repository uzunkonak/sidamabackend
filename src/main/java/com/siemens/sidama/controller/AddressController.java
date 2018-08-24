package com.siemens.sidama.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.siemens.sidama.entity.Address;
import com.siemens.sidama.entity.User;
import com.siemens.sidama.service.AddressService;
import com.siemens.sidama.service.UserService;
import com.siemens.sidama.util.CustomErrorType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author FBG
 *
 */
@Slf4j
@RestController
//@RequestMapping("address")
@Api(value = "onlinestore-address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    // Request method to get list of all addresses
    @ApiOperation(value = "Request method to get list of all addresses", response = List.class)
    //@CrossOrigin
    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    public ResponseEntity<?> listAllAddresses() {
        List<Address> allAddresses = addressService.listAll();
        if (allAddresses == null) {
            log.error("WEIRD: empty list");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("WEIRD: empty list"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<Address>>(allAddresses, HttpStatus.OK);
    }

    // Request method to add an address for a user
    @ApiOperation(value = "Request method to add an address for a user", response = Address.class)
    //@CrossOrigin
    @RequestMapping(value = "/user/{userId}/address", method = RequestMethod.POST)
    public ResponseEntity<?> listAddressesForUser(@PathVariable (value = "userId") Long userId, @RequestBody Address newAddress) {
        log.info("userId: " + userId);
        User user = userService.find(userId);
        if (user == null) {
            log.error("WEIRD: user not found");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("WEIRD: user not found"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        newAddress.setUser(user);

        return new ResponseEntity<Address>(addressService.save(newAddress), HttpStatus.OK);
    }

    // Request method to get list of all addresses
    @ApiOperation(value = "Request method to get list of all addresses", response = List.class)
    //@CrossOrigin
    @RequestMapping(value = "/user/{userId}/address", method = RequestMethod.GET)
    public ResponseEntity<?> listAddressesForUser(@PathVariable (value = "userId") Long userId) {
        List<Address> addresses = addressService.listByUserId(userId);

        return new ResponseEntity<List<Address>>(addresses, HttpStatus.OK);
    }

    // Request method to delete an address
    @ApiOperation(value = "Request method to delete an address", response = Void.class)
    //@CrossOrigin
    @RequestMapping(value = "/address/{addrId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAddress(@PathVariable Long addrId) {
        try {
            addressService.find(addrId);
        } catch (NoSuchElementException e) {
            log.error("address with id " + addrId + " does not exist");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("address with id " + addrId + " does not exist"),
                    HttpStatus.NOT_FOUND);
        }
        addressService.delete(addrId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // Request method to update an address
    @ApiOperation(value = "Request method to delete an address", response = Address.class)
    //@CrossOrigin
    @RequestMapping(value = "/address/{addrId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateAddress(@PathVariable Long addrId, @RequestBody Address newAddress) {
        Address theAddress = null;

        try {
            theAddress = addressService.find(addrId);
        } catch (NoSuchElementException e) {
            log.error("address with id " + addrId + " does not exist");
            return new ResponseEntity<CustomErrorType>(
                    new CustomErrorType("address with id " + addrId + " does not exist"),
                    HttpStatus.NOT_FOUND);
        }
        newAddress.setId(theAddress.getId());
        newAddress.setUser(theAddress.getUser());

        return new ResponseEntity<Address>(addressService.save(newAddress), HttpStatus.OK);
    }
}
