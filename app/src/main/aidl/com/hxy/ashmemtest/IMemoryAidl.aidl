// IMemoryAidl.aidl
package com.hxy.ashmemtest;

// Declare any non-default types here with import statements

interface IMemoryAidl {

    ParcelFileDescriptor getFileDescriptor();
    SharedMemory getSharedMemory();
}
