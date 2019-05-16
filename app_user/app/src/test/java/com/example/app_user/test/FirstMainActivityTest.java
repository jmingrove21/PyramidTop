package com.example.app_user.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class FirstMainActivityTest {

    @Test
    public void onCreate() {
        System.out.println("onCreate run");
    }

    @Test
    public void onNavigationItemSelected() {
        System.out.println("onNavigationItemSelected run");

    }

    @Test
    public void onCreateOptionsMenu() {
        System.out.println("onCreateOptionsMenu run");

    }

    @Test
    public void onBackPressed() {
        System.out.println("onBackPressed run");

    }

    @Test
    public void store_info_specification() {
        System.out.println("get_store_information run");

    }
}