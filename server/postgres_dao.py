import psycopg2

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
        sql = "INSERT INTO Employee(first_name, last_name, email, store_id, working) VALUES(\"{0}\", \"{1}\", \"{2}\", \"{3}\");".format(
            content["first_name"], content["last_name"], content["email"], content["store_id"], False)
    print(sql)
    cursor.execute(sql)
    conn.commit()

    return "sucess"
