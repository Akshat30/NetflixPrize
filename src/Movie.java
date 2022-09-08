import java.util.*;

public class Movie {
	private ArrayList<Rating> ratings;
	private int id;
	private int imdbid;
	private int tmdbid;
	private String title;
	private ArrayList<Tag> tags;
	private String[] genres;
	private int year;

	public Movie(int id, int year, String title, String[] genres) {
		this.id = id;
		this.title = title;
		this.genres = genres;
		this.year = year;
		ratings = new ArrayList<Rating>();
		imdbid = 0;
		tmdbid = 0;
		tags = new ArrayList<Tag>();
	}

	public String toString() {
		String out = "ID: " + id;
		out += "\nYEAR: " + year;
		out += "\nTITLE: " + title;
		out += "\nGENRES: " + Arrays.toString(genres);
		out += "\nIMDBID: " + imdbid;
		out += "\nTMDBID: " + tmdbid;
		out += "\nRATINGS" + ratings.toString();
		out += "\nTAGS" + tags.toString();

		return out;
	}

	public double getAvgRating() {
		
		if(ratings.size() == 0)
			return -1;
		
		double rating = 0;
		for (Rating r : ratings) {
			rating += r.getRating();
		}
		
		return rating/ratings.size();
	}

	public ArrayList<Rating> getRatings() {
		return ratings;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

	public void addRating(Rating r) {
		ratings.add(r);
	}

	public void addTag(Tag t) {
		tags.add(t);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImdbid() {
		return imdbid;
	}

	public void setImdbid(int imdbid) {

		this.imdbid = imdbid;
	}

	public int getTmdbid() {
		return tmdbid;
	}

	public void setTmdbid(int tmdbid) {
		this.tmdbid = tmdbid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
