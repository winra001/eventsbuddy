package com.eventsbuddy.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eventsbuddy.dto.ChairDto;
import com.eventsbuddy.dto.ProgramDto;
import com.eventsbuddy.dto.VenueDto;
import com.eventsbuddy.model.Chair;
import com.eventsbuddy.model.Program;
import com.eventsbuddy.model.ProgramChair;
import com.eventsbuddy.model.ProgramVenue;
import com.eventsbuddy.model.Venue;
import com.eventsbuddy.service.PoiService;
import com.eventsbuddy.service.ProgramService;
import com.eventsbuddy.utils.ProgramUtils;

@Controller
public class ProgramController {

	private static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);

	private final String IMPORT_PROGRAM_URL_MAPPING = "/importPrograms";

	private final String IMPORT_PROGRAM_VIEW_NAME = "program/importPrograms";

	public final String IMPORT_PROGRAM_MESSAGE_KEY = "imported";

	@Autowired
	private PoiService poiService;

	@Autowired
	private ProgramService programService;

	/**
	 * Fetches Program
	 * 
	 * @param id
	 *            The program id
	 * @return Program
	 */
	// For Modal
	/*@RequestMapping(value = "/program/{id}", method = RequestMethod.GET)
	public @ResponseBody Program programGet(@PathVariable("id") Long id) {
		Program program = programService.findById(id);
		return program;
	}*/

	@RequestMapping("/program/{id}")
	public String programGet(@PathVariable("id") Long id, ModelMap model) {
		Program program = programService.findById(id);
		model.addAttribute("program", program);
		return "program/program";
	}

	/**
	 * Fetches Program list
	 */
	@RequestMapping("/programs")
	public String programsGet(ModelMap model) {
		Map<Date, List<Program>> programList = new HashMap<Date, List<Program>>();

		List<Date> eventDates = programService.findEventDates();
		for (Date eventDate : eventDates) {
			List<Program> programs = programService.findProgramsByBeginDate(eventDate);
			programList.put(eventDate, programs);
		}

		model.addAttribute("eventDates", eventDates);
		model.addAttribute("programList", programList);
		return "program/programs";
	}

	@RequestMapping(value = IMPORT_PROGRAM_URL_MAPPING, method = RequestMethod.GET)
	public String importProgramsGet(ModelMap model) {
		return IMPORT_PROGRAM_VIEW_NAME;
	}

	/**
	 * Imports Programs from Excel
	 */
	@RequestMapping(value = IMPORT_PROGRAM_URL_MAPPING, method = RequestMethod.POST)
	public String importProgramsPost(ModelMap model,
									@RequestParam(name = "file", required = true) MultipartFile file) throws IOException {
		if (file != null && !file.isEmpty()) {
			List<ChairDto> chairDtoList = null;
			List<VenueDto> venueDtoList = null;
			Chair chair = null;
			Venue venue = null;
			Set<ProgramChair> chairs = null;
			Set<ProgramVenue> venues = null;

			List<ProgramDto> programDtoList = poiService.extractProgram(file);

			for (ProgramDto programDto : programDtoList) {
				Program program = ProgramUtils.fromProgramDtoToDomainProgram(programDto);

				chairDtoList = programDto.getChairs();
				venueDtoList = programDto.getVenues();

				if (chairDtoList != null && !chairDtoList.isEmpty()) {
					chairs = new HashSet<ProgramChair>();
					for (ChairDto chairDto : chairDtoList) {
						chair = ProgramUtils.fromProgramDtoToDomainChair(chairDto);
						chairs.add(new ProgramChair(program, chair));
					}
				}

				if (venueDtoList != null && !venueDtoList.isEmpty()) {
					venues = new HashSet<ProgramVenue>();
					for (VenueDto venueDto : venueDtoList) {
						venue = ProgramUtils.fromProgramDtoToDomainVenue(venueDto);
						venues.add(new ProgramVenue(program, venue));
					}
				}

				programService.createProgram(program, chairs, venues);
			}
		}

		LOG.info("Program has been imported successfully");

		model.addAttribute(IMPORT_PROGRAM_MESSAGE_KEY, "true");

		return IMPORT_PROGRAM_VIEW_NAME;
	}

}
