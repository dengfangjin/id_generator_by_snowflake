#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-09 15:04
Desc   :
"""

import socket
import os

port = 8002

# %%
try:
    ip_address = socket.gethostbyname(socket.gethostname())
    dc = worker = ip_address[-1]
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
