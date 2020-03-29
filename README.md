# knife

简单，实用的Java工具类，目前包含以下工具:
 
|    工具模块       |     接口/实现             |        描述       |
| :---:            |     :---:                |  :----:           |
| BeanUtils        |   BeanUtils              |    提供对象与对象之间的转换功能,并且能根据指定的对象，创建目标对象，把指定的对象的属性值赋值目标对象 。|
| IdGenerator      |   SnowflakeIdGenerator   |  id生成器。目前有64位雪花ID生成器和54位雪花Id生成器实现，54位雪花Id生成器是为了兼容前端JS的最大安全值。  |
| AssertUtils      |   AssertUtils            |  提供简单的判断函数，消除冗余的if语句。 |   
| MailUtils        |   MailSender             |  邮件工具，支持普通文本邮件，html邮件，附件邮件。 |
| ReflectionUtils  | ReflectionUtils          |  反射工具，包含了常用的反射方法。|

TODO:

- [ ] 正则工具

使用:

maven:
````text
 <groupId>com.github.guang19</groupId>
 <artifactId>knife</artifactId>
 <version>1.0.1</version>
````

