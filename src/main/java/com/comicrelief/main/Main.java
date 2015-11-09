package com.comicrelief.main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.comicrelief.helpers.SystemCommandExecutor;

/**
 * Created by manoharprabhu on 10/15/2015.
 */
public class Main {

	private static final int IMAGE_WIDTH = 1024;
	private static final int IMAGE_HEIGHT = 768;
	private static final String IMAGE_RES = IMAGE_WIDTH + "x" + IMAGE_HEIGHT;
	private static final int FONT_SIZE = 21;

	public static void main(String[] args) throws IOException, InterruptedException {

		String validate = null;
		Map<String, String> commandMap = buildCommand(args);
		if (commandMap == null) {
			System.exit(1);
		}
		if ((validate = validateInput(commandMap)) != null) {
			System.out.println(validate);
			System.exit(1);
		}
		List<Dialogue> list = SRTParser.parse(new File(commandMap.get("-s")));
		if (list == null) {
			System.out.println("Error while parsing subtitle file");
			System.exit(1);
		}

		List<String> command = initializeCommand(commandMap);

		for (Dialogue dialogue : list) {
			prepareCommandForFrame(command, dialogue, commandMap);
			File outputPicture = getOutputPictureForFrame(commandMap, dialogue);
			SystemCommandExecutor commandExecutor = new SystemCommandExecutor(command);
			commandExecutor.executeCommand();
			addCaptionToPicture(outputPicture, dialogue);
			System.out.print("\rProcessed frame " + dialogue.getDialogueNumber() + " of " + list.size());
		}
	}

	private static void addCaptionToPicture(File outputPicture, Dialogue dialogue) throws IOException {
		BufferedImage image = ImageIO.read(outputPicture);
		Graphics g = image.getGraphics();
		g.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE));
		g.setColor(new Color(0, 0, 0, 190));
		g.fillRect(0, IMAGE_HEIGHT - 40, IMAGE_WIDTH, 40);
		g.setColor(Color.white);
		g.drawString(dialogue.getDialogueText(),
				(IMAGE_WIDTH / 2) - ((dialogue.getDialogueText().length() * (FONT_SIZE / 2)) / 2), IMAGE_HEIGHT - 15);
		g.dispose();
		ImageIO.write(image, "jpg", outputPicture);
	}

	private static List<String> initializeCommand(Map<String, String> commandMap) {
		List<String> command = new ArrayList<String>();
		String quality = "2";
		command.add("ffmpeg");
		command.add("-ss");
		command.add("TIME_arg[2]");
		command.add("-i");
		command.add(commandMap.get("-m"));
		command.add("-vframes");
		command.add("1");
		command.add("-s");
		command.add(IMAGE_RES);
		command.add("-q:v");
		command.add(quality);
		command.add("OUTPUT_FOLDER_arg[11]");
		return command;
	}

	private static File getOutputPictureForFrame(Map<String, String> commandMap, Dialogue dialogue) {
		return new File(getPathToPicture(commandMap, dialogue));
	}

	private static void prepareCommandForFrame(List<String> command, Dialogue dialogue,
			Map<String, String> commandMap) {
		double time = dialogue.getMedianTimeSeconds();
		command.set(2, String.valueOf(time));
		command.set(11, getPathToPicture(commandMap, dialogue));
	}

	private static String getPathToPicture(Map<String, String> commandMap, Dialogue dialogue) {
		return commandMap.get("-o") + File.separator + dialogue.getDialogueNumber() + ".jpg";
	}

	private static String validateInput(Map<String, String> map) {
		try {
			List<String> command = new ArrayList<String>();
			command.add("ffmpeg");
			SystemCommandExecutor cmd = new SystemCommandExecutor(command);
			if (cmd.executeCommand() != 1) {
				return "ffmpeg command needs to be in PATH";
			}
			if (!map.containsKey("-s")) {
				return "Subtitle file not specified";
			}
			if (!map.containsKey("-m")) {
				return "Movie file not specified";
			}
			if (!map.containsKey("-o")) {
				return "Output folder not specified";
			}
			File f = new File(map.get("-s"));
			if (!f.exists() || !f.isFile()) {
				return "Subtitle file not found";
			}
			f = new File(map.get("-m"));
			if (!f.exists() || !f.isFile()) {
				return "Movie file not found";
			}
			f = new File(map.get("-o"));
			if (!f.exists() || !f.isDirectory()) {
				return "Output folder doesn't exist or it is not a folder";
			}
		} catch (IOException e) {
			return "Error while checking the supplied files";
		} catch (InterruptedException e) {
			return "Error while checking the supplied files";
		}
		return null;
	}

	private static Map<String, String> buildCommand(String[] args) {
		if (args.length % 2 == 1) {
			System.out.println(
					"Usage: java -jar comic-relief.jar -s <subtitle_file> -m <movile_file> -o <output_folder>");
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < args.length; i = i + 2) {
			if (args[i].equals("-s")) {
				map.put("-s", args[i + 1]);
			} else if (args[i].equals("-m")) {
				map.put("-m", args[i + 1]);
			} else if (args[i].equals("-o")) {
				map.put("-o", args[i + 1]);
			} else {
				System.out.println("Invalid input option " + args[i]);
				return null;
			}
		}

		return map;

	}
}