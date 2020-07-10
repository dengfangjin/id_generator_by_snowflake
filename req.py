#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-09 15:38
Desc   :
"""

import requests

url = r'http://127.0.0.1:8000/idgen'
# TODO # 不知道为什么localhost这个很慢？？
# url = r'http://localhost:8000/idgen'


# for i in range(10):
#     try:
#         res = requests.get(url)
#         print(res.text)
#     except Exception as e:
#         print(e)

for i in range(10):
    res = requests.get(url)
    print(res.text)
