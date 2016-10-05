package com.njzjz.chemicaltools;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;
import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class TitleActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.njzjz.chemicalTools.MESSAGE";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    private boolean drawerArrowColor;
    public static String historyElementOutput;
    public static String historyMassOutput;
    public static String historyAcidOutput;
    public static int examCorrectNumber;
    public static int examIncorrectnumber;
    public static String[] doyouknowArray;
    public static String doyouknowText;
    public static boolean notfirstCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        if (!notfirstCreate) {
            //activity首次创建时
            if(Build.VERSION.SDK_INT>=23){
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
                ActivityCompat.requestPermissions(this,mPermissionList,123);
            }

            historyElementOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyElementOutput", "");
            historyMassOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyMassOutput", "");
            historyAcidOutput = PreferenceUtils.getPrefString(getApplicationContext(), "historyAcidOutput", "");
            examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examCorrectNumber","0"));
            examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getApplicationContext(),"examIncorrectnumber","0"));
            doyouknowArray = getResources().getStringArray(R.array.doyouknow);

            notfirstCreate=true;
        }
        String loginButton;
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 有
            AVUser.getCurrentUser().fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    // 调用 fetchIfNeededInBackground 和 refreshInBackground 效果是一样的。
                    String historyElementOutput=avObject.getString("historyElementOutput");
                    String historyElementOutputHtml=avObject.getString("historyElementOutputHtml");
                    String historyElementNumber=avObject.getString("historyElementNumber");
                    if(historyElementNumber==null)historyElementNumber="0";
                    String historyElement=avObject.getString("historyElement");
                    String historyMass=avObject.getString("historyMass");
                    String historyMassOutput=avObject.getString("historyMassOutput");
                    String historyAcidOutput=avObject.getString("historyAcidOutput");
                    String examCorrectNumber=avObject.getString("examCorrectNumber");
                    if(examCorrectNumber==null)examCorrectNumber="0";
                    String examIncorrectnumber=avObject.getString("examIncorrectnumber");
                    if(examIncorrectnumber==null)examIncorrectnumber="0";
                    String elementnumber_limit=avObject.getString("elementnumber_limit");
                    if(elementnumber_limit==null)elementnumber_limit="118";
                    String examMode=avObject.getString("examMode");
                    if(examMode==null)examMode="0";
                    Boolean setting_examOptionMode=avObject.getBoolean("setting_examOptionMode");
                    if(setting_examOptionMode==null)setting_examOptionMode=false;
                    String pKw=avObject.getString("pKw");
                    if(pKw==null)pKw="14";
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutput",historyElementOutput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutputHtml",historyElementOutputHtml);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementNumber",historyElementNumber);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElement",historyElement);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyMass",historyMass);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyMassOutput",historyMassOutput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyAcidOutput",historyAcidOutput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"examCorrectNumber",examCorrectNumber);
                    PreferenceUtils.setPrefString(getApplicationContext(),"examIncorrectnumber",examIncorrectnumber);
                    PreferenceUtils.setPrefString(getApplicationContext(),"elementnumber_limit",elementnumber_limit);
                    PreferenceUtils.setPrefString(getApplicationContext(),"examMode",examMode);
                    PreferenceUtils.setPrefBoolean(getApplicationContext(),"setting_examOptionMode",setting_examOptionMode);
                    PreferenceUtils.setPrefString(getApplicationContext(),"pKw",pKw);
                }});
            loginButton=getString(R.string.action_sign_out);
        } else {
            //缓存用户对象为空时，可打开用户注册界面…
            loginButton=getString(R.string.action_sign_in);
        }
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.home);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new CardLayoutFragment()).commit();
        }

        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                drawerArrow, R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        Resources res = getResources();
        String[] values = new String[]{
                loginButton,
                res.getString(R.string.button_element),
                res.getString(R.string.button_mass),
                res.getString(R.string.button_acid),
                res.getString(R.string.button_exam),
                res.getString(R.string.button_Share),
                res.getString(R.string.button_Settings),
                res.getString(R.string.setting_feedback),
                res.getString(R.string.setting_website),
                res.getString(R.string.button_About),
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 1:
                       //Element
                        openElement(view);
                        break;
                    case 2:
                        //Mass
                        openMass(view);
                        break;
                    case 7:
                        //Feedback
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:njzjz@msn.com?subject=Chemical Tools App Feedback"));
                        startActivity(browserIntent);
                        break;
                    case 5:
                        //Share
                        UMImage image = new UMImage(TitleActivity.this, R.drawable.ic_launcher);//资源文件
                        new ShareAction(TitleActivity.this).withText(getString(R.string.app_name)
                                + "，化学专业学生必备的工具，下载地址：chem.njzjz.win").withTargetUrl("http://chem.njzjz.win").withMedia(image)
                                .withTitle(getString(R.string.app_name))
                                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                                .open();
                        break;
                    case 4:
                        //Exam
                        openExam(view);
                        break;
                    case 6:
                        //Settings
                        openSettings();
                        break;
                    case 8:
                        //Website
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://chem.njzjz.win/")));
                        break;
                    case 9:
                        //About
                        new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(TitleActivity.this);
                        break;
                    case 3:
                        //acid
                        openAcid(view);
                        break;
                    case 0:
                        //Login
                        AVUser currentUser = AVUser.getCurrentUser();
                        if(currentUser !=null){
                            AVUser.logOut();// 清除缓存用户对象
                            finish();
                            Intent intent = new Intent(TitleActivity.this, TitleActivity.class);
                            startActivity(intent);
                        }else {
                            openLogin();
                        }
                }

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1002 && resultCode==1003)
            if(data.getStringExtra("result").equals("1")){
                finish();
                Intent intent = new Intent(this, TitleActivity.class);
                startActivity(intent);
            }
    }
    public void openLogin(){
        Intent intent =new Intent(this, LoginActivity.class);
        startActivityForResult(intent,1002);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //菜单栏返回键功能
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
            case R.id.action_share:
                //Intent share = new Intent(Intent.ACTION_SEND);
                //share.setType("text/plain");
                //share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //share.putExtra(Intent.EXTRA_SUBJECT,
                //        getString(R.string.app_name));
                //share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "\n" +
                //        "https://github.com/njzjz/Chemical-Tools-for-Android");
                //startActivity(Intent.createChooser(share,
                //        getString(R.string.app_name)));
                UMImage image = new UMImage(this, R.drawable.ic_launcher);//资源文件
                new ShareAction(this).withText(getString(R.string.app_name)
                        + "，化学专业学生必备的工具，下载地址：chem.njzjz.win").withTargetUrl("http://chem.njzjz.win").withMedia(image)
                        .withTitle(getString(R.string.app_name))
                        .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                        .open();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_Feedback:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:njzjz@msn.com?subject=Chemical Tools App Feedback"));
                startActivity(browserIntent);
                return true;
            case R.id.action_About:
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(TitleActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openElement(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openMass(View view) {
        Intent intent = new Intent(this, MassActivity.class);
        startActivity(intent);
    }
    public void openExam(View view) {
        Intent intent = new Intent(this, ExamActivity.class);
        startActivity(intent);
    }
    public void openAcid(View view) {
        Intent intent = new Intent(this, AcidActivity.class);
        startActivity(intent);
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
