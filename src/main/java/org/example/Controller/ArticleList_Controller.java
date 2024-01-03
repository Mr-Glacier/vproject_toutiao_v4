package org.example.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Dao.Dao_ArticleContent;
import org.example.Dao.Dao_article;
import org.example.Dao.Dao_blogger;
import org.example.Entity.T_Article;
import org.example.Entity.T_ArticleContent;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//获取文章列表的控制类
public class ArticleList_Controller {

    public SaveUntil saveUntil = new SaveUntil();
    public ReadUntil readUntil = new ReadUntil();
    Dao_blogger dao_blogger = new Dao_blogger(0);
    Dao_article dao_article = new Dao_article(1);

    // 1.创建相关文件夹
    public void Method_1_CreatFile(String cratePath) {
        File file_One = new File(cratePath);
        if (!file_One.exists()) {
            file_One.mkdirs();
        }
    }


    //     2.读取需要爬取的用户名单
    public ArrayList<Object> Method_2_getUserList() {
        ArrayList<Object> List = dao_blogger.MethodFind();
        return List;
    }

    //3.下载保存用户的列表数据,后续根据列表数据下载文章页面
    public void Method_3_DownListData(String DownType, String mainUrl, String BeginLocation, String BloggerToken, String Singer, String aid, String app_name, String savePath) {
        while (true) {
//          每次请求的singer都会改变
            String dealSinger = Singer.substring(0, 14) +
                    RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZabcdefghilmnopqrstuvwxyz") +
                    RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZ") +
                    RandomStringUtils.random(1, "123456789") +
                    RandomStringUtils.random(2, "ABCDEFGHILMNOPQRSTUVWXYZ1234567abcdefghilmnopqrstuvwxyz")
                    + Singer.substring(19, Singer.length() - 2) +
                    RandomStringUtils.random(2, "abcdefghilmnopqrstuvwxyz123456789");

            String url = mainUrl + "category=" + DownType + "&token=" + BloggerToken + "&max_behot_time=" + BeginLocation + "&aid=" + aid + "&app_name" + app_name + "&_signature" + dealSinger;
            System.out.println(url);
            System.out.println(BeginLocation);
            BeginLocation = Method_4_RequestIterate(url, savePath, BeginLocation);
            if (BeginLocation.equals("false")) {
                break;
            }
        }


    }

    //    这个类用于迭代发送请求,保存文件,进行逻辑判断
    public String Method_4_RequestIterate(String url, String savePath, String saveFileName) {
        String content = Methdo_publicPlayWright(url);
        JSONObject Item = JSONObject.parseObject(content);

        return null;
    }

