package imageClusteringDemo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import imageClustering.ClusteredImage;
import imageClustering.ImageClusterer;
import imageClustering.highlighting.ClusterHighlighter;
import imageClustering.highlighting.ClusterHighlighterGrayscaleLinear;
import imageClustering.highlighting.ClusterHighlighterGrayscaleRandom;
import imageClustering.highlighting.ClusterHighlighterRandomColors;

public class App extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7403559444859744487L;

	private static final int WINDOW_WIDTH = 1221;
	private static final int WINDOW_HEIGT = 592;
	private static final int CONTENT_HEIGHT = 563;

	private int paramClusters;
	private int paramM;
	private float paramMaxDelta;
	private int paramMaxIterations;

	private JButton bClustersPlus, bMParameterPlus, bDeltaPlus, bItersPlus;
	private JButton bClustersMinus, bMParameterMinus, bDeltaMinus, bItersMinus;
	private JTextField tClusters, tMParameter, tDelta, tIters;

	private JLabel lImage;
	private JButton bCluster;
	private JButton bRepaint;
	private ClusteredImage clusteredImage;
	private JCheckBox checkBox;
	private JButton bFile;
	private JLabel lFileName;
	private File imageFile;
	private BufferedImage clusteredBufferedImage;
	private JButton bSave;

	private JRadioButton radioGrayscale;
	private JRadioButton radioColor;

	private JRadioButton radioGrayscaleLinear;

	public static void main(String[] args) {
		App app = new App();
		app.setVisible(true);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setResizable(false);
	}

	public App() {
		setSize(WINDOW_WIDTH, WINDOW_HEIGT);
		setTitle("Fuzzy c-means image clusterer");
		setLayout(null);
		setup();
	}

	private void setup() {
		setupAlgorithmPanel(1000 + 10, 10);
		setupMainPanel(1000 + 20, 195); // 195
		initDefaultInputValues();
	}

	private void initDefaultInputValues() {
		paramClusters = 4;
		paramM = 2;
		paramMaxDelta = 0.12f;
		paramMaxIterations = 30;
		tClusters.setText("" + paramClusters);
		tMParameter.setText("" + paramM);

		tDelta.setText(String.format("%1.2f", paramMaxDelta));
		tIters.setText("" + paramMaxIterations);
	}

	private void setupMainPanel(int i, int j) {
		bCluster = new JButton("Cluster");
		bCluster.setBounds(i, j, 80, 25);
		bCluster.setFocusPainted(false);
		bCluster.addActionListener(this);
		bCluster.setEnabled(false);
		add(bCluster);

		bRepaint = new JButton("Repaint");
		bRepaint.setBounds(i + 95, j, 80, 25);
		bRepaint.setFocusPainted(false);
		bRepaint.addActionListener(this);
		bRepaint.setEnabled(false);
		add(bRepaint);

		radioGrayscale = new JRadioButton("grayscale random");
		radioGrayscale.setBounds(i, j + 35, 170, 25);
		radioGrayscale.setFocusPainted(false);
		radioGrayscale.setEnabled(false);
		add(radioGrayscale);

		radioGrayscaleLinear = new JRadioButton("grayscale linear");
		radioGrayscaleLinear.setBounds(i, j + 60, 170, 25);
		radioGrayscaleLinear.setFocusPainted(false);
		radioGrayscaleLinear.setEnabled(false);
		add(radioGrayscaleLinear);

		radioColor = new JRadioButton("random colors");
		radioColor.setBounds(i, j + 85, 170, 25);
		radioColor.setFocusPainted(false);
		radioColor.setEnabled(false);
		add(radioColor);

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radioColor);
		radioGroup.add(radioGrayscale);
		radioGroup.add(radioGrayscaleLinear);
		radioGroup.setSelected(radioGrayscale.getModel(), true);

		checkBox = new JCheckBox("Hide clusterization");
		checkBox.setBounds(i, j + 115, 130, 25);
		checkBox.setFocusPainted(false);
		checkBox.addActionListener(this);
		checkBox.setEnabled(false);
		add(checkBox);

		bSave = new JButton("Save");
		bSave.setBounds(i + 95, j + 150, 80, 25);
		bSave.setFocusPainted(false);
		bSave.addActionListener(this);
		bSave.setEnabled(false);
		add(bSave);
	}

	private void setupAlgorithmPanel(int i, int j) {
		JPanel panelAlgorithm = new JPanel();
		panelAlgorithm.setLayout(new BoxLayout(panelAlgorithm, BoxLayout.PAGE_AXIS));
		panelAlgorithm.setBounds(i, j, 195, 178);
		Border lineBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY),
				"Algorithm input");
		panelAlgorithm.setBorder(lineBorder);
		// ===========================
		setupFileSelection(i + 10, j + 20);
		setupClusterControls(i + 10, j + 50);
		setupMParameterControls(i + 10, j + 80);
		setupDeltaControls(i + 10, j + 110);
		setupItersControls(i + 10, j + 140);
		// ===========================
		add(panelAlgorithm);
	}

	private void setupFileSelection(int i, int j) {
		bFile = new JButton("Open");
		bFile.setMargin(new Insets(0, 0, 0, 0));
		bFile.setBounds(i, j, 40, 25);
		bFile.setFocusPainted(false);
		bFile.addActionListener(this);
		add(bFile);

		lFileName = new JLabel("nothing selected");
		lFileName.setBounds(i + 45, j, 135, 25);
		lFileName.setHorizontalAlignment(JTextField.CENTER);
		add(lFileName);
	}

	private void setupClusterControls(int i, int j) {
		JLabel lClusters = new JLabel("Clusters");
		lClusters.setBounds(i, j, 90, 25);
		add(lClusters);

		bClustersMinus = new JButton("-");
		bClustersMinus.setBounds(i + 90, j, 25, 25);
		bClustersMinus.setMargin(new Insets(0, 0, 0, 0));
		bClustersMinus.setFocusPainted(false);
		bClustersMinus.addActionListener(this);
		add(bClustersMinus);

		tClusters = new JTextField("-1");
		tClusters.setBounds(i + 117, j, 30, 25);
		tClusters.setHorizontalAlignment(JTextField.CENTER);
		tClusters.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tClusters.setFocusable(false);
		add(tClusters);

		bClustersPlus = new JButton("+");
		bClustersPlus.setBounds(i + 149, j, 25, 25);
		bClustersPlus.setMargin(new Insets(0, 0, 0, 0));
		bClustersPlus.setFocusPainted(false);
		bClustersPlus.addActionListener(this);
		add(bClustersPlus);
	}

	private void setupMParameterControls(int i, int j) {
		JLabel lClusters = new JLabel("M - parameter");
		lClusters.setBounds(i, j, 90, 25);
		add(lClusters);

		bMParameterMinus = new JButton("-");
		bMParameterMinus.setBounds(i + 90, j, 25, 25);
		bMParameterMinus.setMargin(new Insets(0, 0, 0, 0));
		bMParameterMinus.setFocusPainted(false);
		bMParameterMinus.addActionListener(this);
		add(bMParameterMinus);

		tMParameter = new JTextField("-1");
		tMParameter.setBounds(i + 117, j, 30, 25);
		tMParameter.setHorizontalAlignment(JTextField.CENTER);
		tMParameter.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tMParameter.setFocusable(false);
		add(tMParameter);

		bMParameterPlus = new JButton("+");
		bMParameterPlus.setBounds(i + 149, j, 25, 25);
		bMParameterPlus.setMargin(new Insets(0, 0, 0, 0));
		bMParameterPlus.setFocusPainted(false);
		bMParameterPlus.addActionListener(this);
		add(bMParameterPlus);
	}

	private void setupDeltaControls(int i, int j) {
		JLabel lClusters = new JLabel("Max delta");
		lClusters.setBounds(i, j, 90, 25);
		add(lClusters);

		bDeltaMinus = new JButton("-");
		bDeltaMinus.setBounds(i + 90, j, 25, 25);
		bDeltaMinus.setMargin(new Insets(0, 0, 0, 0));
		bDeltaMinus.setFocusPainted(false);
		bDeltaMinus.addActionListener(this);
		add(bDeltaMinus);

		tDelta = new JTextField("-1");
		tDelta.setBounds(i + 117, j, 30, 25);
		tDelta.setHorizontalAlignment(JTextField.CENTER);
		tDelta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tDelta.setFocusable(false);
		add(tDelta);

		bDeltaPlus = new JButton("+");
		bDeltaPlus.setBounds(i + 149, j, 25, 25);
		bDeltaPlus.setMargin(new Insets(0, 0, 0, 0));
		bDeltaPlus.setFocusPainted(false);
		bDeltaPlus.addActionListener(this);
		add(bDeltaPlus);
	}

	private void setupItersControls(int i, int j) {
		JLabel lClusters = new JLabel("Max iterations");
		lClusters.setBounds(i, j, 90, 25);
		add(lClusters);

		bItersMinus = new JButton("-");
		bItersMinus.setBounds(i + 90, j, 25, 25);
		bItersMinus.setMargin(new Insets(0, 0, 0, 0));
		bItersMinus.setFocusPainted(false);
		bItersMinus.addActionListener(this);
		add(bItersMinus);

		tIters = new JTextField("-1");
		tIters.setBounds(i + 117, j, 30, 25);
		tIters.setHorizontalAlignment(JTextField.CENTER);
		tIters.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		tIters.setFocusable(false);
		add(tIters);

		bItersPlus = new JButton("+");
		bItersPlus.setBounds(i + 149, j, 25, 25);
		bItersPlus.setMargin(new Insets(0, 0, 0, 0));
		bItersPlus.setFocusPainted(false);
		bItersPlus.addActionListener(this);
		add(bItersPlus);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s.equals(bFile)) {
			pickFile();
		}

		if (s.equals(bCluster)) {
			cluster();
			checkBox.setSelected(false);
		}
		if (s.equals(bRepaint)) {
			redraw();
			checkBox.setSelected(false);
		}

		if (s.equals(bClustersMinus)) {
			if (paramClusters > 1)
				paramClusters--;
			tClusters.setText("" + paramClusters);
		}
		if (s.equals(bClustersPlus)) {
			paramClusters++;
			tClusters.setText("" + paramClusters);
		}

		if (s.equals(bMParameterMinus)) {
			if (paramM > 1)
				paramM--;
			tMParameter.setText("" + paramM);
		}
		if (s.equals(bMParameterPlus)) {
			paramM++;
			tMParameter.setText("" + paramM);
		}

		if (s.equals(bDeltaMinus)) {
			if (paramMaxDelta > 0.02f)
				paramMaxDelta -= 0.01f;
			tDelta.setText(String.format("%1.2f", paramMaxDelta));
		}
		if (s.equals(bDeltaPlus)) {
			paramMaxDelta += 0.01f;
			tDelta.setText(String.format("%1.2f", paramMaxDelta));
		}

		if (s.equals(bItersMinus)) {
			if (paramMaxIterations > 1)
				paramMaxIterations--;
			tIters.setText("" + paramMaxIterations);
		}
		if (s.equals(bItersPlus)) {
			paramMaxIterations++;
			tIters.setText("" + paramMaxIterations);
		}

		if (s.equals(checkBox)) {
			boolean selected = checkBox.isSelected();
			if (selected)
				showOriginalImage();
			else
				showClusteredImage();
		}

		if (s.equals(bSave)) {
			saveClusteredImage();
		}

	}

	private void pickFile() {
		File workingDirectory = new File(System.getProperty("user.dir"));
		JFileChooser fc = new JFileChooser(workingDirectory);
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			setNewFile(file);
			showOriginalImage();

			bCluster.setEnabled(true);
		}
	}

	private void saveClusteredImage() {
		File workingDirectory = new File(System.getProperty("user.dir"));
		JFileChooser fc = new JFileChooser(workingDirectory);
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		fc.setFileFilter(new FileNameExtensionFilter(".jpg file", "jpg"));
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().toString();
			if (!filename.endsWith(".jpg"))
				filename += ".jpg";
			File file = new File(filename);
			try {
				ImageIO.write(clusteredBufferedImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void showClusteredImage() {
		remove(lImage);
		lImage = drawImage(clusteredBufferedImage, 0, 0, 1000, CONTENT_HEIGHT);

		add(lImage);
		lImage.repaint();
	}

	private void showOriginalImage() {
		if (lImage != null)
			remove(lImage);
		BufferedImage img = null;
		try {
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lImage = drawImage(img, 0, 0, 1000, CONTENT_HEIGHT);
		add(lImage);
		lImage.repaint();
	}

	void cluster() {
		if (lImage != null)
			remove(lImage);

		ImageClusterer ic = new ImageClusterer();
		try {
			ic.setImage(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		clusteredImage = ic.clusterImage(paramClusters, paramM, paramMaxDelta, paramMaxIterations);

		ClusterHighlighter highlighter;
		if (radioColor.isSelected()) {
			highlighter = new ClusterHighlighterRandomColors(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		} else if (radioGrayscale.isSelected()) {
			highlighter = new ClusterHighlighterGrayscaleRandom(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		} else {
			highlighter = new ClusterHighlighterGrayscaleLinear(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		}

		lImage = drawImage(clusteredBufferedImage, 0, 0, 1000, CONTENT_HEIGHT);
		add(lImage);
		lImage.repaint();

		enableRemainingUIElements();
	}

	private void enableRemainingUIElements() {
		bRepaint.setEnabled(true);
		checkBox.setEnabled(true);
		bSave.setEnabled(true);
		radioColor.setEnabled(true);
		radioGrayscale.setEnabled(true);
		radioGrayscaleLinear.setEnabled(true);
	}

	private void setNewFile(File file) {
		if (file != null) {
			lFileName.setText(file.getName());
			imageFile = file;
		}
	}

	private void redraw() {
		ClusterHighlighter highlighter;
		if (radioColor.isSelected()) {
			highlighter = new ClusterHighlighterRandomColors(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		} else if (radioGrayscale.isSelected()) {
			highlighter = new ClusterHighlighterGrayscaleRandom(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		} else {
			highlighter = new ClusterHighlighterGrayscaleLinear(clusteredImage);
			clusteredBufferedImage = highlighter.highlight();
		}

		remove(lImage);
		lImage = drawImage(clusteredBufferedImage, 0, 0, 1000, CONTENT_HEIGHT);

		add(lImage);
		lImage.repaint();
	}

	private JLabel drawImage(BufferedImage img, int x, int y, int w, int h) {
		Dimension d = scaledDimension(w, h, img.getWidth(), img.getHeight());
		Image image = img.getScaledInstance(d.width, d.height, Image.SCALE_SMOOTH);
		ImageIcon imageIcon = new ImageIcon(image);

		JLabel picLabel = new JLabel(imageIcon);
		picLabel.setBounds(x, y, w, h);
		return picLabel;
	}

	private Dimension scaledDimension(int frameWidth, int frameHeight, int imgWidth, int imgHeight) {
		int newWidth = 0, newHeight = 0;
		float fwh = (float) frameWidth / frameHeight;
		float iwh = (float) imgWidth / imgHeight;
		if (fwh > iwh) {
			if (imgHeight > frameHeight) {
				newHeight = frameHeight;
			} else {
				newHeight = imgHeight;
			}
			newWidth = (int) (iwh * newHeight);
		} else {
			if (imgWidth > frameWidth) {
				newWidth = frameWidth;
			} else {
				newWidth = imgWidth;
			}
			newHeight = (int) (newWidth / iwh);
		}
		return new Dimension(newWidth, newHeight);
	}

}
