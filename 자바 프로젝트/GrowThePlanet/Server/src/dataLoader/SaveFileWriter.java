/**
 * This is the class to write a save data for the users on the server source folder.
 * 
 * @author 2016311221 YoungHoon Lim & 2016313746 JaeHun Yu
 * 
 */
package dataLoader;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class SaveFileWriter {
	BufferedWriter bw;

	public SaveFileWriter (String filename) {
		try {
			OutputStream ios = new FileOutputStream(filename);
			Writer osw = new OutputStreamWriter(ios);
			this.bw = new BufferedWriter(osw); 

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void write (ArrayList<ArrayList<?>> save) {
		for(ArrayList<?> arr : save){
			int i = 0;
			int k =arr.size();
			while(i<k-1){
				try{
					this.bw.write(arr.get(i).toString()+",");
				} catch(IOException e){
					e.printStackTrace();
				}
				i++;
			}
			while(i==k-1){
				try{
					this.bw.write(arr.get(i).toString());
				} catch(IOException e){
					e.printStackTrace();
				}
				i++;
			}
			try {
				this.bw.write("\n");
				this.bw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
