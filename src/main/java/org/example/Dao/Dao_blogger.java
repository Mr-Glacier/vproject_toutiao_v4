package org.example.Dao;

public class Dao_blogger extends DaoFather {

    public Dao_blogger(int choseTable) {
        super(choseTable);
    }


    //更新下载状态
    public void Method_ChangeDownState(int ID,String time){
        String sql1 = "update T_Blogger set  DownState = '是' where id ="+ID;
        MethodIUD(sql1);
        String sql2 =" update T_Blogger set DownTime = '"+time+"' where id =" +ID;
        MethodIUD(sql2);
    }


}
