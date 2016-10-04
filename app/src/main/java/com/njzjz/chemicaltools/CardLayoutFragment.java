package com.njzjz.chemicaltools;

import android.app.Fragment;
import android.content.Intent;
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

import static com.njzjz.chemicaltools.TitleActivity.doyouknowArray;
import static com.njzjz.chemicaltools.TitleActivity.doyouknowText;
import static com.njzjz.chemicaltools.TitleActivity.examCorrectNumber;
import static com.njzjz.chemicaltools.TitleActivity.examIncorrectnumber;
import static com.njzjz.chemicaltools.TitleActivity.historyAcidOutput;
import static com.njzjz.chemicaltools.TitleActivity.historyElementOutput;
import static com.njzjz.chemicaltools.TitleActivity.historyMassOutput;

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
        ArrayList<String> items1 = new ArrayList<String>();
        ArrayList<String> items2 = new ArrayList<String>();
        ArrayList<String> itemsButton1 = new ArrayList<String>();
        ArrayList<String> itemsButton2 = new ArrayList<String>();
        //你知道吗
        items1.add(getString(R.string.button_doyouknow));
        final double d = Math.random();
        final int i = (int)(d*doyouknowArray.length);
        doyouknowText=doyouknowArray[i];
        items2.add(doyouknowText);
        itemsButton1.add(getString(R.string.button_Share));
        itemsButton2.add(getString(R.string.Change));
        //元素查询
        items1.add(getString(R.string.button_element));
        if(!historyElementOutput.equals("")){
            items2.add(historyElementOutput);
        }else{
            items2.add(getString(R.string.button_notUsed));
        }
        itemsButton1.add(getString(R.string.button_Share));
        itemsButton2.add(getString(R.string.button_open));
        //质量计算
        items1.add(getString(R.string.button_mass));
        if(!historyMassOutput.equals("")){
            items2.add(historyMassOutput);
        }else{
            items2.add(getString(R.string.button_notUsed));
        }
        itemsButton1.add(getString(R.string.button_Share));
        itemsButton2.add(getString(R.string.button_open));
        //酸碱计算
        items1.add(getString(R.string.button_acid));
        if(!historyAcidOutput.equals("")){
            items2.add(historyAcidOutput);
        }else{
            items2.add(getString(R.string.button_notUsed));
        }
        itemsButton1.add(getString(R.string.button_Share));
        itemsButton2.add(getString(R.string.button_open));
        //元素记忆
        items1.add(getString(R.string.button_exam));
        int sum=examCorrectNumber+examIncorrectnumber;
        if(sum>0){
            double examCorrectPercent=(double)examCorrectNumber/sum*100;
            items2.add(String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent));
        }else {
            items2.add(getString(R.string.button_notUsed));
        }
        itemsButton1.add(getString(R.string.button_Share));
        itemsButton2.add(getString(R.string.button_open));

        return new CardsAdapter(getActivity(), items1,items2,itemsButton1,itemsButton2, new ListItemButtonClickListener());
    }

    private final class ListItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = cardsList.getFirstVisiblePosition(); i <= cardsList.getLastVisiblePosition(); i++) {
                if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                    String x = "";
                    switch (i){
                        case 0:case 1:case 2:case 3:
                            TextView itemText = (TextView) cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_text2);
                            if(!historyElementOutput.equals(getResources().getString(R.string.button_notUsed))){
                                x=itemText.getText().toString();
                            }else{
                                x=getString(R.string.app_name)+"\nhttps://github.com/njzjz/Chemical-Tools-for-Android";
                            }
                            break;
                        case 4:
                            int sum=examCorrectNumber+examIncorrectnumber;
                            if(sum>0){
                                double examCorrectPercent=(double)examCorrectNumber/sum*100;
                                x=String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent);
                            }else {
                                x=getString(R.string.app_name)+"\nhttps://github.com/njzjz/Chemical-Tools-for-Android";
                            }
                            break;
                    }
                   // Intent share = new Intent(Intent.ACTION_SEND);
                   // share.setType("text/plain");
                   // share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // share.putExtra(Intent.EXTRA_SUBJECT,
                   //         getString(R.string.app_name));
                   // share.putExtra(Intent.EXTRA_TEXT, x);
                   // startActivity(Intent.createChooser(share,
                   //         getString(R.string.app_name)));
                    new ShareAction(getActivity()).withText(x)
                            .setDisplayList(/*SHARE_MEDIA.QQ,*/SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,/*SHARE_MEDIA.SINA,*/SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                            .open();
                } else if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
                    // PERFORM ANOTHER ACTION WITH THE ITEM AT POSITION i
                    switch (i){
                        case 0:
                            final double d = Math.random();
                            final int i2 = (int)(d*doyouknowArray.length);
                            doyouknowText=doyouknowArray[i2];
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
    private String parseContent(String content) {
        content = content.replace("\n","<br>");
        return content;
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
}
