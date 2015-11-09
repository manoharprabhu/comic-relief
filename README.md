Comic Relief
============
Convert your movies along with its subtitle into readable comics.

#### Usage
````java
String[] arguments;
ComicRelief.generateComics(arguments,new IComicsGenerateUpdate(){
	@Override
	public void startWritingFrame(int frame) {
		//Called before writing frame no: 'frame' to disk. 
	}
	
	@Override
	public void startParsingSubtitles() {
		//Called before starting to parse the subtitle file.
	}
	
	@Override
	public void finishWritingFrame(int frame) {
		//Called after writing frame no: 'frame' to disk.
	}
	
	@Override
	public void finishParsingSubtitles() {
		//Called after the subtitle file has been parsed successfully.
		//Wont be called if the subtitle file is malformed or the code 
		//is unable to parse the file for some reason.
	}
	
	@Override
	public void finishGeneratingComics() {
		//Called after all the frames of comics have been written
		//to disk.
	}

	@Override
	public void encounteredError(Exception e,String errorMessage) {
		//Called if error is encountered while parsing the arguments, 
		//processing the subtitle or the movie file.
	}
});
````

#### Requirement
ffmpeg has to be installed and added to the PATH variable