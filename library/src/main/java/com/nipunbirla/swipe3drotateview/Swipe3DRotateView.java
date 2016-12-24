package com.nipunbirla.swipe3drotateview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Nipun on 12/24/2016.
 */

public class Swipe3DRotateView extends FrameLayout {

    public static long ANIMATION_DURATION = 500;

    private Context mContext;
    private float lastX, lastY;
    private static final float MINX = 60;
    private static boolean isAnimating;
    private boolean mIsFirstView = true;
    private boolean allowXRotation = true, allowYRotation = true;
    private _3DRotationType mRotationType;
    View frontView,backView;
    Animation.AnimationListener halmAnimation ,completeAnimation;

    public Swipe3DRotateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Swipe3DRotateView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            return;
        }
        mContext = context;
    }

    private boolean isViewInBounds(View view, int x, int y){

        Rect outRect = new Rect();
        view.getHitRect(outRect);
        return outRect.contains(x, y);
    }

    public void setIsXRotationAllowed(boolean isAllowed){
        allowXRotation = isAllowed;
    }

    public boolean isXRotationAllowed(){
        return allowXRotation;
    }

    public void setIsYRotationAllowed(boolean isAllowed){
        allowYRotation = isAllowed;
    }

    public boolean isYRotationAllowed(){
        return allowYRotation;
    }

    public void setAnimationDuration(long durationMillis){
        ANIMATION_DURATION = durationMillis/2;
    }

    public void setHalfAnimationCompleteListener(Animation.AnimationListener listener){
        halmAnimation = listener;
    }

    public void setCompleteAnimationCompleteListener(Animation.AnimationListener listener){
        completeAnimation = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {

        if(getChildCount() != 2){
            throw new RuntimeException("Swipe3DRotateView can only have 2 children");
        }

        if(frontView == null) {
            frontView = getChildAt(1);
        }
        if(backView == null) {
            backView = getChildAt(0);
            backView.setVisibility(GONE);
        }

        int rawx, rawy;
        rawx = (int)touchevent.getX();
        rawy = (int)touchevent.getY();
        switch (touchevent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                lastY = touchevent.getY();
                break;

            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();
                float currentY = touchevent.getY();

                float diffX = currentX - lastX;
                float diffY = currentY - lastY;

                if((Math.abs(diffX) > Math.abs(diffY)) && Math.abs(diffX) > MINX && !isAnimating && allowYRotation) {

                    mRotationType = _3DRotationType.RotateY;

                    // Handling left to right screen swap.
                    if (lastX < currentX) {
                        applyRotation(0, 90);
                    }

                    // Handling right to left screen swap.
                    if (lastX > currentX) {
                        applyRotation(0, -90);
                    }
                    mIsFirstView = !mIsFirstView;
                } else if((Math.abs(diffY) > Math.abs(diffX)) && Math.abs(diffY) > MINX && !isAnimating && allowXRotation) {

                    mRotationType = _3DRotationType.RotateX;

                    if (lastY > currentY) {
                        applyRotation(0, 90);
                    }

                    if (lastY < currentY) {
                        applyRotation(0, -90);
                    }
                    mIsFirstView = !mIsFirstView;
                }
                break;
        }
        return isViewInBounds( mIsFirstView ? frontView : backView ,rawx, rawy);
    }

    private void applyRotation(float start, float end) {

        View v = mIsFirstView ? frontView : backView;

        // Find the center of view
        final float centerX = v.getWidth() / 2.0f;
        final float centerY = v.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3DAnimation rotation =
                new Flip3DAnimation(start, end, centerX, centerY, mRotationType);
        rotation.setDuration(ANIMATION_DURATION);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(mIsFirstView, frontView, backView, end));

        v.startAnimation(rotation);
    }

    private final class DisplayNextView implements Animation.AnimationListener{

        private boolean mCurrentView;
        View v1,v2;
        float end;

        public DisplayNextView(boolean currentView, View v1, View v2, float end) {
            mCurrentView = currentView;
            this.v1 = v1;
            this.v2 = v2;
            this.end = end;
        }

        public void onAnimationStart(Animation animation) {
            isAnimating = true;

            if(halmAnimation != null){
                halmAnimation.onAnimationStart(animation);
            }
        }

        public void onAnimationEnd(Animation animation) {
            v1.post(new SwapViews(mCurrentView, v1, v2, end));
            if(halmAnimation != null){
                halmAnimation.onAnimationEnd(animation);
            }
        }

        public void onAnimationRepeat(Animation animation) {
            if(halmAnimation != null){
                halmAnimation.onAnimationRepeat(animation);
            }
        }
    }

    private final class SwapViews implements Runnable {
        private boolean mIsFirstView;
        View v1, v2;
        float start,end;

        public SwapViews(boolean isFirstView, View v1, View v2, float end) {
            mIsFirstView = isFirstView;
            this.v1 = v1;
            this.v2 = v2;
            this.start = end * -1;
            this.end = 0;
        }

        public void run() {
            final float centerX = v1.getWidth() / 2.0f;
            final float centerY = v1.getHeight() / 2.0f;
            Flip3DAnimation rotation;

            if (mIsFirstView) {
                v1.setVisibility(View.GONE);
                v2.setVisibility(View.VISIBLE);
                v2.bringToFront();

                rotation = new Flip3DAnimation(start, end, centerX, centerY, mRotationType);
            } else {
                v2.setVisibility(View.GONE);
                v1.setVisibility(View.VISIBLE);
                v1.bringToFront();

                rotation = new Flip3DAnimation(start, end, centerX, centerY, mRotationType);
            }

            rotation.setDuration(ANIMATION_DURATION);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if(completeAnimation != null){
                        completeAnimation.onAnimationStart(animation);
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimating = false;
                    if(completeAnimation != null){
                        completeAnimation.onAnimationEnd(animation);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    if(completeAnimation != null){
                        completeAnimation.onAnimationRepeat(animation);
                    }
                }
            });

            if (mIsFirstView) {
                v2.startAnimation(rotation);
            } else {
                v1.startAnimation(rotation);
            }
        }
    }
}
