package com.eventsbuddy.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eventsbuddy.dao.ChairDao;
import com.eventsbuddy.dao.ProgramDao;
import com.eventsbuddy.dao.VenueDao;
import com.eventsbuddy.model.Chair;
import com.eventsbuddy.model.Program;
import com.eventsbuddy.model.ProgramChair;
import com.eventsbuddy.model.ProgramVenue;
import com.eventsbuddy.model.Venue;

@Service
@Transactional(readOnly = true)
public class ProgramService {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramService.class);

	@Autowired
	private ChairDao chairDao;

	@Autowired
	private VenueDao venueDao;

	@Autowired
	private ProgramDao programDao;

	public Program findById(Long id) {
		return programDao.findOne(id);
	}
	
	public List<Program> findAll() {
		return (List<Program>) programDao.findAll();
	}

	public List<Date> findEventDates() {
		return programDao.findEventDates();
	}

	public List<Program> findProgramsByBeginDate(Date eventDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(eventDate);
		cal.add(Calendar.DATE, 1);
		Date nextEventDate = cal.getTime();

		return programDao.findProgramByBeginDate(eventDate, nextEventDate);
	}

	@Transactional
	public Program createProgram(Program program, Set<ProgramChair> programChairs, Set<ProgramVenue> programVenues) {
		Program createdProgram = null;
		Chair existedChair = null;
		Venue existedVenue = null;

		// Creates Chair
		if (programChairs != null && !programChairs.isEmpty()) {
			for (ProgramChair programChair : programChairs) {
				existedChair = chairDao.findByName(programChair.getChair().getName());

				if (existedChair != null) {
					programChair.setChair(existedChair);
				}

				chairDao.save(programChair.getChair());
			}

			program.getProgramChairs().addAll(programChairs);
		}

		// Creates Venue
		if (programVenues != null && !programVenues.isEmpty()) {
			for (ProgramVenue programVenue : programVenues) {
				existedVenue = venueDao.findByName(programVenue.getVenue().getName());

				if (existedVenue != null) {
					programVenue.setVenue(existedVenue);
				}

				venueDao.save(programVenue.getVenue());
			}
			program.getProgramVenues().addAll(programVenues);
		}

		// Creates Program
		createdProgram = programDao.save(program);

		return createdProgram;
	}

}
