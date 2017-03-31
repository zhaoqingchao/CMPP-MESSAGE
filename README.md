# CMPP-MESSAGE
基于--中国移动通信互联网短信网关接口协议CMPP（V3.0.0） 的短信收发java实现


目  录
1	范围	1
2	引用标准	1
3	术语和定义	1
4	网络结构	3
5	CMPP功能概述	3
6	协议栈	4
7	通信方式	4
7.1	长连接	4
7.2	短连接	6
7.3	本协议涉及的端口号	6
7.4	交互过程中的应答方式	7
8	消息定义	7
8.1	基本数据类型	7
8.2	消息结构	7
8.3	消息头格式（Message Header）	8
8.4	业务提供商(SP)与互联网短信网关(ISMG)间的消息定义	8
8.4.1	SP请求连接到ISMG（CMPP_CONNECT）操作	8
8.4.1.1	CMPP_CONNECT消息定义（SPISMG）	8
8.4.1.2	CMPP_CONNECT_RESP消息定义（ISMG  SP）	9
8.4.2	SP或ISMG请求拆除连接（CMPP_TERMINATE）操作	9
8.4.2.1	CMPP_TERMINATE消息定义（SPISMG或ISMG  SP）	9
8.4.2.2	CMPP_TERMINATE_RESP消息定义（SPISMG或ISMG  SP）	10
8.4.3	SP向ISMG提交短信（CMPP_SUBMIT）操作	10
8.4.3.1	CMPP_SUBMIT消息定义（SPISMG）	10
8.4.3.2	CMPP_SUBMIT_RESP消息定义（ISMG  SP）	11
8.4.4	SP向ISMG查询发送短信状态（CMPP_QUERY）操作	12
8.4.4.1	CMPP_QUERY消息的定义（SPISMG）	12
8.4.4.2	CMPP_QUERY_RESP消息的定义（ISMG SP）	13
8.4.5	ISMG向SP送交短信（CMPP_DELIVER）操作	13
8.4.5.1	CMPP_DELIVER消息定义（ISMGSP）	13
8.4.5.2	CMPP_DELIVER_RESP消息定义（SP  ISMG）	16
8.4.6	SP向ISMG发起删除短信（CMPP_CANCEL）操作	16
8.4.6.1	CMPP_CANCEL消息定义（SP  ISMG）	16
8.4.6.2	CMPP_CANCEL_RESP消息定义（ISMG  SP）	17
8.4.7	链路检测（CMPP_ACTIVE_TEST）操作	17
8.4.7.1	CMPP_ACTIVE_TEST定义（SP  ISMG或ISMGSP）	17
8.4.7.2	CMPP_ACTIVE_TEST_RESP定义（SP  ISMG或ISMGSP）	17
8.5	互联网短信网关(ISMG)之间的消息定义	17
8.5.1	源ISMG请求连接到目的ISMG（CMPP_CONNECT）操作	17
8.5.2	源ISMG请求拆除到目的ISMG的连接（CMPP_TERMINATE）操作	17
8.5.3	链路检测（CMPP_ACTIVE_TEST）操作	17
8.5.4	源ISMG向目的ISMG转发短信（CMPP_FWD）操作	17
8.5.4.1	CMPP_FWD定义（ISMG ISMG）	18
8.5.4.2	CMPP_FWD_RESP定义（ISMG ISMG）	21
8.6	互联网短信网关(ISMG)与汇接网关(GNS)之间的消息定义	21
8.6.1	ISMG请求连接到GNS或GNS请求连接到ISMG（CMPP_CONNECT）操作	21
8.6.2	ISMG请求拆除到GNS的连接或GNS请求拆除到ISMG的连接（CMPP_TERMINATE）操作	21
8.6.3	ISMG向汇接网关查询MT路由（CMPP_MT_ROUTE）操作	21
8.6.3.1	CMPP_MT_ROUTE消息定义（ISMGGNS）	22
8.6.3.2	CMPP_MT_ROUTE_RESP消息定义（GNS  ISMG）	22
8.6.4	ISMG向汇接网关查询MO路由（CMPP_MO_ROUTE）操作	22
8.6.4.1	CMPP_MO_ROUTE消息定义（ISMGGNS）	22
8.6.4.2	CMPP_MO_ROUTE_RESP消息定义（GNS  ISMG）	23
8.6.5	ISMG向汇接网关获取MT路由（CMPP_GET_MT_ROUTE）操作	23
8.6.5.1	CMPP_GET_MT_ROUTE消息定义（ISMGGNS）	24
8.6.5.2	CMPP_GET_ ROUTE_RESP消息定义（GNS  ISMG）	24
8.6.6	SMG向汇接网关获取MO路由（CMPP_GET_MO_ROUTE）操作	25
8.6.6.1	CMPP_GET_MO_ROUTE消息定义（ISMGGNS）	25
8.6.6.2	CMPP_GET_MO_ROUTE_RESP消息定义（GNS  ISMG）	25
8.6.7	ISMG向汇接网关更新MT路由（CMPP_MT_ROUTE_UPDATE）操作	26
8.6.7.1	CMPP_MT_ROUTE_UPDATE消息定义（ISMGGNS）	26
8.6.7.2	CMPP_MT_ROUTE_UPDATE_RESP消息定义（GNS  ISMG）	27
8.6.8	ISMG向汇接网关更新MO路由（CMPP_MO_ROUTE_UPDATE）操作	27
8.6.8.1	CMPP_MO_ROUTE_UPDATE消息定义（ISMGGNS）	27
8.6.8.2	CMPP_MO_ROUTE_UPDATE_RESP消息定义（GNS  ISMG）	28
8.6.9	汇接网关向ISMG更新MT路由（CMPP_PUSH_MT_ROUTE_UPDATE）操作	29
8.6.9.1	CMPP_PUSH_MT_ROUTE_UPDATE消息定义（GNSISMG）	29
8.6.9.2	CMPP_PUSH_MT_ROUTE_UPDATE_RESP消息定义（ISMG  GNS）	29
8.6.10	汇接网关向ISMG更新MO路由（CMPP_PUSH_MO_ROUTE_UPDATE）操作	29
8.6.10.1	CMPP_PUSH_MO_ROUTE_UPDATE消息定义（GNSISMG）	30
8.6.10.2	CMPP_PUSH_MO_ROUTE_UPDATE_RESP消息定义（ISMG  GNS）	30
8.7	系统定义	31
8.7.1	Command_Id定义	31
8.7.2	错误码使用说明	31
8.7.3	ISMG与GNS之间消息使用的错误码定义	32
8.7.4	GNS上路由信息的Route_Id的编号规则	33
9	附录1 短信群发功能的实现	34
10	附录2 GNS协议目前实现说明	34
11	修订历史	36


 

前  言

本规范规定了移动梦网短信业务开展过程中各网元（包括ISMG、GNS和SP）之间的消息类型和定义，目前为3.0.0版本，是在原来2.1.0版本的基础上进行修订而成。根据业务的发展，规范中的信令操作和参数将会做进一步的调整和增加。
本标准由中国移动通信集团公司技术部提出并归口。
本标准起草单位：中国移动通信集团公司研发中心。
本标准主要起草人：党京、孙若雯、于蓉蓉、袁向阳。
本标准解释单位：同提出单位。 
1	范围
本规范规定了以下三方面的内容：
1）	业务提供商与互联网短信网关之间的接口协议；
2）	互联网短信网关之间的接口协议；
3）	互联网短信网关与汇接网关之间的接口协议。
本规范适用于各SP和ISMG、GNS的开发厂商。

