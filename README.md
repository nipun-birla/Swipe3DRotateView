Swipe3DRotateView [![Screenshot](https://img.shields.io/badge/Android%20Arsenal-Swipe3DRotateView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5572)

![Screenshot](https://cloud.githubusercontent.com/assets/7312366/21467583/d5905664-ca18-11e6-898c-8b8b2ca6d03a.gif)

<h2>Purpose</h2>
Swipe3DRotateView is a class designed to simplify the implementation of 3D Flip Rotation in android based on swipe gestures.

Swipe3DRotateView is extended from a Framelayout and should exactly contain 2 views as its children. It detects swipe gestures on its child view, and based on the swipe direction, rotates the children in a 3D manner on their X or Y axis.

<h2>Usage</h2>

Add dependency in your build.gradle(app)

    dependencies {
        compile 'com.github.nipun-birla:Swipe3DRotateView:0.0.1'
    }

Put Swipe3DRotateView in your layout as required :

    <com.nipunbirla.swipe3drotateview.Swipe3DRotateView
        android:id="@+id/rotateView"
        android:layout_height="match_parent"
        android:layout_width="match_parent">

        //Put the backview as first child
        <FrameLayout
            android:layout_width="300dp"
            android:layout_height="300dp"
            android:layout_gravity="center"
            android:background="#934567">
        </FrameLayout>

        //Put the frontview as second child
        <FrameLayout
            android:layout_gravity="center"
            android:layout_width="300dp"
            android:layout_height="300dp"
            android:background="#134567">
        </FrameLayout>

    </com.nipunbirla.swipe3drotateview.Swipe3DRotateView>

Find view in your activity as :

    Swipe3DRotateView swipe3DRotateView = findViewById(R.id.rotateView);

To set if X Rotation is allowed :

    swipe3DRotateView.setIsXRotationAllowed(true);

To set if Y Rotation is allowed :

    swipe3DRotateView.setIsYRotationAllowed(true);

To check if X Rotation is allowed :

    boolean isXAllowed = swipe3DRotateView.isXRotationAllowed();

To check if Y Rotation is allowed :

    boolean isYAllowed = swipe3DRotateView.isYRotationAllowed();

To set Animation duration, default value is 1000 millis

    swipe3DRotateView.setAnimationDuration(1000);

To set animation listener on view up front, use :

    swipe3DRotateView.setHalfAnimationCompleteListener(halfListener);

To set animation listener on view at back, use :

    swipe3DRotateView.setCompleteAnimationCompleteListener(fullListener);




