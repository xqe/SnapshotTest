```
{
    "type": "event_binding_request",
    "payload": {
        "events": 
		[
            {
                "trigger_id": 65533,
                "event_type": "click",
                "event_name": "btn_test_click",
                "deployed": false,
                "target_activity": "com.sensorsdata.analytics.android.clockdemo.Main2Activity",
                "path": [
                    {
                        "prefix": null,
                        "view_class": "com.android.internal.policy.PhoneWindow.DecorView",
                        "index": "-1",
                        "id": "-1",
                        "sa_id_name": null
                    },
                    {
                        "prefix": "shortest",
                        "view_class": "com.android.internal.widget.ActionBarOverlayLayout",
                        "index": "0",
                        "id": "16909220",
                        "sa_id_name": null
                    },
                    {
                        "prefix": "shortest",
                        "view_class": "android.widget.FrameLayout",
                        "index": "0",
                        "id": "16908290",
                        "sa_id_name": "android:content"
                    },
                    {
                        "prefix": "shortest",
                        "view_class": "android.widget.LinearLayout",
                        "index": "0",
                        "id": "-1",
                        "sa_id_name": null
                    },
                    {
                        "prefix": "shortest",
                        "view_class": "android.widget.LinearLayou",
                        "index": "0",
                        "id": "2131558506",
                        "sa_id_name": "btn"
                    }
                ]
            }
        ]
    }
}
```


                    mEditorEventBindings.put(event.get("path").toString(), new MPPair<String, JSONObject>(targetActivity, event));
					
					
					