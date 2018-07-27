package com.charge.ssi.ssicharge.ui.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.charge.ssi.ssicharge.R;
import com.charge.ssi.ssicharge.utils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yu on 2016/11/11.
 */

public class HomeFragment extends Fragment implements LocationSource,
        AMapLocationListener, AMap.OnMapClickListener {
    @BindView(R.id.mapView)
    MapView mapView;
    Unbinder unbinder;
    private String title;

    private double latitude;
    private double longitude;

    private AMap mAMap;
    private Marker mMarker;// 当前车辆marker
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_home, null);
        // bind view
        unbinder = ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        StatusBarUtils.with(getActivity()).init();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initData(){
        if (null == mAMap) {
            mAMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 初始化地图参数
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.positioning));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//        getMap().setLatLonQuanVisible(false);
        mAMap.getUiSettings().setCompassEnabled(false);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图

        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    /*开启定位时*/
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                mlocationClient.stopLocation();

            } else {
                /*没开启定位时*/

//                LatLng latLng = new LatLng(Double.valueOf(notLat), Double.valueOf(notLog));
//                aMap.getUiSettings().setCompassEnabled(false);
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//                aMap.getUiSettings().setScaleControlsEnabled(true);// 设置比例尺
//                MarkerOptions otMarkerOptions = new MarkerOptions();
//                otMarkerOptions.position(latLng);
//                otMarkerOptions.draggable(true);
//                //下面这个是标记上面这个经纬度在地图的位置是
//                otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
//                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
//                aMap.addMarker(otMarkerOptions);
//                latitude = latLng.latitude;
//                longitude = latLng.longitude;
//
//                mlocationClient.stopLocation();
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //点击地图后清理图层插上图标，在将其移动到中心位置
        mAMap.clear();
        latitude = latLng.latitude;
        longitude = latLng.longitude;
        MarkerOptions otMarkerOptions = new MarkerOptions();
        otMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.positioning));
        otMarkerOptions.position(latLng);
        mAMap.addMarker(otMarkerOptions);
        mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
