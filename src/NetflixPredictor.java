import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

public class NetflixPredictor {

	// Add fields to represent your database.
	private ArrayList<Rating> ratings = new ArrayList<Rating>();
	private ArrayList<Tag> tags = new ArrayList<Tag>();
	private TreeMap<Integer, User> users;
	private TreeMap<Integer, Movie> movies;

	/**
	 * 
	 * Use the file names to read all data into some local structures.
	 * 
	 * @param movieFilePath  The full path to the movies database.
	 * @param ratingFilePath The full path to the ratings database.
	 * @param tagFilePath    The full path to the tags database.
	 * @param linkFilePath   The full path to the links database.
	 */
	public NetflixPredictor(String movieFilePath, String ratingFilePath, String tagFilePath, String linkFilePath) {
		movies = new TreeMap<Integer, Movie>();
		users = new TreeMap<Integer, User>();
		ratings = new ArrayList<Rating>();
		tags = new ArrayList<Tag>();

		MovieLensCSVTranslator translator = new MovieLensCSVTranslator();
		initializeMovies(movies, translator, movieFilePath);
		addLinks(movies, translator, linkFilePath);
		initializeRatings(ratings, movies, users, translator, ratingFilePath);
		initializeTags(tags, movies, users, translator, tagFilePath);

	}

	/**
	 * If userNumber has rated movieNumber, return the rating. Otherwise, return -1.
	 * 
	 * @param userNumber  The ID of the user.
	 * @param movieNumber The ID of the movie.
	 * @return The rating that userNumber gave movieNumber, or -1 if the user does
	 *         not exist in the database, the movie does not exist, or the movie has
	 *         not been rated by this user.
	 */
	public double getRating(int userID, int movieID) {
		if (movies.get(movieID) == null || users.get(userID) == null)
			return -1;
		else {
			User u = users.get(userID);
			int i = Collections.binarySearch(u.getRating(), new Rating(userID, movieID));
			if (i >= 0) {
				Rating r = u.getRating().get(i);
				return r.getRating();
			} else {
				return -1;
			}
		}
	}

	/**
	 * If userNumber has rated movieNumber, return the rating. Otherwise, use other
	 * available data to guess what this user would rate the movie.
	 * 
	 * @param userNumber  The ID of the user.
	 * @param movieNumber The ID of the movie.
	 * @return The rating that userNumber gave movieNumber, or the best guess if the
	 *         movie has not been rated by this user.
	 * @pre A user with id userID and a movie with id movieID exist in the database.
	 */
	public double guessRating(int userID, int movieID) { // 0.8493155799520817
		User u = users.get(userID);
		Movie m = movies.get(movieID);

		for (Rating r : u.getRating()) {
			if (movieID == r.getMovieID())
				return r.getRating();
		}

		final double movieRatingWeight = 2.13;
		final double movieYearRatingWeight = 8.3;
		final double userGenreWeight = 0.035;
		final double userTagWeight = 1.07;
		double total = 0;
		double counter = 0;

		double avg = u.getAvgRating();
		double year = 0;
		double count = 0;

		for (Rating r : u.getRating()) {
			for (String g : movies.get(r.getMovieID()).getGenres())
				for (String g2 : m.getGenres())
					if (g.equalsIgnoreCase(g2)) {
						total += r.getRating() * userGenreWeight;
						counter += 1 * userGenreWeight;
					}

			for (Tag t : movies.get(r.getMovieID()).getTags())
				for (Tag t2 : m.getTags()) {
					if (t.getTag().equalsIgnoreCase(t2.getTag())) {
						total += r.getRating() * userTagWeight;
						counter += 1 * userTagWeight;
					}
				}

			if (avg >= 0 && r.getRating() - avg > 1.11) {
				year += movies.get(r.getMovieID()).getYear();
				count++;
			}
		}

		double avgYear = year / count;

		double mr = m.getAvgRating();
		if (mr >= 0) {
			total += mr * counter / movieRatingWeight;
			counter += counter / movieRatingWeight;
			if (Math.abs(m.getYear() - avgYear) <= 39) {
				total += mr * counter / movieYearRatingWeight;
				counter += counter / movieYearRatingWeight;
			}
		}

		double val = 3.954;

		double thres = 0.88;
		double off = 0.44;

		if (counter == 0)
			return 3.8;

		if (total / counter - val >= thres)
			return total / counter - off;
		else if (total / counter - val <= -thres)
			return total / counter - off;
		else if (total / counter - val >= thres / 1.6)
			return total / counter - off / 4.45;
		else if (total / counter - val <= -thres / 1.6)
			return total / counter - off / 4.45;
		else
			return total / counter;
	}

