import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manoharprabhu on 10/15/2015.
 */
public class SRTParser {
    private final static int PARSE_LINE_NUMBER = 0;
    private final static int PARSE_TIME = 1;
    private final static int PARSE_DIALOGUE = 2;

    public static List<Dialogue> parse(File f){
        InputStreamReader r = null;
		try {
			r = new InputStreamReader(new FileInputStream(f),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
        BufferedReader reader = new BufferedReader(r);
        try {
			BOMSkipper.skip(reader);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        String line;
        int state = PARSE_LINE_NUMBER;
        System.out.println("Started parsing subtitle file");
        List<Dialogue> list  = new ArrayList<Dialogue>();
        int lineNumber = 0;
        long startTime = 0;
        long endTime = 0;
        String dialogueText = "";
        try {
        while((line = reader.readLine()) != null){
            if(state == PARSE_LINE_NUMBER){
                lineNumber = Integer.parseInt(line);
                state = PARSE_TIME;
            } else if(state == PARSE_TIME){
                String[] time = line.split(" --> ");
                startTime = getTime(time[0]);
                endTime = getTime(time[1]);
                state = PARSE_DIALOGUE;
            } else if(state == PARSE_DIALOGUE){
                if(line.isEmpty()){
                    state = PARSE_LINE_NUMBER;
                    list.add(new Dialogue(lineNumber,startTime,endTime,dialogueText));
                    dialogueText = "";
                } else {
                    dialogueText += (line + " ");
                }
            }
        }
        if(lineNumber != 0){
        	list.add(new Dialogue(lineNumber,startTime,endTime,dialogueText));
        }
        } catch(Exception e){
        	return null;
        }
        System.out.println("Finished parsing " + list.size() + " dialogues in subtitle file");
        return list;
    }

    private static long getTime(String time){
        String[] parts = time.split(",");
        String[] firstParts = parts[0].split(":");
        int seconds = Integer.parseInt(firstParts[2]);
        seconds += (Integer.parseInt(firstParts[1]) * 60);
        seconds += (Integer.parseInt(firstParts[0]) * 60 * 60);
        long milliseconds = (seconds * 1000) + Integer.parseInt(parts[1]);
        return milliseconds;
    }
}
