package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etNum;
    EditText etPax;
    CheckBox chkNonSmoke;
    Button btnSubmit;
    Button btnReset;
    EditText etDay;
    EditText etTime;
    TimePickerDialog myTimeDialog;
    DatePickerDialog myDateDialog;

    Calendar c = Calendar.getInstance();

    int day = c.get(Calendar.DAY_OF_MONTH);
    int month = c.get(Calendar.MONTH);
    int Year = c.get(Calendar.YEAR);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int Minute = c.get(Calendar.MINUTE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.edName);
        etNum = findViewById(R.id.edMobileNumber);
        etPax = findViewById(R.id.edNumberOfPeople);
        chkNonSmoke = findViewById(R.id.chkNonSmoking);
        btnSubmit = findViewById(R.id.btnReservation);
        btnReset = findViewById(R.id.btnReset);
        etDay = findViewById(R.id.editTextDay);
        etTime = findViewById(R.id.editTextTime);


        etDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        etDay.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                        Year = year;
                        month = monthOfYear;
                        day = dayOfMonth;
                    }
                };

                myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener,Year,month,day);
                myDateDialog.updateDate(Year, month, day);
                myDateDialog.show();

            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                    etTime.setText(hourOfDay + ":" + minute);
                    hour = hourOfDay;
                    Minute = minute;
                }
            };

            Calendar c = Calendar.getInstance();

            myTimeDialog = new TimePickerDialog(MainActivity.this,myTimeListener, hour, Minute,true);
            myTimeDialog.updateTime(hour,Minute);
            myTimeDialog.show();
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //name
                String name = "Name: " + etName.getText().toString();
                //mobile
                String mobile = "Mobile: " + etNum.getText().toString();
                //pax
                String pax = "Pax: " + etPax.getText().toString();

                //area
                String text = "Area: ";
                String area = "";
                if (chkNonSmoke.isChecked()) {
                    area = text + chkNonSmoke.getText().toString();
                }
                else {
                    area = "Smoking Area";
                }
                //date

                String date = etDay.getText().toString();
                String time = etTime.getText().toString();

                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                View viewDialog = inflater.inflate(R.layout.reservation, null);

                final TextView tvName = viewDialog.findViewById(R.id.textViewName);
                final TextView tvSmoking = viewDialog.findViewById(R.id.textViewSmoking);
                final TextView tvSize = viewDialog.findViewById(R.id.textViewSize);
                final TextView tvDate = viewDialog.findViewById(R.id.textViewDate);
                final TextView tvTime = viewDialog.findViewById(R.id.textViewTime);

                tvName.setText(name);
                tvSmoking.setText(area);
                tvSize.setText(pax);
                tvDate.setText("Date: " + date);
                tvTime.setText("Time: " + time);

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                myBuilder.setView(viewDialog);
                myBuilder.setTitle("Confirm Your Order");
                myBuilder.setCancelable(false);

                myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String date = etDay.getText().toString();
                        String time = etTime.getText().toString();
                        tvDate.setText("Date: " + date);
                        tvTime.setText("Time: " + time);
                    }
                });

                myBuilder.setNegativeButton("Cancel",null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText(null);
                etNum.setText(null);
                etPax.setText(null);
                if (chkNonSmoke.isChecked()) {
                    chkNonSmoke.setChecked(false);
                }
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                Year = c.get(Calendar.YEAR);

                hour = c.get(Calendar.HOUR_OF_DAY);
                Minute = c.get(Calendar.MINUTE);





                etTime.setText(c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE));
                etDay.setText(c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String Name = prefs.getString("name","");
        String mNum = prefs.getString("mnum","");
        String pax = prefs.getString("pax","");
        Boolean Smoking = prefs.getBoolean("smoking", false);
        String day = prefs.getString("day", "");
        String time = prefs.getString("time","");

        etName.setText(Name);
        etNum.setText(mNum);
        etPax.setText(pax);
        chkNonSmoke.setChecked(Smoking);
        etDay.setText(day);
        etTime.setText(time);
    }

    @Override
    protected void onPause() {
        super.onPause();

        boolean smoke = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        prefEdit.putString("name", etName.getText().toString());
        prefEdit.putString("mnum", etNum.getText().toString());
        prefEdit.putString("pax", etPax.getText().toString());
        prefEdit.putString("day", etDay.getText().toString());
        prefEdit.putString("time", etTime.getText().toString());

        if(chkNonSmoke.isChecked()){
            smoke = true;
        }
        else{
            smoke = false;
        }
        prefEdit.putBoolean("smoking",smoke);

        prefEdit.commit();


    }
}