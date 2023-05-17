package com.example.manager.shoppy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airbnb.lottie.utils.Utils;
import com.example.manager.shoppy.R;
import com.example.manager.shoppy.retrofit.ApiBanHang;
import com.example.manager.shoppy.retrofit.RetrofitClient;
import com.example.manager.shoppy.ultils.Ultils;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThongKeActivity extends AppCompatActivity {
    Toolbar toolbarThongKe;
    PieChart pieChart;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

//    private static final int MAX_X_VALUE = 7;
//    private static final int MAX_Y_VALUE = 50;
//    private static final int MIN_Y_VALUE = 5;
//    private static final String SET_LABEL = "Thong ke san pham";
//    private static final String[] DAYS = { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
//    private HorizontalBarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        apiBanHang = RetrofitClient.getInstance(Ultils.BASE_URL).create(ApiBanHang.class);
        initView();
        actionToolbar();
        getDataChart();
//        barChart = findViewById(R.id.pieChart);

//        BarData data = createChartData();
//        configureChartAppearance();
//        prepareChartData(data);
//        drawChart();
    }

//    private void drawChart() {
//        BarChart barChart = findViewById(R.id.barChart);
//        barChart.setDrawBarShadow(false);
//        barChart.setDrawValueAboveBar(true);
//        Description description = new Description();
//        description.setText("");
//        barChart.setDescription(description);
//        barChart.setMaxVisibleValueCount(50);
//        barChart.setPinchZoom(false);
//        barChart.setDrawGridBackground(false);
//
//        XAxis xl = barChart.getXAxis();
//        xl.setGranularity(1f);
//        xl.setCenterAxisLabels(true);
//
//        YAxis leftAxis = barChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setSpaceTop(30f);
//        barChart.getAxisRight().setEnabled(false);
//
//        //data
//        float groupSpace = 0.04f;
//        float barSpace = 0.02f;
//        float barWidth = 0.46f;
//
//        int startYear = 1980;
//        int endYear = 1985;
//
//        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
//        List<BarEntry> yVals2 = new ArrayList<BarEntry>();
//
//        for (int i = startYear; i < endYear; i++) {
//            yVals1.add(new BarEntry(i, 0.4f));
//        }
//
//        for (int i = startYear; i < endYear; i++) {
//            yVals2.add(new BarEntry(i, 0.7f));
//        }
//
//        BarDataSet set1, set2;
//
//        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
//            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
//            set1.setValues(yVals1);
//            set2.setValues(yVals2);
//            barChart.getData().notifyDataChanged();
//            barChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, "Company A");
//            set1.setColor(Color.rgb(104, 241, 175));
//            set2 = new BarDataSet(yVals2, "Company B");
//            set2.setColor(Color.rgb(164, 228, 251));
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//            dataSets.add(set2);
//
//            BarData data = new BarData(dataSets);
//            barChart.setData(data);
//        }
//
//        barChart.getBarData().setBarWidth(barWidth);
//        barChart.groupBars(startYear, groupSpace, barSpace);
//        barChart.invalidate();
//
//    }

    private void getDataChart() {
        List<PieEntry> listData = new ArrayList<>();
        compositeDisposable.add(apiBanHang.getThongKe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(thongKeModel -> {
                    if(thongKeModel.isSuccess()){
                        for(int i= 0; i<thongKeModel.getResult().size(); i++){
                            String tensanpham = thongKeModel.getResult().get(i).getTensanpham();
                            int tong = thongKeModel.getResult().get(i).getTong();
                            listData.add(new PieEntry(tong,tensanpham));
                        }
                        PieDataSet pieDataSet = new PieDataSet(listData,"Thống kê");
                        PieData data = new PieData();
                        data.setDataSet(pieDataSet);
                        data.setValueTextSize(10f);
                        data.setValueFormatter(new PercentFormatter(pieChart));
                        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                        pieChart.setData(data);
                        pieChart.animateXY(2000,2000);
                        pieChart.setUsePercentValues(true);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.invalidate();
                    }
                },
                        throwable -> {
                            Log.d("logg", throwable.getMessage());
                        }));
    }
//private void getDataChart() {
//    List<BarEntry> listData = new ArrayList<>();
//    compositeDisposable.add(apiBanHang.getThongKe()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(thongKeModel -> {
//                        if(thongKeModel.isSuccess()){
//                            for(int i= 0; i<thongKeModel.getResult().size(); i++){
//                                String tensanpham = thongKeModel.getResult().get(i).getTensanpham();
//                                int tong = thongKeModel.getResult().get(i).getTong();
//                                listData.add(new BarEntry(tong,tensanpham));
//                            }
//                            PieDataSet pieDataSet = new PieDataSet(listData,"Thống kê");
//                            PieData data = new PieData();
//                            data.setDataSet(pieDataSet);
//                            data.setValueTextSize(12f);
////                            data.setValueFormatter(new PercentFormatter(pieChart));
//                            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//
//                            pieChart.setData(data);
//                            pieChart.animateXY(2000,2000);
//                            pieChart.setUsePercentValues(true);
//                            pieChart.getDescription().setEnabled(false);
//                            pieChart.invalidate();
//                        }
//                    },
//                    throwable -> {
//                        Log.d("logg", throwable.getMessage());
//                    }));
//}

    private void initView() {
        toolbarThongKe = findViewById(R.id.toolbarThongKe);
        pieChart = findViewById(R.id.pieChart);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarThongKe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThongKe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}