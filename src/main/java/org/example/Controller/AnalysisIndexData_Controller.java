package org.example.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.Dao.Dao_article;
import org.example.Dao.Dao_blogger;
import org.example.Entity.T_Article;
import org.example.Entity.T_Blogger;
import org.example.Until.ReadUntil;

import java.util.ArrayList;
import java.util.List;

//
public class AnalysisIndexData_Controller {
    public ReadUntil readUntil = new ReadUntil();
    Dao_blogger dao_blogger = new Dao_blogger(0);
    Dao_article dao_article = new Dao_article(1);


    //    1.获取全部列表
    public ArrayList<Object> Method_GetBloggerList() {
        return dao_blogger.MethodFind();
    }

    //    2.获取文件夹下名称
    public void Method_1_InsertDataListToDB(String projectPath, String typePath) {
        ArrayList<Object> userList = Method_GetBloggerList();
        for (Object user : userList) {
            String user_name = ((T_Blogger) user).getBloggerName();

            String SavePsth = projectPath + user_name + "\\" + typePath;

            List<String> FileList = readUntil.getFileNames(SavePsth);
            for (String fileName : FileList) {
                Method_2_analysisArticle(SavePsth, fileName);
            }

        }
    }

    //    3.进行解析入库
    public void Method_2_analysisArticle(String path, String fileName) {
        String content = readUntil.Method_ReadFile(path + fileName);
        JSONObject jsonObject = JSONObject.parseObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        String State = jsonObject.getString("message");
        if (jsonArray.size() != 0 && !State.equals("error")) {
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject onedata = jsonArray.getJSONObject(i);
                String article_id = onedata.getString("id");
                String article_group_id = onedata.getString("group_id");
                String article_title = onedata.getString("title");//标题
                String article_abstract = onedata.getString("abstract");
                String article_url = onedata.getString("article_url");
                String article_type = onedata.getString("article_type");
                String article_sub_type = onedata.getString("article_sub_type");
                String article_version = onedata.getString("article_version");

                String ban_comment = onedata.getString("ban_comment");
                String behot_time = onedata.getString("behot_time");
                String bury_style_show = onedata.getString("bury_style_show");
                String comment_count = onedata.getString("comment_count");
                String common_raw_data = onedata.getString("common_raw_data");
                String cell_flag = onedata.getString("cell_flag");
                String cell_layout_style = onedata.getString("cell_layout_style");
                String cell_type = onedata.getString("cell_type");
                String group_flags = onedata.getString("group_flags");
                String group_source = onedata.getString("group_source");
                String group_type = onedata.getString("group_type");
                String hot = onedata.getString("hot");
                String has_image = onedata.getString("has_image");
                String has_video = onedata.getString("has_video");
                String like_count = onedata.getString("like_count");
                String publish_time = onedata.getString("publish_time");
                String read_count = onedata.getString("read_count");
                String share_url = onedata.getString("share_url");
                String C_source = onedata.getString("source");
                String tag = onedata.getString("tag");
                String tag_id = onedata.getString("tag_id");
                String bury_count = onedata.getString("bury_count");

                JSONObject UserInfo = onedata.getJSONObject("user_info");
                String user_id = UserInfo.getString("user_id");
                String user_avatar_url = UserInfo.getString("avatar_url");
                String user_description = UserInfo.getString("description"); //简介
                String user_verified_content = UserInfo.getString("verified_content"); //认证
                String user_name = UserInfo.getString("name"); //用户名称

                T_Article t_article = new T_Article();
                t_article.setArticle_id(article_id);
                t_article.setC_user_name(user_name);
                t_article.setC_user_id(user_id);
                t_article.setC_user_avatar_url(user_avatar_url);
                t_article.setC_user_description(user_description);
                t_article.setC_user_verified_content(user_verified_content);
                t_article.setArticle_id(article_id);
                t_article.setArticle_group_id(article_group_id);
                t_article.setArticle_title(article_title);
                t_article.setArticle_abstract(article_abstract);
                t_article.setArticle_url(article_url);
                t_article.setArticle_type(article_type);
                t_article.setArticle_sub_type(article_sub_type);
                t_article.setArticle_version(article_version);
                t_article.setBan_comment(ban_comment);
                t_article.setBehot_time(behot_time);
                t_article.setBury_count(bury_count);
                t_article.setBury_style_show(bury_style_show);
                t_article.setComment_count(comment_count);
                t_article.setCommon_raw_data(common_raw_data);
                t_article.setCell_flag(cell_flag);
                t_article.setCell_layout_style(cell_layout_style);
                t_article.setCell_type(cell_type);
                t_article.setGroup_flags(group_flags);
                t_article.setGroup_source(group_source);
                t_article.setGroup_type(group_type);
                t_article.setHot(hot);
                t_article.setHas_image(has_image);
                t_article.setHas_video(has_video);
                t_article.setLike_count(like_count);
                t_article.setPublish_time(publish_time);
                t_article.setRead_count(read_count);
                t_article.setShare_url(share_url);
                t_article.setC_source(C_source);
                t_article.setTag(tag);
                t_article.setTag_id(tag_id);

                t_article.setDownState("否");
                t_article.setDownTime("00");

                t_article.setFilepathnum(fileName);
                dao_article.MethodInsert(t_article);
            }
        }
    }

}


