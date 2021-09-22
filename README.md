# APIHTEPROTO

## 目录、文件说明

- protocol - 配置文件目录

- protocol/xmsg - 逻辑协议定义文件目录

- protocol/genmsg.bat - 编译 xml 文件，生成逻辑需要的 java 协议文件，从 protocol/xmsg 读取，输出到自定义目标目录（具体逻辑查看 bat 文件内容）

## 逻辑协议的定义

逻辑协议文件在 xmsg 文件夹的 xml 文件中定义，根文件为默认 root.xml，可以定义其他文件，使用 include 在 root.xml 中引入

- namespace 定义包，name 属性定义包名

- msg 定义协议，name 属性定义协议名，id 属性定义协议 id

- attr 定义协议字段，name 定义字段名，type 定义类型，根据不同的字段类型又有以下不同属性定义

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
