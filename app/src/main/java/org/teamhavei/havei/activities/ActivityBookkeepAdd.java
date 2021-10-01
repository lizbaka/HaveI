package org.teamhavei.havei.activities;
// TODO: 2021.09.10 账户功能待实现

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.UniToolKit;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;
import org.teamhavei.havei.fragments.FragmentNumPad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityBookkeepAdd extends BaseActivity {

    private static final int IO_TYPE_EXPENDITURE = 0;
    private static final int IO_TYPE_INCOME = 1;

    private static final int MODE_ADD = 0;
    private static final int MODE_MODIFY = 1;

    private static final String START_PARAM_BOOKKEEP_ID = "bookkeep_id";

    private int selectedTagId;
    private int ioJudge;
    private int mode = MODE_ADD;

    EditText bookTitleET;
    RecyclerView mTagRecycRV;
    RadioGroup IORG;
    TextView dateTV;
    MaterialCardView datePikMC;

    Calendar calendar;
    BookkeepDBHelper bookDBHelper;
    List<HaveITag> mtaglist;
    TagListAdapter mTaglistAdapter;
    Bookkeep mBookkeep;

    FragmentNumPad numPad;

//    int selectAccount;
//    BookCou mBookCou;
//    double couio = 0;


    public static void startAction(Context context) {
        Intent intent = new Intent(context, ActivityBookkeepAdd.class);
        context.startActivity(intent);
    }

    public static void startAction(Context context, int bookkeepId) {
        Intent intent = new Intent(context, ActivityBookkeepAdd.class);
        intent.putExtra(START_PARAM_BOOKKEEP_ID, bookkeepId);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add);
        setSupportActionBar(findViewById(R.id.bookkeep_add_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        numPad = new FragmentNumPad(new FragmentNumPad.NumPadCallback() {
            @Override
            public void onConfirm(Double number) {
                updateBookkeep(number);
                finish();
            }
        },FragmentNumPad.MODE_NORMAL);
        getSupportFragmentManager().beginTransaction().replace(R.id.bookkeep_add_numpad, numPad).commit();

        bookDBHelper = new BookkeepDBHelper(ActivityBookkeepAdd.this, BookkeepDBHelper.DB_NAME, null, BookkeepDBHelper.DATABASE_VERSION);
        initView();
        calendar = Calendar.getInstance();
        dateTV.setText(UniToolKit.eventDateFormatter(calendar.getTime()));
        initTagList();
        initListener();

        //与numpad相关的初始化操作放到onStart中，因为onCreate完成前fragment未完成创建
        if (getIntent().getIntExtra(START_PARAM_BOOKKEEP_ID, -1) != -1) {
            mode = MODE_MODIFY;
            mBookkeep = bookDBHelper.findBookkeepById(getIntent().getIntExtra(START_PARAM_BOOKKEEP_ID, -1));
            selectedTagId = mBookkeep.gettag();
            bookTitleET.setText(mBookkeep.getname());
        } else {
            mBookkeep = new Bookkeep();
            selectedTagId = bookDBHelper.findAllBookTag(true).get(0).getId();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mode == MODE_MODIFY) {
            numPad.getNumberET().setText(Double.toString(mBookkeep.getPM()));
            if (mBookkeep.getPM() > 0) {
                IORG.check(R.id.bookkeep_add_radio_income);
            } else {
                IORG.check(R.id.bookkeep_add_radio_expenditure);
            }
        } else {
            IORG.check(R.id.bookkeep_add_radio_expenditure);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        bookTitleET = findViewById(R.id.bookkeep_add_title);
        mTagRecycRV = findViewById(R.id.bookkeep_add_tag_recyc);
        IORG = findViewById(R.id.bookkeep_add_io);
        dateTV = findViewById(R.id.tv_select_date);
        datePikMC = findViewById(R.id.date_picker);
    }

    public void initListener() {
        IORG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bookkeep_add_radio_expenditure:
                        ioJudge = IO_TYPE_EXPENDITURE;
                        numPad.getNumberET().setTextColor(ContextCompat.getColor(ActivityBookkeepAdd.this, R.color.red_500));
                        break;
                    case R.id.bookkeep_add_radio_income:
                        ioJudge = IO_TYPE_INCOME;
                        numPad.getNumberET().setTextColor(ContextCompat.getColor(ActivityBookkeepAdd.this, R.color.green_500));
                        break;
                }
            }
        });
        datePikMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog pickerDialog = new DatePickerDialog(ActivityBookkeepAdd.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        dateTV.setText(UniToolKit.eventDateFormatter(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                DatePicker picker = pickerDialog.getDatePicker();
                picker.setMaxDate(Calendar.getInstance().getTimeInMillis());
                pickerDialog.show();
            }
        });
        findViewById(R.id.bookkeep_add_tag_mng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySettingsTagMng.startAction(ActivityBookkeepAdd.this, UniToolKit.TAG_TYPE_BOOKKEEP);
            }
        });
    }