2	引用标准
下列标准所包含的条文，通过在本标准中引用而成为本标准的条文。本标准出版时，所示版本均为有效。所有标准都会被修订，使用本标准的各方应探讨使用下列标准最新版本的可能性。
《SMPP》
《移动梦网短信业务技术方案》
3	术语和定义
英文缩写	英文全称	说明
ISMG	Intenet Short Message Gateway	互联网短信网关
DSMP	Data Service Manage Platform	数据业务管理平台
SMPP	Short Message Peer to Peer	短消息点对点协议
CMPP	China Mobile Peer to Peer	中国移动点对点协议
SMSC	Short Message Service Center	短消息中心
GNS	Gateway Name Server	网关名称服务器（汇接网关）
SP	Service Provider	业务提供者
ISMG_Id		网关代码：0XYZ01~0XYZ99，其中XYZ为省会区号，位数不足时左补零，如北京编号为1的网关代码为001001，江西编号为1的网关代码为079101，依此类推
SP_Id		SP的企业代码：网络中SP地址和身份的标识、地址翻译、计费、结算等均以企业代码为依据。企业代码以数字表示，共6位，从“9XY000”至“9XY999”，其中“XY”为各移动公司代码
SP_Code		SP的服务代码：服务代码是在使用短信方式的上行类业务中，提供给用户使用的服务提供商代码。服务代码以数字表示，全国业务服务代码长度为4位，即“1000”－“9999”；本地业务服务代码长度统一为5位，即“01000”－“09999”；信产部对新的SP的服务代码分配提出了新的要求，要求以“1061”－“1069”作为前缀，目前中国移动进行了如下分配：
1062：用于省内SP服务代码
1066：用于全国SP服务代码
其它号段保留。
Service_Id		SP的业务类型，数字、字母和符号的组合，由SP自定，如图片传情可定为TPCQ，股票查询可定义为11

4	网络结构
 
图1	互联网短信网关组网结构

如图1所示，互联网短信网关（ISMG）是业务提供商（SP）与移动网内短信中心之间的中介实体，互联网短信网关一方面负责接收SP发送给移动用户的信息和提交给短信中心。另一方面，移动用户点播SP业务的信息将由短信中心通过互联网短信网关发给SP。另外，为了减轻短信中心的信令负荷，互联网短信网关还应根据路由原则将SP提交的信息转发到相应的互联网短信网关。互联网短信网关通过向汇接网关（GNS）查询的方式获得网关间的转发路由信息。
另外，ISMG还必须与数据业务管理平台DSMP进行连接，在业务流程中对用户、业务以及定购关系等进行鉴权并对业务进行批价。
5	CMPP功能概述
CMPP协议主要提供以下两类业务操作：
（1）短信发送（Short Message Mobile Originate，SM MO）
详细的流程请参考《移动梦网短信业务信令流程规范V3.0.0》；
（2）	短信接收（Short Message Mobile Terminated，SM MT）
详细的流程请参考《移动梦网短信业务信令流程规范V3.0.0》；
6	协议栈
CMPP协议以TCP/IP作为底层通信承载，具体结构由图4所示：
 
图2	CMPP协议栈
7	通信方式
各网元之间共有两种连接方式：长连接和短连接。所谓长连接，指在一个TCP连接上可以连续发送多个数据包，在TCP连接保持期间，如果没有数据包发送，需要双方发链路检测包以维持此连接。短连接是指通信双方有数据交互时，就建立一个TCP连接，数据发送完成后，则断开此TCP连接，即每次TCP连接只完成一对CMPP消息的发送。
现阶段，要求ISMG之间必须采用长连接的通信方式，建议SP与ISMG之间采用长连接的通信方式。
7.1	长连接
通信双方以客户-服务器方式建立TCP连接，用于双方信息的相互提交。当信道上没有数据传输时，通信双方应每隔时间C发送链路检测包以维持此连接，当链路检测包发出超过时间T后未收到响应，应立即再发送链路检测包，再连续发送N-1次后仍未得到响应则断开此连接。
参数C、T、N原则上应可配置，现阶段建议取值为：C=3分钟，T=60秒，N=3。
网关与SP之间、网关之间的消息发送后等待T秒后未收到响应，应立即重发，再连续发送N-1次后仍未得到响应则停发。现阶段建议取值为：T=60秒，N=3。
消息采用并发方式发送，加以滑动窗口流量控制，窗口大小参数W可配置，现阶段建议为16，即接收方在应答前一次收到的消息最多不超过16条。
长连接的操作流程举例如图5所示：
 
图3	长连接操作流程
7.2	短连接
通信双方以客户-服务器方式建立TCP连接，应答与请求在同一个连接中完成。系统采用客户/服务器模式，操作以客户端驱动方式发起连接请求，完成一次操作后关闭此连接。
网关与SP之间、网关之间的消息发送后等待T秒后未收到响应，应立即重发，再连续发送N-1次后仍未得到响应则停发。现阶段建议取值为：T=60秒，N=3。
短连接的操作流程举例如图6所示：
 
图4	短连接操作流程
7.3	本协议涉及的端口号
端口号	应用
7890	长连接（SP与网关间）
7900	短连接（SP与网关间）
7930	长连接（网关之间）
9168	短连接（短信网关与汇接网关之间）

7.4	交互过程中的应答方式
在SP与ISMG之间、SMSC与ISMG之间及ISMG之间的交互过程中均采用异步方式，即任一个网元在收到请求消息后应立即回送响应消息。举例如图7所示：
 
图5	异步交互方式示意图
8	消息定义
8.1	基本数据类型
Unsigned Integer  	无符号整数
Integer	整数，可为正整数、负整数或零
Octet String	定长字符串，位数不足时，如果左补0则补ASCII表示的零以填充，如果右补0则补二进制的零以表示字符串的结束符
8.2	消息结构
项目	说明
Message Header	消息头(所有消息公共包头)
Message Body	消息体

8.3	消息头格式（Message Header）
字段名	字节数	类型	描述
Total_Length		4	Unsigned Integer	消息总长度(含消息头及消息体)
Command_Id	4	Unsigned Integer	命令或响应类型
Sequence_Id	4	Unsigned Integer	消息流水号,顺序累加,步长为1,循环使用（一对请求和应答消息的流水号必须相同）

8.4	业务提供商(SP)与互联网短信网关(ISMG)间的消息定义
SP为客户端，向作为服务器端的ISMG发起连接请求，在通过身份验证之后SP与ISMG之间方可进行数据传输。
8.4.1	SP请求连接到ISMG（CMPP_CONNECT）操作
CMPP_CONNECT操作的目的是SP向ISMG注册作为一个合法SP身份，若注册成功后即建立了应用层的连接，此后SP可以通过此ISMG接收和发送短信。
ISMG以CMPP_CONNECT_RESP消息响应SP的请求。
8.4.1.1	CMPP_CONNECT消息定义（SPISMG）
字段名	字节数	属性	描述
Source_Addr		6	Octet String	源地址，此处为SP_Id，即SP的企业代码。
AuthenticatorSource	16	Octet String	用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下：
AuthenticatorSource =
MD5（Source_Addr+9 字节的0 +shared secret+timestamp）
Shared secret 由中国移动与源地址实体事先商定，timestamp格式为：MMDDHHMMSS，即月日时分秒，10位。
Version	1	Unsigned Integer	双方协商的版本号(高位4bit表示主版本号,低位4bit表示次版本号)，对于3.0的版本，高4bit为3，低4位为0
Timestamp	4	Unsigned Integer	时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐 。
8.4.1.2	CMPP_CONNECT_RESP消息定义（ISMG  SP）
字段名	字节数	属性	描述
Status	4	Unsigned Integer	状态
0：正确
1：消息结构错
 2：非法源地址
 3：认证错
 4：版本太高
  5~ ：其他错误
AuthenticatorISMG	16	Octet String	ISMG认证码，用于鉴别ISMG。
其值通过单向MD5 hash计算得出，表示如下：
AuthenticatorISMG =MD5（Status+AuthenticatorSource+shared secret），Shared secret 由中国移动与源地址实体事先商定，AuthenticatorSource为源地址实体发送给ISMG的对应消息CMPP_Connect中的值。
 认证出错时，此项为空。
Version	1	Unsigned Integer	服务器支持的最高版本号，对于3.0的版本，高4bit为3，低4位为0

