package com.example.cybercell.bms_upload_pic;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SLNOINPART extends Activity {

    ConnectionClass connectionClass;
    ListView lstpro;
    String proid, proid1, idiom;
    ProgressBar pbbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slnodetails);
        connectionClass = new ConnectionClass();
        lstpro = (ListView) findViewById(R.id.mobile_list);
        proid = "";
        proid1 = "";
        idiom="Success";

        FillList fillList = new FillList();
        fillList.execute("");
    }
        public class FillList extends AsyncTask<String, String, String> {
            String z = "";

            List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

            private int lsttemplate;

            @Override
            protected void onPreExecute() {

                 pbbar.setVisibility(View.VISIBLE);
            }


            @Override
            protected void onPostExecute(String r) {

                // pbbar.setVisibility(View.GONE);
                Toast.makeText(SLNOINPART.this, r, Toast.LENGTH_SHORT).show();

                String[] from = {"A"};
                //String[] from = { "A", "B", "C" };
                int[] views = {R.id.lblproid};
                //int[] views = { R.id.lblproid, R.id.lblproname,R.id.lblprodesc };
                final SimpleAdapter ADA = new SimpleAdapter(SLNOINPART.this,
                        prolist, R.layout.lsttemplate, from,
                        views);
                lstpro.setAdapter(ADA);


                lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        proid1 = (String) obj.get("A");

                        Toast.makeText(getApplicationContext(),"SLNOINPART selected is " + proid1,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SLNOINPART.this, final_activity.class);
                        i.putExtra("proid1", idiom);
                        startActivity(i);
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
                        String query = "select distinct SLNOINPART from Voterdata ORDER BY SLNOINPART ASC";

                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        ArrayList<String> data1 = new ArrayList<String>();
                        while (rs.next()) {
                            Map<String, String> datanum = new HashMap<String, String>();
                            datanum.put("A", rs.getString("SLNOINPART"));
                            prolist.add(datanum);
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
