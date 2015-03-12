import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LCS {

	private static String X;
	private static String Y;
	private static int m;
	private static int n;
	private static int[][] c;
	private static char[][] b;

	private static int[] resulti;
	private static int[] resultj;
	private static char[] lcs;
	private static int count;

	private static JFrame frame;
	private static JPanel inputPanel;

	private static JLabel[] blabel;
	private static JLabel LCSlabel;
	private static JButton start;
	private static JButton again;

	private static final int FRAME_WIDTH = 250;
	private static final int FRAME_HEIGHT = 200;

	private static Dimension screenSize;

	public static void main(String[] args) {

		initUI();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});

	}

	private static void initUI() {
		frame = new JFrame();
		// frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setResizable(false);
		frame.setUndecorated(true);
		Toolkit kit = Toolkit.getDefaultToolkit();
		screenSize = kit.getScreenSize();
		frame.setLocation((screenSize.width - FRAME_WIDTH) / 2,
				(screenSize.height - FRAME_HEIGHT) / 2);

		inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(4, 1));

		final JTextField inputX = new JTextField("ABCBDAB");
		final JTextField inputY = new JTextField("BDCABA");

		inputPanel.add(inputX);
		inputPanel.add(inputY);

		JButton match = new JButton("Match");
		match.setForeground(Color.WHITE);
		match.setBackground(Color.BLUE);
		match.setFocusPainted(false);// 设置不绘制焦点
		// match.setBorderPainted(false);
		match.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				X = inputX.getText();
				Y = inputY.getText();
				if (X.isEmpty() || Y.isEmpty()) {
					JOptionPane.showMessageDialog(null, "请输入字符，不接受空输入！", "警告",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				match();
				showResult();
			}
		});
		inputPanel.add(match);

		JButton exit = new JButton("Exit");
		exit.setBackground(Color.BLACK);
		exit.setForeground(Color.WHITE);
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}

		});
		inputPanel.add(exit);

		inputPanel.setBackground(Color.WHITE);

		frame.add(inputPanel);
	}

	private static void match() {
		m = X.length();
		n = Y.length();
		b = new char[m + 1][n + 1];
		c = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			c[i][0] = 0;
			b[i][0] = '0';
		}
		for (int j = 0; j <= n; j++) {
			c[0][j] = 0;
			b[0][j] = '0';
		}
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (X.charAt(i - 1) == Y.charAt(j - 1)) {
					c[i][j] = c[i - 1][j - 1] + 1;
					b[i][j] = '↖';
				} else if (c[i - 1][j] >= c[i][j - 1]) {
					c[i][j] = c[i - 1][j];
					b[i][j] = '↑';
				} else {
					c[i][j] = c[i][j - 1];
					b[i][j] = '←';
				}
			}
		}
	}

	private static void showResult() {
		JFrame resultFrame = new JFrame();
		// resultFrame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		resultFrame.setSize(m * 50 * 3, n * 50);
		resultFrame.setLocation((screenSize.width - m * 50 * 3) / 2,
				(screenSize.height - n * 50) / 2);
		resultFrame.setLayout(new GridLayout(1, 3));

		JPanel cPanel = new JPanel();
		JPanel controlPanel = new JPanel();
		JPanel bPanel = new JPanel();
		cPanel.setBackground(Color.WHITE);
		controlPanel.setBackground(Color.WHITE);
		bPanel.setBackground(Color.WHITE);
		resultFrame.add(cPanel);
		resultFrame.add(controlPanel);
		resultFrame.add(bPanel);
		cPanel.setLayout(new GridLayout(m + 1, n + 1));
		controlPanel.setLayout(new GridLayout(3, 1));
		bPanel.setLayout(new GridLayout(m + 1, n + 1));

		LCSlabel = new JLabel("", JLabel.CENTER);
		LCSlabel.setFont(new Font("Serif", Font.PLAIN, 40));
		start = new JButton("Start");
		start.setFocusPainted(false);
		start.setBackground(Color.GREEN);
		again = new JButton("Again");
		again.setFocusPainted(false);
		again.setBackground(Color.RED);
		controlPanel.add(LCSlabel);
		controlPanel.add(start);
		controlPanel.add(again);
		resultFrame.setVisible(true);

		blabel = new JLabel[(m + 1) * (n + 1)];
		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				JLabel clabel = new JLabel(String.valueOf(c[i][j]),
						JLabel.CENTER);
				blabel[i * (n + 1) + j] = new JLabel(String.valueOf(b[i][j]),
						JLabel.CENTER);
				cPanel.add(clabel);
				bPanel.add(blabel[i * (n + 1) + j]);
			}
		}

		count = 0;
		resulti = new int[m + n];
		resultj = new int[m + n];
		lcs = new char[m + n];
		getLCS(m, n);

		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				start.setEnabled(false);
				showProcess();
			}
		});
		again.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				for (int i = 0; i < count; i++) {
					blabel[resulti[i] * (n + 1) + resultj[i]]
							.setBackground(Color.WHITE);
				}
				showProcess();

			}
		});
	}

	private static void getLCS(int i, int j) {
		resulti[count] = i;
		resultj[count] = j;
		lcs[count] = b[i][j];
		count++;
		if (i == 0 || j == 0) {
			return;
		}
		if (b[i][j] == '↖') {
			getLCS(i - 1, j - 1);
		} else if (b[i][j] == '↑') {
			getLCS(i - 1, j);
		} else {
			getLCS(i, j - 1);
		}
	}

	private static void showProcess() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				again.setEnabled(false);
				String s = "";
				for (int i = 0; i < count; i++) {
					blabel[resulti[i] * (n + 1) + resultj[i]]
							.setBackground(Color.GRAY);
					blabel[resulti[i] * (n + 1) + resultj[i]].setOpaque(true);// 设置不透明
					if (lcs[i] == '↖') {
						s += String.valueOf(X.charAt(resulti[i] - 1));
						LCSlabel.setText(s);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				again.setEnabled(true);
			}

		});
		thread.start();
	}

}
