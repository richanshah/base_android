package com.example.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by cisner-1 on 12/3/18.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {

    private static final String TAG = CommonAdapter.class.getSimpleName();
    private ArrayList<T> data = new ArrayList<>();
    private boolean moreDataAvailable;
    private boolean isLoading;
    private OnLoadMoreListener loadMoreListener;


    public CommonAdapter(ArrayList<T> arrItem) {
        this.data = arrItem;
        setHasStableIds(false);
    }

    public CommonAdapter(ArrayList<T> arrItem, boolean hasStableIds) {
        this.data = arrItem;
        setHasStableIds(hasStableIds);
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
            return new CommonViewHolder(binding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new CommonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommonAdapter.CommonViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && moreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        onBind(holder, position);
        onBind(holder, position, getItem(position));
    }

    public boolean isMoreDataAvailable() {
        return moreDataAvailable;
    }

    public boolean isLoading() {
        return isLoading;
    }

    ;

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    public void addItemNotifyAll(T item) {
        this.data.add(item);
        notifyDataSetChanged();
    }

    public void removeItemNotifyAll(T item) {
        this.data.remove(item);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        try {

            this.data.add(item);
            Log.d(TAG, "additem start");
            notifyItemRangeInserted(data.size() - 1, data.size());
        } catch (Exception e) {
            Log.d(TAG, "additem start ex");
            notifyItemInserted(data.size() - 1);
        }
        Log.d(TAG, "additem start");
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }


    public T getItem(int pos) {
        return this.data.get(pos);
    }

    public void removeItem(int pos) {
        data.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeItem(T item) {
        final int pos = data.indexOf(item);
        data.remove(item);
        notifyItemRemoved(pos);
    }

    public boolean isContain(T item) {
        return this.data.indexOf(item) > -1;
    }

    public void removeItemRange(T item) {
        final int pos = data.indexOf(item);
        data.remove(item);
        try {
            Log.d(TAG, "removeRange start");
            notifyItemRangeRemoved(pos, data.size());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "removeRange start ex");
            notifyItemRemoved(pos);
        }
        Log.d(TAG, "removeRange start end");
    }

    public void updateData(ArrayList<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void startLoadMore() {
        data.add(null);
        notifyItemInserted(data.size() - 1);
    }

    public void startLoadPrevious() {
        data.add(0, null);
        notifyItemInserted(data.size() - 1);
    }

    public void stopLoadPrevious(ArrayList<T> trending) {
        data.remove(0);
        if (trending != null)
            data.addAll(0, trending);
        notifyDataSetChanged();
        isLoading = false;
    }

    public void stopLoadMore(ArrayList<T> trending) {
        data.remove(data.size() - 1);
        if (trending != null)
            data.addAll(trending);
        notifyDataSetChanged();
        isLoading = false;
    }

    public void stopLoadMore2(ArrayList<T> trending) {
        int index = data.indexOf(null);
        data.remove(index);
        if (trending != null)
            data.addAll(trending);
        notifyDataSetChanged();
        isLoading = false;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder {
        public ViewDataBinding binding;

        public CommonViewHolder(ViewDataBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public void onBind(CommonViewHolder holder, int position, T item) {

    }

//    public abstract int getLayoutId();

    public abstract void onBind(CommonViewHolder holder, int position);

}