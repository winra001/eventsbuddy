package com.eventsbuddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventsbuddy.dao.AttendanceDao;
import com.eventsbuddy.model.Attendance;

@Service
@Transactional(readOnly = true)
public class AttendanceService {

	private static final Logger LOG = LoggerFactory.getLogger(AttendanceService.class);

	@Autowired
	private AttendanceDao attendanceDao;

	@Transactional
	public Attendance createAttendance(Attendance attendance) {
		Attendance localAttendance = attendanceDao.save(attendance);
		return localAttendance;
	}

}
