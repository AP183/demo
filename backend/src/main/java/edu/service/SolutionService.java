package edu.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.model.Assignment;
import edu.model.Solution;
import edu.model.SolutionModel;

@Service
public class SolutionService {

	@Autowired
	private CassandraService session;
	//private JAXBService jaxbServ;
	private ObjectMapper om = new ObjectMapper();
	//private UserService asgn = new UserService();

	private Solution extractSolution(Row row) throws Exception {
		// TODO resolve it
		if (row == null)
			throw new Exception("Null row for assignments");

		String description = row.getString("description");
		String title = row.getString("title");
		String id = row.getString("id");
		String subject = row.getString("subject");
		String email = "123";
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// if (auth.isAuthenticated())

		return new Solution(description, title, id, subject, email);
	}

	protected <T> String toJson(T value) throws JsonProcessingException {

		return om.writeValueAsString(value);
	}

	protected <T> T fromJson(String solution, Class<T> cls) throws IOException {
		return om.readValue(solution, cls);
	}

	public String saveSolution(SolutionModel model) throws Exception {

		String email = model.getEmail();
		List<Assignment> assignments = model.getList();

		Statement statement1 = QueryBuilder.select().all().from("edu", " users").allowFiltering()
				.where(QueryBuilder.eq("email", email));

		ResultSet results = session.executeSelect(statement1);

		Row user = results.one();
		/// System.out.println(user.getU.getObject("id").toString());

		String filename = "C:/APPS/workspace/git/training-angular1/xmls/" + user.getObject("firstname").toString()
				+ user.getObject("lastname").toString() + ".xml";

		// generate XML
		JAXBService.marshal(assignments, new File(filename));

		// save sol in db as stringg
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		String sb = stringBuilder.toString();
		System.out.println(sb);

		Statement statement2 = QueryBuilder.select().all().from("edu", "assignments").allowFiltering();

		ResultSet results2 = session.executeSelect(statement2);

		Row asgn = results2.one();

		// String code = user.getObject("title").toString() +
		// user.getObject("description").toString() + date;

		Statement statement = QueryBuilder.insertInto("edu", "assignment_by_student")

				.value("id", asgn.getObject("id").toString()).value("title", asgn.getObject("title").toString())
				.value("subject", asgn.getObject("subject").toString())
				.value("description", asgn.getObject("description").toString())
				.value("email", user.getObject("email").toString());

		session.execute(statement);

		return toJson("The  solution was sent.");
	}

	public String getSolutions() throws Exception {
		Statement statement = QueryBuilder.select().all().from("edu", "assignment_by_student").allowFiltering();

		ResultSet results = session.executeSelect(statement);

		List<Solution> solutions = new ArrayList<Solution>();

		for (Row row : results.all()) {
			Solution p = extractSolution(row);
			solutions.add(p);
		}

		return toJson(solutions);
	}

}
