#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-10 11:10
Desc   :
"""
import datetime
import snowflake.client
import socket
import json

snowflake.client.setup(socket.gethostbyname(socket.gethostname()), 8002)


# %%  计算需要添加的时间戳
def cal_sub_timestamp():
    date_bin = bin(snowflake.client.get_guid())[0:42]
    timestamp = snowflake.client.get_stats()['timestamp']
    sub_date_timestamp = timestamp - int(date_bin, 2)
    print('sub_date_timestamp', int(sub_date_timestamp))
    sub_date = datetime.datetime.fromtimestamp(sub_date_timestamp / 1000)
    print('相差的日期：', sub_date)


def gen_id():
    return snowflake.client.get_guid()


def get_id_date(id_snowflake):
    date_bin = bin(id_snowflake)[:42]
    date_bin_add = int(date_bin, 2) + 550281600003
    dt = datetime.datetime.fromtimestamp(date_bin_add / 1000).strftime('%Y%m%d %H:%M:%S.%f')[:-3]
    return dt


def get_id_dc(id_snowflake):
    return int(bin(id_snowflake)[42:44], 2)


def get_id_worker(id_snowflake):
    return int(bin(id_snowflake)[44:52], 2)


def get_id_num(id_snowflake):
    return int(bin(id_snowflake)[52:], 2)


def parse(id_snowflake):
    return {'id_snow_raw': id_snow,
            'date': get_id_date(id_snow),
            'data_center': get_id_dc(id_snow),
            'worker': get_id_worker(id_snow),
            'serial_num': get_id_num(id_snow)}


if __name__ == '__main__':
    # cal_sub_timestamp()
    id_snow = gen_id()
    print('id:', id_snow)

    id_parse = parse(id_snow)
    print(json.dumps(id_parse, ensure_ascii=False, indent=4))
