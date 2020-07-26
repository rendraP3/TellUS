package com.dotdevs.tellus.view.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.SeriesBase;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Fill;
import com.dotdevs.tellus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    @BindView(R.id.chartView)
    AnyChartView anyChartView;

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);

        setupChartView();

        return view;
    }

    // Fungsi untuk mengatur grafik
    private void setupChartView() {
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.crosshair().enabled(true);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Grafik Orang Hilang di Bandar Lampung");
        cartesian.yAxis(0).title("Jumlah Orang Hilang");

        List<DataEntry> data = new ArrayList<>();
        data.add(new CustomDataEntry("2017", 7, 2));
        data.add(new CustomDataEntry("2018", 5, 8));
        data.add(new CustomDataEntry("2019", 5, 26));

        Set set = Set.instantiate();
        set.data(data);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x' , value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Polda Lampung");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        series1.color("#6200EE");

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Medsos, Website Resmi BNPB & Tribun Lampung");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);
        series2.color("#03DAC5");

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);
        cartesian.credits(false);
    }

    //CUstom class untuk data entry pada grafik
    private class CustomDataEntry extends ValueDataEntry {

        public CustomDataEntry(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);
        }
    }
}

