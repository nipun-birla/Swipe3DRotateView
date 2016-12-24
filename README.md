<h1>Swipe3DRotateView</h1>

<h2>Purpose</h2>
Swipe3DRotateView is a class designed to simplify the implementation of 3D Flip Rotation in android based on swipe gestures.

Swipe3DRotateView is extended from a Framelayout and should exactly contain 2 views as its children. It detects swipe gestures on its child view, and based on the swipe direction, rotates the children in a 3D manner on their X or Y axis.

<h2>Usage</h2>

Put Swipe3DRotateView in your layout as required :

 <com.nipunbirla.swipe3drotateview.Swipe3DRotateView
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


