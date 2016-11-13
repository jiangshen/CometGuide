package gtappathon.cometguide;

import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductInfo extends AppCompatActivity {

    private String name;
    private String modelNumber;
    private double price;
    private int stockQuantity;

    private int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        name = getIntent().getStringExtra("name");
        modelNumber = getIntent().getStringExtra("model_number");
        price = getIntent().getDoubleExtra("price", 0.0);
        stockQuantity = getIntent().getIntExtra("stock_quantity", 0);

        image = getIntent().getIntExtra("image", 0);


        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_model = (TextView) findViewById(R.id.tv_model);
        TextView tv_price_int = (TextView) findViewById(R.id.money_int);
        TextView tv_price_decimal = (TextView) findViewById(R.id.money_decimal);
        TextView tv_stock_qty = (TextView) findViewById(R.id.stock_qty);

        ImageView img_main = (ImageView) findViewById(R.id.img_main);

        img_main.setImageResource(image);

        int integer = (int)price;
        int decimal = (int)((price * 100) % 100);

        tv_name.setText("Name: " + name);
        tv_model.setText("Model: " + modelNumber);
        tv_price_int.setText(String.valueOf(integer));
        if (decimal == 0) {
            tv_price_decimal.setText(decimal + "0");
        } else {
            tv_price_decimal.setText(String.valueOf(decimal));
        }
        tv_stock_qty.setText(String.valueOf(stockQuantity));
    }
}
