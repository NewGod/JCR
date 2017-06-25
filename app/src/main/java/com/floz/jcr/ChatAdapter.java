package com.floz.jcr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;

public class ChatAdapter extends BaseAdapter {//聊天信息的Adapter
    private Context context = null;
    private List<ChatEntity> chatList = null;
    private LayoutInflater inflater = null;
    private int COME_MSG = 0;
    private int TO_MSG = 1;

    public ChatAdapter(Context context,List<ChatEntity> chatList){
        this.context = context;
        this.chatList = chatList;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
        ChatEntity entity = chatList.get(position);
        if (entity.isComeMsg())
        {
            return COME_MSG;
        }else{
            return TO_MSG;
        }
    }

    @Override
    public int getViewTypeCount() {
        // 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
        return 2;
    }
    private int getResource(int imageName){
        Class drawable  =  R.drawable.class;
        Field field;
        int r_id=0;
        try {
            field = drawable.getField('a'+Integer.toString(imageName));
            r_id = field.getInt(field.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r_id;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatHolder chatHolder ;
        if (convertView == null) {//第一次加载
            chatHolder = new ChatHolder();
            if (chatList.get(position).isComeMsg()) {
                convertView = inflater.inflate(R.layout.chat_from_item, null);
            }else {
                convertView = inflater.inflate(R.layout.chat_to_item, null);
            }
            chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
            chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
            chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
            chatHolder.userTextView = (TextView) convertView.findViewById(R.id.iv_user_name);
            convertView.setTag(chatHolder);
        }else {
            chatHolder = (ChatHolder)convertView.getTag();
        }

        chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
        chatHolder.contentTextView.setText(chatList.get(position).getContent());
        chatHolder.userTextView.setText(chatList.get(position).getUser());
        if (!chatList.get(position).getUser().equals("root")) //根据名称hash值获得相应头像
            chatHolder.userImageView.setImageResource(getResource((chatList.get(position).getUser().hashCode()%21+21)%21));
        return convertView;
    }

    private class ChatHolder{
        private TextView timeTextView;
        private ImageView userImageView;
        private TextView contentTextView;
        private TextView userTextView;
    }

}
