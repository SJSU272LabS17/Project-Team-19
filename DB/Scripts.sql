CREATE table DB_DriverRiskAnalysis.Training_Set
(
	DMV Int, 
	VehicleType Int,
	Age Int, 
	Location Int, 
	CreditScore Int, 
	DrivingExperience Int,
	AnnualMileage Int,
	Gender Int,
	MaritalStatus Int,
	RiskCategory Int
);
	
load data local infile 'Data.csv' into table DB_DriverRiskAnalysis.Training_Set fields terminated by ','  
  lines terminated by '\n'
    (	DMV , 
	VehicleType ,
	Age , 
	Location , 
	CreditScore , 
	DrivingExperience ,
	AnnualMileage ,
	Gender ,
	MaritalStatus ,
	RiskCategory )	
