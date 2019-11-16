// Doc:
//  - https://firebase.google.com/docs/auth/web/firebaseui
//  - https://github.com/firebase/firebaseui-web/blob/master/README.md

const firebaseConfig = {
  apiKey: "AIzaSyC9EypFREN7T1ieNuUI8AzA8BFD7pFpHgU",
  authDomain: "zeklin-4394e.firebaseapp.com",
  databaseURL: "https://zeklin-4394e.firebaseio.com",
  projectId: "zeklin-4394e",
  storageBucket: "zeklin-4394e.appspot.com",
  messagingSenderId: "1001347539775",
  appId: "1:1001347539775:web:9c5dd95eda3fd91a0f698a",
  measurementId: "G-9CFG7D8MBW"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();

// -------------------
// -- Firebase Auth --
// -------------------
const uiConfig = {
  callbacks: {
    signInSuccessWithAuthResult: function(authResult) {
      // TODO Jules
      // User successfully signed in.
      // Return type determines whether we continue the redirect automatically
      // or whether we leave that to developer to handle.
      return true;
    },
  },
  // Will use popup for IDP Providers sign-in flow instead of the default, redirect.
  signInFlow: 'popup',
  signInSuccessUrl: 'http://localhost:8080',
  signInOptions: [firebase.auth.GithubAuthProvider.PROVIDER_ID],
  // Terms of service url.
  tosUrl: 'http://localhost:8080/terms',
  // Privacy policy url.
  privacyPolicyUrl: 'http://localhost:8080/privacy'
};

const ui = new firebaseui.auth.AuthUI(firebase.auth());
ui.start('#firebaseui-auth-container', uiConfig);