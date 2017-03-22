package com.eventsbuddy.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eventsbuddy.model.Chair;

@Repository
public interface ChairDao extends CrudRepository<Chair, Long> {

	public Chair findByName(String name);

}
