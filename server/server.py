from os import path, system
from flask import Flask, jsonify, request, redirect, url_for
import pyrebase
import server_init
import uuid
import psycopg2
import postgres_dao as dao

firebase_config = server_init.get_firebase_config()
firebase = pyrebase.initialize_app(firebase_config)
db = firebase.database()
server_init.setup_db(db)
auth = firebase.auth()

app = Flask(__name__)

def login(email, password):
    user = auth.sign_in_with_email_and_password(email, password)
    return user["idToken"]

@app.route('/status', methods=['GET'])
def status():
    return "Up and running"

@app.route('/user', methods=['POST'])
def create_user():
    content = request.get_json()

    # TODO: Add email validation
    auth.create_user_with_email_and_password(content["email"], content["password"])
    res = dao.create_user(content)

    if res == "error":
        return "User already exists", 406

    return login(content["email"], content["password"]), 200

@app.route('/user/auth/init', methods=['POST'])
def auth_user():
    print("Yes")
    content = request.get_json()
    email = content["email"]
    password = content["password"]
    return login(email, password)

@app.route('/user/cart-request', methods=['POST'])
def new_cart_request():
    request_data = request.get_json()
    user_id = request.get_json()["user_id"]
    cart_request = request.get_json()["cart_request"]

    user = db.child("user").get(token=user_id)

    if user == None:
        return "User does not exist", 401
    db.child("cart_request").child(str(uuid.uuid4())).push(cart_request)

@app.route('/user/cart-request/<email>', methods=['GET'])
def get_cart_requests(email):
    user_id = request.headers.get("Authentication")
    user = db.child("user").order_by_child("email").equal_to(email).get()
    print(user.val())

    if user is None:
        return "User does not exist", 401
    # elif email != user["email"]:
   #     return "User not authenticated", 401
    elif user["role"] == "Employee" or user["role"] == "admin":
        return db.child("cart_request").child().get()
    else:
        return "User does not have access", 403

@app.route('/user/cart-request', methods=['DELETE'])
def remove_cart_requests():
    user_id = request.get_json()["user_id"]
    user = db.child("user").get(token=user_id)

    if user == None:
        return "User does not exist", 401
    elif user["role"] == "Employee" or user["role"] == "admin":
        db.child("cart_request").child(request_id).remove()
    else:
        return "User does not have access", 403

if __name__ == '__main__':
    app.run(debug=True)
