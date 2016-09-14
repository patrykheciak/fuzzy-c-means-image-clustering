package imageClustering.highlighting;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import imageClustering.ClusteredImage;
import imageClustering.ImageClusterer;

/**
 * Abstract class for highlighting the clusters on the image.
 * 
 * @author Patryk Hêciak
 * @see ImageClusterer
 * @see ClusterHighlighterGrayscaleLinear
 * @see ClusterHighlighterGrayscaleRandom
 * @see ClusterHighlighterRandomColors
 */
public abstract class ClusterHighlighter {

	protected ClusteredImage clusteredImage;

	public ClusterHighlighter(ClusteredImage clusteredImage) {
		this.clusteredImage = clusteredImage;
	}

	public abstract BufferedImage highlight();

	protected BufferedImage copyImage(BufferedImage bufferedImage) {
		ColorModel cm = bufferedImage.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bufferedImage.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}
