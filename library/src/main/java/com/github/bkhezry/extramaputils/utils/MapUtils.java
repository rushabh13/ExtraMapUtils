package com.github.bkhezry.extramaputils.utils;


import com.github.bkhezry.extramaputils.model.ExtraMarker;
import com.github.bkhezry.extramaputils.model.ExtraPolygon;
import com.github.bkhezry.extramaputils.model.ExtraPolyline;
import com.github.bkhezry.extramaputils.model.OptionView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapUtils {
    public static void showElements(final OptionView optionView, final GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (ExtraMarker extraMarker : optionView.getMarkers()) {
                    builder.include(extraMarker.getCenter());
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(extraMarker.getIcon());
                    googleMap.addMarker(
                            new MarkerOptions()
                                    .icon(icon)
                                    .position(extraMarker.getCenter())
                                    .title(extraMarker.getName())
                    );
                }
                for (ExtraPolygon polygon : optionView.getPolygons()) {
                    googleMap.addPolygon(
                            new PolygonOptions()
                                    .fillColor(polygon.getFillColor())
                                    .strokeColor(polygon.getUiOptions().getColor())
                                    .strokeWidth(polygon.getUiOptions().getWidth())
                                    .zIndex(polygon.getUiOptions().getzIndex())
                                    .add(polygon.getPoints())
                    );
                    for (LatLng latLng : polygon.getPoints()) {
                        builder.include(latLng);
                    }
                }
                for (ExtraPolyline polyline : optionView.getPolylines()) {
                    googleMap.addPolyline(
                            new PolylineOptions()
                                    .color(polyline.getUiOptions().getColor())
                                    .width(polyline.getUiOptions().getWidth())
                                    .zIndex(polyline.getUiOptions().getzIndex())
                                    .add(polyline.getPoints())
                    );
                    for (LatLng latLng : polyline.getPoints()) {
                        builder.include(latLng);
                    }
                }

                if (optionView.isForceCenterMap()) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(optionView.getCenterLatLng(), optionView.getMapsZoom()));
                } else {
                    boundMap(optionView.isListView(), builder, googleMap);
                }
            }
        });

    }

    private static void boundMap(final boolean isListView, final LatLngBounds.Builder builder, final GoogleMap googleMap) {
        LatLngBounds bounds = builder.build();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        float zoom = googleMap.getCameraPosition().zoom;
        if (isListView)
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(zoom - 1f));
    }
}
