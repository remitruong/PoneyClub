package fr.esieaproject.poneyclub.exception.userexceptions;

public class NoUserFoundException extends Exception {
	
	 public NoUserFoundException(String errorMessage) {
	        super(errorMessage);
	    }
	 
	 public NoUserFoundException(String errorMessage, Throwable err) {
	        super(errorMessage, err);
	    }
}
