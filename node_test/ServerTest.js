var http = require('http');
var url = require('url');
var querystring = require('querystring'); 


const mysql = require('mysql');  // mysql 모듈 로드

const conn = {  // mysql 접속 설정
    host: 
    port: 
    user: 
    password: 
    database:
};

var connection = mysql.createConnection(conn); // DB 커넥션 생성
//connection.connect();   // DB 접속

var server = http.createServer(function(request,response){
    console.log('--- log start ---');
    var parsedUrl = url.parse(request.url);

    console.log(parsedUrl);
    var parsedQuery = querystring.parse(parsedUrl.query,'/','=');
    

    console.log(parsedQuery);
    console.log('--- log end ---');

    var Query = parsedUrl.path;
    console.log(Query)
    var QueryLen = Query.length;
    var ParsedNumber = ""
    for(var i = 1; i < QueryLen; i++) ParsedNumber += Query[i]
    console.log('받아온 숫자 : ' + ParsedNumber)
    response.writeHead(200, {'Content-Type':'html/text'});
    testQuery = "SELECT * FROM test WHERE testnum = " + ParsedNumber;

    var result;
    connection.query(testQuery, function (err, results, fields) { // testQuery 실행
        if (err) {
            console.log(err);
        }
        result = JSON.stringify(results[0])
        response.end(result)
        console.log("쿼리문 결과1 : " + result)
    });

});

server.listen(8080, function(){
    console.log('Server is running...');
});

