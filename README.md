# bottomBar
### 当前版本 ` 1.1 `  
app底部导航栏  
### 使用:  


- 添加依赖  
 1.项目gradle添加一下配置:  

		allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
			}
		}  

	2.module中的gradle添加依赖:

		dependencies {
	        implementation 'com.github.truemi:bottomBar:V1.1'
		} 
  
- xml中添加view:  
		
		<com.truemi.mbottombar.BottomBar
        	android:id="@+id/bottom_bar"
        	android:layout_width="match_parent"
        	android:layout_height="48dp"
        	android:layout_alignParentBottom="true"
        	android:background="#FFFFFF"
        	android:gravity="center"
        	app:mTextNormalColor="#999999"
        	app:mTextSelectColor="@color/colorAccent"
        	app:mtextSize="10dp">
		</com.truemi.mbottombar.BottomBar>  
- activity中初始化:
   
		BottomBar bottomBar = findViewById(R.id.bottom_bar);
		bottomBar.init(getSupportFragmentManager(),R.id.fl_home)//.setRippleColor(R.drawable.bottom_bar_bg)//水波纹效果
                .addItem("消息", getResources().getDrawable(R.drawable.bottom_bar_selected_01), new MessageFragment(), true)
                .addItem("首页", getResources().getDrawable(R.drawable.bottom_bar_selected_02), new HomeFragment(), false)
                .addItem("我的", getResources().getDrawable(R.drawable.bottom_bar_selected_03), new MyFragment(), true)
                .create(1);//默认显示第几个界面从0开始
        bottomBar.setOnBottomBarOnClick(this);//设置点击监听  

### 自定义属性:  

|  属性        | 值 |  描述  |
| --------   | -----   | ---- |
| mTextNormalColor        | #999999      |   标题的默认(未选中状态)颜色   |
| mTextSelectColor        | #FF0000     |   标题选中状态颜色    |
| mtextSize     | 10dp     |   标题的文字大小    |
| mIconSize     | 20dp     |   图片的大小(不设置,使用图片的实际大小)     |

### 公开方法:  

- setOnBottomBarOnClick(this);//设置点击监听
- showRedPoint(1,0,0);//只有初始化时设置可显示小红点为true时,设置显示才有效
- showRed(2);//显示小红点,从0开始,从左到右
- hideRed(0);//隐藏小红点,从0开始,从左到右
- showSubPage(1);//显示第几个界面,从0开始,从左到右