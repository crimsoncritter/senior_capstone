import json
from pathlib import Path

# Get the firebase config for initialization
def get_firebase_config():
    file = Path("./config/firebase_config.json")
    if not file.is_file():
       print("Setup your firebase config in \"./config/firebase_config.json\" ")
       return

    with open("./config/firebase_config.json") as f:
       return json.load(f)

# Initialize the root objects of the databse if they don't already exist
def setup_db(db):
    db.child("customer")
    db.child("employee")
    db.child("car")
    db.child("session")
    db.child("store")
    db.child("cart_request")
