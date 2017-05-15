import random
from AssessRisk import *
import zipcode #pip install zipcode


def formatInput(age,PlateNo,Experience,Zip_Code,Mileage,Gender,Marital_Status):
    #['DMV', 'VehicleType', 'Age', 'Location', 'CreditScore','DrivingExperience','AnnualMileage','Gender','MaritalStatus','RiskCategory']
    dmvScore = generateDmv()
    creditScore = generateCredit()
    print "dmv: ", dmvScore
    print "credit: ", creditScore
    #zip = Zip_Code.isequal(Zip_Code)
    zip = zipcode.isequal('95129')
    state = zip.state
    city =  zip.city
    input = [dmvScore,PlateNo,age,Zip_Code,creditScore,Experience,Mileage,Gender,Marital_Status]
    #input = [dmvScore,4,70,95129,creditScore,2,30000,2,2]
    result = predictRisk(input)
    #returnVals = riskscore; dmvscore; creditscore; state corresponding to zip, city
    returnVals = str(result) + ";" + str(dmvScore) + ";" + str(creditScore) + ";" + str(state) + ";" + str(city)
    return returnVals


def generateDmv():
    for x in range(10):
        return random.randint(1,12)


def generateCredit():
    return random.randrange(100,800)



