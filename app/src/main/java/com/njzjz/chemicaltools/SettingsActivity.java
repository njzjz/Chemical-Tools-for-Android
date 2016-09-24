package com.njzjz.chemicaltools;

/**
 * Created by zeng on 2016/9/23.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.aboutlibraries.Libs;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        PreferenceFragment fragment = new PreferenceFragment() {
            @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                               Bundle savedInstanceState) {
                View view = super.onCreateView(inflater, container, savedInstanceState);
                //Set night-mode for background
                return view;
            }

            @Override public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.settings_general);
                findPreference("about").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override public boolean onPreferenceClick(Preference preference) {
                                new Libs.Builder().withActivityTitle( getString(R.string.button_About)).withFields(R.string.class.getFields()).start(getActivity());
                                return false;
                            }
                        });
                findPreference("clearSettings").setOnPreferenceClickListener(
                        new Preference.OnPreferenceClickListener() {
                            @Override public boolean onPreferenceClick(Preference preference) {
                                showSimpleDialog(getView());
                                return false;
                            }
                        });
                findPreference("elementnumber_limit").setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                String elementnumber_limit=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"elementnumber_limit",elementnumber_limit);
                                return true;
                            }
                        }
                );
                ListPreference elementnumber_limitList= (ListPreference) findPreference("elementnumber_limit");
                switch (PreferenceUtils.getPrefString(getApplicationContext(),"elementnumber_limit","118")){
                    case "18":
                        elementnumber_limitList.setValueIndex(0);
                        break;
                    case "36":
                        elementnumber_limitList.setValueIndex(1);
                        break;
                    case "54":
                        elementnumber_limitList.setValueIndex(2);
                        break;
                    case "86":
                        elementnumber_limitList.setValueIndex(3);
                        break;
                    case "118":
                        elementnumber_limitList.setValueIndex(4);
                        break;
                };
                findPreference("examMode").setOnPreferenceChangeListener(
                        new Preference.OnPreferenceChangeListener() {
                            @Override
                            public boolean onPreferenceChange(Preference preference, Object newValue) {
                                String examMode=newValue.toString();
                                PreferenceUtils.setPrefString(getApplicationContext(),"examMode",examMode);
                                return true;
                            }
                        }
                );
                ListPreference examModeList= (ListPreference) findPreference("examMode");
                switch (PreferenceUtils.getPrefString(getApplicationContext(),"examMode","0")){
                    case "0":
                        examModeList.setValueIndex(0);
                        break;
                    case "1":
                        examModeList.setValueIndex(1);
                        break;
                    case "2":
                        examModeList.setValueIndex(2);
                        break;
                    case "3":
                        examModeList.setValueIndex(3);
                        break;
                    case "4":
                        examModeList.setValueIndex(4);
                        break;
                    case "5":
                        examModeList.setValueIndex(5);
                        break;
                    case "6":
                        examModeList.setValueIndex(6);
                        break;
                    case "7":
                        examModeList.setValueIndex(7);
                        break;
                    case "8":
                        examModeList.setValueIndex(8);
                        break;
                    case "9":
                        examModeList.setValueIndex(9);
                        break;
                    case "10":
                        examModeList.setValueIndex(10);
                        break;
                    case "11":
                        examModeList.setValueIndex(11);
                        break;
                };
            }

        };
        getFragmentManager().beginTransaction()
                .replace(R.id.include_settings_container, fragment)
                .commit();
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
    private void showSimpleDialog(View view) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(R.string.setting_clearSettings);
        builder.setMessage(R.string.setting_message);

        //监听下方button点击事件
        builder.setPositiveButton(R.string.postive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferenceUtils.clearPreference(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
            }
        });
        builder.setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //设置对话框是可取消的
        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}