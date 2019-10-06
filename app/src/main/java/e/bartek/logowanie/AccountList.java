package e.bartek.logowanie;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bartek on 01.09.2018.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import e.bartek.logowanie.R;


public class AccountList extends ArrayAdapter<Account> {

    private Activity context;
    private List<Account> accountList;
    private boolean flag;



    public AccountList(Activity context, List<Account> accountList){
        super(context, R.layout.listcompute_layout, accountList);
        this.context = context;
        this.accountList = accountList;
        flag = false;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater1 = context.getLayoutInflater();

        View listViewItem1 = inflater1.inflate(R.layout.listcompute_layout, null, true);

        TextView textViewNameUser = (TextView) listViewItem1.findViewById(R.id.textViewNameUser);
        TextView textViewOverallCost = (TextView) listViewItem1.findViewById(R.id.textViewOverallCost);


        Account account =accountList.get(position);

        textViewNameUser.setText(account.getOwner());
        textViewOverallCost.setText(account.getRental().toString());


        if(flag == true){
            textViewOverallCost.setBackgroundColor(Color.RED);
        }


        return listViewItem1;
    }

}
