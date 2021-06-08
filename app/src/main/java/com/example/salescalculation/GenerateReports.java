package com.example.salescalculation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateReports extends AppCompatActivity {

    EditText etListDate;
    Button btnSubmit;
    ListView listView;
    Calendar calendar;
    int year,month,dayOfMonth;
    DatePickerDialog datePickerDialog;
    DatabaseReference report;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_reports);

        etListDate = findViewById(R.id.etListDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        listView = findViewById(R.id.listView);

        final ArrayList<String> report_list = new ArrayList<>();
        //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(GenerateReports.this,R.layout.list,report_list);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etListDate.getText().toString().isEmpty()){
                    Toast.makeText(GenerateReports.this,"Please enter date to generate report",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress = new ProgressDialog(GenerateReports.this);
                    progress.setTitle("Retrieving Bill Details");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.show();
                    report = FirebaseDatabase.getInstance().getReference().child("Bill Details");
                    report.child(etListDate.getText().toString().trim()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                int total8mm=0,total10mm=0,total12mm=0,total16mm=0,total20mm=0;
                                double totalamount=0;
                                Toast.makeText(GenerateReports.this,"Retrieving Bill Details",Toast.LENGTH_SHORT).show();
                                report_list.clear();
                                report_list.add("-----------BILL DETAILS-----------"+etListDate.getText().toString().trim());
                                report_list.add("DATE\t\t\tNUM\t\t\t8\t\t\t10\t\t\t12\t\t\t16\t\t\t20\t\t\t\tTOTAL");
                                for (DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    SteelQuantity qty = snapshot1.getValue(SteelQuantity.class);
                                    String txt = qty.getBillDate()+" : "+qty.getBillNum()+"\t\t\t"+qty.getLength8mm()+"\t\t\t"+qty.getLength10mm()+"\t\t\t"
                                            +qty.getLength12mm()+"\t\t"+qty.getLength16mm()+"\t\t"+qty.getLength20mm()+"\t\t\t"+qty.getBillAmount();
                                    total8mm += qty.getLength8mm();
                                    total10mm += qty.getLength10mm();
                                    total12mm += qty.getLength12mm();
                                    total16mm += qty.getLength16mm();
                                    total20mm += qty.getLength20mm();
                                    totalamount += qty.getBillAmount();
                                    report_list.add(txt);
                                }
                                String finaltxt = "Total Quantity : \t"+total8mm+"\t\t\t"+total10mm+"\t\t\t"+total12mm+"\t\t\t"
                                        +total16mm+"\t\t\t"+total20mm+"\t\t\t"+totalamount;
                                report_list.add(finaltxt);
                                double finalTotal8mm = (total8mm*4.74);
                                double finalTotal10mm = (total10mm*7.4);
                                double finalTotal12mm = (total12mm*10.66);
                                double finalTotal16mm = (total16mm*18.96);
                                double finalTotal20mm = (total20mm*29.62);
                                double finalTotalamount = (finalTotal8mm+finalTotal10mm+finalTotal12mm+finalTotal16mm+finalTotal20mm);
                                String finalweights = "\t\t\t\t\t8MM : " + finalTotal8mm + "\n\n\t\t\t\t\t10MM: " + finalTotal10mm + "\n\n\t\t\t\t\t12MM : "
                                                        + finalTotal12mm + "\n\n\t\t\t\t\t16MM : " + finalTotal16mm + "\n\n\t\t\t\t\t20MM : " + finalTotal20mm +
                                                        "\n\n\t\t\t\t\tTotal Weights : " + finalTotalamount;
                                report_list.add(finalweights);
                                ReportAdapter reportAdapter = new ReportAdapter(GenerateReports.this,report_list);
                                listView.setAdapter(reportAdapter);
                                reportAdapter.notifyDataSetChanged();
                                progress.dismiss();
                            }
                            else
                            {
                                Toast.makeText(GenerateReports.this,"Please Enter a valid Bill Date",Toast.LENGTH_SHORT).show();
                                etListDate.getText().clear();
                                progress.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        etListDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(GenerateReports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etListDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();
                report_list.clear();
                etListDate.getText().clear();
                ReportAdapter reportAdapter = new ReportAdapter(GenerateReports.this,report_list);
                listView.setAdapter(reportAdapter);
                reportAdapter.notifyDataSetChanged();
            }
        });
    }

}