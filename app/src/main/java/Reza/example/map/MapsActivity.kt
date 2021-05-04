package Reza.example.map

import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.getmapsapi.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation : LatLng
    private lateinit var currentAddress : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var locationList = mutableMapOf <String,Double>("latLoc1" to 0.0 , "longLoc1" to 0.0 , "latLoc2" to 0.0 , "longLoc2" to 0.0 )
        var addressList = mutableMapOf<String,String>("address1" to "" , "address2" to "")

        val ceves = LatLng(-6.234618347492962, 106.99041743952743)
        mMap.addMarker(MarkerOptions().position(ceves).title("Marker in cevest"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ceves))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ceves, 14F))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mMap.setOnCameraIdleListener {
            currentLocation = mMap.cameraPosition.target
            val geoCoder = Geocoder(this)
            mMap.clear()
            var geoResult = geoCoder.getFromLocation(currentLocation.latitude , currentLocation.longitude,1)
            currentAddress = geoResult[0].getAddressLine(0)
            mMap.addMarker(MarkerOptions().position(currentLocation).title("Posisi Sekarang"))
        }
        BtnSet.setOnClickListener {
            if(locationList.get("latLoc1") == 0.0 ){
                locationList.put("latLoc1",currentLocation.latitude)
                locationList.put("longLoc1",currentLocation.longitude)
                addressList.put("address1",currentAddress)
            }else{
                locationList.put("latLoc2" , currentLocation.latitude)
                locationList.put("longLoc2" , currentLocation.longitude)
                addressList.put("address2",currentAddress)
            }
//            lokasi1.text = "${locationList.get("latLoc1")} - ${locationList.get("longLoc1")}"
//            lokasi2.text = "${locationList.get("latLoc2")} - ${locationList.get("longLoc2")}"
            lokasi1.text = "${addressList.get("address1")}"
            lokasi2.text = "${addressList.get("address2")}"
        }

        Btnhitung.setOnClickListener {
            val loc1 = LatLng(locationList.get("latLoc1")!!, locationList.get("longLoc1")!!)
            val location1 = Location("")
            location1.latitude = locationList.get("latLoc1")!!
            location1.longitude = locationList.get("longLoc1")!!

            val loc2 = LatLng(locationList.get("latLoc2")!!, locationList.get("longLoc2")!!)
            val location2 = Location("")
            location2.latitude = locationList.get("latLoc2")!!
            location2.longitude = locationList.get("longLoc2")!!

            mMap.addMarker(MarkerOptions().position(loc1).title("position 1"))
            mMap.addMarker(MarkerOptions().position(loc2).title("Position 2"))

            mMap.addPolyline(PolylineOptions()
                    .clickable(true)
                    .add(loc1,loc2)
                    .color(R.color.purple_200)
                    .width(16f)
            )
            var distance1 = location1.distanceTo(location2)
            total.text = "${distance1/1000} KM"
        }
        btnReset.setOnClickListener {
            locationList.putAll(setOf("latLoc1" to 0.0, "longLoc1" to 0.0, "latLoc2" to 0.0, "longLoc2" to 0.0))
            lokasi1.text = ""
            lokasi2.text = ""
            total.text = ""
            mMap.clear()

        }
    }
}