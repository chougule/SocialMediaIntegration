package dipu.socialmediaintegration;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button fb, google, twitter,loadimage;
    ImageView profile;
    TextView detail;
    LoginButton loginButton;
    CallbackManager callbackManager;
    FloatingActionButton main,whatsapp,gmail,message;
    TextView tv_whatsapp,tv_gmail,tv_message;
    boolean isopen=false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        fb = (Button) findViewById(R.id.button);
        google = (Button) findViewById(R.id.button2);
        twitter = (Button) findViewById(R.id.button3);
        loadimage= (Button) findViewById(R.id.button4);
        profile = (ImageView) findViewById(R.id.profileimage);
        detail = (TextView) findViewById(R.id.textView);
        tv_whatsapp= (TextView) findViewById(R.id.text_whatsapp);
        tv_gmail= (TextView) findViewById(R.id.tv_gmail);
        tv_message= (TextView) findViewById(R.id.tv_message);

        main= (FloatingActionButton) findViewById(R.id.fab_main);
        whatsapp=(FloatingActionButton) findViewById(R.id.fab_whatsapp);
        gmail=(FloatingActionButton) findViewById(R.id.fab_gmail);
        message=(FloatingActionButton) findViewById(R.id.fab_message);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        main.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        gmail.setOnClickListener(this);
        message.setOnClickListener(this);
        loadimage.setOnClickListener(this);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isopen){

                    main.startAnimation(rotate_backward);
                    whatsapp.startAnimation(fab_close);
                    gmail.startAnimation(fab_close);
                    message.startAnimation(fab_close);

                    tv_gmail.startAnimation(fab_close);
                    tv_message.startAnimation(fab_close);
                    tv_whatsapp.startAnimation(fab_close);

                    whatsapp.setClickable(false);
                    gmail.setClickable(false);
                    message.setClickable(false);
                    isopen = false;
                    Log.d("Raj", "close");
                }else {

                    main.startAnimation(rotate_forward);
                    whatsapp.startAnimation(fab_open);
                    gmail.startAnimation(fab_open);
                    message.startAnimation(fab_open);
                    tv_whatsapp.startAnimation(fab_open);
                    tv_message.startAnimation(fab_open);
                    tv_gmail.startAnimation(fab_open);

                    whatsapp.setClickable(true);
                    gmail.setClickable(true);
                    message.setClickable(false);
                    isopen = true;

                }
            }
        });

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_friends"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                graphRequest(loginResult.getAccessToken());
                //GraphRequest request=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),MainActivity.this);

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        fb.setOnClickListener(this);
        google.setOnClickListener(this);
        twitter.setOnClickListener(this);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                detail.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                detail.setText("Login attempt failed.");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();
    }

    public void graphRequest(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {

            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String firstName = "NA";
                    String lastName = "NA";
                    String email = "NA";
                    String birthday = "NA";
                    String gender = "NA";
                    String userId = object.getString("id");
                    URL profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                    if (object.has("first_name")) {
                        firstName = object.getString("first_name");
                    }
                    if (object.has("last_name")) {
                        lastName = object.getString("last_name");
                    }
                    if (object.has("email")) {
                        email = object.getString("email");
                    }
                    if (object.has("birthday")) {
                        birthday = object.getString("birthday");
                    }
                    if (object.has("gender")) {
                        gender = object.getString("gender");
                    }

                    detail.setText("Name :-" + firstName + " " + lastName + "\n" + "Email:-" + email + "\n" + "Birthday:-" + birthday + "\n" + "Gendar:-" + gender);
                    Picasso.with(MainActivity.this)
                            .load(profilePicture.toString())
                            .error(R.drawable.com_facebook_button_icon)
                            .into(profile);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle b = new Bundle();
        //b.putString("fields","id,email,first_name,last_name,picture.type(large)");
        b.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
        request.setParameters(b);
        request.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Picasso.with(MainActivity.this)
                        .load(resultUri)
                        .error(R.drawable.com_facebook_button_icon)
                        .into(profile);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();


        if (id == fb.getId()) {

            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email", "user_friends"));

        } else if (id == twitter.getId()) {

        } else if (id == google.getId()) {

        }else if(id==loadimage.getId()){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
    }
}
