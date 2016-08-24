package org.opensrp.camp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.opensrp.camp.dao.Camp;
import org.opensrp.camp.dao.CampDate;
import org.opensrp.camp.dto.CampDateDTO;
import org.opensrp.camp.interfaces.CampInterface;
import org.opensrp.camp.repository.CampDateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class CampDateService {
	
	private static Logger logger = LoggerFactory.getLogger(CampDateService.class);
	
	private CampDateRepository campDateRepository;
	
	public CampDateService() {
		
	}
	
	@Autowired
	public void setCampDateRepository(CampDateRepository campDateRepository) {
		this.campDateRepository = campDateRepository;
	}
	
	public List<CampDate> getCampDateBySessionId(String session_id) {
		List<CampDate> campDates = campDateRepository.findBySessionId(session_id);
		Gson gson = new Gson();
		// convert your list to json
		String jsonCartList = gson.toJson(campDates);
		
		logger.info("Camp : " + jsonCartList.toString());
		return campDates;
		
	}	
	
	public CampDate CampDateDTO2CampDate(CampDateDTO campDateDTO,Camp camp) throws ParseException{
		CampDate campDate = new CampDate();
		campDate.setSession_date(campDateDTO.getSession_date());
		campDate.setSession_id(camp.getId());
		campDate.setContact(camp.getContact());
		campDate.setHealth_assistant(camp.getHealth_assistant());
		campDate.setStatus(campDateDTO.getStatus());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(campDateDTO.getSession_date().toString());
		campDate.setTimestamp(date.getTime());
		return campDate;		
	}

	
}
