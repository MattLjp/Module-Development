# Module-Development
# 一、为什么要模块化开发?
如果App代码量不大，业务量比较少，这时候没有必要进行模块化开发。但是如果App业务较多的时候，代码复杂，每个模块之间的代码耦合严重，结构臃肿，每个开发人员开发部分代码时会影响其他开发人员的模块，修改一处代码要编译整个工程，效率较低，这时候最好的办法就是进行模块化拆分。不同的人可以独立负责不同的模块，相互不影响。

# 二、项目架构设计
项目架构设计的思路主要分为主App、公共基础模块、业务处理模块
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210113105723316.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0NjgyOQ==,size_16,color_FFFFFF,t_70)

**主App：** 一个空壳app，本身不实现任何业务逻辑，主要用于将所有的模块打包成完整的release APK。
**公共基础模块：** 包含着各个模块都公用的基础组件，例如网络通信模块、事件通信模块、工具类等。
**业务处理模块：** 由各个开发人员负责自己开发的模块，尽量使不同的的业务模块相互不影响。


# 三、代码实现方案
在项目架构设计完成之后，每个开发人员单独负责自己开发的模块，这时候需要module能单独作为Application编译，在module开发完成之后验证没问题后加入到主App中。

## 3.1增加release和debug模式切换Flag
在gradle.properties文件中增加一个变量，当isDebug为true时，为Debug模式，其它的Module可以作为单独的App运行。当isDebug为false时，为Release模式，其它的Module为Library模式，不能单独运行,此时只有主App可以运行。

```bash
#是否处于Debug模式
isDebug = false
```

## 3.2 修改各个Module的build.gradle文件
在开发时，切换成application；在发布时，切换成library。
```bash
if (isDebug) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}   

```
## 3.3 修改app的build.gradle文件
在Release模式下，导入所有的组件
```bash
dependencies {
	...
	//公共基础模块
	implementation project(':common')
	if (!isDebug) {
    	implementation project(':moduleA')
    	implementation project(':moduleB')
    	implementation project(':moduleC')
    	implementation project(':moduleMain')
	}
}
```
## 3.4 配置不同的AndroidManifest.xml
因为module需要在library和application之间切换，所以需要配置两套`AndroidManifest.xml`
```bash
sourceSets {
    main {
        if (isDebug) {
            manifest.srcFile 'src/main/debug/AndroidManifest.xml'
        } else {
            manifest.srcFile 'src/main/AndroidManifest.xml'
            java { exclude 'debug/**' }
        }
    }
}
```

## 3.5 统一管理Module版本号
1、为便于统一管理版本号，在项目的根目录下的build.gradle文件中增加统一的版本号

```bash
ext {
    compileSdkVersion = 29

    minSdkVersion = 21
    targetSdkVersion = 29
    versionCode = 1
    versionName = "1.0.0"
}
```
2、在所有的Module和application的build.gradle文件下修改

```bash
android {
    compileSdkVersion rootProject.ext.compileSdkVersion
    buildToolsVersion rootProject.ext.buildToolsVersion
     
    defaultConfig {
        minSdkVersion rootProject.ext.minSdkVersion 
        targetSdkVersion rootProject.ext.targetSdkVersion 
        versionCode rootProject.ext.versionCode 
        versionName rootProject.ext.versionName 
		...
	}
	
	...
}
        
```



# 四、ARouter框架
在完成module开发之后，接下来就是要将所有的module关联使用了。这时候需要解决以下几个问题：
- 模块间页面跳转；
- 模块间事件通信；
- 模块间服务调用；
- 模块的独立运行；
- 模块间页面跳转路由拦截（如登录）

对于上面的问题，都可以使用ARouter进行解决。


## 4.1 添加依赖和配置

```groovy
android {
    defaultConfig {
        ...
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [AROUTER_MODULE_NAME: project.getName()]
            }
        }
    }
}

dependencies {
    // 要与compiler匹配使用，均使用最新版可以保证兼容
    compile 'com.alibaba:arouter-api:1.5.1'
    annotationProcessor 'com.alibaba:arouter-compiler:1.5.1'
    ...
}
```

## 4.2 初始化SDK

```java
if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
    ARouter.openLog();     // 打印日志
    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
}
ARouter.init(mApplication); // 尽可能早，推荐在Application中初始化
```

## 4.3 添加注解

```java
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = "/test/activity")
public class YourActivity extend Activity {
    ...
}

```
## 4.4 发起路由操作

```java
// 1. 应用内简单的跳转
ARouter.getInstance().build("/test/activity").navigation();

// 2. 跳转并携带参数
ARouter.getInstance().build("/test/activity")
            .withString("name", "Jack")
            .withInt("age", 23)
            .withSerializable("key3", new Test("David", 25))
            .navigation();
```
## 4.5 解析参数
```java
// 在支持路由的页面上添加注解(必选)
// 这里的路径需要注意的是至少需要有两级，/xx/xx
@Route(path = "/test/activity")
public class YourActivity extend Activity {
	// 为每一个参数声明一个字段，并使用 @Autowired 标注
    @Autowired
    String name;
    
	// 通过name来映射URL中的不同参数
	@Autowired(name = "age") 
    int key2;

	// 支持解析自定义对象，URL中使用json传递
	@Autowired
	Test key3;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    ARouter.getInstance().inject(this)
    }
}

```

```java
// 如果需要传递自定义对象，新建一个类（并非自定义对象类），然后实现 SerializationService
public class Test implements SerializationService {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

## 4.6 常见问题
### 1.kotlin项目的中的配置方式与java项目不一样

```groovy
apply plugin: 'kotlin-kapt'

kapt {
    arguments {
        arg("AROUTER_MODULE_NAME", project.getName())
    }
}

dependencies {
    compile 'com.alibaba:arouter-api:x.x.x'
    kapt 'com.alibaba:arouter-compiler:x.x.x'
    ...
}
```

### 2.路由跳转时报错`"W/ARouter::: ARouter::There is no route match the path [/xxx/xxx], in group [xxx][ ]"`

遇到这种情况是没有找到目标页面，如果页面存在可以可以按照下面的步骤进行排查

**a.** 检查目标页面的注解是否配置正确，正确的注解形式应该是 (@Route(path="/test/test"), 最少有两层且第一层前要有“/”
**b.** 检查目标页面所在的模块的gradle脚本中是否依赖了 arouter-compiler sdk，例如`annotationProcessor 'com.alibaba:arouter-compiler:1.5.1'` (需要注意的是，要使用apt依赖，而不是compile关键字依赖)

### 3.在kotlin项目中解析参数
在kotlin项目中除了需要添加注解`@Autowired`还需要添加注解`@JvmField`，如下所示

```kotlin
    @Autowired
    @JvmField
    var name: String = ""
```
`@JvmField`，消除了变量的getter与setter方法。在kotlin中，我们的属性最终经过编译都是private的，只提供了getter和setter。但是在某些需要使用到反射或者其他的情况下，我们需要属性是public的。这时候我们就用`@JvmField`去标注它，那么编译出来的属性就是public的。


关于ARouter更详细的使用参考[ARouter](https://github.com/alibaba/ARouter/blob/master/README_CN.md)
