package colas.dichan.ChannelMessaging;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrivateMessageListAdapter extends ArrayAdapter<PrivateMessage> {
    private final Context context;
    private final List<PrivateMessage> values;

    public PrivateMessageListAdapter(Context context, List<PrivateMessage> messages ){
            super(context, 0, messages);
            this.context = context;
            this.values = messages;
            }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            PrivateMessage message = getItem(position);

            if (convertView == null) {
                if(message.getSendbyme() == 0)
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_layout, parent, false);
                else
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_message_layout_right, parent, false);
            }

            TextView text = (TextView) convertView.findViewById(R.id.textViewMessage);
            TextView user = (TextView) convertView.findViewById(R.id.textViewUser);
            TextView date = (TextView) convertView.findViewById(R.id.textViewDate);
            CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.imageViewAvatar);

            text.setText(message.getMessage());
            if(message.getEverRead().equals("0"))
                text.setTypeface(null, Typeface.BOLD);
            user.setText(message.getUsername().toString() + " : ");
            date.setText(message.getDate());

            if(ChannelActivity.images.containsKey(message.getImageUrl())){
                avatar.setImageBitmap(ChannelActivity.images.get(message.getImageUrl()));
            }else{
                new DownloadImageTask(avatar).execute(message.getImageUrl());
            }

            return convertView;
    }
}
