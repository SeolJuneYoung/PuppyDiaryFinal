package org.techtown.puppydiary.network.Response;

import java.util.List;

public class ShowKgMonthResponse {


    private int status;
    private boolean success;
    private String message;
    private String data;


    public int getStatus(){
        return status;
    }

    public boolean getSuccess(){
        return success;
    }

    public String getMessage(){
        return message;
    }

    public String getData() {
        return data;
    }



}
