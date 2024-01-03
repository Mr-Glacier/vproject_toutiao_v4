package org.example.Controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Dao.Dao_blogger;
import org.example.Entity.T_Blogger;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;

import java.util.ArrayList;
import java.util.List;

//控制索引列表的下载  控制类
public class IndexListDown_Controller {

    public SaveUntil saveUntil = new SaveUntil();
    public ReadUntil readUntil = new ReadUntil();

//    共用保存文件方法
    public void Method_SaveFile(String savePath,String fileName,String content){
        saveUntil.Method_SaveFile(savePath+fileName, content);
    }

    //    读取blogger表 下载索引...
    Dao_blogger dao_blogger = new Dao_blogger(0);

    //    1.获取所需爬取的对象信息
    public ArrayList<Object> Method_1_GetBloggerList() {
        return dao_blogger.MethodFind();
    }

//    1.全部用户下载  传参,项目地址,文件类型保存地址
    public void Method_1_DownAllIndexData(String projectPath,String typePath,String DataType,String mainUrl,String aid ,String app_name){
        ArrayList<Object> users = Method_1_GetBloggerList();
        for (Object user:users){
            String user_name = ((T_Blogger) user).getBloggerName();
            String user_token = ((T_Blogger) user).getToken();
            String user_singer = ((T_Blogger) user).getSinger();
            String DownState = ((T_Blogger) user).getDownState();
            String Begin = "0";
            String SavePsth = projectPath + user_name + "\\"+typePath;

            if (DownState.equals("否")){
                Method_2_DownListData(DataType,mainUrl,Begin,user_token,user_singer,aid,app_name,SavePsth);
            }else {
                System.out.println(user_name+"  已经下载完成");
            }
        }
    }


    //2.下载保存用户的列表数据,后续根据列表数据下载文章页面
    public void Method_2_DownListData(String DownType, String mainUrl, String BeginCode, String BloggerToken, String Singer, String aid, String app_name, String savePath) {
        while (true) {
//          每次请求的singer都会改变
            String dealSinger = Singer.substring(0, 14) +
                    RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZabcdefghilmnopqrstuvwxyz") +
                    RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZ") +
                    RandomStringUtils.random(1, "123456789") +
                    RandomStringUtils.random(2, "ABCDEFGHILMNOPQRSTUVWXYZ1234567abcdefghilmnopqrstuvwxyz")
                    + Singer.substring(19, Singer.length() - 2) +
                    RandomStringUtils.random(2, "abcdefghilmnopqrstuvwxyz123456789");

            String url = mainUrl + "category=" + DownType + "&token=" + BloggerToken + "&max_behot_time=" + BeginCode + "&aid=" + aid + "&app_name" + app_name + "&_signature" + dealSinger;
            System.out.println(url);
            System.out.println(BeginCode);
            BeginCode = Method_3_JudgmentHasMore(url, savePath, BeginCode);
            if (BeginCode.equals("false")) {
                break;
            }
        }
    }

    //    2.进行是否具有下一集的判断,并进行保存
    public String Method_3_JudgmentHasMore(String url, String path, String nextCode) {
        String content = Method_4_RequestIterate(url);
        JSONObject Item1 = JSON.parseObject(content);
        Method_SaveFile(path, nextCode + ".txt", content);
        nextCode = Item1.getJSONObject("next").getString("max_behot_time");
        String hasMore = Item1.getString("has_more");
        if (hasMore.equals("true")) {
            return nextCode;
        } else{
            nextCode = "false";
            return nextCode;
        }
    }


    //    3.进行单次的请求,并返回JSON文本数据
    public String Method_4_RequestIterate(String url) {
        try (Playwright playwright = Playwright.create()) {
            //默认为无头浏览器方式启动
            Browser browser = playwright.chromium().launch();
            //参数设定方式启动
            //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser.newContext(new Browser.NewContextOptions()
                    .setIgnoreHTTPSErrors(true)
                    .setJavaScriptEnabled(true)
                    .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
                    .setViewportSize(2880, 1800));
            Page page = browser.newPage();

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
}
