# movie-seeker
Lucene search engine based on Douban movie data. :beers:

## 一、demo地址
[https://search.andrewpqc.xyz/service/](https://search.andrewpqc.xyz/service/)


## 二、技术点总结
- python网络爬虫
- mysql(数据存储)
- lucene(全文搜索引擎)
- java WEB
- tomcat服务器
- docker(应用环境封装)
- nginx(反向代理)
- let’s encrypt(https证书)

## 三、源码介绍
+ 目录树如下图

![目录树](/images/screenshot.png)







根目录下的lib目录内的内容为代码所依赖的jar包
根目录下的out目录下的内容为编译输出的内容，可以将其打包并发布
src为源代码目录，也是实现功能的主要目录：
indexer下面的LuceneIndex.java：利用Lucene建立索引的demo
retrieval下面的LuceneRetrieval.java：利用Lucene进行检索的demo
main下面的文件为应用程序使用的核心文件，SearchUtil.java将Lucene的索引建立与检索组织成了函数，以供jsp中直接调用，主要包括索引建立，单字段查询，多字段联合查询，模糊查询，基于范围的查询等。(具体的代码中包含了大量注释，可查看代码获取使用方法)，SearchTest.java是对应的测试。其他的tester,other目录下的内容为本人开发过程中进行小测试时书写的代码，与app功能的实现并无直接关系，可以不用管他们。
web目录下的内容为jsp页面
index.jsp:首页页面，主要是一些表单，收集用户的查询关键字以及查询逻辑
main_single.jsp：单字段查询请求的处理页面
mian_mult.jsp：多字段查询请求的处理页面
nobodyknows.jsp：索引建立页面，只在服务开启的时候访问一次，不能对外部开放

关于数据库的说明:
上面已经给出了数据库的服务器ip,所暴露的端口，用户名和密码等。注：searchenginedbuser用户只有Test数据库的movieinfo表的读的权限，无法插入删除更改数据。

镜像用法说明:
安装docker,运行下面的两条命令:
``` bash
docker pull  pqcsdockerhub/searchengine-image
docker run -d -p 8081:8080 pqcsdockerhub/searchengine-image
```
然后打开浏览器，访问https://localhost:8081/service/即可访问到封装好的服务。注：服务如果需要正常运行，请先访问http://localhost:8081/service/nobodyknows.jsp,访问此url,让程序在后台连接数据库，并且进行索引建立的工作，之后搜索服务即可正常运行。
