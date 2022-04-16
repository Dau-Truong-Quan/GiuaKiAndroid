package com.reintrinh.quanlytruyenhinh_nhom10.thongke;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.reintrinh.quanlytruyenhinh_nhom10.R;
import com.reintrinh.quanlytruyenhinh_nhom10.adapter.ThongKeAdapter;
import com.reintrinh.quanlytruyenhinh_nhom10.helper.QuanLyTruyenHinhHelper;
import com.reintrinh.quanlytruyenhinh_nhom10.model.ThongKe;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tab2Fragment extends Fragment {

    QuanLyTruyenHinhHelper db;
    View mView;
    BarChart chartChuongTrinh;
    Button btnInReport2;

    ListView lvThongKeCT;
    ArrayList<ThongKe> dataList = new ArrayList<>();;
    ThongKeAdapter thongKeAdapter;

    PdfDocument document;

    public static final int CREATE_FILE = 100;

    public Tab2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tab2, container, false);

        setControl();
        getData();

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        BarEntry barEntry;
        for (int i = 0; i < dataList.size(); i++) {
            //Chỉ lấy 5 item đầu
            if(i >= 5)
                break;
            barEntry = new BarEntry(i, dataList.get(i).getValue());
            barEntries.add(barEntry);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < dataList.size(); i++) {
            //Chỉ lấy 5 item đầu
            if(i >= 5)
                break;
            labels.add(dataList.get(i).getLabel());
        }

        XAxis xAxis = chartChuongTrinh.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Chương trình");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        chartChuongTrinh.setData(new BarData(barDataSet));
        chartChuongTrinh.animateY(1000);
        chartChuongTrinh.getDescription().setText("Top 5 thời lượng phát sóng theo chương trình");
        chartChuongTrinh.getDescription().setTextColor(Color.BLUE);

        thongKeAdapter = new ThongKeAdapter(getContext(), R.layout.list_layout_thongke, dataList);
        lvThongKeCT.setAdapter(thongKeAdapter);

        setEvent();

        return mView;
    }

    public void getData() {
        db = new QuanLyTruyenHinhHelper(getContext());
        dataList.clear();
        String sql = "SELECT ct.TenCT, SUM(tt.ThoiLuong) ThoiLuongPhatSong\n" +
                    "FROM ThongTinPhatSong tt INNER JOIN ChuongTrinh ct\n" +
                    "ON tt.MaCT = ct.MaCT\n" +
                    "GROUP BY ct.TenCT\n" +
                    "ORDER BY ThoiLuongPhatSong DESC";
        Cursor cursor = db.getData(sql);

        ThongKe tk;
        while (cursor.moveToNext()) {
            tk = new ThongKe();
            tk.setLabel(cursor.getString(0));
            tk.setValue(cursor.getInt(1));
            dataList.add(tk);
        }
    }

    private void setControl() {
        lvThongKeCT = mView.findViewById(R.id.lvThongKeCT);
        chartChuongTrinh = mView.findViewById(R.id.chartChuongTrinh);
        btnInReport2 = mView.findViewById(R.id.btnInReport2);
    }

    private void setEvent() {
        btnInReport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View report = mView.findViewById(R.id.chartChuongTrinh);
                checkPermission(report);
            }
        });
    }

    public void checkPermission(View view) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                createFile();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    public void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "report-chuong-trinh");
        startActivityForResult(intent, CREATE_FILE);
    }

    public Image getImageFromView(View view) {
        Bitmap bitmap = getBitmapFromView(view);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return image;
    }

    public Bitmap getBitmapFromView(View view) {
        // Định nghĩa một Bitmap với cùng kích cỡ với view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // Gắn một canvas vào
        Canvas canvas = new Canvas(returnedBitmap);

        // Lấy background của view
        Drawable drawable = view.getBackground();
        if (drawable != null) {
            // nếu có bg thì vẽ lên canvas
            drawable.draw(canvas);
        }
        else {
            // nếu không có bg thì vẽ bg trắng lên canvas
            canvas.drawColor(Color.WHITE);
        }

        // vẽ view lên canvas
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if(data != null) {
                uri = data.getData();

                ParcelFileDescriptor pdf = null;
                try {
                    pdf = getActivity().getContentResolver().openFileDescriptor(uri, "w");
                    FileOutputStream fileOutputStream = new FileOutputStream(pdf.getFileDescriptor());

                    PdfWriter writer = new PdfWriter(fileOutputStream);
                    com.itextpdf.kernel.pdf.PdfDocument pdfDocument = new com.itextpdf.kernel.pdf.PdfDocument(writer);
                    Document document = new Document(pdfDocument);

//                        FontFactory.register(font_path.toString(), "test_font");
//                        Font font = FontFactory.getFont("test_font");
//                        PdfFont pdfFont = PdfFontFactory.createFont(fontPath, true);
                    Paragraph title = new Paragraph("THONG KE THOI LUONG PHAT SONG THEO CHUONG TRINH");
                    title.setTextAlignment(TextAlignment.CENTER);
                    title.setBold();

                    Paragraph title1 = new Paragraph("1. Danh sach thoi luong phat song cua cac chuong trinh");
                    title1.setTextAlignment(TextAlignment.LEFT);
                    title1.setBold();

                    Paragraph title2 = new Paragraph("2. TOP 5 chuong trinh co thoi luong phat song nhieu nhat");
                    title2.setTextAlignment(TextAlignment.LEFT);
                    title2.setBold();

                    View report = mView.findViewById(R.id.chartChuongTrinh);
                    Image image = getImageFromView(report);


                    // Creating a table
                    float [] pointColumnWidths = {50F, 200F, 150F};
                    Table table = new Table(pointColumnWidths);
                    table.setTextAlignment(TextAlignment.CENTER);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                    // Adding cells to the table
                    table.addCell(new Cell().add(new Paragraph("STT").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("Chuong trinh").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("Thoi luong (phut)").setTextAlignment(TextAlignment.CENTER)));

                    for (int i = 0; i < dataList.size(); i++) {
                        table.addCell(new Cell().add(new Paragraph((i + 1) + "")));
                        table.addCell(new Cell().add(new Paragraph(dataList.get(i).getLabel()).setTextAlignment(TextAlignment.LEFT)));
                        table.addCell(new Cell().add(new Paragraph(dataList.get(i).getValue() + "").setTextAlignment(TextAlignment.RIGHT)));
                    }

                    document.add(title);
                    document.add(title1);
                    document.add(table);
                    document.add(title2);
                    document.add(image);

                    document.close();

                    Toast.makeText(getContext(), "Pdf is saved successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ex) {
                    try {
                        DocumentsContract.deleteDocument(getActivity().getContentResolver(), uri);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ex.printStackTrace();
                }
            }
        }
    }
}