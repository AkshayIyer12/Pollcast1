package com.example.cybercell.bms_upload_pic;

import com.example.cybercell.bms_upload_pic.Model.Assembly;
import com.example.cybercell.bms_upload_pic.Model.LoginData;
import com.example.cybercell.bms_upload_pic.Model.PollingStation;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Tan on 8/10/2015.
 */
public interface BasicService {

    //So these are the list available in our WEB API and the methods look straight forward
    @GET("/GetUserDetails")
    public void getuserdetails(@Query("userid") String uid, @Query("password") String pass, Callback<List<LoginData>> callback);
//    @GET("/GetDistrictJSON")
//    public void getArrayWithObjects(Callback<List<Basic>> callback);
      @GET("/GetAssemblyJSON")
      public void getassemblywithdistrict(@Query("dis_no") String dis, Callback<List<Assembly>> callback);
    @GET("/GetPSJSON")
    public void getpollingstation(@Query("dis_id") String dis,@Query("ass_id") String assemb, Callback<List<PollingStation>> callback);
//    @GET("/GetRecordJSON")
//    public void getalldata(@Query("dis_no") String dis, @Query("ac_no") String assemb, Callback<List<PollData>> callback);
//    @GET("/GetdAllIntentDataJSON")
//    public void bindvalues(@Query("selectrow") String r, @Query("dis_no") String dis, @Query("ac_no") String assemb, Callback<List<PollData>> callback);
}