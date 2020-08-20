# knife

简单，实用的Java工具类，目前包含以下工具:
 
|    工具模块       |      描述         |
| :---:            |     :----:       |
| BeanUtils        | 提供对象与对象之间的转换功能,并且能根据指定的对象，创建目标对象，把指定的对象的属性值赋值目标对象。|
| RSA              | RSA加密算法                  |

项目依赖:
maven:
````text
 <groupId>com.github.guang19</groupId>
 <artifactId>knife</artifactId>
 <version>3.2.0</version>
````

### BeanUtils

BeanUtils应该是各位同学经常使用的工具，我也对比各个BeanUtils的性能，采用了cglib的方式来实现。

BeanUtils的使用Demo见: [测试](https://github.com/guang19/knife/blob/master/src/test/java/com/github/guang19/knife/BeanUtilTest.java)

普通对象的属性拷贝:

````text
Person1 person1 = new Person1();
person1.setId(1L);
person1.setAge(19);
person1.setName("yxg");
person1.setInner(new Inner());

Person2 person2 = new Person2();

BeanUtils.copy(person1,person2);c
````

根据已有对象，创建目标类型的对象:

````text
 Person1 person1 = new Person1();
 person1.setId(1L);
 person1.setAge(19);
 person1.setName("yxg");

 BeanUtils.createNewTypeObj(person1,Person2.class)
````

根据已有对象集合，创建目标类型的对象集合:

````text
List<Person1> person1List = new ArrayList<>();
person1List.add(person1);
person1List.add(person2);

List<Person2> person2List = BeanUtils.createNewTypeCollection(person1List, Person2.class);
````

且BeanUtils还支持bean字段值的类型转换,只需要写一个函数式接口:BeanFieldValTypeConverter 就行了。

假设有以下2个类: Person1 和 Person2

````text
public class Inner{}
     
public class Person1
{
   //wrapper type
    private Long id;
     
    private String name;
     
    //wrapper type
    private Integer age;
     
    private Inner inner;
     
    //setter
    ...
}
     
public class Person2
{
    //primitive type
    private long id;
     
    private String name;
     
    //primitive type
    private int age;
     
    //type different Inner
    private String inner;
     
    //setter
    ...
}
````

普通的BeanUtils是这样的：

````text
  Person1 person1 = new Person1(1L,"yxg",19,new Inner);
  Person2 person2 = new Person2();
 
  BeanUtil.copy(person1,person2);
    
 //结果就是:
 person2{id:0L,name:"yxg",age:0,inner:null};
````

以上这段代码在普通的BeanUtil中，只有name属性能够被正确的拷贝，
其他属性因为类型不同，不会被拷贝成功。
而BeanFieldValTypeConverter接口就是为了解决这个问题而生的，你可以定义你想要转换的属性的值。

`````text
  BeanFieldValTypeConverter converter = fieldVal ->
  {
      //如果当前拷贝的属性值是 age 属性
      if(fieldVal instanceof Integer)
      {
          return (Integer)field;
      }
      //如果当前拷贝的属性值是 id 属性
      if(fieldVal instanceof Long)
      {
          return (Long) fieldVal;
      }
      //如果当前拷贝的属性值是 Inner 属性
      if(fieldVal instanceof Inner)
      {
          return fieldVal.toString();
      }
      return fieldVal;
  }

//执行copy方法时指定converter就行了
BeanUtil.copy(person1,person2,converter);

//结果如下:
person2{id:1L,name:"yxg",age:19, inner::toString()};
`````


### RSA

关于RSA相关知识可以阅读阮一峰前辈的文章： [RSA算法原理](http://www.ruanyifeng.com/blog/2013/06/rsa_algorithm_part_one.html)

使用:

````text
//创建KeySize为1024的RSA加密解密工具
RSA rsa = RSA.keySize1024();

//生成公匙和私匙
RSAKeyPair rsaKeyPair = rsa.generateKeyPair();

//对 "guang19" 字符串进行加密
String encrypt = rsa.encrypt("guang19", rsaKeyPair.getPublicKey());
System.out.println(encrypt);

//对加密后的密文进行解密
String decrypt = rsa.decrypt(encrypt, rsaKeyPair.getPrivateKey());
System.out.println(decrypt);

//生成签名
String sign = rsa.sign(encrypt, rsaKeyPair.getPrivateKey());
System.out.println(sign);

//检验签名
boolean checkSign = rsa.checkSign(encrypt,sign,rsaKeyPair.getPublicKey());
System.out.println(checkSign);
````

在这里说一下RSA如何作用于注册功能吧：

1. 用户进入注册页面后，向后端请求公匙；
2. 后端使用RSA生成RSAKeyPair密匙对，并将密匙对存入NoSQL数据库；
3. 前端收到公匙后，用户提交用户名（邮箱）和密码（加密后的密文）等信息；
4. 后端收到用户提交的信息后，使用私匙解密密码，校验信息成功后，使用其他加密算法（如argon2）对密码进行加密，
最后将用户信息存入数据库，最后响应Cookie或Jwt。