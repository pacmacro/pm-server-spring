package com.pm.server.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;
import com.pm.server.response.LocationResponse;
import com.pm.server.response.PacdotCountResponse;
import com.pm.server.response.PacdotResponse;
import com.pm.server.response.PacdotUneatenResponse;

@RestController
@RequestMapping("/pacdots")
public class PacdotController {

	@Autowired
	private PacdotRegistry pacdotRegistry;

	private final static Logger log =
			LogManager.getLogger(PacdotController.class.getName());

	@RequestMapping(
			value="/count",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public PacdotCountResponse getPacdotCount(HttpServletResponse response) {

		log.debug("Mapped GET /pacdots/count");

		PacdotCountResponse countResponse = new PacdotCountResponse();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			countResponse.incrementTotal();
			if(pacdot.getEaten()) {
				countResponse.incrementEaten();
			}
			else {
				countResponse.incrementUneaten();
				if(pacdot.getPowerdot()) {
					countResponse.incrementUneatenPowerdots();
				}
			}

		}
		return countResponse;
	}

	@RequestMapping(
			value="/uneaten",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PacdotUneatenResponse>
			getUneatenPacdots(HttpServletResponse response) {

		log.debug("Mapped GET /pacdots/uneaten");

		List<PacdotUneatenResponse> responseList = new ArrayList<PacdotUneatenResponse>();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {

			if(!pacdot.getEaten()) {
				PacdotUneatenResponse pacdotResponse = new PacdotUneatenResponse();

				LocationResponse locationResponse = new LocationResponse();
				locationResponse.setLatitude(pacdot.getLocation().getLatitude());
				locationResponse.setLongitude(pacdot.getLocation().getLongitude());
				pacdotResponse.setLocation(locationResponse);
				pacdotResponse.setPowerdot(pacdot.getPowerdot());

				responseList.add(pacdotResponse);
			}
		}

		return responseList;

	}

	@RequestMapping(
			value="",
			method=RequestMethod.GET,
			produces={ "application/json" }
	)
	@ResponseStatus(value = HttpStatus.OK)
	public List<PacdotResponse> getAllPacdots(HttpServletResponse response) {

		log.debug("Mapped GET /pacdots");

		List<PacdotResponse> responseList = new ArrayList<PacdotResponse>();
		List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
		for(Pacdot pacdot : pacdotList) {
			PacdotResponse pacdotResponse = new PacdotResponse();

			LocationResponse locationResponse = new LocationResponse();
			locationResponse.setLatitude(pacdot.getLocation().getLatitude());
			locationResponse.setLongitude(pacdot.getLocation().getLongitude());
			pacdotResponse.setLocation(locationResponse);

			pacdotResponse.setEaten(pacdot.getEaten());
			pacdotResponse.setPowerdot(pacdot.getPowerdot());

			responseList.add(pacdotResponse);
		}

		return responseList;

	}

}
