package com.hxy.ashmemtest;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.system.ErrnoException;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class MemoryService extends Service {
    private static final String SHMEM_FILE_NAME = "shared_memory";
    private static final int SHMEM_FILE_SIZE = 1024;
    public MemoryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MemoryServer();
    }
    static class MemoryServer extends IMemoryAidl.Stub {
        @Override
        public SharedMemory getSharedMemory() throws RemoteException {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                SharedMemory sharedMemory = null;
                try {
                    sharedMemory = SharedMemory.create(SHMEM_FILE_NAME, SHMEM_FILE_SIZE);
                    ByteBuffer map = sharedMemory.mapReadWrite();
                    map.putInt(1314520);
                } catch (ErrnoException e) {
                    e.printStackTrace();
                }
                return sharedMemory;
            }
            return null;
        }

        @Override
        public ParcelFileDescriptor getFileDescriptor() throws RemoteException {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
                try {
                    MemoryFile memoryFile = new MemoryFile(SHMEM_FILE_NAME,SHMEM_FILE_SIZE);
                    memoryFile.getOutputStream().write(new byte[]{1,2,3,4,5});
                    Method method = MemoryFile.class.getDeclaredMethod("getFileDescriptor");
                    FileDescriptor fileDescriptor = (FileDescriptor)method.invoke(memoryFile);
                    return ParcelFileDescriptor.dup(fileDescriptor);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {

            }
            return null;
        }
    }
}
