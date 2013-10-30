package com.google.cloud.backend.android;

import java.io.IOException;

import android.widget.Toast;

public class Handlers extends CloudBackendActivity {
	
	public static CloudCallbackHandler<CloudEntity> getHandler2(){
		CloudCallbackHandler<CloudEntity> handler2 = new CloudCallbackHandler<CloudEntity>() {
	
		@Override
		public void onComplete(final CloudEntity result) {
			
		}

		@Override
		public void onError(final IOException exception) {
			//handleEndpointException(exception);
		}
		
	};
	
	return handler2;
}
}
