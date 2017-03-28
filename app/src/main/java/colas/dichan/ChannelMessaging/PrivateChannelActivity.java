package colas.dichan.ChannelMessaging;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class PrivateChannelActivity extends AppCompatActivity {
    private ListView messages;
    private EditText myMessage;
    private List<PrivateMessage> oldMessages;
    private FloatingActionButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_channel);

        messages = (ListView) findViewById(R.id.listViewMsg);
        myMessage = (EditText) findViewById(R.id.editTextMessage);
        btnSend = (FloatingActionButton) findViewById(R.id.btnSend);

        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                String accesstoken = settings.getString("accesstoken","");

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("accesstoken",accesstoken);
                params.put("userid", getIntent().getStringExtra("friendid"));
                Connexion connexion = new Connexion(getApplicationContext(), params, "http://www.raphaelbischof.fr/messaging/?function=getmessages");

                connexion.setOnDownloadCompleteListener(new  OnDownloadCompleteListener() {
                    @Override
                    public void onDownloadCompleted(String result) {
                        //déserialisation
                        Gson gson = new Gson();
                        PrivateMessageContainer obj = gson.fromJson(result, PrivateMessageContainer.class);
                        Collections.reverse(obj.getMessages());

                        //Teste s'il y a un nouveau message avant de refresh
                        if(!obj.getMessages().equals(oldMessages)){
                            oldMessages = obj.getMessages();
                            messages.setAdapter((new PrivateMessageListAdapter(getApplicationContext(), obj.getMessages())));
                        }
                    }
                });
                connexion.execute();

                handler.postDelayed(this, 1000);
            }
        };

        handler.postDelayed(r, 1000);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
                String accesstoken = settings.getString("accesstoken","");

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("accesstoken",accesstoken);
                params.put("userid", getIntent().getStringExtra("friendid"));
                params.put("message",myMessage.getText().toString());

                myMessage.setText("");
                Connexion connexion = new Connexion(getApplicationContext(), params, "http://www.raphaelbischof.fr/messaging/?function=sendmessage");

                connexion.setOnDownloadCompleteListener(new  OnDownloadCompleteListener() {
                    @Override
                    public void onDownloadCompleted(String result) {
                        //déserialisation
                        Gson gson = new Gson();
                        Reponse obj = gson.fromJson(result, Reponse.class);

                        if(!obj.getCode().equals("200")){
                            Toast.makeText(getApplicationContext(), "Erreur lors de l'envoi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                connexion.execute();
            }
        });
    }
}
