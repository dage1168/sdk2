#使用方法：
将nativesdk.aar 放入libs文件夹
build.gradle引入aar文件

```
repositories {
    flatDir {
        dirs 'libs'
    }
}

implementation(name:'nativesdk-debug', ext:'aar')

```

##1、自己处理回调跳转逻辑

```
ApiUtils.requestHostConfig(this, (isSuccess, openConfig) -> {
            if(isSuccess && openConfig != null){
                String isOpen = openConfig.getIsOpen();
                if (isOpen.equals("2")) {
                    // FIXME: 2022/6/16 展示B页面逻辑
                }else {
                    String urlA = openConfig.getUrlA();
                    Log.i("sdk","url:" + urlA);
                    // FIXME: 2022/6/16 展示A页面逻辑
                }
            }else {
                Log.i("sdk","请求失败");
            }
        });
```

##2、由sdk处理跳转逻辑 （请求结果为A页面自动在当前activity 添加WebView展示A页面， 如果为B页面自动重新打开当前Activity 走app正常逻辑）
建议在启动页onCreate 增加一下代码

```
    if(ApiUtils.sUrlConfig == null) {
        ApiUtils.requestHostConfig(this);
        return;
    }

例如：
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ApiUtils.sUrlConfig == null) {
            ApiUtils.requestHostConfig(this);
            return;
        }

        Toast.makeText(this, "进入原生页面", Toast.LENGTH_LONG).show();
    }
```
