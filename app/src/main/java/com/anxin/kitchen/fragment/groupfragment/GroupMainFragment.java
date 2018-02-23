package com.anxin.kitchen.fragment.groupfragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anxin.kitchen.bean.ContactEntity;
import com.anxin.kitchen.bean.MenuEntity;
import com.anxin.kitchen.bean.UserEntity;
import com.anxin.kitchen.decoration.IndexStickyViewDecoration;
import com.anxin.kitchen.fragment.HomeBaseFragment;
import com.anxin.kitchen.user.R;
import com.anxin.kitchen.utils.Log;
import com.bluetooth.tangwuyang.fantuanlibrary.IndexStickyView;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexHeaderFooterAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.adapter.IndexStickyViewAdapter;
import com.bluetooth.tangwuyang.fantuanlibrary.entity.BaseEntity;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemClickListener;
import com.bluetooth.tangwuyang.fantuanlibrary.listener.OnItemLongClickListener;

import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 饭团主界面
 */
public class GroupMainFragment extends HomeBaseFragment implements View.OnClickListener ,OnItemClickListener,OnItemLongClickListener{
    private Log LOG = Log.getLog();
    private View view;
    SwipeRefreshLayout mRefreshLayout;
    IndexStickyView mIndexStickyView;
    MyIndexStickyViewAdapter mAdapter;
    private ImageView mMenuImg;

    IndexHeaderFooterAdapter<UserEntity> mFavAdapter;
    IndexHeaderFooterAdapter<MenuEntity> mMenuAdapter;
    IndexHeaderFooterAdapter<UserEntity> mNormalAdapter;
    IndexHeaderFooterAdapter mBannerAdapter;
    IndexHeaderFooterAdapter<UserEntity> mFooterAdapter;
    IndexHeaderFooterAdapter mFooterBannerAdapter;


