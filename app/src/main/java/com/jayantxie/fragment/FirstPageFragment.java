package com.jayantxie.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.jayantxie.R;
import com.jayantxie.activity.MyBlueToothService;
import com.jayantxie.activity.MainActivity;
import com.jayantxie.activity.SettingsActivity;
import com.jayantxie.activity.UploadActivity;
import com.jayantxie.pojo.EvaluationData;
import com.jayantxie.pojo.InitialSettings;
import com.jayantxie.utils.JsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;



public class FirstPageFragment extends Fragment
        implements android.view.View.OnClickListener{
    private Switch blueToothSwitch;               //蓝牙开关
    private Button settingsButton;
    private Button setAngleButton;
    private Button reSetAngleButton;
    private Button finishSetAngle;
    private Button beginWork;
    private Button searchButton;
    private Button connectButton;
    //private Button suspendedButton;
    //private Button continuelButton;

    private EvaluationData mEvaluationData;
    private MainActivity mMainActivity;            //传入上下文
    private BluetoothAdapter bluetoothAdapter;    //蓝牙适配器
    private ArrayAdapter<String> adapter;         //存储适配器
    private List<String> list = new ArrayList<>();
    private Spinner blueToothList;
    private String macAddress;
    private BluetoothSocket theSocket;            //蓝牙通讯套接字
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean booleanConnect = false;
    private InitialSettings mInitialSettings;
    private ConnectedThread mConnectedThread;
    private MyBlueToothService.MyBinder myBinder;

    //蓝牙Service启动和初始化，设置了InputStream、初始设置的参数，并启动监听
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyBlueToothService.MyBinder) service;
            myBinder.setInOutputStream(inputStream,outputStream);
            myBinder.setInitialSettings(mInitialSettings);
            myBinder.beginListen();
            myBinder.beginTimerTask();
            myBinder.beginEndTimerTask();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("MyFinishReceiver");
            mMainActivity.registerReceiver(finishReceiver,intentFilter);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_first_page, container, false);
        //组件设置
        mMainActivity = (MainActivity)getActivity();
        blueToothSwitch = (Switch) rootView.findViewById(R.id.mySwitch);
        settingsButton = (Button) rootView.findViewById(R.id.settings);
        setAngleButton = (Button) rootView.findViewById(R.id.setangle);
        reSetAngleButton = (Button) rootView.findViewById(R.id.resetangle);
        finishSetAngle = (Button) rootView.findViewById(R.id.finishsetangle);
        beginWork = (Button) rootView.findViewById(R.id.beginWork);
        searchButton = (Button) rootView.findViewById(R.id.mysearch_button);
        connectButton  = (Button) rootView.findViewById(R.id.myconnect_button);
        //suspendedButton = (Button) rootView.findViewById(R.id.suspended);
        //continuelButton = (Button)rootView.findViewById(R.id.continuel);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        blueToothList = (Spinner) rootView.findViewById(R.id.list);

        settingsButton.setOnClickListener(this);
        setAngleButton.setOnClickListener(this);
        reSetAngleButton.setOnClickListener(this);
        finishSetAngle.setOnClickListener(this);
        beginWork.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        connectButton.setOnClickListener(this);
        //suspendedButton.setOnClickListener(this);
        //continuelButton.setOnClickListener(this);

        //检查手机是否支持蓝牙功能，不是则不再显示功能，界面不加载
        if(bluetoothAdapter == null){
            //此手机不支持蓝牙
            Toast.makeText(mMainActivity, "未发现蓝牙设备", Toast.LENGTH_SHORT).show();
            return rootView;
        }
        if(bluetoothAdapter.isEnabled()) {
            blueToothSwitch.setChecked(true);
        }

        adapter = new ArrayAdapter<>(mMainActivity,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blueToothList.setAdapter(adapter);

        //选中对应的连接设备，传出mac地址，并设置连接
        blueToothList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                macAddress = adapter.getItem(position).substring(0,17);
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        blueToothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!bluetoothAdapter.isEnabled()){  //开启蓝牙
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(enableIntent);
                    }else{
                        Toast.makeText(mMainActivity,"已打开蓝牙！",Toast.LENGTH_SHORT).show();
                    }
                }else{                                  //关闭蓝牙
                    bluetoothAdapter.disable();
                    Toast.makeText(mMainActivity,"已关闭蓝牙！",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            mInitialSettings = (InitialSettings) data.getSerializableExtra("initialSettings");
            Toast.makeText(mMainActivity,"设置成功！"+mInitialSettings.getEndTime().toString(),
                    Toast.LENGTH_SHORT).show();
            settingsButton.setVisibility(View.INVISIBLE);
            setAngleButton.setVisibility(View.VISIBLE);
        }
    }

    private void searchBlueToothDevice(Context context){
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if(devices.size() > 0){
            for(BluetoothDevice device:devices){
                adapter.remove(device.getAddress()+ "  "+device.getName());
                adapter.add(device.getAddress()+"  "+device.getName());
            }
        }else{
            //注册，一个新的设备被发现时调用mReceive
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver(mReceiver,filter);
        }
        Log.d("myDebug", "11111");
    }


    //蓝牙连接广播接收器
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //已经配对则跳过
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    adapter.add(device.getAddress()+"  "+device.getName());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){ //搜索结束
                if(adapter.getCount() == 0){
                    Toast.makeText(mMainActivity,"没有搜索到蓝牙设备！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //工作结束终止service监听，调用广播接收器回返数据
    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //结束后处理逻辑
            mMainActivity.unbindService(connection);
            try {
                theSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String distractionTimes = intent.getStringExtra("distractionTimes");
            blueToothSwitch.setVisibility(View.VISIBLE);
            searchButton.setVisibility(View.VISIBLE);
            blueToothList.setVisibility(View.VISIBLE);
            Intent mIntent = new Intent();
            mIntent.putExtra("distractionTimes",distractionTimes);
            mIntent.putExtra("mEvaluationData",intent.getStringExtra("mEvaluationData"));
            mIntent.setClass(mMainActivity, UploadActivity.class);
            startActivity(mIntent);
        }
    };


    private class ConnectThread extends Thread {
        private BluetoothSocket mSocket;
        private BluetoothDevice mDevice;

        public ConnectThread(BluetoothDevice device){
            mDevice = device;
            try {
                mSocket = mDevice.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            }catch (IOException e){
                mSocket = null;
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            bluetoothAdapter.cancelDiscovery();    //取消设备查找
            try{
                Log.d("myDebug",mSocket.toString());
                mSocket.connect();
            }catch (IOException E){
                try{
                    mSocket.close();
                    mUIChangeHandler.sendEmptyMessage(1);
                }catch (IOException e){
                }
                E.printStackTrace();
                return;
            }
            Log.d("myDebug","333333333");
            booleanConnect = true;
            theSocket = mSocket;
            mConnectedThread = new ConnectedThread(theSocket);
            mUIChangeHandler.sendEmptyMessage(0);
        }
    }

    public class UIChangeHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            if(msg.what == 0){
                Toast.makeText(mMainActivity,"连接成功！",Toast.LENGTH_SHORT).show();
                blueToothSwitch.setVisibility(View.INVISIBLE);
                connectButton.setVisibility(View.INVISIBLE);
                blueToothList.setVisibility(View.INVISIBLE);
                settingsButton.setVisibility(View.VISIBLE);
            }
            else{
                Toast.makeText(mMainActivity,"连接失败！",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public UIChangeHandler mUIChangeHandler = new UIChangeHandler();

    private class ConnectedThread extends Thread {
        private BluetoothSocket mSocket;

        public ConnectedThread(BluetoothSocket socket){
            mSocket = socket;
            try{
                inputStream = mSocket.getInputStream();
                outputStream = mSocket.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
        }

        public void write(char a){
            try{
                outputStream.write(a);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public  void onClick(View arg0){
        switch (arg0.getId()) {
            case R.id.settings:
                Intent intent = new Intent();
                intent.setClass(mMainActivity, SettingsActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.setangle:
                mConnectedThread.write('a');
                Toast.makeText(mMainActivity,"请设置工作范围！",Toast.LENGTH_SHORT).show();
                setAngleButton.setVisibility(View.INVISIBLE);
                reSetAngleButton.setVisibility(View.VISIBLE);
                finishSetAngle.setVisibility(View.VISIBLE);
                break;
            case R.id.resetangle:
                Toast.makeText(mMainActivity,"重新设置工作范围！",Toast.LENGTH_SHORT).show();
                mConnectedThread.write('a');
                break;
            case R.id.finishsetangle:
                mConnectedThread.write('b');
                Toast.makeText(mMainActivity,"设置成功！",Toast.LENGTH_SHORT).show();
                reSetAngleButton.setVisibility(View.INVISIBLE);
                finishSetAngle.setVisibility(View.INVISIBLE);
                beginWork.setVisibility(View.VISIBLE);
                break;
            case R.id.beginWork:
                mConnectedThread.write('d');
                mInitialSettings.setBeginTime(new Date());
                //service可能需要后续的在onDestroy中关闭，
                //同时，广播接收器也要关闭
                Intent bindIntent = new Intent(mMainActivity,
                        MyBlueToothService.class);
                mMainActivity.bindService(bindIntent,
                        connection,Context.BIND_AUTO_CREATE);
                beginWork.setVisibility(View.INVISIBLE);
                //suspendedButton.setVisibility(View.VISIBLE);
                break;
            case R.id.mysearch_button:
                if (bluetoothAdapter == null) {
                    Toast.makeText(mMainActivity, "未发现蓝牙设备", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(mMainActivity, "蓝牙设备未开启", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开启搜索设备
                searchBlueToothDevice(mMainActivity);
                searchButton.setVisibility(View.INVISIBLE);
                connectButton.setVisibility(View.VISIBLE);
                break;
            case R.id.myconnect_button:
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAddress);
                ConnectThread connectThread = new ConnectThread(device);
                connectThread.start();

                break;
            /*
            case R.id.suspended:
                mConnectedThread.write('g');
                suspendedButton.setVisibility(View.INVISIBLE);
                continuelButton.setVisibility(View.VISIBLE);
                break;
            case R.id.continuel:
                mConnectedThread.write('d');
                suspendedButton.setVisibility(View.VISIBLE);
                continuelButton.setVisibility(View.INVISIBLE);
                break;
            */
            default:
                break;
        }
    }

}
