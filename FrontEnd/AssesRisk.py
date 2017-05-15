# Load libraries
import pandas as pd
import numpy as np
from pandas.tools.plotting import scatter_matrix
import matplotlib.pyplot as plt
from sklearn import model_selection
from sklearn.metrics import classification_report
from sklearn.metrics import confusion_matrix
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis

def predictRisk(input):
 	#Load dataset
	url = '/Volumes/Macintosh HD/SJSU/272/Project-Team-19/ML/Data.csv'
	#names = ['dmv', 'vehicle_type', 'age', 'location', 'credit_score','driving_exp','annual_mileage','gender','marital_status','risk_category']
	names = ['DMV', 'VehicleType', 'Age', 'Location', 'CreditScore','DrivingExperience','AnnualMileage','Gender','MaritalStatus','RiskCategory']

	print "INSIDE PredictRisk"

	#get pandas dataframe and convert it into numpy array
	datafr = pd.read_csv(url)
	array = np.array(datafr)
	print "INSIDE PredictRisk-----"
	# Split-out validation dataset
	X = array[:,0:9]
	Y = array[:,9]
	validation_size = 0.00
	seed = 7
	print "----Check ---"
	X_train, X_validation, Y_train, Y_validation = model_selection.train_test_split(X, Y, test_size=validation_size, random_state=seed)

	print "----Check1 ---"
	lda = LinearDiscriminantAnalysis()
	print "----Check2---"
	lda.fit(X_train,Y_train)
	print "----Check3---"
	print input
	result = lda.predict(input)

	print "INSIDE PredictRisk9999999"
	print result
	return result

