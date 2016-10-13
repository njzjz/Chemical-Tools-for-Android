package com.njzjz.chemicaltools;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;

public class CardLayoutFragment extends Fragment {

    private ListView cardsList;

    public CardLayoutFragment() {
        // nop
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_layout, container, false);
        cardsList = (ListView) rootView.findViewById(R.id.cards_list);
        setupList();
        return rootView;
    }

    private void setupList() {
        cardsList.setAdapter(createAdapter());
        cardsList.setOnItemClickListener(new ListItemClickListener());
    }

    private CardsAdapter createAdapter() {
        ArrayList<Integer> pic = new ArrayList<Integer>();
        ArrayList<String> items1 = new ArrayList<String>();
        ArrayList<String> items2 = new ArrayList<String>();
        ArrayList<String> itemsButton1 = new ArrayList<String>();
        ArrayList<String> itemsButton2 = new ArrayList<String>();

        TypedArray iconArray=getResources().obtainTypedArray(R.array.iconArray);
        for(int i=0;i<5;i++) {
            String items1Text = null,items2Text = null,Button1Text = null,Button2Text = null;
            Integer resId = iconArray.getResourceId(i, 0);
            switch (i){
                case 0:
                    items1Text=getString(R.string.button_doyouknow);
                    break;
                case 1:
                    items1Text=getString(R.string.button_element);
                    break;
                case 2:
                    items1Text=getString(R.string.button_mass);
                    break;
                case 3:
                    items1Text=getString(R.string.button_acid);
                    break;
                case 4:
                    items1Text=getString(R.string.button_exam);
                    break;
            }
            switch (i){
                case 0:
                    items2Text=doyouknow();
                    break;
                case 1:
                    String historyElementOutput = PreferenceUtils.getPrefString(getActivity().getApplicationContext(), "historyElementOutput", "");
                    items2Text=historyElementOutput;
                    break;
                case 2:
                    String historyMassOutput = PreferenceUtils.getPrefString(getActivity().getApplicationContext(), "historyMassOutput", "");
                    items2Text=historyMassOutput;
                    break;
                case 3:
                    String historyAcidOutput=PreferenceUtils.getPrefString(getActivity().getApplicationContext(), "historyAcidOutput", "");
                    items2Text=historyAcidOutput;
                    break;
                case 4:
                    int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getActivity().getApplicationContext(),"examCorrectNumber","0"));
                    int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getActivity().getApplicationContext(),"examIncorrectnumber","0"));
                    int sum=examCorrectNumber+examIncorrectnumber;
                    if(sum>0){
                        double examCorrectPercent=(double)examCorrectNumber/sum*100;
                        items2Text=String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent);
                    }
                    break;
            }
            if(items2Text==null)items2Text=getString(R.string.button_notUsed);
            switch (i){
                case 0:case 1:case 2:case 3:case 4:
                    Button1Text=getString(R.string.button_Share);
                    break;
            }
            switch (i){
                case 0:
                    Button2Text=getString(R.string.Change);
                    break;
                case 1:case 2:case 3:case 4:
                    Button2Text=getString(R.string.button_open);
            }
            pic.add(resId);
            items1.add(items1Text);
            items2.add(items2Text);
            itemsButton1.add(Button1Text);
            itemsButton2.add(Button2Text);
        }
        return new CardsAdapter(getActivity(), pic,items1,items2,itemsButton1,itemsButton2, new ListItemButtonClickListener());
    }

    private final class ListItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = cardsList.getFirstVisiblePosition(); i <= cardsList.getLastVisiblePosition(); i++) {
                if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                    String x = null;
                    switch (i){
                        case 0:case 1:case 2:case 3:
                            TextView itemText = (TextView) cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_text2);
                            if(!itemText.getText().toString().equals(getResources().getString(R.string.button_notUsed))){
                                x=itemText.getText().toString();
                            }
                            break;
                        case 4:
                            int examCorrectNumber=Integer.parseInt(PreferenceUtils.getPrefString(getActivity().getApplicationContext(),"examCorrectNumber","0"));
                            int examIncorrectnumber=Integer.parseInt(PreferenceUtils.getPrefString(getActivity().getApplicationContext(),"examIncorrectnumber","0"));
                            int sum=examCorrectNumber+examIncorrectnumber;
                            if(sum>0){
                                double examCorrectPercent=(double)examCorrectNumber/sum*100;
                                x=String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent);
                            }
                            break;
                    }
                    if(x==null)x=getString(R.string.app_name)+"\n"+getString(R.string.app_website);
                    new ShareAction(getActivity()).withText(x)
                            .setDisplayList(SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                            .open();
                } else if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
                    // PERFORM ANOTHER ACTION WITH THE ITEM AT POSITION i
                    switch (i){
                        case 0:
                            String doyouknowText=doyouknow();
                            TextView textView= (TextView) cardsList.getChildAt(0).findViewById(R.id.list_item_card_text2);
                            textView.setText(doyouknowText);
                            break;
                        case 1:
                            Intent intent1 =new Intent(getActivity(), MainActivity.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 =new Intent(getActivity(), MassActivity.class);
                            startActivity(intent2);
                            break;
                        case 3:
                            Intent intent3 =new Intent(getActivity(), AcidActivity.class);
                            startActivity(intent3);
                            break;
                        case 4:
                            Intent intent4 =new Intent(getActivity(), ExamActivity.class);
                            startActivity(intent4);
                            break;
                    }
                }
            }
        }
    }

    private final class ListItemClickListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1:
                    Intent intent1 =new Intent(getActivity(), MainActivity.class);
                    startActivity(intent1);
                    break;
                case 2:
                    Intent intent2 =new Intent(getActivity(), MassActivity.class);
                    startActivity(intent2);
                    break;
                case 3:
                    Intent intent3 =new Intent(getActivity(), AcidActivity.class);
                    startActivity(intent3);
                    break;
                case 4:
                    Intent intent4 =new Intent(getActivity(), ExamActivity.class);
                    startActivity(intent4);
                    break;
            }
        }
    }

    private String doyouknow(){
        final double d = Math.random();
        String[] doyouknowArray = getResources().getStringArray(R.array.doyouknow);
        final int i = (int)(d*doyouknowArray.length);
        String doyouknowText=doyouknowArray[i];
        return doyouknowText;
    }
}
