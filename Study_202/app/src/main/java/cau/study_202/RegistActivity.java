package cau.study_202;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.app.AlertDialog.*;
/**
 * Created by HOME on 2016-06-27.
 */
public class RegistActivity extends AppCompatActivity {

    private EditText editEmail, editPassword, editPasswordCheck; // 이메일, 비밀번호, 비밀번호 확인
    private EditText editName, editNickname, editBirth; // 이름, 닉네임, 생일 입력
    private EditText editPhone; // 전화번호 입력
    private ImageButton btnNicknameCheck; // 닉네임 중복 확인 버튼
    private RadioGroup RbtnGroup; // 성별 라디오 버튼 그룹
    private ImageButton btnDone, btnCancel; // 가입, 취소 버튼

    // datePicker 사용
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog dialog;

    private boolean isNick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPasswordCheck = (EditText) findViewById(R.id.editPasswordCheck);

        editName = (EditText) findViewById(R.id.editName);
        editNickname = (EditText) findViewById(R.id.editNickname);

        // 생년월일 입력 시 필요한 것들
        editBirth = (EditText) findViewById(R.id.editBirth);
        editBirth.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        btnDone = (ImageButton) findViewById(R.id.btnDone);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnNicknameCheck = (ImageButton) findViewById(R.id.btnNicknameCheck);
        //btnClickEvent();

        // 생년월일 눌렀을 때 datePicker 띄우기
        Calendar newCalendar = Calendar.getInstance();
        dialog = new DatePickerDialog(RegistActivity.this, THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                editBirth.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        editBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        /*
        // 비밀번호 일치 검사
        editPasswordCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = editPassword.getText().toString();
                String check = editPasswordCheck.getText().toString();

                if (password.equals(check)) {
                    editPassword.setTextColor(Color.GREEN);
                    editPasswordCheck.setTextColor(Color.GREEN);
                } else {
                    editPassword.setTextColor(Color.RED);
                    editPasswordCheck.setTextColor(Color.RED);
                }

            }

        });
        */

    }
}

