package com.example.camerastudy.component;

import ohos.agp.components.AttrSet;
import ohos.agp.components.StackLayout;
import ohos.agp.components.surfaceprovider.SurfaceProvider;
import ohos.agp.graphics.Surface;
import ohos.agp.graphics.SurfaceOps;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.camera.CameraKit;
import ohos.media.camera.device.Camera;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.CameraStateCallback;
import ohos.media.camera.device.FrameConfig;
import ohos.media.image.Image;
import ohos.media.image.ImageReceiver;

import static ohos.agp.components.ComponentContainer.LayoutConfig.MATCH_PARENT;
import static ohos.media.camera.device.Camera.FrameConfigType.FRAME_CONFIG_PREVIEW;

public class CameraView extends StackLayout {

    private static final HiLogLabel LOG_LABEL = new HiLogLabel(HiLog.DEBUG, 3, "CameraView");

    private static final String CAMERA_RUNNER_NAME = "cameraRunner";

    private SurfaceProvider surfaceProvider;

    private Surface previewSurface;

    private Camera mCamera;


    public CameraView(Context context) {
        super(context);
        initView();
    }

    public CameraView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initView();
    }

    public CameraView(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initView();
    }

    private void initView() {
        surfaceProvider = new SurfaceProvider(getContext());
        addComponent(surfaceProvider, MATCH_PARENT, MATCH_PARENT);
    }

    public void startPreview() {
        surfaceProvider.getSurfaceOps().get().addCallback(new SurfaceOps.Callback() {
            @Override
            public void surfaceCreated(SurfaceOps surfaceOps) {
                previewSurface = surfaceOps.getSurface();
                createCamera();
            }

            @Override
            public void surfaceChanged(SurfaceOps surfaceOps, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceOps surfaceOps) {

            }
        });
    }

    public void takePicture() {
    }

    private void createCamera() {
        CameraKit cameraKit = CameraKit.getInstance(getContext());
        if (cameraKit == null) {
            debug("cameraKit is null");
            return;
        }

        String[] cameraIds = cameraKit.getCameraIds();
        if (cameraIds.length == 0) {
            debug("No available camera");
            return;
        }

        String cameraId = cameraIds[0];
        cameraKit.createCamera(cameraId, mCameraStateCallback, new EventHandler(EventRunner.create(CAMERA_RUNNER_NAME)));
    }

    private CameraStateCallback mCameraStateCallback = new CameraStateCallback() {

        @Override
        public void onCreated(Camera camera) {
            super.onCreated(camera);
            debug("camera create success");
            mCamera = camera;
            CameraConfig.Builder cameraConfigBuilder = mCamera.getCameraConfigBuilder();
            cameraConfigBuilder.addSurface(previewSurface);
            mCamera.configure(cameraConfigBuilder.build());
        }

        @Override
        public void onCreateFailed(String cameraId, int errorCode) {
            super.onCreateFailed(cameraId, errorCode);
            debug("camera create failed:" + errorCode);
        }

        @Override
        public void onConfigured(Camera camera) {
            super.onConfigured(camera);
            debug("camera configure success");
            FrameConfig.Builder frameConfigBuilder = mCamera.getFrameConfigBuilder(FRAME_CONFIG_PREVIEW);
            frameConfigBuilder.addSurface(previewSurface);
            try {
                mCamera.triggerLoopingCapture(frameConfigBuilder.build());
            } catch (IllegalArgumentException | IllegalStateException e) {
                e.printStackTrace();
                debug("configure error:" + e.getMessage());
            }

        }

        @Override
        public void onConfigureFailed(Camera camera, int errorCode) {
            super.onConfigureFailed(camera, errorCode);
            debug("camera configure fail");
        }

        @Override
        public void onPartialConfigured(Camera camera) {
            super.onPartialConfigured(camera);
            debug("camera partial configured");
        }

        @Override
        public void onCaptureIdle(Camera camera) {
            super.onCaptureIdle(camera);
            debug("camera capture idle");
        }

        @Override
        public void onFatalError(Camera camera, int errorCode) {
            super.onFatalError(camera, errorCode);
            debug("camera fatal error:" + errorCode);
        }

        @Override
        public void onCaptureRun(Camera camera) {
            super.onCaptureRun(camera);
            debug("camera capture run");
        }

        @Override
        public void onReleased(Camera camera) {
            super.onReleased(camera);
            debug("camera released");
        }
    };

    private void debug(String message) {
        HiLog.error(LOG_LABEL, message);
    }
}
