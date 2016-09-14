package imageClustering.highlighting;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import imageClustering.ClusteredImage;

/**
 * Linear grayscale implementation of the ClusterHighlighter. Aside from the
 * number of clusters all the shades of gray will always have even spacing.
 * 
 * @author Patryk Hêciak
 * @see ClusterHighlighterGrayscaleRandom
 * @see ClusterHighlighterRandomColors
 */
public class ClusterHighlighterGrayscaleLinear extends ClusterHighlighter {

	public ClusterHighlighterGrayscaleLinear(ClusteredImage clusteredImage) {
		super(clusteredImage);
	}

	@Override
	public BufferedImage highlight() {
		int clusters = clusteredImage.clusters;
		Color[] car = new Color[clusters];
		int[] randomSeries = integerSeriesInRandomOrder(clusters);
		for (int i = 0; i < clusters; i++) {
			int los = randomSeries[i];
			int color = 255 * los / (clusters - 1);
			car[i] = new Color(color, color, color);
		}
		int elementNr = 0;
		BufferedImage image = copyImage(clusteredImage.image);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int cluster = clusteredImage.clusterization[elementNr].cluster;
				image.setRGB(x, y, car[cluster].getRGB());
				elementNr++;
			}
		}
		return image;
	}

	private int[] integerSeriesInRandomOrder(int max) {
		int[] res = new int[max];
		for (int i = 0; i < max; i++)
			res[i] = -1;
		int sum = 0;
		for (int i = 0; i <= max; i++)
			sum = sum + i;
		Random r = new Random();
		for (int i = 0; i < max; i++) {
			int random;
			do {
				random = r.nextInt(max);
			} while (arrayContainsNumber(res, random));
			res[i] = random;
			sum = sum - random;
		}
		return res;
	}

	private boolean arrayContainsNumber(int[] arr, int n) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == n)
				return true;
		return false;
	}
}
