package gtappathon.cometguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    private static final int recyclerViewImagesElectrical[] = {
            R.drawable.eone, R.drawable.etwo, R.drawable.ethree, R.drawable.efour, R.drawable.efive,
            R.drawable.esix, R.drawable.eseven
    };

    private static final int recyclerViewImagesAppliances[] = {
            R.drawable.aone, R.drawable.atwo, R.drawable.athree, R.drawable.afour, R.drawable.afive
    };

    private BeaconManager beaconManager;
    private Region region;

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

        // Icy Marshmallow Beacon = Aisle 2 = Appliances

        ArrayList<Product> appliancesArrayList = new ArrayList<>();
        appliancesArrayList.add(new Product("LG Electronics 4.5 cu. ft. High Efficiency Front Load Washer with Steam in Graphite Steel, Energy Star", "WF45K6200AW", 599.00, 8));
        appliancesArrayList.add(new Product("Maxx Cold X-Series 49 cu. ft. Double Door Commercial Reach In Upright Freezer in Stainless Steel", "MXCF-49FD", 3151.53, 18));
        appliancesArrayList.add(new Product("BLACK+DECKER 6-Slice Digital Convection Toaster Oven in Stainless Steel", "CTO6335S", 64.99, 21));
        appliancesArrayList.add(new Product("Hoover WindTunnel 2 Pet Rewind Bagless Upright Vacuum Cleaner", "UH70832", 118.00, 32));
        appliancesArrayList.add(new Product("Carrier Installed Comfort Series Heat Pump", "HSINSTCARCHP", 2499.00, 25));

        beaconProductMap.put("19272:58530", new ArrayList<Product>());
        keyViewHashMap.put("19272:58530", recyclerViewImagesAppliances);

        // Blueberry Blue Beacon = Aisle 3

        beaconProductMap.put("19272:1600", new ArrayList<Product>());

        BEACON_PRODUCT_MAP = Collections.unmodifiableMap(beaconProductMap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager = new BeaconManager(this);
        region = new Region("ranged region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);

                    List<Product> nearestBeaconProductsList = productsNearBeacon(nearestBeacon);
                    int[] images = keyViewHashMap.get(String.format("%d:%d", nearestBeacon.getMajor(), nearestBeacon.getMinor()));

                    String s = "";

                    for (int i = 0; i < nearestBeaconProductsList.size(); i++) {
                        s += nearestBeaconProductsList.get(i).getName() + ", ";
                    }
                    
//                    TODO update
                    mAdapter.updateData(prepareData(nearestBeaconProductsList, images));
                    
                    Log.d("Beacon: " + nearestBeacon.getMinor(), ". Products: " + s);


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

        int[] emptyImagesArray = {};
        ArrayList<AndroidVersion> av = prepareData(new ArrayList<Product>(), emptyImagesArray);
        mAdapter = new AndroidDataAdapter(getApplicationContext(), av);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        switch (i) {
                            case 0:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();

//                                ArrayList<AndroidVersion> av = new ArrayList<>();
//
//                                List<Product> p = BEACON_PRODUCT_MAP.get("19272:58530");
//
//                                for (int j = 0; j < p.size(); j++) {
//                                    AndroidVersion mAndroidVersion = new AndroidVersion();
//                                    mAndroidVersion.setAndroidVersionName(p.get(j).getName());
//                                    mAndroidVersion.setrecyclerViewImage(recyclerViewImagesElectrical[j]);
//                                    av.add(mAndroidVersion);
//                                }
//
//                                mAdapter.updateData(av);
                                break;
                            case 1:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 5:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 6:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 7:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 8:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 9:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 10:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                            case 11:
                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                break;
                        }
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
}
