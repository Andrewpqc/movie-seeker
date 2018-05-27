# movie-seeker
Lucene search engine based on Douban movie data. :beers:

## 一、demo地址
[https://search.andrewpqc.xyz/service/](https://search.andrewpqc.xyz/service/)


## 二、技术点总结
- python网络爬虫
- mysql(数据存储)
- lucene(全文搜索引擎)
- java WEB/JSP
- tomcat服务器
- docker(应用环境封装)
- nginx(反向代理)
- let’s encrypt(https证书)

## 三、源码介绍
目录树如下图

![目录树](/images/screenshot.png)


1.根目录下的`lib`目录内的内容为代码所依赖的jar包

2.根目录下的`out`目录下的内容为编译输出的内容，可以将其打包并发布

3.根目录下`src` 为源代码目录，也是实现功能的主要目录,下面是对该目录下的包的介绍：
- `indexer`包下的`LuceneIndex.java`：利用`Lucene`创建索引的demo
- `retrieval`包下的`LuceneRetrieval.java`：利用`Lucene`进行检索的demo
- `main`包是应用程序使用的核心文件，`SearchUtil.java`将`Lucene`的索引创建与各种类型的检索组织成了类成员函数，以在JSP文件中直接调用。主要包括**索引建立，单字段查询，多字段联合查询，模糊查询，基于范围的查询等**。`SearchTest.java`是上述函数的测试文件。

- 其他的`tester`,`other`包下的内容为本人开发过程中进行小测试时书写的代码，与app功能的实现并无直接关系，不可不必理会。

4.根目录下web目录下的内容为JSP页面
- index.jsp:首页页面，主要是一些表单，收集用户的查询关键字以及查询逻辑
- main_single.jsp：单字段查询请求的处理页面
- mian_mult.jsp：多字段查询请求的处理页面
- nobodyknows.jsp：索引建立页面，只在服务开启的时候访问一次，不对外部开放

## 四、关于数据源
此搜索引擎基于之前爬取的**8000**余条豆瓣电影的数据,下面是数据库结构和单条数据模板的介绍表格:

| 字段 | 描述 | 样例数据 |
| - | :-: | :--: |
| name | 影片名(含中文与外文) | 肖申克的救赎 The Shawshank Redemption |
| Screenwriter | 编剧 | 弗兰克·德拉邦特,斯蒂芬·金 |
| actor | 主演 | 蒂姆·罗宾斯,摩根·弗里曼,鲍勃·冈顿,威廉姆·赛德勒,克兰西·布朗,吉尔·贝罗斯,马克·罗斯顿,詹姆斯·惠特摩,杰弗里·德曼,拉里·布兰登伯格,尼尔·吉恩托利,布赖恩·利比,大卫·普罗瓦尔,约瑟夫·劳格诺,祖德·塞克利拉|
| type |  类型 | 剧情，犯罪　|
| country | 国家　| 美国　|
| displaytime| 上映时间 |1994-09-10 ,多伦多电影节　|
| score | 豆瓣评分　| 9.6 |
| othername |　别名　|　月黑高飞(港)，刺激1995(台)，地狱诺言，铁窗岁月，消香克的救赎　|
| shortcut | 影片简介 | 20世纪40年代末，小有成就的青年银行家安迪（蒂姆·罗宾斯 Tim Robbins 饰）因涉嫌杀害妻子及她的情人而锒铛入狱。在这座名为肖申克的监狱内，希望似乎虚无缥缈，终身监禁的惩罚无疑注定了安迪接下来灰暗绝望的人生。未过多久，安迪尝试接近囚犯中颇有声望的瑞德（摩根·弗里曼 Morgan Freeman 饰），请求对方帮自己搞来小锤子。以此为契机，二人逐渐熟稔，安迪也仿佛在鱼龙混杂、罪恶横生、黑白混淆的牢狱中找到属于自己的求生之道。他利用自身的专业知识，帮助监狱管理层逃税、洗黑钱，同时凭借与瑞德的交往在犯人中间也渐渐受到礼遇。表面看来，他已如瑞德那样对那堵高墙从憎恨转变为处之泰然，但是对自由的渴望仍促使他朝着心中的希望和目标前进。而关于其罪行的真相，似乎更使这一切朝前推进了一步……本片根据著名作家斯蒂芬·金（Stephen Edwin King）的...　|
| url | 当前影片对应的豆瓣详情页url |  https://movie.douban.com/subject/1292052/  |


## 五、镜像用法
安装docker,运行下面的两条命令:
``` bash
docker pull  pqcsdockerhub/searchengine-image
docker run -d -p 8081:8080 pqcsdockerhub/searchengine-image
```
然后打开浏览器，访问｀https://localhost:8081/service/｀即可访问到封装好的服务。注：服务如果需要正常运行，请先访问｀http://localhost:8081/service/nobodyknows.jsp｀,访问此url,让程序在后台连接数据库，并且进行索引建立的工作，之后搜索服务即可正常运行。