8.4.2	SP或ISMG请求拆除连接（CMPP_TERMINATE）操作
CMPP_TERMINATE操作的目的是SP或ISMG基于某些原因决定拆除当前的应用层连接而发起的操作。此操作完成后SP与ISMG之间的应用层连接被释放，此后SP若再要与ISMG通信时应发起CMPP_CONNECT操作。
ISMG或SP以CMPP_TERMINATE_RESP消息响应请求。
8.4.2.1	CMPP_TERMINATE消息定义（SPISMG或ISMG  SP）
无消息体。
8.4.2.2	CMPP_TERMINATE_RESP消息定义（SPISMG或ISMG  SP）
无消息体。

8.4.3	SP向ISMG提交短信（CMPP_SUBMIT）操作
CMPP_SUBMIT操作的目的是SP在与ISMG建立应用层连接后向ISMG提交短信。
ISMG以CMPP_SUBMIT_RESP消息响应。
8.4.3.1	CMPP_SUBMIT消息定义（SPISMG）
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识。
Pk_total	1	Unsigned Integer	相同Msg_Id的信息总条数，从1开始。
Pk_number	1	Unsigned Integer	相同Msg_Id的信息序号，从1开始。
Registered_Delivery	1	Unsigned Integer	是否要求返回状态确认报告：
0：不需要；
1：需要。
Msg_level	1	Unsigned Integer	信息级别。
Service_Id	10	Octet String	业务标识，是数字、字母和符号的组合。
Fee_UserType	1	Unsigned Integer	计费用户类型字段：
0：对目的终端MSISDN计费；
1：对源终端MSISDN计费；
2：对SP计费；
3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
Fee_terminal_Id	32	Octet String	被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。
Fee_terminal_type	1	Unsigned Integer	被计费用户的号码类型，0：真实号码；1：伪码。
TP_pId	1	Unsigned Integer	GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.9。
TP_udhi	1	Unsigned Integer	GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐。
Msg_Fmt	1	Unsigned Integer	信息格式：
0：ASCII串；
3：短信写卡操作；
4：二进制信息；
8：UCS2编码；
15：含GB汉字。。。。。。
Msg_src	6	Octet String	信息内容来源(SP_Id)。
FeeType	2	Octet String	资费类别：
01：对“计费用户号码”免费；
02：对“计费用户号码”按条计信息费；
03：对“计费用户号码”按包月收取信息费。
FeeCode	6	Octet String	资费代码（以分为单位）。
ValId_Time	17	Octet String	存活有效期，格式遵循SMPP3.3协议。
At_Time	17	Octet String	定时发送时间，格式遵循SMPP3.3协议。
Src_Id	21	Octet String	源号码。SP的服务代码或前缀为服务代码的长号码, 网关将该号码完整的填到SMPP协议Submit_SM消息相应的source_addr字段，该号码最终在用户手机上显示为短消息的主叫号码。
DestUsr_tl	1	Unsigned Integer	接收信息的用户数量(小于100个用户)。
Dest_terminal_Id	32*DestUsr_tl	Octet String	接收短信的MSISDN号码。
Dest_terminal_type	1	Unsigned Integer	接收短信的用户的号码类型，0：真实号码；1：伪码。
Msg_Length	1	Unsigned Integer	信息长度(Msg_Fmt值为0时：<160个字节；其它<=140个字节)，取值大于或等于0。
Msg_Content	Msg_length	Octet String	信息内容。
LinkID	20	Octet String	点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。

系统应该支持短信的群发功能，关于短信群发功能的实现请参阅“附录1 短信群发功能的实现”。
8.4.3.2	CMPP_SUBMIT_RESP消息定义（ISMG  SP）

字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识，生成算法如下：
采用64位（8字节）的整数：
（1）	时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中
bit64~bit61：月份的二进制表示；
bit60~bit56：日的二进制表示；
bit55~bit51：小时的二进制表示；
bit50~bit45：分的二进制表示；
bit44~bit39：秒的二进制表示；
（2）	短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中；
（3）	序列号：bit16~bit1，顺序增加，步长为1，循环使用。
各部分如不能填满，左补零，右对齐。
（SP根据请求和应答消息的Sequence_Id一致性就可得到CMPP_Submit消息的Msg_Id）
Result	4	Unsigned Integer	结果：
0：正确；
1：消息结构错；
 2：命令字错；
 3：消息序号重复；
4：消息长度错；
5：资费代码错；
6：超过最大信息长；
7：业务代码错；
8：流量控制错；
9：本网关不负责服务此计费号码；
10：Src_Id错误；
11：Msg_src错误；
12：Fee_terminal_Id错误；
13：Dest_terminal_Id错误；
……

8.4.4	SP向ISMG查询发送短信状态（CMPP_QUERY）操作
CMPP_QUERY操作的目的是SP向ISMG查询某时间的业务统计情况，可以按总数或按业务代码查询。ISMG以CMPP_QUERY_RESP应答。


8.4.4.1	CMPP_QUERY消息的定义（SPISMG）
字段名	字节数	属性	描述
Time	8	Octet String	时间YYYYMMDD(精确至日)。
Query_Type	1	Unsigned Integer	查询类别：
0：总数查询；
1：按业务类型查询。
Query_Code	10	Octet String	查询码。
当Query_Type为0时，此项无效；当Query_Type为1时，此项填写业务类型Service_Id.。
Reserve	8	Octet String	保留。

8.4.4.2	CMPP_QUERY_RESP消息的定义（ISMG SP）
字段名	字节数	属性	描述
Time	8	Octet String	时间(精确至日)。
Query_Type	1	Unsigned Integer	查询类别：
0：总数查询；
1：按业务类型查询。
Query_Code	10	Octet String	查询码。
MT_TLMsg	4	Unsigned Integer	从SP接收信息总数。
MT_Tlusr	4	Unsigned Integer	从SP接收用户总数。
MT_Scs	4	Unsigned Integer	成功转发数量。
MT_WT	4	Unsigned Integer	待转发数量。
MT_FL	4	Unsigned Integer	转发失败数量。
MO_Scs	4	Unsigned Integer	向SP成功送达数量。
MO_WT	4	Unsigned Integer	向SP待送达数量。
MO_FL	4	Unsigned Integer	向SP送达失败数量。

8.4.5	ISMG向SP送交短信（CMPP_DELIVER）操作
CMPP_DELIVER操作的目的是ISMG把从短信中心或其它ISMG转发来的短信送交SP，SP以CMPP_DELIVER_RESP消息回应。
8.4.5.1	CMPP_DELIVER消息定义（ISMGSP）
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识。
生成算法如下：
采用64位（8字节）的整数：
（1）	时间（格式为MMDDHHMMSS，即月日时分秒）：bit64~bit39，其中
bit64~bit61：月份的二进制表示；
bit60~bit56：日的二进制表示；
bit55~bit51：小时的二进制表示；
bit50~bit45：分的二进制表示；
bit44~bit39：秒的二进制表示；
（2）	短信网关代码：bit38~bit17，把短信网关的代码转换为整数填写到该字段中；
（3）	序列号：bit16~bit1，顺序增加，步长为1，循环使用。
各部分如不能填满，左补零，右对齐。
Dest_Id	21	Octet String	目的号码。
SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码；该号码是手机用户短消息的被叫号码。
Service_Id	10	Octet String	业务标识，是数字、字母和符号的组合。
TP_pid	1	Unsigned Integer	GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9。
TP_udhi	1	Unsigned Integer	GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23，仅使用1位，右对齐。
Msg_Fmt	1	Unsigned Integer	信息格式：
0：ASCII串；
3：短信写卡操作；
4：二进制信息；
8：UCS2编码；
15：含GB汉字。
Src_terminal_Id	32	Octet String	源终端MSISDN号码（状态报告时填为CMPP_SUBMIT消息的目的终端号码）。
Src_terminal_type	1	Unsigned Integer	源终端号码类型，0：真实号码；1：伪码。
Registered_Delivery	1	Unsigned Integer	是否为状态报告：
0：非状态报告；
1：状态报告。
Msg_Length	1	Unsigned Integer	消息长度，取值大于或等于0。
Msg_Content	Msg_length	Octet String	消息内容。
LinkID	20	Octet String	点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。

