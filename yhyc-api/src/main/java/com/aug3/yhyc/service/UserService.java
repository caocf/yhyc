package com.aug3.yhyc.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlRootElement;

@Path("/userservice/")
@XmlRootElement()
public class UserService {

	@Produces("application/json")
	@GET
	@Path("/login")
	// @AccessTrace
	// @AccessToken
	public boolean login() {
		return false;
	}

}
