package org.example.Entity;


public class T_ArticleContent {
    private  int ID;
    private String ArticleID;
    private String Author;

    private String ArticleTitle;

    private String ContentList;
    private String ImgList;
    private String ArticlePushTime;
    private String DownState ;
    private String DownTime;

    public int getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        GroupNumber = groupNumber;
    }

    private int GroupNumber;


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

    public String getArticlePushTime() {
        return ArticlePushTime;
    }

    public void setArticlePushTime(String articlePushTime) {
        ArticlePushTime = articlePushTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getArticleID() {
        return ArticleID;
    }

    public void setArticleID(String articleID) {
        ArticleID = articleID;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }

    public String getContentList() {
        return ContentList;
    }

    public void setContentList(String contentList) {
        ContentList = contentList;
    }

    public String getImgList() {
        return ImgList;
    }

    public void setImgList(String imgList) {
        ImgList = imgList;
    }
}
