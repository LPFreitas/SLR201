package utils;

import java.io.PrintStream;
import java.io.File;
import java.nio.charset.Charset;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Logger {
	private PrintStream stream = null;
	
	public Logger(PrintStream stream) {
		this.stream = new PrintStream(stream);
	}
	
	public Logger(File file) {
		try {
			this.stream = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void log(String message) {
		try {
			this.stream.write(message.getBytes(Charset.forName("UTF-8")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
