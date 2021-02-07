//package com.mageddo.tinyserverspring.controller;
//
//import com.mageddo.tinyserverspring.entity.CustomerEntity;
//import com.mageddo.tinyserverspring.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class SpringCustomerController {
//
//    @Autowired
//    private CustomerService customerService;
//
//    @RequestMapping("/customers/{id}")
//    public ResponseEntity<CustomerEntity> afterPropertiesSet(@PathVariable("id") long id) throws Exception {
//        return ResponseEntity.ok(customerService.findByID(id));
//    }
//}
