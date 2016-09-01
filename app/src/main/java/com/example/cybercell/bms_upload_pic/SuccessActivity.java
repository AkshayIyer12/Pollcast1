package com.example.cybercell.bms_upload_pic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cybercell.bms_upload_pic.Model.Assembly;
import com.example.cybercell.bms_upload_pic.Model.PollingStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SuccessActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RestClient restClient;
    TextView dis;
    String Vdis, assid,ps_id;
    Button btn_get;
    ArrayList<String> ass_id;
    ArrayList<String> ass_name;
    ArrayList<String> psid;
    ArrayList<String> ps_name;
    SessionManagement session;
    int acpos;
    Spinner spinnerassembly, spinnerps;
   // public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        restClient = new RestClient();
        dis=(TextView)findViewById(R.id.txt_dis);
        spinnerassembly = (Spinner) findViewById(R.id.spinassemb);
        spinnerps=(Spinner)findViewById(R.id.spinps);
        btn_get=(Button)findViewById(R.id.btn_chkrep);
        Intent intent=getIntent();
        Bundle extras = intent.getExtras();
        session=new SessionManagement(getApplicationContext());

        // Get district Code from session
        HashMap<String, String> user = session.getDistrict();
        // Get district Code
        Vdis = user.get(SessionManagement.KEY_DISID);
        dis.setText(user.get(SessionManagement.KEY_DISNAME) + " (" + user.get(SessionManagement.KEY_DISID) + ")");
        binddistrictgetassembly(Vdis);
    }

    private void binddistrictgetassembly(String dis) {
        restClient.getService().getassemblywithdistrict(dis, new Callback<List<Assembly>>() {

            @Override
            public void success(List<Assembly> basics, Response response) {
                ass_name = new ArrayList<String>();
                ass_id=new ArrayList<String>();
                int arrsize = basics.size();
               // int arrsize1 = arrsize + 1;
                ass_name.add(0,"SELECT");
                ass_id.add(0,"0");
                for (int i = 0; i < basics.size(); i++) {
                    ass_name.add("("+basics.get(i).AC_NO+") "+basics.get(i).AC_NAME_v1);
                    ass_id.add(basics.get(i).AC_NO);
                }
                bindassmspinner(ass_name);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        spinnerassembly.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.title_menu, menu);
        return true;
    }


    public void getresult(View view)
    {
        Intent intent=new Intent(this,SLNOINPART.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinassem = (Spinner)parent;
        Spinner spinpos = (Spinner)parent;
        if(spinassem.getId() == R.id.spinassemb)
        {
            String pos=ass_id.get(position);
            acpos=position;
            assid=pos;
            bindpollings(Vdis,assid);
//            Toast.makeText(this, "Your choose :" + assid,Toast.LENGTH_SHORT).show();
        }
        else if(spinpos.getId() == R.id.spinps)
        {
            ps_id=psid.get(position);
            if(position>0) {
                session.createACPSSession(assid, ass_name.get(acpos), ps_id, ps_name.get(position));
            }

        }
    }

    private void bindpollings(String dis,String assemb) {
        restClient.getService().getpollingstation(dis,assemb, new Callback<List<PollingStation>>() {

            @Override
            public void success(List<PollingStation> basics, Response response) {
                psid = new ArrayList<String>();
                ps_name=new ArrayList<String>();
                int arrsize = basics.size();
                // int arrsize1 = arrsize + 1;
                ps_name.add(0,"SELECT");
                psid.add(0,"0");
                for (int i = 0; i < basics.size(); i++) {
                    ps_name.add("("+basics.get(i).Part_No +") "+ basics.get(i).Part_Name_v1);
                    psid.add(basics.get(i).Part_No);
                }
                bindpsspinner(ps_name);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        spinnerps.setOnItemSelectedListener(this);
    }

    private void bindpsspinner(ArrayList<String> ps_name) {
        ArrayAdapter<String> spinnerpsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, ps_name);
        spinnerpsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerps.setAdapter(spinnerpsAdapter);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void bindassmspinner(ArrayList list)
    {
        ArrayAdapter<String> spinnerdisAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        spinnerdisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinnerassembly.setAdapter(spinnerdisAdapter);

    }
}
