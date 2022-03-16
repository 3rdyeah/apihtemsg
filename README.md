# APIHTEPROTO

## 目录、文件说明

- protocol - 配置文件目录

- protocol/xmsg - 逻辑协议定义文件目录

- protocol/genmsg.bat - 编译 xml 文件，生成逻辑需要的 java 协议文件，从 protocol/xmsg 读取，输出到自定义目标目录（具体逻辑查看 bat 文件内容）

## 逻辑协议的定义

逻辑协议文件在 xmsg 文件夹的 xml 文件中定义，根文件为默认 root.xml，可以定义其他文件，使用 include 在 root.xml 中引入

### xml 文件的引用关系

例如 root.xml 的内容可以是这样：

```xml
<?xml version="1.0" encoding="utf-8"?>
<application name="apihte" xmlns:xi="http://www.w3.org/2001/XInclude">
	<namespace name="apihte.logic.protocol">
		<xi:include href="example.xml"/>
	</namespace>
</application>
```

上面的 root.xml 文件中包含一个 example.xml 文件，它可以是这样的

```xml
<?xml version="1.0" encoding="utf-8"?>
<namespace name="apihte.logic.example"> <!-- 201~300 -->
	<msg name="CSendContext" id="201">
		<var name="id" type="int"/>
		<var name="context" type="string"/>
		<var name="ismap" type="map" key="int" value="string"/>
		<var name="ibeanmap" type="map" key="int" value="apihte.logic.example.TestBean"/>
		<var name="testbytes" type="bytes"/>
		<var name="beanllist" type="llist" value="apihte.logic.example.TestBean"/>
		<var name="longalist" type="alist" value="long"/>
		<var name="shortset" type="set" value="short"/>
	</msg>

	<msg name="SSendContext" id="202">
		<var name="succ" type="boolean"/>
	</msg>

	<bean name="TestBean">
		<var name="id" type="int"/>
		<var name="value" type="byte"/>
	</bean>
</namespace>
```

上面的 xml 配置文件可以生成 3 个 Java 类，分别是：

- apihte.logic.example.CSendContext
- apihte.logic.example.SSendContext
- apihte.logic.example.TestBean

### 使用 xml 文件定义协议类的关键字

- namespace 定义包，name 属性定义包名

- bean 定义普通类，name 属性定义类名

- msg 定义协议类，name 属性定义协议类名，id 属性定义协议 id

- var 定义协议字段，name 属性定义字段名，type 属性定义类型，根据不同的字段类型又有以下不同属性定义

    - type 为 bytes，生成 bytes[]

    - type 为 alist，生成 ArrayList，value 定义元素类型

    - type 为 llist，生成 LinkedList，value 定义元素类型

    - type 为 set，生成 Set，value 定义元素类型

    - type 为 map，生成 Map，key 定义 key 类型，value 定义元素类型

## 如何通过协议文件实现逻辑

协议逻辑的实现应该协议类的 process() 方法中，如果逻辑需要 import，则需要将 import 的内容写道 EXT IMPORT 区域中，简单来说就是遵守下面两条约定：

- 逻辑内容写在 // YOU CAN ADD CODE AFTER THIS ONLY 和 // YOU CAN ADD CODE BEFORE THIS ONLY 中间

- import 内容写在 // EXT IMPORT BEGIN 和 // EXT IMPORT END 中间

> 除以上两个位置外，其他地方的代码会在重新生成协议的时候被覆盖

## Jar 文件的使用

示例：

假设文件路径如下：

```
xmldir---|
         |---root.xml
         |---example.xml
```

使用的命令行如下：

```shell
java -cp apihtecore.jar apihte.util.gen.MsgGen -root "./xmldir/root.xml" -target "../src/test/java"
```

则会从 "./xmldir/root.xml" 目录读取 xml 文件，最后输出 java 文件到 "../src/test/java" 目录

## 协议的编解码

自动生成的协议文件会包含 encode、_encode、decode、_decode 几个方法

encode 为编码方法，会将消息 id 写到最头部。区别于 encode 方法，_encode 方法只写入类的属性，不会在头部写入消息 id 

decode 为解码方法，会先从消息头部取一个 int 类型来获取消息 id。同样，区别于 decode 方法，_decode 方法只解码类的属性，不会在头部取消息 id

没有特殊需求的话，只关注 encode 和 decode 两个方法即可

相对于编码可以直接使用 encode 方法，解码时由于需要一个消息 id 到消息类的映射，这样才能在解码到消息 id 时正确的解码对应的消息，这里提供了一个解决方案

- 程序启动时构造一个 apihteproto.io.ProtoReflector 对象，将生成协议的输出目录作为参数传递给 ProtoReflector 对象的 init 方法

- 接到消息的 bytes 时，将 bytes 传递给 ProtoReflector 对象的 create 方法，则会解码到正确的消息类