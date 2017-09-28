package com.example.rahmanda.livre2;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rahmanda on 9/27/17.
 */

public class AdapterRegion extends ArrayAdapter<ModalRegion>  {
    private Activity context;
    private List<ModalRegion> regions;

    public AdapterRegion (Activity context, List<ModalRegion> regions){

        super(context, R.layout.region_list_layout, regions);
        this.context = context;
        this.regions = regions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.region_list_layout, null, true);

        TextView textViewRegion = (TextView) ListViewItem.findViewById(R.id.namaLocation);

        ModalRegion region = regions.get(position);

        textViewRegion.setText(region.getLocationName());

        return ListViewItem;
    }
}
