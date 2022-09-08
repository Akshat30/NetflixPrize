import java.util.ArrayList;
import java.util.TreeMap;

public class MovieLensCSVTranslator {
	public Movie translateMovie(String line) {
		ArrayList<String> pieces = getLinePieces(line); // Get all the sections of the line separated by commas (but not
														// in quotes)

		int id = Integer.parseInt(pieces.get(0)); // ID is the first piece of data

		String details = pieces.get(1);

		int index = details.lastIndexOf('(');
		String title = "";
		if (index != -1)
			title = details.substring(0, details.lastIndexOf('(')); // title is the first piece of data
		else
			title = details;

		int yearStart = details.lastIndexOf("(");
		int year = 0;
		if (yearStart != -1)
			year = Integer.parseInt(details.substring(yearStart + 1, yearStart + 5)); // Extract the year from inside
																						// parenthesis
		else
			year = -1;

		String[] genres = null;

		if (!pieces.get(2).contains("("))
			genres = pieces.get(2).split("\\|");
		else
			genres = new String[0];

		return new Movie(id, year, title, genres);
	}

	public void translateLink(String line, TreeMap<Integer, Movie> movies) {
		ArrayList<String> pieces = getLinePieces(line);

		int id = Integer.parseInt(pieces.get(0));
		int imdbid = Integer.parseInt(pieces.get(1));
		int tmdbid = 0;
		try {
			tmdbid = Integer.parseInt(pieces.get(2));
		} catch (NumberFormatException e) {
			tmdbid = -1;
		}

		movies.get(id).setImdbid(imdbid);
		movies.get(id).setTmdbid(tmdbid);
	}
	
	public Rating translateRating(String line) {
		ArrayList<String> pieces = getLinePieces(line);
		
		int uid = Integer.parseInt(pieces.get(0));
		int mid = Integer.parseInt(pieces.get(1));
		double r = Double.parseDouble(pieces.get(2));
		int ts = Integer.parseInt(pieces.get(3));
		
		return new Rating(uid, mid, r, ts);
	}
	
	public Tag translateTag(String line) {
		ArrayList<String> pieces = getLinePieces(line);
		
		int uid = Integer.parseInt(pieces.get(0));
		int mid = Integer.parseInt(pieces.get(1));
		String t = pieces.get(2);
		int ts = Integer.parseInt(pieces.get(3));
		
		return new Tag(uid, mid, t, ts);
	}

	public ArrayList<String> getLinePieces(String line) {
		ArrayList<String> pieces = new ArrayList<String>(); // Holds comma separated pieces of the line
		boolean quoted = false; // Keeps track of whether the current character is inside of quotes or not
		int start = 0;
		for (int i = 0; i < line.length(); i++) { // For each character...
			char thisChar = line.charAt(i);
			if (thisChar == '"') { // If we see a quote symbol
				quoted = !quoted; // Then we're inside of quotes
			} else if (thisChar == ',' && !quoted) { // If we see a comma and we're not inside of quotes
				pieces.add(line.substring(start, i)); // Add this chunk of data to the pieces list
				start = i + 1;
			}
		}
		pieces.add(line.substring(start));
		return pieces;
	}
}
