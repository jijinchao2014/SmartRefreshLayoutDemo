package com.example.xu.news.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import com.example.xu.news.R;
import com.example.xu.news.Utils.ConstanceValue;
import com.example.xu.news.adapter.ChannelAdapter;
import com.example.xu.news.bean.Channel;
import com.example.xu.news.listener.ItemDragHelperCallBack;
import com.example.xu.news.listener.OnChannelDragListener;
import com.example.xu.news.listener.OnChannelListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 频道管理页面
 * Created by Xu on 2018/4/8
 */

public class ChannelDialogFragment extends DialogFragment implements OnChannelDragListener {
    private static final String TAG = "ChannelDialogFragment";
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_channel)
    LinearLayout ll_channel;

    private ItemTouchHelper mHelper;
    private List<Channel> mDatas = new ArrayList<>();
    private ChannelAdapter mAdapter;

    private Unbinder unbinder;// 注解
    private GestureDetector gesture; //手势识别
    // 拖拽 移动相关回调
    private OnChannelListener mOnChannelListener;

    public void setOnChannelListener(OnChannelListener onChannelListener) {
        mOnChannelListener = onChannelListener;
    }

    // DialogFragment销毁回调
    private DialogInterface.OnDismissListener mOnDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        // DialogFragment背景透明 style生效
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (null != dialog) dialog.getWindow().setWindowAnimations(R.style.dialogSlideAnim);
        return inflater.inflate(R.layout.act_channel, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        processLogic();
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas, OnChannelListener onChannelListener) {
        return newInstance(selectedDatas, unselectedDatas, onChannelListener, null);
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas, DialogInterface.OnDismissListener onDismissListener) {
        return newInstance(selectedDatas, unselectedDatas, null, onDismissListener);
    }

    public static ChannelDialogFragment newInstance(List<Channel> selectedDatas, List<Channel> unselectedDatas, OnChannelListener onChannelListener, DialogInterface.OnDismissListener onDismissListener) {
        ChannelDialogFragment dialogFragment = new ChannelDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstanceValue.DATA_SELECTED, (Serializable) selectedDatas);
        bundle.putSerializable(ConstanceValue.DATA_UNSELECTED, (Serializable) unselectedDatas);
        dialogFragment.setArguments(bundle);
        dialogFragment.mOnChannelListener = onChannelListener;
        dialogFragment.mOnDismissListener = onDismissListener;
        return dialogFragment;
    }

    private void setDataType(List<Channel> datas, int type) {
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setItemType(type);
        }
    }

    private void processLogic() {
        mDatas.add(new Channel(Channel.TYPE_MY, "我的频道", ""));
        Bundle bundle = getArguments();
        List<Channel> selectedDatas = (List<Channel>) bundle.getSerializable(ConstanceValue.DATA_SELECTED);
        List<Channel> unselectedDatas = (List<Channel>) bundle.getSerializable(ConstanceValue.DATA_UNSELECTED);
        setDataType(selectedDatas, Channel.TYPE_MY_CHANNEL);
        setDataType(unselectedDatas, Channel.TYPE_OTHER_CHANNEL);

        mDatas.addAll(selectedDatas);
        mDatas.add(new Channel(Channel.TYPE_OTHER, "频道推荐", ""));
        mDatas.addAll(unselectedDatas);


        mAdapter = new ChannelAdapter(mDatas);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                return itemViewType == Channel.TYPE_MY_CHANNEL || itemViewType == Channel.TYPE_OTHER_CHANNEL ? 1 : 4;
            }
        });
        ItemDragHelperCallBack callBack = new ItemDragHelperCallBack(this);
        mHelper = new ItemTouchHelper(callBack);
        mAdapter.setOnChannelDragListener(this);
        //attachRecyclerView
        mHelper.attachToRecyclerView(mRecyclerView);
    }

    @OnClick(R.id.icon_collapse)
    public void onClick(View v) {
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mOnDismissListener != null)
            mOnDismissListener.onDismiss(dialog);
        if (null != unbinder) unbinder.unbind();
    }

    @Override
    public void onStarDrag(BaseViewHolder baseViewHolder) {
        // 开始拖动
        Log.e(TAG, "开始拖动");
        mHelper.startDrag(baseViewHolder);
    }


    @Override
    public void onItemMove(int starPos, int endPos) {
        //        if (starPos < 0||endPos<0) return;
        // 我的频道之间移动
        if (mOnChannelListener != null)
            mOnChannelListener.onItemMove(starPos - 1, endPos - 1);//去除标题所占的一个index
        onMove(starPos, endPos);
    }

    private void onMove(int starPos, int endPos) {
        Channel startChannel = mDatas.get(starPos);
        // 先删除之前的位置
        mDatas.remove(starPos);
        // 添加到现在的位置
        mDatas.add(endPos, startChannel);
        mAdapter.notifyItemMoved(starPos, endPos);
    }

    @Override
    public void onMoveToMyChannel(int starPos, int endPos) {
        // 移动到我的频道
        onMove(starPos, endPos);

        if (mOnChannelListener != null)
            mOnChannelListener.onMoveToMyChannel(starPos - 1 - mAdapter.getMyChannelSize(), endPos - 1);
    }

    @Override
    public void onMoveToOtherChannel(int starPos, int endPos) {
        // 移动到推荐频道
        onMove(starPos, endPos);
        if (mOnChannelListener != null)
            mOnChannelListener.onMoveToOtherChannel(starPos - 1, endPos - 2 - mAdapter.getMyChannelSize());
    }
}
