package org.example.Entity;

public class T_Blogger {

    private  int id;
    private String BloggerName;
    private String Token;
    private String DownState;
    private String DownTime;
    private String Singer;

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getBloggerName() {
        return BloggerName;
    }

    public void setBloggerName(String bloggerName) {
        BloggerName = bloggerName;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDownState() {
        return DownState;
    }

    public void setDownState(String downState) {
        DownState = downState;
    }

    public String getDownTime() {
        return DownTime;
    }

    public void setDownTime(String downTime) {
        DownTime = downTime;
    }
}
