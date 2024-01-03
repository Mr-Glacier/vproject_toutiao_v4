package org.example;

import org.example.Controller.*;

public class Main {
    public static void main(String[] args) {

        String projectPath = "E:\\ZKZD2023\\大V项目\\头条数据\\";

//        只存文章列表
        String onlyArticlePath = "1_onlyArticleData\\";
//        只存视频列表
        String videoListpath = "2_videoAndArticleData\\";
//        只存微头条列表
        String littleArticlePath = "3_littleArticleData\\";
//        文章页面数据
        String ArticlePageDataPath = "4_ArticleData\\";
//        微头条页面数据
        String ArticleLittlePath = "5_LittleArticleData\\";

//        文章评论数据存放地址
        String ArticleCommentsPath = "6_ArticleFirstComments\\";


        AnalysisArticlePage_Controller analysisArticlePage_controller = new AnalysisArticlePage_Controller();


      //  analysisArticlePage_controller.Method_1_GetAllPagePath(projectPath,ArticlePageDataPath);


        DownComments_Controller downComments_controller = new DownComments_Controller();

        String GetCommentsMainUrl = "https://www.toutiao.com/article/v2/tab_comments/?aid=24&app_name=toutiao_web";
        String _singer = "_02B4Z6wo00901YEjDPQAAIDBASH2tM-7yYmBBwhAAAUWD8Rlmdj92MwEVuGLWSEE14Y2cfU-Byck79.5fqDUuCrGbUg5oIs63ad2mNRr-KLULAO3e2tGoqc76eFboLsNhbV4bbcxVdhI4gjNb3";
        int Begin = 0;
        int count = 30;

        int GroupNum = 1;

        //downComments_controller.Method_1_FindAllList(projectPath,ArticleCommentsPath,GetCommentsMainUrl,_singer,Begin,count,GroupNum);


//        下载第一层列表
        AnalysisArticleComments_Controller analysisArticleComments_controller = new AnalysisArticleComments_Controller();
        //analysisArticleComments_controller.Method_1_FindAllList(projectPath, ArticleCommentsPath);


//        下载第二层数据
        //DownSecondComments_Controller downSecondComments_controller = new DownSecondComments_Controller();

        String secondCommentsPath = "7_SecondCommentsData\\";

        String SecondMainUrl = "https://www.toutiao.com/2/comment/v2/reply_list/?aid=";
        String Singer = "_02B4Z6wo00901nXypvAAAIDC9fBcs0hI-D511qJAAPgsG2hqFJAhzK0CxPD.YBu81xvNQswY-aIMXgu7eNsmjUwlcMpOx-0jbZsaEzvc7MoEVvdISeEBtE4sdjq6r.9TxXaf3ZCKmNvs7oQz7f";
        //downSecondComments_controller.Method_1_DownSecondeComments(projectPath, secondCommentsPath,SecondMainUrl,Singer);


//        private String mainPath = "E:\\ZKZD2023\\大V项目\\多线程测试\\";
//        private String typePath = "7_SecondCommentsData\\";

        String mainSecondPath = "E:\\ZKZD2023\\大V项目\\多线程测试\\";


        String secondCommentsTypePath = "7_SecondCommentsData\\";

        AnalysisSecondComments_Controller analysisSecondComments_controller = new AnalysisSecondComments_Controller();

        analysisSecondComments_controller.Method_1_GetAllPath(projectPath,secondCommentsTypePath);
    }
}