package com.example.app_user.Item_dir;

public class MapPoint {
    private boolean gps_flag;

    public MapPoint(boolean gps_flag){
        this.gps_flag = gps_flag;
    }

    public boolean isGps_flag() {
        return gps_flag;
    }

    public void setGps_flag(boolean gps_flag) {
        this.gps_flag = gps_flag;
    }
}
