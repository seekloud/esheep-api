#esheep API设计文档 2018.11


### 概述
1. 序列化使用protobuf
2. 通信服务使用gRPC


### 基础概念
1. room: 一个模拟环境，同一个room下的player在同一个模拟环境内进行比赛；
2. player: 参与虚拟空间进行比赛的角色;
3. action: player可以使用行为;
4. observation: 可以获取到的包括所有player的模拟环境信息;
5. information: 分数，击杀，死亡等信息;
6. reward: 一个分数奖励;
7. 背景: 不可变，并且不可交互的元素
8. 物品: 可变，或者可交互的元素
9. episode:  


### 主要流程
1. 创建房间
2. 加入房间
3. 提交action(s)
4. 获得observation
5. 获取information
6. 死亡后复活
7. 退出



### 动作
1. 移动方向（move）
    * 一共八种输入：←↑→↓↖↙↗↘
    * 八选一；
    * 表示键盘的方向按键；
2. 鼠标指针移动（swing）
    * 一个二元向量（r，d）
    * r表示朝向：(-pi，pi]
    * d表示距离：[0, Dmax], Dmax为预先定义好的移动最大距离；
    * 表示以鼠标指针当前位置为起点，按照朝着r方向，移动距离d；
3. 激发（fire）
    * 一个状态量，
    * 取值范围0，1，2，3，4，等等；
    * 0表示无激发；
    * 表示攻击，投掷，加速等等主要的激发操作；
    * 通常对应于鼠标左键，右键，键盘空格等主操作；
4. 功能（function）
    * 一个状态量
    * 取值范围0，1，2，3，4，等等；
    * 0表示不使用功能；
    * 表示，分裂，喂食，使用技能，使用道具，等辅助功能操作；
    * 通常对应于键盘的E，F，1，2，3，4等辅助操作；


### 观察数据 (Observation)
1. 分层视图
    * 视野在整个地图中的位置
    * 视野内不可交互的元素（地图背景元素）
    * 视野内可交互的元素（物品、道具、子弹等）
    * 视野内包括自己的所有玩家
    * 视野内的自己
    * 面板状态信息图层
2. 人类视图(可选)


### 互动信息 (Information)
1. 击杀
2. 得分
3. 受伤
4. 被杀







### Copyright and License Information

---
Copyright 2018 seekloud (https://github.com/seekloud)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.







