package com.example.manager.shoppy.retrofit;

import com.example.manager.shoppy.model.NotiResponse;
import com.example.manager.shoppy.model.NotiSendData;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                    "Content-Type:application/json",
                        "Authorization: key = AAAALZiCSFQ:APA91bGBNl_ScOIpT3CoYf7vl2vSJ3jiYlctuF-d7oSyxx3otv6CedPf05CzlZslXFdXE8E6r9fpcTrrr2XV3jv8_afBP2ybnsp_NQV3GFGIXsC7u407BpRaint_n26sz9F4oFlBhX6-"
            }
    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSendData data);
}
