package com.example.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.appyvet.materialrangebar.RangeBar;
import com.cisner.cisnerapp.BR;
import com.cisner.cisnerapp.BaseActivity;
import com.cisner.cisnerapp.BaseBinder;
import com.cisner.cisnerapp.BuildConfig;
import com.cisner.cisnerapp.Cisner;
import com.cisner.cisnerapp.MainActivity;
import com.cisner.cisnerapp.R;
import com.cisner.cisnerapp.adapter.CommonAdapter;
import com.cisner.cisnerapp.api.ApiClient;
import com.cisner.cisnerapp.api.BaseUrl;
import com.cisner.cisnerapp.api.CommonAPI;
import com.cisner.cisnerapp.api.ResponseUtils;
import com.cisner.cisnerapp.cometchat.utils.MyLg;
import com.cisner.cisnerapp.comment.CommentBottomsheetDialog;
import com.cisner.cisnerapp.databinding.AudioCommentLayoutBinding;
import com.cisner.cisnerapp.databinding.BottomsheetAddmoneyBinding;
import com.cisner.cisnerapp.databinding.CheckoutPopupViewBinding;
import com.cisner.cisnerapp.databinding.DialogCreateAlbumBinding;
import com.cisner.cisnerapp.databinding.DialogDeactivatePasswordBinding;
import com.cisner.cisnerapp.databinding.DialogDeactivateReasonBinding;
import com.cisner.cisnerapp.databinding.DialogOneTwoButtonsBinding;
import com.cisner.cisnerapp.databinding.DialogRecyclerBinding;
import com.cisner.cisnerapp.databinding.DropdownListviewBinding;
import com.cisner.cisnerapp.databinding.FailPopupBinding;
import com.cisner.cisnerapp.databinding.ItemDropdownViewBinding;
import com.cisner.cisnerapp.databinding.PopupImageLayoutBinding;
import com.cisner.cisnerapp.databinding.PopupImageLayoutBottomBinding;
import com.cisner.cisnerapp.databinding.PostCommentViewBinding;
import com.cisner.cisnerapp.databinding.PostLikeViewBinding;
import com.cisner.cisnerapp.databinding.ReportPopupViewBinding;
import com.cisner.cisnerapp.databinding.RowCommentListItemBinding;
import com.cisner.cisnerapp.databinding.RowItemCountrySearchableDialogBinding;
import com.cisner.cisnerapp.databinding.RowItemPopupDialogBinding;
import com.cisner.cisnerapp.databinding.RowItemPopupDialogIconBinding;
import com.cisner.cisnerapp.databinding.RowItemSearchableDialogBinding;
import com.cisner.cisnerapp.databinding.RowLikeListItemBinding;
import com.cisner.cisnerapp.databinding.RowPeopleKnowListItemBinding;
import com.cisner.cisnerapp.databinding.RowPrivacyItemVisibilityBinding;
import com.cisner.cisnerapp.databinding.SearchableCountryDailogBinding;
import com.cisner.cisnerapp.databinding.SearchableDailogBinding;
import com.cisner.cisnerapp.databinding.UpgradeNowPopupViewBinding;
import com.cisner.cisnerapp.databinding.WalletRequestStatusBinding;
import com.cisner.cisnerapp.databinding.WatcherFilterBinding;
import com.cisner.cisnerapp.databinding.WithdrawPopupViewBinding;
import com.cisner.cisnerapp.download.DownloadIntentService;
import com.cisner.cisnerapp.enums.AccountActionEnum;
import com.cisner.cisnerapp.eventbus.ListCountChange;
import com.cisner.cisnerapp.forgotpwd.ForgotPasswordActivity;
import com.cisner.cisnerapp.interfaces.CallbackTask;
import com.cisner.cisnerapp.interfaces.OnConfirmationDialog;
import com.cisner.cisnerapp.interfaces.OnSearchableDialog;
import com.cisner.cisnerapp.like.LikeButton;
import com.cisner.cisnerapp.like.OnLikeListener;
import com.cisner.cisnerapp.model.CommentData;
import com.cisner.cisnerapp.model.CountryData;
import com.cisner.cisnerapp.model.Download;
import com.cisner.cisnerapp.model.Dropdownitem;
import com.cisner.cisnerapp.model.Member;
import com.cisner.cisnerapp.model.Post;
import com.cisner.cisnerapp.model.WatcherCategory;
import com.cisner.cisnerapp.moments.AddMomentActivity;
import com.cisner.cisnerapp.pages.PagesDetailsActivity;
import com.cisner.cisnerapp.profile.OtherUserProfileActivity;
import com.cisner.cisnerapp.profile.SelfProfileActivity;
import com.cisner.cisnerapp.view.AudioView;
import com.cisner.cisnerapp.view.TagGroup;
import com.cisner.cisnerapp.visualizer.SimpleWaveform;
import com.cisner.cisnerapp.watchers.WatchersActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cisner-1 on 13/3/18.
 */

public class AlertUtils {

    private static final String TITLE = "title";
    private static final String ICON = "icon";
    private static final String SUBTITLE = "subtitle";
    private static CustomPowerMenu customPowerMenu;
    private static PowerMenu powerMenu;
    public static PopupWindow popupWindow;
    public static List<HashMap<String, Object>> customData = new ArrayList<HashMap<String, Object>>();
    private static Dropdownitem mSelectedValue;
    private static String mImageUrl;
    public static int pageid = 1;
    private static boolean isNextPage = false, isLoadMore = false, isRefresh = false;
    private static CommonAdapter<Member> mAdapter;
    private static CommonAdapter<Member> mAdapterFriend = null;

    private static LocalBroadcastManager bManager;
    private static final String AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION = "AudioFileDownloadFromCommentsAction";
    private static Visualizer visualizer;
    private static MediaPlayer player;
    static boolean isAudioView;

    static String replyText = "";

    static int blogcommentid = 0, blogreplycommentid = 0;


    public static void showSnackBar(Activity context, int stringId) {
        showSnackBar(context, BaseBinder.getLabel(stringId));
    }


    public static void showSnackBar(Activity context, String msg) {
        if (context == null)
            return;

        TSnackbar snackbar = TSnackbar.make(context.getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_SHORT);
        TSnackbar.SnackbarLayout layout = (TSnackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(Color.TRANSPARENT);
        layout.setPadding(30, 40, 30, 0);
        // Hide the default text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        // Inflate our custom view
        View snackView = context.getLayoutInflater().inflate(R.layout.custom_snackbar_view, null);
        snackView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, (int) context.getResources().getDimension(R.dimen._100sdp)));
        TextView txtMsg = (TextView) snackView.findViewById(R.id.tvMsg);
        BaseBinder.loadLabel(txtMsg, msg);

        // Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
        //layout.setMinimumHeight(200);

