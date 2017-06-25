package com.floz.jcr;
import android.os.Handler;

import java.sql.*;
import java.util.List;

import static java.lang.Math.min;

public class output extends Thread {//监控并进行更新聊天信息
	Handler handler;
	ChatAdapter chatAdapter;
	private List<ChatEntity> chatList;
	String room;
    String name;
	volatile Thread blinker;
	public void _stop(){
        blinker=null;
    }//控制其停止
	output(ChatAdapter adapter,List<ChatEntity> list,Handler tmp,String room,String name){
        chatAdapter=adapter; chatList=list;handler=tmp;this.room=room;
		this.name=name;
	}
	//ChatAdapter 通过监控 ChatList 是否产生更新来更新聊天信息的显示
	private void add(final ChatEntity tmp) {//通过handle进行更新
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
		//获取最近10条信息并输出
		try {
			lastest_information = db_chat.chat_info(room);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for(int cnt=min(lastest_information.size()-1,9);cnt>=0;cnt--) {
            ChatEntity tmp = lastest_information.get(cnt);
            tmp.setComeMsg(name);
			add(tmp);
        }
		ChatEntity lst = lastest_information.get(0);
		//监控是否出现新的信息
		while (blinker==thisThread){
			try {
				lastest_information = db_chat.chat_info(room);
			} catch (SQLException e) {
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