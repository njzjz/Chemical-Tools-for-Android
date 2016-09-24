package com.njzjz.chemicaltools;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class CardsAdapter extends BaseAdapter {

    private List<String> items1;
    private List<String> items2;
    private final OnClickListener itemButtonClickListener;
    private final Context context;

    public CardsAdapter(Context context, List<String> items1,List<String> items2, OnClickListener itemButtonClickListener) {
        this.context = context;
        this.items1= items1;
        this.items2= items2;
        this.itemButtonClickListener = itemButtonClickListener;
    }

    @Override
    public int getCount() {
        return items2.size();
    }

    @Override
    public String getItem(int position) {
        return items2.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_card, null);

            holder = new ViewHolder();
            holder.itemText1 = (TextView) convertView.findViewById(R.id.list_item_card_text1);
            holder.itemText2 = (TextView) convertView.findViewById(R.id.list_item_card_text2);
            holder.itemButton1 = (Button) convertView.findViewById(R.id.list_item_card_button_1);
            holder.itemButton2 = (Button) convertView.findViewById(R.id.list_item_card_button_2);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemText1.setText(items1.get(position));
        holder.itemText2.setText(items2.get(position));

        if (itemButtonClickListener != null) {
            holder.itemButton1.setOnClickListener(itemButtonClickListener);
            holder.itemButton2.setOnClickListener(itemButtonClickListener);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemText1;
        private TextView itemText2;
        private Button itemButton1;
        private Button itemButton2;
    }

}