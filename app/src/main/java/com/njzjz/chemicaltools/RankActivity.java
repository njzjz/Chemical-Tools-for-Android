package com.njzjz.chemicaltools;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.selectKeys(Arrays.asList("examCorrectNumber", "qqname","username"));
        userQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e==null) {
                    List<AVUser> userlist = list;// 符合 priority = 0 的 Todo 数组
                    //生成动态数组，并且转载数据
                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    for (int i = 0; i < userlist.size(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        String score_str="0";
                        if(userlist.get(i).getString("examCorrectNumber")!=null)
                            score_str = userlist.get(i).getString("examCorrectNumber");
                        int score = Integer.parseInt(score_str);
                        if (score > 0) {
                            String name = userlist.get(i).getString("qqname");
                            if (name == null) {
                                name = userlist.get(i).getString("username");
                                name = name.substring(0, name.indexOf("@"));
                            }
                            map.put("text1", name);
                            map.put("text2", score_str);
                            mylist.add(map);
                        }
                    }

                    //冒泡排序
                    int i, j, len = mylist.size();
                    HashMap<String, String> temp;
                    for (i = 0; i < len - 1; i++)
                        for (j = 0; j < len - 1 - i; j++)
                            if (Integer.parseInt(mylist.get(j).get("text2")) < Integer.parseInt(mylist.get(j + 1).get("text2"))) {
                                temp = mylist.get(j);
                                mylist.set(j, mylist.get(j + 1));
                                mylist.set(j + 1, temp);
                            }
                    for (i = 0; i < len; i++) mylist.get(i).put("number", String.valueOf(i + 1));

                    //生成适配器，数组===》ListItem
                    SimpleAdapter mSchedule = new SimpleAdapter(RankActivity.this, //没什么解释
                            mylist,//数据来源
                            R.layout.ranklist,//ListItem的XML实现

                            //动态数组与ListItem对应的子项
                            new String[]{"number", "text1", "text2"},

                            //ListItem的XML文件里面的两个TextView ID
                            new int[]{R.id.list_item_card_number, R.id.list_item_card_text1, R.id.list_item_card_text2});
                    //添加并且显示
                    ListView rank_list = (ListView) findViewById(R.id.rank_list);
                    rank_list.setAdapter(mSchedule);
                }else{
                    Toast.makeText(RankActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //菜单栏返回键功能
            case android.R.id.home:
                Intent intent = new Intent(this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
