package com.hxy.ashmemtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind();
    }
    private void bind() {
        Intent intent = new Intent(this, MemoryService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                byte[] count = new byte[10];
                SharedMemory memory = null;
                try {
                    memory = IMemoryAidl.Stub.asInterface(service).getSharedMemory();
                    int num = memory.mapReadWrite().getInt();
                    Log.d("Bruce", "get int num is:" + num);
                } catch (Exception e) {
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Service.BIND_AUTO_CREATE);
    }
}
