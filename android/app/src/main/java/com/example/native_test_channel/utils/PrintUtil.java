package com.example.native_test_channel.utils;


import com.example.native_test_channel.MyApplication;
import com.gengcon.www.jcprintersdk.JCPrintApi;
import com.gengcon.www.jcprintersdk.callback.Callback;

/**
 * 打印工具类
 *
 * @author zhangbin
 */
public class PrintUtil {
    /**
     * 单例实例，使用 volatile 保证多线程可见性和有序性
     */
    private static volatile JCPrintApi api;

    /**
     * 回调接口，用于处理打印机状态变化事件
     */
    private static final Callback CALLBACK = new Callback() {
        /**
         * 连接成功回调
         *
         * @param address 设备地址，蓝牙为蓝牙 MAC 地址，WIFI 为 IP 地址
         * @param type   连接类型，0 表示蓝牙连接，1 表示 WIFI 连接
         */
        @Override
        public void onConnectSuccess(String address, int type) {

        }

        /**
         * 断开连接回调
         * 当设备断开连接时，将调用此方法。
         */
        @Override
        public void onDisConnect() {

        }

        /**
         * 电量变化回调
         * 当设备电量发生变化时，将调用此方法。
         *
         * @param powerLevel 电量等级，取值范围为 1 到 4，代表有 1 到 4 格电，满电是 4 格
         */
        @Override
        public void onElectricityChange(int powerLevel) {

        }

        /**
         * 监测上盖状态变化回调
         * 当上盖状态发生变化时，将调用此方法。目前该回调仅支持 H10/D101/D110/D11/B21/B16/B32/Z401/B3S/B203/B1/B18 系列打印机。
         *
         * @param coverStatus 上盖状态，0 表示上盖打开，1 表示上盖关闭
         */
        @Override
        public void onCoverStatus(int coverStatus) {

        }

        /**
         * 监测纸张状态变化
         * 当纸张状态发生变化时，将调用此方法。目前该回调仅支持H10/D101/D110/D11/B21/B16/B32/Z401/B203/B1/B18 系列打印机。
         *
         * @param paperStatus 0为不缺纸 1为缺纸
         */
        @Override
        public void onPaperStatus(int paperStatus) {

        }

        /**
         * 监测标签rfid读取状态变化
         * 当标签rfid读取状态发生变化时，将调用此方法。
         *
         * @param rfidReadStatus 0为未读取到标签RFID 1为成功读取到标签RFID 目前该回调仅支持H10/D101/D110/D11/B21/B16/B32/Z401/B203/B1/B18 系列打印机。
         */
        @Override
        public void onRfidReadStatus(int rfidReadStatus) {

        }


        /**
         * 监测碳带rfid读取状态变化
         * 当碳带rfid读取状态发生变化时，将调用此方法。
         *
         * @param ribbonRfidReadStatus 0为未读取到碳带RFID 1为成功读取到碳带RFID 目前该回调仅支持B18/B32/Z401/P1/P1S 系列打印机。
         */
        @Override
        public void onRibbonRfidReadStatus(int ribbonRfidReadStatus) {

        }

        /**
         * 监测碳带状态变化
         * 当纸张状态发生变化时，将调用此方法
         *
         * @param ribbonStatus 0为无碳带 1为有碳带 目前该回调仅支持B18/B32/Z401/P1/P1S系列打印机。
         */
        @Override
        public void onRibbonStatus(int ribbonStatus) {

        }


        /**
         * 固件异常回调，需要升级
         * 当设备连接成功但出现固件异常时，将调用此方法，表示需要进行固件升级。
         */
        @Override
        public void onFirmErrors() {

        }
    };


    /**
     * Use init Default Image Library Settings instead, the meaning of the method name is more accurate
     *
     * @return JCPrintApi 实例
     */
    public static JCPrintApi getInstance() {
        // Double-check locking to ensure instances are only initialized once
        if (api == null) {
            synchronized (PrintUtil.class) {
                if (api == null) {
                    api = JCPrintApi.getInstance(CALLBACK);
                    //api.init has been abandoned, use init Sdk instead, the meaning of the method name is more accurate
                    api.initSdk(MyApplication.getInstance());
                    //api.initImageProcessingDefault，Use init Default Image Library Settings instead, the meaning of the method name is more accurate
                    api.initDefaultImageLibrarySettings("", "");

                }
            }
        }

        return api;

    }




    /**
     * Turn on the printer via Bluetooth connection via the printer's mac address (synchronization)
     *
     * @param address printer address
     * @return success or failure
     */
    public static int connectBluetoothPrinter(String address) {
       //Get a singleton instance to ensure thread safety
        JCPrintApi localApi = getInstance();
        //api.openPrinterByAddress(address)，使用connectBluetoothPrinter替代，方法名含义更准确
        return localApi.connectBluetoothPrinter(address);
    }

    /**
     * 关闭打印机
     */
    public static void close() {
        // 获取单例实例以确保线程安全
        JCPrintApi localApi = getInstance();
        localApi.close();
    }

    /**
     * 检查打印机是否连接
     *
     * @return 连接状态代码
     */
    public static int isConnection() {
        // 获取单例实例以确保线程安全
        JCPrintApi localApi = getInstance();
        return localApi.isConnection();
    }


}
