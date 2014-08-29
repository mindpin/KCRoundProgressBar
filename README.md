Android KCRoundProgressBar
============
KCRoundProgressBar 圆形进度条组件
![](http://mindpin.oss-cn-hangzhou.aliyuncs.com/image_service/images/VshqiG4h/VshqiG4h.png)

如何引用此组件：
-------------
** 安装 **

```
git clone https://github.com/mindpin/KCRoundProgressBar
cd KCRoundProgressBar
mvn clean install
```

** maven引用 **

在maven项目，pom.xml添加以下依赖引用：

```
<dependency>
    <groupId>com.mindpin.android.kcroundprogressbar</groupId>
    <artifactId>kcroundprogressbar</artifactId>
    <version>0.1.1</version>
    <type>apklib</type>
</dependency>
```

** android权限设置 **
无

组件的调用API
-----------------------
样式相关的API(参照图片中的详细说明来看)
```java
// 设置前景色，默认黑色
// 参数中的 int 值用下面的这个方法获得
// int color = Color.parseColor("#636161")
public void set_fg_color(int color)
// 设置背景色，默认透明
public void set_bg_color(int color)
// 设置是否显示进度条组件中间的数字，默认不显示
public void set_text_display(boolean flag)
// 设置组件宽度，默认值为空，不设置宽度，组件无法初始化
public void set_width_px(int px)
public void set_width_dp(int dp)
// 设置环形厚度（环形厚度和组件宽度一半的比例值），默认0.3
public void set_thickness(float scale)
// 设置是否显示外圈，默认不显示
public void set_border_display(boolean flag)
// 设置中间的数字字体大小，默认是组件宽度的三分之一
public void set_text_size(int dp)
// 设置起始角度，默认0
public void set_start_angle(int angle)
```

进度相关的API
```java
// 设置进度最小值，默认是0
public void set_min(int num)
// 设置进度最大值，默认是100
public void set_max(int num)
// 设置进度当前值（该值会显示在进度条组件中间）
// 运行这个方法后组件的数字直接变成这个值
// 环形显示的角度直接变成 (current-min) / (max-min) * 360
public void set_current(int num)
// 设置进度当前值（该值会显示在进度条组件中间）
// 运行这个方法后组件的数字从正在显示的值平滑渐变的变化到这个值（不能出现小数）
// 环形显示的角度从正在显示的值平滑渐变的变成 (current-min) / (max-min) * 360
public void set_current_smooth(int num)
```

*********************

使用说明
---------------------
请参考示例


参考
---------------------
* [destinyd/android-archetypes][android-archetypes]
* [Todd-Davies/ProgressWheel][ProgressWheel]


[android-archetypes]: https://github.com/destinyd/android-archetypes
[ProgressWheel]: https://github.com/Todd-Davies/ProgressWheel
