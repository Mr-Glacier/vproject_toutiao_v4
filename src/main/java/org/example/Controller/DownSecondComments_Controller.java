package org.example.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Dao.Dao_ArticleComments;
import org.example.Entity.T_ArticleComments;
import org.example.Entity.T_ArticleContent;
import org.example.Until.SaveUntil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownSecondComments_Controller {

    public SaveUntil saveUntil = new SaveUntil();
    Dao_ArticleComments dao_articleComments = new Dao_ArticleComments(3);

    public void Method_1_CreatFile(String cratePath) {
        File file_One = new File(cratePath);
        if (!file_One.exists()) {
            file_One.mkdirs();
        }
    }
    public void Method_1_DownSecondeComments(String mainPath, String typePath, String mianurl, String singer) {
        ArrayList<Object> CommentsList = dao_articleComments.MethodFind();
        for (Object Comments : CommentsList) {
            String CommentsID = ((T_ArticleComments) Comments).getCommentsID();
            String State = ((T_ArticleComments) Comments).getDownState();
            String user_name = ((T_ArticleComments) Comments).getAuthor();
            String savePath = mainPath + user_name + "\\" + typePath;

            Method_1_CreatFile(savePath);
            int ID = ((T_ArticleComments) Comments).getID();
            String repost = "0";
            String aid = "24";
            int count = 5;
            int offset = 0;
            String app_name = "toutiao_web";
            if (State.equals("否")) {

                String content = Method_2_RequestSecondComments(mianurl, singer, repost, count, offset, CommentsID, app_name, aid);

                if (!content.equals("Error")) {
                    saveUntil.Method_SaveFile(savePath + CommentsID + "_SecondComments.txt", content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
                    dao_articleComments.Methoc_ChangeDownState(ID, dateFormat.format(date));
                    System.out.println(CommentsID+"下载完成了");
                } else {
                    saveUntil.Method_SaveFile_True(mainPath + user_name + "\\" + "_SecondCommentsError.txt", user_name + "_" + CommentsID + "\n");
                }
            } else {
                System.out.println(user_name + "\t" + CommentsID + "\t" + "评论已经完成下载了");
            }
        }
    }


    //https://www.toutiao.com/2/comment/v2/reply_list/?aid=24&app_name=toutiao_web&id=7298509375772443392&offset=0&count=5&repost=0&_signature=_02B4Z6wo00101FZbfGwAAIDA1lmGLkfeGAhWf3jAAHD-YCIHjdNtSD.YDgQ2AMFwr.4C4f333AVfMH0JYyfc4xGBcJxNloPLofDN6q7Af7Ld80SWNzJ3Jzg4nd2inidNfrXppYOoHF5EU4H7e9
    public String Method_2_RequestSecondComments(String mainurl, String _Singer, String repost, int count, int offset, String commentsId, String app_name, String aid) {
        JSONArray Array = new JSONArray();
        try {
            while (true) {
                String dealSinger = _Singer.substring(0, 14) +
                        RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZabcdefghilmnopqrstuvwxyz") +
                        RandomStringUtils.random(1, "ABCDEFGHILMNOPQRSTUVWXYZ") +
                        RandomStringUtils.random(1, "123456789") +
                        RandomStringUtils.random(2, "ABCDEFGHILMNOPQRSTUVWXYZ1234567abcdefghilmnopqrstuvwxyz")
                        + _Singer.substring(19, _Singer.length() - 2) +
                        RandomStringUtils.random(2, "abcdefghilmnopqrstuvwxyz123456789");

//        https://www.toutiao.com/2/comment/v2/reply_list/?aid=

                String url = mainurl + aid + "&app_name=" + app_name + "&id=" + commentsId + "&offset=" + offset + "&count=" + count + "&repost=" + repost + "&_signature=" + dealSinger;
                Document document = Jsoup.parse(new URL(url).openStream(), "UTf-8", url);
                JSONObject jsonObject = JSON.parseObject(document.text());
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                Array.add(jsonObject1);
                if (jsonObject1.getString("has_more").equals("false")){
                    break;
                }

                offset += count;
            }
            return Array.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }

    }


}
