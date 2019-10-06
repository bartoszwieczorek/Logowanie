package e.bartek.logowanie;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Bartek on 01.09.2018.
 */

public class Account extends AppCompatActivity {
    public String owner;
    public Float rental; //należność pieniężna
   // public Boolean flag;

    public Account(){}

    public Account(String owner, Float rental){
        this.owner = owner;
        this.rental = rental;
        //this.flag = flag;
    }

    public String getOwner() {
        return owner;
    }

    public Float getRental() {
        return rental;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setRental(Float rental) {
        this.rental = rental;
    }

}
