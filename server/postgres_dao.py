import psycopg2
import json

conn = psycopg2.connect(host="localhost", port = 5432, database="cart_assist", user="postgres", password = "postgres")
cursor = conn.cursor()

def create_user(content):
    cursor.execute("select count(id) from Customer where email = \'{0}\';".format(content["email"]))
    rows = cursor.fetchall()
    customer = rows[0][0]
    cursor.execute("select count(id) from Employee where email = \'{0}\';".format(content["email"]))
    rows = cursor.fetchall()
    employee = rows[0][0]

    if customer != 0 or employee != 0:
        return "error"

    sql = ""
    if content["role"].lower() == "customer":
        sql = "INSERT INTO Customer(first_name, last_name, email) VALUES(\'{0}\', \'{1}\', \'{2}\');".format(
            content["first_name"], content["last_name"], content["email"])
    else:
        sql = "INSERT INTO Employee(first_name, last_name, email, store_id, working) VALUES(\'{0}\', \'{1}\', \'{2}\', \'{3}\', False);".format(
            content["first_name"], content["last_name"], content["email"], content["store_id"])
    cursor.execute(sql)
    conn.commit()

    return "success"

def is_employee(email):
    rows = cursor.execute("select count(id) from Customer where email = \'{0}\';".format(email))
    print(rows)
    return rows is None or rows[0][0] > 0

def cart_request(store_id, email):
    sql = "INSERT INTO CartRequest(user_email, store_id, car_id, request_time) VALUES(\'{0}\', \'{1}\', 0, now());".format(
            email, store_id)
    cursor.execute(sql)
    conn.commit()

    return "success"

def get_cart_requests(store_id):
    rows = cursor.execute("select user_email, car_id from CartRequest where store_id = {0}::int;".format(store_id))

    sql = "select user_email, car_id from CartRequest where store_id = {0}::int;".format(store_id)
    print(sql)
    rows = cursor.execute("select * from CartRequest;")


    print("ROWWWWWWS")
    print(rows)

    return json.dumps(rows)

