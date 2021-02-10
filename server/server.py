from os import path, system
from flask import Flask, jsonify, request, redirect, url_for
import pyrebase
import server_init

firebase_config = server_init.get_firebase_config()
firebase = pyrebase.initialize_app(firebase_config)
db = firebase.database()
server_init.setup_db(db)

app = Flask(__name__)

@app.route('/user', methods=['POST'])
def create_user(user_data):
    content = request.json
    # firebase.database()

@app.route('/user/auth/init', methods=['POST'])
def auth_user(auth_data):
    content = request.json
    # firebase.auth()

@app.route('/user/auth/expire', methods=['POST'])
def auth_user(auth_data):
    content = request.json
    # firebase.auth()

if __name__ == '__main__':
    app.run(debug=True)
