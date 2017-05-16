import random
from AssesRisk import *


def formatInput(age,PlateNo,Experience,Zip_Code,Mileage,Gender,Marital_Status):
    #['DMV', 'VehicleType', 'Age', 'Location', 'CreditScore','DrivingExperience','AnnualMileage','Gender','MaritalStatus','RiskCategory']
    dmvScore = generateDmv()
    creditScore = generateCredit()
    vehicleScore = generateVehicle()
    #3,3,40,90013,535,480,28000,2,2,2
    #zip = zipcode.isequal(str(Zip_Code))
    #if(zip):
        #state = zip.state
        #city =  zip.city
        #print city,state
    print "dmv: ", dmvScore
    print "credit: ", creditScore
    input = [dmvScore,vehicleScore,age,Zip_Code,creditScore,Experience,Mileage,Gender,Marital_Status]
    result = predictRisk(input)
    print result
    result.append(dmvScore)
    result.append(creditScore)
    result.append(vehicleScore)
    result.append(Experience)
    result.append(Mileage)
    print result
    #returnVals = riskscore; dmvscore; creditscore; state corresponding to zip, city
    #returnVals = str(result) + ";" + str(dmvScore) + ";" + str(creditScore) + ";" + str(state) + ";" + str(city)
    return result


def generateDmv():
    for x in range(10):
        return random.randint(1,12)

def generateCredit():
    return random.randrange(100,800)

def generateVehicle():
    return random.randrange(1,5)

#FOR TEST
#age,PlateNo,Experience,Zip_Code,Mileage,Gender,Marital_Status
#input = [40,3,4,90013,28000,2,2]
#formatInput(31,3,24,95129,30000,2,2)



