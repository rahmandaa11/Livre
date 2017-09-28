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

public class AdapterMatch extends ArrayAdapter<ModalMatch> {
    private Activity context;
    private List<ModalMatch> matches;

    public AdapterMatch (Activity context, List<ModalMatch> matches){

        super(context, R.layout.region_list_layout, matches);
        this.context = context;
        this.matches = matches;
    } //tutorial 2

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View ListViewItem = inflater.inflate(R.layout.match_list_item, null, true);

        TextView textViewMatch = (TextView) ListViewItem.findViewById(R.id.namaMatch);

        ModalMatch match = matches.get(position);

        textViewMatch.setText(match.getTeam1()+" vs "+match.getTeam2());
        return ListViewItem;
    } //tutorial 2
}
