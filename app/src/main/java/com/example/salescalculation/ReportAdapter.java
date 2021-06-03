package com.example.salescalculation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ReportAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final ArrayList<String> reports;


    public ReportAdapter(@NonNull Context context, ArrayList<String> reports) {
        super(context, R.layout.list,reports);
        this.context = context;
        this.reports = reports;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list,parent,false);

        TextView tvReport = rowView.findViewById(R.id.tvReport);

        tvReport.setText(reports.get(position));

        return rowView;
    }
}
