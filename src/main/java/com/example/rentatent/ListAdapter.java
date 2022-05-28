package com.example.rentatent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<RAT> {
    Context c;
    public ListAdapter(Context context, ArrayList< RAT > userArrayList) {
        super(context, R.layout.list_item,userArrayList);
        this.c = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RAT rat = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }
        TextView title = convertView.findViewById(R.id.list_title);
        TextView address = convertView.findViewById(R.id.list_address);
        TextView  description= convertView.findViewById(R.id.list_description);
        TextView  price= convertView.findViewById(R.id.list_price);
        title.setText(rat.getName());
        address.setText(rat.getAddress());
        description.setText(rat.getDescription());
        price.setText("₹"+rat.getPrice());
        return convertView;
    }
}
