package edu.ewubd.cse489_2021_2_60_041;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomItemAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private ArrayList<Item> records;
    public CustomItemAdapter(Context context, ArrayList<Item> records){
        super(context, -1, records);
        this.records = records;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View template = inflater.inflate(R.layout.row_item, parent, false);

        TextView tvSN = template.findViewById(R.id.tvSN);
        TextView tvDate = template.findViewById(R.id.tvDate);
        TextView tvItemName = template.findViewById(R.id.tvItemName);
        TextView tvCost = template.findViewById(R.id.tvCost);

        tvSN.setText(String.valueOf(position+1));
        tvDate.setText(getFormattedDate(records.get(position).date));
        tvItemName.setText(records.get(position).itemName);
        tvCost.setText(String.valueOf(records.get(position).cost));

        return template;
    }
    private String getFormattedDate(long milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(milliseconds);
        return formatter.format(date);
    }
}
