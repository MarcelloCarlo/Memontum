package com.example.jcgut.notethunder.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;



public class PermissionControl {
    static boolean permFlag = true;

    public static final String PERMISSION_ARRAY[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Activity activity, int req_perm) {
        for(String permission : PERMISSION_ARRAY) {
            if(activity.checkSelfPermission(permission)!= PackageManager.PERMISSION_GRANTED) {
                permFlag = false;
                break;
            } else {

            }
        }

        if(!permFlag) {
            activity.requestPermissions(PERMISSION_ARRAY, req_perm);
            return false;
        } else {
            return true;
        }
    }

    public static boolean onCheckResult(int[] grantResults) {
        boolean checkResult = true;
        for(int result : grantResults) {
            if(result!=PackageManager.PERMISSION_GRANTED) {
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }
}
