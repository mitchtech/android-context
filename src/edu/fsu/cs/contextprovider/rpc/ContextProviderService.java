package edu.fsu.cs.contextprovider.rpc;

import java.util.HashMap;

import edu.fsu.cs.contextprovider.ContextProvider;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import android.util.Log;

public class ContextProviderService extends Service {
	private static final String TAG = "ContextProviderService";

	private final IContextProviderService.Stub mBinder = new IContextProviderService.Stub() {
		public HashMap<String, String> getAll() throws RemoteException {
			HashMap<String, String> cntx = ContextProvider.getAllUnordered();
			return cntx;
		}
	};



	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(TAG, "onStartCommand");

		return 0;
	}



	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(TAG, "onBind");

		return mBinder;
	}

}
