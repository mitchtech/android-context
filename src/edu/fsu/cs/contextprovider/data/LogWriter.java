package edu.fsu.cs.contextprovider.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LogWriter{
		
	BufferedWriter buff = null;
	
	public LogWriter(String dest){
		try {
			buff = new BufferedWriter(new FileWriter(dest, true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void flush(){
		try {
			buff.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void write(String line){
		
		try {
//			buff.write("<<" + System.currentTimeMillis() + " " + line + ">>\n");
			buff.write("<<" + line + ">>\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void close(){
		try {
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
