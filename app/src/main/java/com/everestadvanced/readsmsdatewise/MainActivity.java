package com.everestadvanced.readsmsdatewise;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnshowsms;
    ListView listsmsdata;
    public ArrayList sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnshowsms= (Button) findViewById(R.id.showsms);
        listsmsdata= (ListView) findViewById(R.id.smsdata);

        btnshowsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try
                {

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String CurrentDate = sdf.format(new Date(System.currentTimeMillis()));

                    String[] date1 = CurrentDate.split("-");

                    //for get previous date
                    String tempday= String.valueOf(Integer.parseInt(date1[2])-1);
                    String PreviousDate=date1[0].trim()+"-"+date1[1].trim()+"-"+tempday.trim();

//                    String FirstDate="2017-10-30";
//                    String ScondDate="2017-10-31";

                    // Now create a start and end time for this date in order to setup the filter.
                    Date dateStart = formatter.parse(PreviousDate + "T00:00:00");
                    Date dateEnd = formatter.parse(CurrentDate + "T23:59:59");

                    // Now create the filter and query the messages.
                    String filter = "date>=" + dateStart.getTime() + " and date<=" + dateEnd.getTime();


                    final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
                    Cursor cursor =getContentResolver().query(SMS_INBOX, null,filter, null, null);

                    sms = new ArrayList();
                    List<String> items = new ArrayList<String>();

                    //Get all lines
                    if (cursor != null)
                    {
                        cursor.moveToLast();

                        for(int i=0;i<cursor.getCount();i++)
                        {

                            //Gets the SMS information
                            String address = cursor.getString(cursor.getColumnIndex("address"));
                            String person = cursor.getString(cursor.getColumnIndex("person"));
                            String date = cursor.getString(cursor.getColumnIndex("date"));
                            String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
                            String read = cursor.getString(cursor.getColumnIndex("read"));
                            String status = cursor.getString(cursor.getColumnIndex("status"));
                            String type = cursor.getString(cursor.getColumnIndex("type"));
                            String subject = cursor.getString(cursor.getColumnIndex("subject"));
                            String body = cursor.getString(cursor.getColumnIndex("body"));
                            long SmsMessageId =Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                            String phoneNumber = " (" +cursor.getString(cursor.getColumnIndex("address")) + ") ";

                            sms.add(address+"\n"+body);

                            items.add("From : " + phoneNumber + "\n" +
                                    "Date Sent: " +  date + "\n" +
                                    "Message : " + body + "\n");

                            cursor.moveToPrevious();
                        }
                        ListAdapter listAdapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, items);
                        listsmsdata.setAdapter(listAdapter);
                    }

                    //close the cursor
                    cursor.close();

                }
                catch (Exception e)
                {
                    Log.d("Exception file wirt",e.toString());
                }

            }
        });
    }
}
