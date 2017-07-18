package ru.worksolutions.mapsapp

import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import android.os.AsyncTask.execute
import android.view.View
import android.widget.*
import com.directions.route.*
import java.util.ArrayList
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, RoutingListener,
        LocationListener {

    private var from: String? = null
    private var to: String? = null

    private var googleMap: GoogleMap? = null

    private var spinnerFrom: Spinner? = null
    private var spinnerTo: Spinner? = null
    private var tvRouteInformation: TextView? = null

    private var mLocationRequest: LocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
            .setFastestInterval(1 * 1000); // 1 second, in milliseconds


    private lateinit var mTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val options1 = arrayOf("Радиорынок", "ОДНТ", "Аэропорт", "Новочеркасск", "Склад")
        val options2 = arrayOf("Радиорынок", "ОДНТ", "Аэропорт", "Новочеркасск", "Склад")
        val adapter_from = ArrayAdapter(applicationContext,
                R.layout.spinner_item,
                options1)
        adapter_from.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val adapter_to = ArrayAdapter(applicationContext,
                R.layout.spinner_item,
                options2)
        adapter_to.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerFrom = findViewById(R.id.spinner_from) as Spinner
        spinnerFrom!!.setAdapter(adapter_from)
        spinnerFrom!!.setSelection(0)
        spinnerFrom!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                from = options1[i]
                updateMap()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {

            }
        }

        spinnerTo = findViewById(R.id.spinner_to) as Spinner
        spinnerTo!!.setAdapter(adapter_to)
        spinnerTo!!.setSelection(0)
        spinnerTo!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                to = options2[i]
                updateMap()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }


        tvRouteInformation = findViewById(R.id.tv_route_information) as TextView


    }

    private fun updateMap() {
        if (from == null || to == null)
            return

        requestWay(getCoordinates(from!!), getCoordinates(to!!))

    }

    private fun getCoordinates(point: String): LatLng {
        return when (point) {
            "Радиорынок" -> LatLng(47.219241, 39.677243)
            "ОДНТ" -> LatLng(47.230880, 39.764353)
            "Аэропорт" -> LatLng(47.254820, 39.802986)
            "Новочеркасск" -> LatLng(47.413948, 40.092566)
            "Склад" -> LatLng(47.215513, 39.691762)
            else -> LatLng(47.240128, 39.687957)
        }
    }

    override fun onStart() {
        super.onStart()
        initMap()
    }

    override fun onLocationChanged(location: Location) {
        handleNewLocation(location)
    }

    override fun onConnected(p0: Bundle?) {
        val location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        } else {
            handleNewLocation(location)
        };

    }


    private fun handleNewLocation(location: Location, zoom: Float = 17f) {
        Log.e("MainActivity", location.toString())
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 13f))

        val cameraPosition = CameraPosition.Builder()
                .target(LatLng(location.latitude, location.longitude))      // Sets the center of the map to location user
                .zoom(zoom)                   // Sets the zoom
                .build()                   // Creates a CameraPosition from the builder
        googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun initMap() {
        if (null == googleMap)
            (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
    }

    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .build()

        mGoogleApiClient?.connect()
        googleMap?.isMyLocationEnabled = true
    }

    private fun requestWay(start: LatLng, end: LatLng) {
//        val  = LatLng(47.240101, 39.688036)
//        val end = LatLng(47.260023, 39.648247)

        val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build()
        routing.execute()
    }

    private var polyline: Polyline? = null
    private val markers: MutableList<Marker> = mutableListOf()

    override fun onRoutingSuccess(routes: ArrayList<Route>, p1: Int) {
        polyline?.remove()
        markers.forEach { m -> m.remove() }

        val route = routes.first()

        markers.add(googleMap!!.addMarker(MarkerOptions()
                .position(route.points.first())
                .title("From")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)))
        )

        markers.add(googleMap!!.addMarker(MarkerOptions()
                .position(route.points.last())
                .title("To")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        )

        val latLngs = ArrayList<LatLng>()
        for (point in route.points)
            latLngs.add(LatLng(point.latitude, point.longitude))
        val crntPolyline = PolylineOptions().addAll(latLngs)
        polyline = googleMap?.addPolyline(crntPolyline)

        val formattedLength = "Length (per kilometers): " + (route.distanceValue / 1000).toString()
        val formattedDuration = "Duration (per minutes): " + (route.durationValue / 60).toString()
        tvRouteInformation!!.text = formattedLength + "\n" + formattedDuration


        val cu = CameraUpdateFactory.newLatLngBounds(routes[0].latLgnBounds, 5)
        googleMap?.animateCamera(cu)


//        handleNewLocation(loc, 13f)

    }


    override fun onRoutingCancelled() {

    }

    override fun onRoutingStart() {

    }

    override fun onRoutingFailure(p0: RouteException?) {

    }
}