from flask import Flask, render_template,request,redirect,url_for
import random
app = Flask(__name__)

@app.route("/")
def main():
    return "Welcome!"

@app.route("/enterDetails")
def showForm():
    return render_template('inputs.html')

@app.route("/getScore", methods=['POST'])
def getRiskScore():
    if request.method == 'POST':
        dmvPoint = request.form['inputDmv']
        if(dmvPoint == ""):
            dmvPoint = generateRandom()
        vehicleType = request.form['inputVtype']
        age = request.form['inputAge']
        location = request.form["inputLoc"]
        #data = {'dmv' : dmvPoint, 'vehicleType' : vehicleType, 'age' : age, 'location' : location}
        return redirect(url_for('showOutput', result=dmvPoint))

@app.route("/showScore")
def showOutput():
    res = request.args.get('result', None)
    return render_template('output.html',result=res)

def generateRandom():
    for x in range(10):
        return random.randint(1,12)



if __name__ == "__main__":
    app.run()


