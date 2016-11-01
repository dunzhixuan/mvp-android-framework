package com.mvp.framework.module.base.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mvp.framework.MyApp;
import com.mvp.framework.config.ServerManager;
import com.mvp.framework.module.base.model.imodel.IBaseModel;
import com.mvp.framework.module.base.presenter.BasePresenter;
import com.mvp.framework.module.base.presenter.BaseUploadPresenter;
import com.mvp.framework.network.PostUploadRequest;
import com.mvp.framework.network.SingletonRequestQueue;


import org.json.JSONObject;

/**
 * @ClassName: BaseUploadModel
 * @author create by Tang
 * @date date 16/9/29 下午3:57
 * @Description: TODO
 */
public class BaseUploadModel implements IBaseModel {

    private static final int  VOLLEY_ERROR = 10001;   //网络错误
    private static final String VOLLEY_ERROR_DESC = "点击屏幕重新加载";

    private PostUploadRequest postUploadRequest;
    private String apiInterface;
    private String address;

    private BaseUploadPresenter baseUploadPresenter;

    public BaseUploadModel(BaseUploadPresenter baseUploadPresenter){
        this.baseUploadPresenter = baseUploadPresenter;
    }

    @Override
    public void sendRequestToServer() {

        postUploadRequest = new PostUploadRequest(baseUploadPresenter.getParams()
                , ServerManager.getServerUrl(apiInterface)
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                baseUploadPresenter.accessSucceed(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                baseUploadPresenter.volleyError(VOLLEY_ERROR
                        ,VOLLEY_ERROR_DESC,apiInterface);
            }
        });
        SingletonRequestQueue.getInstance(MyApp.getInstance()).addRequestQueue(postUploadRequest);
    }



    @Override
    public void setMethod(int method ){
        // TODO: 上传model只能以post方式提交,所以该方法不做任何处理
    }

    @Override
    public void setApiInterface(String apiInterface){
        this.apiInterface = apiInterface;
    }

    @Override
    public void setServerAddress(String address) {
        this.address = address;
    }

    @Override
    public void cancelRequest() {
        if (postUploadRequest != null){
            postUploadRequest.cancel();
        }

    }
}
