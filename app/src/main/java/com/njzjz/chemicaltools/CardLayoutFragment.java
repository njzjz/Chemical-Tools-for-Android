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

import java.util.ArrayList;

import static com.njzjz.chemicaltools.TitleActivity.examCorrectNumber;
import static com.njzjz.chemicaltools.TitleActivity.examIncorrectnumber;
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
        items1.add(getString(R.string.button_element));
        if(!historyElementOutput.equals("")){
            items2.add(historyElementOutput);
        }else{
            items2.add(getString(R.string.button_notUsed));
        }
        items1.add(getString(R.string.button_mass));
        if(!historyMassOutput.equals("")){
            items2.add(historyMassOutput);
        }else{
            items2.add(getString(R.string.button_notUsed));
        }
        items1.add(getString(R.string.button_exam));
        int sum=examCorrectNumber+examIncorrectnumber;
        if(sum>0){
            double examCorrectPercent=(double)examCorrectNumber/sum*100;
            items2.add(String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent));
        }else {
            items2.add(getString(R.string.button_notUsed));
        }
        return new CardsAdapter(getActivity(), items1,items2, new ListItemButtonClickListener());
    }

    private final class ListItemButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = cardsList.getFirstVisiblePosition(); i <= cardsList.getLastVisiblePosition(); i++) {
                if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                    String x = "";
                    switch (i){
                        case 0:
                            if(!historyElementOutput.equals("")){
                                x=historyElementOutput;
                            }else{
                                x=getString(R.string.app_name)+"\nhttps://github.com/njzjz/Chemical-Tools-for-Android";
                            }
                            break;
                        case 1:
                            if(!historyMassOutput.equals("")){
                                x=historyMassOutput;
                            }else{
                                x=getString(R.string.app_name)+"\nhttps://github.com/njzjz/Chemical-Tools-for-Android";
                            }
                            break;
                        case 2:
                            int sum=examCorrectNumber+examIncorrectnumber;
                            if(sum>0){
                                double examCorrectPercent=(double)examCorrectNumber/sum*100;
                                x=String.format(getResources().getString(R.string.examScoreOutput_name),sum,examCorrectNumber,examCorrectPercent);
                            }else {
                                x=getString(R.string.app_name)+"\nhttps://github.com/njzjz/Chemical-Tools-for-Android";
                            }
                            break;
                    }
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    share.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.app_name));
                    share.putExtra(Intent.EXTRA_TEXT, x);
                    startActivity(Intent.createChooser(share,
                            getString(R.string.app_name)));
                } else if (v == cardsList.getChildAt(i - cardsList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
                    // PERFORM ANOTHER ACTION WITH THE ITEM AT POSITION i
                    switch (i){
                        case 0:
                            Intent intent1 =new Intent(getActivity(), MainActivity.class);
                            startActivity(intent1);
                            break;
                        case 1:
                            Intent intent2 =new Intent(getActivity(), MassActivity.class);
                            startActivity(intent2);
                            break;
                        case 2:
                            Intent intent3 =new Intent(getActivity(), ExamActivity.class);
                            startActivity(intent3);
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
                case 0:
                    Intent intent1 =new Intent(getActivity(), MainActivity.class);
                    startActivity(intent1);
                    break;
                case 1:
                    Intent intent2 =new Intent(getActivity(), MassActivity.class);
                    startActivity(intent2);
                    break;
                case 2:
                    Intent intent3 =new Intent(getActivity(), ExamActivity.class);
                    startActivity(intent3);
                    break;
            }
        }
    }
}