        // Show the Snackbar
        snackbar.show();
    }

    @SuppressLint("ResourceType")
    public static void showCountryCodeSearchableDialog(final Activity activity, final List<CountryData> data, final boolean multiSelect, final OnSearchableDialog searchableDialog) {
        final Dialog dialog = new Dialog(activity, R.style.SearchableDialogStyle);
        final SearchableCountryDailogBinding binder = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.searchable_country_dailog, null, false);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, android.R.color.transparent));
        dialog.setContentView(binder.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        final List<CountryData> objectsDup = new ArrayList<>();
        objectsDup.addAll(data);

        if (multiSelect) {
            binder.tvSelect.setText(BaseBinder.getLabel(activity.getResources().getString(R.string.done)));
            binder.tvSelect.setVisibility(View.VISIBLE);
            binder.tvSelect.setEnabled(true);
        } else {
            binder.tvSelect.setVisibility(View.GONE);
        }

        binder.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binder.edtSearch.getWindowToken(), 0);
                ArrayList<CountryData> arrData = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isSelected())
                        arrData.add(data.get(i));
                }
                if (searchableDialog != null)
                    searchableDialog.onItemSelected(arrData);
                dialog.dismiss();
            }
        });


        class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> implements SectionIndexer {
            private ArrayList<Integer> mSectionPositions;

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RowItemCountrySearchableDialogBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.row_item_country_searchable_dialog, parent, false);
                return new MyViewHolder(binding);
            }

            @Override
            public int getItemCount() {
                return objectsDup.size();
            }

            @Override
            public void onBindViewHolder(final MyViewHolder holder, final int position) {
                final CountryData cd = (CountryData) objectsDup.get(position);
                holder.binding.setVariable(BR.item, cd);
                holder.binding.executePendingBindings();
                holder.binding.ivCountryFlag.setImageResource(cd.getCountryFlagRes());
                if (multiSelect) {
                    holder.binding.cbSelected.setVisibility(View.VISIBLE);
                    if (objectsDup.get(position).isSelected()) {
                        holder.binding.cbSelected.setChecked(true);
                    } else {
                        holder.binding.cbSelected.setChecked(false);
                    }

                } else {
                    holder.binding.cbSelected.setVisibility(View.GONE);
                }

                holder.binding.cbSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.binding.cbSelected.isChecked()) {
                            objectsDup.get(position).setSelected(true);
                        } else {
                            objectsDup.get(position).setSelected(false);
                        }
                    }
                });
                holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!multiSelect) {
                            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(binder.edtSearch.getWindowToken(), 0);
                            if (searchableDialog != null)
                                searchableDialog.onItemSelected(objectsDup.get(position));
                            dialog.dismiss();
                        }
                    }
                });
            }


            @Override
            public Object[] getSections() {
                List<String> sections = new ArrayList<>(26);
                mSectionPositions = new ArrayList<>(26);

                for (int i = 0, size = objectsDup.size(); i < size; i++) {
                    String section = String.valueOf(objectsDup.get(i).toString().charAt(0)).toUpperCase();
                    if (!sections.contains(section)) {
                        sections.add(section);
                        mSectionPositions.add(i);
                    }
                }
                return sections.toArray(new String[0]);
            }

            @Override
            public int getPositionForSection(int sectionIndex) {
                return mSectionPositions.get(sectionIndex);
            }

            @Override
            public int getSectionForPosition(int position) {
                return 0;
            }

            class MyViewHolder extends RecyclerView.ViewHolder {
                public RowItemCountrySearchableDialogBinding binding;

                MyViewHolder(RowItemCountrySearchableDialogBinding itemView) {
                    super(itemView.getRoot());
                    binding = itemView;
                }
            }

            void filter(String charText) {

                charText = charText.toLowerCase(Locale.getDefault());
                if (objectsDup != null) {
                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < objectsDup.size(); j++) {
                            if (objectsDup.get(j).isSelected())
                                if (objectsDup.get(j).getCountryCode().equalsIgnoreCase(data.get(i).getCountryCode())
                                        && objectsDup.get(j).getCountryNameEng().equalsIgnoreCase(data.get(i).getCountryNameEng()))
                                    data.get(i).setSelected(true);
                        }
                    }
                }
                objectsDup.clear();
                if (charText.length() == 0) {
                    objectsDup.addAll(data);
                } else {
                    for (CountryData obj : data) {
                        if (obj.toString().toLowerCase(Locale.getDefault()).startsWith(charText) || obj.getCountryCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                            objectsDup.add(obj);
                        }
                    }
                }
                if (objectsDup.isEmpty()) {
                    binder.txtNoRecordFound.setVisibility(View.VISIBLE);
                } else {
                    binder.txtNoRecordFound.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        }

        final CountryAdapter adapter = new CountryAdapter();

        binder.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = binder.edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                if (text.length() > 0) {
                    adapter.filter(text);
                } else {
                    objectsDup.addAll(data);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        binder.rvItems.setIndexBarTextColor("#5499D2");
        binder.rvItems.setIndexBarTransparentValue(0);
        binder.rvItems.setPreviewVisibility(false);

        binder.rvItems.setLayoutManager(new LinearLayoutManager(activity));
        binder.rvItems.setAdapter(adapter);

        dialog.show();
        WindowManager.LayoutParams windowManager = activity.getWindow().getAttributes();
        windowManager.dimAmount = 0.2f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        final View activityRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isOpened = false;
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    binder.rvItems.setIndexBarVisibility(false);
                } else {
                    binder.rvItems.setIndexBarVisibility(true);
                }
            }
        });
    }

    public static void showSearchableDialog(final Activity activity, final List<?> data, boolean ifShowSearch, final OnSearchableDialog searchableDialog) {

        final Dialog dialog = new Dialog(activity, R.style.SearchableDialogStyle);
        final SearchableDailogBinding binder = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.searchable_dailog, null, false);
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(activity, android.R.color.transparent));
        dialog.setContentView(binder.getRoot());
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (ifShowSearch) {
            binder.edtSearch.setVisibility(View.VISIBLE);
        } else {
            binder.edtSearch.setVisibility(View.GONE);
        }

        final List<Object> objectsDup = new ArrayList<>();
        objectsDup.addAll(data);

        class SearchableAdapter extends RecyclerView.Adapter<SearchableAdapter.MyViewHolder> {

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                RowItemSearchableDialogBinding binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.row_item_searchable_dialog, parent, false);
                return new MyViewHolder(binding);
            }

            @Override
            public int getItemCount() {
                return objectsDup.size();
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, final int position) {
                Object cd = objectsDup.get(position);
                holder.binding.setVariable(BR.item, cd);
                holder.binding.executePendingBindings();

                holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(binder.edtSearch.getWindowToken(), 0);
                        if (searchableDialog != null)
                            searchableDialog.onItemSelected(objectsDup.get(position));
                        dialog.dismiss();
                    }
                });
            }

            class MyViewHolder extends RecyclerView.ViewHolder {
                public RowItemSearchableDialogBinding binding;

                MyViewHolder(RowItemSearchableDialogBinding itemView) {
                    super(itemView.getRoot());
                    binding = itemView;
                }
            }

            void filter(String charText) {
                charText = charText.toLowerCase(Locale.getDefault());
                objectsDup.clear();
                if (charText.length() == 0) {
                    objectsDup.addAll(data);
                } else {
                    for (Object obj : data) {
                        if (obj.toString().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                            objectsDup.add(obj);
                        }
                    }
                }
                if (objectsDup.isEmpty()) {
                    binder.txtNoRecordFound.setVisibility(View.VISIBLE);
                } else {
                    binder.txtNoRecordFound.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        }

        final SearchableAdapter adapter = new SearchableAdapter();

        binder.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                String text = binder.edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        binder.rvItems.setLayoutManager(new LinearLayoutManager(activity));
        binder.rvItems.setAdapter(adapter);

        dialog.show();

        WindowManager.LayoutParams windowManager = activity.getWindow().getAttributes();
        windowManager.dimAmount = 0.2f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    public static void showBottomSheetListDialog(final Activity context, final List<?> data, String title, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_recycler, null);

        final DialogRecyclerBinding binding = DialogRecyclerBinding.bind(sheetView);
        binding.rvItems.setVisibility(View.VISIBLE);
        binding.loader.setVisibility(View.GONE);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setNestedScrollingEnabled(false);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.vw1.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
            binding.vw1.setVisibility(View.GONE);
        }

        class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {

            RowItemPopupDialogBinding binder;

            class MyViewHolder extends RecyclerView.ViewHolder {
                RowItemPopupDialogBinding binder;

                public MyViewHolder(RowItemPopupDialogBinding binding) {
                    super(binding.getRoot());
                    this.binder = binding;
                }
            }

            @Override
            public void onBindViewHolder(GenderAdapter.MyViewHolder holder, final int position) {
                holder.binder.setItem(data.get(position));
                final RowItemPopupDialogBinding binding = ((RowItemPopupDialogBinding) holder.binder);
                if (data.get(position).toString().equalsIgnoreCase(BaseBinder.getLabel(context.getResources().getString(R.string.logout)))) {

                    binding.tvItem.setTextColor(context.getResources().getColor(R.color.colorRed));
                } else {
                    binding.tvItem.setTextColor(context.getResources().getColor(R.color.colorBlueProfile));
                }
                holder.binder.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected(data.get(position));
                        mBottomSheetDialog.dismiss();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            @Override
            public GenderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                binder = RowItemPopupDialogBinding.inflate(context.getLayoutInflater(), parent, false);
                return new GenderAdapter.MyViewHolder(binder);
            }
        }

        binding.rvItems.setAdapter(new GenderAdapter());
        mBottomSheetDialog.show();
    }

    public static void showBottomSheetListDialogWithIcon(final Activity context, final int typeArrayId, final int stringArrayId, String title, final OnSearchableDialog onSearchableDialog) {
        TypedArray icons = context.getResources().obtainTypedArray(typeArrayId);
        String[] titles = context.getResources().getStringArray(stringArrayId);
        showBottomSheetListDialogWithIcon(context, icons, titles, title, onSearchableDialog);
    }

    public static void showBottomSheetListDialogWithIcon(final Activity context, final TypedArray tp, final String[] data, String title, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_recycler, null);

        final DialogRecyclerBinding binding = DialogRecyclerBinding.bind(sheetView);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setNestedScrollingEnabled(false);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (title != null) {
            binding.tvTitle.setText(BaseBinder.getLabel(title));
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.vw1.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
            binding.vw1.setVisibility(View.GONE);
        }

        class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {

            RowItemPopupDialogIconBinding binder;

            class MyViewHolder extends RecyclerView.ViewHolder {
                RowItemPopupDialogIconBinding binder;

                public MyViewHolder(RowItemPopupDialogIconBinding binding) {
                    super(binding.getRoot());
                    this.binder = binding;
                }
            }

            @Override
            public void onBindViewHolder(GenderAdapter.MyViewHolder holder, final int position) {
                final RowItemPopupDialogIconBinding binding = holder.binder;

                binding.ivItem.setImageDrawable(tp.getDrawable(position));
                binding.tvItem.setText(BaseBinder.getLabel(data[position]));

                holder.binder.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected(position);
                        mBottomSheetDialog.dismiss();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.length;
            }

            @Override
            public GenderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                binder = RowItemPopupDialogIconBinding.inflate(context.getLayoutInflater(), parent, false);
                return new GenderAdapter.MyViewHolder(binder);
            }
        }

        binding.rvItems.setAdapter(new GenderAdapter());
        mBottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                // In a previous life I used this method to get handles to the positive and negative buttons
                // of a dialog in order to change their Typeface. Good ol' days.

                BottomSheetDialog d = (BottomSheetDialog) dialog;

                // This is gotten directly from the source of BottomSheetDialog
                // in the wrapInBottomSheet() method
                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);

                // Right here!
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomSheetFriendListDialog(final Activity context, int id, int type, String title, String APINAME, boolean isReply, final OnSearchableDialog onSearchableDialog) {


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.post_like_view, null);
        final PostLikeViewBinding binding = PostLikeViewBinding.bind(sheetView);
        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.tvTitle.setText(title);
        binding.loader.setVisibility(View.GONE);
        binding.rvLikeList.setVisibility(View.GONE);
        binding.rvLikeList.setLayoutManager(new LinearLayoutManager(context));


        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
        }

        pageid = 0;
        isNextPage = false;

        isLoadMore = false;
        if (!mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        }

        mBottomSheetDialog.show();

        LikeList(context, APINAME, id, binding, type, isReply);

    }

    public static void showImageLikesUser(final Activity context, int post_id, int photo_id, String title, String APINAME, boolean isReply, final OnSearchableDialog onSearchableDialog) {


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.post_like_view, null);
        final PostLikeViewBinding binding = PostLikeViewBinding.bind(sheetView);
        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.tvTitle.setText(title);
        binding.loader.setVisibility(View.VISIBLE);
        binding.rvLikeList.setVisibility(View.GONE);
        binding.rvLikeList.setLayoutManager(new LinearLayoutManager(context));

        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
        }

        pageid = 0;
        isNextPage = false;

        isLoadMore = false;

        mBottomSheetDialog.show();

        LikeList(context, APINAME, post_id, photo_id, binding, 8, isReply);

    }

    public static void showBottomSheetFriendListDialog(final Activity context, int id, int type, int commentid, int replyid, String title, String APINAME, boolean isReply, final OnSearchableDialog onSearchableDialog) {


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.post_like_view, null);
        final PostLikeViewBinding binding = PostLikeViewBinding.bind(sheetView);
        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.tvTitle.setText(title);
        binding.loader.setVisibility(View.VISIBLE);
        binding.rvLikeList.setVisibility(View.GONE);
        binding.rvLikeList.setLayoutManager(new LinearLayoutManager(context));

        blogcommentid = commentid;
        blogreplycommentid = replyid;
        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
        }

        pageid = 0;
        isNextPage = false;

        isLoadMore = false;

        mBottomSheetDialog.show();

        LikeList(context, APINAME, id, binding, type, isReply);

    }

    public static void showBottomSheetListMenuDialog(final Activity context, final List<?> data, String title, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_recycler, null);

        final DialogRecyclerBinding binding = DialogRecyclerBinding.bind(sheetView);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setNestedScrollingEnabled(false);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.vw1.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
            binding.vw1.setVisibility(View.GONE);
        }

        class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {

            RowItemPopupDialogBinding binder;

            class MyViewHolder extends RecyclerView.ViewHolder {
                RowItemPopupDialogBinding binder;

                public MyViewHolder(RowItemPopupDialogBinding binding) {
                    super(binding.getRoot());
                    this.binder = binding;
                }
            }

            @Override
            public void onBindViewHolder(GenderAdapter.MyViewHolder holder, final int position) {
                holder.binder.setItem(data.get(position));
                final RowItemPopupDialogBinding binding = ((RowItemPopupDialogBinding) holder.binder);
                binding.tvItem.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                if (position == 0) {
                    binding.tvItem.setPadding(80, 80, 80, 40);
                } else if (position == data.size() - 1) {
                    binding.tvItem.setPadding(80, 40, 80, 80);
                } else {
                    binding.tvItem.setPadding(80, 40, 80, 40);
                }
                if (data.get(position).toString().equalsIgnoreCase(BaseBinder.getLabel(context.getResources().getString(R.string.logout)))) {

                    binding.tvItem.setTextColor(context.getResources().getColor(R.color.colorRed));
                } else {
                    binding.tvItem.setTextColor(context.getResources().getColor(R.color.defaultContentColor));
                }
                holder.binder.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected(position);
                        mBottomSheetDialog.dismiss();
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            @Override
            public GenderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                binder = RowItemPopupDialogBinding.inflate(context.getLayoutInflater(), parent, false);
                return new GenderAdapter.MyViewHolder(binder);
            }
        }

        binding.rvItems.setAdapter(new GenderAdapter());
        mBottomSheetDialog.show();
    }

    public static View showBottomSheetListDialog1(final Activity context, final List<?> data, String title, final OnSearchableDialog onSearchableDialog) {


        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_recycler, null);

        DialogRecyclerBinding binding = DialogRecyclerBinding.bind(sheetView);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));

        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (title != null) {
            binding.tvTitle.setText(title);
            binding.tvTitle.setVisibility(View.VISIBLE);
            binding.vw1.setVisibility(View.VISIBLE);
        } else {
            binding.tvTitle.setVisibility(View.GONE);
            binding.vw1.setVisibility(View.GONE);
        }

        class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {

            RowItemPopupDialogBinding binder;

            class MyViewHolder extends RecyclerView.ViewHolder {
                RowItemPopupDialogBinding binder;

                public MyViewHolder(RowItemPopupDialogBinding binding) {
                    super(binding.getRoot());
                    this.binder = binding;
                }
            }

            @Override
            public void onBindViewHolder(GenderAdapter.MyViewHolder holder, final int position) {
                holder.binder.setItem(data.get(position));
                holder.binder.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSearchableDialog != null)
                            onSearchableDialog.onItemSelected(data.get(position));
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            @Override
            public GenderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                binder = RowItemPopupDialogBinding.inflate(context.getLayoutInflater(), parent, false);
                return new GenderAdapter.MyViewHolder(binder);
            }
        }

        binding.rvItems.setAdapter(new GenderAdapter());
        return sheetView;
    }

    public static void showImagePopupBottomSheet(Activity context, String imageId, String uri, boolean isUri) {
        try {

            final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
            mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            View sheetView = context.getLayoutInflater().inflate(R.layout.popup_image_layout_bottom, null);
            final PopupImageLayoutBottomBinding binding = PopupImageLayoutBottomBinding.bind(sheetView);
            mBottomSheetDialog.setContentView(binding.getRoot());
            final View inflatedView = (View) sheetView.getParent();
            inflatedView.setBackgroundColor(Color.TRANSPARENT);
            BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(inflatedView);
            //height
            inflatedView.setFitsSystemWindows(true);
            sheetView.measure(0, 0);
            DisplayMetrics displaymetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenHeight = displaymetrics.heightPixels;
            bottomSheetBehavior.setPeekHeight(screenHeight);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) inflatedView.getLayoutParams();

            params.height = screenHeight;
            inflatedView.setLayoutParams(params);

            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });

            if (isUri) {
                BaseBinder.setImageUrl(binding.imageView, uri);
                //binding.imageView.setImageURI(uri);
            } else {
                BaseBinder.setImageUrl(binding.imageView, imageId);
            }

            binding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                }
            });

            binding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                }
            });
            binding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBottomSheetDialog.dismiss();
                }
            });

            mBottomSheetDialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showImagePopup(Activity mActivity, String imageId, String uri, boolean isUri) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            PopupImageLayoutBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.popup_image_layout, null, false);

            //set Image
            if (isUri) {
                BaseBinder.setImageUrl(binding.imageView, uri);
                //binding.imageView.setImageURI(uri);
            } else {
                BaseBinder.setImageUrl(binding.imageView, imageId);
            }

            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(binding.lytMain, ViewGroup.LayoutParams.MATCH_PARENT, Utils.getPopupHeight(mActivity), true);
            // display the popup in the center
            pw.showAtLocation(binding.lytMain, Gravity.BOTTOM, 0, 0);

            binding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pw.dismiss();
                }
            });
            binding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Show Confirm Option Popup
    public static void showCustomErrorPopup(Activity mActivity, String message, int color, String btnTitle, final boolean touchable, final OnSearchableDialog searchableDialog) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) mActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            FailPopupBinding binding = DataBindingUtil.inflate(
                    inflater, R.layout.fail_popup, null, false);


            // create a 300px width and 470px height PopupWindow
            final PopupWindow pw = new PopupWindow(binding.lytMain, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(binding.lytMain, Gravity.CENTER, 0, 0);

            pw.setTouchable(true);
            if (touchable) {
                pw.setFocusable(true);
                pw.setOutsideTouchable(true);
            } else {
                pw.setFocusable(false);
                pw.setOutsideTouchable(false);
            }
            binding.txtMessage.setText(BaseBinder.getLabel(message));


            GradientDrawable drawable = (GradientDrawable) binding.lytTop.getBackground();
            drawable.setColor(Color.WHITE);

            drawable = (GradientDrawable) binding.lytBottom.getBackground();
            drawable.setColor(Color.WHITE);

            drawable = (GradientDrawable) binding.btnOk.getBackground();
            drawable.setColor(color);

            binding.txtYes.setText(btnTitle);


            Utils.buttonEffect(binding.btnOk, mActivity);

            binding.lytMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (touchable)
                        if (searchableDialog != null) {
                            searchableDialog.onItemSelected(0);
                            pw.dismiss();
                        }
                }
            });

            binding.btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchableDialog != null) {
                        searchableDialog.onItemSelected(0);
                        pw.dismiss();
                    }
                }
            });

            pw.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {

                    }
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showBottomSheetDeactivateReasonDialog(final Activity context, final AccountActionEnum accountType, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_deactivate_reason, null);

        final DialogDeactivateReasonBinding binding = DialogDeactivateReasonBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (accountType == AccountActionEnum.DEACTIVATE) {
            BaseBinder.loadLabel(binding.tvTitle, context.getResources().getString(R.string.deactivate_account));
            BaseBinder.loadLabel(binding.tvTitleText, context.getResources().getString(R.string.deactivate_account_reason));
            BaseBinder.loadLabel(binding.tvDeactivate, context.getResources().getString(R.string.deactivate_account));
        } else {
            BaseBinder.loadLabel(binding.tvTitle, context.getResources().getString(R.string.delete_account));
            BaseBinder.loadLabel(binding.tvTitleText, context.getResources().getString(R.string.delete_account_reason));
            BaseBinder.loadLabel(binding.tvDeactivate, context.getResources().getString(R.string.delete_account));
        }

        binding.rgReason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == -1 && binding.edtOtherReason.getText().toString().trim().length() < 20) {
                    Utils.setButtonEnable(binding.tvDeactivate, false);
                } else {
                    Utils.setButtonEnable(binding.tvDeactivate, true);
                }
            }
        });

        binding.edtOtherReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtOtherReason.getText().toString().trim().length() >= 10) {
                    Utils.setButtonEnable(binding.tvDeactivate, true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        binding.tvDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();

                String reason = "";
                if (binding.edtOtherReason.getText().toString().trim().length() > 0) {
                    reason = binding.edtOtherReason.getText().toString().trim();
                } else {
                    int selectedId = binding.rgReason.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) v1.findViewById(selectedId);
                    reason = radioButton.getText().toString();
                }

                showBottomSheetDeactivatePasswordDialog(context, accountType, reason, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {

                    }
                });

            }
        });

        mBottomSheetDialog.show();
    }

    public static void showBottomSheetDeactivatePasswordDialog(final Activity context, final AccountActionEnum accountType, final String reason, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_deactivate_password, null);

        final DialogDeactivatePasswordBinding binding = DialogDeactivatePasswordBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (accountType == AccountActionEnum.DEACTIVATE) {
            BaseBinder.loadLabel(binding.tvTitle, context.getResources().getString(R.string.deactivate_account));
            BaseBinder.loadLabel(binding.tvTitleText, context.getResources().getString(R.string.deactivate_account_password_text));
            BaseBinder.loadLabel(binding.tvDeactivate, context.getResources().getString(R.string.deactivate_account));
        } else if (accountType == AccountActionEnum.DELETE) {
            BaseBinder.loadLabel(binding.tvTitle, context.getResources().getString(R.string.delete_account));
            BaseBinder.loadLabel(binding.tvTitleText, context.getResources().getString(R.string.delete_account_password_text));
            BaseBinder.loadLabel(binding.tvDeactivate, context.getResources().getString(R.string.delete_account));
        } else {
            BaseBinder.loadLabel(binding.tvTitle, context.getResources().getString(R.string.remove_page));
            BaseBinder.loadLabel(binding.tvTitleText, context.getResources().getString(R.string.remove_page_password_text));
            BaseBinder.loadLabel(binding.tvDeactivate, context.getResources().getString(R.string.remove_the_page));
        }

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    Utils.setButtonEnable(binding.tvDeactivate, false);
                } else {
                    Utils.setButtonEnable(binding.tvDeactivate, true);
                }
            }
        });

        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        binding.tvDeactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (accountType == AccountActionEnum.DEACTIVATE) {
                    showBottomSheetSimpleConfirmationDialog(context, BaseBinder.getLabel(context.getString(R.string.deactive_text)), null, false, BaseBinder.getLabel(context.getString(R.string.cancel)), BaseBinder.getLabel(context.getString(R.string.label_continue)), new OnConfirmationDialog() {
                        @Override
                        public void onYes() {
                            Utils.showProgressBar(context);
                            CommonAPI.deactiveAccount(reason, Utils.getUserId(context), Utils.getSessionValue(context), binding.edtPassword.getText().toString().trim(), new CallbackTask() {

                                @Override
                                public void onFail(Object object) {
                                    Utils.dismissProgressBar();
                                    AlertUtils.showSnackBar(context, object.toString());
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Utils.dismissProgressBar();
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    Utils.dismissProgressBar();

                                    Utils.setUserData(context, null);
                                    Utils.setSessionValue(context, "");
                                    Utils.setUserId(context, "");
                                    Utils.setIsLoggedIn(context, false);
                                    Cisner.getInstance().setProfile(null);
                                    Utils.setMembershipData(context, null);

                                    //Logout to cometChat
                                    ((BaseActivity) context).logoutToCometChat();

                                    AlertUtils.showBottomSheetSimpleConfirmationDialogNotCancelable(context, BaseBinder.getLabel(R.string.deactive_success), null, true, null, context.getResources().getString(R.string.ok), new OnConfirmationDialog() {
                                        @Override
                                        public void onYes() {
                                            context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            context.finish();
                                        }

                                        @Override
                                        public void onNo() {

                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onNo() {

                        }
                    });
                } else if (accountType == AccountActionEnum.DELETE) {
                    showBottomSheetSimpleConfirmationDialog(context, BaseBinder.getLabel(context.getString(R.string.delete_account_text)), null, false, BaseBinder.getLabel(context.getString(R.string.cancel)), BaseBinder.getLabel(context.getString(R.string.delete)), new OnConfirmationDialog() {
                        @Override
                        public void onYes() {
                            Utils.showProgressBar(context);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("current_password", binding.edtPassword.getText().toString().trim());
                            jsonObject.addProperty("reason", reason);

                            CommonAPI.deleteAccount(Utils.getUserId(context), Utils.getSessionValue(context), jsonObject, context, new CallbackTask() {

                                @Override
                                public void onFail(Object object) {
                                    Utils.dismissProgressBar();
                                    AlertUtils.showSnackBar(context, object.toString());
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Utils.dismissProgressBar();
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    Utils.dismissProgressBar();

                                    Utils.setUserData(context, null);
                                    Utils.setSessionValue(context, "");
                                    Utils.setUserId(context, "");
                                    Utils.setIsLoggedIn(context, false);
                                    Cisner.getInstance().setProfile(null);
                                    Utils.setMembershipData(context, null);

                                    //Logout to cometChat
                                    ((BaseActivity) context).logoutToCometChat();

                                    AlertUtils.showBottomSheetSimpleConfirmationDialogNotCancelable(context, BaseBinder.getLabel(R.string.delete_success), null, true, null, context.getResources().getString(R.string.ok), new OnConfirmationDialog() {
                                        @Override
                                        public void onYes() {
                                            context.startActivity(new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            context.finish();
                                        }

                                        @Override
                                        public void onNo() {

                                        }
                                    });


                                }
                            });
                        }

                        @Override
                        public void onNo() {
                            mBottomSheetDialog.dismiss();
                        }
                    });
                } else {
                    showBottomSheetSimpleConfirmationDialog(context, BaseBinder.getLabel(context.getString(R.string.confirmPageAccount)), null, false, BaseBinder.getLabel(context.getString(R.string.no)), BaseBinder.getLabel(context.getString(R.string.yes)), new OnConfirmationDialog() {
                        @Override
                        public void onYes() {

                            JSONObject requestParams = new JSONObject();
                            try {
                                requestParams.put("user_id", Utils.getUserId(context));
                                requestParams.put("s", Utils.getSessionValue(context));
                                requestParams.put("password", binding.edtPassword.getText().toString().trim());
                                requestParams.put("page_id", reason);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            CommonAPI.deletePage(requestParams, new CallbackTask() {

                                @Override
                                public void onFail(Object object) {
                                    AlertUtils.showSnackBar(context, object.toString());
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    ResponseUtils.getExceptionMessage(context, t);
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    if (onSearchableDialog != null) {
                                        onSearchableDialog.onItemSelected("success");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNo() {

                        }
                    });
                }
            }
        });

        binding.tvForgottenPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ForgotPasswordActivity.class));
            }
        });

        mBottomSheetDialog.show();
    }

    public static void showBottomSheetSimpleConfirmationDialog(final Activity context, String titleText, String subTitleText, boolean showOneButton, String negativeText, String positiveText, final OnConfirmationDialog onConfirmationDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_one_two_buttons, null);

        final DialogOneTwoButtonsBinding binding = DialogOneTwoButtonsBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.tvTitle.setText(BaseBinder.getLabel(titleText));
        binding.tvPositive.setText(BaseBinder.getLabel(positiveText));

        if (subTitleText != null) {
            binding.tvSubTitle.setText(BaseBinder.getLabel(subTitleText));
            binding.tvSubTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvSubTitle.setVisibility(View.GONE);
        }

        if (negativeText != null) {
            binding.tvNegative.setText(BaseBinder.getLabel(negativeText));
        }

        if (showOneButton) {
            binding.tvNegative.setVisibility(View.GONE);
        } else {
            binding.tvNegative.setVisibility(View.VISIBLE);
        }

        binding.tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (onConfirmationDialog != null) {
                    onConfirmationDialog.onYes();
                }
            }
        });

        binding.tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (onConfirmationDialog != null) {
                    onConfirmationDialog.onNo();
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomSheetSimpleConfirmationDialogNotCancelable(final Activity context, String titleText, String subTitleText, boolean showOneButton, String negativeText, String positiveText, final OnConfirmationDialog onConfirmationDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_one_two_buttons, null);

        final DialogOneTwoButtonsBinding binding = DialogOneTwoButtonsBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        mBottomSheetDialog.setCancelable(false);
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.tvTitle.setText(BaseBinder.getLabel(titleText));
        binding.tvPositive.setText(BaseBinder.getLabel(positiveText));

        if (subTitleText != null) {
            binding.tvSubTitle.setText(BaseBinder.getLabel(subTitleText));
            binding.tvSubTitle.setVisibility(View.VISIBLE);
        } else {
            binding.tvSubTitle.setVisibility(View.GONE);
        }

        if (negativeText != null) {
            binding.tvNegative.setText(BaseBinder.getLabel(negativeText));
        }

        if (showOneButton) {
            binding.tvNegative.setVisibility(View.GONE);
        } else {
            binding.tvNegative.setVisibility(View.VISIBLE);
        }

        binding.tvPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (onConfirmationDialog != null) {
                    onConfirmationDialog.onYes();
                }
            }
        });

        binding.tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
                if (onConfirmationDialog != null) {
                    onConfirmationDialog.onNo();
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomSheetCreateAlbum(final Activity context, String mData, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_create_album, null);

        final DialogCreateAlbumBinding binding = DialogCreateAlbumBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.tvCancelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mImageUrl = "";
        binding.llupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.selectImageDialog(context, false, false, new ImageUtils.OnPickPhoto() {
                    @Override
                    public void onPick(Uri uri, String path) {
                        binding.ivCoverPic.setVisibility(View.VISIBLE);
                        mImageUrl = path;
                        binding.ivCoverPic.setImageURI(uri);
                        binding.llupload.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {
                    }


                });
            }
        });

        binding.ivCoverPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.selectImageDialog(context, false, false, new ImageUtils.OnPickPhoto() {
                    @Override
                    public void onPick(Uri uri, String path) {
                        binding.ivCoverPic.setVisibility(View.VISIBLE);
                        mImageUrl = path;
                        binding.ivCoverPic.setImageURI(uri);
                        binding.llupload.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancel() {
                    }


                });
            }
        });


        final ArrayList<Dropdownitem> dropdownList = new ArrayList<>();
        Dropdownitem item = new Dropdownitem();
        item.setTitle(BaseBinder.getLabel(context.getResources().getString(R.string.lbl_public)));
        item.setIcon(R.drawable.ic_world);
        item.setItemcolor(context.getResources().getColor(R.color.defaultContentColor));
        item.setSubtitle(BaseBinder.getLabel(context.getResources().getString(R.string.lbl_public_desc)));
        item.setItemValue("0");
        dropdownList.add(item);


        Dropdownitem item1 = new Dropdownitem();
        item1.setTitle(BaseBinder.getLabel(context.getResources().getString(R.string.friends)));
        item1.setIcon(R.drawable.ic_friends);
        item1.setItemcolor(context.getResources().getColor(R.color.defaultContentColor));
        item1.setSubtitle(BaseBinder.getLabel(context.getResources().getString(R.string.lbl_friend_desc)));
        item1.setItemValue("1");
        dropdownList.add(item1);

        Dropdownitem item2 = new Dropdownitem();
        item2.setTitle(BaseBinder.getLabel(context.getResources().getString(R.string.only_me)));
        item2.setIcon(R.drawable.ic_hide);
        item2.setItemcolor(context.getResources().getColor(R.color.defaultContentColor));
        item2.setSubtitle(BaseBinder.getLabel(context.getResources().getString(R.string.lbl_onlyme_desc)));
        item2.setItemValue("3");
        dropdownList.add(item2);
        mSelectedValue = dropdownList.get(0);

        binding.llprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertUtils.showCustomDropdownRecycle(context, binding.llprivacy, dropdownList, R.layout.row_privacy_item_visibility, false, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        mSelectedValue = (Dropdownitem) o;
                        binding.txtVisiblitytype.setText(mSelectedValue.getTitle());
                        binding.txtVisiblityDesc.setText(mSelectedValue.getSubtitle());
                        binding.icon.setImageResource(mSelectedValue.getIcon());
                    }
                });
            }
        });

        binding.tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtAlbumname.getText().toString().trim().length() <= 0) {
                    AlertUtils.showSnackBar(context, BaseBinder.getLabel(context.getResources().getString(R.string.error_albumname)));
                } else if (mImageUrl == null || mImageUrl.equalsIgnoreCase("")) {
                    AlertUtils.showSnackBar(context, BaseBinder.getLabel(context.getResources().getString(R.string.addCoverImage)));
                } else {
                    if (onSearchableDialog != null) {
                        HashMap<String, String> mData = new HashMap<>();
                        mData.put("name", binding.edtAlbumname.getText().toString());
                        mData.put("privacy", mSelectedValue.getItemValue());
                        mData.put("image", mImageUrl);
                        onSearchableDialog.onItemSelected(mData);
                        mBottomSheetDialog.dismiss();
                    }

                }
            }
        });

        binding.tvCancelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.show();
    }

    public static void showBottomSheetAdvertFilter(final Activity context, final ArrayList<WatcherCategory> arrCategory, boolean isCat, final OnSearchableDialog onSearchableDialog) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View sheetView = context.getLayoutInflater().inflate(R.layout.watcher_filter, null);

        final WatcherFilterBinding binding = WatcherFilterBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        mBottomSheetDialog.setCancelable(true);
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        if (isCat) {
            binding.txtAlbumname.setVisibility(View.GONE);
            binding.llcategory.setVisibility(View.GONE);
        }

        if (WatchersActivity.filter_categoryname.length() > 0) {
            binding.tvCategory.setText(WatchersActivity.filter_categoryname);
        } else {
            binding.tvCategory.setText("");
        }
        binding.llcategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Dropdownitem> dropdownList = new ArrayList<>();
                for (int i = 0; i < arrCategory.size(); i++) {
                    Dropdownitem item = new Dropdownitem();
                    item.setTitle(arrCategory.get(i).getTitle());
                    item.setItemValue(String.valueOf(arrCategory.get(i).getId()));
                    item.setIcon(0);
                    item.setItemcolor(context.getResources().getColor(R.color.defaultContentColor));
                    dropdownList.add(item);
                }
                AlertUtils.showCustomDropdownRecycle(context, binding.llcategory, dropdownList, R.layout.item_dropdown_view, false, new OnSearchableDialog() {
                    @Override
                    public void onItemSelected(Object o) {
                        Dropdownitem item = (Dropdownitem) o;
                        binding.tvCategory.setText(item.getTitle());
                        WatchersActivity.filter_categoryname = item.getTitle();
                        WatchersActivity.filter_category = item.getItemValue();

                    }
                });
            }
        });

        int leftpinprice = 1, rightpinprice = 100;
        if (WatchersActivity.filter_min_price.trim().length() > 0) {
            leftpinprice = Integer.parseInt(WatchersActivity.filter_min_price);
            binding.priceMin.setText(leftpinprice + "$");

        }
        if (WatchersActivity.filter_max_price.trim().length() > 0) {
            rightpinprice = Integer.parseInt(WatchersActivity.filter_max_price);
            binding.priceMax.setText(rightpinprice + "$");
        }
        binding.priceRangeBar.setRangePinsByValue(leftpinprice, rightpinprice);

        double leftpinlegth = 0.01f, rightpinlength = 1f;
        if (WatchersActivity.filter_min_length.trim().length() > 0) {
            leftpinlegth = Double.parseDouble(WatchersActivity.filter_min_length);
            binding.tvvideoMin.setText(String.valueOf(leftpinlegth).replace(".", ":").replace("0:", "00:"));
        }
        if (WatchersActivity.filter_max_length.trim().length() > 0) {
            rightpinlength = Double.parseDouble(WatchersActivity.filter_max_length);
            binding.tvvideoMax.setText(String.valueOf(rightpinlength).replace(".", ":").replace("0:", "00:"));
            if (binding.tvvideoMax.getText().toString().equals("1")) {
                binding.tvvideoMax.setText("01:00");
            }
        }
        binding.videoRangeBar.setRangePinsByValue(Float.parseFloat(String.valueOf(leftpinlegth)), Float.parseFloat(String.valueOf(rightpinlength)));

        binding.priceRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                binding.priceMin.setText(leftPinValue + "$");
                binding.priceMax.setText(rightPinValue + "$");

                WatchersActivity.filter_min_price = leftPinValue;
                WatchersActivity.filter_max_price = rightPinValue;
            }


        });

        binding.videoRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex, String leftPinValue, String rightPinValue) {

                binding.tvvideoMin.setText(leftPinValue.replace(".", ":").replace("0:", "00:"));
                binding.tvvideoMax.setText(rightPinValue.replace(".", ":").replace("0:", "00:"));
                if (binding.tvvideoMax.getText().toString().equals("1")) {
                    binding.tvvideoMax.setText("01:00");
                }

                WatchersActivity.filter_min_length = leftPinValue;
                WatchersActivity.filter_max_length = rightPinValue;

            }


        });

        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSearchableDialog.onItemSelected(true);
                mBottomSheetDialog.dismiss();
            }
        });

        binding.tvCancelOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchableDialog.onItemSelected(false);
                WatchersActivity.filter_categoryname = "";
                WatchersActivity.filter_category = "";
                WatchersActivity.filter_min_price = "";
                WatchersActivity.filter_max_price = "";
                WatchersActivity.filter_min_length = "";
                WatchersActivity.filter_max_length = "";
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.show();
    }


    public static void showPowerMenu(final Activity context, View mView, ArrayList<PowerMenuItem> PowerMenuItem, final OnSearchableDialog onSearchableDialog) {

        powerMenu = new PowerMenu.Builder(context)
                .setMenuRadius(15f)
                .setMenuShadow(10f)
                .setWith((int) context.getResources().getDimension(R.dimen._110sdp))
                .setTextColor(context.getResources().getColor(R.color.colorBlack))
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(Color.WHITE)
                .setShowBackground(false)
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                        onSearchableDialog.onItemSelected(position);
                        if (powerMenu != null) powerMenu.dismiss();
                        powerMenu = null;
                    }
                })
                .build();

        for (int i = 0; i < PowerMenuItem.size(); i++) {
            powerMenu.addItem(PowerMenuItem.get(i));
        }
        powerMenu.showAsDropDown(mView, -80, -80);
    }

    public static void showPowerMenuWithColors(final Activity context, View mView, ArrayList<PowerMenuItem> PowerMenuItem, final OnSearchableDialog onSearchableDialog) {

        powerMenu = new PowerMenu.Builder(context)
                .setMenuRadius(15f)
                .setMenuShadow(10f)
                .setWith((int) context.getResources().getDimension(R.dimen._110sdp))
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(Color.WHITE)
                .setShowBackground(false)
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
                        //Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
                        onSearchableDialog.onItemSelected(position);
                        if (powerMenu != null) powerMenu.dismiss();
                        powerMenu = null;
                    }
                })
                .build();

        for (int i = 0; i < PowerMenuItem.size(); i++) {
            switch (i) {
                case 0:
                case 1:
                    powerMenu.setTextColor(ContextCompat.getColor(Cisner.instance, R.color.colorBlack));
                    break;

                case 2:
                case 3:
                    powerMenu.setTextColor(ContextCompat.getColor(Cisner.instance, R.color.colorBlack));
                    break;
            }
            powerMenu.addItem(i, PowerMenuItem.get(i));
        }
        powerMenu.showAsDropDown(mView, -80, -80);
    }

    public static void showCustomDropdownRecycle(final Activity context, View mView, final ArrayList<Dropdownitem> dropdownList, final int layout, boolean roundedBgRequire, final OnSearchableDialog onSearchableDialog) {

        popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        DropdownListviewBinding binding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.dropdown_listview, null, true);

        //binding.recyclePopup.setBackgroundColor(context.getResources().getColor(R.color.ef_white));
        //binding.recyclePopup.setBackground(null);

        popupWindow.setWidth(mView.getWidth());

        //popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);


        CommonAdapter<Dropdownitem> mAdapter = new CommonAdapter<Dropdownitem>(dropdownList) {

            @Override
            public void onBind(CommonViewHolder holder, final int position) {

                if (holder.binding instanceof ItemDropdownViewBinding) {
                    final ItemDropdownViewBinding binding = ((ItemDropdownViewBinding) holder.binding);
                    if (dropdownList.get(position).getIcon() != 0) {
                        binding.icon.setVisibility(View.VISIBLE);
                        binding.icon.setImageResource(dropdownList.get(position).getIcon());
                    } else {
                        binding.icon.setVisibility(View.GONE);
                    }

                    ((ItemDropdownViewBinding) holder.binding).view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            Dropdownitem item = dropdownList.get(position);
                            onSearchableDialog.onItemSelected(item);

                        }
                    });

                } else if (holder.binding instanceof RowPrivacyItemVisibilityBinding) {
                    final RowPrivacyItemVisibilityBinding binding = ((RowPrivacyItemVisibilityBinding) holder.binding);
                    binding.icon.setImageResource(dropdownList.get(position).getIcon());
                    binding.txtVisiblityTitle.setText(dropdownList.get(position).getTitle());
                    binding.txtVisiblityDesc.setText(dropdownList.get(position).getSubtitle());
                    binding.llprivacy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            Dropdownitem item = dropdownList.get(position);
                            onSearchableDialog.onItemSelected(item);
                        }
                    });
                }

                holder.binding.setVariable(BR.item, dropdownList.get(position));
                holder.binding.executePendingBindings();

            }

            @Override
            public int getItemViewType(int position) {

                return layout;

            }
        };

        binding.recyclePopup.setAdapter(mAdapter);
        Drawable border;
        if (!roundedBgRequire) {
            border = context.getResources().getDrawable(R.drawable.bottom_corner_bg);
        } else {
            border = context.getResources().getDrawable(R.drawable.card_bg);
        }
        popupWindow.setBackgroundDrawable(border);
        popupWindow.setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(10.0f);
        }

        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(mView);
    }

    public static void showBottomLikeDialog(final Activity context, Post post, String blogId, String commentId, String commentReplyId, String type) {

        final ArrayList<Member>[] arrData = new ArrayList[1];
        arrData[0] = new ArrayList<>();

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.post_like_view, null);

        final PostLikeViewBinding binding = PostLikeViewBinding.bind(sheetView);

        binding.tvTitle.setText(BaseBinder.getLabel(context.getResources().getString(R.string.people_who_like)));
        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

    /*    BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) sheetView.getParent());
        mBehavior.setPeekHeight(500);
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);*/

        binding.loader.setVisibility(View.VISIBLE);
        binding.rvLikeList.setVisibility(View.GONE);

        class T {
            public void t(ArrayList<Member> arrData) {
                mAdapterFriend = new CommonAdapter<Member>(arrData) {
                    @Override
                    public void onBind(CommonViewHolder holder, final int position) {
                        if (holder.binding instanceof RowLikeListItemBinding) {
                            Member md = arrData.get(position);

                            ((RowLikeListItemBinding) holder.binding).setItem(md);

                            ((RowLikeListItemBinding) holder.binding).getRoot().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (md.getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                        context.startActivity(new Intent(context, SelfProfileActivity.class));
                                    } else {
                                        //context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("id", md.getUserId()));
                                        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", md));
                                    }
                                }
                            });

                            /*((RowLikeListItemBinding) holder.binding).tvName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (md.getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                        context.startActivity(new Intent(context, SelfProfileActivity.class));
                                    } else {
                                        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", md));
                                    }
                                }
                            });*/

                            if (md.getUserId().equalsIgnoreCase(Utils.getUserId(Cisner.getInstance())) || md.isFriend()) {
                                ((RowLikeListItemBinding) holder.binding).btnAddFriend.setVisibility(View.GONE);
                            } else {
                                ((RowLikeListItemBinding) holder.binding).btnAddFriend.setVisibility(View.VISIBLE);
                            }
                            ((RowLikeListItemBinding) holder.binding).btnAddFriend.setText(Utils.getMemberStatus(md, context));

                            ((RowLikeListItemBinding) holder.binding).btnAddFriend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int status = Utils.callMemberStatus(md);
                                    switch (status) {
                                        case 0://Already Friends
                                            md.setLoading(true);
                                            CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), md.getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                                @Override
                                                public void onFail(Object object) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    md.setLoading(false);
                                                    try {
                                                        if (((JsonObject) object).has("total_friends")) {
                                                            int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                            EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                        }
                                                    } catch (Exception e) {

                                                    }
                                                    if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                        if (md.isFriend()) {
                                                            md.setFriend(false);
                                                            md.setRequestSend(false);
                                                        } else {
                                                            md.setFriend(false);
                                                            md.setRequestSend(true);
                                                        }
                                                    }
                                                    mAdapterFriend.notifyDataSetChanged();
                                                }
                                            });
                                            break;
                                        case 1://Request Send
                                            md.setLoading(true);
                                            CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), md.getUserId(), "cancel", context, new CallbackTask() {

                                                @Override
                                                public void onFail(Object object) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    md.setLoading(false);
                                                    md.setFriend(false);
                                                    md.setRequestSend(false);
                                                    md.setFriendRequest(false);
                                                    mAdapterFriend.notifyDataSetChanged();
                                                }
                                            });
                                            break;
                                        case 2://Accept Friend Request
                                            md.setLoading(true);

                                            CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), md.getUserId(), "accept", context, new CallbackTask() {

                                                @Override
                                                public void onFail(Object object) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    md.setLoading(false);

                                                    md.setFriend(true);
                                                    md.setRequestSend(false);
                                                    md.setFriendRequest(false);


                                                    mAdapterFriend.notifyDataSetChanged();
                                                }
                                            });

                                            break;
                                        case 3://Block User
                                            md.setLoading(true);
                                            CommonAPI.blockUnblockUser(Utils.getUserId(Cisner.getInstance()), "un-block", md.getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                                @Override
                                                public void onFail(Object object) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    md.setLoading(false);
                                                    if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                        md.setBlock(false);
                                                    }
                                                    mAdapterFriend.notifyDataSetChanged();
                                                }
                                            });
                                            break;
                                        case 4:// Add Friends
                                            md.setLoading(true);
                                            CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), md.getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                                @Override
                                                public void onFail(Object object) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                    md.setLoading(false);
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    md.setLoading(false);
                                                    try {
                                                        if (((JsonObject) object).has("total_friends")) {
                                                            int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                            EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                        }
                                                    } catch (Exception e) {

                                                    }
                                                    if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                        if (!md.isFriend()) {
                                                            md.setRequestSend(true);
                                                        } else {
                                                            md.setFriend(false);
                                                            md.setRequestSend(false);
                                                            md.setFriendRequest(false);
                                                        }
                                                    }
                                                    mAdapterFriend.notifyDataSetChanged();
                                                }
                                            });
                                            break;
                                    }
                                }
                            });
                            ((RowLikeListItemBinding) holder.binding).executePendingBindings();
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        if (getItem(position) == null)
                            return R.layout.item_load_more;
                        return R.layout.row_like_list_item;
                    }
                };

                binding.rvLikeList.setAdapter(mAdapterFriend);

                binding.loader.setVisibility(View.GONE);
                binding.rvLikeList.setVisibility(View.VISIBLE);

                mAdapter.setLoadMoreListener(new CommonAdapter.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        binding.rvLikeList.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.startLoadMore();

                                isLoadMore = true;
                                //fetchFriendList();

                            }
                        });
                    }
                });
            }
        }

        if (type.equalsIgnoreCase("post")) {
            CommonAPI.postGetLikeUsersApi(context, post, new CallbackTask() {
                @Override
                public void onFail(Object object) {

                }

                @Override
                public void onSuccess(Object object) {
                    JsonObject jObjData = (JsonObject) object;
                    arrData[0] = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("liked_users").toString(), Member[].class));
                    T t1 = new T();
                    t1.t(arrData[0]);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else if (type.equalsIgnoreCase("blog")) {
            CommonAPI.blogGetLikeUsersApi(context, blogId, commentId, commentReplyId, new CallbackTask() {
                @Override
                public void onFail(Object object) {

                }

                @Override
                public void onSuccess(Object object) {
                    JsonObject jObjData = (JsonObject) object;
                    arrData[0] = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("bloglike_users").toString(), Member[].class));
                    T t1 = new T();
                    t1.t(arrData[0]);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else if (type.equalsIgnoreCase("blogComment")) {
            CommonAPI.blogCommentGetLikeUsersApi(context, blogId, commentId, commentReplyId, new CallbackTask() {
                @Override
                public void onFail(Object object) {

                }

                @Override
                public void onSuccess(Object object) {
                    JsonObject jObjData = (JsonObject) object;
                    arrData[0] = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("bloglike_users").toString(), Member[].class));
                    T t1 = new T();
                    t1.t(arrData[0]);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else if (type.equalsIgnoreCase("postComment")) {
            int id = commentId.equalsIgnoreCase("0") ? Integer.parseInt(commentReplyId) : Integer.parseInt(commentId);
            CommonAPI.commentGetLikeUsersApi(context, id, commentId.equalsIgnoreCase("0"), new CallbackTask() {
                @Override
                public void onFail(Object object) {

                }

                @Override
                public void onSuccess(Object object) {
                    JsonObject jObjData = (JsonObject) object;
                    arrData[0] = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("post_comments").toString(), Member[].class));
                    T t1 = new T();
                    t1.t(arrData[0]);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }
        binding.rvLikeList.setAdapter(mAdapterFriend);
        mBottomSheetDialog.show();

    }

    public static void LoadWaveForm(SimpleWaveform simpleWaveform) {

        simpleWaveform.init();
        //Simple WaveView
        Paint barPencilFirst = new Paint();
        Paint barPencilSecond = new Paint();
        Paint peakPencilFirst = new Paint();
        Paint peakPencilSecond = new Paint();
        Paint xAxisPencil = new Paint();


        LinkedList<Float> ampList = new LinkedList<>();
        //if(mpost.getWave().length()>0)
        // {
        //String[] mValue = mpost.getWave().split(",");
        //generate random data
        /*for (int i = 0; i < mValue.length; i++) {
            ampList.add((Float.valueOf(mValue[i]) * 900) );
        }
        }
        else
        {*/
        //generate dummy data
        for (int i = 0; i < 80; i++) {
            ampList.add(Utils.randomInt(5, 30));
        }
        // }
        simpleWaveform.setDataList(ampList);

        //define bar gap
        simpleWaveform.barGap = 20;

        //define x-axis direction
        simpleWaveform.modeDirection = SimpleWaveform.MODE_DIRECTION_LEFT_RIGHT;

        //define if draw opposite pole when show bars
        simpleWaveform.modeAmp = SimpleWaveform.MODE_AMP_ABSOLUTE;
        //define if the unit is px or percent of the view's height
        simpleWaveform.modeHeight = SimpleWaveform.MODE_HEIGHT_PERCENT;
        //define where is the x-axis in y-axis
        simpleWaveform.modeZero = SimpleWaveform.MODE_ZERO_CENTER;
        //if show bars?
        simpleWaveform.showBar = true;

        //define how to show peaks outline
        simpleWaveform.modePeak = SimpleWaveform.MODE_PEAK_PARALLEL;
        //if show peaks outline?
        simpleWaveform.showPeak = false;

        //show x-axis
        simpleWaveform.showXAxis = true;
        xAxisPencil.setStrokeWidth(5);
        xAxisPencil.setColor(Color.parseColor("#fffeff"));
        simpleWaveform.xAxisPencil = xAxisPencil;

        //define pencil to draw bar
        barPencilFirst.setStrokeWidth(15);
        barPencilFirst.setColor(Color.parseColor("#56a8e2"));
        simpleWaveform.barPencilFirst = barPencilFirst;
        barPencilSecond.setStrokeWidth(15);
        barPencilSecond.setColor(Color.parseColor("#56a8e2"));
        simpleWaveform.barPencilSecond = barPencilSecond;

        //define pencil to draw peaks outline
        peakPencilFirst.setStrokeWidth(5);
        peakPencilFirst.setColor(0xfffe2f3f);
        simpleWaveform.peakPencilFirst = peakPencilFirst;
        peakPencilSecond.setStrokeWidth(5);
        peakPencilSecond.setColor(0xfffeef3f);
        simpleWaveform.peakPencilSecond = peakPencilSecond;

        //the first part will be draw by PencilFirst
        simpleWaveform.firstPartNum = 20;

        //define how to clear screen
        simpleWaveform.clearScreenListener = new SimpleWaveform.ClearScreenListener() {
            @Override
            public void clearScreen(Canvas canvas) {
                canvas.drawARGB(255, 255, 255, 255);
            }
        };
        simpleWaveform.refresh();
    }

    public static void showBottomComment(AppCompatActivity activity) {
        CommentBottomsheetDialog bottomSheetFragment = new CommentBottomsheetDialog();
        bottomSheetFragment.show(activity.getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public static void showBottomCommentDialog(Activity me, Post post, final ArrayList<CommentData> arrComments1, boolean ifShowKeyboard) {
        final ArrayList<CommentData>[] arrComments = new ArrayList[1];
        final int[] commentReplyId = new int[1];
        final int[] commentReplyInnerId = new int[1];
        final int[] commentReplyPosition = new int[1];
        final int[] commentReplyMainPosition = new int[1];
        final int[] page = new int[1];
        final boolean[] isNextPage = new boolean[1];
        final boolean[] isLoadMore = new boolean[1];
        final String[] commentString = new String[1];

        commentString[0] = "";

        //  Collections.reverse(arrComments1);

        arrComments[0] = new ArrayList<>();
        arrComments[0].addAll(arrComments1);
        MyLg.e("arr comment size: ", arrComments[0].size() + "");

        commentReplyId[0] = -1;
        commentReplyInnerId[0] = -1;
        commentReplyPosition[0] = -1;
        commentReplyMainPosition[0] = -1;

        final BottomSheetDialog msBottomSheetDialog = new BottomSheetDialog(me);
        msBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //msBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View sheetView = me.getLayoutInflater().inflate(R.layout.post_comment_view, null);

        PostCommentViewBinding commentDialogBinding = PostCommentViewBinding.bind(sheetView);

        msBottomSheetDialog.setContentView(commentDialogBinding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        int height = me.getWindowManager().getDefaultDisplay().getHeight();

        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) sheetView.getParent());
        mBehavior.setPeekHeight(height);
        CommonAdapter<CommentData> commentAdapter = new CommonAdapter<CommentData>(arrComments[0]) {

            @Override
            public void onBind(CommonViewHolder holder, int position, CommentData item) {
                super.onBind(holder, position, item);

                if (holder.binding instanceof RowCommentListItemBinding) {
                    RowCommentListItemBinding mainBinding = ((RowCommentListItemBinding) holder.binding);

                    final CommentData bd = item;
                    mainBinding.setItem(bd);
                    mainBinding.executePendingBindings();

                    mainBinding.tvMessage.setText(SpanUtils.bindCommentReplyUser(item.getTagged_user_data(), item.getMessage(), me, item.getUserId()));

                    mainBinding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                    mainBinding.tvLoadMoreReplies.setVisibility(View.GONE);
                    mainBinding.btnLikes.setLiked(bd.isLike());

                    if (bd.getAudioFile() != null && !bd.getAudioFile().isEmpty()) {
                        mainBinding.ivComment.setVisibility(View.GONE);
                        mainBinding.tvMessage.setVisibility(View.GONE);
                        mainBinding.mainAudioContent.setVisibility(View.VISIBLE);
                        mainBinding.visualizer.setVisibility(View.GONE);
                        mainBinding.simplewaveform.setVisibility(View.VISIBLE);

                        LoadWaveForm(mainBinding.simplewaveform);

                        File downloadedFile = FileUtils.getAudioFolderFromFile(FileUtils.getFileNameFromURL(bd.getAudioFile()));

                        if (downloadedFile.exists()) {
                            long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(Utils.getFileDuration(downloadedFile)));

                            mainBinding.tvDuration.setText((seconds > 0 ? seconds : "0") + "s");

                        } else {
                            mainBinding.tvDuration.setText((bd.getDuration().length() > 0 ? bd.getDuration() : "0") + "s");

                        }

                        mainBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                        mainBinding.btnPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                try {
                                    // Check file downloaded
                                    File downloadedFile = FileUtils.getAudioFolderFromFile(FileUtils.getFileNameFromURL(bd.getAudioFile()));

                                    if (!downloadedFile.exists()) {
                                        mainBinding.downloadProgressBar.setVisibility(View.VISIBLE);
                                        mainBinding.btnPlay.setAlpha(0.6f);

                                        // Register Receiver
                                        bManager = LocalBroadcastManager.getInstance(me);
                                        IntentFilter intentFilter = new IntentFilter();
                                        intentFilter.addAction(AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION);
                                        bManager.registerReceiver(new BroadcastReceiver() {
                                            @Override
                                            public void onReceive(Context context, Intent intent) {

                                                try {
                                                    if (intent.getAction() != null && intent.getAction().equals(AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION)) {
                                                        Download download = intent.getParcelableExtra("download");
                                                        mainBinding.downloadProgressBar.setProgress(download.getProgress());
                                                        if (download.getProgress() == 100 && download.getSuccess()) {
                                                            mainBinding.btnPlay.setAlpha(1f);
                                                            mainBinding.downloadProgressBar.setVisibility(View.GONE);
                                                            mainBinding.btnPlay.performClick();
                                                            bManager.unregisterReceiver(this);

                                                            long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(
                                                                    Utils.getFileDuration(new File(download.getFileName()))));
                                                            mainBinding.tvDuration.setText((seconds > 0 ? seconds : "0") + "s");

                                                        } else if (!download.getSuccess()) {
                                                            mainBinding.btnPlay.setAlpha(1f);
                                                            mainBinding.downloadProgressBar.setVisibility(View.GONE);
                                                            bManager.unregisterReceiver(this);
                                                            Toast.makeText(context, "Something Error occured, Please try again", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            mainBinding.btnPlay.setAlpha(0.6f);
                                                            mainBinding.downloadProgressBar.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, intentFilter);

                                        Intent intent = new Intent(me, DownloadIntentService.class);
                                        intent.putExtra(DownloadIntentService.FILE_KEY, bd.getAudioFile());
                                        intent.putExtra(DownloadIntentService.ACTION_KEY, AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION);
                                        me.startService(intent);
                                    } else {
                                        mainBinding.btnPlay.setAlpha(1f);
                                        mainBinding.downloadProgressBar.setVisibility(View.GONE);
                                        mainBinding.btnPlay.setSelected(!mainBinding.btnPlay.isSelected());
                                        if (mainBinding.btnPlay.isSelected()) {

                                            if (player != null && player.isPlaying()) {
                                                player.stop();
                                                player.reset();
                                            }
                                            player = new MediaPlayer();
                                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    mainBinding.visualizer.setVisibility(View.VISIBLE);
                                                    mainBinding.simplewaveform.setVisibility(View.GONE);

                                                    visualizer = new Visualizer(mp.getAudioSessionId());
                                                    if (visualizer.getEnabled())
                                                        visualizer.setEnabled(false);
                                                    visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
                                                    visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
                                                    visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                                                        @Override
                                                        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] bytes,
                                                                                          int samplingRate) {
                                                            try {
                                                                mainBinding.visualizer.update(bytes);

                                                                // Displaying Total Duration time
                                                                if (player != null && player.isPlaying()) {
                                                                    mainBinding.tvDuration.setText(TimeUnit.MILLISECONDS.toSeconds(player.getDuration()) + "s");
                                                                    // Displaying time completed playing
                                                                    mainBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(player.getCurrentPosition()));
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                if (visualizer != null) {
                                                                    visualizer.release();
                                                                    visualizer.setDataCaptureListener(null, 0, false, false);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                                                                     int samplingRate) {
                                                        }
                                                    }, Visualizer.getMaxCaptureRate() / 2, true, false);

                                                    visualizer.setEnabled(true);
                                                    player.start();

                                                }
                                            });
                                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {

                                                    mainBinding.btnPlay.setSelected(false);

                                                    mainBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                                                    mainBinding.visualizer.setVisibility(View.GONE);
                                                    mainBinding.simplewaveform.setVisibility(View.VISIBLE);

                                                }
                                            });
                                            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                @Override
                                                public boolean onError(MediaPlayer mp, int what, int extra) {

                                                    mainBinding.btnPlay.setSelected(false);
                                                    mainBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                                                    mainBinding.visualizer.setVisibility(View.GONE);
                                                    mainBinding.simplewaveform.setVisibility(View.VISIBLE);

                                                    mp.release();
                                                    return false;
                                                }
                                            });
                                            try {
                                                player.setDataSource(me, Uri.fromFile(downloadedFile));
                                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                player.prepareAsync();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            try {
                                                player.pause();
                                                mainBinding.visualizer.setVisibility(View.GONE);
                                                mainBinding.simplewaveform.setVisibility(View.VISIBLE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } else {
                        mainBinding.mainAudioContent.setVisibility(View.GONE);
                        if (bd.getImageFile() != null && !bd.getImageFile().isEmpty()) {
                            mainBinding.ivComment.setVisibility(View.VISIBLE);
                            mainBinding.tvMessage.setVisibility(View.GONE);
                        } else {
                            mainBinding.ivComment.setVisibility(View.GONE);
                            mainBinding.tvMessage.setVisibility(View.VISIBLE);
                        }
                    }

                    mainBinding.ivComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertUtils.showImagePopupBottomSheet(me, null, bd.getImageFile(), true);
                        }
                    });

                    if (bd.getReplyData().size() > 0) {

                        //  Collections.reverse(bd.getReplyData());

                        mainBinding.rvReplayList.setVisibility(View.VISIBLE);
                        final ArrayList<CommentData> arrReplayMain = bd.getReplyData();
                        final ArrayList<CommentData> arrReplay = new ArrayList<>();

                        if (arrReplayMain.size() <= 2) {
                            arrReplay.addAll(arrReplayMain);
                            mainBinding.tvLoadMoreReplies.setVisibility(View.GONE);
                        } else {
                            arrReplay.add(arrReplayMain.get(0));
                            arrReplay.add(arrReplayMain.get(1));
                            mainBinding.tvLoadMoreReplies.setVisibility(View.VISIBLE);
                        }

                        CommonAdapter<CommentData> replayAdapter = new CommonAdapter<CommentData>(arrReplay) {
                            boolean isLoadMore = true;

                            @Override
                            public void onBind(CommonViewHolder holder1, final int pos) {
                                if (holder1.binding instanceof RowCommentListItemBinding) {
                                    final RowCommentListItemBinding subBinding = ((RowCommentListItemBinding) holder1.binding);

                                    subBinding.btnLikes.setLiked(arrReplay.get(pos).isLike());

                                    if (arrReplay.get(pos).getAudioFile() != null && !arrReplay.get(pos).getAudioFile().isEmpty()) {

                                        subBinding.ivComment.setVisibility(View.GONE);
                                        subBinding.mainAudioContent.setVisibility(View.VISIBLE);
                                        subBinding.visualizer.setVisibility(View.GONE);
                                        subBinding.simplewaveform.setVisibility(View.VISIBLE);

                                        subBinding.tvMessage.setText(SpanUtils.bindCommentReplyUser(arrReplay.get(pos).getTagged_user_data(), arrReplay.get(pos).getMessage(), me, arrReplay.get(pos).getUserId()));
                                        subBinding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                                        LoadWaveForm(subBinding.simplewaveform);

                                        File downloadedFile = FileUtils.getAudioFolderFromFile(FileUtils.getFileNameFromURL(arrReplay.get(pos).getAudioFile()));

                                        if (downloadedFile.exists()) {
                                            long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(Utils.getFileDuration(downloadedFile)));

                                            subBinding.tvDuration.setText((seconds > 0 ? seconds : "0") + "s");

                                        } else {
                                            subBinding.tvDuration.setText((arrReplay.get(pos).getDuration().length() > 0 ? arrReplay.get(pos).getDuration() : "0") + "s");

                                        }

                                        subBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if (arrReplay.get(position).getUserData().getPageid() != null && Integer.parseInt(arrReplay.get(position).getUserData().getPageid()) > 0) {
                                                    Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                    mIntent.putExtra("pageId", arrReplay.get(position).getUserData().getPageid());
                                                    me.startActivity(mIntent);
                                                } else {

                                                    if (arrReplay.get(position).getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                        me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                    } else {
                                                        me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                    }
                                                }
                                            }
                                        });

                                        subBinding.tvUserName.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (arrReplay.get(position).getUserData().getPageid() != null && Integer.parseInt(arrReplay.get(position).getUserData().getPageid()) > 0) {
                                                    Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                    mIntent.putExtra("pageId", arrReplay.get(position).getUserData().getPageid());
                                                    me.startActivity(mIntent);
                                                } else {

                                                    if (arrReplay.get(position).getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                        me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                    } else {
                                                        me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                    }
                                                }
                                            }
                                        });


                                        subBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                                        subBinding.btnPlay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                try {
                                                    // Check file downloaded
                                                    File downloadedFile = FileUtils.getAudioFolderFromFile(FileUtils.getFileNameFromURL(arrReplay.get(pos).getAudioFile()));

                                                    if (!downloadedFile.exists()) {
                                                        subBinding.downloadProgressBar.setVisibility(View.VISIBLE);
                                                        subBinding.btnPlay.setAlpha(0.6f);

                                                        // Register Receiver
                                                        bManager = LocalBroadcastManager.getInstance(me);
                                                        IntentFilter intentFilter = new IntentFilter();
                                                        intentFilter.addAction(AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION);
                                                        bManager.registerReceiver(new BroadcastReceiver() {
                                                            @Override
                                                            public void onReceive(Context context, Intent intent) {

                                                                try {
                                                                    if (intent.getAction() != null && intent.getAction().equals(AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION)) {
                                                                        Download download = intent.getParcelableExtra("download");
                                                                        subBinding.downloadProgressBar.setProgress(download.getProgress());
                                                                        if (download.getProgress() == 100 && download.getSuccess()) {
                                                                            subBinding.btnPlay.setAlpha(1f);
                                                                            subBinding.downloadProgressBar.setVisibility(View.GONE);
                                                                            subBinding.btnPlay.performClick();
                                                                            bManager.unregisterReceiver(this);

                                                                            long seconds = TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(
                                                                                    Utils.getFileDuration(new File(download.getFileName()))));
                                                                            subBinding.tvDuration.setText((seconds > 0 ? seconds : "0") + "s");

                                                                        } else if (!download.getSuccess()) {
                                                                            subBinding.btnPlay.setAlpha(1f);
                                                                            subBinding.downloadProgressBar.setVisibility(View.GONE);
                                                                            bManager.unregisterReceiver(this);
                                                                            Toast.makeText(context, "Something Error occured, Please try again", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            subBinding.btnPlay.setAlpha(0.6f);
                                                                            subBinding.downloadProgressBar.setVisibility(View.VISIBLE);
                                                                        }
                                                                    }
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }, intentFilter);

                                                        Intent intent = new Intent(me, DownloadIntentService.class);
                                                        intent.putExtra(DownloadIntentService.FILE_KEY, arrReplay.get(pos).getAudioFile());
                                                        intent.putExtra(DownloadIntentService.ACTION_KEY, AUDIO_FILE_DOWNLOAD_COMMENTS_ACTION);
                                                        me.startService(intent);
                                                    } else {
                                                        subBinding.btnPlay.setAlpha(1f);
                                                        subBinding.downloadProgressBar.setVisibility(View.GONE);
                                                        subBinding.btnPlay.setSelected(!subBinding.btnPlay.isSelected());
                                                        if (subBinding.btnPlay.isSelected()) {

                                                            if (player != null && player.isPlaying()) {
                                                                player.stop();
                                                                player.reset();
                                                            }
                                                            player = new MediaPlayer();
                                                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                                @Override
                                                                public void onPrepared(MediaPlayer mp) {
                                                                    subBinding.visualizer.setVisibility(View.VISIBLE);
                                                                    subBinding.simplewaveform.setVisibility(View.GONE);

                                                                    visualizer = new Visualizer(mp.getAudioSessionId());
                                                                    if (visualizer.getEnabled())
                                                                        visualizer.setEnabled(false);
                                                                    visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
                                                                    visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
                                                                    visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                                                                        @Override
                                                                        public void onWaveFormDataCapture(Visualizer visualizer, final byte[] bytes,
                                                                                                          int samplingRate) {
                                                                            try {
                                                                                subBinding.visualizer.update(bytes);

                                                                                // Displaying Total Duration time
                                                                                if (player != null && player.isPlaying()) {
                                                                                    subBinding.tvDuration.setText(TimeUnit.MILLISECONDS.toSeconds(player.getDuration()) + "s");
                                                                                    // Displaying time completed playing
                                                                                    subBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(player.getCurrentPosition()));
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                                if (visualizer != null) {
                                                                                    visualizer.release();
                                                                                    visualizer.setDataCaptureListener(null, 0, false, false);
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                                                                                     int samplingRate) {
                                                                        }
                                                                    }, Visualizer.getMaxCaptureRate() / 2, true, false);

                                                                    visualizer.setEnabled(true);
                                                                    player.start();

                                                                }
                                                            });
                                                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                                @Override
                                                                public void onCompletion(MediaPlayer mp) {

                                                                    subBinding.btnPlay.setSelected(false);

                                                                    subBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                                                                    subBinding.visualizer.setVisibility(View.GONE);
                                                                    subBinding.simplewaveform.setVisibility(View.VISIBLE);

                                                                }
                                                            });
                                                            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                                                                @Override
                                                                public boolean onError(MediaPlayer mp, int what, int extra) {

                                                                    subBinding.btnPlay.setSelected(false);
                                                                    subBinding.tvPlayDuration.setText("" + Utils.milliSecondsToTimer(0));
                                                                    subBinding.visualizer.setVisibility(View.GONE);
                                                                    subBinding.simplewaveform.setVisibility(View.VISIBLE);

                                                                    mp.release();
                                                                    return false;
                                                                }
                                                            });
                                                            try {
                                                                player.setDataSource(me, Uri.fromFile(downloadedFile));
                                                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                                player.prepareAsync();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            try {
                                                                player.pause();
                                                                subBinding.visualizer.setVisibility(View.GONE);
                                                                subBinding.simplewaveform.setVisibility(View.VISIBLE);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                    } else {

                                        subBinding.ivComment.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertUtils.showImagePopupBottomSheet(me, null, arrReplay.get(pos).getImageFile(), true);
                                            }
                                        });

                                        subBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (bd.getUserData().getPageid() != null && Integer.parseInt(bd.getUserData().getPageid()) > 0) {
                                                    Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                    mIntent.putExtra("pageId", bd.getUserData().getPageid());
                                                    me.startActivity(mIntent);
                                                } else {

                                                    if (bd.getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                        me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                    } else {
                                                        me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                    }
                                                }
                                            }
                                        });

                                        subBinding.tvUserName.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (bd.getUserData().getPageid() != null && Integer.parseInt(bd.getUserData().getPageid()) > 0) {
                                                    Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                    mIntent.putExtra("pageId", bd.getUserData().getPageid());
                                                    me.startActivity(mIntent);
                                                } else {

                                                    if (bd.getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                        me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                    } else {
                                                        me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                    }
                                                }
                                            }
                                        });

                                        subBinding.mainAudioContent.setVisibility(View.GONE);
                                        subBinding.tvMessage.setText(SpanUtils.bindCommentReplyUser(arrReplay.get(pos).getTagged_user_data(), arrReplay.get(pos).getMessage(), me, arrReplay.get(pos).getUserId()));
                                        subBinding.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
                                        if (arrReplay.get(pos).getImageFile() != null && !arrReplay.get(pos).getImageFile().isEmpty()) {
                                            subBinding.ivComment.setVisibility(View.VISIBLE);
                                            subBinding.tvMessage.setVisibility(View.GONE);
                                        } else {
                                            subBinding.ivComment.setVisibility(View.GONE);
                                            subBinding.tvMessage.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    subBinding.setItem(arrReplay.get(pos));
                                    subBinding.executePendingBindings();

                                    subBinding.btnReplay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            replyText = "@" + arrReplay.get(pos).getUserData().getName() + " ";
                                            commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                                            commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                                            commentDialogBinding.edtComment.requestFocus();
                                            commentReplyId[0] = bd.getId();
                                            commentReplyInnerId[0] = arrReplay.get(pos).getId();
                                            commentReplyPosition[0] = pos;
                                            commentReplyMainPosition[0] = position;
                                            Utils.openKeyBoard(commentDialogBinding.edtComment);

                                            isLoadMore = true;
                                            notifyDataSetChanged();
                                        }
                                    });

                                    subBinding.tvReplay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            replyText = "@" + arrReplay.get(pos).getUserData().getName() + " ";
                                            commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                                            commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                                            commentDialogBinding.edtComment.requestFocus();
                                            commentReplyInnerId[0] = arrReplay.get(pos).getId();
                                            commentReplyId[0] = bd.getId();
                                            commentReplyPosition[0] = pos;
                                            commentReplyMainPosition[0] = position;
                                            Utils.openKeyBoard(commentDialogBinding.edtComment);

                                            isLoadMore = true;
                                            notifyDataSetChanged();
                                        }
                                    });

                                    subBinding.btnLikes.setOnLikeListener(new OnLikeListener() {
                                        @Override
                                        public void liked(LikeButton likeButton) {
                                            if (arrReplay.get(pos).isLike()) {
                                                arrComments[0].get(position).getReplyData().get(pos).setLikeCounts(arrComments[0].get(position).getReplyData().get(pos).getLikeCounts() - 1);
                                            } else {
                                                arrComments[0].get(position).getReplyData().get(pos).setLikeCounts(arrComments[0].get(position).getReplyData().get(pos).getLikeCounts() + 1);
                                            }
                                            arrReplay.get(pos).setLike(!arrReplay.get(pos).isLike());
                                            notifyDataSetChanged();

                                            CommonAPI.commentLikeApi(me, true, arrReplay.get(pos).getId(), new CallbackTask() {
                                                @Override
                                                public void onFail(Object object) {
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    arrComments[0].get(position).getReplyData().get(pos).setLikeCounts((Integer) object);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                }
                                            });
                                        }

                                        @Override
                                        public void unLiked(LikeButton likeButton) {
                                            arrReplay.get(pos).setLike(!arrReplay.get(pos).isLike());
                                            notifyDataSetChanged();

                                            CommonAPI.commentLikeApi(me, true, arrReplay.get(pos).getId(), new CallbackTask() {
                                                @Override
                                                public void onFail(Object object) {
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    arrComments[0].get(position).getReplyData().get(pos).setLikeCounts((Integer) object);
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                }
                                            });
                                        }
                                    });

                                    subBinding.tvLikes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //AlertUtils.showBottomLikeDialog(me, null, "", "0", String.valueOf(arrReplay.get(pos).getId()), "postComment");
                                            if (arrReplay.get(pos).getLikeCounts() > 0) {
                                                AlertUtils.showBottomSheetFriendListDialog(me, arrReplay.get(pos).getId(), 6, BaseBinder.getLabel(me.getResources().getString(R.string.people_who_like_comment)), BaseUrl.POST_COMMENT_LIKE_USERS, true, new OnSearchableDialog() {
                                                    @Override
                                                    public void onItemSelected(Object o) {
                                                        String s = (String) o;

                                                    }
                                                });
                                            }
                                            /*
                                            CommonAPI.commentGetLikeUsersApi(me, arrReplay.get(pos).getId(), new CallbackTask() {
                                                @Override
                                                public void onFail(Object object) {
                                                }

                                                @Override
                                                public void onSuccess(Object object) {
                                                    JsonObject jObjData = (JsonObject) object;
                                                    ArrayList<Member> arrLikeBlogUsers = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("post_comments").toString(), Member[].class));
                                                   */
