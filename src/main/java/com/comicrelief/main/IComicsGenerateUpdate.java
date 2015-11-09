package com.comicrelief.main;

public interface IComicsGenerateUpdate {
	
	void startParsingSubtitles();
	void finishParsingSubtitles();
	void startWritingFrame(int frame);
	void finishWritingFrame(int frame);
	void finishGeneratingComics();
	void encounteredError(Exception e,String errorMessage);
}
