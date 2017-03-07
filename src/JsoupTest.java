import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JsoupTest {

	public static void main(String[] args) throws IOException {
		File file= new File("D:\\Desktop\\WEBPAGES_RAW\\0\\14");
		Document fileDoc = Jsoup.parse(file, "UTF-8");
		
    	Elements title = fileDoc.getElementsByTag("title");
    	Elements h1 = fileDoc.getElementsByTag("h1");
    	Elements h2 = fileDoc.getElementsByTag("h2");
    	Elements h3 = fileDoc.getElementsByTag("h3");
    	Elements bold = fileDoc.getElementsByTag("b");
		
    	
    	String[] tokens = fileDoc.text().split(" ");
    	for(String t:tokens)
    		System.out.println(t);
	}

}
