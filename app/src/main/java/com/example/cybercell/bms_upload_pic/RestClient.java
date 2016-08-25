package com.example.cybercell.bms_upload_pic;

/**
 * Created by CYBERCELL on 01-06-2016.
 */
public class RestClient {
//   private static final String URL = "http://10.135.32.152/demo/DemoService.asmx/";
//    private static final String URL = "http://10.0.3.2:5419/DemoService.asmx/";
    private static final String URL = "http://164.100.180.82/mobpdms/demoservice.asmx/";
   // private static final String URL = "http://10.135.0.61/demoservice/demoservice.asmx/";
//    private static final String URL = "http://instinctcoder.com/wp-content/uploads/2015/08/";
 //private static final String URL = "http://10.0.3.2:40282/Demoservice/DemoService.asmx/";
    private retrofit.RestAdapter restAdapter;
    private BasicService basicService;

    public RestClient()
    {

        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        basicService = restAdapter.create(BasicService.class);
    }

    public BasicService getService()
    {
        return basicService;
    }
}
