package gtappathon.cometguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class ProductList extends AppCompatActivity {

    public RecyclerView mRecyclerView;
    public AndroidDataAdapter mAdapter;
    public static Map<String, int[]> keyViewHashMap = new HashMap<>();
    public static Map<String, String> keyCategoryHashMap = new HashMap<>();
    TextView categoryTextView;
    TextView characterTextView;

    int[] images;
    List<Product> nearestBeaconProductsList;

    private static final int recyclerViewImagesElectrical[] = {
            R.drawable.eone, R.drawable.etwo, R.drawable.ethree, R.drawable.efour, R.drawable.efive,
            R.drawable.esix, R.drawable.eseven
    };

    private static final int recyclerViewImagesAppliances[] = {
            R.drawable.aone, R.drawable.atwo, R.drawable.athree, R.drawable.afour, R.drawable.afive
    };

    private static final int recyclerViewImagesPlumbing[] = {
            R.drawable.pone, R.drawable.ptwo, R.drawable.pthree, R.drawable.pfour, R.drawable.pfive,
            R.drawable.psix, R.drawable.pseven, R.drawable.peight
    };

    private BeaconManager beaconManager;
    private Region region;

    private String prevMajorMinorKey;

    private static Map<String, List<Product>> BEACON_PRODUCT_MAP = null;

    static {
        // Mint Cocktail Beacon = Aisle 1 = Electrical

        Map<String, List<Product>> beaconProductMap = new HashMap<>();

        ArrayList<Product> electricalArrayList = new ArrayList<>();
        electricalArrayList.add(new Product("1-Gang 18 cu. in. Round Old Work Ceiling Box", "B618RR", 1.94, 15));
        electricalArrayList.add(new Product("AFC Cable Systems 1/2 in. x 100 ft. Non-Metallic Liquidtight Conduit", "6002-30-00", 39.17, 22));
        electricalArrayList.add(new Product("Romex 1000 ft. 12/2 Solid SIMpull NM-B Wire", "28828201", 193.00, 7));
        electricalArrayList.add(new Product("Leviton 15 Amp Tamper-Resistant Combination Switch and Outlet, Ivory", "R62-T5225-0WS", 7.99, 43));
        electricalArrayList.add(new Product("EcoSmart 60W Equivalent Day Light A19 Non Dimmable LED Light Bulb (8-Pack)", "A800ST-Q1-09", 19.97, 61));
        electricalArrayList.add(new Product("AFINIA H-Series H800 3D-Printer", "H800", 1899.00, 13));
        electricalArrayList.add(new Product("Samsung 43 in. Class LED 1080p 60Hz Internet Enabled HDTV with Wi-Fi Direct", "UN43J5200AF", 499.99, 59));

        beaconProductMap.put("19272:25761", electricalArrayList);
        keyViewHashMap.put("19272:25761", recyclerViewImagesElectrical);
        keyCategoryHashMap.put("19272:25761", "Electrical");

        // Icy Marshmallow Beacon = Aisle 2 = Appliances

        ArrayList<Product> appliancesArrayList = new ArrayList<>();
        appliancesArrayList.add(new Product("LG Electronics 4.5 cu. ft. High Efficiency Front Load Washer with Steam in Graphite Steel, Energy Star", "WF45K6200AW", 599.00, 8));
        appliancesArrayList.add(new Product("Maxx Cold X-Series 49 cu. ft. Double Door Commercial Reach In Upright Freezer in Stainless Steel", "MXCF-49FD", 3151.53, 18));
        appliancesArrayList.add(new Product("BLACK+DECKER 6-Slice Digital Convection Toaster Oven in Stainless Steel", "CTO6335S", 64.99, 21));
        appliancesArrayList.add(new Product("Hoover WindTunnel 2 Pet Rewind Bagless Upright Vacuum Cleaner", "UH70832", 118.00, 32));
        appliancesArrayList.add(new Product("Carrier Installed Comfort Series Heat Pump", "HSINSTCARCHP", 2499.00, 25));

        beaconProductMap.put("19272:58530", appliancesArrayList);
        keyViewHashMap.put("19272:58530", recyclerViewImagesAppliances);
        keyCategoryHashMap.put("19272:58530", "Appliances");

        // Blueberry Blue Beacon = Aisle 3 = Plumbing

        ArrayList<Product> plumbingArrayList = new ArrayList<>();
        plumbingArrayList.add(new Product("Westinghouse 52 Gal. 5500-Watt Lifetime Residential Electric Water Heater with durable 316L Stainless Steel Tank", "WER052C2X055", 599.99, 16));
        plumbingArrayList.add(new Product("NIBCO 3/4 in. Lead-Free Copper and CPVC CTS Silicon Alloy Slip x FIPT Transition Union", "C4733-3-LF", 8.61, 64));
        plumbingArrayList.add(new Product("3/4 in. Bronze Pressure Vacuum Breaker", "800M4-QT", 67.65, 34));
        plumbingArrayList.add(new Product("Instant Power 67.6 oz. Hair and Grease Drain Opener", "1970", 10.98, 57));
        plumbingArrayList.add(new Product("RIDGID K750 115-Volt Drum Machine with 5/8 in. Pigtail", "51402", 1548.14, 16));
        plumbingArrayList.add(new Product("RID-X 19.6 oz. Professional Powder 2-Dose Septic Tank Treatment", "19200-83623", 12.47, 40));
        plumbingArrayList.add(new Product("Rain Bird 25 - 41 ft. P5R Professional Grade Riser-Mounted Polymer Impact Sprinkler", "P5R", 6.48, 22));
        plumbingArrayList.add(new Product("Peerless Choice 2-Handle Wall Mount Kitchen Faucet in Chrome", "P299305LF", 60.44, 31));


        beaconProductMap.put("19272:1600", plumbingArrayList);
        keyViewHashMap.put("19272:1600", recyclerViewImagesPlumbing);
        keyCategoryHashMap.put("19272:1600", "Plumbing");

        BEACON_PRODUCT_MAP = Collections.unmodifiableMap(beaconProductMap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        prevMajorMinorKey = "";

        categoryTextView = (TextView) findViewById(R.id.tv_title);
        characterTextView = (TextView) findViewById(R.id.tv_title_s);

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager = new BeaconManager(this);
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    nearestBeaconProductsList = productsNearBeacon(nearestBeacon);

                    String majorMinorKeyString = String.format("%d:%d", nearestBeacon.getMajor(), nearestBeacon.getMinor());
                    images = keyViewHashMap.get(majorMinorKeyString);

                    categoryTextView.setText(keyCategoryHashMap.get(majorMinorKeyString));
                    characterTextView.setText(keyCategoryHashMap.get(majorMinorKeyString).substring(0,1));

                    mAdapter.updateData(prepareData(nearestBeaconProductsList, images));

//                    if (!prevMajorMinorKey.equals(majorMinorKeyString)) {
//                        rvDisappear();
//                        mAdapter.updateData(prepareData(nearestBeaconProductsList, images));
//                        rvAppear();
//                    } else {
//                        mAdapter.updateData(prepareData(nearestBeaconProductsList, images));
//                    }
                    prevMajorMinorKey = majorMinorKeyString;
                }
            }
        });

        initRecyclerViews();

    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }

    /*
    Returns an ArrayList with the aisle's products to the nearest detected beacon
    or an empty ArrayList if beacon detected doesn't exist in code.
    */
    private List<Product> productsNearBeacon(Beacon beacon) {
        String beaconKey = String.format("%d:%d", beacon.getMajor(), beacon.getMinor());
        if (BEACON_PRODUCT_MAP.containsKey(beaconKey)) {
            return BEACON_PRODUCT_MAP.get(beaconKey);
        }
        return Collections.emptyList();
    }

    private void initRecyclerViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        rvDisappear();

        int[] emptyImagesArray = {};
        ArrayList<AndroidVersion> av = prepareData(new ArrayList<Product>(), emptyImagesArray);
        mAdapter = new AndroidDataAdapter(getApplicationContext(), av);
        mRecyclerView.setAdapter(mAdapter);

