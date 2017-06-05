package com.floz.jcr;
import android.os.Handler;
import android.widget.EditText;

import java.sql.*;

public class output implements Runnable {
	Handler handler;
	EditText output;
	String room;
	output(EditText out,Handler tmp,String Name){output=out; handler=tmp;room=Name;}
	private void print(final String s) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				output.append(s+'\n');
			}
		});
	}
	public void run(){
		String lastest_information[] = null;
		try {
			lastest_information = db_chat.chat_info(room);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int cnt=9;cnt>=0;cnt-- ) 
			if (lastest_information[cnt]!=null) {
				print(lastest_information[cnt]);
            }
		String lst = new String(lastest_information[0]);
		while (true){
			try {
				lastest_information = db_chat.chat_info(room);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int cnt=0;
			for (cnt=9;cnt>=0;cnt--) 
				if (lastest_information[cnt]!=null&&lastest_information[cnt].equals(lst)) break;
			for(--cnt;cnt>=0;cnt--) 
				if (lastest_information[cnt]!=null) {
                    print(lastest_information[cnt]);
                }
			lst = new String(lastest_information[0]);

		}
	}
}