from flask import Flask, render_template

app = Flask(__name__)

@app.route('/')
def home():
  return render_template("index.html")

@app.route('/store')
def store():
  return render_template("store.html")

@app.route('/access')
def access():
  return render_template("access.html")

@app.route('/login')
def login():
  return render_template("login.html")

@app.route('/add_employee')
def add_employee():
  return render_template("add_employee.html")

if __name__ == '__main__':
  app.run(debug=True)
