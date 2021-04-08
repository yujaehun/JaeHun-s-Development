package edu.skku.map.personalproject;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Mystore_listviewAdapter extends BaseAdapter {

    public ArrayList<Mystore_listItem> listviewItem_arr = new ArrayList<Mystore_listItem>();

    public Mystore_listviewAdapter(){

    }

    @Override
    public int getCount() {
        return listviewItem_arr.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewItem_arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        final Context context = parent.getContext();

        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_layout, parent, false);
        }

        TextView store_name = (TextView)convertView.findViewById(R.id.textView);

        Mystore_listItem listItem = listviewItem_arr.get(pos);
        store_name.setText(listItem.getStore_name());

        return convertView;
    }

    public void addItem(String store_name){
        Mystore_listItem item = new Mystore_listItem();

        item.setStore_name(store_name);

        listviewItem_arr.add(item);
    }
}
