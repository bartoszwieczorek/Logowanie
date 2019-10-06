package e.bartek.logowanie;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bartek on 25.08.2018.
 */

public class UserInformationList extends ArrayAdapter<UserInformation> {

    private Activity context;
    private List<UserInformation> userInformationList;

    public UserInformationList(Activity context, List<UserInformation> userInformationList){
        super(context, R.layout.list_layout, userInformationList);
        this.context = context;
        this.userInformationList = userInformationList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.textViewCost);

        UserInformation userInformation = userInformationList.get(position);

        textViewDesc.setText(userInformation.getName());
        textViewCost.setText(userInformation.getAddress());

        return listViewItem;
    }
}
