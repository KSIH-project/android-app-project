package com.project.ksih_android.utility.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(

            {
                    "Content-Type:Application/json",
                    "Authorization:key=AAAAQXikyqY:APA91bHNXMvbzV25GcGqjaCUhTbWxtwZFEEgvsXYkS9uuyb7Zxz-Iv5RDWUb5WqGdIy3FNldvSndXBOAfLjYUOhBE0Wh_6Gx9VA7du72sJ5TU7CzP5IjwfHVzmkiUvGmQpoMSii_MEwH"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
