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
		<var name="testmap" type="map" key="int" value="string"/>
		<var name="testbytes" type="bytes"/>
		<var name="testllist" type="llist" value="apihte.logic.example.TestBean"/>
		<var name="testalist" type="alist" value="long"/>
		<var name="testset" type="set" value="short"/>
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
