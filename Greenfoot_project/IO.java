import java.util.*;
import java.io.*;
public class IO  
{
    public static void writeFile(String fileName, String[] lines) 
    //throws IOException
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (int x = 0; x < lines.length; x++)
            {
                writer.write(lines[x]);
                if (x < lines.length - 1) 
                    writer.newLine();

            }
            writer.close();
        }
        catch (Exception e) {
            System.out.println("Unable to write file");
        }
    }

    public static String[] readFile(String fileName)
    {

        File file = new File(fileName);
        String[] lines = null;
        try {
            int nlines= 0;
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine())
            {
                String s = sc.nextLine();
                nlines++;
            }
            sc.close();
            sc = new Scanner(file);
            lines = new String[nlines];
            nlines = 0;
            while (sc.hasNextLine())
            {
                lines[nlines] = sc.nextLine();
                nlines++;
            }
            sc.close();

        } 
        catch (Exception e) {
            System.out.println("Unable to open " + file);
        }
        return lines;
    }
}
