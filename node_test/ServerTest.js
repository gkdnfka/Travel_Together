var http = require('http');
var url = require('url');
var querystring = require('querystring'); 


const mysql = require('mysql');  // mysql 모듈 로드

const conn = {  // mysql 접속 설정
    host: '',
    port: '',
    user: '',
    password: '',
    database: ''
};

var connection = mysql.createConnection(conn); // DB 커넥션 생성


var server = http.createServer(function(request,response){
    var tmp_parsed = decodeURI(request.url);
    var parsedUrl = url.parse(tmp_parsed);
    var parsedQuery = querystring.parse(parsedUrl.path,'/', '=');
    
    console.log(parsedQuery)
    console.log('첫번째 문자 : ' + parsedQuery["func"])
    response.writeHead(200, {'Content-Type':'html/text'});


    if(parsedQuery["func"] == "SearchPlace"){ // 장소를 검색하기 위한 쿼리 파싱
        var testQuery = ""
        if(parsedQuery["type"] == "ByName") testQuery = "SELECT * FROM test WHERE testname = '%" + parsedQuery["str"] + "%'";
        else testQuery = "SELECT * FROM test WHERE testaddr like '%" + parsedQuery["str"] + "%'";

        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(results)
            result = JSON.stringify(results)
            response.end(result)
            console.log("쿼리문 결과1 : " + result)
        });
    }

    if(parsedQuery["func"] == "SearchPost"){ // 게시글을 불러오기 위한 쿼리 파싱
        var testQuery = ""
        if(parsedQuery["type"] == "default") testQuery = "SELECT * FROM NOTICEBOARD_DB";
        else testQuery = "SELECT * FROM test WHERE testaddr like '%" + parsedQuery["str"] + "%'";

        var result;
        connection.query(testQuery, function (err, results, fields) { // testQuery 실행
            if (err) {
                console.log(err);
            }
            console.log(results)
            result = JSON.stringify(results)
            response.end(result)
            console.log("쿼리문 결과 : " + result)
        });
    }

});

server.listen(8080, function(){
    console.log('Server is running...');
});

