// const firebaseConfig = {
//     apiKey: "AIzaSyAleKIv1rONAaYJUvFPMZgGmAVvzFRgVsM",
//     authDomain: "xcape-hint-app.firebaseapp.com",
//     databaseURL: "https://xcape-hint-app-default-rtdb.asia-southeast1.firebasedatabase.app",
//     projectId: "xcape-hint-app",
//     storageBucket: "xcape-hint-app.appspot.com",
//     messagingSenderId: "1063603328752",
//     appId: "1:1063603328752:web:b1bfe3f8065a83bb769435",
//     measurementId: "G-ZFF1CFH0J6"
// };

const firebaseConfig = {
    apiKey: "AIzaSyCV-6PL4K8619M6MSaIwlSY2hJpVlGPQh8",
    authDomain: "xcape-test.firebaseapp.com",
    databaseURL: "https://xcape-test-default-rtdb.firebaseio.com",
    projectId: "xcape-test",
    storageBucket: "xcape-test.appspot.com",
    messagingSenderId: "23621818914",
    appId: "1:23621818914:web:e7b25cb8a7d2deab59087b",
    measurementId: "G-NR4N1HXN5P"
};

firebase.initializeApp(firebaseConfig);

const merchantList = firebase.database().ref('xcape/merchantList/').value;

const sample = firebase.database().ref('xcape/merchantList/0/themeList/0/recentStartTime');
sample.on('value', snapshot => {
    console.log(snapshot.val());
});

// const firebaseFocus = {
//     xcape: 'xcape',
//     merchantList: 'xcape/merchantList',
//     themeList: (merchantId) => {
//         return `xcape/merchantList/`
//     }
// }