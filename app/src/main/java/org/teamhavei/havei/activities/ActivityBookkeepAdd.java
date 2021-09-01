package org.teamhavei.havei.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cocosw.bottomsheet.BottomSheet;

import org.teamhavei.havei.Event.BookCou;
import org.teamhavei.havei.Event.BookTag;
import org.teamhavei.havei.Event.Bookkeep;
import org.teamhavei.havei.Event.HaveITag;
import org.teamhavei.havei.R;
import org.teamhavei.havei.adapters.TagListAdapter;
import org.teamhavei.havei.databases.BookkeepDBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ActivityBookkeepAdd extends AppCompatActivity {

    private Integer tagId;
    private Double now_Num;
    private String Num_text;
    private int ioJud=0;
    private int dot=0;
    EditText BookTitle;
    EditText BookNum;
    RecyclerView mTagRecyc;
    RadioGroup io;
    BookkeepDBHelper bookDBHelper;
    DatePicker datePicker;
    Button datePik;
    Button dateSure;


    int selectAccount;
    List<org.teamhavei.havei.Event.HaveITag> mtaglist;
    TagListAdapter mTaglistAdapter;
    String Date;
    String sDate;
    Bookkeep mBookkeep;
    BookCou mBookCou;
    double couio=0;

    int iconid;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookkeep_add_detail);
        init();

//        io.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                RadioButton radbtn = (RadioButton) findViewById(checkedId);
//
//                if(radbtn.getText().equals(R.string.bookkeep_expenditure))
//                    ioJud=0;
//                else
//                    ioJud=1;
//            }
//        });
    }
    public void init()
    {
        selectAccount=0;
        tagId =0;
        now_Num =0d;
        dot=0;
        bookDBHelper=new BookkeepDBHelper(ActivityBookkeepAdd.this,BookkeepDBHelper.DB_NAME,null, BookkeepDBHelper.DATABASE_VERSION);
        BookTitle = (EditText) findViewById(R.id.add_account_detail_account_label);
        BookNum = (EditText) findViewById(R.id.add_account_detail__money_value);
        mTagRecyc=(RecyclerView)findViewById(R.id.add_recyc_booklist);
        io = (RadioGroup) findViewById(R.id.add_account_detail_expenditure_or_income);
        datePicker=findViewById(R.id.bookkeep_datepicker);
        datePik=findViewById(R.id.buttom_date);
        dateSure=findViewById(R.id.date_sure);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat   sDateFormat   =   new SimpleDateFormat("yyyy-MM-dd");
        Date=sDateFormat.format(new java.util.Date());
        datePik.setText(Date);
        init_tag();
        initListen();
    }

    public void edit()
    {
        Num_text=BookTitle.getText().toString();
        now_Num= Double.parseDouble(BookNum.getText().toString());
        if(ioJud==0) {
            now_Num=now_Num*(-1);
        }
        sDate=Date.substring(0,7);
        mBookkeep=setBookkeep();
        bookDBHelper.insertBookkeep(mBookkeep);
        setBookCou();
    }

    public void init_tag(){
        List<BookTag> List = bookDBHelper.findAllBoktag();
        mtaglist = new ArrayList<>();
        mtaglist.addAll(List);
        mTaglistAdapter = new TagListAdapter(mtaglist, ActivityBookkeepAdd.this,0, new TagListAdapter.OnTagClickListener() {
            @Override
            public void onClick(HaveITag tag) {
                tagId = tag.getId();
            }
        });
        mTagRecyc.setLayoutManager(new GridLayoutManager(ActivityBookkeepAdd.this, 3, GridLayoutManager.HORIZONTAL, false));
        mTagRecyc.setAdapter(mTaglistAdapter);
    }
    public void editGetDate()
    {
        datePicker.setVisibility(View.VISIBLE);
        dateSure.setVisibility(View.VISIBLE);
        String day=String.format("%d-%02d-%02d",datePicker.getYear(),datePicker.getMonth()+1,datePicker.getDayOfMonth());
        dateSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date=day;
                datePik.setText(Date);
                datePicker.setVisibility(View.GONE);
                dateSure.setVisibility(View.GONE);
            }
        });
    }


    public Bookkeep setBookkeep()
    {
        Bookkeep A=new Bookkeep();
        A.setIconId(iconid);
        A.setname(Num_text);
        A.setPM(now_Num);
        A.settag(tagId);
        A.setTime(Date);
        return A;
    }
    public void setBookCou()
    {
        if(bookDBHelper.findBookcouByMonth(sDate)==null)
        {
            if(ioJud==1)
            {
                couio=now_Num;
                mBookCou.setTime(sDate);
                mBookCou.setIn(couio);
                mBookCou.setOut(0d);
            }
            else
            {
                couio=(-1)*now_Num;
                mBookCou.setTime(sDate);
                mBookCou.setIn(0d);
                mBookCou.setOut(couio);
            }

            bookDBHelper.insertBookCou(mBookCou);
        }
        else
        {
            BookCou oldCou= bookDBHelper.findBookcouByMonth(sDate);
            mBookCou.setTime(sDate);
            if(ioJud==1)
            {
                mBookCou.setIn(oldCou.getIn()+now_Num);
                mBookCou.setOut(oldCou.getOut());
            }
            else
            {
                mBookCou.setIn(oldCou.getIn());
                mBookCou.setOut(oldCou.getOut()-now_Num);
            }
            bookDBHelper.updateBookCou(oldCou,mBookCou);
        }
    }
    public  void initListen()
    {
        //RADIO
        io.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            RadioButton radbtn = (RadioButton) findViewById(checkedId);

            if(radbtn.getText().equals(R.string.bookkeep_expenditure))
                ioJud=0;
            else
                ioJud=1;
        }
    });
        datePik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGetDate();
            }
        });

    }


    public void onClick_ADD(View view) {

        new BottomSheet.Builder(ActivityBookkeepAdd.this,R.style.BottomSheet_Dialog).title("選擇改變賬戶").sheet(R.menu.book_bottomsheet).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case R.id.book_bank:
                        selectAccount=0;
                        break;

                    case R.id.book_wechat:
                        selectAccount=1;
                        break;
                    case R.id.book_airpay:
                        selectAccount=2;
                        break;
                    case R.id.book_money:
                        selectAccount=3;
                        break;
                }
            }
        }).show();
        edit();
        finish();

    }

    public void onClick_BACK(View view) {
        finish();
    }
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

