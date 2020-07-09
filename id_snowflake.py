#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-09 14:29
Desc   :

snowflake_start_server [--dc=DC_ID] [--worker=WORKER_ID] [--host=ADDRESS] [--port=PORT]

"""

import snowflake.client
from start_snowflake_server import start_snowflake

ip_address = start_snowflake.ip_address
port = start_snowflake.port
snowflake.client.setup(ip_address, port)


def gen():
    return snowflake.client.get_guid()


#%%
print(snowflake.client.get_stats())


if __name__ == '__main__':
    for i in range(10):
        print(gen())
