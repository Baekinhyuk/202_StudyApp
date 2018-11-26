package cau.study_202;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import cau.study_202.network.Phprequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckInFragment extends Fragment {
    private Activity activity;
    String pd;
    int method;
    int state;

    static final int REQUEST_TAKE_PHOTO = 1;
    private Uri photoUri;
    boolean check = true;
    String imageFilePath;
    String ConvertImage;
    String GetImageNameFromEditText;
    String ImageNameFieldOnServer = "image_name" ;
    String ImagePathFieldOnServer = "image_path" ;
    ProgressDialog progressDialog ;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
    public CheckInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in, container, false);
        Button photoButton = rootView.findViewById(R.id.check_to_photo);
        Button beaconButton = rootView.findViewById(R.id.check_to_bluetooth);
        Button gpsButton = rootView.findViewById(R.id.check_to_gps);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makepd(0);
                method = 0;
                CheckIn task = new CheckIn();
                task.execute( Phprequest.BASE_URL+"check_in.php","");
            }
        });
        beaconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = 1;
                makepd(1);
                CheckIn task = new CheckIn();
                task.execute( Phprequest.BASE_URL+"check_in.php","");
            }
        });
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method = 2;
                makepd(2);
                CheckIn task = new CheckIn();
                task.execute( Phprequest.BASE_URL+"check_in.php","");
            }
        });
        return rootView;
    }
    private void makepd(int method) {
        pd = "ID="+LoginStatus.getMemberID()+"&"
                +"GROUPID="+LoginStatus.getGroupID()+"&"
                +"METHOD="+method;
    }
    public class CheckIn extends AsyncTask<String, Void, String> {
        String errorString = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("0")){ // 출석 가능한 시간
                if(method == 0) { // 사진으로 출석시
                    int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                    if(permissionCheck == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);

                    } else {
                        dispatchTakePictureIntent();
                        state = 0;
                    }

                } else if(method == 1) { // beacon으로 출석시
                    Intent i = new Intent(getActivity(), BeaconActivity.class);
                    i.putExtra("state",0);
                    startActivity(i);
                } else if(method == 2) { // GPS로 출석시
                    Intent i = new Intent(getActivity(), GpsActivity.class);
                    i.putExtra("state",0);
                    startActivity(i);
                }

            }
            else if(result.equals("1")){ // 지각 시간
                if(method == 0) { // 사진으로 출석시
                    int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
                    if(permissionCheck == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, 0);

                    } else {
                        dispatchTakePictureIntent();
                        state = 1;
                    }

                } else if(method == 1) { // beacon으로 출석시
                    Intent i = new Intent(getActivity(), BeaconActivity.class);
                    i.putExtra("state",1);
                    startActivity(i);
                } else if(method == 2) { // GPS로 출석시
                    Intent i = new Intent(getActivity(), GpsActivity.class);
                    i.putExtra("state",1);
                    startActivity(i);
                }
            } else if(result.equals("2")){ // 결석
                Toast.makeText(activity,"결석",Toast.LENGTH_SHORT).show();
            } else if(result.equals("3")) { // 출석시간 아님
                Toast.makeText(activity, "출석은 한시간 전부터 가능합니다.", Toast.LENGTH_SHORT).show();
            } else // 이미 출석함
                Toast.makeText(activity,"이미 출석하셨습니다.",Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(15000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(pd.getBytes("utf-8"));
                outputStream.flush();
                outputStream.close();
                String result = readStream(conn.getInputStream());
                conn.disconnect();
                Log.i("PHPRequest", result);
                return result;
            } catch (Exception e) {
                Log.d("thread", "GetData : Error ", e);
                errorString = e.toString();
                return null;
            }
        }
        private String readStream(InputStream in) throws IOException {
            StringBuilder jsonHtml = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String line = null;
            while((line = reader.readLine()) != null)
                jsonHtml.append(line);
            reader.close();
            return jsonHtml.toString();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        GetImageNameFromEditText = timeStamp;
        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("파일생성", "파일생성에 문제");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(activity, activity.getPackageName(),
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == activity.RESULT_OK) {


            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;



            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, bmOptions);
            //Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            bitmap = rotate(bitmap, exifDegree);

            ConvertImage = BitMapToString(bitmap);

            AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();

            AsyncTaskUploadClassOBJ.execute();



        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(activity,"출석 처리 중","잠시만 기다려 주세요.",false,false);
        }

        @Override
        protected void onPostExecute(String string1) {

            super.onPostExecute(string1);
            progressDialog.dismiss();
            // Printing uploading success message coming from server on android app.
            if(string1.equals("1") && state == 0)
                Toast.makeText(activity,"출석",Toast.LENGTH_SHORT).show();
            else if(string1.equals("1") && state == 1)
                Toast.makeText(activity, "지각", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(activity, "문제 발생", Toast.LENGTH_SHORT).show();
                Log.i("사진으로 출석", string1);
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            ImageProcessClass imageProcessClass = new ImageProcessClass();
            HashMap<String,String> HashMapParams = new HashMap<String,String>();
            HashMapParams.put(ImageNameFieldOnServer, GetImageNameFromEditText);
            HashMapParams.put(ImagePathFieldOnServer, ConvertImage);
            String FinalData = imageProcessClass.ImageHttpRequest(Phprequest.BASE_URL+"create_image_board.php", HashMapParams);
            return FinalData;
        }

        public class ImageProcessClass{

            public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

                StringBuilder stringBuilder = new StringBuilder();

                try {
                    Log.i("왜안되는데",requestURL);
                    URL url;
                    HttpURLConnection httpURLConnectionObject ;
                    OutputStream OutPutStream;
                    BufferedWriter bufferedWriterObject ;
                    BufferedReader bufferedReaderObject ;
                    int RC ;

                    url = new URL(requestURL);

                    httpURLConnectionObject = (HttpURLConnection) url.openConnection();

                    httpURLConnectionObject.setReadTimeout(19000);

                    httpURLConnectionObject.setConnectTimeout(19000);

                    httpURLConnectionObject.setRequestMethod("POST");

                    httpURLConnectionObject.setDoInput(true);

                    httpURLConnectionObject.setDoOutput(true);

                    OutPutStream = httpURLConnectionObject.getOutputStream();

                    bufferedWriterObject = new BufferedWriter(

                            new OutputStreamWriter(OutPutStream, "UTF-8"));

                    bufferedWriterObject.write(bufferedWriterDataFN(PData));

                    bufferedWriterObject.flush();

                    bufferedWriterObject.close();

                    OutPutStream.close();

                    RC = httpURLConnectionObject.getResponseCode();

                    if (RC == HttpsURLConnection.HTTP_OK) {

                        bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));

                        stringBuilder = new StringBuilder();

                        String RC2;

                        while ((RC2 = bufferedReaderObject.readLine()) != null){

                            stringBuilder.append(RC2);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return stringBuilder.toString();
            }

            private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

                StringBuilder stringBuilderObject;

                stringBuilderObject = new StringBuilder();

                for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {

                    if (check)

                        check = false;
                    else
                        stringBuilderObject.append("&");

                    stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                    stringBuilderObject.append("=");

                    stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
                }
                stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode("memberID", "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(LoginStatus.getMemberID(), "UTF-8"));

                stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode("groupID", "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(LoginStatus.getGroupID()+"", "UTF-8"));

                stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode("state", "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(state+"", "UTF-8"));

                return stringBuilderObject.toString();
            }

        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

}