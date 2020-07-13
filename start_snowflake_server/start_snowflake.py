#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-09 15:04
Desc   :
生成的数据由于64位二进制的组成，并转成十进制。 随机码为12位，单个work、单个dc、单个毫秒内可以产生4096个订单。
1    41      2       8     12
无  时间戳  数据中心 worker数 随机码
"""

import socket
import os

port = 8002

# %%
try:
    ip_address = socket.gethostbyname(socket.gethostname())
    dc = 1  # 取值范围1-3
    worker = ip_address[-1]  # 取值范围1-255
except Exception as e:
    print(f'start_snowflake error:{e}')
    ip_address = '127.0.0.1'
    dc = worker = ip_address[-1]

# %%
seq = f'snowflake_start_server --dc={dc} --worker={worker} --address={ip_address} --port={port}'

try:
    os.system(seq)
except Exception as e:
    print(f'os error:{e}')
