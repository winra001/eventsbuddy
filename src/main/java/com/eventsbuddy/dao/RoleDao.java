package com.eventsbuddy.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eventsbuddy.model.security.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Integer> {

}
