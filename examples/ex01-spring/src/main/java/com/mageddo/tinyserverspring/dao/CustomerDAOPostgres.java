package com.mageddo.tinyserverspring.dao;

import com.mageddo.tinyserverspring.entity.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAOPostgres implements CustomerDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public CustomerEntity findByID(long id){
        return jdbcTemplate.queryForObject("SELECT * FROM CUSTOMER", CustomerEntity.mapper());
    }
}
