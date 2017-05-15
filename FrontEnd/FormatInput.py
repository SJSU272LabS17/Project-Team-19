import random
from AssesRisk import *
import zipcode #pip install zipcode


def formatInput(age,PlateNo,Experience,Zip_Code,Mileage,Gender,Marital_Status):
    #['DMV', 'VehicleType', 'Age', 'Location', 'CreditScore','DrivingExperience','AnnualMileage','Gender','MaritalStatus','RiskCategory']
    print age
    print PlateNo
    print Experience
    print Zip_Code
    print Mileage
    print Gender
    print Marital_Status
    dmvScore = generateDmv()
    print dmvScore
    creditScore = generateCredit()
    print creditScore
    city = ''
    state = ''
    #3,3,40,90013,535,480,28000,2,2,2
    #zip = zipcode.isequal(str(Zip_Code))
    #print zip
    print "dmv: ", dmvScore
    print "credit: ", creditScore
    input = [dmvScore,PlateNo,age,Zip_Code,creditScore,Experience,Mileage,Gender,Marital_Status]
    result = predictRisk(input)
    print result
    listVal = list()
    #returnVals = riskscore; dmvscore; creditscore; state corresponding to zip, city
    #returnVals = str(result) + ";" + str(dmvScore) + ";" + str(creditScore) + ";" + str(state) + ";" + str(city)
    return result


def generateDmv():
    for x in range(10):
        return random.randint(1,12)


def generateCredit():
    return random.randrange(100,800)

#FOR TEST
#age,PlateNo,Experience,Zip_Code,Mileage,Gender,Marital_Status
#input = [40,3,4,90013,28000,2,2]
#formatInput(31,3,24,93125,30000,2,2)



