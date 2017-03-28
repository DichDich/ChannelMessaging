package colas.dichan.ChannelMessaging;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dynamitechetan.flowinggradient.FlowingGradientClass;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Random;


public class LoginActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText id;
    private EditText mdp;
    private Button btnValider;
    private Handler mHandlerTada;
    private int mShortDelay;
    private TextView infoConnexion;
    private AVLoadingIndicatorView avi;
    private static final String[] explainStringArray = {
            "Connecte toi et chat avec tes amis",
            "Viens, on est bien bien bien bien bien bien",
            "<3"
    };

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnValider = (Button) findViewById(R.id.btnValider);
        id = (EditText) findViewById(R.id.editTextId);
        mdp = (EditText) findViewById(R.id.editTextMdp);
        imageView = (ImageView) findViewById(R.id.imageView);
        infoConnexion = (TextView) findViewById(R.id.textViewInfoConnexion);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);


        LinearLayout ll = (LinearLayout) findViewById(R.id.llBackground);
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(R.drawable.translate)
                .onLinearLayout(ll)
                .setTransitionDuration(4000)
                .start();

        mHandlerTada = new Handler(); // android.os.handler
        mShortDelay = 3000; //milliseconds

        mHandlerTada.postDelayed(new Runnable(){
            public void run(){
                YoYo.with(Techniques.RubberBand)
                        .duration(1000)
                        .playOn(findViewById(R.id.imageView));
                mHandlerTada.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);

        final Handler mHandlerMessage = new Handler(); // android.os.handler

        mHandlerMessage.postDelayed(new Runnable(){
            public void run(){
                YoYo.with(Techniques.SlideOutRight)
                        .duration(1000).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        infoConnexion.setText(explainStringArray[new Random().nextInt(explainStringArray.length)]);
                        YoYo.with(Techniques.SlideInLeft)
                                .duration(1000)
                                .playOn(infoConnexion);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                })
                        .playOn(infoConnexion);
                mHandlerMessage.postDelayed(this, mShortDelay);
            }
        }, mShortDelay);


        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("username",id.getText().toString());
                params.put("password",mdp.getText().toString());

                //Animation animSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);

                //infoConnexion.startAnimation(animSlideLeft);
                YoYo.with(Techniques.SlideOutDown)
                        .duration(200)
                        .playOn(infoConnexion);

                YoYo.with(Techniques.FadeOut)
                        .duration(200)
                        .playOn(btnValider);

                avi.show();

                Connexion connexion = new Connexion(getApplicationContext(), params, "http://www.raphaelbischof.fr/messaging/?function=connect");

                connexion.setOnDownloadCompleteListener(new  OnDownloadCompleteListener() {
                    @Override
                    public void onDownloadCompleted(String result) {
                        //déserialisation
                        Gson gson = new Gson();
                        Reponse obj = gson.fromJson(result, Reponse.class);

                        if(obj.getResponse().equals("Ok")){
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.llBackground), "Connecté", Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                            //Toast.makeText(getApplicationContext(), "Connecté", Toast.LENGTH_SHORT).show();

                            //Shared preferences
                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("accesstoken", obj.getAccesstoken());
                            editor.putString("username", id.getText().toString());
                            // Commit the edits!
                            editor.commit();

                            Intent myIntent = new Intent(getApplicationContext(),ChannelListActivity.class);
                            startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, imageView, "logo").toBundle());

                            /*
                            YoYo.with(Techniques.FadeIn)
                                    .duration(200)
                                    .playOn(btnValider);

                            avi.hide();*/

                        }
                        else{
                            infoConnexion.clearAnimation();

                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.llBackground), "Erreur de connexion", Snackbar.LENGTH_SHORT);
                            //TODO : ajouter l'action de la snackbar
                            //mySnackbar.setAction(R.string.undo_string, new MyUndoListener());
                            mySnackbar.show();
                        }
                        avi.hide();
                        YoYo.with(Techniques.FadeIn)
                                .duration(200)
                                .playOn(findViewById(R.id.btnValider));
                    }
                });
                connexion.execute();
            }
        };

        btnValider.setOnClickListener(myListener);
    }
}