//    public void edit() {
//        Num_text = BookTitle.getText().toString();
//        now_Num = Double.parseDouble(BookNum.getText().toString());
//        if (ioJudge == 0) {
//            now_Num = now_Num * (-1);
//        }
//        sDate = Date.substring(0, 7);
//        mBookkeep = setBookkeep();
//        bookDBHelper.insertBookkeep(mBookkeep);
//        setBookCou();
//    }

    public void initTagList() {
        mtaglist = new ArrayList<>();
        mtaglist.addAll(bookDBHelper.findAllBookTag(true));
        mTaglistAdapter = new TagListAdapter(mtaglist, ActivityBookkeepAdd.this, 0, TagListAdapter.ORIENTATION_VERTICAL, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                selectedTagId = tag.getId();
            }
        });
        mTagRecycRV.setLayoutManager(new GridLayoutManager(ActivityBookkeepAdd.this, 4, GridLayoutManager.VERTICAL, false));
        mTagRecycRV.setAdapter(mTaglistAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mtaglist.clear();
        mtaglist.addAll(bookDBHelper.findAllBookTag(true));
        mTaglistAdapter.notifyDataSetChanged();
    }

    private void updateBookkeep(Double value) {
        if (value == 0) {
            Toast.makeText(ActivityBookkeepAdd.this, R.string.bookkeep_add_invalid_money, Toast.LENGTH_SHORT).show();
            return;
        }
        double io = (ioJudge == IO_TYPE_INCOME ? 1 : -1);
        mBookkeep.setname(bookTitleET.getText().toString());
        mBookkeep.setPM(value * io);
        mBookkeep.settag(selectedTagId);
        mBookkeep.setTime(UniToolKit.eventDateFormatter(calendar.getTime()));
        if (mode == MODE_ADD) {
            bookDBHelper.insertBookkeep(mBookkeep);
        } else {
            bookDBHelper.updateBookkeep(mBookkeep.getid(), mBookkeep);
        }
    }

//    public void setBookCou() {
//        // TODO: 2021.09.10 ？
//        if (bookDBHelper.findBookcouByMonth(sDate) == null) {
//            if (ioJudge == 1) {
//                couio = now_Num;
//                mBookCou.setTime(sDate);
//                mBookCou.setIn(couio);
//                mBookCou.setOut(0d);
//            } else {
//                couio = (-1) * now_Num;
//                mBookCou.setTime(sDate);
//                mBookCou.setIn(0d);
//                mBookCou.setOut(couio);
//            }
//
//            bookDBHelper.insertBookCou(mBookCou);
//        } else {
//            BookCou oldCou = bookDBHelper.findBookcouByMonth(sDate);
//            mBookCou.setTime(sDate);
//            if (ioJudge == 1) {
//                mBookCou.setIn(oldCou.getIn() + now_Num);
//                mBookCou.setOut(oldCou.getOut());
//            } else {
//                mBookCou.setIn(oldCou.getIn());
//                mBookCou.setOut(oldCou.getOut() - now_Num);
//            }
//            bookDBHelper.updateBookCou(oldCou, mBookCou);
//        }
//    }


