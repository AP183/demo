package edu.service;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import edu.model.Assignment;
import edu.model.Assignments;

public class JAXBService {
	      //marshal(produse,new File("C:/APPS/jaxb-prod.xml"));
	    public static void marshal(List<Assignment> assignments, File selectedFile)
				throws IOException, JAXBException {
			JAXBContext context;
			BufferedWriter writer = null;
			writer = new BufferedWriter(new FileWriter(selectedFile));
			context = JAXBContext.newInstance(Assignments.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(new Assignments(assignments), writer);
			writer.close();
		}
	}


