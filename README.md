# mini-mybatis
该项目是mybatis的mini版本，包含动态SQL，参数绑定，结果集处理，以及插件支持等核心功能，写mini-mybatis的初衷是想参照现有mybatis的功能，自己从0到1实现一个简化版，同时也希望帮助其他人熟悉mybatis的设计思想以及源码理解，为降低代码复杂度，该项目主要以实现核心功能为主，不会太注重性能以及线程安全等问题。该项目采用一步一步（step-by-step）的方式完善功能，每一个小功能模块使用一个独立的分支，分支前缀带有递增序号，序号由小到大表示功能的完善程度。
# 功能模块
[1.注册Mapper接口](https://github.com/FuriousPws002/mini-mybatis/wiki/1.%E6%B3%A8%E5%86%8CMapper%E6%8E%A5%E5%8F%A3 "Markdown") <br>
[2.解析xml中静态sql的mapper](https://github.com/FuriousPws002/mini-mybatis/wiki/2.%E8%A7%A3%E6%9E%90xml%E4%B8%AD%E9%9D%99%E6%80%81sql%E7%9A%84mapper "Markdown") <br>