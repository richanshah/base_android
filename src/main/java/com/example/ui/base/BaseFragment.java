package com.example.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ApplicationClass;
import com.example.R;
import com.example.util.GlideUtils;
import com.example.util.SessionManager;
import com.google.android.material.snackbar.Snackbar;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {

    public Context mContext;
    public Activity mActivity;

    public TextView title;
    public SessionManager session;
    private Snackbar snackbar;

    protected GlideUtils glideUtils;

    private ProgressDialog dialog;
    public setPermissionListener permissionListener;


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mActivity = getActivity();
        session = new SessionManager(mContext);
        glideUtils = new GlideUtils(ApplicationClass.getAppContext());
    }

   /* public void setupToolBar(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        ((BaseActivity) mActivity).setSupportActionBar(toolbar);
        actionBar = ((BaseActivity) mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }



    public void setupToolBarWithBackArrow(Toolbar toolbar) {
        setupToolBarWithBackArrow(toolbar, null);
    }

    public void setupToolBarWithBackArrow(Toolbar toolbar, @Nullable String Title) {
        ActionBar actionBar;

        ((BaseActivity) mActivity).setSupportActionBar(toolbar);
        actionBar = ((BaseActivity) mActivity).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_vector_arrow_back);
        }
        toolbar.setNavigationOnClickListener(view -> mActivity.onBackPressed());
        title = toolbar.findViewById(R.id.tvTitle);
        title.setText(Title != null ? Title : "");
    }
*/
    public void updateToolBarTitle(@Nullable String Title) {
        title.setText(Title != null ? Title : mContext.getResources().getString(R.string.app_name));
    }

    // Set false for Tab Back press management. It will manage from MainActivity
    public boolean onBackPressed() {
        return false;
    }

    public void showShortToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(View view, String msg) {
        showSnackBar(view, msg, Snackbar.LENGTH_SHORT);
    }

    public void showSnackBar(View view, String msg, int LENGTH) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        snackbar.show();
    }

   /* public void showSnackBar(View view, String msg, int LENGTH, String action, final OnSnackBarActionListener actionListener) {
        if (view == null) return;
        snackbar = Snackbar.make(view, msg, LENGTH);
        snackbar.setActionTextColor(ContextCompat.getColor(mContext, R.color.red));
        if (actionListener != null) {
            snackbar.setAction(action, view1 -> {
                snackbar.dismiss();
                actionListener.onAction();
            });
        }
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        snackbar.show();
    }*/

    public void showSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            if (mActivity.getWindow().getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(mActivity.getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ProgressDialog showProgressBar() {
        return showProgressBar(null);
    }

    public ProgressDialog showProgressBar(String message) {
        if (dialog == null) dialog = new ProgressDialog(mContext, message);
        return dialog;
    }

    public void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void requestAppPermissions(final String[] requestedPermissions, final int requestCode,
                                      setPermissionListener listener) {
        this.permissionListener = listener;
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + ContextCompat.checkSelfPermission(mActivity, permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(requestedPermissions, requestCode);
        } else {
            if (permissionListener != null) permissionListener.onPermissionGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (permissionListener != null) permissionListener.onPermissionGranted(requestCode);
        } else {
            if (permissionListener != null) permissionListener.onPermissionDenied(requestCode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
    }

    public interface setPermissionListener {
        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode);
    }

    /*protected void pushFragment(Fragment fragment) {
        if (mActivity instanceof VirtualBankActivity) {
            pushFragment(((VirtualBankActivity) mActivity).mCurrentTab, fragment, true, true);
        } else if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).pushFragment(fragment);
        }
    }

    protected void pushFragment(String tag, Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
        if (mActivity instanceof VirtualBankActivity) {
            ((VirtualBankActivity) mActivity).pushFragment(tag, fragment, shouldAnimate, shouldAdd);
        } *//*else {

        }*//*
    }

    protected void clearBackStackInclusive() {
        if (mActivity instanceof VirtualBankActivity) {
            clearBackStackInclusive(false);
        } else if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).clearBackStackInclusive(true);
        }
    }

    protected void clearBackStackInclusive(boolean isRedirectToHomeTabOrFinishActivity) {
        if (mActivity instanceof VirtualBankActivity) {
            ((VirtualBankActivity) mActivity).clearBackStackInclusive(isRedirectToHomeTabOrFinishActivity);
        } else if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).clearBackStackInclusive(isRedirectToHomeTabOrFinishActivity);
        }
    }

    protected void clearBackStackInclusive(String tag) {
        if (mActivity instanceof VirtualBankActivity) {
            ((VirtualBankActivity) mActivity).clearBackStackInclusive(tag);
        } else if (mActivity instanceof MainActivity) {
            ((MainActivity) mActivity).clearBackStackInclusive(tag);
        }
    }

    protected SharedViewModel getSharedViewModel() {
        if (mActivity instanceof VirtualBankActivity) {
            return ViewModelProviders.of(((VirtualBankActivity) mActivity)).get(SharedViewModel.class);
        } else if (mActivity instanceof MainActivity) {
            return ViewModelProviders.of(((MainActivity) mActivity)).get(SharedViewModel.class);
        } else if (getActivity() != null) {
            return ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        } else {
            return null;
        }
    }

    protected CreateInvoiceSharedViewModel getCreateInvoiceSharedViewModel() {
        if (mActivity instanceof MainActivity) {
            return ViewModelProviders.of(((MainActivity) mActivity)).get(CreateInvoiceSharedViewModel.class);
        } else if (getActivity() != null) {
            return ViewModelProviders.of(getActivity()).get(CreateInvoiceSharedViewModel.class);
        } else {
            return null;
        }
    }

    protected MultipleContactSelectionSharedViewModel getMultipleContactSelectionSharedViewModel() {
        if (mActivity instanceof MainActivity) {
            return ViewModelProviders.of(((MainActivity) mActivity)).get(MultipleContactSelectionSharedViewModel.class);
        } else if (getActivity() != null) {
            return ViewModelProviders.of(getActivity()).get(MultipleContactSelectionSharedViewModel.class);
        } else {
            return null;
        }
    }*/
}