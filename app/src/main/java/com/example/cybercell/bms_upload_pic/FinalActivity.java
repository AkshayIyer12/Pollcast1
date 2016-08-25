package com.example.cybercell.bms_upload_pic;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class final_activity extends Activity{
    ConnectionClass connectionClass;
    TextView Rlname, name, age1, sex1;
    String proid2, rln_fm_nameEn, fm_nameEn, age, sex;
    ListView lstproder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        connectionClass = new ConnectionClass();
        Rlname = (TextView) findViewById(R.id.FnName1);
        name = (TextView) findViewById(R.id.voter_name1);
        age1 = (TextView) findViewById(R.id.voter_age1);
        sex1 = (TextView) findViewById(R.id.voter_gender1);
        proid2=getIntent().getExtras().getString("proid1");
        NewList newList = new NewList();
        newList.execute("");


    }
    public class NewList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist1 = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            // pbbar.setVisibility(View.VISIBLE);
        }


        @Override
        protected void onPostExecute(String r) {

            // pbbar.setVisibility(View.GONE);
            Toast.makeText(final_activity.this, r, Toast.LENGTH_SHORT).show();

            String[] from = {"B", "C", "D", "E"};
            //String[] from = { "A", "B", "C" };
            int[] views = {R.id.voter_name1, R.id.FnName1, R.id.voter_age1, R.id.voter_gender1};
            //int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc };
            final SimpleAdapter ADA = new SimpleAdapter(final_activity.this,
                    prolist1, R.layout.activity_list, from,
                    views);
            lstproder.setAdapter(ADA);


            lstproder.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    rln_fm_nameEn = (String) obj.get("B");
                    fm_nameEn = (String) obj.get("C");
                    age = (String) obj.get("D");
                    sex = (String) obj.get("E");
                    name.setText(fm_nameEn);
                    age1.setText(age);
                    sex1.setText(sex);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query1 = "SELECT Rln_Fm_NmEn, Fm_NameEn, AGE, SEX  FROM Voterdata  WHERE SLNOINPART = "+ proid2;
                    PreparedStatement ps1 = con.prepareStatement(query1);
                    ResultSet rs1 = ps1.executeQuery();


                    ArrayList<String> data1 = new ArrayList<String>();
                    while(rs1.next()){
                        Map<String, String> datanum2 = new HashMap<String, String>();
                        datanum2.put("B", rs1.getString("Rln_Fm_NmEn"));
                        datanum2.put("C", rs1.getString("Fm_NameEn"));
                        datanum2.put("D",rs1.getString("AGE"));
                        datanum2.put("E",rs1.getString("SEX"));
                        prolist1.add(datanum2);
                    }
                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";

            }
            return z;
        }
    }
}
