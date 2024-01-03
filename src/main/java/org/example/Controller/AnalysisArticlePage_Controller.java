package org.example.Controller;

import org.example.Dao.Dao_ArticleContent;
import org.example.Dao.Dao_blogger;
import org.example.Entity.T_ArticleContent;
import org.example.Entity.T_Blogger;
import org.example.Until.ReadUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

// 解析文章页面并入库 控制类
public class AnalysisArticlePage_Controller {
    public ReadUntil readUntil = new ReadUntil();

    Dao_blogger dao_blogger = new Dao_blogger(0);
    Dao_ArticleContent dao_articleContent = new Dao_ArticleContent(2);


    public void Method_1_GetAllPagePath(String projectPath,String typePath){
        ArrayList<Object> users = dao_blogger.MethodFind();
        long number = 0;
        for (Object user:users) {
            String user_name = ((T_Blogger) user).getBloggerName();
            String SavePsth = projectPath + user_name + "\\" + typePath;
            System.out.println(user_name);
            List<String> fileList = readUntil.getFileNames(SavePsth);

            for (String filename:fileList){
                String ArticleID = filename.replace(".txt", "");
                String path = SavePsth +filename;
                System.out.println(ArticleID);
                Method_2_InsertArticleContent(user_name,ArticleID,path);
                number += 1;
            }
        }

        System.out.println(number);
    }

    public void Method_2_InsertArticleContent(String userName,String ArticleID,String path){
        String content = readUntil.Method_ReadFile(path);
        Document document = Jsoup.parse(content);

        Elements Item = document.select("article");

        String Title= document.select("h1").text();
        System.out.println(Title);

        String time = document.select(".article-meta").select("span").text();
        System.out.println(time);

        StringBuilder Sb = new StringBuilder();

        Elements ContentItem = Item.select("p");

        for (Element t :ContentItem){
            String linetext = t.text();
            //System.out.println(linetext);
            Sb.append(linetext);
        }

        //System.out.println(Sb.toString());

        Elements imagItems = Item.select("img");
        StringBuilder SBimg = new StringBuilder();
        for (Element img: imagItems){
            String imageurl = img.attr("src");
            SBimg.append(imageurl).append(",");
        }

        T_ArticleContent t_articleContent = new T_ArticleContent();
        t_articleContent.setArticleID(ArticleID);
        t_articleContent.setArticleTitle(Title);
        t_articleContent.setAuthor(userName);
        t_articleContent.setContentList(Sb.toString().replace('\'', '"'));
        t_articleContent.setImgList(SBimg.toString());
        t_articleContent.setArticlePushTime(time);
        t_articleContent.setDownState("否");
        t_articleContent.setDownTime("00");
        dao_articleContent.MethodInsert(t_articleContent);
    }
}
