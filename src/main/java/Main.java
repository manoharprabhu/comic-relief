
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by manoharprabhu on 10/15/2015.
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        for(String s: args) {
        	System.out.println(s);
        }
    	if(args.length != 3){
            printUsage();
            System.exit(1);
        }
    	
    	if(!System.getenv("PATH").contains("ffmpeg")){
    		System.out.println("ffmpeg not found in PATH");
    		System.exit(1);
    	}
        
        List<Dialogue> list = SRTParser.parse(new File(args[0]));
        List<String> command = new ArrayList<String>();

        command.add("D:\\ffmpeg\\bin\\ffmpeg");
        command.add("-ss");
        command.add("56");
        command.add("-i");
        command.add(args[1]);
        command.add("-vframes");
        command.add("1");
        command.add("-s");
        command.add("640x480");
        command.add("-q:v");
        command.add("2");
        command.add("E:\\shots\\out.jpg");

        for(int i=0;i<list.size();i++){
            double time = (list.get(i).getEndTime() + list.get(i).getStartTime())/2000;
            command.set(2, String.valueOf(time));
            command.set(11, args[2] + "\\" + i + ".jpg");
            SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command);
            commandExecutor.executeCommand();

            BufferedImage image = ImageIO.read(new File(args[2] + "\\" +  i + ".jpg"));
            Graphics g = image.getGraphics();
            g.setFont(g.getFont().deriveFont(16f));
            g.setColor(new Color(0, 0,0, 128 ));
            g.fillRect(0, 440, 640, 40);
            g.setColor(Color.white);
            g.drawString(list.get(i).getDialogueText(), 10, image.getHeight() - 15);
            g.dispose();
            ImageIO.write(image,"jpg",new File(args[2] + "\\"  + i + ".jpg"));
            System.out.print("\rProcessed frame " + i +" of " + list.size());
            //System.out.println(commandExecutor.getStandardErrorFromCommand());
        }

    }

    public static void printUsage(){
        System.out.println("Usage: java -jar comic-relief.jar <subtitle_file> <movile_file> <output_folder>");
    }

}
