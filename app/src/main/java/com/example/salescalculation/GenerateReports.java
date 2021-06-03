package com.example.salescalculation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
    ImageView ivDate;
    Button btnSubmit;
    ListView listView;
    Calendar calendar;
    int year,month,dayOfMonth;
    DatePickerDialog datePickerDialog;
    DatabaseReference report,weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_reports);

        etListDate = findViewById(R.id.etListDate);
        ivDate = findViewById(R.id.ivDate);
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
                                report_list.add("DATE\t\t\tNUM\t\t8\t\t10\t\t12\t\t16\t\t20\t\t\t\tTOTAL");
                                for (DataSnapshot snapshot1 : snapshot.getChildren())
                                {
                                    SteelQuantity qty = snapshot1.getValue(SteelQuantity.class);
                                    String txt = qty.getBillDate()+" : "+qty.getBillNum()+"\t\t"+qty.getLength8mm()+"\t\t"+qty.getLength10mm()+"\t\t"+qty.getLength12mm()+"\t\t"
                                            +qty.getLength16mm()+"\t\t"+qty.getLength20mm()+"\t\t\t"+qty.getBillAmount();
                                    total8mm += qty.getLength8mm();
                                    total10mm += qty.getLength10mm();
                                    total12mm += qty.getLength12mm();
                                    total16mm += qty.getLength16mm();
                                    total20mm += qty.getLength20mm();
                                    totalamount += qty.getBillAmount();
                                    report_list.add(txt);
                                }
                                String finaltxt = "Total Quantity : \t"+total8mm+"\t\t"+total10mm+"\t\t"+total12mm+"\t\t"+total16mm+"\t\t"+total20mm+"\t\t"+totalamount;
                                report_list.add(finaltxt);
                                weight = FirebaseDatabase.getInstance().getReference().child("SteelWeights").child("TATA STEEL");
                                int finalTotal8mm = total8mm;
                                int finalTotal10mm = total10mm;
                                int finalTotal12mm = total12mm;
                                int finalTotal16mm = total16mm;
                                int finalTotal20mm = total20mm;
                                double finalTotalamount = totalamount;
                                weight.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        double finalweight8=0,finalweight10=0,finalweight12=0,finalweight16=0,finalweight20;
                                        if (snapshot.exists())
                                        {
                                            for (DataSnapshot snapweight : snapshot.getChildren())
                                            {
                                                SteelWeights weights = snapweight.getValue(SteelWeights.class);
                                                finalweight8 = weights.getWeight8mm()* finalTotal8mm;
                                                finalweight10 = weights.getWeight10mm()* finalTotal10mm;
                                                finalweight12 = weights.getWeight12mm()* finalTotal12mm;
                                                finalweight16 = weights.getWeight16mm()* finalTotal16mm;
                                                finalweight20 = weights.getWeight20mm()* finalTotal20mm;
                                            }
                                            String finalweights = "Total Weights : \t"+finalTotal8mm+"\t\t"+finalTotal10mm+"\t\t"+finalTotal12mm+"\t\t"+finalTotal16mm+"\t\t"+finalTotal20mm+"\t\t"+ finalTotalamount;
                                            report_list.add(finalweights);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                ReportAdapter reportAdapter = new ReportAdapter(GenerateReports.this,report_list);
                                listView.setAdapter(reportAdapter);
                                reportAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(GenerateReports.this,"Please Enter a valid Bill Date",Toast.LENGTH_SHORT).show();
                                etListDate.getText().clear();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        ivDate.setOnClickListener(new View.OnClickListener() {
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
            }
        });
    }

}