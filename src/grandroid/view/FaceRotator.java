/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grandroid.view;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * 處理畫面旋轉的問題
 */
public class FaceRotator extends Animation implements Animation.AnimationListener {

    private float fromDegrees;
    private float toDegrees;
    private float centerX;
    private float centerY;
    private float depthZ;
    private boolean reverse;
    private Camera camera;
    private RotationFace face;
    private int state;
    FaceRotator secondRotator;

    /**
     * 
     * @param face
     */
    public FaceRotator(RotationFace face) {
        this(face, 0);
    }

    /**
     * 
     * @param face
     * @param state
     */
    protected FaceRotator(RotationFace face, int state) {
        super();
        centerX = face.faceGroup.getWidth() / 2.0f;
        centerY = face.faceGroup.getHeight() / 2.0f;
        depthZ = 310f;
        this.face=face;
        this.state = state;
        this.setDuration(500);
        this.setFillAfter(true);
        if (state == 0) {
            this.fromDegrees = 0;
            this.toDegrees = 90;
            this.reverse = true;
            this.setInterpolator(new AccelerateInterpolator());
            this.setAnimationListener(this);
            secondRotator = new FaceRotator(face, 1);
        } else {
            this.fromDegrees = -90;
            this.toDegrees = 0;
            this.reverse = false;
            this.setInterpolator(new DecelerateInterpolator());
            this.setAnimationListener(this);
        }
    }

    /**
     * 
     * @param width
     * @param height
     * @param parentWidth
     * @param parentHeight
     */
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    /**
     * 
     */
    public void play() {
        face.faceGroup.startAnimation(this);
    }

    /**
     * 
     * @param interpolatedTime
     * @param t
     */
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);

        final Matrix matrix = t.getMatrix();

        camera.save();
        if (reverse) {
            camera.translate(0.0f, 0.0f, depthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, depthZ * (1.0f - interpolatedTime));
        }
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

    /**
     * 
     * @param animation
     */
    public void onAnimationStart(Animation animation) {
    }

    /**
     * 
     * @param animation
     */
    public void onAnimationEnd(Animation animation) {
        //this.setAnimationListener(null);
        if (state == 0) {
            face.faceGroup.post(new SwapViews(face));
        } else {
            face.changeFrame();
            face.afterFaceChange();
            face=null;
        }
    }

    /**
     * 
     * @param animation
     */
    public void onAnimationRepeat(Animation animation) {
    }

    private final class SwapViews implements Runnable {

        private final RotationFace face;

        public SwapViews(RotationFace face) {
            this.face = face;
        }

        public void run() {
            face.activeLayout.setVisibility(View.GONE);
            face.fakeLayout.setVisibility(View.VISIBLE);
            face.fakeLayout.requestFocus();
            secondRotator.play();
        }
    }
}
