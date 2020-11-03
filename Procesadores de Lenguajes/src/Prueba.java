import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Prueba {

	public static void main(String[] args) throws IOException {
		/*
		 * File f = new File("input.txt"); // Creation of File Descriptor for input file
		 * FileReader fr = new FileReader(f); // Creation of File Reader object
		 * BufferedReader br = new BufferedReader(fr); // Creation of BufferedReader
		 * object int c = 0; while ((c = br.read()) != -1) // Read char by Char {
		 * 
		 * char character = (char) c; // converting integer to char
		 * System.out.println(character == 10); System.out.println(character); //
		 * Display the Character }
		 */

		toFile();
		int f = 46;
		char h = 'a';
		String s ="Hola";
		s += (char)f;
		s += h;
		System.out.println(s);
	}

	private static void toFile() throws IOException {
		FileWriter myWriter = new FileWriter("filename.txt");
		myWriter.write("Files in Java mig \n"
				+ "is fun \n"
				+ "enough!\n");
		myWriter.close();
	}

}
