package com.freelance.brooklyn.coffeeorder;

//import android.icu.text.NumberFormat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.name;
import static com.freelance.brooklyn.coffeeorder.R.dimen.txtTotal;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(findViewById(R.id)){
//            mTwoPane = true;
//
//        }else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "You can't have more than 100 coffees.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity += 1;
        displayQuantity(quantity);

    }

    public void decrement(View view) {
        if (quantity == 1 || quantity == 0) {
            Toast.makeText(this, "You can't have less than 1 coffee.", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity -= 1;
        displayQuantity(quantity);
    }

    public void submitOrder(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.extName);
        String name = nameEditText.getText().toString();
        //Log.v("MainActivity", "Name: " + name);

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.chkWhippedCream);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chkChocolate);
        boolean hasChocolate = chocolateCheckBox.isChecked();
        //Log.v("MainActivity", "Has Whipped Cream: " + hasWhippedCream);

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));//only email apps should handle this
        i.putExtra(Intent.EXTRA_SUBJECT, "STARBUCKS Coffee Order " + name);
        i.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
        /*displayMessage(priceMessage);*/
    }

    //show data for Quantity
    private void displayQuantity(int numberOfCoffees) {
        TextView txtCount = (TextView) findViewById(R.id.txtCount);
        txtCount.setText("" + numberOfCoffees);
    }

    //show data for Total Order Summary
//    private void displayMessage(String message) {
//        TextView totalOrderSummary = (TextView) findViewById(R.id.txtTotalOrderSummary);
//        totalOrderSummary.setText(message);
//    }

    /**
     * Calculates the price of the order.
     *
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        //Price of 1 cup of coffee
        int basePrice = 5;

        //Add $1 if the user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }

        //Add $2 if the user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }
        return quantity * basePrice;
    }

    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = "Name: " + name;
        priceMessage += "\nAdd Whipped Cream? " + addWhippedCream;
        priceMessage += "\nAdd Chocolate? " + addChocolate;
        priceMessage += "\nQuantity: " + quantity;
        priceMessage += "\nTotal: $ " + price;
        priceMessage += "\nThank you!";
        return priceMessage;
    }

}
