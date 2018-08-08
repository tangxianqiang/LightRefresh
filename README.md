# LightRefresh
A view which can refresh and load more.It is easy to customizable(see [header](https://github.com/BLCodes/LightRefresh/tree/master/header)).It's a simple refresh and load more lib.
It supports damping rebound and multi-finger touch.It is a little bit difficult to use but more easier to to customize.
## function
* 1.only keep header or footer or none of them.
* 2.support most common views（RecyclerView、ListView、WebView、ScrollView and so on）.
* 3.support custom handling of event conflicts.
* 4.support custom refresh header, load more.
* 5.support Multi - finger touch , and there is no layout jitter in frequent pulling.
* 6.support auto refresh.
* 7.support fixed refresh layout
## How to use
1.use a framlayout to wrap the BounceLayout and header or footer,eg:
    
 ```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android" android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/fl_root"
    >
    <!--the header or footer will be added here-->
    <com.blcodes.views.refresh.BounceLayout
        android:id="@+id/bl"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#eee"
        >
        <android.support.v7.widget.RecyclerView
            android:id="@+id/rv_test"
            android:layout_width="match_parent"
            android:layout_height="match_parent"></android.support.v7.widget.RecyclerView>


    </com.blcodes.views.refresh.BounceLayout>
</FrameLayout> 
```
2.add header or footer and set the Boolean value to make the view not bounce:
```
bounceLayout.setDisallowBounce(true);
FrameLayout rootView = findViewById(R.id.fl_root);
bounceLayout.setHeaderView(new DefaultHeader(this),rootView);//if HeaderView is null,it just bounce.
bounceLayout.setFooterView(new DefaultFooter(this),rootView);
```
3.set scroll BounceHandler and EventForwardingHelper call back:
```
RecyclerView recyclerView = findViewById(R.id.rv_test);
bounceLayout.setBounceHandler(new NormalBounceHandler(),recyclerView);

bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
```
4.add refresh and load more call back:
```
bounceLayout.setBounceCallBack(new BounceCallBack() {
            @Override
            public void startRefresh() {

            }

            @Override
            public void startLoadingMore() {

            }
        });
```
5.set auto refresh or not:
```
bounceLayout.autoRefresh()
```
# LISENCE
```
MIT License

Copyright (c) 2018 BLCodes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