/* if (!arrLikeBlogUsers.isEmpty()) {
                                                        AlertUtils.showBottomLikeDialog(me, arrLikeBlogUsers);
                                                    }*//*

                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                }
                                            });
*/
                                        }
                                    });

                                    subBinding.btnMore.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Utils.hideSoftKeyboard(me);
                                            ArrayList<PowerMenuItem> PowerMenuItemList = new ArrayList<>();

                                            PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.replay)), false));
                                            if (arrReplay.get(pos).isOwner()) {
                                                PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.delete)), false));
                                            } else {
                                                PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.report)), false));
                                            }

                                            AlertUtils.showPowerMenu(me, v, PowerMenuItemList, new OnSearchableDialog() {
                                                @Override
                                                public void onItemSelected(Object o) {
                                                    if (((int) o) == 0) {
                                                        replyText = "@" + arrReplay.get(pos).getUserData().getName() + " ";
                                                        commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                                                        commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                                                        commentDialogBinding.edtComment.requestFocus();
                                                        commentReplyId[0] = bd.getId();
                                                        commentReplyInnerId[0] = arrReplay.get(pos).getId();
                                                        commentReplyPosition[0] = pos;
                                                        commentReplyMainPosition[0] = position;
                                                        Utils.openKeyBoard(commentDialogBinding.edtComment);

                                                        isLoadMore = true;
                                                        notifyDataSetChanged();
                                                    } else {
                                                        if (arrReplay.get(pos).isOwner()) {
                                                            AlertUtils.showBottomSheetSimpleConfirmationDialog(me, me.getString(R.string.are_you_sure_want_delete_this_comment), null, false, me.getString(R.string.no), me.getString(R.string.yes), new OnConfirmationDialog() {
                                                                @Override
                                                                public void onYes() {
                                                                    int id = arrReplay.get(pos).getId();
                                                                    arrReplay.remove(pos);
                                                                    arrReplayMain.remove(pos);
                                                                    notifyDataSetChanged();
                                                                    CommonAPI.commentDeleteApi(me, true, id, new CallbackTask() {
                                                                        @Override
                                                                        public void onFail(Object object) {
                                                                        }

                                                                        @Override
                                                                        public void onSuccess(Object object) {
                                                                            //PostUtils.commentUpdate(post, 0);

                                                                            if (arrComments[0].isEmpty()) {
                                                                                commentDialogBinding.rvComment.setVisibility(View.GONE);
                                                                                commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                                                            } else {
                                                                                commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                                                                commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                                                            }
                                                                            arrComments[0].get(position).getReplyData().remove(pos);
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Throwable t) {
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onNo() {
                                                                }
                                                            });
                                                        } else {
                                                            AlertUtils.showBottomReportPopup(me, R.string.give_feedbak_on_this_comment, new OnSearchableDialog<String>() {
                                                                @Override
                                                                public void onItemSelected(String reason) {
                                                                    CommonAPI.commentReportApi(me, true, arrReplay.get(pos).getId(), reason, new CallbackTask() {
                                                                        @Override
                                                                        public void onFail(Object object) {
                                                                        }

                                                                        @Override
                                                                        public void onSuccess(Object object) {
                                                                            JsonObject jObj = (JsonObject) object;
                                                                            try {
                                                                                AlertUtils.showSnackBar(me, jObj.get("respond_message").getAsString());
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Throwable t) {
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });

                                            subBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (bd.getUserData().getPageid() != null && Integer.parseInt(arrReplay.get(position).getUserData().getPageid()) > 0) {
                                                        Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                        mIntent.putExtra("pageId", arrReplay.get(position).getUserData().getPageid());
                                                        me.startActivity(mIntent);
                                                    } else {

                                                        if (arrReplay.get(position).getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                            me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                        } else {
                                                            me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                        }
                                                    }
                                                }
                                            });

                                            subBinding.tvUserName.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (arrReplay.get(position).getUserData().getPageid() != null && Integer.parseInt(arrReplay.get(position).getUserData().getPageid()) > 0) {
                                                        Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                                        mIntent.putExtra("pageId", arrReplay.get(position).getUserData().getPageid());
                                                        me.startActivity(mIntent);
                                                    } else {

                                                        if (arrReplay.get(position).getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                                            me.startActivity(new Intent(me, SelfProfileActivity.class));
                                                        } else {
                                                            me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", arrReplay.get(pos).getUserData()));
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                            }

                            @Override
                            public int getItemCount() {
                                return arrReplay.size();
                            }

                            @Override
                            public int getItemViewType(int position) {
                                return R.layout.row_comment_list_item;
                            }
                        };

                        mainBinding.tvLoadMoreReplies.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                arrReplay.clear();
                                arrReplay.addAll(arrReplayMain);
                                replayAdapter.notifyDataSetChanged();
                                mainBinding.tvLoadMoreReplies.setVisibility(View.GONE);
                            }
                        });

                        mainBinding.rvReplayList.setAdapter(replayAdapter);
                        mainBinding.rvReplayList.setNestedScrollingEnabled(false);
                        if (mainBinding.rvReplayList.getAdapter().getItemCount() > 0) {
                            mainBinding.rvReplayList.smoothScrollToPosition(arrReplay.size() - 1);
                        }
                    } else {
                        mainBinding.rvReplayList.setVisibility(View.GONE);
                    }

                    mainBinding.btnReplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replyText = "@" + bd.getUserData().getName() + " ";
                            commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                            commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                            commentDialogBinding.edtComment.requestFocus();
                            commentReplyId[0] = bd.getId();
                            commentReplyInnerId[0] = 0;
                            commentReplyMainPosition[0] = position;
                            Utils.openKeyBoard(commentDialogBinding.edtComment);

                            notifyDataSetChanged();
                        }
                    });

                    mainBinding.tvReplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            replyText = "@" + bd.getUserData().getName() + " ";
                            commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                            commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                            commentDialogBinding.edtComment.requestFocus();
                            commentReplyId[0] = bd.getId();
                            commentReplyInnerId[0] = 0;
                            commentReplyMainPosition[0] = position;
                            Utils.openKeyBoard(commentDialogBinding.edtComment);

                            notifyDataSetChanged();
                        }
                    });

                    mainBinding.btnMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Utils.hideSoftKeyboard(me);
                            ArrayList<PowerMenuItem> PowerMenuItemList = new ArrayList<>();

                            PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.replay)), false));
                            if (bd.isOwner()) {
                                PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.delete)), false));
                            } else {
                                PowerMenuItemList.add(new PowerMenuItem(BaseBinder.getLabel(me.getResources().getString(R.string.report)), false));
                            }

                            AlertUtils.showPowerMenu(me, v, PowerMenuItemList, new OnSearchableDialog() {
                                @Override
                                public void onItemSelected(Object o) {
                                    if (((int) o) == 0) {
                                        replyText = "@" + bd.getUserData().getName() + " ";
                                        commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(replyText));
                                        commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                                        commentDialogBinding.edtComment.requestFocus();
                                        commentReplyId[0] = bd.getId();
                                        commentReplyInnerId[0] = 0;
                                        commentReplyMainPosition[0] = position;
                                        Utils.openKeyBoard(commentDialogBinding.edtComment);

                                        notifyDataSetChanged();

                                    } else {
                                        if (bd.isOwner()) {
                                            AlertUtils.showBottomSheetSimpleConfirmationDialog(me, me.getString(R.string.are_you_sure_want_delete_this_comment), null, false, me.getString(R.string.no), me.getString(R.string.yes), new OnConfirmationDialog() {
                                                @Override
                                                public void onYes() {
                                                    CommonAPI.commentDeleteApi(me, false, bd.getId(), new CallbackTask() {
                                                        @Override
                                                        public void onFail(Object object) {
                                                        }

                                                        @Override
                                                        public void onSuccess(Object object) {
                                                            PostUtils.commentUpdate(post, 0);
                                                            arrComments[0].remove(position);
                                                            notifyDataSetChanged();
                                                            if (arrComments[0].isEmpty()) {
                                                                commentDialogBinding.rvComment.setVisibility(View.GONE);
                                                                commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                                            } else {
                                                                commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                                                commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Throwable t) {
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onNo() {
                                                }
                                            });

                                        } else {
                                            AlertUtils.showBottomReportPopup(me, R.string.give_feedbak_on_this_comment, new OnSearchableDialog<String>() {
                                                @Override
                                                public void onItemSelected(String reason) {
                                                    CommonAPI.commentReportApi(me, false, bd.getId(), reason, new CallbackTask() {
                                                        @Override
                                                        public void onFail(Object object) {
                                                        }

                                                        @Override
                                                        public void onSuccess(Object object) {
                                                            JsonObject jObj = (JsonObject) object;
                                                            try {
                                                                AlertUtils.showSnackBar(me, jObj.get("respond_message").getAsString());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Throwable t) {
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                                }
                            });
                        }
                    });

                    mainBinding.btnLikes.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            if (bd.isLike()) {
                                arrComments[0].get(position).setLikeCounts(arrComments[0].get(position).getLikeCounts() - 1);
                            } else {
                                arrComments[0].get(position).setLikeCounts(arrComments[0].get(position).getLikeCounts() + 1);
                            }

                            bd.setLike(!bd.isLike());
                            notifyDataSetChanged();

                            CommonAPI.commentLikeApi(me, false, bd.getId(), new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    arrComments[0].get(position).setLikeCounts((Integer) object);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });

                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {
                            bd.setLike(!bd.isLike());
                            notifyDataSetChanged();

                            CommonAPI.commentLikeApi(me, false, bd.getId(), new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    arrComments[0].get(position).setLikeCounts((Integer) object);
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                        }
                    });

                    mainBinding.tvLikes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //AlertUtils.showBottomLikeDialog(me, null, "", String.valueOf(bd.getId()), "0", "postComment");
                            if (bd.getLikeCounts() > 0) {
                                AlertUtils.showBottomSheetFriendListDialog(me, bd.getId(), 6, BaseBinder.getLabel(me.getResources().getString(R.string.people_who_like_comment)), BaseUrl.POST_COMMENT_LIKE_USERS, false, new OnSearchableDialog() {
                                    @Override
                                    public void onItemSelected(Object o) {
                                        String s = (String) o;

                                    }
                                });
                            }
/*
                            CommonAPI.commentGetLikeUsersApi(me, bd.getId(), new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    JsonObject jObjData = (JsonObject) object;
                                    */
/*ArrayList<Member> arrLikeBlogUsers = new ArrayList<>(Utils.stringToList(jObjData.getAsJsonArray("post_comments").toString(), Member[].class));
                                    if (!arrLikeBlogUsers.isEmpty()) {
                                        AlertUtils.showBottomLikeDialog(me, arrLikeBlogUsers);
                                    }*//*

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
*/
                        }
                    });


                    mainBinding.ivProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (bd.getUserData().getPageid() != null && Integer.parseInt(bd.getUserData().getPageid()) > 0) {
                                Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                mIntent.putExtra("pageId", bd.getUserData().getPageid());
                                me.startActivity(mIntent);
                            } else {

                                if (bd.getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                    me.startActivity(new Intent(me, SelfProfileActivity.class));
                                } else {
                                    me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", bd.getUserData()));
                                }
                            }
                        }
                    });

                    mainBinding.tvUserName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (bd.getUserData().getPageid() != null && Integer.parseInt(bd.getUserData().getPageid()) > 0) {
                                Intent mIntent = new Intent(me, PagesDetailsActivity.class);
                                mIntent.putExtra("pageId", bd.getUserData().getPageid());
                                me.startActivity(mIntent);
                            } else {

                                if (bd.getUserData().getUserId().equalsIgnoreCase(Utils.getUserId(me))) {
                                    me.startActivity(new Intent(me, SelfProfileActivity.class));
                                } else {
                                    me.startActivity(new Intent(me, OtherUserProfileActivity.class).putExtra("friendData", bd.getUserData()));
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onBind(final CommonViewHolder holder, final int position) {

            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position) == null)
                    return R.layout.item_load_more;
                return R.layout.row_comment_list_item;
            }
        };

        commentAdapter.setLoadMoreListener(new CommonAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                commentDialogBinding.rvComment.post(new Runnable() {
                    @Override
                    public void run() {
                        commentAdapter.startLoadPrevious();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLoadMore[0] = true;
                                CommonAPI.getPostCommentsApi(me, post, page[0], new CallbackTask() {
                                    @Override
                                    public void onFail(Object object) {
                                    }

                                    @Override
                                    public void onSuccess(Object object) {
                                        JsonObject jsonData = (JsonObject) object;
                                        if (jsonData != null) {
                                            if (arrComments[0] != null)
                                                arrComments[0].clear();
                                            ArrayList<CommentData> cd = new ArrayList<>(Utils.stringToList(String.valueOf(jsonData.getAsJsonArray("post_comments")), CommentData[].class));
                                            //  Collections.reverse(cd);
                                            commentAdapter.updateData(cd);
                                            //commentAdapter.stopLoadMore();
                                            arrComments[0] = cd;
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }
                        }, 1200);
                    }
                });
            }
        });

        if (post.getPostCommentObj().getNextPage() != 0) {
            isNextPage[0] = true;
            page[0] = post.getPostCommentObj().getNextPage();
        } else {
            isNextPage[0] = false;
        }

        if (isLoadMore[0]) {
            commentAdapter.stopLoadPrevious(arrComments[0]);
        } else {
            if (arrComments[0].size() > 0) {
                commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
            } else {
                commentDialogBinding.rvComment.setVisibility(View.GONE);
            }
        }
        commentAdapter.setMoreDataAvailable(isNextPage[0]);
        commentDialogBinding.rvComment.setAdapter(commentAdapter);

        if (commentDialogBinding.rvComment.getAdapter().getItemCount() > 0) {
            commentDialogBinding.rvComment.scrollToPosition(arrComments[0].size() - 1);
        }

        if (arrComments[0].isEmpty()) {
            commentDialogBinding.rvComment.setVisibility(View.GONE);
            commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
        } else {
            commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
            commentDialogBinding.noData.mainView.setVisibility(View.GONE);
        }

        if (ifShowKeyboard) {
            Utils.openKeyBoard(commentDialogBinding.edtComment);
        }

        commentDialogBinding.ivPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideSoftKeyboard(msBottomSheetDialog);
                ImageUtils.selectImageDialog(me, false, false, new ImageUtils.OnPickPhoto() {
                    @Override
                    public void onPick(Uri imageUri, String path) {

                        //show Dialog
                        Utils.showProgressBar(me);

                        if (commentReplyId[0] != -1) {
                            CommonAPI.createCommentReplyApi(me, null, imageUri, String.valueOf(commentReplyId[0]), String.valueOf(commentReplyId[0]), "", 1, new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();
                                }

                                @Override
                                public void onSuccess(Object object) {
                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();

                                    JsonObject jObj = (JsonObject) object;
                                    commentDialogBinding.edtComment.setText("");
                                    // PostUtils.commentUpdate(post, 1);

                                    commentReplyId[0] = -1;
                                    commentReplyInnerId[0] = -1;

                                    CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("reply_data").toString(), CommentData.class);
                                    //arrComments[0].get(commentReplyMainPosition[0]).getReplyData().get(commentReplyPosition[0]).getReplyData().add(bcd);
                                    arrComments[0].get(commentReplyMainPosition[0]).getReplyData().add(bcd);
                                    commentAdapter.notifyDataSetChanged();
                                    //commentDialogBinding.rvComment.smoothScrollToPosition(commentReplyPosition[0]);

                                    commentString[0] = "";

                                    if (arrComments[0].isEmpty()) {
                                        commentDialogBinding.rvComment.setVisibility(View.GONE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                    } else {
                                        commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                    }

                                    if (commentDialogBinding.rvComment.getAdapter().getItemCount() > 0) {
                                        commentDialogBinding.rvComment.scrollToPosition(arrComments[0].size() - 1);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();
                                }
                            });

                        } else {
                            CommonAPI.createCommentApi(me, null, imageUri, post, "", 1, new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();
                                }

                                @Override
                                public void onSuccess(Object object) {

                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();

                                    JsonObject jObj = (JsonObject) object;
                                    commentDialogBinding.edtComment.setText("");
                                    PostUtils.commentUpdate(post, 1);

                                    CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("comment_data").toString(), CommentData.class);
                                    arrComments[0].add(bcd);
                                    commentAdapter.notifyDataSetChanged();
                                    //commentDialogBinding.rvComment.smoothScrollToPosition(0);

                                    commentString[0] = "";

                                    if (arrComments[0].isEmpty()) {
                                        commentDialogBinding.rvComment.setVisibility(View.GONE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                    } else {
                                        commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                    }

                                    if (commentDialogBinding.rvComment.getAdapter().getItemCount() > 0) {
                                        commentDialogBinding.rvComment.smoothScrollToPosition(arrComments[0].size() - 1);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    //Dismiss Dialog
                                    Utils.dismissProgressBar();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });

        commentDialogBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.hideSoftKeyboard(msBottomSheetDialog);
                if (!Utils.isNetworkAvailable()) {
                    AlertUtils.showSnackBar(me, BaseBinder.getLabel(me.getResources().getString(R.string.no_internet_connection)));
                    return;
                }

                if (commentDialogBinding.edtComment.getText().toString().isEmpty()) {
                    commentDialogBinding.edtComment.setSelection(commentDialogBinding.edtComment.length());
                    commentDialogBinding.edtComment.requestFocus();
                    AlertUtils.showSnackBar(me, me.getResources().getString(R.string.enterComment));
                } else {
                    commentString[0] = EmojiEncodeUtils.encode(commentDialogBinding.edtComment.getText().toString());
                    commentDialogBinding.edtComment.setText("");

                    //Show Dialog
                    Utils.showProgressBar(me);

                    if (commentReplyId[0] != -1) {
                        CommonAPI.createCommentReplyApi(me, null, null, String.valueOf(commentReplyId[0]), String.valueOf(commentReplyInnerId[0]), EmojiEncodeUtils.encode(commentString[0].replace(replyText, "")), 1, new CallbackTask() {
                            @Override
                            public void onFail(Object object) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();
                                commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(commentString[0]));
                            }

                            @Override
                            public void onSuccess(Object object) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();

                                JsonObject jObj = (JsonObject) object;
                                // PostUtils.commentUpdate(post, 1);
                                commentReplyId[0] = -1;

                                CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("reply_data").toString(), CommentData.class);
                                //arrComments[0].get(commentReplyMainPosition[0]).getReplyData().get(commentReplyPosition[0]).getReplyData().add(bcd);
                                arrComments[0].get(commentReplyMainPosition[0]).getReplyData().add(bcd);
                                commentAdapter.notifyDataSetChanged();

                                commentString[0] = "";

                                if (arrComments[0].isEmpty()) {
                                    commentDialogBinding.rvComment.setVisibility(View.GONE);
                                    commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                } else {
                                    commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                    commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                }
                                commentDialogBinding.rvComment.scrollToPosition(commentReplyPosition[0]);
                                //commentDialogBinding.rvComment.scrollToPosition(arrComments[0].size() - 1);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();
                                commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(commentString[0]));
                            }
                        });
                    } else {
                        CommonAPI.createCommentApi(me, null, null, post, EmojiEncodeUtils.encode(commentString[0]), 1, new CallbackTask() {
                            @Override
                            public void onFail(Object object) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();
                                commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(commentString[0]));
                            }

                            @Override
                            public void onSuccess(Object object) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();
                                JsonObject jObj = (JsonObject) object;
                                PostUtils.commentUpdate(post, 1);

                                CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("comment_data").toString(), CommentData.class);
                                arrComments[0].add(bcd);
                                commentAdapter.notifyDataSetChanged();

                                commentString[0] = "";

                                if (arrComments[0].isEmpty()) {
                                    commentDialogBinding.rvComment.setVisibility(View.GONE);
                                    commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                } else {
                                    commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                    commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                }
                                // commentDialogBinding.rvComment.scrollToPosition(0);
                                commentDialogBinding.rvComment.scrollToPosition(arrComments[0].size() - 1);
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                //Dismiss Dialog
                                Utils.dismissProgressBar();
                                commentDialogBinding.edtComment.setText(EmojiEncodeUtils.decode(commentString[0]));
                            }
                        });
                    }
                }
            }
        });

        commentDialogBinding.btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog mAudioBottomSheetDialog = new BottomSheetDialog(me);
                mAudioBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                LayoutInflater li = (LayoutInflater) me.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View sheetView = li.inflate(R.layout.audio_comment_layout, null);

                AudioCommentLayoutBinding binding = AudioCommentLayoutBinding.bind(sheetView);

                mAudioBottomSheetDialog.setContentView(binding.getRoot());
                mAudioBottomSheetDialog.setCanceledOnTouchOutside(false);
                mAudioBottomSheetDialog.setCancelable(false);

                final View v1 = (View) sheetView.getParent();
                v1.setBackgroundColor(Color.TRANSPARENT);

                binding.audioView.setOnAudioRecordListener(new AudioView.onAudioRecordListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onClose() {
                        if (isAudioView) {
                            isAudioView = false;
                            if (mAudioBottomSheetDialog != null)
                                mAudioBottomSheetDialog.dismiss();
                        } else {
                            isAudioView = true;
                            if (binding != null)
                                binding.audioView.refresh();
                            if (mAudioBottomSheetDialog != null && !mAudioBottomSheetDialog.isShowing())
                                mAudioBottomSheetDialog.show();
                        }
                    }

                    @Override
                    public void onRefresh() {
                    }

                    @Override
                    public void onDone(File file) {
                        if (isAudioView) {
                            isAudioView = false;
                            if (mAudioBottomSheetDialog != null)
                                mAudioBottomSheetDialog.dismiss();
                        } else {
                            isAudioView = true;
                            if (binding != null)
                                binding.audioView.refresh();
                            if (mAudioBottomSheetDialog != null && !mAudioBottomSheetDialog.isShowing())
                                mAudioBottomSheetDialog.show();
                        }
                        String strPAudioUrl = file.getAbsolutePath();
                        mAudioBottomSheetDialog.dismiss();

                        Utils.showProgressBar(me);

                        if (commentReplyId[0] != -1) {
                            CommonAPI.createCommentReplyApi(me, Uri.fromFile(new File(strPAudioUrl)),
                                    null, String.valueOf(commentReplyId[0]), String.valueOf(commentReplyId[0]),
                                    "",
                                    1, new CallbackTask() {
                                        @Override
                                        public void onFail(Object object) {
                                            Utils.dismissProgressBar();
                                            Toast.makeText(me, "Comment added failed,Please try again", Toast.LENGTH_SHORT).show();

                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            JsonObject jObj = (JsonObject) object;
                                            commentDialogBinding.edtComment.setText("");
                                            // PostUtils.commentUpdate(post, 1);

                                            commentReplyId[0] = -1;

                                            CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("reply_data").toString(), CommentData.class);
                                            try {
                                                arrComments[0].get(commentReplyPosition[0]).getReplyData().add(bcd);
                                                commentAdapter.notifyDataSetChanged();
                                                commentDialogBinding.rvComment.smoothScrollToPosition(commentReplyPosition[0]);
                                            } catch (Exception e) {
                                                arrComments[0].get(commentReplyMainPosition[0]).getReplyData().add(bcd);
                                                commentAdapter.notifyDataSetChanged();
                                                commentDialogBinding.rvComment.smoothScrollToPosition(commentReplyMainPosition[0]);

                                            }
                                            if (arrComments[0].isEmpty()) {
                                                commentDialogBinding.rvComment.setVisibility(View.GONE);
                                                commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                            } else {
                                                commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                                commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                            }

                                            //commentDialogBinding.rvComment.smoothScrollToPosition(commentReplyPosition[0]);

                                            Utils.dismissProgressBar();

                                            // Toast.makeText(me, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Utils.dismissProgressBar();
                                            Toast.makeText(me, "Comment added failed,Please try again", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else {
                            CommonAPI.createCommentApi(me, Uri.fromFile(new File(strPAudioUrl)), null, post, "", 1, new CallbackTask() {
                                @Override
                                public void onFail(Object object) {
                                    Utils.dismissProgressBar();
                                    Toast.makeText(me, "Comment added failed,Please try again", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onSuccess(Object object) {
                                    JsonObject jObj = (JsonObject) object;
                                    commentDialogBinding.edtComment.setText("");
                                    PostUtils.commentUpdate(post, 1);

                                    CommentData bcd = new Gson().fromJson(jObj.getAsJsonObject("comment_data").toString(), CommentData.class);
                                    arrComments[0].add(bcd);
                                    commentAdapter.notifyDataSetChanged();
                                    if (arrComments[0].isEmpty()) {
                                        commentDialogBinding.rvComment.setVisibility(View.GONE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.VISIBLE);
                                    } else {
                                        commentDialogBinding.rvComment.setVisibility(View.VISIBLE);
                                        commentDialogBinding.noData.mainView.setVisibility(View.GONE);
                                    }

                                    commentDialogBinding.rvComment.smoothScrollToPosition(0);

                                    Utils.dismissProgressBar();

                                    //Toast.makeText(me, "Comment added successfully", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    Utils.dismissProgressBar();

                                    Toast.makeText(me, "Comment added failed,Please try again", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                });

                if (!mAudioBottomSheetDialog.isShowing())
                    mAudioBottomSheetDialog.show();
            }
        });

        //add EditText Change Listener
        commentDialogBinding.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().trim().equals(replyText.trim())) {
                        commentDialogBinding.btnAudio.setVisibility(View.VISIBLE);
                        commentDialogBinding.btnSend.setVisibility(View.GONE);
                        commentDialogBinding.ivPickImage.setVisibility(View.VISIBLE);
                        commentDialogBinding.btnSmiley.setVisibility(View.GONE);
                    } else {
                        commentDialogBinding.btnAudio.setVisibility(View.GONE);
                        commentDialogBinding.btnSend.setVisibility(View.VISIBLE);
                        commentDialogBinding.ivPickImage.setVisibility(View.GONE);
                        commentDialogBinding.btnSmiley.setVisibility(View.GONE);
                    }

                    if (!commentDialogBinding.edtComment.toString().toLowerCase().contains(replyText.toLowerCase())) {
                        commentReplyInnerId[0] = 0;
                    }
                    /* }
                   else
                    {
                        commentDialogBinding.btnAudio.setVisibility(View.GONE);
                        commentDialogBinding.btnSend.setVisibility(View.VISIBLE);
                        commentDialogBinding.ivPickImage.setVisibility(View.GONE);
                        commentDialogBinding.btnSmiley.setVisibility(View.GONE);
                    }*/
                } else {
                    if (commentString[0].isEmpty()) {
                        if (commentReplyId[0] != -1) {
                            commentReplyId[0] = -1;
                        }
                    }

                    commentReplyInnerId[0] = 0;
                    commentDialogBinding.btnAudio.setVisibility(View.VISIBLE);
                    commentDialogBinding.ivPickImage.setVisibility(View.VISIBLE);
                    commentDialogBinding.btnSmiley.setVisibility(View.GONE);
                    commentDialogBinding.btnSend.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        msBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.hideSoftKeyboard(me);
                MyLg.e(Cisner.TAG, "DialogDismissListener");
                try {
                    post.getPostCommentObj().setData(arrComments[0]);

                    try {
                        if (visualizer != null) {
                            visualizer.setEnabled(false);
                            visualizer.release();
                            visualizer.setDataCaptureListener(null, 0, false, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //commentAdapter.mainBinding.btnPlay.setSelected(false);
                    if (player != null) {
                        player.stop();
                        //  mainBinding.tvPlayDuration.setText(""+ Utils.milliSecondsToTimer(0));
                        //  mainBinding.visualizer.setVisibility(View.GONE);
                        //  mainBinding.simplewaveform.setVisibility(View.VISIBLE);
                        player.reset();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        msBottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                MyLg.e(Cisner.TAG, "DialogCancelListener");

                try {
                    try {
                        if (visualizer != null) {
                            visualizer.setEnabled(false);
                            visualizer.release();
                            visualizer.setDataCaptureListener(null, 0, false, false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //commentAdapter.mainBinding.btnPlay.setSelected(false);
                    if (player != null) {
                        player.stop();
                        //    mainBinding.tvPlayDuration.setText(""+ Utils.milliSecondsToTimer(0));
                        //   mainBinding.visualizer.setVisibility(View.GONE);
                        //   mainBinding.simplewaveform.setVisibility(View.VISIBLE);
                        player.reset();
                    }

                } catch (Exception e) {

                }
            }
        });
        msBottomSheetDialog.show();
    }

    public static void openGalleryActivity(Activity Context, View view) {


        ActivityOptionsCompat options = ActivityOptionsCompat.makeBasic();
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int revealX = location[0] + (view.getWidth() / 2);
        int revealY = location[1] + (view.getHeight() / 2);


        Intent intent = new Intent(Context, AddMomentActivity.class);
        intent.putExtra(AddMomentActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(AddMomentActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra(AddMomentActivity.EXTRA_TYPE, AddMomentActivity.TYPE_COMMENT);

        ActivityCompat.startActivity(Context, intent, options.toBundle());
    }


    public static void showBottomUpgradePopup(final Activity context, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.upgrade_now_popup_view, null);

        final UpgradeNowPopupViewBinding binding = UpgradeNowPopupViewBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);


        binding.btnUpgradeMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(0);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.btnUpgradeFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(1);
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomCheckOutPopup(final Activity context, final String price, String type, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.checkout_popup_view, null);

        final CheckoutPopupViewBinding binding = CheckoutPopupViewBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        //binding.tvWalletAmount.setText("$" + MembershipListActivity.strWalletBalance);
        binding.tvWalletAmount.setText("$" + Cisner.getInstance().getProfile().getWalletBalance());

        if (type.equalsIgnoreCase("article")) {
            binding.btnPaypal.setVisibility(View.GONE);
        } else {
            binding.btnPaypal.setVisibility(View.VISIBLE);
        }

        binding.btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MembershipListActivity.strWalletBalance
                if (Float.parseFloat(price) > Cisner.getInstance().getProfile().getWalletBalance()) {
                    AlertUtils.showSnackBar(context, BaseBinder.getLabel(context.getString(R.string.you_dont_have_sufficient_balance)));
                } else {
                    if (onSearchableDialog != null) {
                        onSearchableDialog.onItemSelected(0);
                        mBottomSheetDialog.dismiss();
                    }
                }
            }
        });

        binding.btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(1);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(2);
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomReportPopup(final Activity context, int resTitle, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.report_popup_view, null);

        final ReportPopupViewBinding binding = ReportPopupViewBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.executePendingBindings();

        binding.tvTitle.setText(BaseBinder.getLabel(resTitle));
        binding.tvTitleLearn.setText(BaseBinder.getLabel(R.string.we_use_your_feedback_to_help_us_learn_something_isn_t_right));
        binding.tvCallEmg.setText(BaseBinder.getLabel(R.string.if_you_are_concerned_about_someont_who_is_in_immediate_danger));

        ArrayList<String> arrCategory = new ArrayList<>();
        arrCategory.add(BaseBinder.getLabel(R.string.nudity));
        arrCategory.add(BaseBinder.getLabel(R.string.violence));
        arrCategory.add(BaseBinder.getLabel(R.string.harassment));

        //don't change this used to store label key on server
        ArrayList<String> arrLabelkeys = new ArrayList<>();
        arrLabelkeys.add(context.getString(R.string.nudity));
        arrLabelkeys.add(context.getString(R.string.violence));
        arrLabelkeys.add(context.getString(R.string.harassment));

        final String[] strReason = new String[1];

        binding.tagGroup.setTags(arrCategory);
        binding.tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                strReason[0] = tag;
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strReason[0] != null) {
                    if (!strReason[0].toString().equalsIgnoreCase("")) {
                        if (onSearchableDialog != null) {
                            int index = arrCategory.indexOf(strReason[0]);
                            onSearchableDialog.onItemSelected(arrLabelkeys.get(index));
                            mBottomSheetDialog.dismiss();
                        }
                        mBottomSheetDialog.dismiss();
                    }
                } else {
                    showSnackBar(context, BaseBinder.getLabel(R.string.please_select_reason));
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomWithdrawPopup(final Activity context, final int type, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.withdraw_popup_view, null);

        final WithdrawPopupViewBinding binding = WithdrawPopupViewBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.lytPaypal.setVisibility(View.GONE);
        binding.imgPaypalLine.setVisibility(View.GONE);
        binding.imgPaypal.setVisibility(View.GONE);
        binding.lytWallet.setVisibility(View.GONE);
        binding.lytCard.setVisibility(View.GONE);
        if (type == 0) {
            binding.lytBank.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(BaseBinder.getLabel(context.getResources().getString(R.string.select_withdraw_option)));
        } else {
            binding.lytCard.setVisibility(View.VISIBLE);
            binding.tvTitle.setText(BaseBinder.getLabel(context.getResources().getString(R.string.select_add_option)));
        }

        binding.lytWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(0);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.lytPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(1);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.lytBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(2);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.lytCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(2);
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        mBottomSheetDialog.show();
    }

    public static void showBottomWithdrawStatusPopup(final Activity context, int status, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.wallet_request_status, null);

        final WalletRequestStatusBinding binding = WalletRequestStatusBinding.bind(sheetView);


        if (status == 0) {
            binding.step11.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
            binding.step11.setTextColor(context.getResources().getColor(R.color.white));

            binding.step21.setBackgroundResource(R.drawable.rounded_rect_blue_capsule_border);
            binding.step21.setTextColor(context.getResources().getColor(R.color.topBarColor));

            binding.step12.setTextColor(context.getResources().getColor(R.color.defaultContentColor));
            binding.step22.setTextColor(context.getResources().getColor(R.color.colorMutual));

            ViewCompat.setBackgroundTintList(binding.process2, ColorStateList.valueOf(Cisner.instance.getResources().getColor(R.color.colorMutual)));

        } else if (status == 4) {
            binding.step11.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
            binding.step11.setTextColor(context.getResources().getColor(R.color.white));

            binding.step21.setBackgroundResource(R.drawable.rounded_rect_blue_capsule);
            binding.step21.setTextColor(context.getResources().getColor(R.color.white));

            binding.step12.setTextColor(context.getResources().getColor(R.color.defaultContentColor));
            binding.step22.setTextColor(context.getResources().getColor(R.color.defaultContentColor));

        }

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(0);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        mBottomSheetDialog.show();
    }


    public static void showBottomSheetWatcherPayment(final Activity context, final double walletBalance, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.withdraw_popup_view, null);

        final WithdrawPopupViewBinding binding = WithdrawPopupViewBinding.bind(sheetView);

        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.lytPaypal.setVisibility(View.GONE);
        binding.imgPaypalLine.setVisibility(View.GONE);
        binding.imgPaypal.setVisibility(View.GONE);
        binding.lytWallet.setVisibility(View.VISIBLE);
        binding.lytCard.setVisibility(View.VISIBLE);
        binding.tvAmount.setText("$" + walletBalance);

        binding.lytWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    if (walletBalance <= 0) {
                        ResponseUtils.showMessageDialog(context, BaseBinder.getLabel(context.getResources().getString(R.string.entervalid_budget)));
                    } else {
                        onSearchableDialog.onItemSelected(0);
                        mBottomSheetDialog.dismiss();
                    }
                }
            }
        });

        binding.lytPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(1);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.lytBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(2);
                    mBottomSheetDialog.dismiss();
                }
            }
        });

        binding.lytCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchableDialog != null) {
                    onSearchableDialog.onItemSelected(2);
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        mBottomSheetDialog.show();
    }


    public static void showBottomWithdrawAddMoney(final Activity context, final OnSearchableDialog onSearchableDialog) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View sheetView = context.getLayoutInflater().inflate(R.layout.bottomsheet_addmoney, null);

        final BottomsheetAddmoneyBinding binding = BottomsheetAddmoneyBinding.bind(sheetView);


        mBottomSheetDialog.setContentView(binding.getRoot());
        final View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtAmount.getText().toString().trim().length() > 0) {
                    if (onSearchableDialog != null) {
                        onSearchableDialog.onItemSelected(binding.edtAmount.getText().toString().trim());
                        mBottomSheetDialog.dismiss();
                    }
                } else {
                    AlertUtils.showSnackBar(context, BaseBinder.getLabel(context.getResources().getString(R.string.enterAmount)));
                }
            }
        });

        mBottomSheetDialog.show();
    }

    public static void LikeList(final Activity context, final String APINAME, final int id, final PostLikeViewBinding binding, final int type, final boolean isReply) {
        LikeList(context, APINAME, id, 0, binding, type, isReply);
    }

    public static void LikeList(final Activity context, final String APINAME, final int id, final int photo_id, final PostLikeViewBinding binding, final int type, final boolean isReply) {
        //Utils.showProgressBar(context);
        try {
            JSONObject requestParams = new JSONObject();
            requestParams.put("s", Utils.getSessionValue(context));
            requestParams.put("user_id", Utils.getUserId(context));
            requestParams.put("page", pageid);

            if (type == 1 || type == 2) {
                requestParams.put("ads_id", id);
            } else if (type == 3) {
                requestParams.put("user_profile_id", id);
                requestParams.put("type", "get_users_mutual_friends");
            } else if (type == 4) {
                requestParams.put("post_id", id);
            } else if (type == 5) {
                requestParams.put("blog_id", id);
            } else if (type == 6) {
                if (isReply) {
                    requestParams.put("comment_reply_id", id);
                } else {
                    requestParams.put("comment_id", id);
                }
            } else if (type == 7) {
                requestParams.put("blog_id", id);
                requestParams.put("comment_id", blogcommentid);
                requestParams.put("comment_reply_id", blogreplycommentid);
            } else if (type == 8) {
                requestParams.put("post_id", id);
                requestParams.put("image_id", photo_id);
            }


            Utils.printLog("Request Params", requestParams.toString());

            final Call<JsonObject> labelResponse = ApiClient.getService()
                    .callPostMethod(BuildConfig.URL_COMMON + APINAME, ResponseUtils.getRequestData(requestParams.toString()));

            labelResponse.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {
                        binding.loader.setVisibility(View.GONE);
                        if (response.body() != null) {
                            Utils.dismissProgressBar();
                            String momentData = ResponseUtils.getResponseData(response.body().get("data").getAsString());
                            JsonObject jObjData = new Gson().fromJson(momentData, JsonObject.class);
                            if (ResponseUtils.isSuccess(response.body())) {

                                Utils.dismissProgressBar();
                                ArrayList<Member> arrData = new ArrayList<>();
                                if (type == 4 || type == 8) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("liked_users").getAsJsonArray().toString(), Member[].class));
                                } else if (type == 3) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("users").getAsJsonArray().toString(), Member[].class));
                                } else if (type == 1 || type == 2) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("adslike_users").getAsJsonArray().toString(), Member[].class));
                                } else if (type == 5) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("bloglike_users").getAsJsonArray().toString(), Member[].class));
                                } else if (type == 6) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("post_comments").getAsJsonArray().toString(), Member[].class));
                                } else if (type == 7) {
                                    arrData = new ArrayList<>(Utils.stringToList(jObjData.get("bloglike_users").getAsJsonArray().toString(), Member[].class));
                                }

                                if (jObjData.get("nextPage").getAsString().equals("0")) {
                                    isNextPage = false;
                                    pageid = Integer.parseInt(jObjData.get("nextPage").getAsString());
                                } else {
                                    isNextPage = true;
                                    pageid = Integer.parseInt(jObjData.get("nextPage").getAsString());
                                }

                                if (isLoadMore) {
                                    mAdapter.stopLoadMore(arrData);
                                } else {
                                    binding.shimmerViewContainer.setVisibility(View.GONE);
                                    if (arrData.size() > 0) {

                                        binding.rvLikeList.setVisibility(View.VISIBLE);
                                        setupFriends(context, id, arrData, binding, APINAME, type, isReply);

                                    } else {
                                        //binding.txtNoRecordFound.setVisibility(View.VISIBLE);
                                        binding.rvLikeList.setVisibility(View.GONE);
                                    }
                                }

                                mAdapter.setMoreDataAvailable(isNextPage);

                            } else {
                                //binding.txtNoRecordFound.setVisibility(View.VISIBLE);
                                binding.shimmerViewContainer.stopShimmer();
                                binding.shimmerViewContainer.setVisibility(View.GONE);
                                binding.rvLikeList.setVisibility(View.GONE);
                                Utils.dismissProgressBar();
                            }
                        } else {
                            //binding.txtNoRecordFound.setVisibility(View.VISIBLE);
                            binding.shimmerViewContainer.stopShimmer();
                            binding.shimmerViewContainer.setVisibility(View.GONE);
                            binding.rvLikeList.setVisibility(View.GONE);
                            Utils.dismissProgressBar();
                        }
                    } catch (Exception e) {
                        //binding.txtNoRecordFound.setVisibility(View.VISIBLE);
                        binding.shimmerViewContainer.stopShimmer();
                        binding.shimmerViewContainer.setVisibility(View.GONE);
                        binding.rvLikeList.setVisibility(View.GONE);
                        Utils.dismissProgressBar();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    //Get Failure Message
                    binding.shimmerViewContainer.stopShimmer();
                    binding.shimmerViewContainer.setVisibility(View.GONE);
                    Utils.dismissProgressBar();
                }
            });

        } catch (Exception e) {

            Utils.dismissProgressBar();
            e.printStackTrace();
        }

    }

    private static void setupFriends(final Activity context, final int adsid, final ArrayList<Member> arrData, final PostLikeViewBinding binding, final String APINAME, final int type, final boolean isReply) {
        mAdapter = new CommonAdapter<Member>(arrData) {

            public void onBind(final CommonViewHolder holder, final int position) {
                if (holder.binding instanceof RowPeopleKnowListItemBinding) {
                    ((RowPeopleKnowListItemBinding) holder.binding).setItem(arrData.get(position));
                    BaseBinder.setImageUri(((RowPeopleKnowListItemBinding) holder.binding).ivProfile, Uri.parse(arrData.get(position).getAvatar()));
                    ((RowPeopleKnowListItemBinding) holder.binding).btnAccept.setText(Utils.getMemberStatus(arrData.get(position), context));
                    if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context)) || arrData.get(position).isFriend()) {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.INVISIBLE);
                    } else {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.VISIBLE);
                    }

                    if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                        ((RowPeopleKnowListItemBinding) holder.binding).tvMutualFriends.setVisibility(View.GONE);
                    } else {
                        ((RowPeopleKnowListItemBinding) holder.binding).tvMutualFriends.setVisibility(View.VISIBLE);
                    }

                    if (!isNextPage && position == arrData.size() - 1)
                        ((RowPeopleKnowListItemBinding) holder.binding).line.setVisibility(View.GONE);

                    ((RowPeopleKnowListItemBinding) holder.binding).ivProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                context.startActivity(new Intent(context, SelfProfileActivity.class));
                            } else {
                                //context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("id", arrData.get(position).getUserId()));
                                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", arrData.get(position)));
                            }
                        }
                    });

                    ((RowPeopleKnowListItemBinding) holder.binding).tvName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                context.startActivity(new Intent(context, SelfProfileActivity.class));
                            } else {
                                //context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("id", arrData.get(position).getUserId()));
                                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", arrData.get(position)));
                            }
                        }
                    });

                   /* ((RowPeopleKnowListItemBinding) holder.binding).tvName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                context.startActivity(new Intent(context, SelfProfileActivity.class));
                            } else {
                                //context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("id", arrData.get(position).getUserId()));
                                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", arrData.get(position)));
                            }
                        }
                    });*/

                    ((RowPeopleKnowListItemBinding) holder.binding).btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int status = Utils.callMemberStatus(arrData.get(position));
                            switch (status) {
                                case 0://Already Friends
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                try {
                                                    if (((JsonObject) object).has("total_friends")) {
                                                        int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                        EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                    }
                                                } catch (Exception e) {

                                                }
                                                if (!arrData.get(position).isFriend()) {
                                                    arrData.get(position).setRequestSend(true);
                                                } else {
                                                    arrData.get(position).setFriend(false);
                                                    arrData.get(position).setRequestSend(false);
                                                    arrData.get(position).setFriendRequest(false);
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 1://Request Send
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), arrData.get(position).getUserId(), "cancel", context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            arrData.get(position).setRequestSend(false);
                                            arrData.get(position).setFriendRequest(false);
                                            arrData.get(position).setFriend(false);

                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 2://Accept Friend Request
                                    arrData.get(position).setLoading(true);
                                    //call api for accept request
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), arrData.get(position).getUserId(), "accept", context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            arrData.get(position).setRequestSend(false);
                                            arrData.get(position).setFriendRequest(false);
                                            arrData.get(position).setFriend(true);
                                            arrData.get(position).setRequestSend(false);

                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });

                                    break;
                                case 3://Block User
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.blockUnblockUser(Utils.getUserId(Cisner.getInstance()), "un-block", arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                arrData.get(position).setBlock(false);
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 4:// Add Friends
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                try {
                                                    if (((JsonObject) object).has("total_friends")) {
                                                        int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                        EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                    }
                                                } catch (Exception e) {

                                                }
                                                if (!arrData.get(position).isFriend()) {
                                                    arrData.get(position).setRequestSend(true);
                                                } else {
                                                    arrData.get(position).setFriend(false);
                                                    arrData.get(position).setRequestSend(false);
                                                    arrData.get(position).setFriendRequest(false);
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                if (getItem(position) == null)
                    return R.layout.loadmore_user;
                return R.layout.row_people_know_list_item;
            }
        };

        binding.rvLikeList.setAdapter(mAdapter);

        mAdapter.setLoadMoreListener(new CommonAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                binding.rvLikeList.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.startLoadMore();

                        isLoadMore = true;

                        LikeList(context, APINAME, adsid, binding, type, isReply);


                    }
                });
            }
        });
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, int stringId) {
        showToast(context, BaseBinder.getLabel(stringId));
    }


    public static void showTaggedListDialog(final Activity context, ArrayList<Member> members) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        View sheetView = context.getLayoutInflater().inflate(R.layout.dialog_recycler, null);

        final DialogRecyclerBinding binding = DialogRecyclerBinding.bind(sheetView);
        binding.loader.setVisibility(View.GONE);
        binding.rvItems.setVisibility(View.GONE);
        binding.rvItems.setLayoutManager(new LinearLayoutManager(context));
        binding.rvItems.setNestedScrollingEnabled(false);

        mBottomSheetDialog.setContentView(binding.getRoot());
        View v1 = (View) sheetView.getParent();
        v1.setBackgroundColor(Color.TRANSPARENT);

        binding.tvTitle.setVisibility(View.GONE);
        binding.vw1.setVisibility(View.GONE);

        setupTaggedUsers(context, members, binding, mBottomSheetDialog);


    }

    private static void setupTaggedUsers(final Activity context, final ArrayList<Member> arrData, final DialogRecyclerBinding binding, final BottomSheetDialog mBottomSheetDialog) {
        mAdapter = new CommonAdapter<Member>(arrData) {

            public void onBind(final CommonViewHolder holder, final int position) {
                if (holder.binding instanceof RowPeopleKnowListItemBinding) {
                    ((RowPeopleKnowListItemBinding) holder.binding).setItem(arrData.get(position));
                    BaseBinder.setImageUri(((RowPeopleKnowListItemBinding) holder.binding).ivProfile, Uri.parse(arrData.get(position).getAvatar()));
                    ((RowPeopleKnowListItemBinding) holder.binding).btnAccept.setText(Utils.getMemberStatus(arrData.get(position), context));

                    /*if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(Cisner.getInstance())) || arrData.get(position).isFriend()) {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.GONE);
                    } else {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.VISIBLE);
                    }*/

                    if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(Cisner.getInstance())) || arrData.get(position).isFriend()) {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.INVISIBLE);
                        ((RowPeopleKnowListItemBinding) holder.binding).tvMutualFriends.setVisibility(View.GONE);
                    } else {
                        ((RowPeopleKnowListItemBinding) holder.binding).btnview.setVisibility(View.VISIBLE);
                        ((RowPeopleKnowListItemBinding) holder.binding).tvMutualFriends.setVisibility(View.VISIBLE);
                    }

                    ((RowPeopleKnowListItemBinding) holder.binding).ivProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                context.startActivity(new Intent(context, SelfProfileActivity.class));
                            } else {
                                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", arrData.get(position)));
                            }
                        }
                    });

                    ((RowPeopleKnowListItemBinding) holder.binding).tvName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrData.get(position).getUserId().equalsIgnoreCase(Utils.getUserId(context))) {
                                context.startActivity(new Intent(context, SelfProfileActivity.class));
                            } else {
                                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("friendData", arrData.get(position)));
                            }
                        }
                    });
                    ((RowPeopleKnowListItemBinding) holder.binding).btnAccept.setVisibility(View.GONE);
                    ((RowPeopleKnowListItemBinding) holder.binding).btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int status = Utils.callMemberStatus(arrData.get(position));
                            switch (status) {
                                case 0://Already Friends
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                try {
                                                    if (((JsonObject) object).has("total_friends")) {
                                                        int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                        EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                    }
                                                } catch (Exception e) {

                                                }
                                                if (!arrData.get(position).isFriend()) {
                                                    arrData.get(position).setRequestSend(true);
                                                } else {
                                                    arrData.get(position).setFriend(false);
                                                    arrData.get(position).setRequestSend(false);
                                                    arrData.get(position).setFriendRequest(false);
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 1://Request Send
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), arrData.get(position).getUserId(), "cancel", context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);

                                            arrData.get(position).setFriendRequest(false);
                                            arrData.get(position).setFriend(true);

                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 2://Accept Friend Request
                                    arrData.get(position).setLoading(true);
                                    //call api for accept request
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.acceptRejectFriendRequest(Utils.getUserId(Cisner.getInstance()), Utils.getSessionValue(Cisner.getInstance()), arrData.get(position).getUserId(), "accept", context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);

                                            arrData.get(position).setFriendRequest(false);
                                            arrData.get(position).setFriend(false);
                                            arrData.get(position).setRequestSend(false);

                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });

                                    break;
                                case 3://Block User
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.blockUnblockUser(Utils.getUserId(Cisner.getInstance()), "un-block", arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                arrData.get(position).setBlock(false);
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                                case 4:// Add Friends
                                    arrData.get(position).setLoading(true);
                                    CommonAPI.callFriendUnfriend(Utils.getUserId(Cisner.getInstance()), arrData.get(position).getUserId(), Utils.getSessionValue(Cisner.getInstance()), context, new CallbackTask() {

                                        @Override
                                        public void onFail(Object object) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            arrData.get(position).setLoading(false);
                                        }

                                        @Override
                                        public void onSuccess(Object object) {
                                            arrData.get(position).setLoading(false);
                                            if (object != null && !object.toString().equalsIgnoreCase("success")) {
                                                try {
                                                    if (((JsonObject) object).has("total_friends")) {
                                                        int totalfriends = ((JsonObject) object).get("total_friends").getAsInt();
                                                        EventBus.getDefault().post(new ListCountChange(totalfriends));
                                                    }
                                                } catch (Exception e) {

                                                }
                                                if (!arrData.get(position).isFriend()) {
                                                    arrData.get(position).setRequestSend(true);
                                                } else {
                                                    arrData.get(position).setFriend(false);
                                                    arrData.get(position).setRequestSend(false);
                                                    arrData.get(position).setFriendRequest(false);
                                                }
                                            }
                                            mAdapter.notifyDataSetChanged();
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                    holder.binding.executePendingBindings();
                }
            }

            @Override
            public int getItemViewType(int position) {
                return R.layout.row_people_know_list_item;
            }
        };

        binding.rvItems.setAdapter(mAdapter);
        binding.rvItems.setVisibility(View.VISIBLE);
        mBottomSheetDialog.show();

    }

    public static void showUpdateDialog(Activity context) {
        new AlertDialog.Builder(context)
                .setTitle("We have new update")
                .setMessage("For a better experience, please update the application.")
                .setCancelable(false)
                .setPositiveButton(BaseBinder.getLabel(context.getResources().getString(R.string.update)), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        context.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.finish();
                    }
                }).show();
    }


}