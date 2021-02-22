package com.example.camerastudy;

import com.example.camerastudy.slice.CameraViewSlice;
import com.example.camerastudy.slice.EmptySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.IBundleManager;
import ohos.security.SystemPermission;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(EmptySlice.class.getName());
        if (verifySelfPermission(SystemPermission.CAMERA) == IBundleManager.PERMISSION_GRANTED && verifySelfPermission(SystemPermission.MICROPHONE) == IBundleManager.PERMISSION_GRANTED) {
            super.setMainRoute(CameraViewSlice.class.getName());
        } else {
            if (canRequestPermission(SystemPermission.CAMERA) || canRequestPermission(SystemPermission.MICROPHONE)) {
                requestPermissionsFromUser(new String[]{SystemPermission.CAMERA, SystemPermission.MICROPHONE}, 1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsFromUserResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsFromUserResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            if (grantResult != IBundleManager.PERMISSION_GRANTED) {
                isAllGranted = false;
                break;
            }
        }
        if (isAllGranted) {
            Operation operation = new Intent.OperationBuilder()
                    .withDeviceId("")
                    .withBundleName("com.example.camerastudy")
                    .withAbilityName("com.example.camerastudy.CameraViewAbility")
                    .build();

            Intent intent = new Intent();
            intent.setOperation(operation);
            intent.setParam("age", 10);
            setAbilitySliceAnimator(null);
            startAbility(intent);
            terminateAbility();
        } else {
            new ToastDialog(getContext()).setText("相机或麦克风权限未获授权，应用即将关闭").show();
            terminateAbility();
        }
    }
}
