package fr.esieaproject.poneyclub.exception;

public class ExpiredTokenException extends Exception {
	public ExpiredTokenException(String message) {
		super(message);
	}
}
