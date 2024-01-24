// const mysql = require('mysql');

// const db = mysql.createPool({
//     host : 'localhost:3000',
//     user : 'root',
//     password : 'mysql',
//     database : 'foodtest'
// });

const h2 = require('h2');
const db = h2.createPool({
    host : 'localhost:3000',
    user : 'sa',
    password : '',
    database : 'foodtest'
});

module.exports = db;