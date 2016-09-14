package imageClustering.highlighting;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import imageClustering.ClusteredImage;

/**
 * Random colors implementation of the ClusterHighlighter. Color for each
 * cluster is random.
 * 
 * @author Patryk Hêciak
 * @see ClusterHighlighterGrayscaleLinear
 * @see ClusterHighlighterGrayscaleRandom
 */
public class ClusterHighlighterRandomColors extends ClusterHighlighter {

	public ClusterHighlighterRandomColors(ClusteredImage clusteredImage) {
		super(clusteredImage);
	}

	@Override
	public BufferedImage highlight() {
		int clusters = clusteredImage.clusters;
		Color[] car = new Color[clusters];
		for (int i = 0; i < clusters; i++)
			car[i] = randomColor();
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

	private Color randomColor() {
		Random rand = new Random();
		int r = rand.nextInt(255);
		int g = rand.nextInt(255);
		int b = rand.nextInt(255);
		return new Color(r, g, b);
	}
}
