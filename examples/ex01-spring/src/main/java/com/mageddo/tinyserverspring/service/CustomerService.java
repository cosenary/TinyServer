package com.mageddo.tinyserverspring.service;

import com.mageddo.tinyserverspring.dao.CustomerDAO;
import com.mageddo.tinyserverspring.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    public CustomerEntity findByID(long id){
        return customerDAO.findByID(id);
    }
}
