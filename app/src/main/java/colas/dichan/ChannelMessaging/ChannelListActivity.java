package colas.dichan.ChannelMessaging;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;
import colas.dichan.ChannelMessaging.R;

import colas.dichan.ChannelMessaging.fragment.ChannelListFragment;
import colas.dichan.ChannelMessaging.fragment.MessageFragment;

public class ChannelListActivity extends AppCompatActivity{
    private ListView channels;
    private Button btnFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_list);

        channels = (ListView) findViewById(R.id.listViewChannels);
        btnFriends = (Button) findViewById(R.id.buttonFriends);

        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        String accesstoken = settings.getString("accesstoken","");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken",accesstoken);

        Connexion connexion = new Connexion(getApplicationContext(), params, "http://www.raphaelbischof.fr/messaging/?function=getchannels");

        connexion.setOnDownloadCompleteListener(new  OnDownloadCompleteListener() {
            @Override
            public void onDownloadCompleted(String result) {
                //déserialisation
                Gson gson = new Gson();
                ChannelsContainer obj = gson.fromJson(result, ChannelsContainer.class);

                channels.setAdapter((new ChannelArrayAdapter(getApplicationContext(), obj.getChannels())));
            }
        });


        channels.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cast de la ligne en objet Channel pour récupérer l'id
                Channel channel = (Channel) channels.getItemAtPosition(position);
                /*
                Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                intent.putExtra("channelid", Integer.toString(channel.getChannelID()));
                startActivity(intent);*/
                ChannelListFragment fragA = (ChannelListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentA_ID);
                MessageFragment fragB = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentB_ID);
                if(fragB == null || !fragB.isInLayout()){
                    Intent i = new Intent(getApplicationContext(), ChannelActivity.class);
                    Intent intent = new Intent(getApplicationContext(), ChannelActivity.class);
                    intent.putExtra("channelid", Integer.toString(channel.getChannelID()));
                    startActivity(intent);
                    //i.putExtra("channelid", Integer.toString(channel.getChannelID()));
                    //startActivity(i);
                }
                else
                {
                    fragB.channelId = Integer.toString(channel.getChannelID());
                    //fragB.fillTextView(fragA.listItems[position]);
                }
            }
        });

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
            }
        });

        connexion.execute();
    }
}
