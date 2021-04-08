package edu.skku.map.personalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Find_listviewAdapter extends BaseAdapter {
    public ArrayList<Find_listItem> list_arr = new ArrayList<>();

    @Override
    public int getCount() {
        return list_arr.size();
    }

    @Override
    public Object getItem(int position) {
        return list_arr.get(position);
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
            convertView = inflater.inflate(R.layout.find_listview_layout, parent, false);
        }

        TextView store_name = (TextView)convertView.findViewById(R.id.store_name_list);
        TextView job_type = (TextView)convertView.findViewById(R.id.jop_type_list);
        TextView pay = (TextView)convertView.findViewById(R.id.pay_list);
        TextView location  = (TextView)convertView.findViewById(R.id.location_list);

        Find_listItem listItem = list_arr.get(pos);
        store_name.setText(listItem.getFind_store());
        job_type.setText(listItem.getJob_type());
        pay.setText(listItem.getPay());
        location.setText(listItem.getLocation());

        return convertView;
    }

    public void addItem(String find_store, String find_job_type, String find_pay, String find_location){
        Find_listItem item = new Find_listItem();

        item.setFind_store(find_store);
        item.setJob_type(find_job_type);
        item.setPay(find_pay);
        item.setLocation(find_location);

        list_arr.add(item);
    }

    public void deleteItem(){
        list_arr.clear();
    }
}
