package edu.service;

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

@Service
public class AssignmentService {

	@Autowired
	private CassandraService session;
	private ObjectMapper om = new ObjectMapper();

	private Assignment extractAssignment(Row row) throws Exception {
		// TODO resolve it
		if (row == null)
			throw new Exception("Null row for assignments");
		String title = row.getString("title");
		String subject = row.getString("subject");
		String description = row.getString("description");
		String id = row.getString("id");

		return new Assignment(id, subject, description, title);
	}

//	private AssignmentXML extractAssignmentXML(Row row) throws Exception {
//		// TODO resolve it
//		if (row == null)
//			throw new Exception("Null row for assignments");
//		String title = row.getString("title");
//		String subject = row.getString("subject");
//		String description = row.getString("description");
//		String id = row.getString("id");
//
//		return new AssignmentXML(id, subject, description, title);
//	}

	protected <T> String toJson(T value) throws JsonProcessingException {

		return om.writeValueAsString(value);
	}

	protected <T> T fromJson(String solution, Class<T> cls) throws IOException {
		return om.readValue(solution, cls);
	}

	public String getAssignments() throws Exception {
		Statement statement = QueryBuilder.select().all().from("edu", "assignments").allowFiltering();

		ResultSet results = session.executeSelect(statement);

		List<Assignment> assignments = new ArrayList<Assignment>();

		for (Row row : results.all()) {
			Assignment p = extractAssignment(row);
			assignments.add(p);
		}

		return toJson(assignments);
	}

	public String addAssignment(final Assignment assignment) throws JsonProcessingException {

		Statement statement1 = QueryBuilder.select().all().from("edu", "assignments").allowFiltering()
				.where(QueryBuilder.eq("id", assignment.getID()));

		ResultSet results = session.executeSelect(statement1);

		if (results.one() != null)
			return toJson("Assignment already exist");
		else {
			Statement statement = QueryBuilder.insertInto("edu", "assignments")

					.value("id", assignment.getID()).value("title", assignment.getTitle())
					.value("subject", assignment.getSubject()).value("description", assignment.getDescription());

			session.execute(statement);

			Statement statement2 = QueryBuilder.insertInto("edu", "assignments")

					.value("id", assignment.getID()).value("title", assignment.getTitle())
					.value("subject", assignment.getSubject()).value("description", assignment.getDescription());

			session.execute(statement2);

			return toJson("The assignment was added successfully");
		}

	}

}
