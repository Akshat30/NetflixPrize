import java.util.ArrayList;

public class User {

	
	private int userID;
	private ArrayList<Tag> tags;
	private ArrayList<Rating> ratings;
	
	public User(int uid) {
		userID = uid;
		tags = new ArrayList<Tag>();
		ratings = new ArrayList<Rating>();
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
	
	public String toString() {
		String out = "USERID: " + userID;
		out += "\nTags: " + tags;
		out += "\nRated Movies" + ratings;
		
		return out;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void addTag(Tag t) {
		tags.add(t);
	}
	
	public void addRating(Rating r) {
		ratings.add(r);
	}
	
	public ArrayList<Tag> getTags(){
		return tags;
	}
	
	public ArrayList<Rating> getRating(){
		return ratings;
	}
	
}