	/**
	 * Recommend a movie that you think this user would enjoy (but they have not
	 * currently rated it).
	 * 
	 * @param userNumber The ID of the user.
	 * @return The ID of a movie that data suggests this user would rate highly (but
	 *         they haven't rated it currently).
	 * @pre A user with id userID exists in the database.
	 */
	public int recommendMovie(int userID) {
		User u = users.get(userID);
		double max = 0;
		int id = 0;
		for (Movie m : movies.values()) {
			boolean rated = false;
			for (Rating r : u.getRating()) {
				if (r.getMovieID() == m.getId()) {
					rated = true;
					break;
				}
			}
			if (rated)
				continue;
			else {
				double rating = guessRating(u.getUserID(), m.getId());
				double urating = u.getAvgRating();
				if (urating < 0)
					urating = 4;
				if (rating >= urating)
					return m.getId();
			}
			double arating = m.getAvgRating() + 0.5;
			if (arating > max) {
				max = arating;
				id = m.getId();
			}
		}

		return 2;
	}

	public static void initializeMovies(TreeMap<Integer, Movie> movies, MovieLensCSVTranslator translator,
			String path) {
		try {
			String moviesCSV = path;
			ArrayList<String> movieStrs = FileIO.readFile(moviesCSV);
			movieStrs.remove(0);
			for (String s : movieStrs) {
				Movie m = translator.translateMovie(s);
				movies.put(m.getId(), m);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void addLinks(TreeMap<Integer, Movie> movies, MovieLensCSVTranslator translator, String path) {
		try {
			String linksCSV = path;
			ArrayList<String> linkStrs = FileIO.readFile(linksCSV);
			linkStrs.remove(0);
			for (String s : linkStrs) {
				translator.translateLink(s, movies);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initializeRatings(ArrayList<Rating> ratings, TreeMap<Integer, Movie> movies,
			TreeMap<Integer, User> users, MovieLensCSVTranslator translator, String path) {
		try {
			String ratingsCSV = path;
			ArrayList<String> ratingStrs = FileIO.readFile(ratingsCSV);
			ratingStrs.remove(0);
			for (String s : ratingStrs) {
				Rating r = translator.translateRating(s);
				ratings.add(r);

				movies.get(r.getMovieID()).addRating(r);

				if (users.get(r.getUserID()) == null) {
					User u = new User(r.getUserID());
					u.addRating(r);
					users.put(r.getUserID(), u);
				} else {
					users.get(r.getUserID()).addRating(r);
				}
			}

			for(User u : users.values()) {
				Collections.sort(u.getRating());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void initializeTags(ArrayList<Tag> tags, TreeMap<Integer, Movie> movies, TreeMap<Integer, User> users,
			MovieLensCSVTranslator translator, String path) {
		try {
			String tagsCSV = path;
			ArrayList<String> tagStrs = FileIO.readFile(tagsCSV);
			tagStrs.remove(0);
			for (String s : tagStrs) {
				Tag t = translator.translateTag(s);
				tags.add(t);

				movies.get(t.getMovieID()).addTag(t);

				if (users.get(t.getUserID()) == null) {
					User u = new User(t.getUserID());
					u.addTag(t);
					users.put(t.getUserID(), u);
				} else {
					users.get(t.getUserID()).addTag(t);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Movie> getMovies() {
		ArrayList<Movie> mov = new ArrayList<Movie>();
		for (Movie m : movies.values()) {
			mov.add(m);
		}
		return mov;
	}

	public TreeMap<Integer, Movie> getMoviesTreeMap() {
		return movies;
	}
}
