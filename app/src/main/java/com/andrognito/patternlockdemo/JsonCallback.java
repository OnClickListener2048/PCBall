package com.andrognito.patternlockdemo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by edz on 2018/2/12.
 */

public abstract class JsonCallback <T> extends AbsCallback<T> {
    String TAG = JsonCallback.class.getSimpleName();
    private Type type;
    private Class<T> clazz;

    protected JsonCallback(Type type) {
        this.type = type;
    }

    protected JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
    }




    @Override
    public void onFinish() {
        super.onFinish();
    }

    @Override
    public T convertResponse(Response response) throws Throwable {
        ResponseBody body = response.body();
        if (body == null) {
            return null;
        }

        T data = null;
        Gson gson = new GsonBuilder().serializeNulls().create();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (type != null) {
            data = gson.fromJson(jsonReader, type);
        }
        if (clazz != null) {
            data = gson.fromJson(jsonReader, clazz);
        }
        return data;
    }
}
