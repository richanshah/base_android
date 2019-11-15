package com.example.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cisner.cisnerapp.utils.$IndexLinkedHashSet;


public abstract class ImageListAdapter<T> extends RecyclerView.Adapter<ImageListAdapter.CommonViewHolder> {

    private $IndexLinkedHashSet<T> data = new $IndexLinkedHashSet<>();
    private boolean moreDataAvailable;
    private boolean isLoading;
    private OnLoadMoreListener loadMoreListener;


    public ImageListAdapter($IndexLinkedHashSet<T> arrItem) {
        this.data = arrItem;
        setHasStableIds(false);
    }

    public ImageListAdapter($IndexLinkedHashSet<T> arrItem,boolean hasStableIds) {
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
    public void onBindViewHolder(ImageListAdapter.CommonViewHolder holder, int position) {
        if (position >= getItemCount() - 1 && moreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true;
            loadMoreListener.onLoadMore();
        } else {
            onBind(holder, position);
            onBind(holder, position, getItem(position));
        }
    }

    public boolean isMoreDataAvailable() {
        return moreDataAvailable;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        this.moreDataAvailable = moreDataAvailable;
    }

    public void addItem(T item) {
        this.data.add(item);
        notifyItemInserted(data.size() - 1);
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
        data.remove(item);
        notifyItemRemoved(data.indexOf(item));
    }

    public void updateData($IndexLinkedHashSet<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void startLoadMore() {
        data.add(null);
        notifyItemInserted(data.size() - 1);
    }

    public void stopLoadMore($IndexLinkedHashSet<T> trending) {
        data.remove(data.size() - 1);
        if (trending != null)
            data.addAll(trending);
        notifyDataSetChanged();
        isLoading = false;
    }

    public $IndexLinkedHashSet<T> getData() {
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
