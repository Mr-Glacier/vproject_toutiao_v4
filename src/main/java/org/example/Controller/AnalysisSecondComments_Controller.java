package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.Dao_ArticleComments;
import org.example.Dao.Dao_blogger;
import org.example.Entity.T_ArticleComments;
import org.example.Entity.T_Blogger;
import org.example.Until.ReadUntil;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//用于解析评论 信息 二级
public class AnalysisSecondComments_Controller {


    Dao_blogger dao_blogger = new Dao_blogger(0);
    Dao_ArticleComments dao_articleComments = new Dao_ArticleComments(3);

    public ReadUntil readUntil = new ReadUntil();


    public void Method_1_GetAllPath(String projectPath, String typePath) {
        ArrayList<Object> UserLsit = dao_blogger.MethodFind();

        for (Object user : UserLsit) {
            String userName = ((T_Blogger) user).getBloggerName();

            String Path = projectPath + userName + "\\" + typePath;

            System.out.println(Path);
            List<String> FileList = readUntil.getFileNames(Path);

            for (String fileName:FileList){

                String content = readUntil.Method_ReadFile(Path+fileName);
                Method_2_AnalysisOneJSON(content,userName,fileName);
            }

        }
    }


    public void Method_2_AnalysisOneJSON(String content,String Author,String fileName){
        JSONArray jsonArray = JSONArray.parseArray(content);
        if (null != jsonArray){
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                JSONArray item = jsonObject.getJSONArray("data");

                if (item!=null){
                    String ArticleId = jsonObject.getString("group_id");
                    String FatherID = jsonObject.getString("id");

//            此批回复中包含文章ID 以及所属于父类的ID  晚点做解析
                    for (int j = 0; j < item.size(); j++) {

                        JSONObject oneComment = item.getJSONObject(j);

                        String CommentID = oneComment.getString("id");
                        String commentText = oneComment.getString("text");
                        long create_time = Long.parseLong(oneComment.getString("create_time"));
                        String dealtime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(create_time * 1000));
                        String publish_loc_info = oneComment.getString("publish_loc_info");
                        String digg_count = oneComment.getString("digg_count");//点赞个数
                        String bury_count = oneComment.getString("bury_count");
                        String has_author_digg = oneComment.getString("has_author_digg");
                        String has_multi_media = oneComment.getString("has_multi_media");

                        JSONObject userInfo = oneComment.getJSONObject("user");

                        String user_name = userInfo.getString("name");
                        String user_id = userInfo.getString("user_id");
                        String user_profile_image_url = userInfo.getString("avatar_url");

                        JSONObject reply_to_comment = oneComment.getJSONObject("reply_to_comment");

                        String ReplyForID = "0000";
                        if (null!=reply_to_comment){
                            ReplyForID = reply_to_comment.getString("id");
                        }

                        T_ArticleComments t_articleComments = new T_ArticleComments();
                        t_articleComments.setArticleID(ArticleId);
                        t_articleComments.setCommenterID(user_id);
                        t_articleComments.setFatherCommentsID(FatherID);

                        t_articleComments.setCommenterName(user_name.replace('\'','"'));
                        t_articleComments.setCommentsID(CommentID);
                        t_articleComments.setCommentsText(commentText.replace('\'','"'));
                        t_articleComments.setPushLocation(publish_loc_info.replace('\'','"'));
                        t_articleComments.setCreateTime(dealtime.replace('\'','"'));
                        t_articleComments.setDiggCount(digg_count);
//                t_articleComments.setReplyCount(reply_count);
                        t_articleComments.setHas_author_digg(has_author_digg);
                        t_articleComments.setHas_multi_media(has_multi_media);
//                t_articleComments.setIs_loyal_fan(is_loyal_fan.replace('\'','"'));
                        t_articleComments.setUser_profile_image_url(user_profile_image_url.replace('\'','"'));
//                t_articleComments.setC_Platform(platform);
                        t_articleComments.setBury_count(bury_count);
                        t_articleComments.setAuthor(Author);

                        t_articleComments.setReplyForID(ReplyForID);

                        t_articleComments.setCommentsGrade("second");
                        //t_articleComments.setGroupNumber(999);


                        //System.out.println(t_articleComments.toString());
                        dao_articleComments.MethodInsert(t_articleComments);

                        System.out.println(fileName+"\t"+CommentID+"   ->完成入库");
//                String reply_count = oneComment.getString("reply_count");
//                String platform = oneComment.getString("platform");
//                String is_loyal_fan = oneComment.getString("is_loyal_fan");\

                    }
                }
            }
        }
    }
}
