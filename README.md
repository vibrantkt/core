# Vibrant

[![Release](https://jitpack.io/v/vibrantkt/core.svg)](https://jitpack.io/#vibrantkt/core)
[![Build Status](https://travis-ci.org/vibrantkt/core.svg?branch=master)](https://travis-ci.org/vibrantkt/core)

![Logo](https://image.ibb.co/fSKk9R/vibrant2.png)

Hey there. Vibrant is a library for prototyping or building distributed apps. Core idea is to provide boilerplate code e.g. peer connection or blockchain synchronization, so developer may focus on logic. 


## Gradle 
1) Add jitpack repo 
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2) Add latest vibrant to dependencies(latest version is in jitpack badge at the top of this page)
```
dependencies {
    compile 'com.github.vibrantkt:core:%latest-version-in-badge%'
}
```