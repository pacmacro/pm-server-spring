package com.pm.server.player;

import com.pm.server.datatype.Coordinate;

public interface Ghost {

	void setId(int id);

	int getId();

	void setLocation(Coordinate location);

	Coordinate getLocation();

}
