package imageClustering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import algorithm.Element;
import algorithm.FCM;

/**
 * Class used for clustering images. Requires etiher BufferedImage or File as an
 * input. Calling clusterImage method with FCM algorithm parametres returns
 * ClusteredImage object. This is a container object for clustered image that
 * contains a BufferedImage object for image data and Element array to determine
 * membership to cluster for each pixel.
 * 
 * @author Patryk Hêciak
 * @see FCM
 */
public class ImageClusterer {

	private BufferedImage bufferedImage;

	/**
	 * Sets the input image
	 * 
	 * @param bufferedImage
	 */
	public void setImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	/**
	 * Sets the input image
	 * 
	 * @param file
	 *            File object pointing to the image
	 * @exception IOException
	 */
	public void setImage(File file) throws IOException {
		bufferedImage = ImageIO.read(file);
	}

	/**
	 * Clusters the bufferedImage.
	 * 
	 * @param clusters
	 *            the number of clusters
	 * @param m
	 *            fuzzy partition matrix exponent for controlling the degree of
	 *            fuzzy overlap, with m > 1. Fuzzy overlap refers to how fuzzy
	 *            the boundaries between clusters are, that is the number of
	 *            Elements that have significant membership in more than one
	 *            cluster.
	 * @param epsilon
	 *            must be greater than the maximum difference between the
	 *            corresponding elements of the previous and the current
	 *            partition matrix in order to perform the next iteration.
	 * @param maxIterations
	 *            maximum iterations number
	 * @see ClusteredImage
	 */
	public ClusteredImage clusterImage(int c, int m, double epsilon, int maxIteracji) {
		Element[] elementArray = convertBufferedImageToElementArray(bufferedImage);
		FCM fcm = new FCM(c, m, epsilon, maxIteracji);
		fcm.setInput(elementArray);
		fcm.runAlgorythm();
		ClusteredImage clusteredImage = new ClusteredImage();
		clusteredImage.clusterization = fcm.getElements();
		clusteredImage.image = bufferedImage;
		clusteredImage.clusters = c;
		return clusteredImage;
	}

	private Element[] convertBufferedImageToElementArray(BufferedImage img) {
		Element[] res = new Element[img.getWidth() * img.getHeight()];
		int i = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				Color color = new Color(img.getRGB(x, y));
				Element element = new Element();
				element.atrributes = new int[3];
				element.atrributes[0] = color.getRed();
				element.atrributes[1] = color.getGreen();
				element.atrributes[2] = color.getBlue();
				res[i++] = element;
			}
		}
		return res;
	}
}
