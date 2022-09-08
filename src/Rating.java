import java.util.Arrays;

public class Rating implements Comparable<Rating> {
	private int userID;
	private int movieID;
	private double rating;
	private int timestamp;

	public Rating(int uid, int mid, double r, int ts) {
		userID = uid;
		movieID = mid;
		rating = r;
		timestamp = ts;
	}
	
	public Rating(int uid, int mid) {
		userID = uid;
		movieID = mid;
	}
	

	public String toString() {
		String out = "USER ID: " + userID;
		out += "\nMOVIE ID: " + movieID;
		out += "\nRATING: " + rating;
		out += "\nTIMESTAMP: " + timestamp;

		return out;
	}

	public int getUserID() {
		return userID;
	}

	public int getMovieID() {
		return movieID;
	}

	public double getRating() {
		return rating;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int compareTo(Rating o) {
		if(this.getMovieID() == o.getMovieID() && this.getUserID() == o.getUserID())
			return 0;
		else if(this.getMovieID() > o.getMovieID())
			return 1;
		else 
			return -1;
	}
}
