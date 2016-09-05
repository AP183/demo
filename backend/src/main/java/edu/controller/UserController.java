package edu.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.model.ResetPasswordModel;
import edu.model.User;
import edu.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String createUser(@RequestBody User user) throws JsonProcessingException {
		return userService.save(user);
	}

	@RequestMapping(value = "/searchUser", method = RequestMethod.POST)
	public @ResponseBody String login(@RequestBody User credentials) throws JsonProcessingException {
		return userService.login(credentials);
	}

	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public @ResponseBody String getUsers() throws IOException {
		return userService.getUsers();
	}

	@RequestMapping(value = "/blockUser", method = RequestMethod.POST)
	public @ResponseBody String blockUser(@RequestBody User user) throws IOException {
		return userService.blockUser(user);
	}

	@RequestMapping(value = "/unblockUser", method = RequestMethod.POST)
	public @ResponseBody String unblockUser(@RequestBody User user) throws IOException {
		return userService.unblockUser(user);
	}

	@RequestMapping(value = "/reset-password-check", method = RequestMethod.POST)
	public @ResponseBody String checkUser(@RequestBody User user) throws IOException {
		return userService.checkUser(user);
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	public @ResponseBody String resetPassword(@RequestBody ResetPasswordModel model) throws IOException {
		return userService.resetPassword(model);
	}
}