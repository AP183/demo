package edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.model.SolutionModel;
import edu.service.SolutionService;

@RestController
public class SolutionController {

	@Autowired
	private SolutionService solutionService;

	@RequestMapping(value = "/sendSolution", method = RequestMethod.POST)
	public String saveSolution(@RequestBody SolutionModel model) throws Exception {
		return solutionService.saveSolution(model);
	}

	@RequestMapping(value = "/getSolution", method = RequestMethod.GET)
	public String getSolution() throws Exception {
		return solutionService.getSolutions();
	}

}
