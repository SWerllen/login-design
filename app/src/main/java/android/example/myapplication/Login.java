package android.example.myapplication;

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

public class Login extends AppCompatActivity {
    TextView emailtv;
    TextView passwordtv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        //setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main;
        emailtv=findViewById(R.id.email);
        passwordtv=findViewById(R.id.password);
    }

    private class login extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String inputemail=emailtv.getText().toString();
            String inputpass=passwordtv.getText().toString();

            String emailcheck = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern emailpar=Pattern.compile(emailcheck);
            Matcher emailmatcher=emailpar.matcher(inputemail);
            if(!emailmatcher.matches()){
                return "The email is illegal！";
            }

            if(inputpass.length()<6){
                return "The password should be more than 6 charactors！";
            }
            if(inputpass.length()>25){
                return "The name of password is too long！";
            }

            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            int time=sharedPreferences.getInt("time",0);
            for(int i=0;i<time;++i){
                String email=sharedPreferences.getString(String.format("email%d",i),"");
                String password=sharedPreferences.getString(String.format("password%d",i),"");
                if(inputemail.equals(email)&&password.equals(inputpass)){
                    return "yes";
                }
            }
            return "Not correct!";
        }
        @Override
        protected void onPostExecute(String info) {
            if(info=="yes"){
                Toast.makeText(getBaseContext(),"Welcome!",Toast.LENGTH_SHORT).show();
                onInMain();
            }
            else{
                Toast.makeText(getBaseContext(),info,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickRegister(View view){
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }

    public void onClickLogin(View view){
        new login().execute();
    }

    public void onInMain(){
        emailtv.setText("");
        passwordtv.setText("");
        Intent intent=new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }

}
