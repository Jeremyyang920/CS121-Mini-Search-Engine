import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Searcher {

	private static ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> everything = new ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		 FileInputStream fileIn = new FileInputStream("D:\\Desktop\\WEBPAGES_RAW\\everything.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         everything = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
         
         Scanner query = new Scanner(System.in);
         String word;
         while(true)
         {
        	 System.out.println("Enter Query");
        	 word=query.nextLine();
        	 System.out.println(everything.get(word));
         }
	}

}
