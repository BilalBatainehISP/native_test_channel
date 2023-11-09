package com.example.native_test_channel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.native_test_channel.utils.PrintUtil;
import com.gengcon.www.jcprintersdk.callback.PrintCallback;
import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.HashMap;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = PrinterStrings.channel;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
                .setMethodCallHandler(
                        (call, result) -> {
                            if(call.method.equals(PrinterStrings.connectCommand)){
                                String printerMac = call.argument(PrinterStrings.macArg);
                                if(printerMac!=null){
                                    runOnUiThread(() -> Toast.makeText(this,"before Connect label = " + printerMac , Toast.LENGTH_LONG).show());
                                    connect(printerMac);
                                    ///  Toast.makeText(this, PrinterManager.connectResult, Toast.LENGTH_SHORT).show();

                                }
                            }
                            else if(call.method.equals(PrinterStrings.printCommand)){
                                String imgPath = call.argument(PrinterStrings.imgPathArg);
                                if(imgPath!=null){
                                    runOnUiThread(() -> Toast.makeText(this,"before print label" , Toast.LENGTH_LONG).show());
                                    printLabel(1,1);
                                }
                            }
                        }
                );
    }


    /**
     * image data
     */
    private ArrayList<String> jsonList;
    /**
     * image processing data
     */
    private ArrayList<String> infoList;
    /**
     * total pages
     */
    private int pageCount;

    /**
     * Number of pages to print
     */
    private int quantity;
    /**
     * Is there a printing error?
     */

    private boolean isError;
    /**
     * Whether to cancel printing
     */
    private boolean isCancel;

    /**
     * Print mode
     */
    private int printMode;

    /**
     * Print density
     */
    private int printDensity;

    /**
     Print magnification (resolution)
     */
    private Float printMultiple;

    /**
     * Global variable used to track the number of pages of print data generated
     */
    private int generatedPrintDataPageCount = 0;

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
     ///   permissionRequest();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        initPrint();
        runOnUiThread(() -> Toast.makeText(this,"Init Print & Data"  , Toast.LENGTH_LONG).show());

        ///   initEvent();
    }

    private void initPrint() {
        initPrintData();
        pageCount = 0;
        quantity = 1;
        isError = false;
        isCancel = false;
    }

    private void initPrintData() {
        jsonList = new ArrayList<>();
        infoList = new ArrayList<>();
    }
    static final String TAG = "MainActivity";

    int connectResult;


    void connect(String printerMac){

        runOnUiThread(() -> Toast.makeText(this,printerMac  , Toast.LENGTH_LONG).show());

        try {
            connectResult = PrintUtil.connectBluetoothPrinter(printerMac);

            runOnUiThread(() -> Toast.makeText(this,"connectBluetoothPrinter"  , Toast.LENGTH_LONG).show());
            runOnUiThread(() -> Toast.makeText(this, String.valueOf(connectResult) , Toast.LENGTH_LONG).show());

        }catch (Exception e){
            runOnUiThread(() -> Toast.makeText(this.getContext(),  e.getMessage(), Toast.LENGTH_LONG).show());
            Log.d(TAG, "Crash log" + e.getMessage());

        }
    }



     void printLabel(int pages, int copies) {
//        // Check if the printer is connected
//        if (PrintUtil.isConnection() != 0) {
//            Toast.makeText(this, "Printer not connected", Toast.LENGTH_SHORT).show();
//            return;
//        }

     //    runOnUiThread(() -> Toast.makeText(this,"Print Label"  , Toast.LENGTH_LONG).show());



         // Reset errors and cancel printing status
        isError = false;
        isCancel = false;
        // 初始化打印数据
        initPrintData();
        // Initialize the number of pages of print data generated before each print job
        generatedPrintDataPageCount = 0;
        // 设置打印的总页数和份数
        pageCount = pages;
        quantity = copies;
        int totalQuantity = pageCount * quantity;
        //setTotalQuantityOfPrints Deprecated, the usage method has a clearer meaning.setTotalPrintQuantity
        PrintUtil.getInstance().setTotalPrintQuantity(totalQuantity);
       //  runOnUiThread(() -> Toast.makeText(this,"setTotalPrintQuantity"  , Toast.LENGTH_LONG).show());

         // Printing parameter settings
       /// Log.d(TAG, "Test: Parameter Settings-Print Density： " + printDensity + "，Print mode:" + printMode);
        /*
         * Parameter 1: Print density, Parameter 2: Paper type Parameter 3: Print mode
         * Print density B50B50WT6T7T8 recommends setting 6 or 8, Z401B32 recommends setting 8, B3SB21B203B1 recommends setting 3
         */
      ///   runOnUiThread(() -> Toast.makeText(this,"startPrintJob"  , Toast.LENGTH_LONG).show());


         try {
             PrintUtil.getInstance().startPrintJob(3, 1, 1, new PrintCallback() {
                 @Override
                 public void onProgress(int pageIndex, int quantityIndex, HashMap<String, Object> hashMap) {

                   ///  runOnUiThread(() -> Toast.makeText(MainActivity.this,"Printing progress: has printed to the"  , Toast.LENGTH_LONG).show());
                     // Update printing progress
                     String progressMessage = "Printing progress: has printed to the" + pageIndex + "Page, no." + quantityIndex + "share";
                   //  Log.d(TAG, "test:" + progressMessage);
                     // Handle printing completion status
                     if (pageIndex == pageCount && quantityIndex == quantity) {
                       ///  runOnUiThread(() -> Toast.makeText(MainActivity.this,"test:onProgress: End printing"  , Toast.LENGTH_LONG).show());

                        // Log.d(TAG, "test:onProgress: End printing");
                         //endJob，使用方法含义更明确的endPrintJob
                         if (PrintUtil.getInstance().endPrintJob()) {
                           ///  runOnUiThread(() -> Toast.makeText(MainActivity.this,"End printing successfully"  , Toast.LENGTH_LONG).show());

                          ///   Log.d(TAG, "End printing successfully");
                         } else {
                          //   runOnUiThread(() -> Toast.makeText(MainActivity.this,"End printing failed"  , Toast.LENGTH_LONG).show());

                           ///  Log.d(TAG, "End printing failed");
                         }

                     }


                 }


                 @Override
                 public void onError(int i) {
                     runOnUiThread(() -> Toast.makeText(MainActivity.this,"onError "+ i  , Toast.LENGTH_LONG).show());

                 }


                 @Override
                 public void onError(int errorCode, int printState) {
                     Log.d(TAG, "test：Report an error");
                     isError = true;
                     runOnUiThread(() -> Toast.makeText(MainActivity.this,"errorCode = "+ errorCode + " printState = "+ printState  , Toast.LENGTH_LONG).show());


                 }

                 @Override
                 public void onCancelJob(boolean isSuccess) {
                     Log.d(TAG, "onCancelJob: " + isSuccess);
                     isCancel = true;
                 }

                 /**
                  * SDK缓存空闲回调，可以在此处传入打印数据
                  *
                  * @param pageIndex The current callback function handles the printing index of the next page
                  * @param bufferSize The size of the cache space
                  */
                 @Override
                 public void onBufferFree(int pageIndex, int bufferSize) {
                     // Returns if an error occurs, printing has been canceled, or pageIndex exceeds the total number of pages

                         if (isError || isCancel || pageIndex > pageCount) {
                             return;
                         }

                         Log.d(TAG, "Test-idle data callback-data generation judgment-total number of pages " + pageCount + ",Number of pages generated:" + generatedPrintDataPageCount + ",Idle callback data length：" + bufferSize);
                         // Generate print data
                         generatePrintDataIfNeeded(bufferSize);


                 }
             });
         }catch (Exception e){

             runOnUiThread(() -> Toast.makeText(this,e.getMessage()  , Toast.LENGTH_LONG).show());

         }


    }

    private void generatePrintDataIfNeeded(int bufferSize) {
        // If the number of pages of print data that has been generated is less than the total number of pages, continue to generate
        if (generatedPrintDataPageCount < pageCount) {
            // Calculate the length of data to be generated this time so as not to exceed the total number of pages
            int commitDataLength = Math.min((pageCount - generatedPrintDataPageCount), bufferSize);

            // Generate data
            generateMultiPagePrintData(generatedPrintDataPageCount, generatedPrintDataPageCount + commitDataLength);

            // Submit print data
            PrintUtil.getInstance().commitData(jsonList.subList(generatedPrintDataPageCount, generatedPrintDataPageCount + commitDataLength), infoList.subList(generatedPrintDataPageCount, generatedPrintDataPageCount + commitDataLength));
            // Update the number of generated print data pages
            generatedPrintDataPageCount += commitDataLength;
        }
    }


    private void generateMultiPagePrintData(int index, int cycleIndex) {
        while (index < cycleIndex) {
            // Set printing parameters
            float width = 60;
            float height = 40;
            int orientation = 0;
            float marginX = 2.0F;
            float marginY = 2.0F;
            //Rectangular box type
            float rectangleWidth = width - marginX * 2;
            float rectangleHeight = height - marginY * 2;
            float lineWidth = 0.5F;
            //1. Circle 2. Ellipse 3. Rectangle 4. Rounded rectangle
            int graphType = 3;

            float lineHeight = rectangleHeight / 5.0F;

            float titleWidth = rectangleWidth * 2 / 5.0F;
            float contentWidth = rectangleWidth * 3 / 5.0F;

            float fontSize = 3.0F;
            // Set initial offset
            float offsetY = 0F;
            float offsetX = 0F;
            //Calculate the y coordinate of the drawn line
            float secondLineY = marginY + lineHeight * 2 - lineWidth + offsetY;
            float thirdLineY = marginY + lineHeight * 3 - lineWidth + offsetY;
            float fourthLineY = marginY + lineHeight * 4 - lineWidth + offsetY;


            // Set canvas size
            PrintUtil.getInstance().drawEmptyLabel(width, height, orientation, "");

            //Draw graphics
            PrintUtil.getInstance().drawLabelGraph(marginX + offsetX, marginY + offsetY, rectangleWidth, rectangleHeight, graphType, 0, 2, lineWidth, 1, new float[]{0.7575f, 0.7575f});


            //draw lines
            PrintUtil.getInstance().drawLabelLine(marginX + offsetX, marginY + lineHeight - lineWidth + offsetY, rectangleWidth, lineWidth, 0, 1, new float[]{});
            PrintUtil.getInstance().drawLabelLine(marginX + offsetX, secondLineY, rectangleWidth, lineWidth, 0, 1, new float[]{});
            PrintUtil.getInstance().drawLabelLine(marginX + offsetX, thirdLineY, rectangleWidth, lineWidth, 0, 1, new float[]{});
            PrintUtil.getInstance().drawLabelLine(marginX + offsetX, fourthLineY, rectangleWidth, lineWidth, 0, 1, new float[]{});

            PrintUtil.getInstance().drawLabelLine(marginX + titleWidth - lineWidth + offsetX, marginY + lineHeight + offsetY, lineWidth, rectangleHeight - lineHeight, 0, 1, new float[]{});

            //To draw a large title, use line wrapping mode 6, fixed width and height, and scale when the content is too large (the difference from mode 1 is that the text content is based on the budgeted font size, and when the budgeted text box width does not exceed the preset height after typesetting, the text will not be enlarged. Instead, the text is aligned with the text box according to the preset alignment)
            PrintUtil.getInstance().drawLabelText(marginX * 3 + offsetX, marginY + offsetY, rectangleWidth - marginX * 4, lineHeight, "Wuhan Jingchen Intelligent Sign Technology Co., Ltd.", "Song Dynasty", fontSize * 1.5F, 0, 1, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            // Draw subtitles
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + offsetX, marginY + lineHeight - lineWidth + offsetY, titleWidth - marginX * 3, lineHeight, "model", "Song Dynasty", fontSize, 0, 1, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + offsetX, secondLineY, titleWidth - marginX * 3, lineHeight, "Asset Number", "Song Dynasty", fontSize, 0, 1, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + offsetX, thirdLineY, titleWidth - marginX * 3, lineHeight, "Activation date", "Song Dynasty", fontSize, 0, 1, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + offsetX, fourthLineY, titleWidth - marginX * 3, lineHeight, "Storage location", "Song Dynasty", fontSize, 0, 1, 1, 6, 0, 1, new boolean[]{false, false, false, false});

            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + titleWidth + offsetX, marginY + lineHeight - lineWidth + offsetY, contentWidth - marginX * 3, lineHeight, "DELL monitor E6540", "Song Dynasty", fontSize, 0, 0, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + titleWidth + offsetX, secondLineY, contentWidth - marginX * 3, lineHeight, "C212004", "Song Dynasty", fontSize, 0, 0, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + titleWidth + offsetX, thirdLineY, contentWidth - marginX * 3, lineHeight, "2014-06-10", "Song Dynasty", fontSize, 0, 0, 1, 6, 0, 1, new boolean[]{false, false, false, false});
            PrintUtil.getInstance().drawLabelText(marginX * 2.5f + titleWidth + offsetX, fourthLineY, contentWidth - marginX * 3, lineHeight, (index + 1) + "Office No.", "Song Dynasty", fontSize, 0, 0, 1, 6, 0, 1, new boolean[]{false, false, false, false});


            //Generate print data
            byte[] jsonByte = PrintUtil.getInstance().generateLabelJson();

            //Convert to json Str
            String jsonStr = new String(jsonByte);


            jsonList.add(jsonStr);
            //Except for B 32 Z 401 T 8, the print multiple is 11.81, the others are 8
            String jsonInfo = "{  " + "\"printerImageProcessingInfo\": " + "{    " + "\"orientation\":" + orientation + "," + "   \"margin\": [      0,      0,      0,      0    ], " + "   \"printQuantity\": " + quantity + ",  " + "  \"horizontalOffset\": 0,  " + "  \"verticalOffset\": 0,  " + "  \"width\":" + width + "," + "   \"height\":" + height + "," + "\"printMultiple\":" + printMultiple + "," + "  \"epc\": \"\"  }}";
            infoList.add(jsonInfo);

            index++;
        }
    }

}


class PrinterStrings {
    // channel name
    static String channel = "android.flutter/printer";
    //commands
    static String connectCommand = "printer_connect";
    static String printCommand = "printer_print";
    // arguments
    static String macArg = "printer_mac";
    static String imgPathArg = "img_path";
}