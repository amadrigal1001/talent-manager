package com.ibm.personafusion.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import com.ibm.personafusion.Engine;
import com.ibm.personafusion.infogen.PersonListGenerator;
import com.ibm.personafusion.model.Person;

/** The controller for the Search functionality.
 *  Handles GET requests to /search.
 *  Requires 'fname' and 'lname' query parameters to search.
 *  @author Sean Welleck 
 **/
@Path("/search")
public class SearchController 
{
	public static List<Person> people = PersonListGenerator.generateDistinctPeople(100);
	
	/** Returns search results as a JSON string. **/
	@GET
	public String handleSearch(@Context UriInfo ui)
	{
		System.out.println("Num people: " + people.size());
		MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
		String fname = getParam("fname", queryParams);
		String lname = getParam("lname", queryParams);
		
		if (fname == null || lname == null)
		{
			return "Error: fname and lname query parameters cannot be null.";
		}
		
		String fullName = fname + " " + lname;
		System.out.println("fname=" + fname + " lname=" + lname);
		
		Engine engine = new Engine(people);
		System.out.println("Engine created.");
		
		List<Person> results = engine.query(fullName);
		System.out.println("Num results: " + results.size());
		
		String json = JsonUtils.getListPersonJson(results);
		System.out.println(json);
		return json;
	}
	
	/** Returns null if a parameter does not exist. **/
	private String getParam(String key, MultivaluedMap<String, String> qp)
	{
		List<String> vals = qp.get(key);
		if (vals == null || vals.size() == 0) return null;
		return vals.get(0);
	}

}
