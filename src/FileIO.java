import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileIO {

	public static final String fileSep = System.getProperty("file.separator");
	public static final String lineSep = System.getProperty("line.separator");

	public static ArrayList<String> readFile(String name) throws IOException {
		String line;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(name));

			line = in.readLine();
			
			while (line != null) // while not end of file
			{
				lines.add(line);
				line = in.readLine();
			}
			in.close();
		} catch (IOException iox) {
			System.out.println("Problem reading " + name);
		}
		return lines;
	}

	public static void writeFile(ArrayList<String> lines, String name) throws IOException {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(name));
			for(String line : lines) {
				out.write(line + "\n");
			}
			out.close();
		} catch (IOException iox) {
			System.out.println("Problem writing " + name);
		}
	}

}
