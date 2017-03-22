package com.eventsbuddy.utils;

import com.eventsbuddy.dto.AttendanceDto;
import com.eventsbuddy.model.Attendance;

public class AttendanceUtils {

	public static <T extends AttendanceDto> Attendance fromAttendanceDtoToDomainAttendance(T attendanceDto) {
		Attendance attendance = new Attendance();
		attendance.setFullName(attendanceDto.getFullName());
		attendance.setEmail(attendanceDto.getEmail());
		attendance.setReference(attendanceDto.getReference());
		return attendance;
	}

}
