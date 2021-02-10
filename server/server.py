from os import path, system
from flask import Flask, jsonify, request, redirect, url_for
import pyrebase
import server_init

firebase_config = server_init.get_firebase_config()
firebase = pyrebase.initialize_app(firebase_config)
db = firebase.database()
server_init.setup_db(db)
auth = firebase.auth()

app = Flask(__name__)

@app.route('/user', methods=['POST'])
def create_user(user_data):
    content = request.json

@app.route('/user/auth/init', methods=['POST'])
def auth_user():
    content = request.get_json()
    email = content["email"]
    password = content["password"]
    user = auth.sign_in_with_email_and_password(email, password)
    return user["idToken"]

@app.route('/user/auth/expire', methods=['POST'])
def expire_user(auth_data):
    content = request.json
    # firebase.auth()

if __name__ == '__main__':
    app.run(debug=True)
