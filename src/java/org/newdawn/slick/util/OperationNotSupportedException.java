package org.newdawn.slick.util;

/**
 * Thrown to indicate that a limited implementation of a class can not
 * support the operation requested. 
 *
 * @author kevin
 */
public class OperationNotSupportedException extends RuntimeException {

	private static final long serialVersionUID = 4530559934748205261L;

	/**
	 * Create a new exception
	 * 
	 * @param msg The message describing the limitation
	 */
	public OperationNotSupportedException(String msg) {
		super(msg);
	}
}
