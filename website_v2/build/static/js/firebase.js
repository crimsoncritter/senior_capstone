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
  if (userAuth) {
    let storeRef = firebase.database().ref("Stores");
    storeRef.on("value", (res) => {
      let data = res.val();
      populate_table(data);
    });
  } else {
    window.location = "/login";
  }
}

function add_row(table, sn, tc, ac, ec) {
  let row1 = table.insertRow();

  let sn_elem = row1.insertCell(0);
  sn_elem.innerHTML = sn;

  let tc_elem = row1.insertCell(1);
  tc_elem.innerHTML = tc;

  let ac_elem = row1.insertCell(2);
  ac_elem.innerHTML = ac;

  let ec_elem = row1.insertCell(3);
  ec_elem.innerHTML = ec;
}

function populate_table(data) {
  const table = document.getElementById("store_table");
  for (let i = 1; i < data.length; i++) {
    add_row(table, i, data[i].total_carts, data[i].available_carts, data[i].empty_carts);
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

function add_employee_row(table, name, email, sn) {
  let row1 = table.insertRow();

  let n_elem = row1.insertCell(0);
  n_elem.innerHTML = name;

  let e_elem = row1.insertCell(1);
  e_elem.innerHTML = email;

  let sn_elem = row1.insertCell(2);
  sn_elem.innerHTML = sn;
}

function employee_table(data, sn) {
  const table = document.getElementById("employee-table");
  let keys = Object.keys(data);
  keys.forEach((uid) => {
    let user = data[uid];
    if (user.isEmployee && user.store == sn) {
      un = user.firstName + " " + user.lastName;
      add_employee_row(table, un, user.email, user.store);
    }
  });
  const card = document.getElementById("employee-card");
  const icard = document.getElementById("init-card");
  card.style.display = "block";
  icard.style.display = "none";
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
