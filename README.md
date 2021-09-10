# Android Architecture

[![](https://jitpack.io/v/ithomasoft/frame.svg)](https://jitpack.io/#ithomasoft/frame)

Android 快速开发框架，融合了mvvm、mvp架构（可二选一）。
包含了Hilt、Room、Retrofit、LiveData、Lifecycle、DataBinding、AndroidX等主流组件。
支持组件化开发。
使用java语言开发，避免再造轮子。
新老项目皆可使用。

## 摘要

  - [怎么使用](#怎么使用)
  - [更新日志](#更新日志)
  - [关于作者](#关于作者)
  - [致谢](#致谢)

## 怎么使用

1. 在项目根目录的`build.gradle`文件中添加如下代码：
        
         
    	    allprojects {
    		    repositories {
    			    ...
    			    maven { url 'https://jitpack.io' }
    		    }
    	    }  
    	
    	
2.  添加项目依赖：
        
        
            dependencies {
                implementation 'com.github.ithomasoft:frame:1.0.0'
            } 
        
            
3.   开始使用！

## 更新日志

 - 1.0.0：初始版本

## 关于作者

渣渣一枚

## 致谢

[AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
[MvvmFrame](https://github.com/jenly1314/MVVMFrame)
[MvpArms](https://github.com/JessYanCoding/MVPArms)