    private List<String> mMenuNames = new ArrayList<>();
    private List<Integer> mMenuImgs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.group_main_fragment, null);
        //initView();//初始化界面控制
        //initEditData();

        mRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        mIndexStickyView = view.findViewById(R.id.indexStickyView);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setRefresh();
        mAdapter = new MyIndexStickyViewAdapter(initDatas());
        mIndexStickyView.setAdapter(mAdapter);
        mIndexStickyView.addItemDecoration(new IndexStickyViewDecoration(getContext()));
        //添加饭团组
        addFanGroup();
        setSearch();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
    }

    //下拉刷新
    private void setRefresh() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(true);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.reset(initDatas());
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void setSearch() {
        //头部搜索
        mBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);
                vh.search_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "搜索", Toast.LENGTH_SHORT).show();
                    }
                });
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {

            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                RelativeLayout search_rl;

                public ImageViewVH(View itemView) {
                    super(itemView);
                    search_rl = (RelativeLayout) itemView.findViewById(R.id.search_rl);
                }
            }
        };
        mIndexStickyView.addIndexHeaderAdapter(mBannerAdapter);
    }

    private void addFanGroup() {
        mMenuAdapter = new IndexHeaderFooterAdapter<MenuEntity>("↑", "饭团", initMenuDatas()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, MenuEntity itemData) {

                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setVisibility(View.GONE);
                contentViewHolder.mName.setText(itemData.getMenuTitle());
                contentViewHolder.mAvatar.setImageResource(itemData.getMenuIconRes());
            }
        };
        mMenuAdapter.setOnItemLongClickListener(new OnItemLongClickListener<MenuEntity>() {
            @Override
            public  void onItemLongClick(View childView, int position, MenuEntity item) {
                Toast.makeText(mContext, "长按：" + item.getMenuTitle() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexHeaderAdapter(mMenuAdapter);
    }

    /**
     * 有需要可以添加顶部视图
     * */
    private void  addTopView(){
        //自定义添加头部收藏信息  如有需要 可以添加头部尾部自定义
        mFavAdapter = new IndexHeaderFooterAdapter<UserEntity>("☆", "我的收藏", initFavDatas()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, UserEntity itemData) {

                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setText(itemData.getMobile());
                contentViewHolder.mName.setText(itemData.getName());
            }
        };
        mFavAdapter.setOnItemClickListener(new OnItemClickListener<UserEntity>() {
            @Override
            public void onItemClick(View childView, int position, UserEntity item) {
                Toast.makeText(mContext, "点击：" + item.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexHeaderAdapter(mFavAdapter);
    }

    /**
     * 有需要可以添加底部视图
     * */
    private void addBottomView(){
        //添加一个底部自定义列表
        mFooterAdapter = new IndexHeaderFooterAdapter<UserEntity>("$", "Footer", initFavDatas()) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
                return new ContentViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, UserEntity itemData) {
                ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
                contentViewHolder.mMobile.setText(itemData.getMobile());
                contentViewHolder.mName.setText(itemData.getName());
            }
        };
        mFooterAdapter.setOnItemClickListener(new OnItemClickListener<UserEntity>() {
            @Override
            public void onItemClick(View childView, int position, UserEntity item) {
                Toast.makeText(mContext, "点击Footer：" + item.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
            }
        });
        mIndexStickyView.addIndexFooterAdapter(mFooterAdapter);

        //Footer Banner
        mFooterBannerAdapter = new IndexHeaderFooterAdapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {

                View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_header_contact_banner, parent, false);
                ImageViewVH vh = new ImageViewVH(view);
                vh.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Footer图片视图点击", Toast.LENGTH_SHORT).show();
                    }
                });
                return vh;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, BaseEntity itemData) {

            }

            class ImageViewVH extends RecyclerView.ViewHolder {
                ImageView img;

                public ImageViewVH(View itemView) {
                    super(itemView);
                    img = (ImageView) itemView.findViewById(R.id.img);
                }
            }
        };
        mIndexStickyView.addIndexFooterAdapter(mFooterBannerAdapter);

    }

    ContactEntity mAdd = new ContactEntity("阿圆add", "15525672987");
    List<ContactEntity> mAddCollections = new ArrayList<>();
    void initEditData() {

        mAddCollections.add(new ContactEntity("阿圆add Collections1", "15525672987"));
        mAddCollections.add(new ContactEntity("阿圆add Collections2", "15525672987"));
    }

    private List<UserEntity> initFavDatas() {
        List<UserEntity> list = new ArrayList<>();
        list.add(new UserEntity("张三", "13298449923"));
        list.add(new UserEntity("李四", "13298449923"));
        return list;
    }

    private List<MenuEntity> initMenuDatas() {
        List<MenuEntity> list = new ArrayList<>();
        list.add(new MenuEntity("星期一午饭团（12人）", R.drawable.vector_contact_focus));
        list.add(new MenuEntity("星期一晚饭团（12人）", R.drawable.vector_contact_focus));
        return list;
    }

    private List<ContactEntity> initDatas() {

        List<ContactEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.contact_array));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.mobile_array));
        for (int i = 0; i < contactStrings.size(); i++) {
            ContactEntity contactEntity = new ContactEntity(contactStrings.get(i), mobileStrings.get(i));
            list.add(contactEntity);
        }
        return list;
    }
    private void initView() {
        ((TextView)getActivity().findViewById(R.id.title_tv)).setText("饭团");
         mMenuImg = getActivity().findViewById(R.id.fantuan_menu_img);
        mMenuImg.setVisibility(View.VISIBLE);
        mMenuImg.setOnClickListener(this);
        getActivity().findViewById(R.id.back_img).setVisibility(View.GONE);
        mMenuNames.add("创建饭团");
        mMenuNames.add("新增团友");
        mMenuNames.add("通讯录导入");
        mMenuNames.add("邀请团友");

        mMenuImgs.add(R.drawable.create_new_group_drawale);
        mMenuImgs.add(R.drawable.new_friend_drawable);
        mMenuImgs.add(R.drawable.contactor_imp_drawable);
        mMenuImgs.add(R.drawable.invate_friend_drawable);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        initView();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fantuan_menu_img:
                popMenu();
                break;
            default:
                break;
        }
    }

    private void popMenu() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fantuan_pop_menu, null);
        ListView menuLl = contentView.findViewById(R.id.menu_ll);
        menuLl.setAdapter(new MenuAdapter());
        PopupWindow popWnd = new PopupWindow (getContext());
        popWnd.setContentView(contentView);

        popWnd.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.fan_menu_bg));
        popWnd.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popWnd.showAsDropDown(mMenuImg,550,20);
    }

    class MenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMenuNames.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fan_menu_item,viewGroup,false);
            ImageView menuImg = view.findViewById(R.id.menu_img);
            menuImg.setImageResource(mMenuImgs.get(i));
            TextView menuName = view.findViewById(R.id.menu_tv);
            menuName.setText(mMenuNames.get(i));
            return view;
        }
    }

    @Override
    public void onItemClick(View childView, int position, Object item) {
        Toast.makeText(mContext, "点击" + mAdd.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View childView, int position, Object item) {
        Toast.makeText(mContext, "长按：" + mAdd.getName() + ",位置：" + position, Toast.LENGTH_SHORT).show();
    }


    class MyIndexStickyViewAdapter extends IndexStickyViewAdapter<ContactEntity> {

        public MyIndexStickyViewAdapter(List<ContactEntity> list) {

            super(list);
        }

        @Override
        public RecyclerView.ViewHolder onCreateIndexViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_index, parent, false);
            return new IndexViewHolder(view);
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.indexsticky_item_contact, parent, false);
            return new ContentViewHolder(view);
        }

        @Override
        public void onBindIndexViewHolder(RecyclerView.ViewHolder holder, int position, String indexName) {

            IndexViewHolder indexViewHolder = (IndexViewHolder) holder;
            indexViewHolder.mTextView.setText(indexName);
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, int position, ContactEntity itemData) {

            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mMobile.setText(itemData.getMobile());
            contentViewHolder.mName.setText(itemData.getName());
           // contentViewHolder.mAvatar.setBackgroundResource(getResources().getDrawable(R.drawable.));
        }
    }


    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView mName;
        TextView mMobile;
        ImageView mAvatar;

        public ContentViewHolder(View itemView) {

            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mMobile = (TextView) itemView.findViewById(R.id.tv_mobile);
            mAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public IndexViewHolder(View itemView) {

            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_index);
        }
    }



}
