const config = {
  apiKey: "AIzaSyCJx7wxQxatZvbv34uiJRXvtSJuBn1jIiI",
  authDomain: "cart-assist-8dc13.firebaseapp.com",
  databaseURL: "https://cart-assist-8dc13-default-rtdb.firebaseio.com/",
  storageBucket: "cart-assist-8dc13.appspot.com",
};

firebase.initializeApp(config);

var database = firebase.database();
var auth = firebase.auth();
var userId = "";
var userAuth = true;

auth.onAuthStateChanged((user) => {
  if (user) {
    userAuth = true;
  } else {
    userAuth = false;
  }
});

function login() {
  let email = document.getElementById("email").value;
  let password = document.getElementById("password").value;
  auth.signInWithEmailAndPassword(email, password).then(
    function (user) {
      sessionStorage.setItem("uid", user.uid);
      window.location = "/";
    },
    function (e) {
      console.error(e.message);
    }
  );
}

function stores() {
  console.log("here");
  if (userAuth) {
    console.log("is auth");
    let storeRef = firebase.database().ref("Stores");
    storeRef.on("value", (res) => {
      let data = res.val();
      console.log(data);
      populate_table(data);
    });
  } else {
    window.location = "/login";
  }
}

function add_row(table, label, value) {
  let row1 = table.insertRow();
  let label_elem = row1.insertCell(0);
  label_elem.innerHTML = label;
  let elem = row1.insertCell(1);
  elem.innerHTML = value;
}

function populate_table(data) {
  const table = document.getElementById("table");
  for (let i = 1; i < data.length; i++) {
    console.log(data[i]);
    add_row(table, "Store Number: ", i);
    add_row(table, "Total Carts: ", data[i].total_carts);
    add_row(table, "Available Carts: ", data[i].available_carts);
    add_row(table, "Empty Carts: ", data[i].empty_carts);
    add_row(table, " ", " ");
  }
}

function edit_store() {
  sn = document.getElementById("store_num").value;
  tc = document.getElementById("total_carts").value;
  console.log("hello there");
  const ref = firebase.database().ref("Stores");

  if (userAuth) {
    firebase
      .database()
      .ref("Stores")
      .once("value")
      .then((res) => {
        if (res.exists()) {
          let store_data = res.val()[sn];
          store_data["total_carts"] = tc;

          let updates = {};
          updates["/Stores/" + sn] = store_data;
          return database.ref().update(updates);
          window.location = "/";
        } else {
          console.log("No data found");
        }
      })
      .catch((error) => {
        console.error(error);
      });
  }
}

function employeeWindow() {
  window.location = "/add_employee";
}

function add_employee() {
  fn = document.getElementById("fname").value;
  ln = document.getElementById("lname").value;
  em = document.getElementById("email").value;
  pwd = document.getElementById("password").value;
  store = document.getElementById("store").value;
  access = document.getElementById("access").checked;

  if (fn == undefined || ln == undefined || em == undefined || pwd == undefined) {
    console.log("Required fields are empty");
    return;
  }

  let postData = {
    firstName: fn,
    lastName: ln,
    email: em,
    store: store,
    access: access,
    isEmployee: true,
  };

  if (userAuth) {
    auth
      .createUserWithEmailAndPassword(em, pwd)
      .then((res) => {
        let updates = {};
        updates["/Users/" + res.uid] = postData;
        database.ref().update(updates);
        window.location = "/access";
      })
      .catch((error) => {
        console.error("Unable to add employee");
      });
  } else {
  }
}

function employee_table(data, sn) {
  const table = document.getElementById("employee-table");
  let keys = Object.keys(data);
  keys.forEach((uid) => {
    let user = data[uid];
    if (user.isEmployee && user.store == sn) {
      add_row(table, "Name: ", user.firstName + " " + user.lastName);
      add_row(table, "Email: ", user.email);
      add_row(table, "Store Number: ", user.store);
      add_row(table, " ", " ");
    }
  });
}

function getEmployees() {
  sn = document.getElementById("store-num").value;

  if (userAuth) {
    let empRef = firebase.database().ref("Users");
    empRef.on("value", (res) => {
      let data = res.val();
      employee_table(data, sn);
    });
  } else {
    window.location = "/login";
  }
}

function add_store_window() {
  window.location = "/add_store";
}

function add_store() {
  sn = document.getElementById("snum").value;
  tc = document.getElementById("tcarts").value;

  if (sn == undefined || tc == undefined) {
    console.log("Required fields are empty");
    return;
  }

  let postData = {
    available_carts: tc,
    empty_carts: tc,
    total_carts: tc,
  };

  if (userAuth) {
    let updates = {};
    updates["/Stores/" + sn] = postData;
    database.ref().update(updates);
    window.location = "/";
  } else {
  }
}
