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

function is_auth() {
  const uid = sessionStorage.getItem("uid");
  return firebase.auth().currentUser.uid == uid;
}

function login() {
  let email = document.getElementById("email").value;
  let password = document.getElementById("password").value;
  auth.signInWithEmailAndPassword(email, password).then(
    function (user) {
      sessionStorage.setItem("uid", user.uid);
      window.location = "./index.html";
    },
    function (e) {
      console.error(e.message);
    }
  );
}

function stores() {
  if (is_auth) {
    let storeRef = firebase.database().ref("Stores");
    storeRef.on("value", (res) => {
      let data = res.val();
      console.log(data);
    });
  } else {
    window.location = "./login.html";
  }
}
