#!/usr/bin/python
# coding=utf-8
import sys
import os
import MySQLdb
import requests

reload(sys)
sys.setdefaultencoding('utf8')


class Work:
    def __init__(self, cols):
        if len(cols) != 9:
            print len(cols), "column error"
            sys.exit()
        self.id = 0
        self.rid = 0
        self.url = ''
        self.fmt = ''
        self.no = cols[0]
        self.stg = cols[1]
        self.sbj = cols[2]
        self.user = cols[3]
        self.name = cols[4]
        self.awd = cols[5]
        self.prv = cols[6]
        self.cty = cols[7]
        self.dst = cols[8]

    def fn(self):
        return self.user + '_' + self.name + '.' + self.fmt


def res(works):
    conn = MySQLdb.connect(host="db0.zgjiaoyan.com", port=3307, user='yxdev', passwd="z1Y#5*76$G23", db='workselect',
                           charset='utf8')
    cursor = conn.cursor()
    for work in works:
        sql = "SELECT w.resource_id FROM works w, participant p WHERE w.work_name LIKE '%%%s%%' \
               AND w.check_status = 1 AND w.activity_id = p.activity_id AND w.author_id = p.user_id AND p.user_name = '%s'" \
              % (work.name, work.user)
        print sql
        cursor.execute(sql)
        rows = cursor.fetchall()
        if len(rows) != 1:
            print len(rows), ' rows error'
            sys.exit()
        work.rid = rows[0][0]
        url = "http://search.api.yanxiu.com:8090/?interf=PcCommunityRid&res_id=%s&source_id=5,6,7,8,9" % work.rid
        r = requests.get(url)
        jo = r.json()
        work.url = jo['data'][0]['res_download_url']
        work.fmt = jo['data'][0]['res_type']
        print work.fn()


def read(fn):
    fp = open(fn)
    lines = fp.readlines()
    works = []
    for line in lines:
        cols = line.split(',')
        work = Work(cols)
        works.append(work)
    print len(lines), 'read'
    return works


def down(works):
    for work in works:
        cmd = "wget '%s' -O '%s'" % (work.url, work.fn())
        print cmd
        os.system(cmd)
        #size logging n retry


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print 'python download.py works.csv'
        sys.exit()
    wks = read(sys.argv[1])
    res(wks)
    down(wks)