当ISMG向SP送交状态报告时，信息内容字段（Msg_Content）格式定义如下：

字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识。
SP提交短信（CMPP_SUBMIT）操作时，与SP相连的ISMG产生的Msg_Id。
Stat	7	Octet String	发送短信的应答结果，含义详见表一。SP根据该字段确定CMPP_SUBMIT消息的处理状态。
Submit_time	10	Octet String	YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）。
Done_time	10	Octet String	YYMMDDHHMM。
Dest_terminal_Id	32	Octet String	目的终端MSISDN号码(SP发送CMPP_SUBMIT消息的目标终端)。
SMSC_sequence	4	Unsigned Integer	取自SMSC发送状态报告的消息体中的消息标识。

表一  Stat字段定义
Message State	Final Message States	Description
DELIVERED	DELIVRD	Message is delivered to destination
EXPIRED	EXPIRED	Message validity period has
expired
DELETED	DELETED	Message has been deleted.
UNDELIVERABLE	UNDELIV	Message is undeliverable
ACCEPTED	ACCEPTD	Message is in accepted state(i.e. has been manually read on behalf of the subscriber by customer service)
UNKNOWN	UNKNOWN	Message is in invalid state
REJECTED	REJECTD	Message is in a rejected state
MA:xxxx	MA:xxxx	SMSC不返回响应消息时的状态报告
MB:xxxx	MB:xxxx	SMSC返回错误响应消息时的状态报告
MC:xxxx	MC:xxxx	没有从SMSC处接收到状态报告时的状态报告
CA:xxxx	CA:xxxx	SCP不返回响应消息时的状态报告
CB:xxxx	CB:xxxx	SCP返回错误响应消息时的状态报告
DA:xxxx	DA:xxxx	DSMP不返回响应消息时的状态报告
DB:xxxx	DB:xxxx	DSMP返回错误响应消息时的状态报告
SA:xxxx	SA:xxxx	SP不返回响应消息时的状态报告
SB:xxxx	SB:xxxx	SP返回错误响应消息时的状态报告
IA:xxxx	IA:xxxx	下一级ISMG不返回响应消息时的状态报告
IB:xxxx	IB:xxxx	下一级ISMG返回错误响应消息时的状态报告
IC:xxxx	IC:xxxx	没有从下一级ISMG处接收到状态报告时的状态报告

注意：
1．	其中ACCEPTED为中间状态，网关若从短信中心收到后应丢弃，不做任何操作；
2．	Stat字段长度为7个字节，填写时应填表一中Final Message States中的缩写形式，如状态为DELIVERED时填写DELIVRD，依此类推；
3．	SP等待状态报告缺省时间为48小时。

8.4.5.2	CMPP_DELIVER_RESP消息定义（SP  ISMG）
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识（CMPP_DELIVER中的Msg_Id字段）。
Result	4	Unsigned Integer	结果：
0：正确；
1：消息结构错；
 2：命令字错；
 3：消息序号重复；
4：消息长度错；
5：资费代码错；
6：超过最大信息长；
7：业务代码错；
8: 流量控制错；
9~ ：其他错误。

8.4.6	SP向ISMG发起删除短信（CMPP_CANCEL）操作
CMPP_CANCEL操作的目的是SP通过此操作可以将已经提交给ISMG的短信删除，ISMG将以CMPP_CANCEL_RESP回应删除操作的结果。

8.4.6.1	CMPP_CANCEL消息定义（SP  ISMG）
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识（SP想要删除的信息标识）。

8.4.6.2	CMPP_CANCEL_RESP消息定义（ISMG  SP）
字段名	字节数	属性	描述
Success_Id	4	Unsigned Integer	成功标识。
0：成功；
1：失败。


8.4.7	链路检测（CMPP_ACTIVE_TEST）操作
本操作仅适用于通信双方采用长连接通信方式时用于保持连接。
8.4.7.1	CMPP_ACTIVE_TEST定义（SP  ISMG或ISMGSP）
无消息体。
8.4.7.2	CMPP_ACTIVE_TEST_RESP定义（SP  ISMG或ISMGSP）
字段名	字节数	属性	描述
Reserved	1		

8.5	互联网短信网关(ISMG)之间的消息定义
网关之间互为客户/服务器，任一方在需要传递消息时，向对方请求建立连接，并在身份验证通过后进行数据传输。
8.5.1	源ISMG请求连接到目的ISMG（CMPP_CONNECT）操作
消息定义同8.4.1.1和8.4.1.2所述。其中Source_Addr填源网关代码。
8.5.2	源ISMG请求拆除到目的ISMG的连接（CMPP_TERMINATE）操作
消息定义同8.4.2.1和8.4.2.2所述。
8.5.3	链路检测（CMPP_ACTIVE_TEST）操作
本操作仅用于通信双方采用长连接通信方式时保持连接。消息定义同8.4.6.1和8.4.6.2所述。
8.5.4	源ISMG向目的ISMG转发短信（CMPP_FWD）操作
CMPP_FWD操作的目的是源ISMG可以根据一定的路由策略将SP提交的短信、MO状态报告、短信中心产生的状态报告、用户提交的短信转发到目的ISMG，目的ISMG以CMPP_FWD_RESP回应。
8.5.4.1	CMPP_FWD定义（ISMG ISMG）
字段名	字节数	属性	描述
Source_Id	6	Octet String	源网关的代码（右对齐，左补0）。
Destination_Id	6	Octet String	目的网关代码（右对齐，左补0）。
NodesCount	1	Unsigned Integer	经过的网关数量。
Msg_Fwd_Type	1	Unsigned Integer	前转的消息类型：
0：MT前转；
1：MO前转；
2：MT时的状态报告；
3：MO时的状态报告；
Msg_Id	8	Unsigned Integer	信息标识。
Pk_total	1	Unsigned Integer	相同Msg_Id的消息总条数，从1开始。
Pk_number	1	Unsigned Integer	相同Msg_Id的消息序号，从1开始。
Registered_Delivery	1	Unsigned Integer	是否要求返回状态确认报告：
0：不需要；
1：需要；
2：产生SMC话单。
Msg_level	1	Unsigned Integer	信息级别。
Service_Id	10	Octet String	业务标识。
Fee_UserType	1	Unsigned Integer	计费用户类型字段：
0：对目的终端MSISDN计费；
1：对源终端MSISDN计费；
2：对SP计费；
3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
Fee_terminal_Id	21	Octet String	被计费用户的号码，当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义。
Fee_terminal_Pseudo	32	Octet String	被计费用户的伪码。
Fee_terminal_UserType	1	Unsigned Integer	计费用户号码的用户类型，0：全球通，1：神州行。
TP_pid	1	Unsigned Integer	GSM协议类型。详细解释请参考GSM03.40中的9.2.3.9。
TP_udhi	1	Unsigned Integer	GSM协议类型。详细解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐。
Msg_Fmt	1	Unsigned Integer	信息格式：
0：ASCII串；
3：短信写卡操作；
4：二进制信息；
8：UCS2编码；
15：含GB汉字。
Msg_src	6	Octet String	信息内容来源（SP_Id，SP的企业代码）。
FeeType	2	Octet String	资费类别。
01：对“计费用户号码”免费；
02：对“计费用户号码”按条计信息费；
03：对“计费用户号码”按包月收取信息费；
06：对“计费用户号码”按包月查询收费。
FeeCode	6	Octet String	资费代码（以分为单位）。
Valid_Time	17	Octet String	有效期。
At_Time	17	Octet String	定时发送的时间。
Src_Id	21	Octet String	源号码。
1．	MT时为SP的服务代码，即CMPP_SUBMIT消息中的Src_Id。
2．	MO时为发送此消息的源终端MSISDN号码。
3．	MT状态报告时，填接收到短信的终端MSISDN号码，即对应CMPP_SUBMIT消息中的Dest_Terminal_Id。
4．	MO状态报告时，填SP的服务代码，即CMPP_DELIVER中的Dest_Id。
Src_Pseudo	32	Octet String	源号码的伪码。
Src_UserType	1	Unsigned Integer	源号码的用户类型，0：全球通，1：神州行。
Src_type	1	Unsigned Integer	传递给SP的源号码的类型，0：真实号码；1：伪码。
DestUsr_tl	1	Unsigned Integer	接收消息的用户数量，必须为1。
Dest_Id	21*DestUsr_tl	Octet String	目的号码。
1．	MT转发时为目的终端MSISDN号码，即对应CMPP_SUBMIT消息中的Dest_Terminal_Id。
2．	MO转发时为SP的服务代码，一般4--6位，或者是前缀为服务代码的长号码，该号码是手机用户短消息的被叫号码。
3．	MT状态报告时，填目的SP的服务代码，即CMPP_SUBMIT消息中的Src_Id。
4．	MO状态报告时，填发送短信的移动用户MSISDN号码。
Dest_Pseudo	32	Octet String	目的用户的伪码。
Dest_UserType	1	Unsigned Integer	目的号码的用户类型，0：全球通，1：神州行。
Msg_Length	1	Unsigned Integer	消息长度，取值大于或等于0。
Msg_Content	Msg_length	Octet String	消息内容。
LinkID	20	Octet String	点播业务使用的LinkID。

