package com.example.salescalculation;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView tvWeight, tv8mm, tv10mm, tv12mm, tv16mm, tv20mm,tvResult,tvCalculate;
    EditText wt8, wt10, wt12, wt16, wt20, date;
    LinearLayout call1,call2,call3,call4,call5,llv1;
    Button btnLoad;
    final int result=1;
    double final1length8,final1length10,final1length12,final1length16,final1length20,wei8,wei10,wei12,wei16,wei20,total;
    String date1;
    //CheckBox cb8, cb10, cb12, cb16, cb20;
    ImageView ivCement,ivSteel,ivCal,ivReport;
    DatabaseReference refer;
    ProgressBar progressBar2;


    public void hideVisibility()
    {
        call1.setVisibility(View.GONE);
        call2.setVisibility(View.GONE);
        call3.setVisibility(View.GONE);
        call4.setVisibility(View.GONE);
        call5.setVisibility(View.GONE);
        llv1.setVisibility(View.GONE);
        tvResult.setVisibility(View.GONE);
    }

    public void changeVisibility()
    {
        call1.setVisibility(View.VISIBLE);
        call2.setVisibility(View.VISIBLE);
        call3.setVisibility(View.VISIBLE);
        call4.setVisibility(View.VISIBLE);
        call5.setVisibility(View.VISIBLE);
        llv1.setVisibility(View.VISIBLE);
        tvResult.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==result)
        {
            if(resultCode == RESULT_OK)
            {
                call1.setVisibility(View.GONE);
                call2.setVisibility(View.GONE);
                call3.setVisibility(View.GONE);
                call4.setVisibility(View.GONE);
                call5.setVisibility(View.GONE);
                llv1.setVisibility(View.GONE);

                final1length8 = data.getDoubleExtra("len8",0);
                final1length10 = data.getDoubleExtra("len10",0);
                final1length12 = data.getDoubleExtra("len12",0);
                final1length16 = data.getDoubleExtra("len16",0);
                final1length20 = data.getDoubleExtra("len20",0);
                total = data.getDoubleExtra("total",0);
                date1 = data.getStringExtra("date1");

                tvResult.setText("Date : "+date1+"\n"+"\n"+
                        "8 MM : "+wei8+" * "+(int)(final1length8/wei8)+" = "+final1length8+"\n\n"+
                        "10 MM : "+wei10+" * "+(int)(final1length10/wei10)+" = "+final1length10+"\n\n"+
                        "12 MM : "+wei12+" * "+(int)(final1length12/wei12)+" = "+final1length12+"\n\n"+
                        "16 MM : "+wei16+" * "+(int)(final1length16/wei16)+" = "+final1length16+"\n\n"+
                        "20 MM : "+wei20+" * "+(int)(final1length20/wei20)+" = "+final1length20+"\n\n"+
                        "Total Weight === "+(final1length8+final1length10+final1length12+final1length16+final1length20)+"\n\n"+
                        "Total Amount ===== "+total);

            }
            else
            {
                hideVisibility();
                ivCement.setVisibility(View.VISIBLE);
                ivSteel.setVisibility(View.VISIBLE);
                ivReport.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv8mm = findViewById(R.id.ctv8mm);
        tv10mm =findViewById(R.id.ctv10mm);
        tv12mm = findViewById(R.id.ctv12mm);
        tv16mm = findViewById(R.id.ctv16mm);
        tv20mm = findViewById(R.id.ctv20mm);
        tvResult = findViewById(R.id.tvResult);
        ivCement = findViewById(R.id.ivCement);
        tvCalculate = findViewById(R.id.tvCalculate);
        ivSteel = findViewById(R.id.ivSteel);
        ivReport =findViewById(R.id.ivReport);

        wt8 =findViewById(R.id.cwt8);
        wt10 = findViewById(R.id.cwt10);
        wt12 =findViewById(R.id.cwt12);
        wt16 = findViewById(R.id.cwt16);
        wt20 = findViewById(R.id.cwt20);
        ivCal = findViewById(R.id.ivCal);
        call1 = findViewById(R.id.call1);
        call2 = findViewById(R.id.call2);
        call3 = findViewById(R.id.call3);
        call4 = findViewById(R.id.call4);
        call5 = findViewById(R.id.call5);
        llv1 = findViewById(R.id.llv1);
        progressBar2 = findViewById(R.id.progressBar2);

        /*cb8 = findViewById(R.id.cb8);
        cb10 = findViewById(R.id.cb10);
        cb12 = findViewById(R.id.cb12);
        cb16 = findViewById(R.id.cb16);
        cb20 = findViewById(R.id.cb20);*/
        btnLoad = findViewById(R.id.btnLoad);

        hideVisibility();


        Toast.makeText(MainActivity.this, "Firebase Connection successful", Toast.LENGTH_SHORT).show();

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar2.setVisibility(View.VISIBLE);
                refer = FirebaseDatabase.getInstance().getReference().child("SteelWeights").child("TATA STEEL");
                refer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String weight8mm = snapshot.child("weight8mm").getValue().toString();
                        String weight10mm = snapshot.child("weight10mm").getValue().toString();
                        String weight12mm = snapshot.child("weight12mm").getValue().toString();
                        String weight16mm = snapshot.child("weight16mm").getValue().toString();
                        String weight20mm = snapshot.child("weight20mm").getValue().toString();

                        wt8.setText(weight8mm);
                        wt10.setText(weight10mm);
                        wt12.setText(weight12mm);
                        wt16.setText(weight16mm);
                        wt20.setText(weight20mm);
                        Toast.makeText(MainActivity.this,"Data Loaded Successfully!!",Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        ivSteel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (ivCement.getVisibility() == View.VISIBLE)
                {
                    ivCement.setVisibility(View.GONE);
                    ivReport.setVisibility(View.GONE);
                    progressBar2.setVisibility(View.GONE);
                    changeVisibility();

                    /*
                    cb8.setChecked(false);
                    cb10.setChecked(false);
                    cb12.setChecked(false);
                    cb16.setChecked(false);
                    cb20.setChecked(false);
                    */

                    wt8.getText().clear();
                    wt10.getText().clear();
                    wt12.getText().clear();
                    wt16.getText().clear();
                    wt20.getText().clear();
                    tvResult.setText("Date : "+"\n"+"\n"+
                            "8 MM : "+"\n\n"+
                            "10 MM : "+"\n\n"+
                            "12 MM : "+"\n\n"+
                            "16 MM : "+"\n\n"+
                            "20 MM : "+"\n\n"+
                            "Total Quantity === "+"\n\n"+
                            "Total Amount === ");
                }
                else
                {
                    ivCement.setVisibility(View.VISIBLE);
                    ivReport.setVisibility(View.VISIBLE);
                    hideVisibility();
                }

            }
        });

        ivCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wt8.getText().toString().isEmpty() || wt10.getText().toString().isEmpty() || wt12.getText().toString().isEmpty()
                    || wt16.getText().toString().isEmpty() || wt20.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter all fields!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    wei8 = Double.parseDouble(wt8.getText().toString().trim());
                    wei10 = Double.parseDouble(wt10.getText().toString().trim());
                    wei12 = Double.parseDouble(wt12.getText().toString().trim());
                    wei16 = Double.parseDouble(wt16.getText().toString().trim());
                    wei20 = Double.parseDouble(wt20.getText().toString().trim());

                    SteelWeights steel = new SteelWeights();
                    refer = FirebaseDatabase.getInstance().getReference().child("SteelWeights");

                    steel.setWeight8mm(wei8);
                    steel.setWeight10mm(wei10);
                    steel.setWeight12mm(wei12);
                    steel.setWeight16mm(wei16);
                    steel.setWeight20mm(wei20);

                    refer.child("TATA STEEL").setValue(steel);

                    Intent intent = new Intent(MainActivity.this,
                            Calculation.class);
                    intent.putExtra("weight8",wei8);
                    intent.putExtra("weight10",wei10);
                    intent.putExtra("weight12",wei12);
                    intent.putExtra("weight16",wei16);
                    intent.putExtra("weight20",wei20);
                    startActivityForResult(intent,result);
                }
            }
        });

        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent report_intent = new Intent(MainActivity.this,GenerateReports.class);
                startActivity(report_intent);
            }
        });


        /*
        cb8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked)
                {
                    wt8.setText(R.string.initialwt8);
                }
                else
                {
                    wt8.getText().clear();
                }
            }
        });


        cb10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked)
                {
                    wt10.setText(R.string.initialwt10);
                }
                else
                {
                    wt10.getText().clear();
                }
            }
        });

        cb12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked)
                {
                    wt12.setText(R.string.initialwt12);
                }
                else
                {
                    wt12.getText().clear();
                }
            }
        });

        cb16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked)
                {
                    wt16.setText(R.string.initialwt16);
                }
                else
                {
                    wt16.getText().clear();
                }
            }
        });

        cb20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if (checked)
                {
                    wt20.setText(R.string.initialwt20);
                }
                else
                {
                    wt20.getText().clear();
                }
            }
        });
        */

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (ivCement.getVisibility() == View.VISIBLE && ivSteel.getVisibility() == View.VISIBLE)
        {
            finish();
        }
        else
        {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}