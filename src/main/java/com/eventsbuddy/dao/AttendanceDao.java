package com.eventsbuddy.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eventsbuddy.model.Attendance;

@Repository
public interface AttendanceDao extends CrudRepository<Attendance, Long> {

}