//        rvAppear();

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
//                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_SHORT).show();

                        transition(i);
                    }
                })
        );

    }

    private ArrayList<AndroidVersion> prepareData(List<Product> nearestBeaconProductsList, int[] images) {
        ArrayList<AndroidVersion> av = new ArrayList<>();

        for (int i = 0; i < nearestBeaconProductsList.size(); i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(nearestBeaconProductsList.get(i).getName());
            mAndroidVersion.setrecyclerViewImage(images[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }

    private void transition(int i) {
        Intent intent = new Intent(this, ProductInfo.class);
        intent.putExtra("name", nearestBeaconProductsList.get(i).getName());
        intent.putExtra("model_number", nearestBeaconProductsList.get(i).getModelNumber());
        intent.putExtra("price", nearestBeaconProductsList.get(i).getPrice());
        intent.putExtra("stock_quantity", nearestBeaconProductsList.get(i).getStockQuantity());
        intent.putExtra("image", images[i]);

        startActivity(intent);
    }

    /**
     * Animation for RecyclerView to fade in
     */
    private void rvAppear() {
        final AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        mRecyclerView.startAnimation(anim);
    }

    /**
     * Animation for RecyclerView to fade out
     */
    private void rvDisappear() {
        final AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        mRecyclerView.startAnimation(anim);
    }
}
