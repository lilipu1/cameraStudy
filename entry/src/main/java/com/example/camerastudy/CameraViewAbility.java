package com.example.camerastudy;

import com.example.camerastudy.slice.CameraViewSlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class CameraViewAbility extends Ability {

    @Override
    protected void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(CameraViewSlice.class.getName());
    }
}
