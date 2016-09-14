package algorithm;

/**
 * Algorithm class. Requires 4 algorithm parameters in the constructor. It takes
 * an Element array or an array of attributes array as an input. The order is
 * important. After running algorithm use getResult() to acquire Element array.
 * Its elements are in the same order as the input. Each of its Elements has now
 * assigned the cluster number.
 * 
 * @author Patryk Hêciak
 * @see Element
 */
public class FCM {

	private int clusters;
	private int m;
	private double epsilon;
	private int maxIterations;

	private int[][] X; // input array X
	private int attributes; // number of attributes
	private float[][] U; // partition matrix
	private float[][] V; // centres of clusters

	/**
	 * Creates FCM algorithm object.
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
	 */
	public FCM(int clusters, int m, double epsilon, int maxIterations) {
		this.clusters = clusters;
		this.m = m;
		this.epsilon = epsilon;
		this.maxIterations = maxIterations;
	}

	public void setInput(int[][] in) {
		X = in;
		attributes = X[0].length;
	}

	public void setInput(Element[] in) {
		int attributes = in[0].atrributes.length;
		int[][] a = new int[in.length][attributes];
		for (int i = 0; i < in.length; i++)
			for (int j = 0; j < attributes; j++)
				a[i][j] = in[i].atrributes[j];
		X = a;
		this.attributes = attributes;
	}

	public void runAlgorythm() {
		float maxDifference = 0;
		U = new float[X.length][clusters];
		fulfilRandomlyU(U);

		int loops = 0;
		do {
			V = centersOfClusters(U);
			float[][] D = distancesMatrix(V);
			if (eachNotNull(D)) {
				float[][] newU = newPartitionMatrix(D);
				maxDifference = maxDifference(U, newU);
				U = newU;
			}
		} while (++loops < maxIterations && maxDifference > epsilon);
	}

	public Element[] getElements() {
		Element[] result = new Element[X.length];
		for (int i = 0; i < result.length; i++)
			result[i] = new Element();
		for (int k = 0; k < result.length; k++) {
			float max = 0;
			int cluster = 0;
			for (int i = 0; i < clusters; i++) {
				float current = U[k][i];
				if (current > max) {
					max = current;
					cluster = i;
				}
			}
			result[k].cluster = cluster;
			result[k].atrributes = X[k];
		}
		return result;
	}

	public float[][] getCentersOfClusters() {
		return V;
	}
	
	private float maxDifference(float[][] arr1, float[][] arr2) {
		float maxDifference = Float.MIN_VALUE;
		for (int column = 0; column < arr1.length; column++) {
			for (int row = 0; row < arr1[0].length; row++) {
				float difference = (float) Math.abs(arr1[column][row] - arr2[column][row]);
				if (difference > maxDifference)
					maxDifference = difference;
			}
		}
		return maxDifference;
	}

	private boolean eachNotNull(float[][] arr) {
		for (int column = 0; column < arr.length; column++)
			for (int row = 0; row < arr[0].length; row++)
				if (arr[column][row] == 0)
					return false;
		return true;
	}

	private float[][] newPartitionMatrix(float[][] D) {
		float[][] newU = new float[X.length][clusters];
		for (int row = 0; row < clusters; row++)
			for (int column = 0; column < X.length; column++)
				newU[column][row] = u_ij(row, column, D);
		return newU;

	}

	private float u_ij(int row, int column, float[][] D) {
		float denominatorSum = 0;
		for (int s = 0; s < clusters; s++)
			denominatorSum += Math.pow(D[column][row] / D[column][s], 2 / (m - 1));
		return 1 / denominatorSum;
	}

	private float[][] distancesMatrix(float[][] V) {
		float[][] result = new float[X.length][clusters];
		for (int column = 0; column < X.length; column++)
			for (int row = 0; row < clusters; row++)
				result[column][row] = distance(column, row, V);
		return result;
	}

	private float distance(int column, int row, float[][] V) {
		float sum = 0;
		for (int i = 0; i < attributes; i++)
			sum += Math.pow(X[column][i] - V[i][row], 2);
		return (float) Math.sqrt(sum);
	}

	private float[][] centersOfClusters(float[][] partitionMatrix) {
		float[][] res = new float[attributes][clusters];
		for (int row = 0; row < clusters; row++)
			for (int column = 0; column < attributes; column++)
				res[column][row] = v_ij(row, column, partitionMatrix);
		return res;
	}

	private float v_ij(int column, int row, float[][] partitionMatrix) {
		float sumNumerator = 0;
		float sumDenominator = 0;
		for (int k = 0; k < X.length; k++) {
			float denominator = (float) Math.pow(partitionMatrix[k][column], m);
			float licznik = denominator * X[k][row];
			sumNumerator += licznik;
			sumDenominator += denominator;
		}
		return sumNumerator / sumDenominator;
	}

	private void fulfilRandomlyU(float[][] U) {
		for (int i = 0; i < U.length; i++) {
			float valueLeft = 1;
			for (int j = 0; j < U[0].length; j++) {
				if (j != U[0].length - 1) {
					float current = (float) (Math.random() * valueLeft);
					U[i][j] = current;
					valueLeft -= current;
				} else
					U[i][j] = valueLeft;
			}
		}
	}
}
