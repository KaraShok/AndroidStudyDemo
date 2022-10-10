package com.karashok.demoglide.glide.manager;

import androidx.fragment.app.Fragment;

import com.karashok.demoglide.glide.RequestManager;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-19-2022
 */
public class SupportRequestManagerFragment extends Fragment {

    ActivityFragmentLifecycle lifecycle;
    RequestManager requestManager;

    public SupportRequestManagerFragment() {
        lifecycle = new ActivityFragmentLifecycle();
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public ActivityFragmentLifecycle getGlideLifecycle() {
        return lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycle.onDestroy();
    }

}