注意：
1．	当转发消息为MO状态报告时，信息内容字段（Msg_Content）格式定义如下：
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识。
给SP的CMPP_Deliver消息中的Msg_Id，与源网关转发MO消息时产生的Msg_Id相同。
Stat	7	Octet String	SP的应答结果，CMPP_DELIVER_RESP中Result为0时，填字符DELIVRD，其余异常的值可能为SA:xxxx或SB:xxxx，含义请参考CMPP_DELIVER中对状态报告的说明。
CMPP_DELIVER_time	10	Octet String	YYMMDDHHMM（YY为年的后两位00-99，MM：01-12，DD：01-31，HH：00-23，MM：00-59）。
注：短信网关发出CMPP_DELIVER的时间。
CMPP_DELIVER_RESP_time	10	Octet String	YYMMDDHHMM。
注：短信网关收到CMPP_DELIVER_RESP的时间。
Dest_Id	21	Octet String	目的SP的服务代码，左对齐。
Reserved	4		

2．	当转发消息为MT状态报告时，信息内容字段（Msg_Content）格式同8.4.5.1定义。
8.5.4.2	CMPP_FWD_RESP定义（ISMG ISMG）
字段名	字节数	属性	描述
Msg_Id	8	Unsigned Integer	信息标识（CMPP_FWD中字段值）
Pk_total	1	Unsigned Integer	相同Msg_Id的消息总条数
Pk_number	1	Unsigned Integer	相同Msg_Id的消息序号
Result	4	Unsigned Integer	结果
0：正确
1：消息结构错
 2：命令字错
 3：消息序号重复
4：消息长度错
5：资费代码错
6：超过最大信息长
7：业务代码错
8: 流量控制错
9: 前转判断错(此SP不应发往本ISMG)
10~ ：其他错误

8.6	互联网短信网关(ISMG)与汇接网关(GNS)之间的消息定义
要求ISMG与GNS在信息交互时使用短连接的通信方式。ISMG与GNS可互为客户/服务器。
8.6.1	ISMG请求连接到GNS或GNS请求连接到ISMG（CMPP_CONNECT）操作
消息定义同8.4.1.1和8.4.1.2所述，其中Source_Addr填源网关代码，可能是ISMG代码或GNS代码。
8.6.2	ISMG请求拆除到GNS的连接或GNS请求拆除到ISMG的连接（CMPP_TERMINATE）操作
消息定义同8.4.2.1和8.4.2.2所述。
8.6.3	ISMG向汇接网关查询MT路由（CMPP_MT_ROUTE）操作
CMPP_MT_ROUTE操作用于ISMG不知道需要转发MT消息的路由时查询GNS。GNS以CMPP_MT_ROUTE_RESP应答。
8.6.3.1	CMPP_MT_ROUTE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Source_Id	6	Octet String	源网关代码
Terminal_Id	21	Octet String	目的终端MSISDN号码

8.6.3.2	CMPP_MT_ROUTE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Route_Id	4	Unsigned Integer	路由编号(MO/MT分别从0开始,由GNS统一分配)
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
(地址格式举例：67.221.134.12，左对齐)
Gateway_port	2	Unsigned Integer	目标网关IP端口
Start_Id	9	Octet String	MT路由起始号码段
End_Id	9	Octet String	MT路由截止号码段
Area_code	4	Octet String	手机所属省代号
Result	1	Unsigned Integer	结果
0：正常
1：没有匹配路由
2：源网关代码错
9：系统繁忙
User_type	1	Unsigned Integer	用户类型
0：全球通
1：神州行
2：M-Zone
…...
Time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

8.6.4	ISMG向汇接网关查询MO路由（CMPP_MO_ROUTE）操作
CMPP_MO_ROUTE操作的目的是使ISMG当不知道需要转发MO消息的路由时可向GNS查询得到。GNS以CMPP_MO_ROUTE_RESP应答。
8.6.4.1	CMPP_MO_ROUTE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Source_Id	6	Octet String	源网关代码
SP_Code	21	Octet String	SP的服务代码
Service_Id	10	Octet String	请求的业务类型（此项适合全网服务内容，如梦网卡图片传情）
Service_Code	4	Unsigned Integer	请求的业务代码
（如果未置Service_Id字段，此字段为空，如梦网卡图片传情TPCQ1000—2000对应某个网站的某些相应图片）

8.6.4.2	CMPP_MO_ROUTE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Route_Id	4	Unsigned Integer	路由编号（MO/MT分别从0开始,由GNS统一分配）
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
SP_Id	6	Octet String	SP的企业代码
SP_Code	21	Octet String	SP的服务代码
SP_AcessType	1	Unsigned Integer	SP接入类型
0：全网业务SP全网接入，即接入网关为SP的主力接入点
1：全网业务SP镜像接入，即接入网关为SP的镜像接入点
Start_code	4	Unsigned Integer	MO路由起始业务代码
（如果未置请求的Service_Id字段，此字段为空）
End_code	4	Unsigned Integer	MO路由截止业务代码
（如果未置请求的Service_Id字段，此字段为空）
Result	1	Unsigned Integer	结果
0：正常
1：没有匹配路由
2：源网关服务代码错
9：系统繁忙
Time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

8.6.5	ISMG向汇接网关获取MT路由（CMPP_GET_MT_ROUTE）操作
CMPP_GET_MT_ROUTE操作的目的是使ISMG可向GNS查询MT的路由信息。GNS以CMPP_GET_MT_ROUTE_RESP消息回应。
8.6.5.1	CMPP_GET_MT_ROUTE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Source_Id	6	Octet String	源网关代码
Route_type	4	Octet String	路由类型
MT：MT路由
（考虑今后的扩展性，故保留此字段）
Last_route_Id	4	Integer	已经接收的上一条路由编号
（第1次发送此请求时Last_route_Id=
-1）

8.6.5.2	CMPP_GET_ ROUTE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Route_Id	4	Unsigned Integer	路由编号（MO/MT分别从0开始,由GNS统一分配）
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
Start_Id	9	Octet String	手机号码段的起始号码
End_Id	9	Octet String	手机号码段的截止号码
Area_code	4	Octet String	手机所属省代码
Result	1	Unsigned Integer	结果
0：正常
1：没有匹配路由
2：源网关代码错
3：路由类型错
9：系统繁忙
User_type	1	Unsigned Integer	用户类型
0：全球通
1：神州行
2：M-Zone
……
Route_total	4	Unsigned Integer	返回路由总数
Route_number	4	Unsigned Integer	当前返回的路由序号，从1开始，顺序递增
Time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

