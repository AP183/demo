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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.model.ResetPasswordModel;
import edu.model.User;

//import net.metrosystems.new_mcat.commons.exceptions.NoResultFoundException;
//import net.metrosystems.new_mcat.commons.util.views.enums.ErrorCode;

@Service
public class UserService {

	@Autowired
	private CassandraService session;
	private ObjectMapper om = new ObjectMapper();
	// @Autowired
	// private JavaMailSender javaMailSender;

	private User extractUser(Row row) {
		// TODO resolve it
		// if (row == null)
		// throw new NoResultFoundException(ErrorCode.NO_CIN_XML_FOUND);
		String firstname = row.getString("firstname");
		String lastname = row.getString("lastname");
		String email = row.getString("email");
		int phone = row.getInt("phone");
		String role = row.getString("role");
		String password = row.getString("password");
		String status = row.getString("status");
		return new User(email, lastname, firstname, password, phone, role, status);
	}

	protected <T> String toJson(T value) throws JsonProcessingException {

		return om.writeValueAsString(value);
	}

	protected <T> T fromJson(String content, Class<T> cls) throws IOException {
		return om.readValue(content, cls);
	}

	public String save(final User user) throws JsonProcessingException {

		Statement statement1 = QueryBuilder.select().all().from("edu", "users").allowFiltering()
				.where(QueryBuilder.eq("email", user.getEmail()));

		ResultSet results = session.executeSelect(statement1);

		if (results.one() != null)
			return toJson("User already registered");
		else {
			Statement statement = QueryBuilder.insertInto("edu", "users")

					.value("firstname", user.getFirstname()).value("lastname", user.getLastname())
					.value("email", user.getEmail()).value("phone", user.getPhone())
					.value("password", user.getPassword()).value("role", user.getRole())
					.value("status", user.getStatus());

			session.execute(statement);

			return toJson("The account was created");
		}

	}

	public String login(final User credentials) throws JsonProcessingException {
		Statement statement = QueryBuilder.select().all().from("edu", "users").allowFiltering()
				.where(QueryBuilder.eq("email", credentials.getEmail()));

		ResultSet results = session.executeSelect(statement);
		Row row = results.one();

		if (row != null) {

			if ((row.getObject("status").toString()).equals("blocked")) {
				return toJson("Your account is blocked");
			} else {

				String pass = row.getObject("password").toString();
				if (pass.equals(credentials.getPassword())) {
					return toJson(row.getObject("role").toString());
				} else {
					return toJson("Invalid password");
				}
			}
		} else {
			return toJson("Unregistered user");
		}

	}

	public String getUsers() throws JsonParseException, JsonMappingException, IOException {
		Statement statement = QueryBuilder.select().all().from("edu", "users").allowFiltering();

		ResultSet results = session.executeSelect(statement);

		List<User> users = new ArrayList<User>();

		for (Row row : results.all()) {
			User u = extractUser(row);
			users.add(u);
		}

		return toJson(users);
	}

	public String getRole(User user) throws JsonParseException, JsonMappingException, IOException {
		System.out.println("email=" + user.getEmail());
		Statement statement1 = QueryBuilder.select().all().from("edu", "users").allowFiltering()
				.where(QueryBuilder.eq("email", user.getEmail()));

		ResultSet results = session.executeSelect(statement1);
		Row row = results.one();
		return toJson(row.getObject("role").toString());
	}

	public String blockUser(User user) throws JsonParseException, JsonMappingException, IOException {
		Statement s = QueryBuilder.update("edu", "users").with(QueryBuilder.set("status", "blocked"))
				.where(QueryBuilder.eq("email", user.getEmail()));

		session.executeSelect(s);

		return toJson("The account was blocked");
	}

	public String unblockUser(User user) throws JsonParseException, JsonMappingException, IOException {

		Statement s = QueryBuilder.update("edu", "users").with(QueryBuilder.set("status", "active"))
				.where(QueryBuilder.eq("email", user.getEmail()));

		session.executeSelect(s);

		return toJson("The account was activated");
	}

	public String checkUser(User user) throws JsonParseException, JsonMappingException, IOException {
		Statement statement = QueryBuilder.select().all().from("edu", "users").allowFiltering()
				.where(QueryBuilder.eq("email", user.getEmail()));

		ResultSet results = session.executeSelect(statement);
		Row row = results.one();

		if (row != null) {
			// System.out.println(user.getEmail());
			// MimeMessage mail = javaMailSender.createMimeMessage();
			// try {
			// MimeMessageHelper helper = new MimeMessageHelper(mail, true);
			// helper.setTo(user.getEmail());
			// helper.setFrom("training2016mcat@gmail.com");
			// helper.setSubject("Reset password");
			// helper.setText("Click on the link to reset your password");
			// } catch (MessagingException e) {
			// e.printStackTrace();
			// } finally {}
			// System.out.println("dupa setari");
			// javaMailSender.send(mail);

			return toJson("Exist");
		} else
			return toJson("Not");
	}

	public String resetPassword(ResetPasswordModel model) throws JsonParseException, JsonMappingException, IOException {
		Statement statement = QueryBuilder.select().all().from("edu", "users").allowFiltering()
				.where(QueryBuilder.eq("email", model.getEmail()));

		ResultSet results = session.executeSelect(statement);
		Row row = results.one();

		if (row.getObject("password").equals(model.getOldpassword())) {
			if (model.getNewpassword().toString().equals(model.getNewpasswordconfirmed())) {
				if (model.getOldpassword().toString().equals(model.getNewpassword().toString())) {
					return toJson("The new password must be different from the old password");
				} else {
					Statement s = QueryBuilder.update("edu", "users")
							.with(QueryBuilder.set("password", model.getNewpassword().toString()))
							.where(QueryBuilder.eq("email", model.getEmail()));

					session.executeSelect(s);

					return toJson("The password was changed");
				}
			} else {
				return toJson("New password must be identic with confirmed password!");
			}
		} else {
			return toJson("Old password is incorrect");
		}

	}
}