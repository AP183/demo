package edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.model.Assignment;
import edu.service.AssignmentService;

@RestController
public class AssignmentController {

	@Autowired
	private AssignmentService assignmentService;

	@RequestMapping(value = "/addAssignment", method = RequestMethod.POST)
	public String addAssignment(@RequestBody Assignment assignment) throws JsonProcessingException {
		return assignmentService.addAssignment(assignment);
	}

	@RequestMapping(value = "/getAssignments", method = RequestMethod.GET)
	public @ResponseBody String getAssignments() throws Exception {
		return assignmentService.getAssignments();
	}

}
