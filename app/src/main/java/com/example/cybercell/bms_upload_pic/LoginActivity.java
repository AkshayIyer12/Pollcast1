package com.example.cybercell.bms_upload_pic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cybercell.bms_upload_pic.Model.LoginData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    RestClient restClient;
    @BindView(R2.id.txt_userid) EditText txtuserid;
    @BindView(R2.id.txt_password)   EditText txtpass;

    @BindView(R2.id.btn_login)
    Button submit;

    public static String  dis_name, dis_id;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Toast.makeText(getApplicationContext(), "Data entered: " + txtuserid, Toast.LENGTH_LONG).show();
        ButterKnife.bind(this);
        Log.d(TAG,"Input!!!!!");
        restClient = new RestClient();

        session=new SessionManagement(getApplicationContext());
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(TAG,"submit clicked");
                chkuser(view);
            }
        });

        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
    }
    public void chkuser(View view) {

        if ((txtuserid.getText().toString().equals("")) && (txtpass.getText().toString().equals(""))) {
            Toast.makeText(getApplicationContext(), "Sorry!!! Please enter UserID and Password", Toast.LENGTH_SHORT).show();

        } else {
            //Toast.makeText(getApplicationContext(), "Data entered: " + txtuserid, Toast.LENGTH_LONG).show();
            restClient.getService().getuserdetails(txtuserid.getText().toString(), txtpass.getText().toString(), new Callback<List<LoginData>>() {

                @Override
                public void success(List<LoginData> basics, Response response) {
                    // for (int i = 0; i < basics.size(); i++) {
                    if(basics.size()>0) {
                        dis_name = basics.get(0).District_Name_v1;
                        dis_id = basics.get(0).District;
                        session.createLoginSession(dis_id,dis_name);
                        successactivity();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Sorry!!! Please enter correct UserID and Password", Toast.LENGTH_SHORT).show();
                    }
                    //}
                   // senddata(name, acname, mobile, desig, district, pollname);
                }
                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public void successactivity() {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        ProgressDialog.show(LoginActivity.this, "",
                "Authenticating...", true);
        Intent intent=new Intent(this,SuccessActivity.class);
        //progressDialog.getProgress();
        startActivity(intent);
    }
}
