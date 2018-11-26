package cau.study_202;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.os.AsyncTask;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import cau.study_202.network.NetworkUtil;
import cau.study_202.network.Phprequest;
import static android.app.AlertDialog.*;

public class RegistActivity extends AppCompatActivity {

    private EditText editID,editEmail, editPassword, editPasswordCheck; // 아이디, 이메일, 비밀번호, 비밀번호 확인
    private EditText editName, editBirth; // 이름, 생일 입력
    private EditText editPhone; // 전화번호 입력
    private ImageButton btnIDCheck; // 이름 중복 확인 버튼
    private RadioGroup RbtnGroup; // 성별 라디오 버튼 그룹
    private ImageButton btnDone, btnCancel; // 가입, 취소 버튼
    private boolean IDcheck = false;
    private String CheckID = "";

    // datePicker 사용
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog dialog;

    private boolean isNick = false;

    //IP 주소


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        NetworkUtil.setNetworkPolicy();

        //화면회전 금지
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        editID = (EditText) findViewById(R.id.editID);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPasswordCheck = (EditText) findViewById(R.id.editPasswordCheck);

        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText)findViewById(R.id.editPhone);
        // 생년월일 입력 시 입력되는 형태
        editBirth = (EditText) findViewById(R.id.editBirth);
        editBirth.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        btnDone = (ImageButton) findViewById(R.id.btnDone);
        btnCancel = (ImageButton) findViewById(R.id.btnCancel);
        btnIDCheck = (ImageButton) findViewById(R.id.btnIDCheck);
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

        //비밀번호 일치검사 - TextWatcher 이용
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
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 취소 버튼 누르면 이전 화면으로 돌아감
        btnIDCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이디 입력 확인
                if (editID.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    editID.requestFocus();
                    return;
                }
                confirm();
            }
        });

        // 취소 버튼 누르면 이전 화면으로 돌아감
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // 아이디 입력 확인
                if (editID.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    editID.requestFocus();
                    return;
                }

                // 비밀번호 입력 확인
                if (editPassword.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    editPassword.requestFocus();
                    return;
                }

                // 비밀번호 확인칸 입력 확인
                if (editPassword.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "비밀번호 확인을 입력하세요", Toast.LENGTH_SHORT).show();
                    editPasswordCheck.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if (!editPassword.getText().toString().equals(editPasswordCheck.getText().toString())) {
                    Toast.makeText(RegistActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    editPassword.setText("");
                    editPasswordCheck.setText("");
                    editPassword.requestFocus();
                    return;
                }
                // 이메일 입력 확인
                if (editEmail.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                    editEmail.requestFocus();
                    return;
                }
                // 전화번호 입력 확인
                if (editPhone.getText().toString().length() == 0) {
                    Toast.makeText(RegistActivity.this, "전화번호를 입력하세요", Toast.LENGTH_SHORT).show();
                    editPhone.requestFocus();
                    return;
                }
                if (!checkEmailForm(editEmail.getText().toString())) {               //이메일 형식 검사
                    Toast.makeText(getApplicationContext(), "이메일 형식을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                    editEmail.requestFocus();
                    return;
                }
                if (!checkPhoneForm(editPhone.getText().toString())) {               //이메일 형식 검사
                    Toast.makeText(getApplicationContext(), "-을 포함한 전화번호 형식을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show();
                    editPhone.requestFocus();
                    return;
                }
                /*
                if(isNick == false){
                    Toast.makeText(RegistActivity.this, "닉네임을 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    editNickname.requestFocus();
                    return;
                }*/
                //가입버튼을 눌렀을 시 현재 입력되어있는 아이디가 체크했었던 아이디인지 확인
                if (!String.valueOf(editID.getText()).equals(CheckID)) {
                    Toast.makeText(getApplicationContext(), "아이디 중복확인을 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    editID.requestFocus();
                    return;
                }
                sign_member();
            }
        });

    }

    private void confirm(){
        try {
            Phprequest request = new Phprequest(Phprequest.BASE_URL +"sameIDcheck.php");
            String result = request.sameIDcheck(String.valueOf(editID.getText()));
            if(Integer.parseInt(result)==0){
                Toast.makeText(getApplication(),"사용가능한 ID입니다",Toast.LENGTH_SHORT).show();
                IDcheck = true;
                //체크했을 당시에 아이디를 임시로 저장
                CheckID = String.valueOf(editID.getText());
            }
            if(Integer.parseInt(result)==1){
                Toast.makeText(getApplication(),"사용불가능한 ID입니다",Toast.LENGTH_SHORT).show();
                IDcheck = false;
            }
            //Toast.makeText(getActivity(),result,Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.i("회원가입확인오류","try-catch문 NULLPOINTER에러");
            GetData task = new GetData();
            task.execute();
            e.printStackTrace();
        }catch (NumberFormatException e){
            Log.i("회원가입확인오류","try-catch문 NumberFormatException에러");
            GetData task = new GetData();
            task.execute();
            e.printStackTrace();
        }
    }
    private class GetData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(RegistActivity.this);

        String errorString = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("인터넷 연결을 재시도 중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            confirm();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 100; i++) {
                    progressDialog.setProgress(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sign_member(){
        try {
            if(IDcheck==true) {
                Phprequest request = new Phprequest(Phprequest.BASE_URL + "member_regist.php");
                String result = request.registmember(String.valueOf(editID.getText()), String.valueOf(editPassword.getText()), String.valueOf(editName.getText()), String.valueOf(editEmail.getText()), String.valueOf(editPhone.getText()), String.valueOf(editBirth.getText()));
                if (result.equals("1")) {
                    Toast.makeText(getApplication(), "정상적으로 가입되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplication(), "가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            Log.i("회원가입오류","try-catch문 NULLPOINTER에러");
            GetData2 task2 = new GetData2();
            task2.execute();
            e.printStackTrace();
        }
    }

    private class GetData2 extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog = new ProgressDialog(RegistActivity.this);

        String errorString = null;

        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("인터넷 연결을 재시도 중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            sign_member();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 100; i++) {
                    progressDialog.setProgress(i);
                    Thread.sleep(10);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public boolean checkEmailForm(String src){              //이메일 형식검사
        String emailRegex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        return Pattern.matches(emailRegex, src);
    }
    public boolean checkPhoneForm(String src){              //전화번호 형식검사
        String phoneRegex = "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
        return Pattern.matches(phoneRegex, src);
    }
}

