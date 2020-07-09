#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
Project: id_generator
Author : Advance
Time   : 2020-07-09 14:23
Desc   :
"""
import flask
from id_snowflake import gen

app = flask.Flask(__name__)


@app.route('/idgen', methods=['GET'])
def id_generator():
    id_num = gen()
    print(id_num)
    with open('id.txt', 'a') as f:
        f.write(str(id_num) + '\n')
    return str(id_num)


if __name__ == '__main__':
    app.run('0.0.0.0', port=8000, debug=1)
