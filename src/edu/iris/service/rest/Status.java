package edu.iris.service.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import edu.iris.dmc.event.model.Event;
import edu.iris.dmc.event.model.Magnitude;
import edu.iris.service.dao.*;
import edu.iris.service.events.*;

import java.sql.*;
import java.util.List;

@Path("/status")
public class Status extends Application {

	private static String version = "1.0.0.1";
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnTitle() {
		return("<p>Java Web Servcie<p>");
	}
	
	@Path("/version")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnVersion() {
		return("<p>Version: "+version+"</p>");
	}
	
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus() throws Exception {
		Connection conn = null;
		String result = null;
		try {
			conn = DatasourceSingleton.getConnection();
			String queryString = "select now();";
			PreparedStatement ps = conn.prepareStatement(queryString);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}
			result = "<p>Database System Data Time: "+result+"</p>";
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error executing query: "+e.getMessage());
		} finally {
			if (conn != null) {
				conn.close();
			}
			
		}
	}
	
	@Path("/earthquake_pakistan")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnPakistanEarthquakeEvent() throws Exception {
		
		
		String result = null;
		
		try {
			EventClient eventPakistan = new EventClient();
			List<Event> events = eventPakistan.getPredefinedCriteriaEvents();
			
			for (Event e : events) {
				   result = result + "<p>Event: " + e.getType() + " " + e.getFlinnEngdahlRegionName()+"</p>";
				   result = result + "&nbsp <p>"+e.getPreferredOrigin()+"</p>";
				   for(Magnitude magnitude:e.getMagnitudes()){
					   String magString = String.format("\tMag: %3.1f %s\n", magnitude.getValue(), magnitude.getType());
				      result = result + "&nbsp <p>"+magString+"</p>";
				   }
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		   throw new Exception("Error occured while getting EarthQuake events: "+e.getMessage());
		}
	}
	
}
