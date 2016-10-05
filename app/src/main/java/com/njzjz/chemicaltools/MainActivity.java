package com.njzjz.chemicaltools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.mikepenz.aboutlibraries.Libs;
import com.tencent.stat.StatService;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,
        AdapterView.OnItemClickListener {
    private EditText etTest;
    private ListPopupWindow lpw;
    private String[] list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
        TextView mFileContentView = (TextView) findViewById(R.id.elementTextview);
        mFileContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mFileContentView.setMovementMethod(LinkMovementMethod.getInstance());
        final Button elementButton = (Button) findViewById(R.id.elementButton);
        final String[] elementNameArray = getResources().getStringArray(R.array.elementNameArray);
        final String[] elementAbbrArray = getResources().getStringArray(R.array.elementAbbrArray);
        final String[] elementMassArray= getResources().getStringArray(R.array.elementMassArray);
        final String[] elementIUPACArray= getResources().getStringArray(R.array.elementIUPACArray);
        final String[] elementOriginArray= getResources().getStringArray(R.array.elementOriginArray);
        final TextView elementTextview=(TextView)findViewById(R.id.elementTextview);
        elementButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i;
                int elementNumber=0;
                String elementInput;
                EditText elementText =(EditText)findViewById(R.id.elementText);
                elementInput=elementText.getText().toString();
                for(i=0;i<118;i++) {
                    if (elementInput.equals(String.valueOf(i+1))){
                        elementNumber=i+1;
                    }else if(elementInput.equals(elementNameArray[i])){
                        elementNumber=i+1;
                    }else if(elementInput.toUpperCase().equals(elementAbbrArray[i].toUpperCase())){
                        elementNumber=i+1;
                    }else if(elementInput.toUpperCase().equals(elementIUPACArray[i].toUpperCase())){
                        elementNumber=i+1;
                    }
                }
                Resources res = getResources();
                if(elementNumber>0){
                    String elementOutput=String.format(res.getString(R.string.elementOutput_name),elementNumber,elementNameArray[elementNumber-1],elementAbbrArray[elementNumber-1],elementMassArray[elementNumber-1],elementIUPACArray[elementNumber-1],elementOriginArray[elementNumber-1]);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElement",elementInput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementNumber",String.valueOf(elementNumber));
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutput",elementOutput);
                    String elementoutputhtml=elementOutput+"\n<a href='https://en.wikipedia.org/wiki/"+elementIUPACArray[elementNumber-1]+"'>"+getResources().getString(R.string.elementWikipedia)+"</a>";
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutputHtml",elementoutputhtml);
                    AVUser currentUser = AVUser.getCurrentUser();
                    if (currentUser != null) {
                        AVUser.getCurrentUser().put("historyElementOutput", elementOutput);
                        AVUser.getCurrentUser().put("historyElementOutputHtml", elementoutputhtml);
                        AVUser.getCurrentUser().put("historyElementNumber", String.valueOf(elementNumber));
                        AVUser.getCurrentUser().put("historyElement", elementInput);
                        AVUser.getCurrentUser().saveInBackground();
                    }
                    elementTextview.setText(Html.fromHtml(parseContent(elementoutputhtml)));
                    TypedArray elementPictureArray = getResources().obtainTypedArray(R.array.elementPictureArray);
                    int  resId = elementPictureArray.getResourceId(elementNumber-1, 0);
                    ImageView elementImage=(ImageView) findViewById(R.id.elementImage);
                    elementImage.setImageResource(resId);
                }else{
                    Snackbar.make(v, res.getString(R.string.error_name), Snackbar.LENGTH_LONG)
                            .setAction("Error", null).show();
                };
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            };
        });
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            // 有
            currentUser .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    // 调用 fetchIfNeededInBackground 和 refreshInBackground 效果是一样的。
                    String historyElementOutput=avObject.getString("historyElementOutput");
                    String historyElementOutputHtml=avObject.getString("historyElementOutputHtml");
                    String historyElementNumber=avObject.getString("historyElementNumber");
                    if(historyElementNumber==null)historyElementNumber="0";
                    String historyElement=avObject.getString("historyElement");
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutput",historyElementOutput);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementOutputHtml",historyElementOutputHtml);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElementNumber",historyElementNumber);
                    PreferenceUtils.setPrefString(getApplicationContext(),"historyElement",historyElement);
                }});
        }
        final EditText elementText = (EditText) findViewById(R.id.elementText);
        elementText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                elementText.setHint(null);
            }
        });
        elementText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    elementButton.performClick();
                }
                return false;
            }
        });
        String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
        int elementNumber=Integer.parseInt(historyElementNumber);
        if (elementNumber>0) {
            TypedArray elementPictureArray = getResources().obtainTypedArray(R.array.elementPictureArray);
            int resId = elementPictureArray.getResourceId(elementNumber - 1, 0);
            ImageView elementImage = (ImageView) findViewById(R.id.elementImage);
            elementImage.setImageResource(resId);
        }
        String historyElementOutputHtml=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementOutputHtml","");
        if(!historyElementOutputHtml.equals("")){
            elementTextview.setText(Html.fromHtml(parseContent(historyElementOutputHtml)));
        }

        etTest = (EditText) findViewById(R.id.elementText);
        etTest.setOnTouchListener((View.OnTouchListener) this);

        list = elementAbbrArray;
        lpw = new ListPopupWindow(this);
        lpw.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list));
        lpw.setAnchorView(etTest);
        lpw.setModal(true);
        lpw.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }
    private String parseContent(String content) {
        content = content.replace("\n","<br>");
        return content;
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
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String item = list[position];
        etTest.setText(item);
        lpw.dismiss();
        Button elementButton=(Button)findViewById(R.id.elementButton);
        elementButton.callOnClick();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                lpw.show();
                String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
                int elementNumber=Integer.parseInt(historyElementNumber);
                lpw.setSelection(elementNumber-1);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //菜单栏返回键功能
            case android.R.id.home:
                Intent intent = new Intent(this, TitleActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_share:
                String historyElementOutput=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementOutput","");
                ShareAction share=new ShareAction(MainActivity.this).withText(historyElementOutput);
                //等垃圾微信sdk什么时候支持图文分享了再加上吧
                /*
                String historyElementNumber=PreferenceUtils.getPrefString(getApplicationContext(),"historyElementNumber","0");
                int elementNumber=Integer.parseInt(historyElementNumber);
                if (elementNumber>0) {
                    TypedArray elementPictureArray = getResources().obtainTypedArray(R.array.elementPictureArray);
                    int resId = elementPictureArray.getResourceId(elementNumber - 1, 0);
                    UMImage image = new UMImage(this,resId);//资源文件
                    share.withMedia(image);
                }
                */
                share.setDisplayList(/*SHARE_MEDIA.QQ,*/SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                     .setCallback(umShareListener).open();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            case R.id.action_Feedback:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:njzjz@msn.com?subject=Chemical Tools App Feedback"));
                startActivity(browserIntent);
                return true;
            case R.id.action_About:
                new Libs.Builder().withActivityTitle(getString(R.string.button_About)).withFields(R.string.class.getFields()).start(MainActivity.this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openSettings(){
        Intent intent =new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
        //    Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t){
        //    Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
      //      Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

};