//    public void onClick_ADD(View view) {
//
//        new BottomSheet.Builder(ActivityBookkeepAdd.this, R.style.BottomSheet_Dialog).title("選擇改變賬戶").sheet(R.menu.book_bottomsheet).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case R.id.book_bank:
//                        selectAccount = 0;
//                        break;
//
//                    case R.id.book_wechat:
//                        selectAccount = 1;
//                        break;
//                    case R.id.book_airpay:
//                        selectAccount = 2;
//                        break;
//                    case R.id.book_money:
//                        selectAccount = 3;
//                        break;
//                }
//            }
//        }).show();
//        edit();
//        finish();
//
//    }
//
//    public void onClick_BACK(View view) {
//        finish();
//    }
}


//    /**
//     *
//     * 自定义PopWindow类，封装了PopWindow的一些常用属性，用Builder模式支持链式调用
//     * Created by zhouwei on 16/11/28.
//     */
//
//    public static class CustomPopWindow {
//        private Context mContext;
//        private int mWidth;
//        private int mHeight;
//        private boolean mIsFocusable = true;
//        private boolean mIsOutside = true;
//        private int mResLayoutId = -1;
//        private View mContentView;
//        private PopupWindow mPopupWindow;
//        private int mAnimationStyle = -1;
//
//        private boolean mClippEnable = true;//default is true
//        private boolean mIgnoreCheekPress = false;
//        private int mInputMode = -1;
//        private PopupWindow.OnDismissListener mOnDismissListener;
//        private int mSoftInputMode = -1;
//        private boolean mTouchable = true;//default is ture
//        private View.OnTouchListener mOnTouchListener;
//        private CustomPopWindow(Context context){
//            mContext = context;
//        }
//
//        public int getWidth() {
//            return mWidth;
//        }
//
//        public int getHeight() {
//            return mHeight;
//        }
//
//        /**
//         *
//         * @param anchor
//         * @param xOff
//         * @param yOff
//         * @return
//         */
//        public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff){
//            if(mPopupWindow!=null){
//                mPopupWindow.showAsDropDown(anchor,xOff,yOff);
//            }
//            return this;
//        }
//
//        public CustomPopWindow showAsDropDown(View anchor){
//            if(mPopupWindow!=null){
//                mPopupWindow.showAsDropDown(anchor);
//            }
//            return this;
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//        public CustomPopWindow showAsDropDown(View anchor, int xOff, int yOff, int gravity){
//            if(mPopupWindow!=null){
//                mPopupWindow.showAsDropDown(anchor,xOff,yOff,gravity);
//            }
//            return this;
//        }
//
//
//        /**
//         * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
//         * @param parent
//         * @param gravity
//         * @param x the popup's x location offset
//         * @param y the popup's y location offset
//         * @return
//         */
//        public CustomPopWindow showAtLocation(View parent, int gravity, int x, int y){
//            if(mPopupWindow!=null){
//                mPopupWindow.showAtLocation(parent,gravity,x,y);
//            }
//            return this;
//        }
//
//        /**
//         * 添加一些属性设置
//         * @param popupWindow
//         */
//        private void apply(PopupWindow popupWindow){
//            popupWindow.setClippingEnabled(mClippEnable);
//            if(mIgnoreCheekPress){
//                popupWindow.setIgnoreCheekPress();
//            }
//            if(mInputMode!=-1){
//                popupWindow.setInputMethodMode(mInputMode);
//            }
//            if(mSoftInputMode!=-1){
//                popupWindow.setSoftInputMode(mSoftInputMode);
//            }
//            if(mOnDismissListener!=null){
//                popupWindow.setOnDismissListener(mOnDismissListener);
//            }
//            if(mOnTouchListener!=null){
//                popupWindow.setTouchInterceptor(mOnTouchListener);
//            }
//            popupWindow.setTouchable(mTouchable);
//
//
//
//        }
//
//        private PopupWindow build(){
//
//            if(mContentView == null){
//                mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId,null);
//            }
//
//            if(mWidth != 0 && mHeight!=0 ){
//                mPopupWindow = new PopupWindow(mContentView,mWidth,mHeight);
//            }else{
//                mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            }
//            if(mAnimationStyle!=-1){
//                mPopupWindow.setAnimationStyle(mAnimationStyle);
//            }
//
//            apply(mPopupWindow);//设置一些属性
//
//            mPopupWindow.setFocusable(mIsFocusable);
//            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            mPopupWindow.setOutsideTouchable(mIsOutside);
//
//            if(mWidth == 0 || mHeight == 0){
//                mPopupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//                //如果外面没有设置宽高的情况下，计算宽高并赋值
//                mWidth = mPopupWindow.getContentView().getMeasuredWidth();
//                mHeight = mPopupWindow.getContentView().getMeasuredHeight();
//            }
//
//            mPopupWindow.update();
//
//            return mPopupWindow;
//        }
//
//        /**
//         * 关闭popWindow
//         */
//        public void dismiss(){
//            if(mPopupWindow!=null){
//                mPopupWindow.dismiss();
//            }
//        }
//
//
//        public static class PopupWindowBuilder{
//            private CustomPopWindow mCustomPopWindow;
//
//            public PopupWindowBuilder(Context context){
//                mCustomPopWindow = new CustomPopWindow(context);
//            }
//            public PopupWindowBuilder size(int width,int height){
//                mCustomPopWindow.mWidth = width;
//                mCustomPopWindow.mHeight = height;
//                return this;
//            }
//
//
//            public PopupWindowBuilder setFocusable(boolean focusable){
//                mCustomPopWindow.mIsFocusable = focusable;
//                return this;
//            }
//
//
//
//            public PopupWindowBuilder setView(int resLayoutId){
//                mCustomPopWindow.mResLayoutId = resLayoutId;
//                mCustomPopWindow.mContentView = null;
//                return this;
//            }
//
//            public PopupWindowBuilder setView(View view){
//                mCustomPopWindow.mContentView = view;
//                mCustomPopWindow.mResLayoutId = -1;
//                return this;
//            }
//
//            public PopupWindowBuilder setOutsideTouchable(boolean outsideTouchable){
//                mCustomPopWindow.mIsOutside = outsideTouchable;
//                return this;
//            }
//
//            /**
//             * 设置弹窗动画
//             * @param animationStyle
//             * @return
//             */
//            public PopupWindowBuilder setAnimationStyle(int animationStyle){
//                mCustomPopWindow.mAnimationStyle = animationStyle;
//                return this;
//            }
//
//
//            public PopupWindowBuilder setClippingEnable(boolean enable){
//                mCustomPopWindow.mClippEnable =enable;
//                return this;
//            }
//
//
//            public PopupWindowBuilder setIgnoreCheekPress(boolean ignoreCheekPress){
//                mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress;
//                return this;
//            }
//
//            public PopupWindowBuilder setInputMethodMode(int mode){
//                mCustomPopWindow.mInputMode = mode;
//                return this;
//            }
//
//            public PopupWindowBuilder setOnDissmissListener(PopupWindow.OnDismissListener onDissmissListener){
//                mCustomPopWindow.mOnDismissListener = onDissmissListener;
//                return this;
//            }
//
//
//            public PopupWindowBuilder setSoftInputMode(int softInputMode){
//                mCustomPopWindow.mSoftInputMode = softInputMode;
//                return this;
//            }
//
//
//            public PopupWindowBuilder setTouchable(boolean touchable){
//                mCustomPopWindow.mTouchable = touchable;
//                return this;
//            }
//
//            public PopupWindowBuilder setTouchIntercepter(View.OnTouchListener touchIntercepter){
//                mCustomPopWindow.mOnTouchListener = touchIntercepter;
//                return this;
//            }
//
//
//            public CustomPopWindow create(){
//                //构建PopWindow
//                mCustomPopWindow.build();
//                return mCustomPopWindow;
//            }
//
//        }
//
//

//
//    ImageButton button= (ImageButton) findViewById(R.id.add_account_detail_wallet);
//    CustomPopWindow popWindow = new CustomPopWindow.PopupWindowBuilder(this)
//            .setView(R.layout.popwindow_add_dedtail_account_choice)//显示的布局，还可以通过设置一个View
//            .size(600,400) //设置显示的大小，不设置就默认包裹内容
//            .setFocusable(true)//是否获取焦点，默认为ture
//            .setOutsideTouchable(true)//是否PopupWindow 以外触摸dismiss
//            .create()//创建PopupWindow
//            .showAsDropDown(button,0,0);//显示PopupWindow
//
//
//}

