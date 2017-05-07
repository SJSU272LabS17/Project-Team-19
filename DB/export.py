import MySQLdb
import sys
import csv

db = MySQLdb.connect("localhost","root","*****","DB_DriverRiskAnalysis" )
dbQuery= "Select *from DB_DriverRiskAnalysis.Training_Set"
cur=db.cursor()
cur.execute(dbQuery)
rows = cur.fetchall()
fp = open('/home/ubuntu/file.csv', 'w')
myFile = csv.writer(fp)
myFile.writerows(rows)
fp.close()
