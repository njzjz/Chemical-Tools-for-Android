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
                    case 0:
                       //Element
                        openElement(view);
                        break;
                    case 1:
                        //Mass
                        openMass(view);
                        break;
                    case 6:
                        //Feedback
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:njzjz@msn.com?subject=Chemical Tools App Feedback"));
                        startActivity(browserIntent);
                        break;
                    case 4:
                        //Share
                        UMImage image = new UMImage(TitleActivity.this, R.drawable.ic_launcher);//资源文件
                        new ShareAction(TitleActivity.this).withText(getString(R.string.app_name)
                                + "，化学专业学生必备的工具，下载地址：chem.njzjz.win").withTargetUrl("http://chem.njzjz.win").withMedia(image)
                                .withTitle(getString(R.string.app_name))
                                .setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                                .open();
                        break;
                    case 3:
                        //Exam
                        openExam(view);
                        break;
                    case 5:
                        //Settings
                        openSettings();
                        break;
                    case 7:
                        //Website
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://chem.njzjz.win/")));
                        break;
                    case 8:
                        //About
                        new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(TitleActivity.this);
                        break;
                    case 2:
                        //acid
                        openAcid(view);
                        break;
                }

            }
        });

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
