package fi.iki.photon.batmud;

/**
 * Exception thrown on problems in program logic, bugs etc.
 * 
 * @author Teppo Kankaanp‰‰
 *
 */

public class BPFException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private String message;
	/**
	 * A standard constructor.
	 * @param m
	 */
	
	BPFException(String m) { message = m; }
	
	@Override
	public String toString() { return message; }
	
}
