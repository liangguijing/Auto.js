package org.liang.test.network.api;

import org.liang.test.network.entity.VersionInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Stardust on 2017/9/20.
 */

public interface UpdateCheckApi {

    @GET("/assets/autojs/version.json")
    @Headers("Cache-Control: no-cache")
    Observable<VersionInfo> checkForUpdates();

}
