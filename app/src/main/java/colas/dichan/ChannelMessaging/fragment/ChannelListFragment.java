package colas.dichan.ChannelMessaging.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.HashMap;

import colas.dichan.ChannelMessaging.Channel;
import colas.dichan.ChannelMessaging.ChannelActivity;
import colas.dichan.ChannelMessaging.ChannelArrayAdapter;
import colas.dichan.ChannelMessaging.ChannelsContainer;
import colas.dichan.ChannelMessaging.Connexion;
import colas.dichan.ChannelMessaging.FriendsActivity;
import colas.dichan.ChannelMessaging.LoginActivity;
import colas.dichan.ChannelMessaging.OnDownloadCompleteListener;
import colas.dichan.ChannelMessaging.R;


public class ChannelListFragment extends Fragment {
    private ListView channels;
    private Button btnFriends;

    public ChannelListFragment() {
        // empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_channel_list, container, false);
        //return inflater.inflate(R.layout.fragment_channel_list, container, false);


        channels = (ListView) v.findViewById(R.id.listViewChannels);
        btnFriends = (Button) v.findViewById(R.id.buttonFriends);

        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        String accesstoken = settings.getString("accesstoken","");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("accesstoken",accesstoken);

        Connexion connexion = new Connexion(getActivity().getApplicationContext(), params, "http://www.raphaelbischof.fr/messaging/?function=getchannels");

        connexion.setOnDownloadCompleteListener(new  OnDownloadCompleteListener() {
            @Override
            public void onDownloadCompleted(String result) {
                //déserialisation
                Gson gson = new Gson();
                ChannelsContainer obj = gson.fromJson(result, ChannelsContainer.class);

                if(getActivity() != null)
                    channels.setAdapter((new ChannelArrayAdapter(getActivity().getApplicationContext(), obj.getChannels())));
            }
        });


        channels.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Cast la ligne en objet Channel pour récupérer l'id
                Channel channel = (Channel) channels.getItemAtPosition(position);
                Intent intent = new Intent(getActivity().getApplicationContext(), ChannelActivity.class);
                intent.putExtra("channelid", Integer.toString(channel.getChannelID()));
                startActivity(intent);
            }
        });

        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
            }
        });

        connexion.execute();

        return v;
    }

}
