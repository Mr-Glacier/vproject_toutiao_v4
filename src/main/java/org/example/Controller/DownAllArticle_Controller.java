package org.example.Controller;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.example.Dao.Dao_article;
import org.example.Entity.T_Article;
import org.example.Until.SaveUntil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DownAllArticle_Controller {
    public SaveUntil saveUntil = new SaveUntil();
    Dao_article dao_article = new Dao_article(1);
    //    批量下载文章的方法
    public void Method_1_DownOnlyArticle(String mainPath, String typePath,int GroupNumber) {
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
                String Content = Method_2_PlayDownArticlePage(url);
                System.out.println(Username + " 本次下载 : " + ArticleId);
                if (Content.equals("false")) {
                    saveUntil.Method_SaveFile_True(mainPath + Username + "\\ArticlePageError.txt", Username + "_" + ArticleId+"\n");
                } else {
                    Method_SaveFile(mainPath + Username + "\\" + typePath, ArticleId + ".txt", Content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
                    dao_article.Methoc_ChangeDownState(ID, dateFormat.format(date));
                }
            } else {
                System.out.println("已经下载 :" + ArticleId);
            }

        }
    }

    //    下载文章页面方法
    public String Method_2_PlayDownArticlePage(String url) {
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
}
