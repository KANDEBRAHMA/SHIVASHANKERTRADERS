package com.example.salescalculation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Calculation extends AppCompatActivity implements View.OnClickListener {

    TextView tvInfo,tv8,tv10,tv12,tv16,tv20,tvBill,tvTotal,tvAdd,tvSubmit,tvDate,tvNum;
    EditText qty8,qty10,qty12,qty16,qty20,etTotal,etDate,etBill;
    ImageView ivAdd,ivSub,ivCalendar;
    LinearLayout linear1,linear2,linear3,linear4,linear5,linear6,linearDate,linear0;
    int len8=0,len10=0,len12=0,len16=0,len20=0,billNum;
    double total=0;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    int year,month,dayOfMonth;
    String getDate;
    DatabaseReference qty;
    Query query;

    @Override
    public void onClick(View v) {
        Intent intent =new Intent();
        double weight8= getIntent().getDoubleExtra("weight8",0);
        double weight10= getIntent().getDoubleExtra("weight10",0);
        double weight12= getIntent().getDoubleExtra("weight12",0);
        double weight16= getIntent().getDoubleExtra("weight16",0);
        double weight20= getIntent().getDoubleExtra("weight20",0);

        intent.putExtra("len8",len8*weight8);
        intent.putExtra("len10",len10*weight10);
        intent.putExtra("len12",len12*weight12);
        intent.putExtra("len16",len16*weight16);
        intent.putExtra("len20",len20*weight20);
        intent.putExtra("total",total);
        intent.putExtra("date1",getDate);

        setResult(RESULT_OK, intent);
        Calculation.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation2);

        tvInfo = findViewById(R.id.tvInfo);
        tv8 = findViewById(R.id.tv8);
        tv10 = findViewById(R.id.tv10);
        tv12 = findViewById(R.id.tv12);
        tv16 = findViewById(R.id.tv16);
        tv20 = findViewById(R.id.tv20);
        tvBill = findViewById(R.id.tvBill);
        tvTotal = findViewById(R.id.tvTotal);
        tvDate = findViewById(R.id.tvDate);
        etDate = findViewById(R.id.etDate);
        tvNum = findViewById(R.id.tvNum);
        etBill = findViewById(R.id.etBill);

        qty8 = findViewById(R.id.qty8);
        qty10 = findViewById(R.id.qty10);
        qty12 = findViewById(R.id.qty12);
        qty16 = findViewById(R.id.qty16);
        qty20 = findViewById(R.id.qty20);
        etTotal =findViewById(R.id.etTotal);

        ivSub = findViewById(R.id.ivSub);
        ivAdd = findViewById(R.id.ivAdd);
        //ivInsert = findViewById(R.id.ivInsert);
        tvAdd = findViewById(R.id.tvAdd);
        tvSubmit =findViewById(R.id.tvSubmit);
        ivCalendar = findViewById(R.id.ivCalendar);

        linear1 = findViewById(R.id.linear1);
        linear2 = findViewById(R.id.linear2);
        linear3 = findViewById(R.id.linear3);
        linear4 = findViewById(R.id.linear4);
        linear5 = findViewById(R.id.linear5);
        linear6 = findViewById(R.id.linear6);
        linearDate = findViewById(R.id.linearDate);
        linear0 = findViewById(R.id.linear0);

        //hideVisibility();

        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Calculation.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etDate.setText(dayOfMonth+"-"+(month+1)+"-"+year);
                            }
                        },year,month,dayOfMonth);
                datePickerDialog.show();
            }
        });

        /*
        ivInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear1.getVisibility() == View.GONE)
                {
                    changeVisibility();
                    ivInsert.setImageResource(R.drawable.arrow_drop_up);
                }
                else
                {
                    hideVisibility();
                    ivInsert.setImageResource(R.drawable.arrow_drop_down);
                }


            }
        });
         */

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (etTotal.getText().toString().isEmpty() || etBill.getText().toString().isEmpty() || etDate.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Needed Information!!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    len8 += (qty8.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty8.getText().toString().trim());
                    len10 += (qty10.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty10.getText().toString().trim());
                    len12 += (qty12.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty12.getText().toString().trim());
                    len16 += (qty16.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty16.getText().toString().trim());
                    len20 += (qty20.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty20.getText().toString().trim());
                    total += (etTotal.getText().toString().isEmpty()) ? 0 : Double.parseDouble(etTotal.getText().toString().trim());
                    getDate = etDate.getText().toString().trim();
                    billNum = Integer.parseInt(etBill.getText().toString().trim());

                    SteelQuantity steelQty = new SteelQuantity();
                    qty = FirebaseDatabase.getInstance().getReference().child("Bill Details");

                    steelQty.setBillDate(getDate);
                    steelQty.setLength8mm((qty8.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty8.getText().toString().trim()));
                    steelQty.setLength10mm((qty10.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty10.getText().toString().trim()));
                    steelQty.setLength12mm((qty12.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty12.getText().toString().trim()));
                    steelQty.setLength16mm((qty16.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty16.getText().toString().trim()));
                    steelQty.setLength20mm((qty20.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty20.getText().toString().trim()));
                    steelQty.setBillAmount((etTotal.getText().toString().isEmpty()) ? 0 : Double.parseDouble(etTotal.getText().toString().trim()));
                    steelQty.setBillNum(billNum);

                    qty.child(getDate).child(String.valueOf(billNum)).equalTo(String.valueOf(billNum)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()){
                                qty.child(getDate).child(String.valueOf(billNum)).setValue(steelQty);
                            }
                            else
                            {
                               qty.child(getDate).setValue(steelQty);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    tvBill.setText("Product"+"\t"+"Entered Quantity"+"\t"+"Total"+"\n"+
                            "8MM :"+"\t\t\t\t"+((qty8.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty8.getText().toString().trim()))+"\t\t\t\t"+len8+"\n"+
                            "10MM :"+"\t\t\t"+((qty10.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty10.getText().toString().trim()))+"\t\t\t\t"+len10+"\n"+
                            "12MM :"+"\t\t\t"+((qty12.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty12.getText().toString().trim()))+"\t\t\t\t"+len12+"\n"+
                            "16MM :"+"\t\t\t"+((qty16.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty16.getText().toString().trim()))+"\t\t\t\t"+len16+"\n"+
                            "20MM :"+"\t\t\t"+((qty20.getText().toString().isEmpty()) ? 0 : Integer.parseInt(qty20.getText().toString().trim()))+"\t\t\t\t"+len20+"\n"+
                            "Total :"+"\t\t\t"+((etTotal.getText().toString().isEmpty()) ? 0 : Double.parseDouble(etTotal.getText().toString().trim()))+"\t\t\t\t"+total+"\n");

                    qty8.getText().clear();
                    qty10.getText().clear();
                    qty12.getText().clear();
                    qty16.getText().clear();
                    qty20.getText().clear();
                    etTotal.getText().clear();
                    etDate.getText().clear();
                    etBill.getText().clear();

                    //hideVisibility();
                    //ivInsert.setImageResource(R.drawable.arrow_drop_down);
                }

            }
        });

        ivSub.setOnClickListener(this);
    }
}