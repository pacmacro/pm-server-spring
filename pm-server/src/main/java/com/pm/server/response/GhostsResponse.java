package com.pm.server.response;

import java.util.ArrayList;
import java.util.List;

public class GhostsResponse {

	private List<GhostResponse> ghostResponsesList;

	public GhostsResponse() {
		ghostResponsesList = new ArrayList<GhostResponse>();
	}

	public void addGhostResponse(GhostResponse ghostResponse) {
		ghostResponsesList.add(ghostResponse);
	}

	public List<GhostResponse> getGhostResponsesList() {
		return ghostResponsesList;
	}

}
