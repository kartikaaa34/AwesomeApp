package com.example.awesomeapp.util;

import com.example.awesomeapp.service.Service;

public class Constant {
    public static String token = "563492ad6f917000010000015ab0e83bf23b427c99279c09de726a64";
    public static String BASE_URL = "https://api.pexels.com/v1/";
    public static Service service = Service.retrofit.create(Service.class);
    public static int INTERNET_STATUS = 0;
}
