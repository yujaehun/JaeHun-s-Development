package edu.skku.map.personalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SR_listviewAdapter extends BaseAdapter {

    public ArrayList<SR_listItem> listviewItem_SR = new ArrayList<SR_listItem>();

    public SR_listviewAdapter(){

    }

    @Override
    public int getCount() {
        return listviewItem_SR.size();
    }

    @Override
    public Object getItem(int position) {
        return listviewItem_SR.get(position);
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

        TextView full_name = (TextView)convertView.findViewById(R.id.textView);

        SR_listItem listItem = listviewItem_SR.get(pos);
        full_name.setText(listItem.getFullname()+" / "+ listItem.getBirth());


        return convertView;
    }
    public void addItem(String fullname, String birth){
        SR_listItem item = new SR_listItem();

        item.setFullname(fullname);
        item.setBirth(birth);

        listviewItem_SR.add(item);
    }
}
