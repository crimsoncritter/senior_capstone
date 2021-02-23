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

def login(email, password):
    user = auth.sign_in_with_email_and_password(email, password)
    return user["idToken"]

@app.route('/user', methods=['POST'])
def create_user():
    content = request.get_json()
    fb_data = {}
    fb_data["role"] = content["role"]
    fb_data["first_name"] = content["first_name"]
    fb_data["last_name"] = content["last_name"]
    fb_data["email"] = content["email"]
    fb_data["password"] = content["password"]

    if fb_data["role"] == "employee":
        fb_data["working"] = True
        fb_data["store_id"] = content["store_id"]

    auth.create_user_with_email_and_password(fb_data["email"], fb_data["password"])
    db.child("users").push(fb_data)
    print(fb_data)

    return login(fb_data["email"], fb_data["password"])

@app.route('/user/auth/init', methods=['POST'])
def auth_user():
    print("Yes")
    content = request.get_json()
    email = content["email"]
    password = content["password"]
    return login(email, password)

@app.route('/user/auth/expire', methods=['POST'])
def expire_user(auth_data):
    content = request.json
    # firebase.auth()

if __name__ == '__main__':
    app.run(debug=True)
