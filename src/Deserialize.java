import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Deserialize 
{
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws ClassNotFoundException
	{
		String s = "";
		for (int i = 0; i < 75; i++)
		{
			if (i == 9)
				continue;
			
			try 
			{
				 ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map;
		         FileInputStream fileIn = new FileInputStream("C:\\Users\\anujs_000\\Desktop\\WEBPAGES_RAW\\" + Integer.toString(i) + ".ser");
		         ObjectInputStream in = new ObjectInputStream(fileIn);
		         map = (ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>) in.readObject();
		         s += map.toString();
		         s += "\n\n";
		         in.close();
		         fileIn.close();
		    }
			catch(IOException e) 
			{
		         e.printStackTrace();
		         return;
			}
		}
		
		BufferedWriter writer = null;
        try 
        {
            // create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(s);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                // Close the writer regardless of what happens...
                writer.close();
            }
            catch (Exception e) {}
        }
	}
}

