package com.comicrelief.main;
import java.io.IOException;

/**
 * Created by manoharprabhu on 10/15/2015.
 */
public class Main {


	public static void main(String[] args) throws IOException, InterruptedException {
		ComicRelief.generateComics(args,new IComicsGenerateUpdate() {
			
			@Override
			public void startWritingFrame(int frame) {
				System.out.println("Writing frame No: " + frame);
			}
			
			@Override
			public void startParsingSubtitles() {
				System.out.println("Parsing the subtitles");
				
			}
			
			@Override
			public void finishWritingFrame(int frame) {
				System.out.println("Wrote frame No: " + frame);
			}
			
			@Override
			public void finishParsingSubtitles() {
				System.out.println("Finished parsing subtitles");
			}
			
			@Override
			public void finishGeneratingComics() {
				System.out.println("Generated comics successfully");
			}

			@Override
			public void encounteredError(Exception e,String errorMessage) {
				if(e != null) {
					System.out.println("Encountered error while processing your request.");
					e.printStackTrace();
				} else {
					System.out.println(errorMessage);
				}
				
			}
		});
	}


}