说明： Route_total和Route_number两个字段，能够让短信网关清楚地知道MT/MO的总路由数，以及当前拿的是第几条路由记录。这样，短信网关就可以更加方便地检验是否已经完全获取所有路由记录，以便更新本地缓存的路由表。

8.6.6	SMG向汇接网关获取MO路由（CMPP_GET_MO_ROUTE）操作
8.6.6.1	CMPP_GET_MO_ROUTE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Source_Id	6	Octet String	源网关代码
Route_type	4	Octet String	路由类型
MO：MO路由
（考虑今后的扩展性，故保留此字段）
Last_route_Id	4	Integer	已经接收的上一条路由编号
（第1次发送此请求时Last_route_Id=
-1）

8.6.6.2	CMPP_GET_MO_ROUTE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Route_Id	4	Unsigned Integer	路由编号（MO/MT分别从0开始,由GNS统一分配）
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
SP_Id	6	Octet String	SP的企业代码
SP_Code	21	Octet String	SP的服务代码
SP_AcessType	1	Unsigned Integer	SP接入类型
0：全网业务SP全网接入，即接入网关为SP的主力接入点
1：全网业务SP镜像接入，即接入网关为SP的镜像接入点
Service_Id	10	Octet String	请求的业务类型
（此项适合全网服务内容，如梦网卡图片传情）
Start_code	4	Unsigned Integer	请求的路由类型=MO时：
起始业务代码（如果未置Service_Id字段，此字段为空）
End_code	4	Unsigned Integer	请求的路由类型=MO时：
截止业务代码（如果未置Service_Id字段，此字段为空）
Result	1	Unsigned Integer	结果
0：正常
1：没有匹配路由
2：源网关代码错
3：路由类型错
9：系统繁忙
Route_total	4	Unsigned Integer	返回路由总数
Route_number	4	Unsigned Integer	当前返回的路由序号，从1开始，顺序递增
Time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

8.6.7	ISMG向汇接网关更新MT路由（CMPP_MT_ROUTE_UPDATE）操作
CMPP_MT_ROUTE_UPDATE操作的目的是使ISMG可向GNS更新MT路由信息。GNS以CMPP_MT_ROUTE_UPDATE _RESP消息回应。
8.6.7.1	CMPP_MT_ROUTE_UPDATE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Update_type	1	Unsigned Integer	0：添加
1：删除
2：更新
Route_Id	4	Unsigned Integer	路由编号（MO/MT分别从0开始,由GNS统一分配）
（若update_type 为0，即添加时，此字段为零）
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
Start_Id	9	Octet String	MT路由起始号码段
End_Id	9	Octet String	MT路由截止号码段
Area_code	4	Octet String	手机所属省代码
User_type	1	Unsigned Integer	用户类型
0：全球通
1：神州行
2：M-Zone
……

8.6.7.2	CMPP_MT_ROUTE_UPDATE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Result	1	Unsigned Integer	0：数据合法，等待核实
4：本节点不支持更新（GNS分节点）
9：系统繁忙
10：Update_type错误
11：路由编号错误
12：目的网关代码错误
13：目的网关IP错误
14：目的网关Port错误
15：MT路由起始号码段错误
16：MT路由截止号码段错误
17：手机所属省代码错误
18：用户类型错误
Route_Id	4	Unsigned Integer	路由编号
（当路由更新类型为更新和删除时返回原路由编号，当路由更新类型为添加时返回新分配的路由编号）
Time_stamp	14	Octet String	本路由信息的更新请求收到时间
格式是：yyyymmddhhmmss，
例如20030117014512

说明：如果插入新路由，汇接网关就分配新的Route_Id，并且返回。

8.6.8	ISMG向汇接网关更新MO路由（CMPP_MO_ROUTE_UPDATE）操作
CMPP_MO_ROUTE_UPDATE操作的目的是使ISMG可向GNS更新MO路由信息。GNS以CMPP_MO_ROUTE_UPDATE _RESP消息回应。
8.6.8.1	CMPP_MO_ROUTE_UPDATE消息定义（ISMGGNS）
字段名	字节数	属性	描述
Update_type	1	Unsigned Integer	0：添加
1：删除
2：更新
Route_Id	4	Unsigned Integer	路由编号
（若update_type 为0，即添加时，此字段为零）
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
SP_Id	6	Octet String	SP的企业代码
SP_Code	21	Octet String	SP的服务号码
SP_AcessType	1	Unsigned Integer	SP接入类型
0：全网业务SP全网接入，即接入网关为SP的主力接入点
1：全网业务SP镜像接入，即接入网关为SP的镜像接入点
Service_Id	10	Octet String	请求的业务类型（此项适合全网服务内容，如梦网卡图片传情,如该路由不包含此业务，此字段为空）
Start_code	4	Unsigned Integer	MO路由起始业务代码（如果未置请求的Service_Id字段，此字段为空）
End_code	4	Unsigned Integer	MO路由截止业务代码（如果未置请求的Service_Id字段，此字段为空）

8.6.8.2	CMPP_MO_ROUTE_UPDATE_RESP消息定义（GNS  ISMG）
字段名	字节数	属性	描述
Result	1	Unsigned Integer	0：数据合法，等待核实
4：本节点不支持更新（GNS分节点）
9：系统繁忙
10：Update_type错误
11：路由编号错误
12：目标网关代码错误
13：目标网关IP错误
14：目标网关Port错误
19：SP_Id错误
20：SP_Code错误
21：SP_AccessType错误
22：Service_Id错误
23：Start_code错误
24：End_code错误

Route_Id	4	Unsigned Integer	路由编号
（当路由更新类型为更新和删除时返回原路由编号，当路由更新类型为添加时返回新分配的路由编号）
Time_stamp	14	Octet String	本路由信息的更新请求收到时间
格式是：yyyymmddhhmmss，
例如20030117014512

说明：如果插入新路由，汇接网关就分配新的Route_Id，并且返回。

8.6.9	汇接网关向ISMG更新MT路由（CMPP_PUSH_MT_ROUTE_UPDATE）操作
CMPP_PUSH_MT_ROUTE_UPDATE操作的目的是使GNS可向ISMG更新MT路由信息。ISMG以CMPP_PUSH_MT_ROUTE_UPDATE_RESP 消息回应。
8.6.9.1	CMPP_PUSH_MT_ROUTE_UPDATE消息定义（GNSISMG）
字段名	字节数	属性	描述
Update_type	1	Unsigned Integer	0：添加；
1：删除；
2：更新
Route_Id	4	Unsigned Integer	路由编号
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
Start_Id	9	Octet String	MT路由起始号码段
End_Id	9	Octet String	MT路由截止号码段
Area_code	4	Octet String	手机所属省代码
User_type	1	Unsigned Integer	用户类型
0：全球通
1：神州行
2：M-Zone
……
Time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

8.6.9.2	CMPP_PUSH_MT_ROUTE_UPDATE_RESP消息定义（ISMG  GNS）
字段名	字节数	属性	描述
Result	1	Unsigned Integer	0：成功更改
5：路由信息更新失败
6：汇接网关路由信息时间戳比本地路由信息时间戳旧
9：系统繁忙

