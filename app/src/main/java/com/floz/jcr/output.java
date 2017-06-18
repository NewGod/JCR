package com.floz.jcr;
import android.os.Handler;

import java.sql.*;
import java.util.List;

import static java.lang.Math.min;

public class output extends Thread {
	Handler handler;
	ChatAdapter chatAdapter;
	private List<ChatEntity> chatList;
	String room;
    String name;
	volatile Thread blinker;
	public void _stop(){
        blinker=null;
    }
	output(ChatAdapter adapter,List<ChatEntity> list,Handler tmp,String room,String name){
        chatAdapter=adapter; chatList=list;handler=tmp;this.room=room;
        this.name=name;
	}
	private void add(final ChatEntity tmp) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				chatList.add(tmp);
				chatAdapter.notifyDataSetChanged();
			}
		});
	}
	@Override
	public void run(){
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Thread thisThread = Thread.currentThread();
		blinker = thisThread;
		List<ChatEntity> lastest_information = null;
		try {
			lastest_information = db_chat.chat_info(room);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int cnt=min(lastest_information.size()-1,9);cnt>=0;cnt--) {
            ChatEntity tmp = lastest_information.get(cnt);
            tmp.setComeMsg(name);
			add(tmp);
        }
		ChatEntity lst = lastest_information.get(0);
		while (blinker==thisThread){
			try {
				lastest_information = db_chat.chat_info(room);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int cnt;
            for (cnt=lastest_information.size()-1;cnt>=0;cnt--) {
                if (lastest_information.get(cnt).equals(lst)) break;
            }
            for (--cnt;cnt>=0;cnt--) {
                ChatEntity tmp = lastest_information.get(cnt);
                tmp.setComeMsg(name);
				add(tmp);
            }
			lst = lastest_information.get(0);
		}
	}
}