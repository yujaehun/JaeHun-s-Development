/**
 * This is for reading the save files from the source folder.
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package dataLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class SaveFileReader {
	private BufferedReader br;

	public SaveFileReader (String filename) {
		try {

			InputStream fis = new FileInputStream (filename);
			Reader isr = new InputStreamReader (fis);
			this.br = new BufferedReader (isr);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public ArrayList<ArrayList<Long>> read () {
		ArrayList<ArrayList<Long>> arr1 = new ArrayList<ArrayList<Long>>();
		String line = "";
		String csvSplitBy = ",";
		int cnt = 0;
		try {
			while ((line = this.br.readLine()) != null){
				ArrayList<Long> arr2 = new ArrayList<Long>();
				if(cnt == 0) {
					cnt++;
					arr2.add((long)0);
					arr1.add(arr2);
					continue;
				}
				String[] tmp = line.split(csvSplitBy);
				for(String q : tmp){
					arr2.add(Long.parseLong(q));
				}
				arr1.add(arr2);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return arr1;
	}
	public String readPW(){
		try {
			return this.br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void close () {
		try {
			this.br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
