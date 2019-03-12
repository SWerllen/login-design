package android.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    TextView usernametv;
    TextView emailtv;
    TextView passwordtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        //setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main);
        usernametv=findViewById(R.id.name);
        emailtv=findViewById(R.id.email);
        passwordtv=findViewById(R.id.password);
    }

    public void onClickLoginInRegister(View v) {
        finish();
    }

    public void onClickRegster(View view){
        new registers().execute();
    }

    private class registers extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String name=usernametv.getText().toString();
            String email=emailtv.getText().toString();
            String password=passwordtv.getText().toString();
            if(name==""||email==""||password=="") {
                return  "The info should not be empty！";
            }
            if(name.length()>25||password.length()>25) {
                return "The name of password is too long！";
            }
            Pattern namepar=Pattern.compile("[a-zA-Z]{2,}$");
            Matcher namematcher=namepar.matcher(name);
            if(!namematcher.matches()){
                return "The name should be in form of English charactors！";
            }

            String emailcheck = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern emailpar=Pattern.compile(emailcheck);
            Matcher emailmatcher=emailpar.matcher(email);
            if(!emailmatcher.matches()){
                return "The email is unlegal！";
            }

            if(password.length()<6) {
                return "The password should be more than 6 charactors!";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            int time=sharedPreferences.getInt("time",0);
            for(int i=0;i<time;++i){
                String dataemail=sharedPreferences.getString(String.format("email%d",i),"");
                String datapassword=sharedPreferences.getString(String.format("password%d",i),"");
                if(email.equals(dataemail)){
                    return "The email has existed!!";
                }
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(String.format("name%d",time),name);
            editor.putString(String.format("email%d",time),email);
            editor.putString(String.format("password%d",time),password);
            editor.putInt("time",time+1);
            editor.commit();
            return "yes";
        }
        @Override
        protected void onPostExecute(String info) {
            if(info=="yes"){
                Toast.makeText(getBaseContext(),"Register successfully!!",Toast.LENGTH_SHORT).show();
                onClickLoginInRegister(findViewById(R.id.btnLogin));
            }
            else{
                Toast.makeText(getBaseContext(),info,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onDestroy() {
        emailtv=null;
        passwordtv=null;
        usernametv=null;
        setContentView(R.layout.nulllayout);
        super.onDestroy();
    }
}