8.6.10	汇接网关向ISMG更新MO路由（CMPP_PUSH_MO_ROUTE_UPDATE）操作
CMPP_PUSH_MO_ROUTE_UPDATE操作的目的是使GNS可向ISMG更新MO路由信息。ISMG以CMPP_PUSH_MO_ROUTE_UPDATE_RESP 消息回应。
8.6.10.1	CMPP_PUSH_MO_ROUTE_UPDATE消息定义（GNSISMG）
字段名	字节数	属性	描述
Update_type	1	Unsigned Integer	0：添加；
1：删除；
2：更新
Route_Id	4	Unsigned Integer	路由编号
Destination_Id	6	Octet String	目标网关代码
Gateway_IP	15	Octet String	目标网关IP地址
Gateway_port	2	Unsigned Integer	目标网关IP端口
SP_Id	6	Octet String	SP的企业代码
SP_Code	21	Octet String	SP的服务号码
SP_AcessType	1	Unsigned Integer	SP接入类型
0：全网业务SP全网接入，即接入网关为SP的主力接入点
1：全网业务SP镜像接入，即接入网关为SP的镜像接入点
Service_Id	10	Octet String	请求的业务类型（此项适合全网服务内容，如梦网卡图片传情,如该路由不包含此业务，此字段为空）
Start_code	4	Unsigned Integer	MO路由起始业务代码（如果未置请求的Service_Id字段，此字段为空）
End_code	4	Unsigned Integer	MO路由截止业务代码（如果未置请求的Service_Id字段，此字段为空）
time_stamp	14	Octet String	本路由信息的最后修改时间
格式是：yyyymmddhhmmss，
例如20030117014512

8.6.10.2	CMPP_PUSH_MO_ROUTE_UPDATE_RESP消息定义（ISMG  GNS）
字段名	字节数	属性	描述
Result	1	Unsigned 
Integer	0：成功更改
5：路由信息更新失败
6：汇接网关路由信息时间戳比本地路由信息时间戳旧
9：系统繁忙

8.7	系统定义
8.7.1	Command_Id定义
消息	Command_Id值	说明
CMPP_CONNECT	0x00000001	请求连接
CMPP_CONNECT_RESP	0x80000001	请求连接应答
CMPP_TERMINATE	0x00000002	终止连接
CMPP_TERMINATE_RESP	0x80000002	终止连接应答
CMPP_SUBMIT		0x00000004	提交短信
CMPP_SUBMIT_RESP	0x80000004	提交短信应答
CMPP_DELIVER	0x00000005	短信下发
CMPP_DELIVER_RESP	0x80000005	下发短信应答
CMPP_QUERY	0x00000006	发送短信状态查询
CMPP_QUERY_RESP	0x80000006	发送短信状态查询应答
CMPP_CANCEL		0x00000007	删除短信
CMPP_CANCEL_RESP	0x80000007	删除短信应答
CMPP_ACTIVE_TEST	0x00000008	激活测试
CMPP_ACTIVE_TEST_RESP	0x80000008	激活测试应答
CMPP_FWD	0x00000009	消息前转
CMPP_FWD_RESP	0x80000009	消息前转应答
CMPP_MT_ROUTE	0x00000010	MT路由请求
CMPP_MT_ROUTE_RESP	0x80000010	MT路由请求应答
CMPP_MO_ROUTE	0x00000011	MO路由请求
CMPP_MO_ROUTE_RESP	0x80000011	MO路由请求应答
CMPP_GET_MT_ROUTE	0x00000012	获取MT路由请求
CMPP_GET_MT_ROUTE_RESP	0x80000012	获取MT路由请求应答
CMPP_MT_ROUTE_UPDATE	0x00000013	MT路由更新
CMPP_MT_ROUTE_UPDATE_RESP	0x80000013	MT路由更新应答
CMPP_MO_ROUTE_UPDATE	0x00000014	MO路由更新
CMPP_MO_ROUTE_UPDATE_RESP	0x80000014	MO路由更新应答
CMPP_PUSH_MT_ROUTE_UPDATE	0x00000015	MT路由更新
CMPP_PUSH_MT_ROUTE_UPDATE_RESP	0x80000015	MT路由更新应答
CMPP_PUSH_MO_ROUTE_UPDATE	0x00000016	MO路由更新
CMPP_PUSH_MO_ROUTE_UPDATE_RESP	0x80000016	MO路由更新应答
CMPP_GET_MO_ROUTE	0x00000017	获取MO路由请求
CMPP_GET_MO_ROUTE_RESP	0x80000017	获取MO路由请求应答

8.7.2	错误码使用说明
目前CMPP2.0中定义了响应消息中的10个返回码，其中0表示成功，1-9分别表示各种错误类型，各厂家在返回错误码时应尽可能向这9个错误码靠拢，当然，因为设计的原因或者查错的方便，厂家可以在规定的厂家自定义错误码空间中定义自己的错误码，但是，定义错误码时不允许存在类似与“其它错误”或者“系统错误”这类含义模糊的情况，即各厂家能够对自己返回的每一个错误码给出明确的含义解释，以利于错误发生时的定位工作。0－99为CMPP协议保留的错误码空间，请各厂家在预留的错误码空间范围：100－199内自行定义，并提供详细的编码解释列表。
8.7.3	ISMG与GNS之间消息使用的错误码定义

0：系统操作成功；
1：没有匹配路由；
2：源网关代码错误；
3：路由类型错误；
4：本节点不支持更新（GNS分节点）；
5：路由信息更新失败；
6：汇接网关路由信息时间戳比本地路由信息时间戳旧；
9：系统繁忙；

10：Update_type错误；
11：路由编号错误；
12：目的网关代码错误；
13：目的网关IP错误；
14：目的网关Port错误；
15：MT路由起始号码段错误；
16：MT路由截止号码段错误；
17：手机所属省代码错误；
18：用户类型错误；
19：SP_Id错误；
20：SP_Code错误；
21：SP_AccessType错误；
22：Service_Id错误；
23：Start_code错误；
24：End_code错误。

100~199：厂家自定义错误码空间。
8.7.4	GNS上路由信息的Route_Id的编号规则
按运营商、MO/MT分类，由0开始往上分配。 

9	附录1 短信群发功能的实现
目前拟定按如下方式实现MT短信的群发功能（对SMC暂不实现群发）：
SP侧ISMG接收到CMPP_SUBMIT消息后，判断是否为群发的MT短信，如果为群发的SMC，则返回错误；如果为其它类型的操作，仍旧按照以前的业务流程进行。如果是群发的MT短信（假设群发目的号码数为n），则将该消息拆包为n条CMPP_Fwd消息或直接发给SMSC的SUBMIT_SM消息，其中计费规则（对谁计费和计费多少）与单条MT短信的计费规则相同。返回给SP的CMPP_SUBMIT_RESP消息中的Msg_Id则暗示一个范围，即Msg_Id ～ Msg_Id＋（n－1）分别对应n个目的手机用户的信息标识，那么在返回的状态报告中可以依据Msg_Id ～ Msg_Id＋（n－1）范围内的数字匹配出群发短信中的每一个短信发送请求。需要注意的是，Msg_Id ～ Msg_Id＋（n－1）范围内的信息标识不能被随后的其它CMPP_SUBMIT_RESP消息使用。
在Msg_Id中的“序列号”如下定义：“序列号：bit16~bit1，顺序增加，步长为1，循环使用。”，那么如果在Msg_Id ～ Msg_Id＋（n－1）范围中仍然依据此规则，如果其中“序列号”到达最大值，则从0开始循环使用。
增加此短信群发功能，实际上等同于SP把以前分为n次发送的短信合并到一条消息中，但是SP仍然应该把此一条消息当作n条短信提交请求，并相应记录n条话单，SP拿n条话单与移动公司进行对帐和结算。
在短信群发的消息中，仍然需要判断消息中的计费号码、所有的目的号码的合法性，SP应该保证其中每个号码都是合法的，如果其中任何一个号码有错误，接入ISMG对此消息判断为错误，并给SP返回错误，不对其中任何号码进行发送处理。
接入ISMG对SP下发的短信的流量具有限制功能，对SP流量的统计对短信群发来说，应该计算为n条短信。

10	附录2 GNS协议目前实现说明
1、	MT路由信息中的Start_Id和End_Id应该相同，格式均应该为13XH0H1H2H3（其中X为5、6、7、8、9）；
2、	MO路由信息目前主要根据Source_Id（源网关代码）和SP_Code（SP的服务代码）决定，Service_Id和Service_Code目前尚未使用；
3、	Route_Id的编号目前仅按照MO/MT进行分别编号，并且MO和MT类的路由Route_Id均从0开始编号。
 

