package org.example.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Dao.Dao_ArticleContent;
import org.example.Entity.T_ArticleContent;
import org.example.Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//下载评论控制器
public class DownComments_Controller {
    public SaveUntil saveUntil = new SaveUntil();

    Dao_ArticleContent dao_articleContent = new Dao_ArticleContent(2);

    public void Method_1_FindAllList(String mainPath, String typePath, String mainUrl, String singer, int begin, int count,int GroupNumber) {
        ArrayList<Object> ArticleList = dao_articleContent.MethodFind(GroupNumber);
        for (Object Article : ArticleList) {
            String ArticleId = ((T_ArticleContent) Article).getArticleID();
            String user_name = ((T_ArticleContent) Article).getAuthor();
            String State = ((T_ArticleContent) Article).getDownState();
            String savePath = mainPath + user_name + "\\" + typePath;
            int ID = ((T_ArticleContent) Article).getID();
            if (State.equals("否")) {
                System.out.println(ID);
                String content = Method_2_RequestComments(mainUrl, ArticleId, singer, begin, count);
                if (!content.equals("false")) {
                    saveUntil.Method_SaveFile(savePath + ArticleId + "_Comments.txt", content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
                    dao_articleContent.Methoc_ChangeDownState(ID, dateFormat.format(date));
                } else {
                    saveUntil.Method_SaveFile_True(mainPath + user_name + "\\" + "CommentsError.txt", user_name + "_" + ArticleId + "\n");
                }
            } else {
                System.out.println(user_name + "\t" + ArticleId + "\t" + "评论已经完成下载了");
            }

        }


    }

    public  String Method_2_RequestComments(String mainurl,String group_id,String _Singer,int begin ,int count){
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
                //System.out.println(url);
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