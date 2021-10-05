package org.teamhavei.havei.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.teamhavei.havei.R;

public class FragmentNumPad extends BottomSheetDialogFragment {

    public static final int MODE_NORMAL = 0;
    public static final int MODE_BOTTOM_SHEET = 1;

    private View.OnClickListener numPadBehavior;

    int mode = MODE_NORMAL;

    EditText numberET;
    double originalValue;

    NumPadCallback callback;

    public FragmentNumPad(NumPadCallback callBack, int mode) {
        this.callback = callBack;
        this.mode = mode;
    }

    public FragmentNumPad(NumPadCallback callBack, int mode, double curValue) {
        this.callback = callBack;
        this.mode = mode;
        originalValue = curValue;
    }

    public EditText getNumberET() {
        return numberET;
    }

    public interface NumPadCallback {
        void onConfirm(Double number);
    }

    public void setOriginalValue(double originalValue) {
        this.originalValue = originalValue;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(this.getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numPadBehavior = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.numpad_0:
                        tryAppend("0");
                        break;
                    case R.id.numpad_1:
                        tryAppend("1");
                        break;
                    case R.id.numpad_2:
                        tryAppend("2");
                        break;
                    case R.id.numpad_3:
                        tryAppend("3");
                        break;
                    case R.id.numpad_4:
                        tryAppend("4");
                        break;
                    case R.id.numpad_5:
                        tryAppend("5");
                        break;
                    case R.id.numpad_6:
                        tryAppend("6");
                        break;
                    case R.id.numpad_7:
                        tryAppend("7");
                        break;
                    case R.id.numpad_8:
                        tryAppend("8");
                        break;
                    case R.id.numpad_9:
                        tryAppend("9");
                        break;
                    case R.id.numpad_dot:
                        tryAppend(".");
                        break;
                    case R.id.numpad_back:
                        back();
                        break;
                    case R.id.numpad_confirm:
                        confirm();
                        break;
                }
            }
        };
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numpad, container, false);
        initView(view);
        if (originalValue != 0) {
            numberET.setText(String.format("%.2f",originalValue));
        }
        return view;
    }

    void initView(View view) {
        numberET = view.findViewById(R.id.number);
        view.findViewById(R.id.numpad_0).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_1).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_2).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_3).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_4).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_5).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_6).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_7).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_8).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_9).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_dot).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_back).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_confirm).setOnClickListener(numPadBehavior);
        view.findViewById(R.id.numpad_back).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                numberET.setText("");
                return true;
            }
        });
    }

    private void tryAppend(String s) {
        String curNum = numberET.getText().toString();
        if (curNum.split("\\.")[0].length()>=6 && !curNum.contains(".") && !s.equals(".")) {
            return;
        }
        if (curNum.contains(".")) {
            if (s.equals(".")) {
                return;
            } else {
                int dotIndex = curNum.indexOf(".");
                if (curNum.length() - dotIndex - 1 >= 2) {
                    return;
                }
            }
        }
        if (curNum.equals("") && s.equals(".")) {
            numberET.append("0");
        }
        if(curNum.equals("0") && !s.equals(".")){
            numberET.setText("");
        }
        numberET.append(s);
    }

    private void back() {
        String curNum = numberET.getText().toString();
        if (!curNum.equals("")) {
            numberET.getText().delete(curNum.length() - 1, curNum.length());
        }
    }

    private void confirm() {
        callback.onConfirm(getNum());
        if (mode == MODE_BOTTOM_SHEET) {
            dismiss();
        }
    }

    public double getNum() {
        if (numberET.getText().toString().equals("")) {
            return 0;
        }
        return Double.parseDouble(numberET.getText().toString());
    }
}
