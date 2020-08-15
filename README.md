# knife

简单，实用的Java工具类，目前包含以下工具库:
 
|    工具模块       |      描述         |
| :---:            |     :----:       |
| BeanUtils        | 提供对象与对象之间的转换功能,并且能根据指定的对象，创建目标对象，把指定的对象的属性值赋值目标对象。|
| ~~IdGenerator~~  | ~~id生成器。目前有64位雪花ID生成器和54位雪花Id生成器实现，54位雪花Id生成器是为了兼容前端JS的最大安全值。~~ |

PS: 个人认为雪花ID不应该作为工具，而是作为一项服务。所以我将以RPC的方式改造美团Leaf，这项服务将会
被我融合进我的项目：[service](https://github.com/guang19/service) 中。

项目依赖:
maven:
````text
 <groupId>com.github.guang19</groupId>
 <artifactId>knife</artifactId>
 <version>3.1.0</version>
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

### ~~IdGenerator~~

ID生成器。相信各位同学或多或少都了解一点关于唯一ID的生成策略，我这里就简单介绍一下吧:
- 自增ID: 适用于单机或少量集群的应用环境，如果数据库实例过多，那么ID将会重复。

- UUID : 虽然UUID可以在很大程度上保证ID的唯一性，但是数据库索引大多为B+树结构，最好要求主键索引的值
是有序且容易排序，否则将会影响查询和修改索引的效率。所以UUID并不适用。

- 雪花ID: 推特开源的一种分布式唯一ID解决方案，支持64位(2^63 - 1)(可以魔改)的最大值。雪花ID由时间戳,工作机器ID，自增序列3部分组成。
雪花ID的优点很多，我写的IDGenerator的实现也是根据它的方案而来的。但是雪花ID有一个缺点，就是它生成ID需要依赖系统时钟。
即系统时钟不能回拨，所以便有了美团开源的Leaf。

- [Leaf](https://github.com/Meituan-Dianping/Leaf):Leaf是美团开源的分布式ID解决方案。他提供了号段模式和雪花ID 这2种模式来
进行ID的生成。且它解决了雪花ID系统时钟回拨问题，但需要依赖于Zookeeper强一致性的中间件。

以上就是一部分的ID解决方案了，它们各有优缺点，看各位同学如何选择。
但无论如何选择，我想有同学应该了解一点JS的"特性"，JS最大支持的安全值类型(Number Safe Value)为2^53-1,
这就导致即使我们生成了有序的唯一数字类型的ID，当与前端交互时，不得不转为String，这很让我伤脑筋。
于是我便自己写了54(最大值为2^53-1)版本的雪花ID。

为什么64位的雪花ID可以被改造成54位的雪花ID，当然需要付出一些代价了。
64位的雪花ID的时间戳部分为41位(可以改)，就意味着64位的雪花ID可以使用:
````text
((1L << 41) / (1000L * 60 * 60 * 24 * 365)) = 69年
````