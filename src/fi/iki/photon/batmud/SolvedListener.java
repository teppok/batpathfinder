package fi.iki.photon.batmud;

/**
 * Interface which will be called when a route is solved.
 * Implementing this interface and calling AreaContainer.solve with it will indicate
 * that you will be able to handle the event invoked by a solving thread when the
 * route has been solved. 
 * 
 * @author Teppo Kankaanp‰‰
 *
 */
public interface SolvedListener {
	
	/**
	 * The method is called with a result string, that is a space separated string of
	 * movement commands. If there is an integer value in the string,
	 * it's not a command but means that the next command must be repeated for that many
	 * times. An example of such string is "3 e ne 2 u". Type is the type parameter used
	 * when calling solve, so that different solver threads can be identified.
	 *  
	 * @param solvedPath
	 * @param type
	 */
	public void solved(String solvedPath, int type);
}
