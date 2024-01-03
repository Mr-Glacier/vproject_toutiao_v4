package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.Dao_ArticleComments;
import org.example.Dao.Dao_ArticleContent;
import org.example.Entity.T_ArticleComments;
import org.example.Entity.T_ArticleContent;
import org.example.Until.ReadUntil;
import org.example.Until.SaveUntil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AnalysisArticleComments_Controller {

    public SaveUntil saveUntil = new SaveUntil();
    public ReadUntil readUntil = new ReadUntil();
    Dao_ArticleContent dao_articleContent = new Dao_ArticleContent(2);
    Dao_ArticleComments dao_articleComments = new Dao_ArticleComments(3);


    public void Method_1_FindAllList(String mainPath, String typePath) {
        ArrayList<Object> ArticleList = dao_articleContent.MethodFind();
        for (Object Article : ArticleList) {
            String ArticleId = ((T_ArticleContent) Article).getArticleID();
            String user_name = ((T_ArticleContent) Article).getAuthor();
            String State = ((T_ArticleContent) Article).getDownState();
            String savePath = mainPath + user_name + "\\" + typePath;
            int ID = ((T_ArticleContent) Article).getID();
            if (State.equals("是")) {
                System.out.println(ID);
                String content = readUntil.Method_ReadFile(savePath + ArticleId + "_Comments.txt");
                Method_2_AnalysisConmmentsList(content,ArticleId);
            } else {
                System.out.println("没下载还");
            }

        }


    }


    public void Method_2_AnalysisConmmentsList(String content, String ArticleId) {
        JSONArray jsonArray = JSONArray.parseArray(content);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
            if (jsonArray1.size()==0){
                saveUntil.Method_SaveFile_True("E:\\ZKZD2023\\大V项目\\"+"NoComments.txt", ArticleId+"\n");
            }else {
                for (int j = 0; j < jsonArray1.size(); j++) {
                    JSONObject Item = jsonArray1.getJSONObject(j);
                    String CommentID = Item.getString("id");
                    JSONObject oneComment = Item.getJSONObject("comment");
                    String commentText = oneComment.getString("text");
                    String user_name = oneComment.getString("user_name");
                    String user_id = oneComment.getString("user_id");
                    String publish_loc_info = oneComment.getString("publish_loc_info");
                    Long create_time = Long.valueOf(oneComment.getString("create_time"));
                    String dealtime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(create_time * 1000));
                    String digg_count = oneComment.getString("digg_count");//点赞个数
                    String reply_count = oneComment.getString("reply_count");
                    String platform = oneComment.getString("platform");
                    String has_multi_media = oneComment.getString("has_multi_media");
                    String user_profile_image_url = oneComment.getString("user_profile_image_url");
                    String is_loyal_fan = oneComment.getString("is_loyal_fan");
                    String has_author_digg = oneComment.getString("has_author_digg");
                    String bury_count = oneComment.getString("bury_count");

                    T_ArticleComments t_articleComments = new T_ArticleComments();
                    t_articleComments.setArticleID(ArticleId);
                    t_articleComments.setCommenterID(user_id);
                    t_articleComments.setFatherCommentsID("0");
                    t_articleComments.setCommenterName(user_name.replace('\'','"'));
                    t_articleComments.setCommentsID(CommentID);
                    t_articleComments.setCommentsText(commentText.replace('\'','"'));
                    t_articleComments.setPushLocation(publish_loc_info.replace('\'','"'));
                    t_articleComments.setCreateTime(dealtime.replace('\'','"'));
                    t_articleComments.setDiggCount(digg_count);
                    t_articleComments.setReplyCount(reply_count);
                    t_articleComments.setHas_author_digg(has_author_digg);
                    t_articleComments.setHas_multi_media(has_multi_media);
                    t_articleComments.setIs_loyal_fan(is_loyal_fan.replace('\'','"'));
                    t_articleComments.setUser_profile_image_url(user_profile_image_url.replace('\'','"'));
                    t_articleComments.setC_Platform(platform);
                    t_articleComments.setBury_count(bury_count);
                    dao_articleComments.MethodInsert(t_articleComments);
                }
            }

        }
    }
}
