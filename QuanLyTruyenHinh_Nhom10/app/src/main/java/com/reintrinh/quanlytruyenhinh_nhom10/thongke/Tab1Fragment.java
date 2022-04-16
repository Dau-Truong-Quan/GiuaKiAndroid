package com.reintrinh.quanlytruyenhinh_nhom10.thongke;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
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
import java.util.ArrayList;
import java.util.List;

public class Tab1Fragment extends Fragment {

    QuanLyTruyenHinhHelper db;
    View mView;
    BarChart chartTheLoai;
    Button btnInReport1;

    ListView lvThongKeTL;
    ArrayList<ThongKe> dataList = new ArrayList<>();;
    ThongKeAdapter thongKeAdapter;

    public static final int CREATE_FILE = 100;
    public static final String fontPath = "src/main/res/font/vuarial.ttf";

    public Tab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tab1, container, false);

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

        XAxis xAxis = chartTheLoai.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAxisMinimum(0f);
        //xAxis.setGranularity(1f);
        xAxis.setLabelCount(labels.size());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Thể loại");

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData barData = new BarData(barDataSet);
        chartTheLoai.setData(new BarData(barDataSet));
        chartTheLoai.animateY(1000);
        chartTheLoai.getDescription().setText("Top 5 thời lượng phát sóng theo thể loại");
        chartTheLoai.getDescription().setTextColor(Color.BLUE);

        thongKeAdapter = new ThongKeAdapter(getContext(), R.layout.list_layout_thongke, dataList);
        lvThongKeTL.setAdapter(thongKeAdapter);

        setEvent();

        return mView;
    }

    private void setEvent() {
        btnInReport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View report = mView.findViewById(R.id.chartTheLoai);
                checkPermission(report);
            }
        });
    }

    public void getData() {
        db = new QuanLyTruyenHinhHelper(getContext());
        dataList.clear();
        String sql = "SELECT tl.TenTL, SUM(tt.ThoiLuong) ThoiLuongPhatSong\n" +
                    "FROM ThongTinPhatSong tt INNER JOIN ChuongTrinh ct INNER JOIN TheLoai tl\n" +
                    "ON tt.MaCT = ct.MaCT and ct.MaTL = tl.MaTL\n" +
                    "GROUP BY tl.TenTL\n" +
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
        lvThongKeTL = mView.findViewById(R.id.lvThongKeTL);
        chartTheLoai = mView.findViewById(R.id.chartTheLoai);
        btnInReport1 = mView.findViewById(R.id.btnInReport1);
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
        intent.putExtra(Intent.EXTRA_TITLE, "report-the-loai");
        startActivityForResult(intent, CREATE_FILE);
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

    public Image getImageFromView(View view) {
        Bitmap bitmap = getBitmapFromView(view);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);
        return image;
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
                    PdfDocument pdfDocument = new PdfDocument(writer);
                    Document document = new Document(pdfDocument);

//                        FontFactory.register(font_path.toString(), "test_font");
//                        Font font = FontFactory.getFont("test_font");
//                        PdfFont pdfFont = PdfFontFactory.createFont(fontPath, true);
                    Paragraph title = new Paragraph("THONG KE THOI LUONG PHAT SONG THEO THE LOAI");
                    title.setTextAlignment(TextAlignment.CENTER);
                    title.setBold();

                    Paragraph title1 = new Paragraph("1. Danh sach thoi luong phat song cua cac the loai");
                    title1.setTextAlignment(TextAlignment.LEFT);
                    title1.setBold();

                    Paragraph title2 = new Paragraph("2. TOP 5 the loai co thoi luong phat song nhieu nhat");
                    title2.setTextAlignment(TextAlignment.LEFT);
                    title2.setBold();

                    View report = mView.findViewById(R.id.chartTheLoai);
                    Image image = getImageFromView(report);


                    // Creating a table
                    float [] pointColumnWidths = {50F, 200F, 150F};
                    Table table = new Table(pointColumnWidths);
                    table.setTextAlignment(TextAlignment.CENTER);
                    table.setHorizontalAlignment(HorizontalAlignment.CENTER);

                    // Adding cells to the table
                    table.addCell(new Cell().add(new Paragraph("STT").setTextAlignment(TextAlignment.CENTER)));
                    table.addCell(new Cell().add(new Paragraph("The loai").setTextAlignment(TextAlignment.CENTER)));
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