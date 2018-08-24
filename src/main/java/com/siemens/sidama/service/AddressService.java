package com.siemens.sidama.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siemens.sidama.entity.Address;
import com.siemens.sidama.repository.AddressRepository;

/**
 * @author FBG
 *
 */
@Service
public class AddressService {

    @Autowired
    AddressRepository addressRepository;

    public Address save(Address address) {
        return addressRepository.saveAndFlush(address);
    }

    public Address update(Address address) {
        return addressRepository.save(address);
    }

    public Address find(Long id) {
        return addressRepository.findById(id).get();
    }

    public List<Address> listAll() {
        List<Address> addresses = new ArrayList<>();
        addressRepository.findAll().forEach(addresses::add);
        return addresses;
    }

    public List<Address> listByUserId(Long userId) {
        List<Address> addresses = new ArrayList<>();
        addressRepository.findByUserId(userId).forEach(addresses::add);
        return addresses;
    }

    public void delete(Long addrId) {
        addressRepository.deleteById(addrId);
    }
}
