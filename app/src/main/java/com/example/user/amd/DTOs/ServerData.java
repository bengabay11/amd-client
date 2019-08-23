package com.example.user.amd.DTOs;

import com.example.user.amd.enums.ServerDataType;

public class ServerData {
    public ServerDataType dataType;
    public String data;

    public ServerData(ServerDataType dataType, String data) {
        this.dataType = dataType;
        this.data = data;
    }
}
