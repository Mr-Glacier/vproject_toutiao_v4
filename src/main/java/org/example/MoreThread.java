package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.Dao.Dao_ArticleComments;
import org.example.Entity.T_ArticleComments;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MoreThread extends Thread {

    private static final Lock lock = new ReentrantLock();
    private String threadName;

    private List<Object> CommentsList = new ArrayList<>();


    private String mainPath = "E:\\ZKZD2023\\大V项目\\多线程测试\\";
    private String typePath = "7_SecondCommentsData\\";


//    public  ArrayList<Object> Method_1_GetDBList(int GroupNumber){
//        return dao_articleComments.MethodFind(GroupNumber);
//    }

    public MoreThread(String threadName, List<Object> CommentsList) {
        this.threadName = threadName;
        this.CommentsList = new CopyOnWriteArrayList<>(CommentsList);
    }


    @Override
    public void run() {
        System.out.println("线程名称" + threadName);

        Method_1_DownSecondeComments(mainPath, typePath);

    }

    public void Method_1_DownSecondeComments(String mainPath, String typePath) {
        Dao_ArticleComments dao_articleComments = new Dao_ArticleComments(3);
        for (Object Comments : CommentsList) {
            String CommentsID = ((T_ArticleComments) Comments).getCommentsID();
            String State = ((T_ArticleComments) Comments).getDownState();
            String user_name = ((T_ArticleComments) Comments).getAuthor();
            String savePath = mainPath + user_name + "\\" + typePath;

            File file_One = new File(savePath);
            if (!file_One.exists()) {
                file_One.mkdirs();
            }

            String mainurl = "https://www.toutiao.com/2/comment/v2/reply_list/?aid=";
            String singer = "_02B4Z6wo00901RW9ZrAAAIDBlb-c8mslHHUVmWIAACAOG2hqFJAhzK0CxPD.YBu81xvNQswY-aIMXgu7eNsmjUwlcMpOx-0jbZsaEzvc7MoEVvdISeEBtE4sdjq6r.9TxXaf3ZCKmNvs7oQz71";
            int ID = ((T_ArticleComments) Comments).getID();
            String repost = "0";
            String aid = "24";
            int count = 5;
            int offset = 0;
            String app_name = "toutiao_web";
            if (State.equals("否")) {

                String content = Method_2_RequestSecondComments(mainurl, singer, repost, count, offset, CommentsID, app_name, aid);
                if (!content.equals("Error")) {
                    Method_3_SaveFile(savePath + CommentsID + "_SecondComments.txt", content);
                    Date date = new Date();//此时date为当前的时间
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置当前时间的格式，为年-月-日
                    dao_articleComments.Methoc_ChangeDownState(ID, dateFormat.format(date));
                    System.out.println(CommentsID + "下载完成了" + "\t" + dateFormat.format(date));
                } else {
                    Method_3_SaveFile(mainPath + user_name + "\\" + "_SecondCommentsError.txt", user_name + "_" + CommentsID + "\n");
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
                if (jsonObject1.getString("has_more").equals("false")) {
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


    public void Method_3_SaveFile(String path, String Content) {
        //这个是覆盖的存储方式
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(path, false), 331074);//165537
            bufferedOutputStream.write(Content.getBytes(StandardCharsets.UTF_8));   //StandardCharsets.UTF_8
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }


    public static void main(String[] args) {
        Dao_ArticleComments dao_articleComments = new Dao_ArticleComments(3);
        ArrayList<Object> list1 = dao_articleComments.MethodFind(1);
        ArrayList<Object> list2 = dao_articleComments.MethodFind(2);
        ArrayList<Object> list3 = dao_articleComments.MethodFind(3);
        MoreThread thread1 = new MoreThread("Thread-1", list1);
        MoreThread thread2 = new MoreThread("Thread-2", list2);
        MoreThread thread3 = new MoreThread("Thread-3", list3);
        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

// 主线程会等待 thread1 和 thread2 执行完毕后再继续执行
        System.out.println("All threads have finished.");

    }

}
