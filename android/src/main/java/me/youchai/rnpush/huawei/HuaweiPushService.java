package me.youchai.rnpush.huawei;

import com.facebook.react.bridge.ReactApplicationContext;
//import com.huawei.hms.api.ConnectionResult;
//import com.huawei.hms.api.HuaweiApiAvailability;
//import com.huawei.hms.api.HuaweiApiClient;
//import com.huawei.hms.support.api.client.PendingResult;
//import com.huawei.hms.support.api.client.ResultCallback;
//import com.huawei.hms.support.api.push.HuaweiPush;
//import com.huawei.hms.support.api.push.TokenResult;

import java.util.List;

import me.youchai.rnpush.Notification;
import me.youchai.rnpush.PushService;
import me.youchai.rnpush.utils.Logger;

public class HuaweiPushService extends PushService /*implements HuaweiApiClient.ConnectionCallbacks, HuaweiApiClient.OnConnectionFailedListener*/ {

  private ReactApplicationContext _rac;
//  private HuaweiApiClient _client;

  public HuaweiPushService(ReactApplicationContext ctx) {
    _rac = ctx;
  }

  @Override
  public void init() {
//    if (_client == null) {
//      _client = new HuaweiApiClient.Builder(_rac)
//          .addApi(HuaweiPush.PUSH_API)
//          .addConnectionCallbacks(this)
//          .addOnConnectionFailedListener(this)
//          .build();
//    }
//    if (!_client.isConnected()) {
//      _client.connect();
//    }
  }

  @Override
  public void stop() {
//    if (_client == null) {
//      return;
//    }
//    if (_client.isConnected()) {
//      _client.disconnect();
//    }
  }

  @Override
  public void resume() {
//    if (_client == null) {
//      return;
//    }
//    if (!_client.isConnected()) {
//      _client.connect();
//    }
  }

  @Override
  public String getName() {
    return "Huawei";
  }

  @Override
  public String getRegistrationId() {
    return null;
  }

  @Override
  public void removeNotifications(List<String> ids) throws Exception {
    Logger.i("not supported");
  }

  @Override
  public void removeAllNotifications() throws Exception {
    Logger.i("not supported");
  }

  @Override
  public void scheduleLocalNotification(Notification notification) {
    Logger.i("not supported");
  }

  @Override
  public void cancelLocalNotifications(List<String> ids) {
    Logger.i("not supported");
  }

  @Override
  public void cancelAllLocalNotifications() {
    Logger.i("not supported");
  }

//  @Override
//  public void onConnected() {
//    Logger.i("Huawei push service connected");
//    PendingResult<TokenResult> re = HuaweiPush.HuaweiPushApi.getToken(_client);
//    re.setResultCallback(new ResultCallback<TokenResult>() {
//      @Override
//      public void onResult(TokenResult result) {
//        Logger.i("get token result status: " + result.getStatus());
//        Logger.i("get token result: " + result.getTokenRes());
//      }
//    });
//  }
//
//  @Override
//  public void onConnectionSuspended(int cause) {
//    Logger.i("Huawei push service suspended: " + cause);
//  }
//
//  @Override
//  public void onConnectionFailed(ConnectionResult result) {
//    Logger.i("Huawei push service connect failed: " + result.getErrorCode());
//    if(HuaweiApiAvailability.getInstance().isUserResolvableError(result.getErrorCode())) {
//      HuaweiApiAvailability.getInstance()
//          .resolveError(_rac.getCurrentActivity(), result.getErrorCode(), 1000);
//    }
//  }
}
