package com.mageddo.tinyserverspring.entity;

import org.springframework.jdbc.core.RowMapper;

public class CustomerEntity {

    private long id;
    private String name;

    public static RowMapper<CustomerEntity> mapper() {
        return (rs, rowNum) -> {
            final CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(rs.getLong("IDT_CUSTOMER"));
            customerEntity.setName(rs.getString("NAM_CUSTOMER"));
            return customerEntity;
        };
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
