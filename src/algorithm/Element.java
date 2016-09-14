package algorithm;

/**
 * Stores the Elements attributes required for clustering and the cluster
 * number to which the Element has been assigned
 * 
 * @author Patryk Hêciak
 * @see FCM
 */
public class Element {

	/**
	 * The cluster number to which the element has been assigned.
	 */
	public int cluster;
	/**
	 * An array (vector) of attributes of the element.
	 */
	public int[] atrributes;
}