    public String Methdo_publicPlayWright(String url) {
        try (Playwright playwright = Playwright.create()) {
            //默认为无头浏览器方式启动
            Browser browser = playwright.chromium().launch();
            //参数设定方式启动
            //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .setViewportSize(2880, 1800));
            Page page = browser.newPage();

            //main main minn main main main mian main main main main main main main main main main main main main main main main main main main main

            //Map<String, String> headers = new HashMap<>();
            //headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            // headers.put("Cookie", "UOR=www.google.com,open.weibo.com,www.google.com; SINAGLOBAL=5902951706312.0625.1699351640532; login_sid_t=96984edd9bcae6476b00b99523dd8ff0; cross_origin_proto=SSL; _s_tentry=www.google.com; Apache=9876801802031.592.1699931729435; ULV=1699931729438:2:2:2:9876801802031.592.1699931729435:1699351640537; XSRF-TOKEN=KnimrY9cYdizTcbDACx7SAYg; SUB=_2A25IV_WiDeRhGeFP4lMS8inIyTWIHXVrLXdqrDV8PUNbmtAGLWvxkW9NQOu2NijDj6O63fOwcH9B1uM1LVAMwiAS; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFN.gi_v6ONR.BSVopsHs235JpX5KzhUgL.FoMp1K20eoMXeo.2dJLoIEBLxK-LB-qL1hBLxKqL1hBL12qLxKqLBKzLBKMLxKqLBK2L1K5t; ALF=1731508594; SSOLoginState=1699972594; WBPSESS=_X_Um1n8CY35FhNEaqmYqIsE2Uy38QGyJR3wt1Pt88MLuComcw1pwhnABVWBDV0RTErjqw50FDm5KdL8UavlpNnv9gG6ayvc_WUSesTQhw6U91Ynb4MPsFokFws-zkrZaMpgcfl1otCW0RX91EGYDA==");
            //page.setExtraHTTPHeaders(headers);
            // 启用 JavaScript

            // 访问指定的URL
            page.navigate(url);

            // 等待页面加载完成
            page.waitForLoadState(LoadState.LOAD);

            // 获取页面源码
            String pageSource = page.content();
            // 打印页面源码或进行其他操作
            //System.out.println(pageSource);

            Thread.sleep(1000);
            List<ElementHandle> lis = page.querySelectorAll("pre");


            StringBuilder sb = new StringBuilder();
            for (ElementHandle li : lis) {
                sb.append(li.innerHTML());
            }
            //System.out.println(sb.toString());

            // 关闭浏览器
//            Thread.sleep(100000);
            browser.close();

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }


    //    批量下载文章的方法
    public void Method_5_DownOnlyArticle(String mainPath, String typePath,int GroupNumber) {
        ArrayList<Object> ArticleList = dao_article.MethodFind(GroupNumber);
        for (Object Article : ArticleList) {
            int ID = ((T_Article) Article).getid();
            String ArticleId = ((T_Article) Article).getArticle_id();
            String ArticleUrl = ((T_Article) Article).getArticle_url().replace("group", "article");
            String Username = ((T_Article) Article).getC_user_name();

            String url = "https://www.toutiao.com/article/"+ArticleId+"/";
            System.out.println(url);

            String State = ((T_Article) Article).getDownState();
            if (State.equals("否")) {
                String Content = Method_6_PlayDownArticlePage(url);
                System.out.println(Username + " 本次下载 : " + ArticleId);
                if (Content.equals("false")) {
                    saveUntil.Method_SaveFile_True(mainPath + Username + "\\ArticlePageError.txt", Username + "_" + ArticleId+"\n");
                } else {
                    Method_SaveFile(mainPath + Username + "\\" + typePath, ArticleId + ".txt", Content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
//                System.out.println(dateFormat.format(date));
                    dao_article.Methoc_ChangeDownState(ID, dateFormat.format(date));
                }
            } else {
                System.out.println("已经下载 :" + ArticleId);
            }

        }
    }

    //    下载文章页面方法
    public String Method_6_PlayDownArticlePage(String url) {
        try (Playwright playwright = Playwright.create()) {
            //默认为无头浏览器方式启动
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            //参数设定方式启动
            //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

            browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"));
//            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
//            .setViewportSize(2880, 1800)
            Page page = browser.newPage();

            // Map<String, String> headers = new HashMap<>();
            // headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            // headers.put("Sec-Ch-Ua", "\"Google Chrome\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"");
            // page.setExtraHTTPHeaders(headers);
            // 启用 JavaScript

            // 访问指定的URL
            page.navigate(url);

            // 等待页面加载完成
            page.waitForLoadState(LoadState.LOAD);


            // 获取页面源码
            //String pageSource = page.content();
            // 打印页面源码或进行其他操作
            //System.out.println(pageSource);
            page.waitForSelector(".article-content");
            Thread.sleep(1000);
            List<ElementHandle> lis = page.querySelectorAll(".article-content");
            StringBuilder sb = new StringBuilder();
            for (ElementHandle li : lis) {
                sb.append(li.innerHTML());
            }

            // 关闭浏览器
            //Thread.sleep(5000);
            browser.close();
            //System.out.println(sb.toString());
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
    }

    //    保存文件方法
    public void Method_SaveFile(String path, String fileName, String content) {
        saveUntil.Method_SaveFile(path + fileName, content);
    }


//    7.解析插入数据库
//    7.1,循环遍历读取所有文件夹
//    7.2,获取该文件夹下的所有文件名称
//    7.3,解析单个文件入库
    Dao_ArticleContent dao_articleContent = new Dao_ArticleContent(2);
    public void Method_7_1_InsertArticleContent(String userName,String ArticleID,String path){
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

//    获取文件路径下的所有名称
    public List<String> Method_7_2_MethodGetAllFileName(String projectPath, String userName, String typePath){
        String path = projectPath + userName+ "\\"+typePath;
        List<String> NameList =readUntil.getFileNames(path);
        return NameList;
    }






//    下载评论数据文件
//    8.1读取所有文章列表的文章id
//    8.2对评论文章json进行下载 保存

    public void Method_8_1_MethodGetAllComents(String mainPath,String typePath,String mainUrl,String singer,int begin ,int count){
        ArrayList<Object> ArticleList = dao_articleContent.MethodFind();
        for (Object Article:ArticleList){
            String ArticleId= ((T_ArticleContent)Article).getArticleID();
            String user_name = ((T_ArticleContent)Article).getAuthor();
            String State = ((T_ArticleContent)Article).getDownState();
            String savePath = mainPath+user_name+"\\"+typePath;
            int ID = ((T_ArticleContent)Article).getID();
            if (State.equals("否")){
                String content =  Method_8_2_RequestComments(savePath,mainUrl,ArticleId, singer, begin, count);
                if (!content.equals("false")){
                    saveUntil.Method_SaveFile(savePath+ArticleId+".txt",content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
                    dao_articleContent.Methoc_ChangeDownState(ID, dateFormat.format(date));
                }else {
                    saveUntil.Method_SaveFile_True(mainPath+user_name+"\\"+"CommentsError.txt", user_name+"_"+ArticleId+"\n");
                }
            }else {
                System.out.println(user_name+"\t"+ArticleId+"\t"+"评论已经完成下载了");
            }

        }
    }

    public  String Method_8_2_RequestComments(String path,String mainurl,String group_id,String _Singer,int begin ,int count){
        try {
            int nowLocation = begin;
            int Size = 0;

            JSONArray AllArry = new JSONArray();
            while (true){

//                对请求的参数处理一下
                String dealSinger = _Singer.substring(0, 14) +
                        RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZabcdefghilmnopqrstuvwxyz") +
                        RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZ") +
                        RandomStringUtils.random(1, "123456789") +
                        RandomStringUtils.random(2, "ABCDEFGHILMNOPQRSTUVWXYZ1234567abcdefghilmnopqrstuvwxyz")
                        + _Singer.substring(19, _Singer.length() - 2) +
                        RandomStringUtils.random(2, "abcdefghilmnopqrstuvwxyz123456789");

                String url = mainurl+"&offset="+nowLocation+"&count="+count+"&group_id="+group_id+"&item_id="+group_id+"&_signature="+dealSinger;
                System.out.println(url);
                Document document = Jsoup.parse(new URL(url).openStream(), "UTf-8", url);
                JSONObject jsonObject = JSON.parseObject(document.text());

                JSONArray jsonArray = jsonObject.getJSONArray("data");

                AllArry.add(jsonArray);

                Size +=jsonArray.size();
                if (jsonArray.size()<count){
                    break;
                }
                nowLocation += count;
            }
            System.out.println("数据共有 : "+Size);
            return AllArry.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "false";
        }
    }




}
