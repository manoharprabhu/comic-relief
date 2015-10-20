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
	public static final int ARG_SUBTITLE_FILE = 0;
	public static final int ARG_MOVIE_FILE = 1;
	public static final int ARG_OUTPUT_FOLDER = 2;
	
	public static void main(String[] args) throws IOException,
			InterruptedException {

		String validate = null;
		if ((validate = validateInput(args)) != null) {
			System.out.println(validate);
			System.exit(1);
		}
		List<Dialogue> list = SRTParser.parse(new File(args[0]));
		if (list == null) {
			System.out.println("Error while parsing subtitle file");
			System.exit(1);
		}
		List<String> command = new ArrayList<String>();
		String quality = "2";
		command.add("ffmpeg");
		command.add("-ss");
		command.add("TIME_arg[2]");
		command.add("-i");
		command.add(args[ARG_MOVIE_FILE]);
		command.add("-vframes");
		command.add("1");
		command.add("-s");
		command.add("640x480");
		command.add("-q:v");
		command.add(quality);
		command.add("OUTPUT_FOLDER_arg[11]");

		for (int i = 0; i < list.size(); i++) {
			double time = (list.get(i).getEndTime() + list.get(i)
					.getStartTime()) / 2000;
			command.set(2, String.valueOf(time));
			command.set(11, args[ARG_OUTPUT_FOLDER] + "\\" + i + ".jpg");
			SystemCommandExecutor commandExecutor = new SystemCommandExecutor(
					command);
			commandExecutor.executeCommand();
			BufferedImage image = ImageIO.read(new File(args[ARG_OUTPUT_FOLDER] + "\\" + i
					+ ".jpg"));
			Graphics g = image.getGraphics();
			g.setFont(g.getFont().deriveFont(16f));
			g.setColor(new Color(0, 0, 0, 128));
			g.fillRect(0, 440, 640, 40);
			g.setColor(Color.white);
			g.drawString(list.get(i).getDialogueText(), 10,
					image.getHeight() - 15);
			g.dispose();
			ImageIO.write(image, "jpg", new File(args[ARG_OUTPUT_FOLDER] + "\\" + i + ".jpg"));
			System.out.print("\rProcessed frame " + i + " of " + list.size());
		}

	}

	public static String validateInput(String[] args) {
		// args[0] subtitle file
		// args[1] movie file
		// args[2] output folder
		if (args.length != 3) {
			return "Usage: java -jar comic-relief.jar <subtitle_file> <movile_file> <output_folder>";
		}
		try {
			List<String> command = new ArrayList<String>();
			command.add("ffmpeg");
			SystemCommandExecutor cmd = new SystemCommandExecutor(command);
			try {
				if (cmd.executeCommand() != 1) {
					return "ffmpeg command needs to be in PATH";
				}
			} catch (Exception e) {
				return "ffmpeg command needs to be in PATH";
			}
			File f = new File(args[0]);
			if (!f.exists() || !f.isFile()) {
				return "Subtitle file not found";
			}
			f = new File(args[1]);
			if (!f.exists() || !f.isFile()) {
				return "Movie file not found";
			}
			f = new File(args[2]);
			if (!f.exists() || !f.isDirectory()) {
				return "Output folder doesn't exist or it is not a folder";
			}
		} catch (ArithmeticException e) {
			return "Error in input files";
		}
		return null;
	}
}