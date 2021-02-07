package com.mageddo.tinyserverspring.dao;

import com.mageddo.tinyserverspring.entity.CustomerEntity;

public interface CustomerDAO {
    CustomerEntity findByID(long id);
}
