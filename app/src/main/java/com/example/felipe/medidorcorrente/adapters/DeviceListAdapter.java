package com.example.felipe.medidorcorrente.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.felipe.medidorcorrente.R;
import com.example.felipe.medidorcorrente.model.Device;
import com.github.mikephil.charting.charts.LineChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DeviceListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Device> itens;
    private Context context;


    public DeviceListAdapter(ArrayList<Device> itens, Context context) {

        this.itens = itens;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;

        if (view == null) {

            view = mInflater.inflate(R.layout.device_grafico_list, null);

            itemHolder = new ItemSuporte();
            itemHolder.txtTitle = ((TextView) view.findViewById(R.id.txtTitle));


            view.setTag(itemHolder);
        } else {

            itemHolder = (ItemSuporte) view.getTag();
        }
        Device item = itens.get(position);
        itemHolder.txtTitle.setText(item.getNome());

        return view;
    }

    private class ItemSuporte {

        TextView txtTitle;
    }
}
