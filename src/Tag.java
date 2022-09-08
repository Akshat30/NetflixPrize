
public class Tag {
	private int userID;
	private int movieID;
	private String tag;
	private int timestamp;
	
	public Tag(int uid, int mid, String t, int ts) {
		userID = uid;
		movieID = mid;
		tag = t;
		timestamp = ts;
	}
	
	public String toString() {
		String out = "USER ID: " + userID;
		out += "\nMOVIE ID: " + movieID;
		out += "\nTAG: " + tag;
		out += "\nTIMESTAMP: " + timestamp;

		return out;
	}
	
	public int getUserID() {
		return userID;
	}

	public int getMovieID() {
		return movieID;
	}

	public String getTag() {
		return tag;
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

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
