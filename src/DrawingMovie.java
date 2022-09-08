
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import processing.core.PApplet;
import processing.core.PImage;

public class DrawingMovie {

	private Movie movie;
	private PImage coverArt;

	public DrawingMovie(Movie m) {
		this.movie = m;
		coverArt = null;
	}

	public void draw(PApplet drawer, float x, float y, float width, float height) {
		if (movie != null) {
			if (coverArt != null) {
				drawer.image(coverArt, x, y, width, height);
			}

			String title = movie.getTitle();
			drawer.text(title, x, y);
		}
		drawer.stroke(0);
		drawer.noFill();
		drawer.rect(x, y, width, height);
	}

	public void downloadArt(PApplet drawer) {

		Thread downloader = new Thread(new Runnable() {

			@Override
			public void run() {

				// Find the cover art using IMDB links
				// Initialize coverArt

				Scanner scan = null;

				String movieId = movie.getImdbid() + "";

				int diff = 7 - movieId.length();
				for (int i = 0; i < diff; i++) {
					movieId = "0" + movieId;
				}

				String url = "https://imdb.com/title/tt" + movieId + "/";

				try {
					String output = "";

					URL reader = new URL(url);
					scan = new Scanner(reader.openStream());

					while (scan.hasNextLine()) {
						String line = scan.nextLine();
						output += line + "\n";
					}

					int pIndex = output.lastIndexOf("Poster\"");
					int eIndex = output.indexOf('=', pIndex);
					int qIndex = output.indexOf('"', eIndex);
					int q1Index = output.indexOf('"', qIndex + 1);
					String imgURL = output.substring(qIndex + 1, q1Index);
					coverArt = drawer.loadImage(imgURL);

				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (scan != null)
						scan.close();
				}
			}

		});

		downloader.start();

	}

}
