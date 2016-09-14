package imageClustering;

import java.awt.image.BufferedImage;
import algorithm.Element;

/**
 * Container object for clustered image. Contains a BufferedImage object for
 * image data, an Element array to determine membership to cluster for each
 * pixel and the number of clusters.
 * 
 * @author Patryk Hêciak
 * @see ImageClusterer
 */
public class ClusteredImage {
	public BufferedImage image;
	public Element[] clusterization;
	public int clusters;
}
