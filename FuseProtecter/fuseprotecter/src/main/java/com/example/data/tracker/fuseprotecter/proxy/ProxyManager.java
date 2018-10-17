package com.example.data.tracker.fuseprotecter.proxy;

import android.util.Log;

import com.example.data.tracker.fuseprotecter.sdkAPI.API;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyManager {

    public API proxy(API target){
        MyInvocationHandler invocationHandler = new MyInvocationHandler(target);
        return (API) Proxy.newProxyInstance(API.class.getClassLoader(),new Class[]{API.class},invocationHandler);
    }

    private class MyInvocationHandler implements InvocationHandler {
        private static final String TAG = "MyInvocationHandler";
        private Object target;

        private MyInvocationHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            Object result = method.invoke(target,objects);
            //count
            Log.i(TAG, "invoke: count");
            return result;
        }
    }
}
