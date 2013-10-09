package edu.iris.service.events;

//Import the error handling classes
import edu.iris.dmc.criteria.CriteriaException;   // error handling
import edu.iris.dmc.service.NoDataFoundException; // error handling
import edu.iris.dmc.service.ServiceNotSupportedException;


//ServiceUtil is where we obtain all the "hooks" to the web services.
import edu.iris.dmc.service.ServiceUtil;


//Import the classes specific to the EVENT example
import edu.iris.dmc.service.EventService;  // the "hook" to ws-event
import edu.iris.dmc.criteria.EventCriteria;// used to dictate which data to fetch
import edu.iris.dmc.event.model.Event;     // fetch returns an Event List
import edu.iris.dmc.event.model.Magnitude; // the events may have Magnitudes

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//Import other Java classes
import java.util.List;
import java.util.TimeZone;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Class modified from edu.iris.dmc.ws.tutorial.EventTutorial 
 * 
 * 
 * @author azhar
 *
 */
public class EventClient {

	private static EventService eventService = null;
	
	public EventClient() {
	// initialize the services
		if (eventService == null) {
			initializeIrisEventService();
		}
	}

	private void initializeIrisEventService() {
		ServiceUtil serviceUtil = ServiceUtil.getInstance();
	    serviceUtil.setAppName("Connector for REST Service");
		eventService = serviceUtil.getEventService();
	}

	private EventCriteria specifyEventCriteria(EventCriteria criteria) throws ParseException {
	    
	    criteria.setMaximumLatitude(28.0);
	    criteria.setMinimumLatitude(26.0);
	    criteria.setMaximumDepth(23.0);
	
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	    Date startTime = sdf.parse("2013-09-24T11:28:00");
	    Date endTime = sdf.parse("2013-09-30T11:30:00");
	
	    criteria.setStartTime(startTime);
	    criteria.setEndTime(endTime);
	    criteria.includeAllMagnitudes(true);
	    
	    return criteria;
	}
	
	public List<Event> getPredefinedCriteriaEvents() throws NoDataFoundException, CriteriaException, IOException, ServiceNotSupportedException, ParseException {
		EventCriteria criteria = new EventCriteria();
		criteria = specifyEventCriteria(criteria);
		return eventService.fetch(criteria);
	}

	/**
	 * Method to enable unit testing
	 * 
	 * @throws ServiceNotSupportedException 
	 * @throws IOException 
	 * @throws CriteriaException 
	 * @throws NoDataFoundException 
	 * @throws ParseException 
	 * 
	 */
	public void testEventClient() throws NoDataFoundException, CriteriaException, IOException, ServiceNotSupportedException, ParseException {
	
		// display the events along with preferred origin 
		// and any associated magnitude values
		List<Event> events = getPredefinedCriteriaEvents();
		for (Event e : events) {
			   System.out.println("Event: " + e.getType() + " " + e.getFlinnEngdahlRegionName());
			   System.out.println("\t"+e.getPreferredOrigin());
			   for(Magnitude magnitude:e.getMagnitudes()){
			      System.out.printf("\tMag: %3.1f %s\n", magnitude.getValue(), magnitude.getType());
			   }
			   
			}
	}

	
	
}
