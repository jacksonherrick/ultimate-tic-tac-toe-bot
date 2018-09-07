package main;

/**
 * 
 * Stores information that is passed into NegaMax.
 * Some fields are modified via getters and setters while NegaMax is run.
 */
public class Controller {
	
	// Reporting level. 0 = none, 1 = evaluations only, 2 = all. 
	int reportingLevel;
	
	public Controller() {
		reportingLevel = 1;
	}

}
