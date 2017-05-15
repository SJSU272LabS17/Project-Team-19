from flask import Flask, render_template, json, request, redirect
from flask.ext.mysql import MySQL
from flask import session
from FormatInput import formatInput
from test import xyz

mysql = MySQL()
app = Flask(__name__)

# MySQL configurations
app.config['MYSQL_DATABASE_USER'] = 'root'
app.config['MYSQL_DATABASE_PASSWORD'] = 'Apple@000'
app.config['MYSQL_DATABASE_DB'] = 'test'
app.config['MYSQL_DATABASE_HOST'] = 'localhost'
mysql.init_app(app)


@app.route('/')
def main():
    return render_template('test.html')


@app.route('/showSignUp')
def showSignUp():
    return render_template('signup.html')

@app.route('/showSignin')
def showSignin():
    return render_template('signin.html')

@app.route('/signUp', methods=['POST', 'GET'])
def signUp():
    try:
        _name = request.form['inputName']
        _email = request.form['inputEmail']
        _password = request.form['inputPassword']

        # validate the received values
        if _name and _email and _password:

            # All Good, let's call MySQL

            conn = mysql.connect()
            cursor = conn.cursor()

            cursor.callproc('sp_createUser', (_name, _email, _password))
            data = cursor.fetchall()

            if len(data) is 0:
                conn.commit()
                return json.dumps({'message': 'User created successfully !'})
            else:
                return json.dumps({'error': str(data[0])})
        else:
            return json.dumps({'html': '<span>Enter the required fields</span>'})

    except Exception as e:
        return json.dumps({'error': str(e)})
    finally:
        cursor.close()
        conn.close()

@app.route('/userHome')
def userHome():

    if session.get('user'):
        return
    else:
        return render_template('error.html',error = 'Unauthorized Access')

@app.route('/logout')
def logout():
    session.pop('user',None)
    return redirect('/')

@app.route('/validateInput', methods=['POST', 'GET'])
def homePage():
    try:
        _Name = request.form['Name']
        _DOB = request.form['DOB']
        _PlateNo = request.form['PlateNo']
        _Zip_Code = request.form['Zip_Code']
        _Experience = request.form['Experience']
        _Mileage = request.form['Mileage']
        _Gender = request.form['Gender']
        _Marital_Status = request.form['Marital_Status']

        _DOB = int(_DOB[:4])
        _age = 2017 - _DOB

        if _Gender == "female":
            _intGender = 1
        else:
            _intGender = 2

        if _Marital_Status == "Married":
            _intMaritalStatus = 1
        else:
            _intMaritalStatus = 2

        print "Before----- "
        temp = formatInput(_age,3,int(_Experience),int(_Zip_Code),int(_Mileage),int(_intGender),int(_intMaritalStatus))

        print "temp "
        print temp[0]
        if temp[0] == 1:
            #progressColor = "progress-bar progress-bar-success"
            risk = 30
            riskp = "width:30%"
            text = "Low Risky"
        elif temp[0] == 2:
            #progressColor = "progress-bar progress-bar-warning"
            risk = 60
            riskp = "width:60%"
            text = "Moderate Risky"
        elif temp[0] == 3:
            #progressColor = "progress-bar progress-bar-danger"
            risk = 90
            riskp = "width:90%"
            text = "High Risky"

        return render_template('Output.html', name = _Name, risk = risk, riskp = riskp,text = text)


    except Exception as e:
        return json.dumps({'error': str(e)})



@app.route('/validateDriverInput', methods=['POST', 'GET'])
def validateDriverInput():
    try:
        print "------- INSIDE -----"
        _Name = request.form['select']
    except Exception as e:
        return json.dumps({'error': str(e)})



@app.route('/validateLogin', methods=['POST'])
def validateLogin():
    try:
        _username = request.form['inputEmail']
        _password = request.form['inputPassword']

        # connect to mysql

        con = mysql.connect()
        cursor = con.cursor()
        cursor.callproc('sp_validateLogin', (_username,))
        data = cursor.fetchall()


        if len(data) > 0:
            if str(data[0][3])== _password:
                #session['user'] = data[0][0]
                return render_template('UserHomeTest.html',name = data[0][1])
            else:
                return render_template('error.html', error='Wrong Email address or Password.')
        else:
            return render_template('error.html', error='Wrong Email address or Password.')




    except Exception as e:
        return render_template('error.html', error=str(e))
    finally:
        cursor.close()
        con.close()

if __name__ == "__main__":

    app.run(port=5001,debug=True)
