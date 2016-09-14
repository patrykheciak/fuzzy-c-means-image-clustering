package imageClustering.highlighting;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import imageClustering.ClusteredImage;

/**
 * Random grayscale implementation of the ClusterHighlighter. Color for each
 * cluster is random grayscale.
 * 
 * @author Patryk Hêciak
 * @see ClusterHighlighterGrayscaleLinear
 * @see ClusterHighlighterRandomColors
 */
public class ClusterHighlighterGrayscaleRandom extends ClusterHighlighter {

	public ClusterHighlighterGrayscaleRandom(ClusteredImage clusteredImage) {
		super(clusteredImage);
	}

	@Override
	public BufferedImage highlight() {
		int clusters = clusteredImage.clusters;
		Color[] car = new Color[clusters];

		for (int i = 0; i < clusters; i++)
			car[i] = randomGrayscaleColor();
		int i = 0;
		BufferedImage image = copyImage(clusteredImage.image);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int cluster = clusteredImage.clusterization[i].cluster;
				image.setRGB(x, y, car[cluster].getRGB());
				i++;
			}
		}
		return image;
	}

	private Color randomGrayscaleColor() {
		Random rand = new Random();
		int r = rand.nextInt(255);
		return new Color(r, r, r);
	}
}
