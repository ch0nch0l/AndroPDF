package chonchol.me.andropdf;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    private PdfPCell cell;
    private Image imgReportLogo;

    BaseColor headColor = WebColors.getRGBColor("#DEDEDE");
    BaseColor tableHeadColor = WebColors.getRGBColor("#F5ABAB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnGenPDF = (Button) findViewById(R.id.btnGenPDF);
        btnGenPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermission()) {
                    try {
                        createPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void createPDF() throws FileNotFoundException, DocumentException {

        //Create document file
        Document document = new Document();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmm");
            FileOutputStream outputStream = new FileOutputStream(
                    new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS) + "/AndroPDF"
                            , "AndroPDF_" + dateFormat.format(Calendar.getInstance().getTime()) + ".pdf"
                    ));

            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            //Open the document
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("AndroPDF");
            document.addCreator("http://chonchol.me");

            //Create Header table
            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            float[] fl = new float[]{20, 45, 35};
            header.setWidths(fl);
            cell = new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            //Set Logo in Header Cell
            Drawable logo = MainActivity.this.getResources().getDrawable(android.R.mipmap.sym_def_app_icon);
            Bitmap bitmap = ((BitmapDrawable) logo).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapLogo = stream.toByteArray();
            try {
                imgReportLogo = Image.getInstance(bitmapLogo);
                imgReportLogo.setAbsolutePosition(330f, 642f);

                cell.addElement(imgReportLogo);
                header.addCell(cell);

                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);

                // Heading
                //BaseFont font = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
                Font titleFont = new Font(Font.FontFamily.COURIER, 22.0f, Font.BOLD, BaseColor.BLACK);

                //Creating Chunk
                Chunk titleChunk = new Chunk("Andro PDF", titleFont);
                //Paragraph
                Paragraph titleParagraph = new Paragraph(titleChunk);

                cell.addElement(titleParagraph);
                cell.addElement(new Paragraph("Simple PDF Report"));
                cell.addElement(new Paragraph("Date: " + format.format(Calendar.getInstance().getTime())));
                header.addCell(cell);

                cell = new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                header.addCell(cell);

                PdfPTable pTable = new PdfPTable(1);
                pTable.setWidthPercentage(100);
                cell = new PdfPCell();
                cell.setColspan(1);
                cell.addElement(header);
                pTable.addCell(cell);

                PdfPTable table = new PdfPTable(6);
                float[] columnWidth = new float[]{6, 30, 30, 20, 20, 30};
                table.setWidths(columnWidth);
                cell = new PdfPCell();
                cell.setBackgroundColor(headColor);
                cell.setColspan(6);
                cell.addElement(pTable);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(" "));
                cell.setColspan(6);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(6);
                cell.setBackgroundColor(tableHeadColor);
                cell = new PdfPCell(new Phrase("#"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Header 1"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Header 2"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Header 3"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Header 4"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase("Header 5"));
                cell.setBackgroundColor(tableHeadColor);
                table.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(6);

                for (int i = 1; i <= 10; i++) {
                    table.addCell(String.valueOf(i));
                    table.addCell("Header 1 row " + i);
                    table.addCell("Header 2 row " + i);
                    table.addCell("Header 3 row " + i);
                    table.addCell("Header 4 row " + i);
                    table.addCell("Header 5 row " + i);

                }

                PdfPTable ftable = new PdfPTable(6);
                ftable.setWidthPercentage(100);
                float[] columnWidtha = new float[]{30, 10, 30, 10, 30, 10};
                ftable.setWidths(columnWidtha);
                cell = new PdfPCell();
                cell.setColspan(6);
                cell.setBackgroundColor(tableHeadColor);
                cell = new PdfPCell(new Phrase("Total Number"));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Phrase(""));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(tableHeadColor);
                ftable.addCell(cell);

                cell = new PdfPCell(new Paragraph("Footer"));
                cell.setColspan(6);
                ftable.addCell(cell);

                cell = new PdfPCell();
                cell.setColspan(6);
                cell.addElement(ftable);
                table.addCell(cell);

                document.add(table);

                Toast.makeText(getApplicationContext(), "New PDF named AndroPDF" + dateFormat.format(Calendar.getInstance().getTime()) + ".pdf successfully generated at DOWNLOADS folder", Toast.LENGTH_LONG).show();
            } catch (DocumentException de) {
                Log.e("PDFCreator", "DocumentException:" + de);
            } catch (IOException e) {
                Log.e("PDFCreator", "ioException:" + e);
            } finally {
                document.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Check READ/WRITE PERMISSION
    private boolean checkPermission() {

        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ;
    }

    //Request for Permission
    private void requestPermissionAndContinue() throws FileNotFoundException, DocumentException {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Storage Access Permission!");
                alertBuilder.setMessage("Please provide storage Access permission to generate PDF.");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            createPDF();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {

                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    try {
                        createPDF();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }

            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
