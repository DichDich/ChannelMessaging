package colas.dichan.ChannelMessaging;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

public class FriendsActivity extends AppCompatActivity {
    private GridView friends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);


        friends = (GridView) findViewById(R.id.gridViewFriends);

        UserDatasource data = new UserDatasource(getApplicationContext());
        data.open();
        List<Friend> myFriends = data.getAllFriends();
        data.close();

        friends.setAdapter((new ItemFriendAdapter(getApplicationContext(), myFriends)));


        friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Friend friend = (Friend) friends.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), PrivateChannelActivity.class);
                intent.putExtra("friendid", Integer.toString(friend.getUserID()));
                startActivity(intent);
            }
        });
    }
}