11	修订历史
版本号	时间	主要内容或重大修改
CMPP V1.2.1	2001.6	
CMPP V2.0	2002.4	1.	修改了Msg_Id的生成算法；
2.	明确了有关短信群发的问题；
3.	CMPP_MO_ROUTE_RESP中的SP_CODE改为SP_Id（SP企业代码）；
4.	ISMG与GNS交互的消息中Area_Code含义定义为省代码，用省会城市区号表示；
5.	对Service_Id字段的要求放宽，可以是数字、字母和符号的组合；
6.	明确Dest_terminal_Id字段允许在用户终端号码前加“86”或“+86”；
7.	规定网关SP之间、网关之间消息发送等待确认时间暂定为60秒，超过则认为超时需要重发两次；
8.	规定了对于包月的SMC消息，应向SP返回成功与否的状态报告，若成功Stat值为DELIVRD，失败Stat值为UNDELIV；
9.	明确状态报告中ACCEPTED为中间状态，网关收到后应丢弃不做任何操作；
10.	修改了CMPP_ACTIVE_TEST_RESP的消息格式；
11.	增加了MO状态报告的格式、流程；
12.	在缩略语中增加了一些定义，改正了一些文字上前后不一致的地方，进行了版面调整；
13.	增加了网关在异常情形下的MO/MT状态报告的产生机制；
14.	对原协议中的端口号作了重新规定。
CMPP V2.1	2003.5	1、取消“源ISMG”和“目的ISMG”的说法，把用户归属的ISMG简称为“归属ISMG”，把SP接入的ISMG简称为“接入ISMG”，如果“归属ISMG”和“接入ISMG”为同一个ISMG，则用“归属/接入ISMG”或者“接入/归属ISMG”来代称；但在泛称时仍可能使用“源ISMG”和“目的ISMG”；
2、明确SP与ISMG之间、ISMG与ISMG之间的连接建议为采用长连接方式；
3、明确SP与ISMG之间的连接方式：SP为客户端、ISMG为服务器端；
4、取消CMPP_SUBMIT中对短信群发必须在SP不要求状态报告时的限制；
5、状态报告中增加了MA:xxxx、MB:xxxx、CA:xxxx、CB:xxxx的状态值；
6、明确CMPP_FWD消息中的Src_Id和Dest_Id字段的填写情况，原则上不再允许状态报告中填写空值；
7、更新了GNS协议的所有消息，增加了GNS协议相关的系统定义（CommandId、错误码、Route_Id的编号规则）；
8、添加了对错误码使用范围的说明；
9、删除了原“附录1 MO状态报告的产生”，因为MO状态报告在《移动梦网短信业务信令流程规范》中已经进行详细的说明；
10、因为要求ISMG对CMPP_SUBMIT中的各字段进行更详细的判断，添加了CMPP_SUBMIT_RESP中的返回码的取值范围；
11、删除了原CMPP_FWD中对状态报告的产生流程的描述，因为这些流程在《移动梦网短信业务信令流程规范》中已经进行了详细的说明；

CMPP V2.1.0	2003-6-23	1、按照标准格式进行修改
2、CMPP_SUBMIT：Msg_Id字段的描述：删除“由SP接入的短信网关本身产生，本处填空。”，直接描述为“信息标识”。
3、CMPP_SUBMIT：Fee_terminal_Id字段的描述：删除“如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥”，修改为“当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义”。
4、CMPP_FWD：Fee_terminal_Id字段的描述：删除“如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥”，修改为“当Fee_UserType为3时该值有效，当Fee_UserType为0、1、2时该值无意义”。
15、CMPP_SUBMIT、CMPP_DELIVER、CMPP_FWD消息的Msg_Length字段：添加“取值大于或等于0”的限制。
CMPP V3.0.0	2003-6-30	1、删除CMPP_SUBMIT、CMPP_DELIVER、CMPP_FWD消息中的Reserve字段，添加LinkID字段；（20个字节长字符串类型）；
2、CMPP_SUBMIT消息：增加Fee_terminal_type字段，表明Fee_terminal_Id是真实用户号码还是伪码；
3、CMPP_SUBMIT消息：扩展Fee_terminal_Id长度为32字节，适应伪码的长度需求，并把其类型从Unsigned Integer修改为Octet String。
4、CMPP_SUBMIT消息：增加Dest_terminal_type字段，表明Dest_terminal_Id是真实用户号码还是伪码；
5、CMPP_SUBMIT消息：扩展Dest_terminal_Id的单元长度为32字节，适应伪码的长度需求。
6、CMPP_DELIVER消息：增加Src_terminal_type字段，表明Src_terminal_Id是真实用户号码还是伪码；
7、CMPP_DELIVER消息：扩展Src_terminal_Id的单元长度为32字节，适应伪码的长度需求。
8、CMPP_FWD消息：增加Fee_terminal_Pseudo字段，32个字节长；
9、CMPP_FWD消息：增加Src_Id_Pseudo字段，32个字节长；
10、CMPP_FWD消息：增加Src_Id_type字段，表明MO时传递给SP的到底是真实号码还是伪码；
11、CMPP_FWD消息：增加Dest_Id_Pseudo字段，32个字节长。
12、CMPP_FWD消息：增加Fee_terminal_UserType字段；
13、CMPP_FWD消息：增加Src_UserType字段；
14、CMPP_FWD消息：增加Dest_UserType字段；
15、CMPP_FWD消息：增加FeeType的值：06表明包月查询。
16、把所有响应消息中的状态码字段从1个字节扩展为4个字节，包括：
    CMPP_CONNECT_RESP消息中的status字段；
    CMPP_SUBMIT_RESP消息中的Result字段；
    CMPP_DELIVER_RESP消息中的Result字段；
    CMPP_CANCEL_RESP消息中的Success_Id字段；
    CMPP_FWD消息中的Result字段；
    GNS部分相关协议不作修改。
17、CMPP_SUBMIT消息：Register_Delivery字段删除对2（产生SMC话单）的支持；
18、对于3.0版本CMPP，要求CMPP_CONNECT消息和CMPP_CONNECT_RESP消息中的Version字段表示3.0版本；
19、把Service_Id的名称从“业务类型”改为“业务标识”；
20、删除CMPP中用于ISMG之间短连接的端口号，不再允许ISMG之间采用短连接；


21、修改“3 术语和定义”中对SP_Code的说明，添加“106x”前缀作为SP服务代码的说明；
22、删除“3 术语和定义”中对SMC的说明，SP不能再发送SMC消息进行包月费用的收取；
23、在“3 术语和定义”中添加对DSMP的说明；
24、修改了“4 网络结构”中的“互联网短信网关组网结构”图，在图中添加了DSMP的网元；
25、把“5 CMPP功能概述”中的流程描述，流程描述在相应的流程规范中有更详细的阐述；
26、修改“7 通信方式”中的描述，明确要求ISMG之间必须采用长连接，SP与ISMG之间建议采用长连接；
27、CMPP_SUBMIT消息中，FeeType字段：删除对04（封顶计费）和05（对SP计费）的支持；
28、CMPP_DELIVER消息传递内容为状态报告时，Msg_Content中的Dest_terminal_Id字段的长度从21个字节扩充为32个字节；
29、CMPP_DELIVER消息中传递内容为状态报告时，Msg_Content中的Stat字段的范围扩充了，把所有网元之间交互的错误情况均列在该字段中传递，文档作了相应的修改；
30、CMPP_FWD消息的FeeType字段：删除对00（核减对称信道费）、04（封顶计费）和05（对SP计费）的方式的支持；
31、CMPP_FWD消息传递MO状态报告时，Msg_Content中的Stat字段的取值定义进行了修改，说明请参见相应的修改内容。






