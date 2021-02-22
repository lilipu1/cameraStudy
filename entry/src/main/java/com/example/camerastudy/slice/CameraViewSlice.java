package com.example.camerastudy.slice;

import com.example.camerastudy.ResourceTable;
import com.example.camerastudy.component.CameraView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;

public class CameraViewSlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_slice_camera_view);

        getWindow().setTransparent(true);

        CameraView cameraView = (CameraView) findComponentById(ResourceTable.Id_cameraView);
        cameraView.startPreview();
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}
