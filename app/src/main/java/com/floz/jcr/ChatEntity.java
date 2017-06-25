package com.floz.jcr;

public class ChatEntity {//聊天信息内容

    private String user;
    private String content;
    private String chatTime;
    private boolean isComeMsg;
    public boolean equals(ChatEntity tmp) {
        return tmp.content.equals(this.content)&&tmp.chatTime.equals(this.chatTime);
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getChatTime() {
        return chatTime;
    }
    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
    public boolean isComeMsg() {
        return isComeMsg;
    }
    public void setComeMsg(String now) {
        this.isComeMsg = !now.equals(user);
    }

}