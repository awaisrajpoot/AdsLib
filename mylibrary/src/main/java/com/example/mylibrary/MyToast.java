package com.example.mylibrary;

import android.content.Context;
import android.widget.Toast;

public class MyToast {

    Context context;

    public MyToast(Context context){
        this.context = context;
    }

    public void showMyToast(){
        Toast.makeText(context,"just testing toast ...", Toast.LENGTH_SHORT).show();
    }

}
