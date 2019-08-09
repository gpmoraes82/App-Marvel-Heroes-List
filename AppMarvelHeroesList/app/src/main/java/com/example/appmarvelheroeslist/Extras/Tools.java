package com.example.appmarvelheroeslist.Extras;

import java.text.SimpleDateFormat;
import java.util.Date;


//---- Class of tools used in the App ----
public class Tools {

    //---- Used to create a Hash needed on the API request
    public static String md5(String md5) {
        try
        {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < array.length; ++i)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch(java.security.NoSuchAlgorithmException e)
        {
        }
        return null;
    }

    //---- Used to create a Hash needed on the API request
    public static String getTimestamp() {

        SimpleDateFormat my_date = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = my_date.format(new Date());

        return timestamp;
    }


}
