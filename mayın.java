package mayýntarlasý;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class mayýn extends Application {

	private static final int kutu = 40;
	private static final int W = 800;
	private static final int H = 600;

	private static final int X_kutu = W / kutu;
	private static final int Ykutu = H / kutu;

	private Tile[][] grid = new Tile[X_kutu][Ykutu];
	private Scene scene;

	private Parent icolustur() {
		Pane root = new Pane();
		root.setPrefSize(W, H);

		for (int y = 0; y < Ykutu; y++) {
			for (int x = 0; x < X_kutu; x++) {
				Tile tile = new Tile(x, y, Math.random() < 0.2);

				grid[x][y] = tile;
				root.getChildren().add(tile);
			}
		}

		for (int y = 0; y < Ykutu; y++) {
			for (int x = 0; x < X_kutu; x++) {
				Tile tile = grid[x][y];

				if (tile.bombakoy)
					continue;

				long bomba = getNeighbors(tile).stream().filter(t -> t.bombakoy).count();

				if (bomba > 0)
					tile.text.setText(String.valueOf(bomba));
			}
		}

		return root;
	}

	private List<Tile> getNeighbors(Tile tile) {
		List<Tile> neighbors = new ArrayList<>();

		int[] points = new int[] { -1, -1, -1, 0, -1, 1, 0, -1, 0, 1, 1, -1, 1, 0, 1, 1 };

		for (int i = 0; i < points.length; i++) {
			int dx = points[i];
			int dy = points[++i];

			int newX = tile.x + dx;
			int newY = tile.y + dy;

			if (newX >= 0 && newX < X_kutu && newY >= 0 && newY < Ykutu) {
				neighbors.add(grid[newX][newY]);
			}
		}

		return neighbors;
	}

	private class Tile extends StackPane {
		private int x, y;
		private boolean bombakoy;
		private boolean acýk = false;

		private Rectangle border = new Rectangle(kutu - 2, kutu - 2);
		private Text text = new Text();

		public Tile(int x, int y, boolean hasBomb) {
			this.x = x;
			this.y = y;
			this.bombakoy = hasBomb;

			border.setStroke(Color.LIGHTGRAY);

			text.setFont(Font.font(18));
			text.setText(hasBomb ? "X" : "");
			text.setVisible(false);

			getChildren().addAll(border, text);

			setTranslateX(x * kutu);
			setTranslateY(y * kutu);

			setOnMouseClicked(e -> ac());
		}

		public void ac() {
	 
			if (acýk)
				return;
         
			if (bombakoy) {
				System.out.println("oyun bitti");
				JOptionPane.showMessageDialog(null, "kaybettin");
				scene.setRoot(icolustur());
				return;
			}
			 
			acýk = true;
			text.setVisible(true);
			border.setFill(null);

			if (text.getText().isEmpty()) {
				getNeighbors(this).forEach(Tile::ac);
			}
		}
	}

	public void start(Stage stage) throws Exception {
		scene = new Scene(icolustur());

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}