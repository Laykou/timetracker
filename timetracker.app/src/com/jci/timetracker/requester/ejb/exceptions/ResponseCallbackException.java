package com.jci.timetracker.requester.ejb.exceptions;

public class ResponseCallbackException extends Exception
{
	private static final long serialVersionUID = 234446571543864L;
	private Exception previousException;

	/**
	 * Add exception that created this exception
	 * 
	 * @param e
	 */
	public ResponseCallbackException(Exception e)
	{
		previousException = e;
	}

	public Exception getException()
	{
		return previousException;
	}
}
