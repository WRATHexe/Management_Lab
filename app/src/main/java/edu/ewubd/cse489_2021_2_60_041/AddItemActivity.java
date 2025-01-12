package edu.ewubd.cse489_2021_2_60_041;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity {

    private EditText etItemName, etCostPrice, etDate;
    private String id = "";
    private final Calendar selectedDate = Calendar.getInstance(); // For storing the selected date

    /*private boolean isValidDate(String date) {
        if (!date.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false;
        }
        String[] parts = date.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        // Check for February( can be leap year)
        if (month == 2) {
            if (isLeapYear(year)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        // Check for months with 30 days
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        else {
            return true;
        }
    }
    private boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = findViewById(R.id.etItemName);
        etCostPrice = findViewById(R.id.etCostPrice);
        etDate = findViewById(R.id.etDate);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnBack = findViewById(R.id.btnBack);

        etDate.setOnClickListener(v -> datePicker());

        Intent i = getIntent();
        if (i != null && i.hasExtra("ID")) {
            id = i.getStringExtra("ID");
            String itemName = i.getStringExtra("ITEM-NAME");
            etItemName.setText(itemName);
            long dateInMilliSeconds = i.getLongExtra("DATE", 0);
            double cost = i.getDoubleExtra("COST", 0);
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(dateInMilliSeconds); // Convert milliseconds to date
            etCostPrice.setText(String.valueOf(cost));
            etDate.setText(date);
        }


        btnAddItem.setOnClickListener(v -> {
            String itemName = etItemName.getText().toString();
            String cost = etCostPrice.getText().toString();
            String date = etDate.getText().toString();

            // Data validation
            if (itemName.length() > 20) {
                Toast.makeText(AddItemActivity.this, "Item name can't exceed 20 letters", Toast.LENGTH_LONG).show();
                return;
            }
            if (!cost.matches("\\d+(\\.\\d+)?")) {
                Toast.makeText(AddItemActivity.this, "Cost should be a number", Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(date)) {
                Toast.makeText(AddItemActivity.this, "Please select a valid date", Toast.LENGTH_LONG).show();
                return;
            }

            /*// Split Date
            String[] dateParts = date.split("-");
            int year = Integer.parseInt(dateParts[2]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Calendar.MONTH is zero-based
            int day = Integer.parseInt(dateParts[0]);

            // if all data are valid, then store
            Calendar currentCal = Calendar.getInstance();
            long currentTime = currentCal.getTimeInMillis();
            currentCal.setTimeInMillis(0);
            currentCal.set(Calendar.YEAR, year);
            currentCal.set(Calendar.MONTH, month);
            currentCal.set(Calendar.DATE, day);*/
            //
            double costValue = Double.parseDouble(cost);
            long dateValue = selectedDate.getTimeInMillis();
            //Store or Update the item
            ItemDB db = new ItemDB(AddItemActivity.this);
            if(id.isEmpty()){
                id = itemName+":"+System.currentTimeMillis();
                db.insertItems(id, itemName, dateValue, costValue);
            } else{
                db.updateItem(id, itemName, dateValue, costValue);
            }

            // Store record to remote Database
            String keys[] = {"action", "sid", "semester", "id", "itemName", "cost", "date"};
            String values[] = {"backup", "2021-2-60-041", "2024-3",id, itemName, String.valueOf(costValue), String.valueOf(dateValue)};
            httpRequest(keys, values);
            db.close();
            finish();
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddItemActivity.this, ReportActivity.class));
            }
        });

    }

    private void httpRequest(final String keys[],final String values[]){
        new AsyncTask <Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {
                List<NameValuePair> params=new ArrayList<NameValuePair >();
                for (int i=0; i<keys.length; i++){
                    params.add(new BasicNameValuePair(keys[i],values[i]));
                }
                String url= "https://www.muthosoft.com/univ/cse489/index.php";
                try {
                    String data=RemoteAccess.getInstance().makeHttpRequest(url,"POST",params);
                    return data;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(String data){
                if(data!=null){
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    // Date picker dialog to select date
    private void datePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddItemActivity.this, // Use AddItemActivity.this for proper context
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth); // Update the Calendar instance
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    etDate.setText(sdf.format(selectedDate.getTime())); // Display formatted date in EditText
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}