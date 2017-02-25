import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Indexer {
    public static File [] listofFiles = new File [74];
    public static ArrayList<File> allFiles= new ArrayList<File>();

    static void addElement(ConcurrentHashMap<String,ConcurrentLinkedQueue<String>>  map,String key,String value )
    {
    	ConcurrentLinkedQueue<String> q = map.get(key);
    	if(q == null) 
    	{
    	  q = new ConcurrentLinkedQueue<String>();
    	  ConcurrentLinkedQueue<String> curQ = map.putIfAbsent(key, q);
    	  if(curQ != null) 
    	  {
    	    q = curQ;
    	  }
    	}
    	q.add(value);
    }
    
    static ArrayList<File> splitFile(Path path) throws IOException
    {
    	  try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) 
    	  {
    	        for (Path entry : stream) 
    	        {
    	            if (Files.isDirectory(entry)) 
    	            {
    	            	allFiles.add(entry.toFile());
    	            }

    	        }
    	  }
		return allFiles;
    }
    	  
    static void i1() throws FileNotFoundException, IOException
    {
		File [] s1=Arrays.copyOfRange(listofFiles,0,37);
		String line;
		for (File f: s1)
		{
			ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			for(Path file:stream)
			{
				try (
				    InputStream fis = new FileInputStream(file.toFile());
				    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				    BufferedReader br = new BufferedReader(isr);
				) 
				{
				    while ((line = br.readLine()) != null) 
				    {
				        String[] words = line.split(" ");
				        for(String word:words)
				        {
				        	addElement(map,word,f.getName());
				        }
	
				    }
				}
			}
			outWrite(map,f.getAbsolutePath());
			map=null;
			System.gc();
			System.out.println(f.getAbsolutePath());
			
		}
    	
    }
    
    static void i2() throws FileNotFoundException, IOException
    {
		File [] s2=Arrays.copyOfRange(listofFiles, 37, 74);
		String line;
		for (File f: s2)
		{
			ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = new ConcurrentHashMap<>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(f.toPath());
			for(Path file:stream)
			{
				try (
				    InputStream fis = new FileInputStream(file.toFile());
				    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				    BufferedReader br = new BufferedReader(isr);
				) 
				{
				    while ((line = br.readLine()) != null) 
				    {
				        String[] words = line.split(" ");
				        for(String word:words)
				        {
				        	addElement(map,word,f.getName());
				        }
	
				    }
				}
			}
			outWrite(map,f.getAbsolutePath());
			map=null;
			System.gc();
			System.out.println(f.getAbsolutePath());

		}
    }
    
	static synchronized void outWrite(ConcurrentHashMap<String,ConcurrentLinkedQueue<String>> map,String dir) throws IOException
	{
		  try {
		         FileOutputStream fileOut =
		         new FileOutputStream(dir+".ser");
		         ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         out.writeObject(map);
		         out.close();
		         fileOut.close();
		         System.out.printf("Serialized data is saved:");
		      }
		  catch(IOException i) 
		  {
		      i.printStackTrace();
		  }

	}

	public static void main(String[] args) throws InterruptedException, IOException 
	{
		splitFile(Paths.get("D:\\Desktop\\WEBPAGES_RAW"));
		listofFiles=  allFiles.toArray(listofFiles);
		ExecutorService service= Executors.newFixedThreadPool(2);
		service.submit(() -> {
			try {
				i1();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		service.submit(() -> {
			try {
				i2();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		service.awaitTermination(1, TimeUnit.SECONDS);
		service.shutdown();
//		for (String key:map.keySet())
//			System.out.println(key + ":" +(map.get(key).toString()));
	}
}
